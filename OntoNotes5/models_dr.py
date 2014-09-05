# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
from __future__ import absolute_import, print_function, unicode_literals
import itertools

from schwa import dr

__all__ = [
    'Token', 'Sentence', 'Doc',
    'Speaker',
    'ParseNode',
    'NamedEntity',
    'PropArg', 'PropArgGroup', 'PropArgPart', 'PropLink', 'PropLinkGroup', 'PropLinkPart', 'PropPredPart', 'Proposition',
    'CorefChain', 'CorefMention',
]


class Token(dr.Ann):
  span = dr.Slice()
  raw = dr.Text()


# =============================================================================
# Dialogue
# =============================================================================
class Speaker(dr.Ann):
  name = dr.Text()
  gender = dr.Text()
  competence = dr.Text()

  def __init__(self, *args, **kwargs):
    super(Speaker, self).__init__(*args, **kwargs)
    if self.name is None:
      self.name = ''


# =============================================================================
# Parse Trees
# =============================================================================
class ParseNode(dr.Ann):
  tag = dr.Text()
  pos = dr.Text()
  token = dr.Pointer(Token)
  phrase_type = dr.Field()
  function_tags = dr.Field()
  coref_section = dr.Field()
  syntactic_link = dr.SelfPointer()
  children = dr.SelfPointers()

  def get_ich_trace_ids(self, trace_ids=None):
    if trace_ids is None:
      trace_ids = []
    if self.is_ich():
      trace_ids.append(self.token.raw[len('*ICH*-'):])
    else:
      for child in self.children:
        child.get_ich_trace_ids(trace_ids)
    return trace_ids

  def has_ich_in_subtree(self):
    if self.is_ich():
      return True
    for child in self.children:
      if child.has_ich_in_subtree():
        return True
    return False

  def has_trace_id(self, trace_ids):
    for trace_id in trace_ids:
      if self.tag.endswith('-' + trace_id):
        return True
    return False

  def is_ich(self):
    return self.is_trace() and self.is_leaf() and self.token.raw.startswith('*ICH*-')

  def is_leaf(self):
    return len(self.children) == 0

  def is_trace(self):
    return self.tag == '-NONE-'

  def get_width(self):
    if self.is_leaf():
      return 1
    return sum(c.get_width() for c in self.children)


# =============================================================================
# Named Entities
# =============================================================================
class NamedEntity(dr.Ann):
  span = dr.Slice(Token)
  tag = dr.Field()
  start_offset = dr.Field()
  end_offset = dr.Field()


# =============================================================================
# Propositions
# =============================================================================
class PropPredPart(dr.Ann):
  encoded = dr.Field()
  node = dr.Pointer(ParseNode)


class PropArgPart(dr.Ann):
  encoded = dr.Field()
  node = dr.Pointer(ParseNode)


class PropArg(dr.Ann):
  parts = dr.Pointers(PropArgPart)


class PropArgGroup(dr.Ann):
  type = dr.Field()
  args = dr.Pointers(PropArg)


class PropLinkPart(dr.Ann):
  encoded = dr.Field()
  node = dr.Pointer(ParseNode)


class PropLink(dr.Ann):
  parts = dr.Pointers(PropLinkPart)


class PropLinkGroup(dr.Ann):
  type = dr.Field()
  associated_argument_id = dr.Field()  # TODO what is this field?
  links = dr.Pointers(PropLink)


class Proposition(dr.Ann):
  encoded = dr.Text()
  quality = dr.Field()
  type = dr.Field()  # <lemma>-<type> are on_sense_lemma_type ids.
  lemma = dr.Text()
  pb_sense_num = dr.Field()  # <lemma>.<pb_sense_num> are pb_sense_type ids.
  leaf = dr.Pointer(ParseNode)
  pred_parts = dr.Pointers(PropPredPart)
  arg_groups = dr.Pointers(PropArgGroup)
  link_groups = dr.Pointers(PropLinkGroup)


# =============================================================================
# Coreference
# =============================================================================
class CorefMention(dr.Ann):
  span = dr.Slice(Token)
  type = dr.Field()
  start_offset = dr.Field()
  end_offset = dr.Field()


class CorefChain(dr.Ann):
  id = dr.Text()
  section = dr.Field()
  type = dr.Field()
  speaker = dr.Pointer(Speaker)
  mentions = dr.Pointers(CorefMention)


# =============================================================================
# Document Structure
# =============================================================================
class Sentence(dr.Ann):
  span = dr.Slice(Token)
  parse = dr.Pointer(ParseNode)
  start_time = dr.Field()
  end_time = dr.Field()
  speakers = dr.Pointers(Speaker)
  propositions = dr.Pointers(Proposition)


class Doc(dr.Doc):
  doc_id = dr.Field()
  subcorpus_id = dr.Field()
  lang = dr.Field()
  genre = dr.Field()
  source = dr.Field()

  tokens = dr.Store(Token)
  sentences = dr.Store(Sentence)

  parse_nodes = dr.Store(ParseNode)

  named_entities = dr.Store(NamedEntity)

  prop_pred_parts = dr.Store(PropPredPart)
  prop_arg_parts = dr.Store(PropArgPart)
  prop_args = dr.Store(PropArg)
  prop_arg_groups = dr.Store(PropArgGroup)
  prop_link_parts = dr.Store(PropLinkPart)
  prop_links = dr.Store(PropLink)
  prop_link_groups = dr.Store(PropLinkGroup)
  propositions = dr.Store(Proposition)

  speakers = dr.Store(Speaker)
  coref_mentions = dr.Store(CorefMention)
  coref_chains = dr.Store(CorefChain)

  @classmethod
  def from_ontonotes(cls, odoc):
    # Construct the Doc object.
    doc = cls()
    src_attrs = ('doc_id', 'subcorpus_id', 'lang_id', 'genre', 'source')
    dst_attrs = ('doc_id', 'subcorpus_id', 'lang', 'genre', 'source')
    for src_attr, dst_attr in itertools.izip(src_attrs, dst_attrs):
      setattr(doc, dst_attr, getattr(odoc, src_attr))

    # Construct the speakers.
    speakers_by_name = {}  # { name : Speaker }
    for name, (gender, competence) in odoc.speakers.iteritems():
      speaker = doc.speakers.create(name=name, gender=gender, competence=competence)
      speakers_by_name[name] = speaker

    # Construct the tokens and sentences.
    token_byte_offset_start = 0
    onode_to_index = {}
    onode_to_token = {}
    for osent in odoc.sentences:
      # Create the tokens.
      tokens = []
      ntokens_before = len(doc.tokens)
      for otoken in osent.tokens:
        encoded = otoken.encode('utf-8')
        span = slice(token_byte_offset_start, token_byte_offset_start + len(encoded))
        token = doc.tokens.create(span=span, raw=otoken)
        token._index = len(doc.tokens) - 1
        tokens.append(token)
        token_byte_offset_start += len(encoded) + 1
      ntokens_after = len(doc.tokens)

      # Create the sentence.
      sent = doc.sentences.create(span=slice(ntokens_before, ntokens_after))
      if osent.start_time is not None or osent.end_time is not None:
        sent.start_time = osent.start_time
        sent.end_time = osent.end_time
        if osent.speakers:
          for name in osent.speakers:
            sent.speakers.append(speakers_by_name[name])

      # Create the parse nodes.
      token_to_parse = {}
      token_upto = 0
      for onode in osent.parse.all_nodes():
        onode_to_index[onode] = len(doc.parse_nodes)
        node = doc.parse_nodes.create()
        src_attrs = ('tag', 'part_of_speech', 'phrase_type', 'function_tags', 'coref_section')
        dst_attrs = ('tag', 'pos', 'phrase_type', 'function_tags', 'coref_section')
        for src_attr, dst_attr in itertools.izip(src_attrs, dst_attrs):
          setattr(node, dst_attr, getattr(onode, src_attr))
        if onode.is_leaf():
          assert onode.word, onode
          node.token = doc.tokens[ntokens_before + token_upto]
          token_to_parse[node.token] = node
          token_upto += 1

      # Create a mapping from the parse leaf nodes to their associated Token objects.
      oleaves = tuple(osent.parse.ordered_leaves())
      assert len(tokens) == len(oleaves)
      for oleaf, token in itertools.izip(oleaves, tokens):
        onode_to_token[oleaf] = token
      del tokens

      # Place the new root parse node on the new sentence.
      sent.parse = doc.parse_nodes[onode_to_index[osent.parse]]

      # Go through them again, updating their syntactic_link and children pointers.
      for onode in osent.parse.all_nodes():
        node = doc.parse_nodes[onode_to_index[onode]]
        if onode.syntactic_link is not None:
          node.syntactic_link = doc.parse_nodes[onode_to_index[onode.syntactic_link]]
        if not onode.is_leaf():
          for ochild in onode.children:
            child = doc.parse_nodes[onode_to_index[ochild]]
            node.children.append(child)

      # Create the named entities, skipping trace nodes in the word count offsets.
      for one_start, one_ntokens, one_type, ostart_char_offset, oend_char_offset in osent.nes:
        start = ntokens_before
        for i in xrange(one_start):
          while token_to_parse[doc.tokens[start]].is_trace():
            start += 1
          start += 1
        end = start
        for i in xrange(one_ntokens):
          while token_to_parse[doc.tokens[end]].is_trace():
            end += 1
          end += 1
        ne = doc.named_entities.create(span=slice(start, end), tag=one_type)
        if ostart_char_offset:
          ne.start_offset = ostart_char_offset
        if oend_char_offset:
          ne.end_offset = oend_char_offset

    # Construct the coref chains.
    for ochain in odoc.coref_chains:
      chain = doc.coref_chains.create(id=ochain.identifier, section=ochain.section, type=ochain.type)
      if ochain.speaker:
        chain.speaker = speakers_by_name[ochain.speaker]

      # Construct the coref mentions.
      for omention in ochain.mentions:
        start = onode_to_token[omention.start_leaf]._index
        end = onode_to_token[omention.end_leaf]._index
        mention = doc.coref_mentions.create(span=slice(start, end + 1), type=omention.type)
        if omention.start_offset:
          mention.start_offset = omention.start_offset
        if omention.end_offset:
          mention.end_offset = omention.end_offset
        chain.mentions.append(mention)

    # Construct the propositions.
    for oprop in odoc.propositions:
      # Construct the proposition.
      leaf = doc.parse_nodes[onode_to_index[oprop.leaf]]
      prop = doc.propositions.create(encoded=oprop.encoded, quality=oprop.quality, type=oprop.type, lemma=oprop.lemma, pb_sense_num=oprop.pb_sense_num, leaf=leaf)

      # Construct the predicate.
      for encoded, onode in oprop.pred_nodes:
        node = doc.parse_nodes[onode_to_index[onode]]
        pred_part = doc.prop_pred_parts.create(encoded=encoded, node=node)
        prop.pred_parts.append(pred_part)

      # Construct the arguments of the proposition.
      for oarg_group in oprop.arg_groups:
        arg_group = doc.prop_arg_groups.create(type=oarg_group.type)
        prop.arg_groups.append(arg_group)
        for oarg in oarg_group.arguments:
          arg = doc.prop_args.create()
          arg_group.args.append(arg)
          for encoded, onode in oarg:
            node = doc.parse_nodes[onode_to_index[onode]]
            arg_part = doc.prop_arg_parts.create(encoded=encoded, node=node)
            arg.parts.append(arg_part)

      # Construct the links of the proposition.
      for olink_group in oprop.link_groups:
        link_group = doc.prop_link_groups.create(type=olink_group.type, associated_argument_id=olink_group.associated_argument_id)
        prop.link_groups.append(link_group)
        for olink in olink_group.links:
          link = doc.prop_links.create()
          link_group.links.append(link)
          for encoded, onode in olink:
            node = doc.parse_nodes[onode_to_index[onode]]
            link_part = doc.prop_link_parts.create(encoded=encoded, node=node)
            link.parts.append(link_part)

      # Add the proposition to the appropriate sentence.
      doc.sentences[oprop.sentence_index].propositions.append(prop)

    return doc
