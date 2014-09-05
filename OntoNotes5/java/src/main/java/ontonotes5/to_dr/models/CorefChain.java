package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class CorefChain extends AbstractAnn {
  @dr.Field public String id;
  @dr.Field public int section;
  @dr.Field public String type;
  @dr.Pointer(store="speakers") public Speaker speaker;
  @dr.Pointer(store="corefMentions") public List<CorefMention> mentions = new ArrayList<CorefMention>();
}
