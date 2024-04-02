package qa;

import dbconn.DbConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QADao {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

public List<QAVo> questionSelect() throws SQLException{
    List<QAVo> list = new ArrayList<>();
    try {
        conn = DbConn.getConnection();
        stmt = conn.createStatement();
        String sql = "SELECT * FROM QAUSER";
        rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String id = rs.getString("id");
            String question = rs.getString("question");
            String answer = rs.getString("answer");
            list.add(new QAVo(id, question, answer));
        }
        DbConn.close(rs);
        DbConn.close(stmt);
        DbConn.close(conn);
    } catch(Exception e) {
        e.printStackTrace();
    }
    return list;
    }
    public int insertQA(String id, String question) throws SQLException {
        conn = DbConn.getConnection();
        String insertSql = "INSERT INTO QAUSER (id, question) VALUES (?, ?)";
        pstmt = conn.prepareStatement(insertSql);
        pstmt.setString(1, id);
        pstmt.setString(2, question);
        // pstmt.setString(3, answer);

        int rst = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rst;

    }

}




// insert, delete