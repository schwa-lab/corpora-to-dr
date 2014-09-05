#ifndef MODELS_DB_H_
#define MODELS_DB_H_

#include <cstring>
#include <functional>
#include <unordered_map>
#include <vector>

#include <schwa/pool.h>

#include <mysql.h>


inline void *
operator new(std::size_t nbytes, schwa::Pool &pool) {
  return pool.alloc(nbytes);
}


template <class T>
struct pool_allocator {
  using const_pointer = const T *;
  using const_reference = const T &;
  using difference_type = std::ptrdiff_t;
  using pointer = T *;
  using reference = T &;
  using size_type = std::size_t;
  using value_type = T;
  template <class U> struct rebind { using other = pool_allocator<U>; };

  schwa::Pool &pool;

  pool_allocator(schwa::Pool &pool) noexcept : pool(pool) { }
  template <typename U> pool_allocator(const pool_allocator<U> &o) : pool(o.pool) { }

  inline T *
  allocate(const std::size_t n) {
    return static_cast<T *>(pool.alloc(n * sizeof(T)));
  }

  template <class U, class... Args>
  inline void
  construct (U* p, Args&&... args) {
    new (p) U(args...);
  }

  inline void deallocate(T *, std::size_t) { }

  template <class U>
  inline void
  destroy (U* p) {
    p->~U();
  }
};


struct cstr_equal_to : public std::binary_function<const char *, const char *, bool> {
  inline bool
  operator ()(const char *const a, const char *const b) const {
    return std::strcmp(a, b) == 0;
  }
};


struct cstr_hash : public std::unary_function<const char *, size_t> {
  inline size_t
  operator ()(const char *str) const {
    size_t hash = 5381;
    int c;
    while ((c = *str++))
      hash = ((hash << 5) + hash) + c;
    return hash;
  }
};


namespace ontonotes5 {

  class ODocument;
  class OParseNode;
  class OSentence;
  class OSpeaker;

  using node_map = std::unordered_map<const char *, OParseNode *, cstr_hash, cstr_equal_to>;


  class OSpeaker {
  public:
    char *name;
    char *gender;
    char *competence;

    OSpeaker(char *name, char *gender, char *competence) : name(name), gender(gender), competence(competence) { }
  };


  class OPropNode {
  public:
    char *encoded;
    OParseNode *node;

    OPropNode(void) { }
  };


  class OPropArg {
  public:
    std::vector<OPropNode *, pool_allocator<OPropNode *>> parts;

    OPropArg(schwa::Pool &pool);
  };


  class OPropArgGroup {
  public:
    char *type;
    std::vector<OPropArg *, pool_allocator<OPropArg *>> args;

    OPropArgGroup(MYSQL *mysql, schwa::Pool &pool, const char *id, OSentence &sentence, unsigned int index, char *type);
  };


  class OPropLink {
  public:
    std::vector<OPropNode *, pool_allocator<OPropNode *>> parts;

    OPropLink(schwa::Pool &pool);
  };


  class OPropLinkGroup {
  public:
    char *type;
    char *associated_argument_id;
    std::vector<OPropLink *, pool_allocator<OPropLink *>> links;

    OPropLinkGroup(MYSQL *mysql, schwa::Pool &pool, const char *id, OSentence &sentence, unsigned int index, char *type, char *associated_argument_id);
  };


  class OProposition {
  public:
    char *encoded;
    char *quality;
    char *type;
    char *lemma;
    char *pb_sense_num;
    OParseNode *leaf;
    std::vector<OPropNode *, pool_allocator<OPropNode *>> pred_nodes;
    std::vector<OPropArgGroup*, pool_allocator<OPropArgGroup *>> arg_groups;
    std::vector<OPropLinkGroup*, pool_allocator<OPropLinkGroup *>> link_groups;

    OProposition(MYSQL *mysql, schwa::Pool &pool, const char *id, ODocument &doc);
  };


  class OCorefMention {
  public:
    OParseNode *start_leaf;
    OParseNode *end_leaf;
    char *type;
    unsigned int start_offset;
    unsigned int end_offset;

    OCorefMention(MYSQL *mysql, schwa::Pool &pool, const char *id, ODocument &doc);
  };


  class OCorefChain {
  public:
    char *identifier;
    char *type;
    char *speaker_name;
    unsigned int section;
    OSpeaker *speaker;
    std::vector<OCorefMention *, pool_allocator<OCorefMention *>> mentions;

    OCorefChain(MYSQL *mysql, schwa::Pool &pool, const char *id, ODocument &doc);
  };


  class ONamedEntity {
  public:
    unsigned int token_index;
    unsigned int length;
    unsigned int start_offset;
    unsigned int end_offset;
    char *tag;

    ONamedEntity(void) { }
  };


  class OParseNode {
  public:
    char *tree_id;
    char *word;
    char *syntactic_link_type;
    char *tag;
    char *pos;
    char *phrase_type;
    char *function_tags;
    OParseNode *parent;
    OParseNode *syntactic_link;
    std::vector<OParseNode *, pool_allocator<OParseNode *>> children;
    int coref_section;

    OParseNode(MYSQL *mysql, schwa::Pool &pool, const char *id, OParseNode *parent);

    inline bool is_leaf(void) const { return children.empty(); }
    inline bool is_trace(void) const { return std::strcmp(tag, "-NONE-") == 0; }

    inline size_t
    nnodes(void) const {
      size_t n = 1;
      for (auto &child : children)
        n += child->nnodes();
      return n;
    }

    void all_nodes(node_map &nodes);
    void all_nodes(std::vector<const OParseNode *> &nodes) const;
    void ordered_leaves(std::vector<OParseNode *> &leaves);
    void ordered_leaves(std::vector<const OParseNode *> &leaves) const;

    OParseNode *nth_leaf(unsigned int n);

  private:
    OParseNode *nth_leaf(unsigned int n, unsigned int &found);
  };


  class OSentence {
  public:
    OParseNode *parse;
    double start_time;
    double end_time;
    std::vector<const char *, pool_allocator<const char *>> tokens;
    std::vector<ONamedEntity *, pool_allocator<ONamedEntity *>> nes;
    std::vector<OSpeaker *, pool_allocator<OSpeaker *>> speakers;

    OSentence(MYSQL *mysql, schwa::Pool &pool, const char *id);

    OParseNode *node_by_propbank_id(const char *id);
  };


  class ODocument {
  public:
    char *doc_id;
    char *subcorpus_id;
    char *lang;
    char *genre;
    char *source;
    std::vector<OSentence *, pool_allocator<OSentence *>> sentences;
    std::vector<OCorefChain *, pool_allocator<OCorefChain *>> coref_chains;
    std::vector<OProposition *, pool_allocator<OProposition *>> propositions;
    std::unordered_map<const char *, OSpeaker *, cstr_hash, cstr_equal_to, pool_allocator<std::pair<const char *, OSpeaker *>>> speakers;

    ODocument(MYSQL *mysql, schwa::Pool &pool, const char *id);
  };


  void find_documents(MYSQL *const mysql, std::vector<std::string> &doc_ids);
}

#endif  // MODELS_DB_H_
