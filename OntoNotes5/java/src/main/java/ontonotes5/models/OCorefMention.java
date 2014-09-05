package ontonotes5.models;


public final class OCorefMention {
  public OParseNode startLeaf;
  public OParseNode endLeaf;
  public String type;
  public int startOffset;
  public int endOffset;

  public OCorefMention(ODocument doc, String type, int sentenceIndex, int startTokenIndex, int endTokenIndex, int startCharOffset, int endCharOffset) {
    this.type = type;
    this.startOffset = startCharOffset;
    this.endOffset = endCharOffset;

    int i = 0;
    for (OParseNode leaf : doc.sentences.get(sentenceIndex).parse.getOrderedLeaves()) {
      if (i == startTokenIndex)
        this.startLeaf = leaf;
      if (i == endTokenIndex) {
        this.endLeaf = leaf;
        break;
      }
      i++;
    }

    if (this.startLeaf == null)
      throw new RuntimeException("startLeaf is null");
    if (this.endLeaf == null)
      throw new RuntimeException("endLeaf is null");
  }
}
