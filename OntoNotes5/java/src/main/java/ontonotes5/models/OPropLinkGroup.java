package ontonotes5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class OPropLinkGroup {
  public String type;
  public String associatedArgumentId;
  public List<List<OPropNode>> links; // Outer is "*", inner is "," for split arguments.

  public OPropLinkGroup(final Connection conn, final OSentence sentence, final String propositionId, final int index, final String type, final String associatedArgumentId) throws SQLException {
    this.type = type;
    this.associatedArgumentId = associatedArgumentId;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Get the link ids first so we can reuse the connection.
    stmt = conn.prepareStatement("SELECT id FROM proposition_link WHERE proposition_id = ? AND link_analogue_index = ? ORDER BY index_in_parent");
    stmt.setString(1, propositionId);
    stmt.setInt(2, index);
    rs = stmt.executeQuery();
    final List<String> linkIds = new ArrayList<String>();
    while (rs.next())
      linkIds.add(rs.getString("id"));
    rs.close();
    stmt.close();

    links = new ArrayList<List<OPropNode>>(linkIds.size());
    for (final String linkId : linkIds) {
      stmt = conn.prepareStatement("SELECT node_id FROM link_node WHERE link_id = ?");
      stmt.setString(1, linkId);
      rs = stmt.executeQuery();
      List<OPropNode> nodes = new ArrayList<OPropNode>();
      while (rs.next()) {
        OPropNode node = new OPropNode(rs.getString("node_id"), sentence);
        nodes.add(node);
      }
      links.add(nodes);
      rs.close();
      stmt.close();
    }
  }
}