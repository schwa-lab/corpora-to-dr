package ontonotes5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public final class OProposition extends DBModel {
  public String encodedProposition;
  public String quality;
  public String type;
  public int sentenceIndex;
  public int tokenIndex;
  public String lemma;
  public String pbSenseNum;
  public String predicateId;
  public OParseNode leaf;
  public List<OPropNode> predNodes;
  public List<OPropArgGroup> argGroups;
  public List<OPropLinkGroup> linkGroups;

  public OProposition(final Connection conn, final ODocument doc, final String propositionId) throws SQLException {
    PreparedStatement stmt;
    ResultSet rs;

    // Fetch the info about this proposition and its predicate.
    stmt = conn.prepareStatement("SELECT x.encoded_proposition, x.quality, y.type, y.sentence_index, y.token_index, y.lemma, y.pb_sense_num, y.id AS predicate_id FROM proposition x JOIN predicate y ON x.id = y.proposition_id WHERE x.id = ?");
    stmt.setString(1, propositionId);
    rs = stmt.executeQuery();
    assert rs.next();
    populateSelf(rs);
    rs.close();
    stmt.close();

    final OSentence sentence = doc.sentences.get(this.sentenceIndex);
    this.leaf = sentence.parse.getOrderedLeaves().get(this.tokenIndex);

    // Fetch the predicate nodes.
    this.predNodes = new LinkedList<OPropNode>();
    stmt = conn.prepareStatement("SELECT node_id FROM predicate_node WHERE predicate_id = ? ORDER BY index_in_parent");
    stmt.setString(1, this.predicateId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      OPropNode predNode = new OPropNode(rs.getString("node_id"), sentence);
      this.predNodes.add(predNode);
    }
    rs.close();
    stmt.close();

    // Fetch the arguments.
    this.argGroups = new LinkedList<OPropArgGroup>();
    stmt = conn.prepareStatement("SELECT argument_analogue_index, type FROM argument WHERE proposition_id = ? GROUP BY argument_analogue_index ORDER BY argument_analogue_index");
    stmt.setString(1, propositionId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      OPropArgGroup argGroup = new OPropArgGroup(conn, sentence, propositionId, rs.getInt("argument_analogue_index"), rs.getString("type"));
      this.argGroups.add(argGroup);
    }
    rs.close();
    stmt.close();

    // Fetch the links.
    this.linkGroups = new LinkedList<OPropLinkGroup>();
    stmt = conn.prepareStatement("SELECT link_analogue_index, type, associated_argument_id FROM proposition_link WHERE proposition_id = ? GROUP BY link_analogue_index ORDER BY link_analogue_index");
    stmt.setString(1, propositionId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      OPropLinkGroup linkGroup = new OPropLinkGroup(conn, sentence, propositionId, rs.getInt("link_analogue_index"), rs.getString("type"), rs.getString("associated_argument_id"));
      this.linkGroups.add(linkGroup);
    }
    rs.close();
    stmt.close();
  }
}
