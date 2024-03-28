package dbconn;


import user.NotUser;
import user.User;

import java.sql.*;


public class DbConn {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public void select(String table) throws SQLException {
        //Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","acos","1234");
        // 학원에서는 @~: 사이의 값을 192.168.10.17 로 변경해서 쓰고,
        // 집에서는 loaclhost 로 변경해서 table 직접 만들어서 써야함.
        stmt = conn.createStatement();
        String query = "SELECT * FROM " + table;

        rs = stmt.executeQuery(query);
        switch (table) {
            case "usertb":
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setPassword(rs.getString("password"));
                    user.setName(rs.getString("name"));
                    user.setQuestion(rs.getString("question"));
                    user.setAnswer(rs.getString("answer"));

                    System.out.println("[id : " + user.getId()
                            + "] [name : " + user.getName() + "]");
                }
                break;
            case "notusertb":
                while( rs.next()){
                    NotUser nuser = new NotUser();
                    nuser.setPhoneNumber(rs.getString("phonenumber"));
                    nuser.setName(rs.getString("name"));

                    System.out.println("[PhoneNumber :"+ nuser.getPhoneNumber()
                    +"] [name : "+nuser.getName() + "]");
                }
        }
        rs.close();
        stmt.close();
        conn.close();
    }
}
