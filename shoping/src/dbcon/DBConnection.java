package dbcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static String url = "jdbc:mariadb://localhost:3306/hrdtest";
    static String user = "root";
    static String password = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
}
