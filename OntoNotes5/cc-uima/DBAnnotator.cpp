#include <fstream>
#include <map>
#include <sstream>
#include <vector>

#include "DBAnnotator.h"
#include "models-db.h"
#include "times.h"

#include <uima/api.hpp>
#include <uima/internal_casserializer.hpp>
#include <uima/xmiwriter.hpp>
#include <uima/xmlwriter.hpp>


namespace ontonotes5 {
namespace to_uima {

DBAnnotator::DBAnnotator(void) {
  std::clog << "DBAnnotator::DBAnnotator()" << std::endl;
}

DBAnnotator::~DBAnnotator(void) {
  std::clog << "DBAnnotator::~DBAnnotator()" << std::endl;
}


uima::TyErrorId
DBAnnotator::initialize(uima::AnnotatorContext &) {
  std::clog << "DBAnnotator::initialize()" << std::endl;
  mysql_init(&mysql);
  db_connect(&mysql);
  return (uima::TyErrorId)UIMA_ERR_NONE;
}


uima::TyErrorId
DBAnnotator::destroy(void) {
  std::clog << "DBAnnotator::destroy()" << std::endl;
  db_disconnect(&mysql);
  output_times();
  return (uima::TyErrorId)UIMA_ERR_NONE;
}


uima::TyErrorId
DBAnnotator::typeSystemInit(const uima::TypeSystem &types) {
  Speaker = types.getType("ontonotes5.to_uima.types.Speaker");
  Speaker__name = Speaker.getFeatureByBaseName("name");
  Speaker__gender = Speaker.getFeatureByBaseName("gender");
  Speaker__competence = Speaker.getFeatureByBaseName("competence");

  Token = types.getType("ontonotes5.to_uima.types.Token");

  ParseNode = types.getType("ontonotes5.to_uima.types.ParseNode");
  ParseNode__tag = ParseNode.getFeatureByBaseName("tag");
  ParseNode__pos = ParseNode.getFeatureByBaseName("pos");
  ParseNode__token = ParseNode.getFeatureByBaseName("token");
  ParseNode__phraseType = ParseNode.getFeatureByBaseName("phraseType");
  ParseNode__functionTags = ParseNode.getFeatureByBaseName("functionTags");
  ParseNode__corefSection = ParseNode.getFeatureByBaseName("corefSection");
  ParseNode__syntacticLink = ParseNode.getFeatureByBaseName("syntacticLink");
  ParseNode__children = ParseNode.getFeatureByBaseName("children");

  NamedEntity = types.getType("ontonotes5.to_uima.types.NamedEntity");
  NamedEntity__tag = NamedEntity.getFeatureByBaseName("tag");
  NamedEntity__startOffset = NamedEntity.getFeatureByBaseName("startOffset");
  NamedEntity__endOffset = NamedEntity.getFeatureByBaseName("endOffset");

  CorefMention = types.getType("ontonotes5.to_uima.types.CorefMention");
  CorefMention__kind = CorefMention.getFeatureByBaseName("kind");
  CorefMention__startOffset = CorefMention.getFeatureByBaseName("startOffset");
  CorefMention__endOffset = CorefMention.getFeatureByBaseName("endOffset");

  CorefChain = types.getType("ontonotes5.to_uima.types.CorefChain");
  CorefChain__id = CorefChain.getFeatureByBaseName("id");
  CorefChain__section = CorefChain.getFeatureByBaseName("section");
  CorefChain__kind = CorefChain.getFeatureByBaseName("kind");
  CorefChain__speaker = CorefChain.getFeatureByBaseName("speaker");
  CorefChain__mentions = CorefChain.getFeatureByBaseName("mentions");

  Sentence = types.getType("ontonotes5.to_uima.types.Sentence");
  Sentence__parse = Sentence.getFeatureByBaseName("parse");
  Sentence__startTime = Sentence.getFeatureByBaseName("startTime");
  Sentence__endTime = Sentence.getFeatureByBaseName("endTime");
  Sentence__speakers = Sentence.getFeatureByBaseName("speakers");
  Sentence__propositions = Sentence.getFeatureByBaseName("propositions");

  PropPredPart = types.getType("ontonotes5.to_uima.types.PropPredPart");
  PropPredPart__encoded = PropPredPart.getFeatureByBaseName("encoded");
  PropPredPart__node = PropPredPart.getFeatureByBaseName("node");

  PropArgPart = types.getType("ontonotes5.to_uima.types.PropArgPart");
  PropArgPart__encoded = PropArgPart.getFeatureByBaseName("encoded");
  PropArgPart__node = PropArgPart.getFeatureByBaseName("node");

  PropArg = types.getType("ontonotes5.to_uima.types.PropArg");
  PropArg__parts = PropArg.getFeatureByBaseName("parts");

  PropArgGroup = types.getType("ontonotes5.to_uima.types.PropArgGroup");
  PropArgGroup__kind = PropArgGroup.getFeatureByBaseName("kind");
  PropArgGroup__args = PropArgGroup.getFeatureByBaseName("args");

  PropLinkPart = types.getType("ontonotes5.to_uima.types.PropLinkPart");
  PropLinkPart__encoded = PropLinkPart.getFeatureByBaseName("encoded");
  PropLinkPart__node = PropLinkPart.getFeatureByBaseName("node");

  PropLink = types.getType("ontonotes5.to_uima.types.PropLink");
  PropLink__parts = PropLink.getFeatureByBaseName("parts");

  PropLinkGroup = types.getType("ontonotes5.to_uima.types.PropLinkGroup");
  PropLinkGroup__kind = PropLinkGroup.getFeatureByBaseName("kind");
  PropLinkGroup__associatedArgumentId = PropLinkGroup.getFeatureByBaseName("associatedArgumentId");
  PropLinkGroup__links = PropLinkGroup.getFeatureByBaseName("links");

  Proposition = types.getType("ontonotes5.to_uima.types.Proposition");
  Proposition__encoded = Proposition.getFeatureByBaseName("encoded");
  Proposition__quality = Proposition.getFeatureByBaseName("quality");
  Proposition__kind = Proposition.getFeatureByBaseName("kind");
  Proposition__lemma = Proposition.getFeatureByBaseName("lemma");
  Proposition__pbSenseNum = Proposition.getFeatureByBaseName("pbSenseNum");
  Proposition__leaf = Proposition.getFeatureByBaseName("leaf");
  Proposition__predParts = Proposition.getFeatureByBaseName("predParts");
  Proposition__argGroups = Proposition.getFeatureByBaseName("argGroups");
  Proposition__linkGroups = Proposition.getFeatureByBaseName("linkGroups");

  Document = types.getType("ontonotes5.to_uima.types.Document");
  Document__docId = Document.getFeatureByBaseName("docId");
  Document__subcorpusId = Document.getFeatureByBaseName("subcorpusId");
  Document__genre = Document.getFeatureByBaseName("genre");
  Document__source = Document.getFeatureByBaseName("source");

  return (uima::TyErrorId)UIMA_ERR_NONE;
}


uima::TyErrorId
DBAnnotator::process(uima::CAS &cas, const uima::ResultSpecification &) {
  icu::UnicodeString ustr;

  // Setup and reset state.
  this->onode_to_node.clear();
  this->onode_to_token.clear();
  this->ospeaker_to_speaker.clear();
  this->sentences.clear();
  this->speakers_by_name.clear();
  this->cas = &cas;
  this->index_repo = &cas.getIndexRepository();
  this->token_char_upto = 0;

  // Get the document annotation.
  const icu::UnicodeString &index_id = cas.getAnnotationIndexID();
  uima::FSIndex index = index_repo->getIndex(index_id, Document);
  assert(index.getSize() == 1);
  uima::FSIterator it = index.iterator();
  uima::DocumentFS doc = static_cast<uima::DocumentFS>(it.get());
  doc_id = doc.getStringValue(Document__docId).asUTF8();

  // Load the document from the DB.
  schwa::Pool pool(4 * 1024 * 1024);
  const time_point_t loading_start = clock_t::now();
  odoc = new (pool) ODocument(&mysql, pool, doc_id.c_str());
  const time_point_t loading_end = clock_t::now();
  add_loading_time(loading_end - loading_start);

  const time_point_t converting_start = clock_t::now();

  // Set the document level properties.
  ustr = icu::UnicodeString(odoc->subcorpus_id, "utf-8");
  doc.setStringValue(Document__subcorpusId, ustr);
  ustr = icu::UnicodeString(odoc->lang, "utf-8");
  doc.setLanguage(ustr);
  ustr = icu::UnicodeString(odoc->genre, "utf-8");
  doc.setStringValue(Document__genre, ustr);
  ustr = icu::UnicodeString(odoc->source, "utf-8");
  doc.setStringValue(Document__source, ustr);
  index_repo->addFS(doc);

  // Convert the speakers.
  for (auto &pair : odoc->speakers)
    convert_speaker(pair.second);

  // Convert the tokens, sentences, parse nodes, and named entities.
  for (auto &osent : odoc->sentences)
    convert_sentence(osent);

  // Convert coref chains and mentions.
  for (auto &ochain : odoc->coref_chains)
    convert_coref(ochain);

  // Convert the propositions.
  for (auto &oprop : odoc->propositions)
    convert_proposition(oprop);

  // Place the propositions back on the sentences, and then save the sentences to the CAS.
  for (decltype(sentences)::size_type i = 0; i != sentences.size(); ++i) {
    uima::AnnotationFS sent = sentences[i];
    // TODO preposition logic
    index_repo->addFS(sent);
  }

  const time_point_t converting_end = clock_t::now();
  add_converting_delta(converting_end - converting_start);

  // Serialize the CAS.
  serialise_cas_binary();
  serialise_cas_xcas();
  serialise_cas_xmi();

  return UIMA_ERR_NONE;
}


void
DBAnnotator::convert_speaker(const OSpeaker *const ospeaker) {
  icu::UnicodeString ustr;
  std::string name;

  uima::FeatureStructure fs = cas->createFS(Speaker);
  ustr = icu::UnicodeString(ospeaker->name, "utf-8");
  ustr.toUTF8String(name);
  fs.setStringValue(Speaker__name, ustr);
  ustr = icu::UnicodeString(ospeaker->gender, "utf-8");
  fs.setStringValue(Speaker__gender, ustr);
  ustr = icu::UnicodeString(ospeaker->competence, "utf-8");
  fs.setStringValue(Speaker__competence, ustr);
  index_repo->addFS(fs);

  speakers_by_name.emplace(name, fs);
  ospeaker_to_speaker.emplace(ospeaker, fs);
}


void
DBAnnotator::convert_sentence(const OSentence *const osent) {
  uima::AnnotationFS first_token, last_token;
  icu::UnicodeString ustr;

  // Construct the tokens.
  std::vector<uima::AnnotationFS> tokens;
  unsigned int i = 0;
  for (auto &otoken : osent->tokens) {
    ustr = icu::UnicodeString(otoken, "utf-8");
    uima::AnnotationFS token = cas->createAnnotation(Token, token_char_upto, token_char_upto + ustr.length());
    index_repo->addFS(token);
    tokens.push_back(token);

    token_char_upto += ustr.length() + 1;
    if (i == 0)
      first_token = token;
    if (i == osent->tokens.size() - 1)
      last_token = token;
    ++i;
  }

  // Construct the sentence.
  uima::AnnotationFS sent = cas->createAnnotation(Sentence, first_token.getBeginPosition(), last_token.getEndPosition());
  sent.setDoubleValue(Sentence__startTime, osent->start_time);
  sent.setDoubleValue(Sentence__endTime, osent->end_time);
  if (!osent->speakers.empty()) {
    i = 0;
    uima::ArrayFS speakers = cas->createArrayFS(osent->speakers.size());
    for (auto &ospeaker : osent->speakers)
      speakers.set(i++, ospeaker_to_speaker[ospeaker]);
    sent.setFSValue(Sentence__speakers, speakers);
  }
  sentences.push_back(sent);

  // Construct the parse nodes.
  std::map<uima::AnnotationFS, const OParseNode *> token_to_onode;
  std::vector<const OParseNode *> onodes;
  osent->parse->all_nodes(onodes);
  size_t nleaves_found = 0;
  for (auto &onode : onodes) {
    uima::FeatureStructure node = cas->createFS(ParseNode);
    ustr = icu::UnicodeString(onode->tag, "utf-8");
    node.setStringValue(ParseNode__tag, ustr);
    if (onode->pos) {
      ustr = icu::UnicodeString(onode->pos, "utf-8");
      node.setStringValue(ParseNode__pos, ustr);
    }
    if (onode->phrase_type) {
      ustr = icu::UnicodeString(onode->phrase_type, "utf-8");
      node.setStringValue(ParseNode__phraseType, ustr);
    }
    if (onode->function_tags) {
      ustr = icu::UnicodeString(onode->function_tags, "utf-8");
      node.setStringValue(ParseNode__functionTags, ustr);
    }
    node.setIntValue(ParseNode__corefSection, onode->coref_section);
    if (onode->is_leaf()) {
      uima::AnnotationFS token = tokens[nleaves_found++];
      node.setFSValue(ParseNode__token, token);
      token_to_onode.emplace(token, onode);
      onode_to_token.emplace(onode, token);
    }
    onode_to_node.emplace(onode, node);
  }

  // Place the new root parse node on the new sentence.
  sent.setFSValue(Sentence__parse, onode_to_node[osent->parse]);

  // Go through the parse nodes, updating their syntactic links and children.
  for (auto &onode : onodes) {
    uima::FeatureStructure node = onode_to_node[onode];
    if (onode->syntactic_link)
      node.setFSValue(ParseNode__syntacticLink, onode_to_node[onode->syntactic_link]);
    if (!onode->children.empty()) {
      i = 0;
      uima::ArrayFS children = cas->createArrayFS(onode->children.size());
      for (auto &ochild : onode->children)
        children.set(i++, onode_to_node[ochild]);
      node.setFSValue(ParseNode__children, children);
    }
    index_repo->addFS(node);
  }

  // Create the named eitites, skipping trace nodes in the word count offset.
  std::vector<const OParseNode *> oleaves;
  osent->parse->ordered_leaves(oleaves);
  for (auto &one : osent->nes) {
    unsigned int start_leaf = 0;
    for (unsigned int i = 0; i != one->token_index; ++i) {
      while (oleaves[start_leaf]->is_trace())
        ++start_leaf;
      ++start_leaf;
    }
    unsigned int end_leaf = start_leaf;
    for (unsigned int i = 1; i != one->length; ++i) {
      while (oleaves[end_leaf]->is_trace())
        ++end_leaf;
      ++end_leaf;
    }

    const size_t start = onode_to_token[oleaves[start_leaf]].getBeginPosition();
    const size_t end = onode_to_token[oleaves[end_leaf]].getEndPosition();
    uima::AnnotationFS ne = cas->createAnnotation(NamedEntity, start, end);
    ustr = icu::UnicodeString(one->tag, "utf-8");
    ne.setStringValue(NamedEntity__tag, ustr);
    ne.setIntValue(NamedEntity__startOffset, one->start_offset);
    ne.setIntValue(NamedEntity__endOffset, one->end_offset);
    index_repo->addFS(ne);
  }
}


void
DBAnnotator::convert_coref(const OCorefChain *const ochain) {
  icu::UnicodeString ustr;

  uima::FeatureStructure chain = cas->createFS(CorefChain);
  ustr = icu::UnicodeString(ochain->identifier, "utf-8");
  chain.setStringValue(CorefChain__id, ustr);
  ustr = icu::UnicodeString(ochain->type, "utf-8");
  chain.setStringValue(CorefChain__kind, ustr);
  chain.setIntValue(CorefChain__section, ochain->section);
  if (ochain->speaker)
    chain.setFSValue(CorefChain__speaker, ospeaker_to_speaker[ochain->speaker]);
  uima::ArrayFS mentions = cas->createArrayFS(ochain->mentions.size());
  unsigned int i = 0;
  for (auto &omention : ochain->mentions) {
    uima::FeatureStructure mention = cas->createFS(CorefMention);
    ustr = icu::UnicodeString(omention->type, "utf-8");
    mention.setStringValue(CorefMention__kind, ustr);
    mention.setIntValue(CorefMention__startOffset, omention->start_offset);
    mention.setIntValue(CorefMention__endOffset, omention->end_offset);

    index_repo->addFS(mention);
    mentions.set(i++, mention);
  }
  chain.setFSValue(CorefChain__mentions, mentions);

  index_repo->addFS(chain);
}


void
DBAnnotator::convert_proposition(const OProposition *const oprop) {
  icu::UnicodeString ustr;
  size_t i, j, k;

  uima::FeatureStructure prop = cas->createFS(Proposition);
  ustr = icu::UnicodeString(oprop->encoded, "utf-8");
  prop.setStringValue(Proposition__encoded, ustr);
  ustr = icu::UnicodeString(oprop->quality, "utf-8");
  prop.setStringValue(Proposition__quality, ustr);
  ustr = icu::UnicodeString(oprop->type, "utf-8");
  prop.setStringValue(Proposition__kind, ustr);
  ustr = icu::UnicodeString(oprop->lemma, "utf-8");
  prop.setStringValue(Proposition__lemma, ustr);
  ustr = icu::UnicodeString(oprop->pb_sense_num, "utf-8");
  prop.setStringValue(Proposition__pbSenseNum, ustr);
  prop.setFSValue(Proposition__leaf, onode_to_node[oprop->leaf]);

  // Construct the predicate.
  uima::ArrayFS pred_parts = cas->createArrayFS(oprop->pred_nodes.size());
  i = 0;
  for (auto &opred_part : oprop->pred_nodes) {
    uima::FeatureStructure pred_part = cas->createFS(PropPredPart);
    ustr = icu::UnicodeString(opred_part->encoded, "utf-8");
    pred_part.setStringValue(PropPredPart__encoded, ustr);
    pred_part.setFSValue(PropPredPart__node, onode_to_node[opred_part->node]);
    index_repo->addFS(pred_part);
    pred_parts.set(i++, pred_part);
  }
  prop.setFSValue(Proposition__predParts, pred_parts);

  // Construct the argument groups.
  uima::ArrayFS arg_groups = cas->createArrayFS(oprop->arg_groups.size());
  i = 0;
  for (auto &oarg_group : oprop->arg_groups) {
    uima::FeatureStructure arg_group = cas->createFS(PropArgGroup);
    ustr = icu::UnicodeString(oarg_group->type, "utf-8");
    arg_group.setStringValue(PropArgGroup__kind, ustr);
    uima::ArrayFS args = cas->createArrayFS(oarg_group->args.size());
    j = 0;
    for (auto &oarg : oarg_group->args) {
      uima::FeatureStructure arg = cas->createFS(PropArg);
      uima::ArrayFS parts = cas->createArrayFS(oarg->parts.size());
      k = 0;
      for (auto &oarg_part : oarg->parts) {
        uima::FeatureStructure part = cas->createFS(PropArgPart);
        ustr = icu::UnicodeString(oarg_part->encoded, "utf-8");
        part.setStringValue(PropArgPart__encoded, ustr);
        part.setFSValue(PropArgPart__node, onode_to_node[oarg_part->node]);

        index_repo->addFS(part);
        parts.set(k++, part);
      }
      arg.setFSValue(PropArg__parts, parts);

      index_repo->addFS(arg);
      args.set(j++, arg);
    }
    arg_group.setFSValue(PropArgGroup__args, args);

    index_repo->addFS(arg_group);
    arg_groups.set(i++, arg_group);
  }
  prop.setFSValue(Proposition__argGroups, arg_groups);

  // Construct the link groups.
  uima::ArrayFS link_groups = cas->createArrayFS(oprop->link_groups.size());
  i = 0;
  for (auto &olink_group : oprop->link_groups) {
    uima::FeatureStructure link_group = cas->createFS(PropLinkGroup);
    ustr = icu::UnicodeString(olink_group->type, "utf-8");
    link_group.setStringValue(PropLinkGroup__kind, ustr);
    ustr = icu::UnicodeString(olink_group->associated_argument_id, "utf-8");
    link_group.setStringValue(PropLinkGroup__associatedArgumentId, ustr);
    uima::ArrayFS links = cas->createArrayFS(olink_group->links.size());
    j = 0;
    for (auto &olink : olink_group->links) {
      uima::FeatureStructure link = cas->createFS(PropLink);
      uima::ArrayFS parts = cas->createArrayFS(olink->parts.size());
      k = 0;
      for (auto &olink_part : olink->parts) {
        uima::FeatureStructure part = cas->createFS(PropLinkPart);
        ustr = icu::UnicodeString(olink_part->encoded, "utf-8");
        part.setStringValue(PropLinkPart__encoded, ustr);
        part.setFSValue(PropLinkPart__node, onode_to_node[olink_part->node]);

        index_repo->addFS(part);
        parts.set(k++, part);
      }
      link.setFSValue(PropLink__parts, parts);

      index_repo->addFS(link);
      links.set(j++, link);
    }
    link_group.setFSValue(PropLinkGroup__links, links);

    index_repo->addFS(link_group);
    link_groups.set(i++, link_group);
  }
  prop.setFSValue(Proposition__linkGroups, link_groups);

  index_repo->addFS(prop);
}


void
DBAnnotator::serialise_cas_binary(void) {
  std::ostringstream output_path;
  output_path << OUTPUT_DIR << '/' << doc_id.substr(doc_id.rfind('/') + 1) << ".data";

  std::ofstream out(output_path.str());
  const time_point_t write_start = clock_t::now();
  uima::internal::CASSerializer serialiser(false);
  uima::internal::SerializedCAS serialised_cas;
  serialiser.serializeData(*cas, serialised_cas);
  serialised_cas.print(out);
  const time_point_t write_end = clock_t::now();
  out.close();

  add_writing_binary_delta(write_end - write_start);
}


void
DBAnnotator::serialise_cas_xcas(void) {
  std::ostringstream output_path;
  output_path << OUTPUT_DIR << '/' << doc_id.substr(doc_id.rfind('/') + 1) << ".xcas";

  const time_point_t write_start = clock_t::now();
  std::ofstream out(output_path.str());
  uima::XCASWriter writer(*cas, true);
  writer.write(out);
  out.close();
  const time_point_t write_end = clock_t::now();

  add_writing_xcas_delta(write_end - write_start);
}


void
DBAnnotator::serialise_cas_xmi(void) {
  std::ostringstream output_path;
  output_path << OUTPUT_DIR << '/' << doc_id.substr(doc_id.rfind('/') + 1) << ".xmi";

  const time_point_t write_start = clock_t::now();
  std::ofstream out(output_path.str());
  uima::XmiWriter writer(*cas, true);
  writer.write(out);
  out.close();
  const time_point_t write_end = clock_t::now();

  add_writing_xmi_delta(write_end - write_start);
}



}  // namespace to_uima
}  // namespace ontonotes5


// This macro exports an entry point that is used to create the annotator.
extern "C" UIMA_ANNOTATOR_LINK_IMPORTSPEC uima::Annotator *makeAE(void) {
  return new ontonotes5::to_uima::DBAnnotator();
}
