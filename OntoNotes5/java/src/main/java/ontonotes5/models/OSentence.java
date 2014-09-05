package ontonotes5.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;


public final class OSentence extends DBModel {
  public String[] tokens;
  public OParseNode parse;
  public Collection<ONamedEntity> namedEntities;
  public Double startTime;
  public Double endTime;
  public String[] speakerNames;

  public OSentence(final Connection conn, final String rootTreeId, final String string) throws SQLException {
    this.tokens = string.split(" ");
    this.parse = new OParseNode(conn, rootTreeId, null);
    this.namedEntities = new LinkedList<ONamedEntity>();
  }

  public void addNamedEntity(final int startIndex, final int endIndex, final String tag, final int startCharOffset, final int endCharOffset) {
    namedEntities.add(new ONamedEntity(startIndex, endIndex - startIndex + 1, tag, startCharOffset, endCharOffset));
  }

  public void setSpoken(final double startTime, final double endTime, final String[] speakerNames) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.speakerNames = speakerNames;
  }
}
