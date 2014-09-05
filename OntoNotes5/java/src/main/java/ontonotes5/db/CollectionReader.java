package ontonotes5.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ontonotes5.common.StatsCollector;
import ontonotes5.to_uima.types.Document;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;


public class CollectionReader extends CollectionReader_ImplBase {
  private List<String> docIds;
  private int docIdUpto;
  private Connection conn;

  @Override
  public void initialize() throws ResourceInitializationException {
    docIds = new ArrayList<String>();
    docIdUpto = 0;

    Statement s = null;
    ResultSet rs = null;
    try {
      conn = ConnectionManager.createConnection();
      s = conn.createStatement();
      s.execute("SELECT id FROM document");
      rs = s.getResultSet();
      while (rs.next())
        docIds.add(rs.getString("id"));
    }
    catch (SQLException e) {
      throw new ResourceInitializationException(e);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
        if (s != null)
          s.close();
      }
      catch (SQLException e) {
      }
    }

    // Randomly shuffle the document ids for better multi-threaded spread.
    Collections.shuffle(docIds);
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return docIdUpto != docIds.size();
  }

  @Override
  public void getNext(final CAS cas) throws IOException, CollectionException {
    final long startTime = System.currentTimeMillis();

    // Obtain the JCas from the CAS.
    JCas jcas;
    try {
      jcas = cas.getJCas();
    }
    catch (CASException e) {
      throw new CollectionException(e);
    }

    // Construct the fake original document text from the tokens.
    final String docId = docIds.get(docIdUpto);
    String documentText = null;
    try {
      documentText = constructDocumentText(docId);
    }
    catch (SQLException e) {
      throw new CollectionException(e);
    }
    jcas.setDocumentText(documentText);

    // Annotate the document with its doc_id.
    final Document doc = new Document(jcas);
    doc.setDocId(docId);
    doc.setBegin(0);
    doc.setEnd(documentText.length());
    doc.addToIndexes();

    // Increment our progress counter.
    docIdUpto++;

    final long endTime = System.currentTimeMillis();
    StatsCollector.getInstance().addLoadingDelta(endTime - startTime);
  }

  @Override
  public void close() throws IOException {
    try {
      if (conn != null)
        conn.close();
    }
    catch (SQLException e) {
    }
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(docIdUpto, docIds.size(), Progress.ENTITIES) };
  }

  private String constructDocumentText(final String docId) throws SQLException {
    final StringBuilder documentText = new StringBuilder();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      stmt = conn.prepareStatement("SELECT string FROM sentence WHERE document_id = ? ORDER BY sentence_index");
      stmt.setString(1, docId);
      rs = stmt.executeQuery();

      boolean first = true;
      while (rs.next()) {
        if (first)
          first = false;
        else
          documentText.append(' ');
        documentText.append(rs.getString("string"));
      }
    }
    finally {
      if (rs != null)
        rs.close();
      if (stmt != null)
        stmt.close();
    }

    return documentText.toString();
  }
}
