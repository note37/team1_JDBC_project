package user;

import java.sql.*;

public class NotUserDAO {
    public static void main(String[] args) throws SQLException{
        NotUserDAO nuDAO = new NotUserDAO();
        int i  = nuDAO.signIn("010-1234-5678","박상원");
        System.out.println(i);
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String oracleURL = "jdbc:oracle:thin:@localhost:1521:xe";
    String oracleID = "acos";
    String oraclePW = "1234";

    //비회원 로그인
    //최초 1회 바로 회원가입
    //그 이후 이름과 휴대전화 번호로, 번호와 이름 다를 시 로그인 실패
    //휴대전화 번호에 이름 같은 경우, 처음 가입하는 회원 회원가입 됐을 때 return 1,
    // 번호에 이름이 다르면 return 0
    public int signIn(String ph , String name) throws SQLException {
        conn = DriverManager.getConnection(oracleURL,oracleID,oraclePW);
        String q = "SELECT * FROM notusertb WHERE phonenumber = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,ph);
        rs = pstmt.executeQuery();
        int rrs = 0;
        if(rs.next()){
            if(name.equals(rs.getString("name"))){
                rrs = 1;
            }else{
                rrs = 0;
            }
        }else{
            q = "INSERT INTO notusertb (phonenumber,name) VALUES (?,?)";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1,ph);
            pstmt.setString(2,name);
            rrs = pstmt.executeUpdate();
        }
        return rrs;
    }
}
