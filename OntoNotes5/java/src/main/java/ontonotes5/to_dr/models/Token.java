package ontonotes5.to_dr.models;

import org.schwa.dr.*;


@dr.Ann
public class Token extends AbstractAnn {
  @dr.Field public ByteSlice span;
  @dr.Field public String raw;
}
