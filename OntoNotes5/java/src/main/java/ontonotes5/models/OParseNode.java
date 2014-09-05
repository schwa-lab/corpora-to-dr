package ontonotes5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public final class OParseNode extends DBModel {
  public String id;
  public String word;
  public int corefSection;
  public String syntacticLinkType;
  public String tag;
  public String partOfSpeech;
  public String phraseType;
  public String functionTagId;
  public OParseNode parent;
  public OParseNode[] children;
  public OParseNode syntacticLink;
  public String functionTags;

  public OParseNode(final Connection conn, final String id, final OParseNode parent) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Load the node data.
    stmt = conn.prepareStatement("SELECT id, word, coref_section, syntactic_link_type, tag, part_of_speech, phrase_type, function_tag_id FROM tree WHERE id = ?");
    stmt.setString(1, id);
    rs = stmt.executeQuery();
    assert rs.next();
    populateSelf(rs);
    rs.close();
    stmt.close();

    this.parent = parent;
    this.syntacticLink = null;

    // Find and load the children of the node.
    stmt = conn.prepareStatement("SELECT id FROM tree WHERE parent_id = ? ORDER BY child_index");
    stmt.setString(1, id);
    rs = stmt.executeQuery();
    List<String> childIds = new ArrayList<String>();
    while (rs.next())
      childIds.add(rs.getString("id"));
    List<OParseNode> tmpChildren = new ArrayList<OParseNode>();
    for (String childId : childIds) {
      OParseNode child = new OParseNode(conn, childId, this);
      tmpChildren.add(child);
    }
    this.children = new OParseNode[tmpChildren.size()];
    tmpChildren.toArray(this.children);
    rs.close();
    stmt.close();

    // Extract only the useful part of the function_tag_id.
    this.functionTags = null;
    if (this.functionTagId != null && !this.functionTagId.isEmpty())
      this.functionTags = this.functionTagId.split("@")[0];
  }

  public Collection<OParseNode> getAllNodes() {
    Collection<OParseNode> nodes = new ArrayList<OParseNode>();
    getAllNodes(nodes);
    return nodes;
  }

  private void getAllNodes(final Collection<OParseNode> nodes) {
    nodes.add(this);
    for (OParseNode child : children)
      child.getAllNodes(nodes);
  }

  public List<OParseNode> getOrderedLeaves() {
    List<OParseNode> leaves = new ArrayList<OParseNode>();
    getOrderedLeaves(leaves);
    return leaves;
  }

  private void getOrderedLeaves(final List<OParseNode> leaves) {
    if (isLeaf())
      leaves.add(this);
    else
      for (OParseNode child : children)
        child.getOrderedLeaves(leaves);
  }

  public OParseNode getNodeByPropbankNodeId(final String propbankNodeId) {
    final String[] parts = propbankNodeId.split(":");
    final int tokenIndex = Integer.parseInt(parts[0]);
    final int height = Integer.parseInt(parts[1]);
    OParseNode node = null;
    int i = 0;
    for (OParseNode n : getOrderedLeaves()) {
      if (i == tokenIndex) {
        node = n;
        break;
      }
      i++;
    }
    if (node == null)
      throw new RuntimeException("Failed to find node for propbaknNodeId '" + propbankNodeId + "'");

    for (i = 0; i != height; i++)
      node = node.parent;
    return node;
  }

  public boolean isLeaf() {
    return children.length == 0;
  }

  public boolean isTrace() {
    return tag.equals("-NONE-");
  }
}
