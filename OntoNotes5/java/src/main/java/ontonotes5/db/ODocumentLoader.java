package ontonotes5.db;

import java.sql.Connection;
import java.sql.SQLException;

import ontonotes5.common.StatsCollector;
import ontonotes5.models.ODocument;


public final class ODocumentLoader {
  private ODocumentLoader() {
  }

  public static ODocument load(final String docId) throws SQLException {
    final long loadingStartTime = System.currentTimeMillis();

    // Create a connection to the DB and pull in all of the associated information for the current document.
    Connection conn = null;
    ODocument oDoc = null;
    try {
      conn = ConnectionManager.createConnection();
      oDoc = new ODocument(conn, docId);
    }
    finally {
      try {
        if (conn != null)
          conn.close();
      }
      catch (SQLException e) {
      }
    }

    // Update the loading time counter.
    final long loadingEndTime = System.currentTimeMillis();
    StatsCollector.getInstance().addLoadingDelta(loadingEndTime - loadingStartTime);

    return oDoc;
  }
}