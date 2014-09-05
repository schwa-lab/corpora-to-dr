#ifndef DBANNOTATOR_H_
#define DBANNOTATOR_H_

#include <string>
#include <unordered_map>

#include <uima/api.hpp>

#include "db.h"
#include "models-db.h"


namespace ontonotes5 {
  namespace to_uima {

    class DBAnnotator : public uima::Annotator {
    private:
      uima::Type Speaker;
      uima::Feature Speaker__name;
      uima::Feature Speaker__gender;
      uima::Feature Speaker__competence;

      uima::Type Token;

      uima::Type ParseNode;
      uima::Feature ParseNode__tag;
      uima::Feature ParseNode__pos;
      uima::Feature ParseNode__token;
      uima::Feature ParseNode__phraseType;
      uima::Feature ParseNode__functionTags;
      uima::Feature ParseNode__corefSection;
      uima::Feature ParseNode__syntacticLink;
      uima::Feature ParseNode__children;

      uima::Type NamedEntity;
      uima::Feature NamedEntity__tag;
      uima::Feature NamedEntity__startOffset;
      uima::Feature NamedEntity__endOffset;

      uima::Type CorefMention;
      uima::Feature CorefMention__kind;
      uima::Feature CorefMention__startOffset;
      uima::Feature CorefMention__endOffset;

      uima::Type CorefChain;
      uima::Feature CorefChain__id;
      uima::Feature CorefChain__section;
      uima::Feature CorefChain__kind;
      uima::Feature CorefChain__speaker;
      uima::Feature CorefChain__mentions;

      uima::Type Sentence;
      uima::Feature Sentence__parse;
      uima::Feature Sentence__startTime;
      uima::Feature Sentence__endTime;
      uima::Feature Sentence__speakers;
      uima::Feature Sentence__propositions;

      uima::Type PropPredPart;
      uima::Feature PropPredPart__encoded;
      uima::Feature PropPredPart__node;

      uima::Type PropArgPart;
      uima::Feature PropArgPart__encoded;
      uima::Feature PropArgPart__node;

      uima::Type PropArg;
      uima::Feature PropArg__parts;

      uima::Type PropArgGroup;
      uima::Feature PropArgGroup__kind;
      uima::Feature PropArgGroup__args;

      uima::Type PropLinkPart;
      uima::Feature PropLinkPart__encoded;
      uima::Feature PropLinkPart__node;

      uima::Type PropLink;
      uima::Feature PropLink__parts;

      uima::Type PropLinkGroup;
      uima::Feature PropLinkGroup__kind;
      uima::Feature PropLinkGroup__associatedArgumentId;
      uima::Feature PropLinkGroup__links;

      uima::Type Proposition;
      uima::Feature Proposition__encoded;
      uima::Feature Proposition__quality;
      uima::Feature Proposition__kind;
      uima::Feature Proposition__lemma;
      uima::Feature Proposition__pbSenseNum;
      uima::Feature Proposition__leaf;
      uima::Feature Proposition__predParts;
      uima::Feature Proposition__argGroups;
      uima::Feature Proposition__linkGroups;

      uima::Type Document;
      uima::Feature Document__docId;
      uima::Feature Document__subcorpusId;
      uima::Feature Document__genre;
      uima::Feature Document__source;

      MYSQL mysql;
      std::unordered_map<const OParseNode *, uima::FeatureStructure> onode_to_node;
      std::unordered_map<const OParseNode *, uima::AnnotationFS> onode_to_token;
      std::unordered_map<const OSpeaker *, uima::FeatureStructure> ospeaker_to_speaker;
      std::vector<uima::AnnotationFS> sentences;
      std::unordered_map<std::string, uima::FeatureStructure> speakers_by_name;

      const ODocument *odoc;
      std::string doc_id;
      int32_t token_char_upto;

      uima::CAS *cas;
      uima::FSIndexRepository *index_repo;

      void convert_coref(const OCorefChain *ochain);
      void convert_proposition(const OProposition *oprop);
      void convert_sentence(const OSentence *osent);
      void convert_speaker(const OSpeaker *ospeaker);
      void serialise_cas_binary(void);
      void serialise_cas_xmi(void);
      void serialise_cas_xcas(void);

    public:
      DBAnnotator(void);
      virtual ~DBAnnotator(void);

      virtual uima::TyErrorId typeSystemInit(const uima::TypeSystem &types) override;
      virtual uima::TyErrorId initialize(uima::AnnotatorContext &context) override;
      virtual uima::TyErrorId destroy(void) override;

      virtual uima::TyErrorId process(uima::CAS &cas, const uima::ResultSpecification &result_spec) override;
    };

  }
}

#endif  // DBANNOTATOR
