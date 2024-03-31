package user;

import java.sql.*;

public class UserDAO {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    // logIn -> ID, Password 입력
    // return 0 : ID 없음 / 1 : 성공 / 2 : 비밀 번호 틀림
    public int logIn(String id,String pw) throws SQLException {
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","acos","1234");
        stmt = conn.createStatement();
        String q = "SELECT id, password FROM usertb WHERE id = '"+id+"'";
        rs = stmt.executeQuery(q);
        int rrs;
        if(rs.next()){
            // id 있는 경우
            if(rs.getString("password").equals(pw)){
                rrs = 1; // 로그인 성공
            }else{
                rrs = 2; //비밀 번호 다름
            }
        }else{
            // select 문으로 id를 찾지 못했을 때 -> id가 없는것 or 잘 못 입력한 것
            rrs = 0;
        }

        rs.close();
        stmt.close();
        conn.close();
        return rrs;
    }
}
