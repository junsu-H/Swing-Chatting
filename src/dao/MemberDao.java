package dao;

import models.Member;
import util.AESUtil;

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

public class MemberDao {
    public static MemberDao getInstance() {
        return new MemberDao();
    }
    /* DB 연결 객체 */
    private Connection conn = null;

    /* SQL Query를 사용할 객체 */
    private PreparedStatement pstmt = null;

    /* SQL Query 결과 객체 */
    private ResultSet rs;

    /* Success 1, Fail -1 */
    public String findByNickname(Member member){
        String selectSql = "select nickname from member where nickname = ?;";
        try {
            conn = DBConnection.getConnection();
            /* query conn */
            pstmt = conn.prepareStatement(selectSql);

            /* selectSql ?에 들어갈 data setting */
            pstmt.setString(1, member.getNickname());

            /* selectSql Query 리턴값 받아오기 */
            rs = pstmt.executeQuery();

            /* select가 되면 ID가 있으므로 1 반환*/
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
            System.out.println("DB Connect Close");
        }
        /* login fail */
        return String.valueOf(-1);
    }

    /* Success 1, Fail -1 */
    public String findByPassword(Member member) {
        String selectSql = "select password from member where nickname = ?;";
        try {
            conn = DBConnection.getConnection();
            /* query conn */
            pstmt = conn.prepareStatement(selectSql);

            /* selectSql ?에 들어갈 data setting */
            pstmt.setString(1, member.getNickname());

            /* selectSql Query 리턴값 받아오기 */
            rs = pstmt.executeQuery();

            /* select가 되면 ID가 있으므로 1 반환*/
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
            System.out.println("DB Connect Close");
        }
        /* login fail */
        return String.valueOf(-1);
    }

    /* Success 1, Fail -1 */
    public int findByNicknameAndPassword(Member member) {
        String selectSql = "select * from member where nickname = ? and password = ?;";
        try {
            conn = DBConnection.getConnection();
            /* query conn */
            pstmt = conn.prepareStatement(selectSql);

            /* selectSql ?에 들어갈 data setting */
            pstmt.setString(1, member.getNickname());
            pstmt.setString(2, member.getPassword());

            /* selectSql Query 리턴값 받아오기 */
            rs = pstmt.executeQuery();

            /* select가 되면 1 반환 */
            if (rs.next()) {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
            System.out.println("DB Connect Close");
        }
        /* login fail */
        return -1;
    }

    /* Success 1, Fail -1 */
    public int save(Member member) {
        String insertSql = "insert into member(nickname, password) values(?,?);";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, member.getNickname());

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(128, secureRandom);

            String password = member.getPassword();
            String encrypted = AESUtil.encrypt(password);
            pstmt.setString(2, encrypted);
            pstmt.executeUpdate();
            return 1;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
            System.out.println("DB Connect Close");
        }
        return -1;
    }

    /* 탈퇴 시 */
    public void delete(Member member) {
        String deleteSql = "delete from member where nickname = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, member.getNickname());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
            System.out.println("DB Connect Close");
        }
    }

    /* Success 1, Fail all except 1 */
    public int checkNickname(Member member) {
        int count = 1;
        String selectSql = "select count(*) as c from member where nickname = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, member.getNickname());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count -= rs.getInt("c");
            }

            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
            System.out.println("DB Connect Close");
        }
        return 1;
    }
}

