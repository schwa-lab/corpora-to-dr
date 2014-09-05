package ontonotes5.to_uima;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import ontonotes5.to_uima.types.CorefChain;
import ontonotes5.to_uima.types.CorefMention;
import ontonotes5.to_uima.types.Document;
import ontonotes5.to_uima.types.NamedEntity;
import ontonotes5.to_uima.types.ParseNode;
import ontonotes5.to_uima.types.PropArg;
import ontonotes5.to_uima.types.PropArgGroup;
import ontonotes5.to_uima.types.PropArgPart;
import ontonotes5.to_uima.types.PropLink;
import ontonotes5.to_uima.types.PropLinkGroup;
import ontonotes5.to_uima.types.PropLinkPart;
import ontonotes5.to_uima.types.PropPredPart;
import ontonotes5.to_uima.types.Proposition;
import ontonotes5.to_uima.types.Sentence;
import ontonotes5.to_uima.types.Speaker;
import ontonotes5.to_uima.types.Token;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


public final class ToUIMAConverter {
  private final Map<OParseNode, ParseNode> oNodeToNode = new HashMap<OParseNode, ParseNode>();
  private final Map<OParseNode, Token> oNodeToToken = new HashMap<OParseNode, Token>();
  private final Map<OProposition, Proposition> oPropToProp = new HashMap<OProposition, Proposition>();
  private final List<Sentence> sentences = new ArrayList<Sentence>();
  private final Map<Integer, List<Proposition>> sentenceIndexToPropositions = new HashMap<Integer, List<Proposition>>();
  private final Map<String, Speaker> speakersByName = new HashMap<String, Speaker>();
  private int tokenBegin;
  private int tokenEnd;
  private JCas jcas;

  public ToUIMAConverter() {
    reset();
  }

  public void reset() {
    oNodeToNode.clear();
    oNodeToToken.clear();
    oPropToProp.clear();
    sentences.clear();
    sentenceIndexToPropositions.clear();
    speakersByName.clear();
    tokenBegin = tokenEnd = 0;
    jcas = null;
  }

  public void convert(final ODocument oDoc, final JCas jcas) throws AnalysisEngineProcessException {
    this.jcas = jcas;

    // Set the document-level properties.
    final Document doc = getDocAnnotation(jcas);
    doc.setSubcorpusId(oDoc.subcorpusId);
    doc.setLanguage(oDoc.langId);
    doc.setGenre(oDoc.genre);
    doc.setSource(oDoc.source);
    doc.addToIndexes();

    // Construct the speakers.
    for (final OSpeaker oSpeaker : oDoc.speakers.values()) {
      Speaker speaker = new Speaker(jcas);
      speaker.setName(oSpeaker.name);
      speaker.setGender(oSpeaker.gender);
      speaker.setCompetence(oSpeaker.competence);
      speaker.addToIndexes();
      speakersByName.put(speaker.getName(), speaker);
    }

    // Convert the sentences and all of their dependent objects.
    for (final OSentence oSentence : oDoc.sentences)
      convertOSentence(oSentence);

    // Construct the coref chains and mentions.
    for (final OCorefChain oChain : oDoc.corefChains)
      convertOCorefChain(oChain);

    // Construct the propositions.
    for (final OProposition oProposition : oDoc.propositions)
      convertOProposition(oProposition);

    // Place the propositions back on the sentences, and then save the sentences to the CAS.
    for (int i = 0; i != sentences.size(); i++) {
      final Sentence sentence = sentences.get(i);
      if (sentenceIndexToPropositions.containsKey(i))
        sentence.setPropositions(createFSArray(jcas, sentenceIndexToPropositions.get(i)));
      sentence.addToIndexes();
    }
  }

  private void convertOSentence(final OSentence oSentence) throws AnalysisEngineProcessException {
    final List<Token> tokens = new ArrayList<Token>(oSentence.tokens.length);
    int sentenceBegin = tokenBegin;

    // Construct the tokens.
    for (final String oToken : oSentence.tokens) {
      final Token token = new Token(jcas);
      token.setBegin(tokenBegin);
      tokenEnd = tokenBegin + oToken.length();
      token.setEnd(tokenEnd);
      token.addToIndexes();
      tokenBegin = tokenEnd + 1;

      tokens.add(token);
    }
    int sentenceEnd = tokenEnd;

    // Construct the sentence.
    final Sentence sentence = new Sentence(jcas);
    this.sentences.add(sentence);
    sentence.setBegin(sentenceBegin);
    sentence.setEnd(sentenceEnd);
    if (oSentence.startTime != null || oSentence.endTime != null) {
      sentence.setStartTime(oSentence.startTime);
      sentence.setEndTime(oSentence.endTime);
      if (oSentence.speakerNames.length != 0) {
        Speaker[] speakers = new Speaker[oSentence.speakerNames.length];
        for (int i = 0; i != speakers.length; i++)
          speakers[i] = speakersByName.get(oSentence.speakerNames[i]);
        sentence.setSpeakers(createFSArray(jcas, speakers));
      }
    }

    // Construct the parse nodes.
    final Map<Token, ParseNode> tokenToNode = new HashMap<Token, ParseNode>(tokens.size());
    int nLeaves = 0;
    for (final OParseNode oNode : oSentence.parse.getAllNodes()) {
      final ParseNode node = new ParseNode(jcas);
      node.setTag(oNode.tag);
      node.setPos(oNode.partOfSpeech);
      node.setPhraseType(oNode.phraseType);
      node.setFunctionTags(oNode.functionTags);
      node.setCorefSection(oNode.corefSection);
      if (oNode.isLeaf()) {
        final Token token = tokens.get(nLeaves++);
        node.setToken(token);
        tokenToNode.put(token, node);
        oNodeToToken.put(oNode, token);
      }

      oNodeToNode.put(oNode, node);
    }

    // Place the new root on the new sentence.
    sentence.setParse(oNodeToNode.get(oSentence.parse));

    // Go through the parse nodes, updating their syntactic links and children.
    for (final OParseNode oNode : oSentence.parse.getAllNodes()) {
      final ParseNode node = oNodeToNode.get(oNode);
      if (oNode.syntacticLink != null)
        node.setSyntacticLink(oNodeToNode.get(oNode.syntacticLink));
      ParseNode[] children = new ParseNode[oNode.children.length];
      for (int i = 0; i != children.length; i++)
        children[i] = oNodeToNode.get(oNode.children[i]);
      node.setChildren(createFSArray(jcas, children));
      node.addToIndexes();
    }

    // Create the named entities, skipping trace nodes in the word count offsets.
    final List<OParseNode> oLeaves = oSentence.parse.getOrderedLeaves();
    for (final ONamedEntity oNamedEntity : oSentence.namedEntities) {
      final NamedEntity namedEntity = new NamedEntity(jcas);
      namedEntity.setTag(oNamedEntity.tag);
      namedEntity.setStartOffset(oNamedEntity.startOffset);
      namedEntity.setEndOffset(oNamedEntity.endOffset);

      int oLeafUpto = 0;
      for (int i = 0; i != oNamedEntity.startIndex; i++) {
        while (oLeaves.get(oLeafUpto).isTrace())
          oLeafUpto++;
        oLeafUpto++;
      }
      namedEntity.setBegin(oNodeToToken.get(oLeaves.get(oLeafUpto)).getBegin());
      for (int i = 1; i != oNamedEntity.length; i++) {
        while (oLeaves.get(oLeafUpto).isTrace())
          oLeafUpto++;
        oLeafUpto++;
      }
      namedEntity.setEnd(oNodeToToken.get(oLeaves.get(oLeafUpto)).getEnd());

      namedEntity.addToIndexes();
    }
  }

  private void convertOCorefChain(final OCorefChain oChain) throws AnalysisEngineProcessException {
    final CorefChain chain = new CorefChain(jcas);
    chain.setId(oChain.identifier);
    chain.setSection(oChain.section);
    chain.setKind(oChain.type);
    if (oChain.speakerName != null)
      chain.setSpeaker(speakersByName.get(oChain.speakerName));

    List<CorefMention> mentions = new ArrayList<CorefMention>(oChain.mentions.size());
    for (final OCorefMention oMention : oChain.mentions) {
      final CorefMention mention = new CorefMention(jcas);
      mention.setBegin(oNodeToToken.get(oMention.startLeaf).getBegin());
      mention.setEnd(oNodeToToken.get(oMention.endLeaf).getEnd());
      mention.setKind(oMention.type);
      mention.setStartOffset(oMention.startOffset);
      mention.setEndOffset(oMention.endOffset);
      mention.addToIndexes();
    }
    chain.setMentions(createFSArray(jcas, mentions));

    chain.addToIndexes();
  }

  private void convertOProposition(final OProposition oProposition) throws AnalysisEngineProcessException {
    final Proposition proposition = new Proposition(jcas);
    proposition.setEncoded(oProposition.encodedProposition);
    proposition.setQuality(oProposition.quality);
    proposition.setKind(oProposition.type);
    proposition.setLemma(oProposition.lemma);
    proposition.setPbSenseNum(oProposition.pbSenseNum);
    proposition.setLeaf(oNodeToNode.get(oProposition.leaf));

    // Construct the predicate.
    final List<PropPredPart> predParts = new ArrayList<PropPredPart>(oProposition.predNodes.size());
    for (final OPropNode oPredPart : oProposition.predNodes) {
      final PropPredPart predPart = new PropPredPart(jcas);
      predPart.setEncoded(oPredPart.nodeId);
      predPart.setNode(oNodeToNode.get(oPredPart.node));
      predPart.addToIndexes();
    }
    proposition.setPredParts(createFSArray(jcas, predParts));

    // Construct the argument groups.
    final List<PropArgGroup> argGroups = new ArrayList<PropArgGroup>(oProposition.argGroups.size());
    for (final OPropArgGroup oArgGroup : oProposition.argGroups) {
      final PropArgGroup argGroup = new PropArgGroup(jcas);
      argGroup.setKind(oArgGroup.type);

      final List<PropArg> args = new ArrayList<PropArg>(oArgGroup.arguments.size());
      for (final List<OPropNode> oArg : oArgGroup.arguments) {
        final PropArg arg = new PropArg(jcas);

        final List<PropArgPart> argParts = new ArrayList<PropArgPart>(oArg.size());
        for (final OPropNode oArgPart : oArg) {
          final PropArgPart argPart = new PropArgPart(jcas);
          argPart.setEncoded(oArgPart.nodeId);
          argPart.setNode(oNodeToNode.get(oArgPart.node));
          argPart.addToIndexes();
          argParts.add(argPart);
        }
        arg.setParts(createFSArray(jcas, argParts));

        args.add(arg);
        arg.addToIndexes();
      }
      argGroup.setArgs(createFSArray(jcas, args));

      argGroup.addToIndexes();
      argGroups.add(argGroup);
    }
    proposition.setArgGroups(createFSArray(jcas, argGroups));

    // Construct the link groups.
    final List<PropLinkGroup> linkGroups = new ArrayList<PropLinkGroup>(oProposition.linkGroups.size());
    for (final OPropLinkGroup oLinkGroup : oProposition.linkGroups) {
      final PropLinkGroup linkGroup = new PropLinkGroup(jcas);
      linkGroup.setAssociatedArgumentId(oLinkGroup.associatedArgumentId);
      linkGroup.setKind(oLinkGroup.type);

      final List<PropLink> links = new ArrayList<PropLink>(oLinkGroup.links.size());
      for (final List<OPropNode> oLink : oLinkGroup.links) {
        final PropLink link = new PropLink(jcas);

        final List<PropLinkPart> linkParts = new ArrayList<PropLinkPart>(oLink.size());
        for (final OPropNode oLinkPart : oLink) {
          final PropLinkPart linkPart = new PropLinkPart(jcas);
          linkPart.setEncoded(oLinkPart.nodeId);
          linkPart.setNode(oNodeToNode.get(oLinkPart.node));
          linkPart.addToIndexes();
          linkParts.add(linkPart);
        }
        link.setParts(createFSArray(jcas, linkParts));

        links.add(link);
        link.addToIndexes();
      }
      linkGroup.setLinks(createFSArray(jcas, links));

      linkGroup.addToIndexes();
      linkGroups.add(linkGroup);
    }
    proposition.setLinkGroups(createFSArray(jcas, linkGroups));

    proposition.addToIndexes();

    oPropToProp.put(oProposition, proposition);
    if (!sentenceIndexToPropositions.containsKey(oProposition.sentenceIndex))
      sentenceIndexToPropositions.put(oProposition.sentenceIndex, new LinkedList<Proposition>());
    sentenceIndexToPropositions.get(oProposition.sentenceIndex).add(proposition);
  }

  private static Document getDocAnnotation(final JCas jcas) throws AnalysisEngineProcessException {
    // Obtain the singleton document information annotation.
    final FSIterator<Annotation> sources = jcas.getAnnotationIndex(Document.type).iterator();
    if (!sources.hasNext())
      throw new AnalysisEngineProcessException("No DocumentInformation instances found", null);
    return (Document) sources.next();
  }

  public static <T extends FeatureStructure> FSArray createFSArray(final JCas jcas, final Collection<T> collection) {
    final FSArray array = new FSArray(jcas, collection.size());
    int i = 0;
    for (final T obj : collection) {
      array.set(i, obj);
      i++;
    }
    return array;
  }

  public static <T extends FeatureStructure> FSArray createFSArray(final JCas jcas, final T[] collection) {
    final FSArray array = new FSArray(jcas, collection.length);
    int i = 0;
    for (final T obj : collection) {
      array.set(i, obj);
      i++;
    }
    return array;
  }
}
