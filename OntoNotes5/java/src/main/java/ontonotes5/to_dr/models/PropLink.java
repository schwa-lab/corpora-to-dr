package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class PropLink extends AbstractAnn {
  @dr.Pointer(store="propLinkParts") public List<PropLinkPart> parts = new ArrayList<PropLinkPart>();
}
