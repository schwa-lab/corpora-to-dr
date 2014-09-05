#ifndef MODELS_DR_H_
#define MODELS_DR_H_

#include <string>
#include <vector>

#include <schwa/dr.h>


namespace ontonotes5 {
  namespace to_dr {

    struct Speaker : public schwa::dr::Ann {
      std::string name;
      std::string gender;
      std::string competence;

      class Schema;
    };


    struct Token : public schwa::dr::Ann {
      schwa::dr::Slice<uint64_t> span;
      std::string raw;

      class Schema;
    };


    struct ParseNode : public schwa::dr::Ann {
      std::string tag;
      std::string pos;
      std::string phrase_type;
      std::string function_tags;
      schwa::dr::Pointer<Token> token;
      schwa::dr::Pointer<ParseNode> syntactic_link;
      int coref_section;
      schwa::dr::Pointers<ParseNode> children;

      class Schema;
    };


    struct PropPredPart : public schwa::dr::Ann {
      std::string encoded;
      schwa::dr::Pointer<ParseNode> node;

      class Schema;
    };


    struct PropArgPart : public schwa::dr::Ann {
      std::string encoded;
      schwa::dr::Pointer<ParseNode> node;

      class Schema;
    };


    struct PropArg : public schwa::dr::Ann {
      schwa::dr::Pointers<PropArgPart> parts;

      class Schema;
    };


    struct PropArgGroup : public schwa::dr::Ann {
      std::string type;
      schwa::dr::Pointers<PropArg> args;

      class Schema;
    };


    struct PropLinkPart : public schwa::dr::Ann {
      std::string encoded;
      schwa::dr::Pointer<ParseNode> node;

      class Schema;
    };


    struct PropLink : public schwa::dr::Ann {
      schwa::dr::Pointers<PropLinkPart> parts;

      class Schema;
    };


    struct PropLinkGroup : public schwa::dr::Ann {
      std::string type;
      std::string associated_argument_id;
      schwa::dr::Pointers<PropLink> links;

      class Schema;
    };


    struct Proposition : public schwa::dr::Ann {
      std::string encoded;
      std::string quality;
      std::string type;
      std::string lemma;
      std::string pb_sense_num;
      schwa::dr::Pointer<ParseNode> leaf;
      schwa::dr::Pointers<PropPredPart> pred_nodes;
      schwa::dr::Pointers<PropArgGroup> arg_groups;
      schwa::dr::Pointers<PropLinkGroup> link_groups;

      class Schema;
    };

    struct NamedEntity : public schwa::dr::Ann {
      schwa::dr::Slice<Token *> span;
      std::string tag;
      unsigned int start_offset;
      unsigned int end_offset;

      class Schema;
    };


    struct CorefMention : public schwa::dr::Ann {
      schwa::dr::Slice<Token *> span;
      std::string type;
      unsigned int start_offset;
      unsigned int end_offset;

      class Schema;
    };


    struct CorefChain : public schwa::dr::Ann {
      std::string id;
      std::string type;
      unsigned int section;
      schwa::dr::Pointer<Speaker> speaker;
      schwa::dr::Pointers<CorefMention> mentions;

      class Schema;
    };


    struct Sentence : public schwa::dr::Ann {
      schwa::dr::Slice<Token *> span;
      schwa::dr::Pointer<ParseNode> parse;
      double start_time;
      double end_time;
      schwa::dr::Pointers<Speaker> speakers;

      Sentence(void) : schwa::dr::Ann(), start_time(0), end_time(0) { }

      class Schema;
    };


    struct Doc : public schwa::dr::Doc {
      std::string doc_id;
      std::string subcorpus_id;
      std::string lang;
      std::string genre;
      std::string source;

      schwa::dr::Store<Token> tokens;
      schwa::dr::Store<Sentence> sentences;
      schwa::dr::Store<ParseNode> parse_nodes;
      schwa::dr::Store<PropPredPart> prop_pred_parts;
      schwa::dr::Store<PropArgPart> prop_arg_parts;
      schwa::dr::Store<PropArg> prop_args;
      schwa::dr::Store<PropArgGroup> prop_arg_groups;
      schwa::dr::Store<PropLinkPart> prop_link_parts;
      schwa::dr::Store<PropLink> prop_links;
      schwa::dr::Store<PropLinkGroup> prop_link_groups;
      schwa::dr::Store<Proposition> propositions;
      schwa::dr::Store<NamedEntity> named_entities;
      schwa::dr::Store<Speaker> speakers;
      schwa::dr::Store<CorefMention> coref_mentions;
      schwa::dr::Store<CorefChain> coref_chains;

      class Schema;
    };


    // ========================================================================
    // Schemas
    // ========================================================================
    struct Speaker::Schema : public schwa::dr::Ann::Schema<Speaker> {
      DR_FIELD(&Speaker::name) name;
      DR_FIELD(&Speaker::gender) gender;
      DR_FIELD(&Speaker::competence) competence;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct Token::Schema : public schwa::dr::Ann::Schema<Token> {
      DR_FIELD(&Token::span) span;
      DR_FIELD(&Token::raw) raw;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct ParseNode::Schema : public schwa::dr::Ann::Schema<ParseNode> {
      DR_FIELD(&ParseNode::tag) tag;
      DR_FIELD(&ParseNode::pos) pos;
      DR_FIELD(&ParseNode::phrase_type) phrase_type;
      DR_FIELD(&ParseNode::function_tags) function_tags;
      DR_POINTER(&ParseNode::token, &Doc::tokens) token;
      DR_SELF(&ParseNode::syntactic_link) syntactic_link;
      DR_FIELD(&ParseNode::coref_section) coref_section;
      DR_SELF(&ParseNode::children) children;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropPredPart::Schema : public schwa::dr::Ann::Schema<PropPredPart> {
      DR_FIELD(&PropPredPart::encoded) encoded;
      DR_POINTER(&PropPredPart::node, &Doc::parse_nodes) node;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropArgPart::Schema : public schwa::dr::Ann::Schema<PropArgPart> {
      DR_FIELD(&PropArgPart::encoded) encoded;
      DR_POINTER(&PropArgPart::node, &Doc::parse_nodes) node;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropArg::Schema : public schwa::dr::Ann::Schema<PropArg> {
      DR_POINTER(&PropArg::parts, &Doc::prop_arg_parts) parts;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropArgGroup::Schema : public schwa::dr::Ann::Schema<PropArgGroup> {
      DR_FIELD(&PropArgGroup::type) type;
      DR_POINTER(&PropArgGroup::args, &Doc::prop_args) args;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropLinkPart::Schema : public schwa::dr::Ann::Schema<PropLinkPart> {
      DR_FIELD(&PropLinkPart::encoded) encoded;
      DR_POINTER(&PropLinkPart::node, &Doc::parse_nodes) node;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropLink::Schema : public schwa::dr::Ann::Schema<PropLink> {
      DR_POINTER(&PropLink::parts, &Doc::prop_link_parts) parts;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct PropLinkGroup::Schema : public schwa::dr::Ann::Schema<PropLinkGroup> {
      DR_FIELD(&PropLinkGroup::type) type;
      DR_FIELD(&PropLinkGroup::associated_argument_id) associated_argument_id;
      DR_POINTER(&PropLinkGroup::links, &Doc::prop_links) links;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct Proposition::Schema : public schwa::dr::Ann::Schema<Proposition> {
      DR_FIELD(&Proposition::encoded) encoded;
      DR_FIELD(&Proposition::quality) quality;
      DR_FIELD(&Proposition::type) type;
      DR_FIELD(&Proposition::lemma) lemma;
      DR_FIELD(&Proposition::pb_sense_num) pb_sense_num;
      DR_POINTER(&Proposition::leaf, &Doc::parse_nodes) leaf;
      DR_POINTER(&Proposition::pred_nodes, &Doc::prop_pred_parts) pred_nodes;
      DR_POINTER(&Proposition::arg_groups, &Doc::prop_arg_groups) arg_groups;
      DR_POINTER(&Proposition::link_groups, &Doc::prop_link_groups) link_groups;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct NamedEntity::Schema : public schwa::dr::Ann::Schema<NamedEntity> {
      DR_POINTER(&NamedEntity::span, &Doc::tokens) span;
      DR_FIELD(&NamedEntity::tag) tag;
      DR_FIELD(&NamedEntity::start_offset) start_offset;
      DR_FIELD(&NamedEntity::end_offset) end_offset;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct CorefMention::Schema : public schwa::dr::Ann::Schema<CorefMention> {
      DR_POINTER(&CorefMention::span, &Doc::tokens) span;
      DR_FIELD(&CorefMention::type) type;
      DR_FIELD(&CorefMention::start_offset) start_offset;
      DR_FIELD(&CorefMention::end_offset) end_offset;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct CorefChain::Schema : public schwa::dr::Ann::Schema<CorefChain> {
      DR_FIELD(&CorefChain::id) id;
      DR_FIELD(&CorefChain::type) type;
      DR_FIELD(&CorefChain::section) section;
      DR_POINTER(&CorefChain::speaker, &Doc::speakers) speaker;
      DR_POINTER(&CorefChain::mentions, &Doc::coref_mentions) mentions;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct Sentence::Schema : public schwa::dr::Ann::Schema<Sentence> {
      DR_POINTER(&Sentence::span, &Doc::tokens) span;
      DR_POINTER(&Sentence::parse, &Doc::parse_nodes) parse;
      DR_FIELD(&Sentence::start_time) start_time;
      DR_FIELD(&Sentence::end_time) end_time;
      DR_POINTER(&Sentence::speakers, &Doc::speakers) speakers;

      Schema(void);
      virtual ~Schema(void) { }
    };


    struct Doc::Schema : public schwa::dr::Doc::Schema<Doc> {
      DR_FIELD(&Doc::doc_id) doc_id;
      DR_FIELD(&Doc::subcorpus_id) subcorpus_id;
      DR_FIELD(&Doc::lang) lang;
      DR_FIELD(&Doc::genre) genre;
      DR_FIELD(&Doc::source) source;

      DR_STORE(&Doc::tokens) tokens;
      DR_STORE(&Doc::sentences) sentences;
      DR_STORE(&Doc::parse_nodes) parse_nodes;
      DR_STORE(&Doc::prop_pred_parts) prop_pred_parts;
      DR_STORE(&Doc::prop_arg_parts) prop_arg_parts;
      DR_STORE(&Doc::prop_args) prop_args;
      DR_STORE(&Doc::prop_arg_groups) prop_arg_groups;
      DR_STORE(&Doc::prop_link_parts) prop_link_parts;
      DR_STORE(&Doc::prop_links) prop_links;
      DR_STORE(&Doc::prop_link_groups) prop_link_groups;
      DR_STORE(&Doc::propositions) propositions;
      DR_STORE(&Doc::named_entities) named_entities;
      DR_STORE(&Doc::speakers) speakers;
      DR_STORE(&Doc::coref_mentions) coref_mentions;
      DR_STORE(&Doc::coref_chains) coref_chains;

      Schema(void);
      virtual ~Schema(void) { }
    };

  }
}

#endif  // MODELS_DR_H_
