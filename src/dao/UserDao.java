package dao;

import models.User;
import util.AES;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dao.DBConnection.close;

public class UserDao implements UserInterface {
    /* DB Connection Object */
    private Connection conn = null;

    /* Object to use SQL Query */
    private PreparedStatement pstmt = null;

    /* SQL Query result Object */
    private ResultSet rs;

    public static UserDao getInstance() {
        return new UserDao();
    }

    private String getRsString(User user, String selectSql) {
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, user.getEmail());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
        return String.valueOf(-1);
    }

    /* Success 1, Fail -1 */
    public String findByEmail(User user) {
        String selectSql = "select email from user where email = ?;";
        return getRsString(user, selectSql);
    }

    /* Success 1, Fail -1 */
    public String findByPassword(User user) {
        String selectSql = "select password from user where email = ?;";
        return getRsString(user, selectSql);
    }

    /* Success 1, Fail all except 1 */
    public int checkEmail(User user) {
        int count = 1;
        String selectSql = "select count(*) as c from user where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, user.getEmail());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count -= rs.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
        return 1;
    }

    /* Success 1, Fail -1 */
    public int save(User user) {
        String insertSql = "insert into user(email, password) values(?,?);";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, user.getEmail());

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(128, secureRandom);

            String password = user.getPassword();
            String encrypted = AES.encrypt(AES.cbc, AES.dbIv, password);
            pstmt.setString(2, encrypted);
            pstmt.executeUpdate();
            return 1;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
        return -1;
    }

    /* Success 1, Fail -1 */
    public int updateNickname(String nickname, User user) {
        String updateSql = "update from user set nickname = ? where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, nickname);
            pstmt.setString(2, user.getEmail());
            pstmt.executeUpdate();

            return 1;
        } catch (SQLException e) {
            System.out.println("updateNickname Error" + e);
        } finally {
            close(conn, pstmt, rs);
        }
        return -1;

    }

    /* Success 1, Fail -1 */
    public int delete(User user) {
        String deleteSql = "update user where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, user.getEmail());
            pstmt.executeUpdate();

            return 1;
        } catch (SQLException e) {
            System.out.println("delete Error" + e);
        } finally {
            close(conn, pstmt, rs);
        }
        return -1;

    }
}

