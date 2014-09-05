package ontonotes5.to_dr;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.schwa.dr.ByteSlice;
import org.schwa.dr.Slice;

import ontonotes5.models.OCorefChain;
import ontonotes5.models.OCorefMention;
import ontonotes5.models.ODocument;
import ontonotes5.models.ONamedEntity;
import ontonotes5.models.OParseNode;
import ontonotes5.models.OPropArgGroup;
import ontonotes5.models.OPropLinkGroup;
import ontonotes5.models.OPropNode;
import ontonotes5.models.OProposition;
import ontonotes5.models.OSentence;
import ontonotes5.models.OSpeaker;
import ontonotes5.to_dr.models.CorefChain;
import ontonotes5.to_dr.models.CorefMention;
import ontonotes5.to_dr.models.Doc;
import ontonotes5.to_dr.models.NamedEntity;
import ontonotes5.to_dr.models.ParseNode;
import ontonotes5.to_dr.models.PropArg;
import ontonotes5.to_dr.models.PropArgGroup;
import ontonotes5.to_dr.models.PropArgPart;
import ontonotes5.to_dr.models.PropLink;
import ontonotes5.to_dr.models.PropLinkGroup;
import ontonotes5.to_dr.models.PropLinkPart;
import ontonotes5.to_dr.models.PropPredPart;
import ontonotes5.to_dr.models.Proposition;
import ontonotes5.to_dr.models.Sentence;
import ontonotes5.to_dr.models.Speaker;
import ontonotes5.to_dr.models.Token;


public final class ToDocrepConverter {
  private Doc doc;
  private final Map<OParseNode, Integer> oNodeToIndex = new HashMap<OParseNode, Integer>();
  private final Map<OParseNode, Token> oNodeToToken = new HashMap<OParseNode, Token>();
  private final Map<String, Speaker> speakersByName = new HashMap<String, Speaker>();
  private long tokenByteOffsetStart;

  public ToDocrepConverter() {
    reset();
  }

  public void reset() {
    doc = null;
    oNodeToIndex.clear();
    oNodeToToken.clear();
    speakersByName.clear();
  }

  public Doc convert(final ODocument oDoc) throws AnalysisEngineProcessException {
    // Set the document-level properties.
    doc = new Doc();
    doc.docId = oDoc.id;
    doc.subcorpusId = oDoc.subcorpusId;
    doc.lang = oDoc.langId;
    doc.genre = oDoc.genre;
    doc.source = oDoc.source;

    // Construct the speakers.
    for (final OSpeaker oSpeaker : oDoc.speakers.values()) {
      Speaker speaker = doc.speakers.create(Speaker.class);
      speaker.name = oSpeaker.name;
      speaker.gender = oSpeaker.gender;
      speaker.competence = oSpeaker.competence;
      speakersByName.put(speaker.name, speaker);
    }

    // Convert the sentences and all of their dependent objects.
    try {
      tokenByteOffsetStart = 0;
      for (final OSentence oSentence : oDoc.sentences)
        convertOSentence(oSentence);
    }
    catch (UnsupportedEncodingException e) {
      throw new AnalysisEngineProcessException(e);
    }

    // Construct the coref chains and mentions.
    for (final OCorefChain oChain : oDoc.corefChains)
      convertOCorefChain(oChain);

    // Construct the propositions.
    for (final OProposition oProposition : oDoc.propositions)
      convertOProposition(oProposition);

    return doc;
  }

  private void convertOSentence(final OSentence oSentence) throws AnalysisEngineProcessException, UnsupportedEncodingException {
    // Create the tokens.
    final List<Token> tokens = new ArrayList<Token>(oSentence.tokens.length);
    final int nTokensBefore = doc.tokens.size();
    for (final String oToken : oSentence.tokens) {
      final byte[] encoded = oToken.getBytes("UTF-8");
      final Token token = doc.tokens.create(Token.class);
      token.span = new ByteSlice(tokenByteOffsetStart, tokenByteOffsetStart + encoded.length);
      token.raw = oToken;
      tokens.add(token);
      tokenByteOffsetStart += encoded.length + 1;
    }

    // Create the sentence.
    final Sentence sent = doc.sentences.create(Sentence.class);
    sent.span = new Slice<Token>(tokens.get(0), tokens.get(tokens.size() - 1));
    if (oSentence.startTime != null || oSentence.endTime != null) {
      sent.startTime = oSentence.startTime;
      sent.endTime = oSentence.endTime;
      for (final String speakerName : oSentence.speakerNames)
        sent.speakers.add(speakersByName.get(speakerName));
    }

    // Create the parse nodes.
    final Map<Token, ParseNode> tokenToParse = new HashMap<Token, ParseNode>();
    int tokenUpto = 0;
    for (final OParseNode oNode : oSentence.parse.getAllNodes()) {
      oNodeToIndex.put(oNode, doc.parseNodes.size());
      final ParseNode node = doc.parseNodes.create(ParseNode.class);
      node.tag = oNode.tag;
      node.pos = oNode.partOfSpeech;
      node.pharseType = oNode.phraseType;
      node.functionTags = oNode.functionTags;
      node.corefSection = oNode.corefSection;

      if (oNode.isLeaf()) {
        node.token = doc.tokens.get(nTokensBefore + tokenUpto);
        tokenToParse.put(node.token, node);
        oNodeToToken.put(oNode, node.token);
        tokenUpto++;
      }
    }

    // Place the new root parse node on the sentence.
    sent.parse = doc.parseNodes.get(oNodeToIndex.get(oSentence.parse));

    // Go through the nodes again, updating their syntactic link and children pointers.
    for (final OParseNode oNode : oSentence.parse.getAllNodes()) {
      final ParseNode node = doc.parseNodes.get(oNodeToIndex.get(oNode));
      if (oNode.syntacticLink != null)
        node.syntacticLink = doc.parseNodes.get(oNodeToIndex.get(oNode.syntacticLink));
      for (final OParseNode oChild : oNode.children)
        node.children.add(doc.parseNodes.get(oNodeToIndex.get(oChild)));
    }

    // Create the named entities, skipping trace nodes in the word count offsets.
    final List<OParseNode> oLeaves = oSentence.parse.getOrderedLeaves();
    for (final ONamedEntity oNamedEntity : oSentence.namedEntities) {
      int oLeafUpto = 0;
      for (int i = 0; i != oNamedEntity.startIndex; i++) {
        while (oLeaves.get(oLeafUpto).isTrace())
          oLeafUpto++;
        oLeafUpto++;
      }
      final Token startLeaf = oNodeToToken.get(oLeaves.get(oLeafUpto));
      for (int i = 1; i != oNamedEntity.length; i++) {
        while (oLeaves.get(oLeafUpto).isTrace())
          oLeafUpto++;
        oLeafUpto++;
      }
      final Token endLeaf = oNodeToToken.get(oLeaves.get(oLeafUpto));

      final NamedEntity namedEntity = doc.namedEntities.create(NamedEntity.class);
      namedEntity.span = new Slice<Token>(startLeaf, endLeaf);
      namedEntity.tag = oNamedEntity.tag;
      namedEntity.startOffset = oNamedEntity.startOffset;
      namedEntity.endOffset = oNamedEntity.endOffset;
    }
  }

  private void convertOCorefChain(final OCorefChain oChain) throws AnalysisEngineProcessException {
    final CorefChain chain = doc.corefChains.create(CorefChain.class);
    chain.id = oChain.identifier;
    chain.section = oChain.section;
    chain.type = oChain.type;
    if (oChain.speakerName != null)
      chain.speaker = speakersByName.get(oChain.speakerName);

    for (final OCorefMention oMention : oChain.mentions) {
      final CorefMention mention = doc.corefMentions.create(CorefMention.class);
      mention.span = new Slice<Token>(oNodeToToken.get(oMention.startLeaf), oNodeToToken.get(oMention.endLeaf));
      mention.type = oMention.type;
      mention.startOffset = oMention.startOffset;
      mention.endOffset = oMention.endOffset;
      chain.mentions.add(mention);
    }
  }

  private void convertOProposition(final OProposition oProposition) throws AnalysisEngineProcessException {
    // Construct the proposition.
    final Proposition proposition = doc.propositions.create(Proposition.class);
    proposition.encoded = oProposition.encodedProposition;
    proposition.quality = oProposition.quality;
    proposition.type = oProposition.type;
    proposition.lemma = oProposition.lemma;
    proposition.pbSenseNum = oProposition.pbSenseNum;
    proposition.leaf = doc.parseNodes.get(oNodeToIndex.get(oProposition.leaf));

    // Construct the predicate.
    for (final OPropNode oPredNode : oProposition.predNodes) {
      final PropPredPart part = doc.propPredParts.create(PropPredPart.class);
      part.encoded = oPredNode.nodeId;
      part.node = doc.parseNodes.get(oNodeToIndex.get(oPredNode.node));
      proposition.predParts.add(part);
    }

    // Construct the arguments.
    for (final OPropArgGroup oArgGroup : oProposition.argGroups) {
      final PropArgGroup argGroup = doc.propArgGroups.create(PropArgGroup.class);
      argGroup.type = oArgGroup.type;
      proposition.argGroups.add(argGroup);

      for (final List<OPropNode> oArgs : oArgGroup.arguments) {
        final PropArg arg = doc.propArgs.create(PropArg.class);
        argGroup.args.add(arg);

        for (final OPropNode oArgPart : oArgs) {
          final PropArgPart part = doc.propArgParts.create(PropArgPart.class);
          part.encoded = oArgPart.nodeId;
          part.node = doc.parseNodes.get(oNodeToIndex.get(oArgPart.node));
          arg.parts.add(part);
        }
      }
    }

    // Construct the links.
    for (final OPropLinkGroup oLinkGroup : oProposition.linkGroups) {
      final PropLinkGroup linkGroup = doc.propLinkGroups.create(PropLinkGroup.class);
      linkGroup.type = oLinkGroup.type;
      linkGroup.associatedArgumentId = oLinkGroup.associatedArgumentId;
      proposition.linkGroups.add(linkGroup);

      for (final List<OPropNode> oLinks : oLinkGroup.links) {
        final PropLink link = doc.propLinks.create(PropLink.class);
        linkGroup.links.add(link);

        for (final OPropNode oLinkPart : oLinks) {
          final PropLinkPart part = doc.propLinkParts.create(PropLinkPart.class);
          part.encoded = oLinkPart.nodeId;
          part.node = doc.parseNodes.get(oNodeToIndex.get(oLinkPart.node));
          link.parts.add(part);
        }
      }
    }

    // Place the proposition on the sentence.
    doc.sentences.get(oProposition.sentenceIndex).propositions.add(proposition);
  }
}
