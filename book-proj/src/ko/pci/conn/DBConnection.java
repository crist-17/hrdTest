package ko.pci.conn;

import java.sql.*;

public class DBConnection {
    static String url =  "jdbc:mariadb://localhost:3306/hrdtest";
    static String user= "root";
    static String password = "1234";


    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.printf("***MariaDB 연결성공(%s)%n",url);
        }catch (Exception e){
            System.out.println("===DB 연결 실패===");
        }
        return conn;
    }


    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e)
        { e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(Statement stmt, Connection conn) {
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
    public static void close(PreparedStatement ps, Connection conn) {
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

}
