package ontonotes5.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectionManager {
  private static final String DB_DATABASE = "ontonotes5";
  private static final String DB_HOST = "129.78.xxx.xx"
  private static final String DB_USER = "ontonotes";
  private static final String DB_PASSWD = "xxxxxxxxx";

  public static Connection createConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_DATABASE + "?user=" + DB_USER + "&password=" + DB_PASSWD);
  }
}
