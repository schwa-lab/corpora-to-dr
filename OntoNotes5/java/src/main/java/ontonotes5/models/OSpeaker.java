package ontonotes5.models;


public final class OSpeaker {
  public String name;
  public String gender;
  public String competence;

  public OSpeaker(String name) {
    this(name, "unknown", "unknown");
  }

  public OSpeaker(String name, String gender, String competence) {
    this.name = name;
    this.gender = gender;
    this.competence = competence;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    else if (o == null)
      return false;
    else if (!(o instanceof OSpeaker))
      return false;
    else
      return name.equals(((OSpeaker) o).name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
