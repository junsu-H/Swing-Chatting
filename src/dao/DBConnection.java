package dao;

import java.sql.*;

public class DBConnection {
    private static final String url = "jdbc:mysql://localhost/chatting?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String id = "root";
    private static final String pw = "root";
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, id, pw);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Loading Fail");
        } catch (SQLException e) {
            System.out.println("SQL Error " + e);
        }
        return conn;
    }

    public static void close(Connection c, PreparedStatement p, ResultSet r) {
        try {
            if (c != null) c.close();
            if (p != null) p.close();
            if (r != null) r.close();
        } catch (Exception e) {
            System.out.println("close(c, p, r) Error");
        }
    }
}
