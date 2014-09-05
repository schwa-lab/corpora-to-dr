package ontonotes5.to_dr.models;

import org.schwa.dr.*;


@dr.Ann
public class Speaker extends AbstractAnn {
  @dr.Field public String name;
  @dr.Field public String gender;
  @dr.Field public String competence;
}
