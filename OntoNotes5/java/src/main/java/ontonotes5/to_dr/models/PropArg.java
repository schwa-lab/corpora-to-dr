package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class PropArg extends AbstractAnn {
  @dr.Pointer(store="propArgParts") public List<PropArgPart> parts = new ArrayList<PropArgPart>();
}
