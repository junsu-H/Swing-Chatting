package dao;

import models.Member;
import util.AESUtil;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
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
    public String findByEmail(Member member){
        String selectSql = "select email from member where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, member.getEmail());

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
    public String findByPassword(Member member) {
        String selectSql = "select password from member where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, member.getEmail());

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
    public int findByNicknameAndPassword(Member member) {
        String selectSql = "select * from member where email = ? and password = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);

            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getPassword());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
        return -1;
    }

    /* Success 1, Fail all except 1 */
    public int checkEmail(Member member) {
        int count = 1;
        String selectSql = "select count(*) as c from member where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, member.getEmail());
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
    public int save(Member member) {
        String insertSql = "insert into member(email, password) values(?,?);";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, member.getEmail());

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(128, secureRandom);

            String password = member.getPassword();
            String encrypted = AESUtil.encrypt(password, AESUtil.dbIv);
            pstmt.setString(2, encrypted);
            pstmt.executeUpdate();
            return 1;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            System.out.println("회원가입이 안 되어 있습니다.");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
        return -1;
    }

    public void delete(Member member) {
        String deleteSql = "delete from member where email = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, member.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
    }

}

