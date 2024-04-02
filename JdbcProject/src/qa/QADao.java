package qa;

import dbconn.DbConn;
import user.NotUser;
import user.User;

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
        String sql = "SELECT * FROM QAUSER WHERE ANSWER IS NULL";
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
    public int insertQA(User user, String id, String question) throws SQLException {
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
    public void deleteQA(User user, String id, String question) throws SQLException {
        try {
            conn = DbConn.getConnection();
            String deleteSql = "DELETE FROM QAUSER WHERE ID = ? AND QUESTION = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, id);
            pstmt.setString(2, question);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(pstmt);
            DbConn.close(conn);
        }
    }

    /// 비회원 유저 //////////////
    public List<NotQAVo> noQuestionSelect() throws SQLException{
        List<NotQAVo> list = new ArrayList<>();

        try {
            conn = DbConn.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM QANOTUSER WHERE ANSWER IS NULL";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String phonenumber = rs.getString("phonenumber");
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                list.add(new NotQAVo(phonenumber, question, answer));
            }
            DbConn.close(rs);
            DbConn.close(stmt);
            DbConn.close(conn);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }
public int insertQA(NotUser notUser, String phonenumber, String question) throws SQLException {
        conn = DbConn.getConnection();
        String insertSql = "INSERT INTO QANOTUSER (phonenumber, question) VALUES (?, ?)";
        pstmt = conn.prepareStatement(insertSql);
        pstmt.setString(1, phonenumber);
        pstmt.setString(2, question);


        int rst = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rst;
    }
    public void deleteQA(NotUser notUser, String id, String question) throws SQLException {
        try {
            conn = DbConn.getConnection();
            String deleteSql = "DELETE FROM QAUSER WHERE ID = ? AND QUESTION = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, id);
            pstmt.setString(2, question);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(pstmt);
            DbConn.close(conn);
        }
    }
}




