#include "models-dr.h"

#define CBF_SCHEMA(super, name) name::Schema::Schema(void) : dr::super::Schema<name>(#name, #name)
#define CBF_FIELD(name) name(*this, #name, #name, dr::FieldMode::READ_WRITE)

namespace dr = schwa::dr;


namespace ontonotes5 {
namespace to_dr {

CBF_SCHEMA(Ann, Speaker),
    CBF_FIELD(name), CBF_FIELD(gender), CBF_FIELD(competence) { }

CBF_SCHEMA(Ann, Token),
    CBF_FIELD(span), CBF_FIELD(raw) { }

CBF_SCHEMA(Ann, ParseNode),
    CBF_FIELD(tag), CBF_FIELD(pos), CBF_FIELD(phrase_type), CBF_FIELD(function_tags), CBF_FIELD(token), CBF_FIELD(syntactic_link), CBF_FIELD(coref_section), CBF_FIELD(children) { }

CBF_SCHEMA(Ann, PropPredPart),
    CBF_FIELD(encoded), CBF_FIELD(node) { }

CBF_SCHEMA(Ann, PropArgPart),
    CBF_FIELD(encoded), CBF_FIELD(node) { }

CBF_SCHEMA(Ann, PropArg),
    CBF_FIELD(parts) { }

CBF_SCHEMA(Ann, PropArgGroup),
    CBF_FIELD(type), CBF_FIELD(args) { }

CBF_SCHEMA(Ann, PropLinkPart),
    CBF_FIELD(encoded), CBF_FIELD(node) { }

CBF_SCHEMA(Ann, PropLink),
    CBF_FIELD(parts) { }

CBF_SCHEMA(Ann, PropLinkGroup),
    CBF_FIELD(type), CBF_FIELD(associated_argument_id), CBF_FIELD(links) { }

CBF_SCHEMA(Ann, Proposition),
    CBF_FIELD(encoded), CBF_FIELD(quality), CBF_FIELD(type), CBF_FIELD(lemma), CBF_FIELD(pb_sense_num), CBF_FIELD(leaf), CBF_FIELD(pred_nodes), CBF_FIELD(arg_groups), CBF_FIELD(link_groups) { }

CBF_SCHEMA(Ann, NamedEntity),
    CBF_FIELD(span), CBF_FIELD(tag), CBF_FIELD(start_offset), CBF_FIELD(end_offset) { }

CBF_SCHEMA(Ann, CorefMention),
    CBF_FIELD(span), CBF_FIELD(type), CBF_FIELD(start_offset), CBF_FIELD(end_offset) { }

CBF_SCHEMA(Ann, CorefChain),
    CBF_FIELD(id), CBF_FIELD(type), CBF_FIELD(section), CBF_FIELD(speaker), CBF_FIELD(mentions) { }

CBF_SCHEMA(Ann, Sentence),
    CBF_FIELD(span), CBF_FIELD(parse), CBF_FIELD(start_time), CBF_FIELD(end_time), CBF_FIELD(speakers) { }

CBF_SCHEMA(Doc, Doc),
    CBF_FIELD(doc_id), CBF_FIELD(subcorpus_id), CBF_FIELD(lang), CBF_FIELD(genre), CBF_FIELD(source),
    CBF_FIELD(tokens), CBF_FIELD(sentences), CBF_FIELD(parse_nodes), CBF_FIELD(prop_pred_parts), CBF_FIELD(prop_arg_parts), CBF_FIELD(prop_args), CBF_FIELD(prop_arg_groups), CBF_FIELD(prop_link_parts), CBF_FIELD(prop_links), CBF_FIELD(prop_link_groups), CBF_FIELD(propositions), CBF_FIELD(named_entities), CBF_FIELD(speakers), CBF_FIELD(coref_mentions), CBF_FIELD(coref_chains) { }

}  // namesapce to_dr
}  // namespace ontonotes5
