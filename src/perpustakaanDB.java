import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class perpustakaanDB {
  private static final String URL = "jdbc:mysql://localhost:3306/perpustakaan";
  private static final String USER = "root";
  private static final String PASS = "";

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.err.println("MySQL JDBC Driver tidak ditemukan!");
      e.printStackTrace();
    }
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASS);
  }

}
