package dao;

import models.Member;
import util.AESUtil;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public String findByPassword(String nickname) {
        String selectSql = "select password from member where nickname = ?;";
        try {
            /* DB connect */
            conn = DBConnection.getConnection();

            /* query conn */
            pstmt = conn.prepareStatement(selectSql);

            /* selectSql ?에 들어갈 data setting */
            pstmt.setString(1, nickname);

            /* selectSql Query 리턴값 받아오기 */
            rs = pstmt.executeQuery();

            /* select가 되면 ID가 있으므로 1 반환*/
            if(rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* login fail */
        return selectSql;
    }

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

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyData = secretKey.getEncoded();

            // TODO: 암호화 처리
            String password = member.getPassword();

//            String encrypted = AES256.encrypt(password, "secretKey");
//            System.out.println("AES-256 : enc - " + encrypted);
            String encrypted = AESUtil.encrypt(password);
            pstmt.setString(2, encrypted);
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }catch (InvalidAlgorithmParameterException e) {
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

