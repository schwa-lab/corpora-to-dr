package ontonotes5.to_dr.models;

import org.schwa.dr.*;


@dr.Ann
public class PropLinkPart extends AbstractAnn {
  @dr.Field public String encoded;
  @dr.Pointer(store="parseNodes") public ParseNode node;
}
