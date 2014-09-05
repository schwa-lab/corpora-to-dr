package ontonotes5.models;

public final class ONamedEntity {
  public int startIndex;
  public int length;
  public String tag;
  public int startOffset;
  public int endOffset;

  public ONamedEntity(final int startIndex, final int length, final String tag, final int startOffset, final int endOffset) {
    this.startIndex = startIndex;
    this.length = length;
    this.tag = tag;
    this.startOffset = startOffset;
    this.endOffset = endOffset;
  }
}