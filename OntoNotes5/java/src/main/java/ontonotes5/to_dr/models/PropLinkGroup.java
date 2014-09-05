package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class PropLinkGroup extends AbstractAnn {
  @dr.Field public String type;
  @dr.Field(serial = "associated_argument_id") public String associatedArgumentId;
  @dr.Pointer(store = "propLinks") public List<PropLink> links = new ArrayList<PropLink>();
}
