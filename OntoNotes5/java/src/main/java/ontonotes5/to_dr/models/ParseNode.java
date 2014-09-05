package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class ParseNode extends AbstractAnn {
  @dr.Field public String tag;
  @dr.Field public String pos;
  @dr.Pointer(store="tokens") public Token token;
  @dr.Field(serial="phrase_type") public String pharseType;
  @dr.Field(serial="function_tags") public String functionTags;
  @dr.Field(serial="coref_section") public int corefSection;
  @dr.SelfPointer(serial="syntactic_link") public ParseNode syntacticLink;
  @dr.SelfPointer public List<ParseNode> children = new ArrayList<ParseNode>();
}
