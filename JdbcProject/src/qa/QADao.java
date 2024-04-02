package qa;

import dbconn.DbConn;

import java.sql.*;

public class QADao {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

public int userQuestion(String id, String question) throws SQLException {
    conn = DbConn.getConnection();
    stmt = conn.createStatement();
    String q = "SELECT * FROM UserQA WHERE id = '"+id+"'";
    rs = stmt.executeQuery(q);
    int rrs = 0;
    if(rs.next()) {
        // 질문 있는 경우
        if(rs.getString("psw629").equals(id)){
            rrs = 1;
            System.out.println("질문내용은 '" +rs.getString("question")+"'입니다");
        } else {
            rrs = 2;
            System.out.println("질문내역이 없습니다.");
        }
    }
    DbConn.close(rs);
    DbConn.close(pstmt);
    DbConn.close(conn);
    return rrs;
}






}
