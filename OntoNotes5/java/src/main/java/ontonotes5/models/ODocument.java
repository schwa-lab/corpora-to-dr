package ontonotes5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class ODocument extends DBModel {
  public String id;
  public String subcorpusId;
  public String langId;
  public String genre;
  public String source;
  public List<OSentence> sentences;
  public Map<String, OSpeaker> speakers;
  public Collection<OCorefChain> corefChains;
  public List<OProposition> propositions;

  public ODocument(final Connection conn, final String docId) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Load the document data.
    stmt = conn.prepareStatement("SELECT id, subcorpus_id, lang_id, genre, source FROM document WHERE id = ?");
    stmt.setString(1, docId);
    rs = stmt.executeQuery();
    assert rs.next();
    populateSelf(rs);
    rs.close();
    stmt.close();

    // Load the sentences and their parse trees.
    this.sentences = new ArrayList<OSentence>();
    stmt = conn.prepareStatement("SELECT id, string FROM sentence WHERE document_id = ? ORDER BY sentence_index");
    stmt.setString(1, docId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      OSentence sentence = new OSentence(conn, rs.getString("id"), rs.getString("string"));
      this.sentences.add(sentence);
    }
    rs.close();
    stmt.close();

    // Load the named entities.
    stmt = conn.prepareStatement("SELECT type, sentence_index, start_word_index, end_word_index, start_char_offset, end_char_offset FROM name_entity WHERE document_id = ?");
    stmt.setString(1, docId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      final OSentence sentence = this.sentences.get(rs.getInt("sentence_index"));
      sentence.addNamedEntity(rs.getInt("start_word_index"), rs.getInt("end_word_index"), rs.getString("type"), rs.getInt("start_char_offset"), rs.getInt("end_char_offset"));
    }
    rs.close();
    stmt.close();

    // Find and load any spoken text information.
    this.speakers = new HashMap<String, OSpeaker>();
    stmt = conn.prepareStatement("SELECT line_number, start_time, stop_time, name, gender, competence FROM speaker_sentence WHERE document_id = ?");
    stmt.setString(1, docId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      final String competence = rs.getString("competence");
      final String[] names = rs.getString("name").split(",");
      String[] genders = rs.getString("gender").split(",");
      if (genders.length == 1 && names.length != 1) { // If only one gender was provided, assume it is the same for each speaker. This appears to be the case when gender is "unknown".
        String[] newGenders = new String[names.length];
        Arrays.fill(newGenders, genders[0]);
        genders = newGenders;
      }

      for (int i = 0; i != names.length; i++)
        addSpeaker(names[i], genders[i], competence);

      sentences.get(rs.getInt("line_number")).setSpoken(rs.getDouble("start_time"), rs.getDouble("stop_time"), names);
    }
    rs.close();
    stmt.close();

    // Find and load any coref chains (and mentions).
    this.corefChains = new ArrayList<OCorefChain>();
    stmt = conn.prepareStatement("SELECT id FROM coreference_chain WHERE document_id = ?");
    stmt.setString(1, docId);
    rs = stmt.executeQuery();
    while (rs.next()) {
      OCorefChain chain = new OCorefChain(conn, this, rs.getString("id"));
      this.corefChains.add(chain);
    }
    rs.close();
    stmt.close();

    // Find and load any propositions.
    this.propositions = new ArrayList<OProposition>();
    stmt = conn.prepareStatement("SELECT id FROM proposition WHERE document_id = ?");
    stmt.setString(1, docId);
    rs = stmt.executeQuery();
    List<String> propositionIds = new ArrayList<String>();
    while (rs.next())
      propositionIds.add(rs.getString("id"));
    for (String propositionId : propositionIds) {
      OProposition proposition = new OProposition(conn, this, propositionId);
      this.propositions.add(proposition);
    }
    rs.close();
    stmt.close();
  }

  public void addSpeaker(final String name) {
    if (!speakers.containsKey(name))
      speakers.put(name, new OSpeaker(name));
  }

  public void addSpeaker(final String name, final String gender, final String competence) {
    if (!speakers.containsKey(name))
      speakers.put(name, new OSpeaker(name, gender, competence));
  }
}
