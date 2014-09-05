package ontonotes5.models;

public final class OPropNode {
  public String nodeId;
  public OParseNode node;

  public OPropNode(final String nodeId, final OSentence sentence) {
    this.nodeId = nodeId;
    this.node = sentence.parse.getNodeByPropbankNodeId(nodeId);
  }
}