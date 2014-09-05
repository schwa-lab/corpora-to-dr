package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class PropArgGroup extends AbstractAnn {
  @dr.Field public String type;
  @dr.Pointer(store="propArgs") public List<PropArg> args = new ArrayList<PropArg>();
}
