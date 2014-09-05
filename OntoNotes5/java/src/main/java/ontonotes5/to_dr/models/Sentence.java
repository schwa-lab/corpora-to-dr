package ontonotes5.to_dr.models;

import java.util.ArrayList;
import java.util.List;

import org.schwa.dr.*;


@dr.Ann
public class Sentence extends AbstractAnn {
  @dr.Pointer(store="tokens") public Slice<Token> span;
  @dr.Pointer(store="parseNodes") public ParseNode parse;
  @dr.Field(serial="start_time") public double startTime;
  @dr.Field(serial="end_time") public double endTime;
  @dr.Pointer(store="speakers") public List<Speaker> speakers = new ArrayList<Speaker>();
  @dr.Pointer(store="propositions") public List<Proposition> propositions = new ArrayList<Proposition>();
}
