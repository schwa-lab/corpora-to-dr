package ontonotes5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;


public final class OCorefChain {
  public String identifier;
  public int section;
  public String type;
  public String speakerName;
  public Collection<OCorefMention> mentions;

  public OCorefChain(final Connection conn, final ODocument doc, final String chainId) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Load the data about the chain.
    stmt = conn.prepareStatement("SELECT number, section, type, speaker FROM coreference_chain WHERE id = ?");
    stmt.setString(1, chainId);
    rs = stmt.executeQuery();
    assert rs.next();
    this.identifier = rs.getString("number");
    this.section = Integer.parseInt(rs.getString("section"));
    this.type = rs.getString("type");
    this.speakerName = rs.getString("speaker");
    rs.close();
    stmt.close();

    // Clean the speaker value and update the document about a potentially new unknown speaker.
    if (this.speakerName != null) {
      this.speakerName = this.speakerName.trim();
      if (this.speakerName.isEmpty())
        this.speakerName = null;
    }
    if (this.speakerName != null)
      doc.addSpeaker(this.speakerName);

    // Load up the mentions associated with this chain.
    this.mentions = new LinkedList<OCorefMention>();
    stmt = conn.prepareStatement("SELECT type, sentence_index, start_token_index, end_token_index, start_char_offset, end_char_offset FROM coreference_link WHERE coreference_chain_id = ?");
    stmt.setString(1, chainId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      OCorefMention mention = new OCorefMention(doc, rs.getString("type"), rs.getInt("sentence_index"), rs.getInt("start_token_index"), rs.getInt("end_token_index"), rs.getInt("start_char_offset"), rs.getInt("end_char_offset"));
      this.mentions.add(mention);
    }
    rs.close();
    stmt.close();
  }
}
