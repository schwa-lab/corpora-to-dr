# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
from __future__ import absolute_import, print_function, unicode_literals
import itertools


class ODocument(object):
  """
  mysql> DESC document;
  +--------------+--------------+------+-----+---------+-------+
  | Field        | Type         | Null | Key | Default | Extra |
  +--------------+--------------+------+-----+---------+-------+
  | id           | varchar(255) | NO   | PRI | NULL    |       |
  | subcorpus_id | varchar(255) | NO   |     | NULL    |       |
  | lang_id      | varchar(16)  | NO   |     | NULL    |       |
  | genre        | varchar(16)  | NO   |     | NULL    |       |
  | source       | varchar(16)  | NO   |     | NULL    |       |
  | text         | longtext     | NO   |     | NULL    |       |
  +--------------+--------------+------+-----+---------+-------+
  mysql> DESC name_entity;
  +-------------------+--------------+------+-----+---------+-------+
  | Field             | Type         | Null | Key | Default | Extra |
  +-------------------+--------------+------+-----+---------+-------+
  | id                | varchar(255) | NO   | PRI | NULL    |       |
  | type              | varchar(255) | NO   |     | NULL    |       |
  | document_id       | varchar(255) | NO   | MUL | NULL    |       |
  | sentence_index    | int(11)      | NO   |     | NULL    |       |
  | start_word_index  | int(11)      | NO   |     | NULL    |       |
  | end_word_index    | int(11)      | NO   |     | NULL    |       |
  | start_char_offset | int(11)      | NO   |     | NULL    |       |
  | end_char_offset   | int(11)      | NO   |     | NULL    |       |
  | subtree_id        | varchar(255) | YES  | MUL | NULL    |       |
  | string            | longtext     | YES  |     | NULL    |       |
  +-------------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('doc_id', 'subcorpus_id', 'lang_id', 'genre', 'source', 'sentences', 'speakers', 'coref_chains', 'propositions')

  def __init__(self, conn, doc_id):
    # Load the document data.
    cur = conn.cursor()
    cur.execute('SELECT id, subcorpus_id, lang_id, genre, source FROM document WHERE id = %s', doc_id)
    assert cur.rowcount == 1, ('ODocument rowcount == 1', cur.rowcount)
    for attr, value in itertools.izip(('doc_id', 'subcorpus_id', 'lang_id', 'genre', 'source'), cur.fetchone()):
      setattr(self, attr, value)

    # Load the sentence parse trees.
    self.sentences = []
    cur.execute('SELECT id, string FROM sentence WHERE document_id = %s ORDER BY sentence_index', self.doc_id)
    for row in cur:
      node = OSentence(conn, *row)
      self.sentences.append(node)
    self.sentences = tuple(self.sentences)

    # Load the named entities.
    cur.execute('SELECT type, sentence_index, start_word_index, end_word_index, start_char_offset, end_char_offset FROM name_entity WHERE document_id = %s', self.doc_id)
    for ne_type, sent_index, start_index, end_index, start_char_offset, end_char_offset in cur:
      self.sentences[sent_index].add_ne(start_index, end_index, ne_type, start_char_offset, end_char_offset)

    # Find and load any spoken text information.
    self.speakers = {}  # { name : (gender, competence) }
    cur.execute('SELECT line_number, start_time, stop_time, name, gender, competence FROM speaker_sentence WHERE document_id = %s', self.doc_id)
    for line_number, start_time, end_time, names, genders, competence in cur:
      names = names.split(',')
      genders = genders.split(',')
      if len(genders) == 1 and len(names) != 1:  # If only one gender was provided, assume it is the same for each speaker. This appears to be the case when gender is "unknown".
        genders = [genders[0]] * len(names)
      assert len(names) == len(genders), ('len(names) == len(genders)', doc_id, len(names), len(genders), names, genders)
      speakers = []
      for name, gender in itertools.izip(names, genders):
        if name not in self.speakers:
          self.speakers[name] = (gender, competence)
        speakers.append(name)
      self.sentences[line_number].set_spoken(start_time, end_time, tuple(speakers))

    # Find and load any coref chains (and mentions).
    self.coref_chains = []
    cur.execute('SELECT id FROM coreference_chain WHERE document_id = %s', self.doc_id)
    for row in cur:
      chain = OCorefChain(conn, self, row[0])
      self.coref_chains.append(chain)

    # Find and load any propositions.
    self.propositions = []
    cur.execute('SELECT id FROM proposition WHERE document_id = %s', self.doc_id)
    proposition_ids = [row[0] for row in cur]
    for proposition_id in proposition_ids:
      prop = OProposition(cur, self, proposition_id)
      self.propositions.append(prop)

    cur.close()

  def __str__(self):
    return 'doc={} sentence_count={}'.format(self.doc_id, self.sentence_count())

  def sentence_count(self):
    return len(self.sentences)


class OSentence(object):
  """
  mysql> DESC sentence;
  +-----------------+--------------+------+-----+---------+-------+
  | Field           | Type         | Null | Key | Default | Extra |
  +-----------------+--------------+------+-----+---------+-------+
  | id              | varchar(255) | NO   | PRI | NULL    |       |
  | sentence_index  | int(11)      | NO   |     | NULL    |       |
  | document_id     | varchar(255) | NO   |     | NULL    |       |
  | string          | longtext     | NO   |     | NULL    |       |
  | no_trace_string | longtext     | NO   |     | NULL    |       |
  +-----------------+--------------+------+-----+---------+-------+
  mysql> DESC speaker_sentence;
  +-------------+--------------+------+-----+---------+-------+
  | Field       | Type         | Null | Key | Default | Extra |
  +-------------+--------------+------+-----+---------+-------+
  | id          | varchar(255) | NO   | PRI | NULL    |       |
  | line_number | int(11)      | NO   |     | NULL    |       | # sentence number in the document
  | document_id | varchar(255) | NO   | MUL | NULL    |       |
  | start_time  | double       | NO   |     | NULL    |       | # milliseconds, floating point
  | stop_time   | double       | NO   |     | NULL    |       | # as above
  | name        | varchar(255) | NO   |     | NULL    |       | # multiple, comma separated
  | gender      | varchar(255) | NO   |     | NULL    |       | # as above
  | competence  | varchar(255) | NO   |     | NULL    |       | # singleton, assumed to be the same when multiple names
  +-------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('tokens', 'parse', 'nes', 'start_time', 'end_time', 'speakers')

  def __init__(self, conn, root_tree_id, string):
    cur = conn.cursor()
    self.tokens = tuple(string.split())
    self.parse = OParseNode(cur, root_tree_id, None)
    self.nes = []
    self.start_time = self.end_time = self.speakers = None

    # Ensure we're reconstructing these tokens correctly.
    a = ' '.join(self.tokens)
    b = ' '.join(n.word for n in self.parse.ordered_leaves())
    assert a == b, ('OSentence token reconstruction', self.parse.tree_id, a, b)

    # Find and load any syntactic links.
    nodes_by_id = {n.tree_id: n for n in self.parse.all_nodes()}
    for n in self.parse.all_nodes():
      if n.syntactic_link_type is not None:
        cur.execute('SELECT type, identity_subtree_id FROM syntactic_link WHERE reference_subtree_id = %s', n.tree_id)
        if cur.rowcount == 1:
          link_type, tree_id = cur.fetchone()
          n.syntactic_link = nodes_by_id[tree_id]
        elif cur.rowcount != 0:
          assert cur.rowcount < 2, ('syntactic_link count', n.tree_id)
    del nodes_by_id

    cur.close()

  def __str__(self):
    return '(parse={} ne_count={})'.format(self.parse, self.ne_count())

  def add_ne(self, start_index, end_index, ne_type, start_char_offset, end_char_offset):
    if self.nes is None:
      self.nes = []  # [ (inclusive, length, tag, start char offset, end char offset) ]
    self.nes.append((start_index, end_index - start_index + 1, ne_type, start_char_offset, end_char_offset))

  def get_node_by_propbank_node_id(self, node_id):
    token_index, height = map(int, node_id.split(':'))
    for i, node in enumerate(self.parse.ordered_leaves()):
      if i == token_index:
        break
    else:
      raise ValueError('Could not find token number {}'.format(token_index))
    for i in xrange(height):
      node = node.parent
    return node

  def set_spoken(self, start_time, end_time, speakers):
    self.start_time = start_time
    self.end_time = end_time
    self.speakers = speakers

  def ne_count(self):
    return 0 if self.nes is None else len(self.nes)


# =============================================================================
# Parse Nodes
# =============================================================================
class OParseNode(object):
  """
  mysql> DESC tree;
  +---------------------+--------------+------+-----+---------+-------+
  | Field               | Type         | Null | Key | Default | Extra |
  +---------------------+--------------+------+-----+---------+-------+
  | id                  | varchar(255) | NO   | PRI | NULL    |       |
  | parent_id           | varchar(255) | YES  | MUL | NULL    |       |
  | document_id         | varchar(255) | YES  | MUL | NULL    |       |
  | word                | varchar(255) | YES  |     | NULL    |       |
  | child_index         | int(11)      | YES  |     | NULL    |       |
  | start               | int(11)      | NO   |     | NULL    |       |
  | end                 | int(11)      | NO   |     | NULL    |       |
  | coref_section       | int(11)      | NO   |     | NULL    |       |
  | syntactic_link_type | varchar(255) | YES  | MUL | NULL    |       |
  | tag                 | varchar(255) | NO   |     | NULL    |       |
  | part_of_speech      | varchar(255) | YES  | MUL | NULL    |       |
  | phrase_type         | varchar(255) | YES  | MUL | NULL    |       |
  | function_tag_id     | varchar(255) | YES  | MUL | NULL    |       |
  | string              | longtext     | YES  |     | NULL    |       |
  | no_trace_string     | longtext     | YES  |     | NULL    |       |
  | parse               | longtext     | YES  |     | NULL    |       |
  +---------------------+--------------+------+-----+---------+-------+
  mysql> DESC syntactic_link;
  +----------------------+--------------+------+-----+---------+-------+
  | Field                | Type         | Null | Key | Default | Extra |
  +----------------------+--------------+------+-----+---------+-------+
  | id                   | varchar(255) | NO   |     | NULL    |       |
  | type                 | varchar(255) | NO   |     | NULL    |       |
  | word                 | varchar(255) | YES  |     | NULL    |       |
  | reference_subtree_id | varchar(255) | NO   | MUL | NULL    |       |
  | identity_subtree_id  | varchar(255) | NO   | MUL | NULL    |       |
  +----------------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('tree_id', 'word', 'coref_section', 'syntactic_link_type', 'tag', 'part_of_speech', 'phrase_type', 'function_tag_id', 'parent', 'children', 'syntactic_link', 'function_tags')

  def __init__(self, cur, tree_id, parent):
    # Load the node data.
    cur.execute('SELECT id, word, coref_section, syntactic_link_type, tag, part_of_speech, phrase_type, function_tag_id FROM tree WHERE id = %s', tree_id)
    assert cur.rowcount == 1, ('OParseNode rowcount == 1', cur.rowcount)
    for attr, value in itertools.izip(('tree_id', 'word', 'coref_section', 'syntactic_link_type', 'tag', 'part_of_speech', 'phrase_type', 'function_tag_id'), cur.fetchone()):
      setattr(self, attr, value)
    self.parent = parent
    self.syntactic_link = None

    # Find and load the children of the node.
    cur.execute('SELECT id FROM tree WHERE parent_id = %s ORDER BY child_index', self.tree_id)
    if cur.rowcount != 0:
      self.children = []
      child_ids = [row[0] for row in cur]
      for child_id in child_ids:
        child = OParseNode(cur, child_id, self)
        self.children.append(child)
      self.children = tuple(self.children)
    else:
      self.children = None

    # Extract only the useful part of the function_tag_id.
    self.function_tags = None
    if self.function_tag_id is not None:
      function_tags, tree_id = self.function_tag_id.split('@', 1)
      assert tree_id == self.tree_id, ('function_tag_id sanity check', tree_id, self.tree_id)
      self.function_tags = function_tags

  def __repr__(self):
    return 'OParseNode{}'.format(self.__str__())

  def __str__(self):
    if self.is_leaf():
      return '({} {} {})'.format(self.tag, self.part_of_speech, self.word)
    else:
      return self.tag

  def all_nodes(self):
    yield self
    if not self.is_leaf():
      for child in self.children:
        for n in child.all_nodes():
          yield n

  def is_leaf(self):
    return self.children is None

  def is_trace(self):
    return self.tag == '-NONE-'

  def ordered_leaves(self, include_trace=True):
    if self.is_leaf():
      if not self.is_trace() or include_trace:
        yield self
    else:
      for child in self.children:
        for n in child.ordered_leaves():
          yield n


# =============================================================================
# Coreference
# =============================================================================
class OCorefMention(object):
  """
  mysql> DESC coreference_link;
  +----------------------+--------------+------+-----+---------+-------+
  | Field                | Type         | Null | Key | Default | Extra |
  +----------------------+--------------+------+-----+---------+-------+
  | id                   | varchar(255) | NO   | PRI | NULL    |       |
  | type                 | varchar(255) | NO   | MUL | NULL    |       |
  | coreference_chain_id | varchar(255) | NO   | MUL | NULL    |       |
  | sentence_index       | int(11)      | NO   |     | NULL    |       |
  | start_token_index    | int(11)      | NO   |     | NULL    |       |
  | end_token_index      | int(11)      | NO   |     | NULL    |       |
  | start_char_offset    | int(11)      | NO   |     | NULL    |       |
  | end_char_offset      | int(11)      | NO   |     | NULL    |       |
  | subtree_id           | varchar(255) | YES  | MUL | NULL    |       |
  | string               | longtext     | YES  |     | NULL    |       |
  +----------------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('start_leaf', 'end_leaf', 'type', 'start_offset', 'end_offset')

  def __init__(self, doc, type, sentence_index, start_token_index, end_token_index, start_char_offset, end_char_offset):
    self.start_offset = start_char_offset
    self.end_offset = end_char_offset
    self.type = type
    for i, leaf in enumerate(doc.sentences[sentence_index].parse.ordered_leaves()):
      if i == start_token_index:
        self.start_leaf = leaf
      if i == end_token_index:
        self.end_leaf = leaf
        break
    assert self.start_leaf is not None, ('OCorefMention start_leaf not found', doc.doc_id, sentence_index, start_token_index, end_token_index)
    assert self.end_leaf is not None, ('OCorefMention end_leaf not found', doc.doc_id, sentence_index, start_token_index, end_token_index)


class OCorefChain(object):
  """
  mysql> DESC coreference_chain;
  +-------------+--------------+------+-----+---------+-------+
  | Field       | Type         | Null | Key | Default | Extra |
  +-------------+--------------+------+-----+---------+-------+
  | id          | varchar(255) | NO   | PRI | NULL    |       |
  | number      | varchar(128) | NO   |     | NULL    |       |
  | section     | varchar(16)  | YES  |     | NULL    |       |
  | document_id | varchar(255) | NO   | MUL | NULL    |       |
  | type        | varchar(16)  | NO   |     | NULL    |       |
  | speaker     | varchar(256) | NO   | MUL | NULL    |       |
  +-------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('identifier', 'section', 'type', 'speaker', 'mentions')

  def __init__(self, conn, doc, chain_id):
    cur = conn.cursor()
    cur.execute('SELECT number, section, type, speaker FROM coreference_chain WHERE id = %s', chain_id)
    assert cur.rowcount == 1, ('OCorefChain rowcount', cur.rowcount)
    for attr, value in itertools.izip(('identifier', 'section', 'type', 'speaker'), cur.fetchone()):
      setattr(self, attr, value)
    self.section = int(self.section)  # Why on earth is this stored in the DB as a string...?

    # Correct the speaker attribute to point to the unified OSpeaker object.
    if not self.speaker:
      self.speaker = None
    elif self.speaker not in doc.speakers:
     doc.speakers[self.speaker] = ('unknown', 'unknown')

    # Load up the mentions associated with this chain.
    self.mentions = []
    cur.execute('SELECT type, sentence_index, start_token_index, end_token_index, start_char_offset, end_char_offset FROM coreference_link WHERE coreference_chain_id = %s', chain_id)
    for row in cur:
      mention = OCorefMention(doc, *row)
      self.mentions.append(mention)

    cur.close()


# =============================================================================
# Propositions
# =============================================================================
class OPropArgGroup(object):
  """
  The ArgGroup is used to remove one level of denormalisation done by OntoNotes.
  mysql> DESC argument;
  +-------------------------+--------------+------+-----+---------+-------+
  | Field                   | Type         | Null | Key | Default | Extra |
  +-------------------------+--------------+------+-----+---------+-------+
  | id                      | varchar(255) | NO   | PRI | NULL    |       |
  | argument_analogue_index | int(11)      | NO   |     | NULL    |       |
  | type                    | varchar(255) | YES  | MUL | NULL    |       |
  | index_in_parent         | int(11)      | YES  |     | NULL    |       |
  | split_argument_flag     | int(11)      | YES  |     | NULL    |       |
  | argument_subtype        | varchar(255) | YES  |     | NULL    |       |
  | proposition_id          | varchar(255) | YES  | MUL | NULL    |       |
  +-------------------------+--------------+------+-----+---------+-------+
  mysql> DESC argument_node;
  +-----------------+--------------+------+-----+---------+-------+
  | Field           | Type         | Null | Key | Default | Extra |
  +-----------------+--------------+------+-----+---------+-------+
  | id              | varchar(255) | NO   | PRI | NULL    |       |
  | argument_id     | varchar(255) | NO   | MUL | NULL    |       |
  | node_id         | varchar(255) | NO   |     | NULL    |       |
  | index_in_parent | int(11)      | YES  |     | NULL    |       |
  +-----------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('type', 'arguments')

  def __init__(self, cur, sentence, proposition_id, index, type):
    self.type = type
    self.arguments = []  # [ [(node_id, node)] ]. Outer is "*", inner is "," for split arguments.
    cur.execute('SELECT id FROM argument WHERE proposition_id = %s AND argument_analogue_index = %s ORDER BY index_in_parent', (proposition_id, index))
    assert cur.rowcount != 0, ('OPropArgGroup rowcount', proposition_id, index)
    argument_ids = tuple(row[0] for row in cur)
    for argument_id in argument_ids:
      argument = []
      cur.execute('SELECT node_id FROM argument_node WHERE argument_id = %s', argument_id)
      for node_id, in cur:
        node = sentence.get_node_by_propbank_node_id(node_id)
        argument.append((node_id, node))
      self.arguments.append(argument)


class OPropLinkGroup(object):
  """
  mysql> DESC proposition_link;
  +------------------------+--------------+------+-----+---------+-------+
  | Field                  | Type         | Null | Key | Default | Extra |
  +------------------------+--------------+------+-----+---------+-------+
  | id                     | varchar(255) | NO   | PRI | NULL    |       |
  | index_in_parent        | int(11)      | NO   |     | NULL    |       |
  | link_analogue_index    | int(11)      | NO   |     | NULL    |       |
  | type                   | varchar(255) | YES  |     | NULL    |       |
  | proposition_id         | varchar(255) | YES  | MUL | NULL    |       |
  | associated_argument_id | varchar(255) | YES  | MUL | NULL    |       |
  +------------------------+--------------+------+-----+---------+-------+
  mysql> DESC link_node;
  +-----------------+--------------+------+-----+---------+-------+
  | Field           | Type         | Null | Key | Default | Extra |
  +-----------------+--------------+------+-----+---------+-------+
  | id              | varchar(255) | NO   | PRI | NULL    |       |
  | link_id         | varchar(255) | NO   | MUL | NULL    |       |
  | node_id         | varchar(255) | NO   |     | NULL    |       |
  | index_in_parent | int(11)      | YES  |     | NULL    |       |
  +-----------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('type', 'associated_argument_id', 'links')

  def __init__(self, cur, sentence, proposition_id, index, type, associated_argument_id):
    self.type = type
    self.associated_argument_id = associated_argument_id
    self.links = []  # [ [(node_id, node)] ]. Outer is "*", inner is "," for split arguments.
    cur.execute('SELECT id FROM proposition_link WHERE proposition_id = %s AND link_analogue_index = %s ORDER BY index_in_parent', (proposition_id, index))
    assert cur.rowcount != 0, ('OPropLinkGroup rowcount', proposition_id, index)
    link_ids = tuple(row[0] for row in cur)
    for link_id in link_ids:
      link = []
      cur.execute('SELECT node_id FROM link_node WHERE link_id = %s', link_id)
      for node_id, in cur:
        node = sentence.get_node_by_propbank_node_id(node_id)
        link.append((node_id, node))
      self.links.append(link)


class OProposition(object):
  """
  mysql> DESC proposition;
  +---------------------+--------------+------+-----+---------+-------+
  | Field               | Type         | Null | Key | Default | Extra |
  +---------------------+--------------+------+-----+---------+-------+
  | id                  | varchar(255) | NO   | PRI | NULL    |       |
  | document_id         | varchar(255) | NO   | MUL | NULL    |       |
  | encoded_proposition | text         | NO   |     | NULL    |       |
  | quality             | varchar(16)  | NO   |     | NULL    |       |
  +---------------------+--------------+------+-----+---------+-------+
  There is a 1-to-1 mapping between proposition and predicate.
  mysql> DESC predicate;
  +-----------------+--------------+------+-----+---------+-------+
  | Field           | Type         | Null | Key | Default | Extra |
  +-----------------+--------------+------+-----+---------+-------+
  | id              | varchar(255) | NO   | PRI | NULL    |       |
  | index_in_parent | int(11)      | NO   |     | NULL    |       |
  | proposition_id  | varchar(255) | YES  | MUL | NULL    |       |
  | type            | varchar(255) | YES  | MUL | NULL    |       |
  | sentence_index  | int(11)      | NO   |     | NULL    |       |
  | token_index     | int(11)      | YES  |     | NULL    |       |
  | lemma           | varchar(255) | YES  |     | NULL    |       |
  | pb_sense_num    | varchar(255) | YES  |     | NULL    |       |
  +-----------------+--------------+------+-----+---------+-------+
  mysql> DESC predicate_node;
  +-----------------+--------------+------+-----+---------+-------+
  | Field           | Type         | Null | Key | Default | Extra |
  +-----------------+--------------+------+-----+---------+-------+
  | id              | varchar(255) | NO   | PRI | NULL    |       |
  | predicate_id    | varchar(255) | YES  | MUL | NULL    |       |
  | node_id         | varchar(16)  | YES  |     | NULL    |       |
  | primary_flag    | int(11)      | YES  |     | NULL    |       |
  | index_in_parent | int(11)      | YES  |     | NULL    |       |
  +-----------------+--------------+------+-----+---------+-------+
  """
  __slots__ = ('encoded', 'quality', 'type', 'sentence_index', 'token_index', 'lemma', 'pb_sense_num', 'leaf', 'pred_nodes', 'arg_groups', 'link_groups')

  def __init__(self, cur, doc, proposition_id):
    # Fetch the info about this proposition and its predicate.
    cur.execute('SELECT x.encoded_proposition, x.quality, y.type, y.sentence_index, y.token_index, y.lemma, y.pb_sense_num, y.id FROM proposition x JOIN predicate y ON x.id = y.proposition_id WHERE x.id = %s', proposition_id)
    assert cur.rowcount == 1, ('OProposition rowcount', cur.rowcount, proposition_id)
    row = cur.fetchone()
    for attr, value in itertools.izip(('encoded', 'quality', 'type', 'sentence_index', 'token_index', 'lemma', 'pb_sense_num'), row):
      setattr(self, attr, value)
    sentence = doc.sentences[self.sentence_index]
    self.leaf = tuple(sentence.parse.ordered_leaves())[self.token_index]

    # Fetch the predicate nodes.
    self.pred_nodes = []  # [(node_id, node)]
    cur.execute('SELECT node_id FROM predicate_node WHERE predicate_id = %s ORDER BY index_in_parent', row[-1])
    assert cur.rowcount != 0, ('OProposition predicate rowcount', cur.rowcount, proposition_id, row[-1])
    for node_id, in cur:
      node = sentence.get_node_by_propbank_node_id(node_id)
      self.pred_nodes.append((node_id, node))

    # Fetch the arguments.
    self.arg_groups = []
    cur.execute('SELECT argument_analogue_index, type FROM argument WHERE proposition_id = %s GROUP BY argument_analogue_index ORDER BY argument_analogue_index', proposition_id)
    rows = tuple(cur)
    for row in rows:
      arg = OPropArgGroup(cur, sentence, proposition_id, *row)
      self.arg_groups.append(arg)

    # Fetch the links.
    self.link_groups = []
    cur.execute('SELECT link_analogue_index, type, associated_argument_id FROM proposition_link WHERE proposition_id = %s GROUP BY link_analogue_index ORDER BY link_analogue_index', proposition_id)
    rows = tuple(cur)
    for row in rows:
      link = OPropLinkGroup(cur, sentence, proposition_id, *row)
      self.link_groups.append(link)
