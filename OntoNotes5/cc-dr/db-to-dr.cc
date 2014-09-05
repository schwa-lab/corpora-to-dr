#include "db-to-dr.h"


namespace ontonotes5 {
namespace to_dr {

Converter::Converter(void) {
  reset();
}


void
Converter::reset(void) {
  doc = nullptr;
  odoc = nullptr;
  onode_to_node.clear();
  onode_to_token.clear();
  ospeaker_to_speaker.clear();
}


Doc *
Converter::convert(const ODocument &odoc) {
  this->odoc = &odoc;

  // Construct the doc object.
  doc = new Doc();
  doc->doc_id = odoc.doc_id;
  doc->subcorpus_id = odoc.subcorpus_id;
  doc->lang = odoc.lang;
  doc->genre = odoc.genre;
  doc->source = odoc.source;

  convert_speakers();
  convert_tokens();
  convert_coref();
  convert_propositions();

  return doc;
}


void
Converter::convert_speakers(void) {
  doc->speakers.create(odoc->speakers.size());
  size_t i = 0;
  for (auto &pair : odoc->speakers) {
    Speaker &speaker = doc->speakers[i++];
    speaker.name = pair.second->name;
    speaker.gender = pair.second->gender;
    speaker.competence = pair.second->competence;
    ospeaker_to_speaker.emplace(pair.second, &speaker);
  }
}


void
Converter::convert_tokens(void) {
  // Allocate space for all of the tokens, sentences, and parse nodes needed in one go.
  size_t ntokens = 0, nsentences = 0, nnodes = 0, nnes = 0;
  for (auto &osent : odoc->sentences) {
    nnes += osent->nes.size();
    nnodes += osent->parse->nnodes();
    ntokens += osent->tokens.size();
  }

  doc->named_entities.create(nnes);
  doc->parse_nodes.create(nnodes);
  doc->sentences.create(odoc->sentences.size());
  doc->tokens.create(ntokens);

  // For each sentence.
  uint64_t byte_offset_start = 0;
  ntokens = nsentences = nnodes = nnes = 0;
  for (auto &osent : odoc->sentences) {
    // Create the tokens.
    Token *first_token = nullptr;  // inclusive.
    for (auto &otoken : osent->tokens) {
      Token &token = doc->tokens[ntokens++];
      token.raw = otoken;
      token.span.start = byte_offset_start;
      token.span.stop = byte_offset_start + token.raw.size();

      byte_offset_start += token.raw.size() + 1;
      if (first_token == nullptr)
        first_token = &token;
    }
    Token *const last_token = &doc->tokens[ntokens];  // exclusive.

    // Create the sentence.
    Sentence &sent = doc->sentences[nsentences++];
    sent.span.start = first_token;
    sent.span.stop = last_token;
    sent.start_time = osent->start_time;
    sent.end_time = osent->end_time;
    for (auto &ospeaker : osent->speakers)
      sent.speakers.items.push_back(ospeaker_to_speaker[ospeaker]);

    // Create the parse nodes.
    std::unordered_map<Token *, const OParseNode *> token_to_onode;
    std::vector<const OParseNode *> onodes;
    osent->parse->all_nodes(onodes);
    size_t nleaves_found = 0;
    for (auto &onode : onodes) {
      ParseNode &node = doc->parse_nodes[nnodes++];
      onode_to_node.emplace(onode, &node);
      node.tag = onode->tag;
      if (onode->pos)
        node.pos = onode->pos;
      if (onode->phrase_type)
        node.phrase_type = onode->phrase_type;
      if (onode->function_tags)
        node.function_tags = onode->function_tags;
      node.coref_section = onode->coref_section;
      if (onode->is_leaf()) {
        node.token.ptr = first_token + nleaves_found;
        token_to_onode.emplace(node.token.ptr, onode);
        onode_to_token.emplace(onode, node.token.ptr);
        ++nleaves_found;
      }
    }

    // Place the new root parse node on the new sentence.
    sent.parse.ptr = onode_to_node[osent->parse];

    // Go through them again, updating their syntactic_link and children pointers.
    for (auto &onode : onodes) {
      ParseNode &node = *onode_to_node[onode];
      if (onode->syntactic_link)
        node.syntactic_link.ptr = onode_to_node[onode->syntactic_link];
      for (auto &ochild : onode->children)
        node.children.items.push_back(onode_to_node[ochild]);
    }

    // Create the named eitites, skipping trace nodes in the word count offset.
    for (auto &one : osent->nes) {
      NamedEntity &ne = doc->named_entities[nnes++];
      ne.tag = one->tag;
      ne.start_offset = one->start_offset;
      ne.end_offset = one->end_offset;
      ne.span.start = first_token;
      for (unsigned int i = 0; i != one->token_index; ++i) {
        while (token_to_onode[ne.span.start]->is_trace())
          ++ne.span.start;
        ++ne.span.start;
      }
      ne.span.stop = ne.span.start;
      for (unsigned int i = 0; i != one->length; ++i) {
        while (token_to_onode[ne.span.stop]->is_trace())
          ++ne.span.stop;
        ++ne.span.stop;
      }
    }
  }
}


void
Converter::convert_coref(void) {
  size_t nchains = 0, nmentions = 0;
  for (auto &ochain : odoc->coref_chains)
    nmentions += ochain->mentions.size();

  doc->coref_chains.create(odoc->coref_chains.size());
  doc->coref_mentions.create(nmentions);

  nchains = nmentions = 0;
  for (auto &ochain : odoc->coref_chains) {
    CorefChain &chain = doc->coref_chains[nchains++];
    chain.id = ochain->identifier;
    chain.type = ochain->type;
    chain.section = ochain->section;
    if (ochain->speaker)
      chain.speaker.ptr = ospeaker_to_speaker[ochain->speaker];

    for (auto &omention : ochain->mentions) {
      CorefMention &mention = doc->coref_mentions[nmentions++];
      mention.span.start = onode_to_token[omention->start_leaf];
      mention.span.stop = onode_to_token[omention->end_leaf] + 1;
      mention.type = omention->type;
      mention.start_offset = omention->start_offset;
      mention.end_offset = omention->end_offset;

      chain.mentions.items.push_back(&mention);
    }
  }
}


void
Converter::convert_propositions(void) {
  size_t nprop_pred_parts = 0, nprop_arg_parts = 0, nprop_link_parts = 0, nprop_args = 0, nprop_links = 0, nprop_arg_groups = 0, nprop_link_groups = 0, npropositions;
  for (auto &oprop : odoc->propositions) {
    nprop_pred_parts += oprop->pred_nodes.size();
    nprop_arg_groups += oprop->arg_groups.size();
    nprop_link_groups += oprop->link_groups.size();
    for (auto &oarg_group : oprop->arg_groups) {
      nprop_args += oarg_group->args.size();
      for (auto &oarg : oarg_group->args)
        nprop_arg_parts += oarg->parts.size();
    }
    for (auto &olink_group : oprop->link_groups) {
      nprop_links += olink_group->links.size();
      for (auto &olink : olink_group->links)
        nprop_link_parts += olink->parts.size();
    }
  }

  doc->prop_pred_parts.create(nprop_pred_parts);
  doc->prop_arg_parts.create(nprop_arg_parts);
  doc->prop_args.create(nprop_args);
  doc->prop_arg_groups.create(nprop_arg_groups);
  doc->prop_link_parts.create(nprop_link_parts);
  doc->prop_links.create(nprop_links);
  doc->prop_link_groups.create(nprop_link_groups);
  doc->propositions.create(odoc->propositions.size());

  nprop_pred_parts = nprop_arg_parts = nprop_link_parts = nprop_args = nprop_links = nprop_arg_groups = nprop_link_groups = npropositions = 0;
  for (auto &oprop : odoc->propositions) {
    Proposition &prop = doc->propositions[npropositions++];
    prop.encoded = oprop->encoded;
    prop.quality = oprop->quality;
    prop.type = oprop->type;
    prop.lemma = oprop->lemma;
    prop.pb_sense_num = oprop->pb_sense_num;
    prop.leaf.ptr = onode_to_node[oprop->leaf];

    for (auto &ogroup : oprop->arg_groups) {
      PropArgGroup &group = doc->prop_arg_groups[nprop_arg_groups++];
      group.type = ogroup->type;
      prop.arg_groups.items.push_back(&group);

      for (auto &oarg : ogroup->args) {
        PropArg &arg = doc->prop_args[nprop_args++];
        group.args.items.push_back(&arg);

        for (auto &opart : oarg->parts) {
          PropArgPart &part = doc->prop_arg_parts[nprop_arg_parts++];
          part.encoded = opart->encoded;
          part.node.ptr = onode_to_node[opart->node];
          arg.parts.items.push_back(&part);
        }
      }
    }

    for (auto &ogroup : oprop->link_groups) {
      PropLinkGroup &group = doc->prop_link_groups[nprop_link_groups++];
      group.type = ogroup->type;
      group.associated_argument_id = ogroup->associated_argument_id;
      prop.link_groups.items.push_back(&group);

      for (auto &olink : ogroup->links) {
        PropLink &link = doc->prop_links[nprop_links++];
        group.links.items.push_back(&link);

        for (auto &opart : olink->parts) {
          PropLinkPart &part = doc->prop_link_parts[nprop_link_parts++];
          part.encoded = opart->encoded;
          part.node.ptr = onode_to_node[opart->node];
          link.parts.items.push_back(&part);
        }
      }
    }
  }
}

}  // namespace to_dr
}  // namespace ontonotes5
