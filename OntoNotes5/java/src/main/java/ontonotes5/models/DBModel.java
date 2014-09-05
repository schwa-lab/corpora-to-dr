package ontonotes5.models;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public abstract class DBModel {
  protected void populateSelf(final ResultSet rs) throws SQLException {
    final ResultSetMetaData metadata = rs.getMetaData();
    final int columnCount = metadata.getColumnCount();
    try {
      for (int i = 1; i <= columnCount; i++) {
        String fieldName = underscoreCaseToCamelCase(metadata.getColumnLabel(i));
        Field field = getClass().getField(fieldName);
        field.set(this, rs.getObject(i));
      }
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  protected static String underscoreCaseToCamelCase(final String orig) {
    final StringBuilder b = new StringBuilder(orig.length());
    boolean upperNext = false;
    for (int i = 0; i != orig.length(); i++) {
      char c = orig.charAt(i);
      if (c == '_')
        upperNext = true;
      else {
        if (upperNext) {
          c = Character.toUpperCase(c);
          upperNext = false;
        }
        b.append(c);
      }
    }
    return b.toString();
  }
}
