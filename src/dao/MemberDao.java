package dao;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import models.Member;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MemberDao {
    private static MemberDao instance=new MemberDao();
    public static MemberDao getInstance() {
        return instance;
    }

    /* DB 연결 객체 */
    private Connection conn;

    /* SQL Query를 사용할 객체 */
    private PreparedStatement pstmt = null;

    /* SQL Query 결과 객체 */
    private ResultSet rs;

    /* Success 1, Fail -1 */
    public int findByUsernameAndPassword(String nickname, String password) {
        String selectSql = "select * from member where nickname = ? and password = ?;";
        try {
            /* DB connect */
            conn = DBConnection.getConnection();

            /* query conn */
            pstmt = conn.prepareStatement(selectSql);

            /* selectSql ?에 들어갈 data setting */
            pstmt.setString(1, nickname);
            pstmt.setString(2, password);

            /* selectSql Query 리턴값 받아오기 */
            rs = pstmt.executeQuery();

            /* select가 되면 ID가 있으므로 1 반환*/
            if(rs.next()) { 
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* login fail */
        return -1;
    }

    /* Success 1, Fail -1 */
    public int save(Member member){
        String insertSql = "insert into member(nickname, password) values(?,?);";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, member.getNickname());

            // TODO: 암호화 처리
            String password = member.getPassword();
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /* 탈퇴 시 */
    public void delete(String nickname) {
        String deleteSql = "delete from member where nickname = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, nickname);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Success 1, Fail all except 1 */
    public int checkNickname(Member member){
        int count = 1;
        String selectSql = "select count(*) as c from member where nickname = ?;";
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, member.getNickname());
            rs = pstmt.executeQuery();

            if (rs.next()){
                count -= rs.getInt("c");
            }

            return count;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 1;
    }
}

