package dbconn;


import user.User;

import java.sql.*;


public class DbConn {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public void selectAll() throws SQLException {
        User user = new User();
        //Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","acos","1234");
        // 학원에서는 @~: 사이의 값을 192.168.10.17 로 변경해서 쓰고,
        // 집에서는 loaclhost 로 변경해서 table 직접 만들어서 써야함.
        stmt = conn.createStatement();
        String query = "SELECT * FROM usertb";

        rs = stmt.executeQuery(query);
        while(rs.next()){
            user.setId(rs.getString("id"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setQuestion(rs.getString("question"));
            user.setAnswer(rs.getString("answer"));

            System.out.println("[id : "+ user.getId()
                    + "] [name : "+user.getName() + "]");
        }
        rs.close();
        stmt.close();
        conn.close();
    }
}
