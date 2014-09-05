package ontonotes5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class OPropArgGroup {
  public String type;
  public List<List<OPropNode>> arguments; // Outer is "*", inner is "," for split arguments.

  public OPropArgGroup(final Connection conn, final OSentence sentence, final String propositionId, final int index, final String type) throws SQLException {
    this.type = type;

    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Get the argument ids first so we can reuse the connection.
    stmt = conn.prepareStatement("SELECT id FROM argument WHERE proposition_id = ? AND argument_analogue_index = ? ORDER BY index_in_parent");
    stmt.setString(1, propositionId);
    stmt.setInt(2, index);
    rs = stmt.executeQuery();
    final List<String> argumentIds = new ArrayList<String>();
    while (rs.next())
      argumentIds.add(rs.getString("id"));
    rs.close();
    stmt.close();

    arguments = new ArrayList<List<OPropNode>>(argumentIds.size());
    for (final String argumentId : argumentIds) {
      stmt = conn.prepareStatement("SELECT node_id FROM argument_node WHERE argument_id = ?");
      stmt.setString(1, argumentId);
      rs = stmt.executeQuery();
      List<OPropNode> nodes = new ArrayList<OPropNode>();
      while (rs.next()) {
        OPropNode node = new OPropNode(rs.getString("node_id"), sentence);
        nodes.add(node);
      }
      arguments.add(nodes);
      rs.close();
      stmt.close();
    }
  }
}