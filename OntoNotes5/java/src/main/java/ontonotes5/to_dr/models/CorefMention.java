package ontonotes5.to_dr.models;

import org.schwa.dr.*;


@dr.Ann
public class CorefMention extends AbstractAnn {
  @dr.Pointer(store="tokens") public Slice<Token> span;
  @dr.Field public String type;
  @dr.Field(serial="start_offset") public int startOffset;
  @dr.Field(serial="end_offset") public int endOffset;
}
