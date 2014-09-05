package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class Proposition extends AbstractAnn {
  @dr.Field public String encoded;
  @dr.Field public String quality;
  @dr.Field public String type;
  @dr.Field public String lemma;
  @dr.Field(serial="pb_sense_num") public String pbSenseNum;
  @dr.Pointer(store="parseNodes") public ParseNode leaf;
  @dr.Pointer(store="propPredParts", serial="pred_parts") public List<PropPredPart> predParts = new ArrayList<PropPredPart>();
  @dr.Pointer(store="propArgGroups", serial="arg_groups") public List<PropArgGroup> argGroups = new ArrayList<PropArgGroup>();
  @dr.Pointer(store="propLinkGroups", serial="link_groups") public List<PropLinkGroup> linkGroups = new ArrayList<PropLinkGroup>();
}
