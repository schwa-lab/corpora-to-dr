#include "models-db.h"

#include <cassert>
#include <cstring>
#include <iostream>
#include <sstream>
#include <string>
#include <tuple>
#include <unordered_map>
#include <utility>
#include <vector>

#include "db.h"


namespace ontonotes5 {

static inline void
clone_str_from_row(char **dest, const char *src, const unsigned long src_len, schwa::Pool &pool) {
  if (src_len == 0)
    *dest = nullptr;
  else {
    *dest = (char *)pool.alloc(src_len + 1);
    std::memcpy(*dest, src, src_len);
    (*dest)[src_len] = '\0';
  }
}


// ============================================================================
// OProposition
// ============================================================================
OPropArg::OPropArg(schwa::Pool &pool) : parts(decltype(parts)::allocator_type(pool)) { }


OPropArgGroup::OPropArgGroup(MYSQL *const mysql, schwa::Pool &pool, const char *const id, OSentence &sentence, const unsigned int index, char *const type) :
    type(type),
    args(decltype(args)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  std::vector<std::string> ids;
  ss << "SELECT id FROM argument WHERE proposition_id = \"" << id << "\" AND argument_analogue_index = " << index << " ORDER BY index_in_parent";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  assert(nrows > 0);

  for (auto &id : ids) {
    OPropArg *arg = new (pool) OPropArg(pool);
    args.push_back(arg);

    ss.str("");
    ss << "SELECT node_id FROM argument_node WHERE argument_id = \"" << id << "\"";
    query = ss.str();
    db_query(mysql, query.c_str());
    result = mysql_use_result(mysql);
    for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
      lengths = mysql_fetch_lengths(result);

      OPropNode *part = new (pool) OPropNode();
      clone_str_from_row(&part->encoded, row[0], lengths[0], pool);
      part->node = sentence.node_by_propbank_id(row[0]);
      arg->parts.push_back(part);
    }
    mysql_free_result(result);
  }
}


OPropLink::OPropLink(schwa::Pool &pool) : parts(decltype(parts)::allocator_type(pool)) { }


OPropLinkGroup::OPropLinkGroup(MYSQL *const mysql, schwa::Pool &pool, const char *const id, OSentence &sentence, const unsigned int index, char *const type, char *const associated_argument_id) :
    type(type),
    associated_argument_id(associated_argument_id),
    links(decltype(links)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  std::vector<std::string> ids;
  ss << "SELECT id FROM proposition_link WHERE proposition_id = \"" << id << "\" AND link_analogue_index = " << index << " ORDER BY index_in_parent";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  assert(nrows > 0);

  for (auto &id : ids) {
    OPropLink *link = new (pool) OPropLink(pool);
    links.push_back(link);

    ss.str("");
    ss << "SELECT node_id FROM link_node WHERE link_id = \"" << id << "\"";
    query = ss.str();
    db_query(mysql, query.c_str());
    result = mysql_use_result(mysql);
    for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
      lengths = mysql_fetch_lengths(result);

      OPropNode *part = new (pool) OPropNode();
      clone_str_from_row(&part->encoded, row[0], lengths[0], pool);
      part->node = sentence.node_by_propbank_id(row[0]);
      link->parts.push_back(part);
    }
    mysql_free_result(result);
  }
}


OProposition::OProposition(MYSQL *const mysql, schwa::Pool &pool, const char *const id, ODocument &doc) :
    pred_nodes(decltype(pred_nodes)::allocator_type(pool)),
    arg_groups(decltype(arg_groups)::allocator_type(pool)),
    link_groups(decltype(link_groups)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;
  unsigned int sentence_index = 0, token_index = 0;
  std::string predicate_id;

  // Load the info about this proposition and its predicate.
  ss << "SELECT x.encoded_proposition, x.quality, y.type, y.sentence_index, y.token_index, y.lemma, y.pb_sense_num, y.id FROM proposition x JOIN predicate y ON x.id = y.proposition_id WHERE x.id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    clone_str_from_row(&this->encoded, row[0], lengths[0], pool);
    clone_str_from_row(&this->quality, row[1], lengths[1], pool);
    clone_str_from_row(&this->type, row[2], lengths[2], pool);
    sentence_index = std::atoi(row[3]);
    token_index = std::atoi(row[4]);
    clone_str_from_row(&this->lemma, row[5], lengths[5], pool);
    clone_str_from_row(&this->pb_sense_num, row[6], lengths[6], pool);
    predicate_id = std::string(row[7], lengths[7]);
  }
  mysql_free_result(result);
  assert(nrows == 1);

  OSentence *const sentence = doc.sentences[sentence_index];
  this->leaf = sentence->parse->nth_leaf(token_index);

  // Fetch the predicate nodes.
  ss.str("");
  ss << "SELECT node_id FROM predicate_node WHERE predicate_id = \"" << predicate_id << "\" ORDER BY index_in_parent";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  while ((row = mysql_fetch_row(result))) {
    lengths = mysql_fetch_lengths(result);

    OPropNode *part = new (pool) OPropNode();
    clone_str_from_row(&part->encoded, row[0], lengths[0], pool);
    part->node = sentence->node_by_propbank_id(row[0]);
    pred_nodes.push_back(part);
  }
  mysql_free_result(result);

  // Fetch the arguments.
  std::vector<std::pair<unsigned int, char *>> arg_data;
  ss.str("");
  ss << "SELECT argument_analogue_index, type FROM argument WHERE proposition_id = \"" << id << "\" GROUP BY argument_analogue_index ORDER BY argument_analogue_index";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  while ((row = mysql_fetch_row(result))) {
    lengths = mysql_fetch_lengths(result);
    char *type;
    clone_str_from_row(&type, row[1], lengths[1], pool);
    arg_data.push_back(std::pair<unsigned int, char *>(std::atoi(row[0]), type));
  }
  mysql_free_result(result);
  for (auto &pair : arg_data) {
    OPropArgGroup *group = new (pool) OPropArgGroup(mysql, pool, id, *sentence, pair.first, pair.second);
    arg_groups.push_back(group);
  }

  // Fetch the links.
  std::vector<std::tuple<unsigned int, char *, char *>> link_data;
  ss.str("");
  ss << "SELECT link_analogue_index, type, associated_argument_id FROM proposition_link WHERE proposition_id =\"" << id << "\" GROUP BY link_analogue_index ORDER BY link_analogue_index";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  while ((row = mysql_fetch_row(result))) {
    lengths = mysql_fetch_lengths(result);
    char *type, *associated_argument_id;
    clone_str_from_row(&type, row[1], lengths[1], pool);
    clone_str_from_row(&associated_argument_id, row[2], lengths[2], pool);
    link_data.push_back(std::tuple<unsigned int, char *, char *>(std::atoi(row[0]), type, associated_argument_id));
  }
  mysql_free_result(result);
  for (auto &tuple : link_data) {
    OPropLinkGroup *group = new (pool) OPropLinkGroup(mysql, pool, id, *sentence, std::get<0>(tuple), std::get<1>(tuple), std::get<2>(tuple));
    link_groups.push_back(group);
  }
}


// ============================================================================
// OCorefMention
// ============================================================================
OCorefMention::OCorefMention(MYSQL *const mysql, schwa::Pool &pool, const char *const id, ODocument &doc) : start_leaf(nullptr), end_leaf(nullptr) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;
  unsigned int sentence_index = 0, start_index = 0, end_index = 0;

  // Load the mention data.
  ss << "SELECT type, sentence_index, start_token_index, end_token_index, start_char_offset, end_char_offset FROM coreference_link WHERE id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    clone_str_from_row(&this->type, row[0], lengths[0], pool);
    sentence_index = std::atoi(row[1]);
    start_index = std::atoi(row[2]);
    end_index = std::atoi(row[3]);
    this->start_offset = std::atoi(row[4]);
    this->end_offset = std::atoi(row[5]);
  }
  mysql_free_result(result);
  assert(nrows == 1);

  std::vector<OParseNode *> ordered_leaves;
  doc.sentences[sentence_index]->parse->ordered_leaves(ordered_leaves);
  for (unsigned int i = 0; i != ordered_leaves.size(); ++i) {
    if (i == start_index)
      this->start_leaf = ordered_leaves[i];
    if (i == end_index) {
      this->end_leaf = ordered_leaves[i];
      break;
    }
  }
  assert(this->start_leaf != nullptr);
  assert(this->end_leaf != nullptr);
}


// ============================================================================
// OCorefChain
// ============================================================================
OCorefChain::OCorefChain(MYSQL *const mysql, schwa::Pool &pool, const char *const id, ODocument &doc) :
    mentions(decltype(mentions)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  // Load the chain data.
  ss << "SELECT number, section, type, speaker FROM coreference_chain WHERE id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    clone_str_from_row(&this->identifier, row[0], lengths[0], pool);
    this->section = atoi(row[1]);
    clone_str_from_row(&this->type, row[2], lengths[2], pool);
    clone_str_from_row(&this->speaker_name, row[3], lengths[3], pool);
  }
  mysql_free_result(result);
  assert(nrows == 1);

  // Ensure speaker object exists.
  if (this->speaker_name == nullptr)
    this->speaker = nullptr;
  else {
    if (doc.speakers.find(this->speaker_name) == doc.speakers.end()) {
      char *unknown = (char *)"unknown";  // Dirty but safe in this situation.
      OSpeaker *speaker = new (pool) OSpeaker(this->speaker_name, unknown, unknown);
      doc.speakers.emplace(this->speaker_name, speaker);
    }
    this->speaker = doc.speakers[this->speaker_name];
  }

  // Find and load the mentions for this chain.
  std::vector<std::string> ids;
  ss.str("");
  ss << "SELECT id FROM coreference_link WHERE coreference_chain_id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  for (auto &id : ids) {
    OCorefMention *mention = new (pool) OCorefMention(mysql, pool, id.c_str(), doc);
    mentions.push_back(mention);
  }
}


// ============================================================================
// OParseNode
// ============================================================================
OParseNode::OParseNode(MYSQL *const mysql, schwa::Pool &pool, const char *const id, OParseNode *const parent) :
    parent(parent),
    syntactic_link(nullptr),
    children(decltype(children)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  // Load the node data.
  ss << "SELECT id, word, coref_section, syntactic_link_type, tag, part_of_speech, phrase_type, function_tag_id FROM tree WHERE id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    clone_str_from_row(&this->tree_id, row[0], lengths[0], pool);
    clone_str_from_row(&this->word, row[1], lengths[1], pool);
    this->coref_section = atoi(row[2]);
    clone_str_from_row(&this->syntactic_link_type, row[3], lengths[3], pool);
    clone_str_from_row(&this->tag, row[4], lengths[4], pool);
    clone_str_from_row(&this->pos, row[5], lengths[5], pool);
    clone_str_from_row(&this->phrase_type, row[6], lengths[6], pool);
    clone_str_from_row(&this->function_tags, row[7], lengths[7], pool);
  }
  mysql_free_result(result);
  assert(nrows == 1);

  // Find and load the children of the node.
  std::vector<std::string> ids;
  ss.str("");
  ss << "SELECT id FROM tree WHERE parent_id = \"" << tree_id << "\" ORDER BY child_index";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  for (auto &id : ids) {
    OParseNode *child = new (pool) OParseNode(mysql, pool, id.c_str(), this);
    children.push_back(child);
  }

  // Extract only the useful information from function_tag_id.
  if (function_tags != nullptr) {
    for (char *ptr = function_tags; *ptr; ++ptr) {
      if (*ptr == '@') {
        *ptr = '\0';
        break;
      }
    }
  }
}


void
OParseNode::all_nodes(node_map &nodes) {
  nodes.emplace(tree_id, this);
  for (auto &child : children)
    child->all_nodes(nodes);
}


void
OParseNode::all_nodes(std::vector<const OParseNode *> &nodes) const {
  nodes.push_back(this);
  for (auto &child : children)
    child->all_nodes(nodes);
}


void
OParseNode::ordered_leaves(std::vector<OParseNode *> &leaves) {
  if (is_leaf())
    leaves.push_back(this);
  else
    for (auto &child : children)
      child->ordered_leaves(leaves);
}


void
OParseNode::ordered_leaves(std::vector<const OParseNode *> &leaves) const {
  if (is_leaf())
    leaves.push_back(this);
  else
    for (auto &child : children)
      child->ordered_leaves(leaves);
}


OParseNode *
OParseNode::nth_leaf(unsigned int n) {
  unsigned int found = 0;
  return nth_leaf(n, found);
}


OParseNode *
OParseNode::nth_leaf(unsigned int n, unsigned int &found) {
  if (is_leaf()) {
    if (found == n)
      return this;
    ++found;
  }
  else {
    for (auto &child : children) {
      OParseNode *node = child->nth_leaf(n, found);
      if (node != nullptr)
        return node;
    }
  }
  return nullptr;
}


// ============================================================================
// OSentence
// ============================================================================
OSentence::OSentence(MYSQL *const mysql, schwa::Pool &pool, const char *const id) :
    start_time(0),
    end_time(0),
    tokens(decltype(tokens)::allocator_type(pool)),
    nes(decltype(nes)::allocator_type(pool)),
    speakers(decltype(speakers)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  // Load the sentence data.
  ss << "SELECT string FROM sentence WHERE id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  char *string = nullptr;
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    clone_str_from_row(&string, row[0], lengths[0], pool);
  }
  mysql_free_result(result);
  assert(nrows == 1);

  // Partition the string into tokens.
  for (char *ptr = string; ; ) {
    tokens.push_back(ptr);
    while (*ptr != '\0' && *ptr != ' ')
      ++ptr;
    if (*ptr == '\0')
      break;
    *ptr++ = '\0';
  }

  // Load the parse tree.
  parse = new (pool) OParseNode(mysql, pool, id, nullptr);

  // Find and load any syntactic links.
  node_map nodes_by_id;
  parse->all_nodes(nodes_by_id);
  for (auto &pair : nodes_by_id) {
    if (pair.second->syntactic_link_type == nullptr)
      continue;
    ss.str("");
    ss << "SELECT identity_subtree_id FROM syntactic_link WHERE reference_subtree_id = \"" << pair.first << "\"";
    query = ss.str();
    db_query(mysql, query.c_str());
    result = mysql_use_result(mysql);
    for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
      lengths = mysql_fetch_lengths(result);
      const std::string id(row[0], lengths[0]);
      pair.second->syntactic_link = nodes_by_id[id.c_str()];
    }
    mysql_free_result(result);
    assert(nrows < 2);
  }
}


OParseNode *
OSentence::node_by_propbank_id(const char *const id) {
  unsigned int token_index = 0, height = 0;
  const char *c;
  for (c = id; *c != ':'; ++c)
    token_index = (10 * token_index) + (*c - '0');
  ++c;
  for ( ; *c; ++c)
    height = (10 * height) + (*c - '0');

  OParseNode *node = parse->nth_leaf(token_index);
  for (unsigned int i = 0; i != height; ++i)
    node = node->parent;
  return node;
}


// ============================================================================
// ODocument
// ============================================================================
ODocument::ODocument(MYSQL *const mysql, schwa::Pool &pool, const char *const id) :
    sentences(decltype(sentences)::allocator_type(pool)),
    coref_chains(decltype(coref_chains)::allocator_type(pool)),
    propositions(decltype(propositions)::allocator_type(pool)),
    speakers(10, decltype(speakers)::hasher(), decltype(speakers)::key_equal(), decltype(speakers)::allocator_type(pool)) {
  std::stringstream ss;
  std::string query;
  MYSQL_RES *result;
  MYSQL_ROW row;
  unsigned long *lengths;
  unsigned int nrows;

  // Load the document data.
  ss.str("");
  ss << "SELECT id, subcorpus_id, lang_id, genre, source FROM document WHERE id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    clone_str_from_row(&this->doc_id, row[0], lengths[0], pool);
    clone_str_from_row(&this->subcorpus_id, row[1], lengths[1], pool);
    clone_str_from_row(&this->lang, row[2], lengths[2], pool);
    clone_str_from_row(&this->genre, row[3], lengths[3], pool);
    clone_str_from_row(&this->source, row[4], lengths[4], pool);
  }
  mysql_free_result(result);
  assert(nrows == 1);

  // Load the sentence parse trees.
  std::vector<std::string> ids;
  ss.str("");
  ss << "SELECT id FROM sentence WHERE document_id = \"" << id << "\" ORDER BY sentence_index";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  assert(nrows > 0);
  for (auto &id : ids) {
    OSentence *sentence = new (pool) OSentence(mysql, pool, id.c_str());
    sentences.push_back(sentence);
  }

  // Load the named entities.
  ss.str("");
  ss << "SELECT sentence_index, start_word_index, end_word_index, start_char_offset, end_char_offset, type FROM name_entity WHERE document_id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    ONamedEntity *const ne = new (pool) ONamedEntity();
    unsigned int sentence_index;

    lengths = mysql_fetch_lengths(result);
    sentence_index = atoi(row[0]);
    ne->token_index = atoi(row[1]);
    ne->length = (atoi(row[2]) - ne->token_index) + 1;
    ne->start_offset = atoi(row[3]);
    ne->end_offset = atoi(row[4]);
    clone_str_from_row(&ne->tag, row[5], lengths[5], pool);

    sentences[sentence_index]->nes.push_back(ne);
  }
  mysql_free_result(result);

  // Find and load any speaker text information.
  ss.str("");
  ss << "SELECT line_number, start_time, stop_time, name, gender, competence FROM speaker_sentence WHERE document_id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  char *unknown = (char *)pool.alloc(8);
  std::strcpy(unknown, "unknown");
  while ((row = mysql_fetch_row(result))) {
    unsigned int line_number;
    char *orig_names, *orig_genders, *competence;

    lengths = mysql_fetch_lengths(result);
    line_number = atoi(row[0]);
    OSentence *const sentence = sentences[line_number];
    sentence->start_time = (lengths[1] == 0) ? 0 : atof(row[1]);
    sentence->end_time = (lengths[2] == 0) ? 0 : atof(row[2]);
    clone_str_from_row(&orig_names, row[3], lengths[3], pool);
    clone_str_from_row(&orig_genders, row[4], lengths[4], pool);
    clone_str_from_row(&competence, row[5], lengths[5], pool);

    std::vector<char *> names, genders;
    for (char *c = orig_names; ; ) {
      names.push_back(c);
      while (*c != '\0' && *c != ',')
        ++c;
      if (*c == '\0')
        break;
      *c++ = '\0';
    }
    for (char *c = orig_genders; ; ) {
      genders.push_back(c);
      while (*c != '\0' && *c != ',')
        ++c;
      if (*c == '\0')
        break;
      *c++ = '\0';
    }
    assert(names.size() == genders.size() || genders.size() == 1);

    for (size_t i = 0; i != names.size(); ++i) {
      if (speakers.find(names[i]) == speakers.end()) {
        char *gender = (genders.size() == 1) ? genders[0] : genders[i];
        OSpeaker *speaker = new (pool) OSpeaker(names[i], gender, competence);
        speakers.emplace(names[i], speaker);
      }
      sentence->speakers.push_back(speakers[names[i]]);
    }

  }
  mysql_free_result(result);

  // Find and load any coref chains and their mentions.
  ids.clear();
  ss.str("");
  ss << "SELECT id FROM coreference_chain WHERE document_id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  for (auto &id : ids) {
    OCorefChain *chain = new (pool) OCorefChain(mysql, pool, id.c_str(), *this);
    coref_chains.push_back(chain);
  }

  // Find and load any propositions.
  ids.clear();
  ss.str("");
  ss << "SELECT id FROM proposition WHERE document_id = \"" << id << "\"";
  query = ss.str();
  db_query(mysql, query.c_str());
  result = mysql_use_result(mysql);
  for (nrows = 0; (row = mysql_fetch_row(result)); ++nrows) {
    lengths = mysql_fetch_lengths(result);
    ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  for (auto &id : ids) {
    OProposition *proposition = new (pool) OProposition(mysql, pool, id.c_str(), *this);
    propositions.push_back(proposition);
  }
}


void
find_documents(MYSQL *const mysql, std::vector<std::string> &doc_ids) {
  db_query(mysql, "SELECT id FROM document");
  MYSQL_RES *const result = mysql_use_result(mysql);
  MYSQL_ROW row;
  unsigned long *lengths;
  while ((row = mysql_fetch_row(result))) {
    lengths = mysql_fetch_lengths(result);
    doc_ids.push_back(std::string(row[0], lengths[0]));
  }
  mysql_free_result(result);
  std::clog << "Found " << doc_ids.size() << " documents." << std::endl;
}

}  // namespace ontonotes5
