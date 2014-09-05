package ontonotes5.to_dr.models;

import org.schwa.dr.*;


@dr.Doc
public class Doc extends AbstractDoc {
  @dr.Field(serial="doc_id") public String docId;
  @dr.Field(serial="subcorpus_id") public String subcorpusId;
  @dr.Field public String lang;
  @dr.Field public String genre;
  @dr.Field public String source;

  @dr.Store public Store<Token> tokens = new Store<Token>();
  @dr.Store public Store<Sentence> sentences = new Store<Sentence>();
  @dr.Store(serial="parse_nodes") public Store<ParseNode> parseNodes = new Store<ParseNode>();
  @dr.Store(serial="named_entities") public Store<NamedEntity> namedEntities = new Store<NamedEntity>();
  @dr.Store(serial="prop_pred_parts") public Store<PropPredPart> propPredParts = new Store<PropPredPart>();
  @dr.Store(serial="prop_arg_parts") public Store<PropArgPart> propArgParts = new Store<PropArgPart>();
  @dr.Store(serial="prop_args") public Store<PropArg> propArgs = new Store<PropArg>();
  @dr.Store(serial="prop_arg_groups") public Store<PropArgGroup> propArgGroups = new Store<PropArgGroup>();
  @dr.Store(serial="prop_link_parts") public Store<PropLinkPart> propLinkParts = new Store<PropLinkPart>();
  @dr.Store(serial="prop_links") public Store<PropLink> propLinks = new Store<PropLink>();
  @dr.Store(serial="prop_link_groups") public Store<PropLinkGroup> propLinkGroups = new Store<PropLinkGroup>();
  @dr.Store(serial="propositions") public Store<Proposition> propositions = new Store<Proposition>();
  @dr.Store public Store<Speaker> speakers = new Store<Speaker>();
  @dr.Store(serial="coref_mentions") public Store<CorefMention> corefMentions = new Store<CorefMention>();
  @dr.Store(serial="coref_chains") public Store<CorefChain> corefChains = new Store<CorefChain>();
}
