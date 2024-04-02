package enrolment;

import dbconn.DbConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EnrolmentDao {
    public static void main(String[] args) throws SQLException {
        EnrolmentDao dao = new EnrolmentDao();
        List<EnrolmentVo> list = dao.allEnrList();
        for(EnrolmentVo ev : list){
            System.out.println("[이름 : " + ev.getName()+"]" +
                    "[예약 날짜 : " + ev.getDate() + "]" +
                    "[전화 번호 : " + ev.getPhoneNumber() + "]" +
                    "[문의 내용 : " + ev.getInquiry() + "]");
        }
    }
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;

    public List<EnrolmentVo> allEnrList() throws SQLException {
        List<EnrolmentVo> list = new ArrayList<>();
        conn = DbConn.getConnection();
        String q = "SELECT * FROM ENROLMENT";
        pStmt = conn.prepareStatement(q);
        rs = pStmt.executeQuery();

        while (rs.next()){
            String name = rs.getString("name");
            String date = rs.getString("ENRDATE");
            String phoneNumber = rs.getString("phonenumber");
            String inquiry = rs.getString("inquiry");
            String id = rs.getString("id");
            EnrolmentVo ev = new EnrolmentVo(name,date,phoneNumber,inquiry,id);

            list.add(ev);
        }
        return list;
    }

    public int user_pe() throws SQLException{
        conn = DbConn.getConnection();
        stmt = conn.createStatement();
        String q = "SELECT ID FROM ENROLMENT";
        pStmt = conn.prepareStatement(q);
        rs = pStmt.executeQuery();

        int count = 0;
        if (rs.next()){
            count = 1;
        }else {
            count = 0;
        }
        return count;
    }
    public void insertData() throws SQLException {
        conn = DbConn.getConnection();
        String q;
        if (user_pe() == 1) {
            // 로그인 상태인 경우
            q = "INSERT INTO YourTableName (NAME,  ENRDATE, INQUIRY, ID) VALUES (?, ?, ?, ?)";
            pStmt = conn.prepareStatement(q);
            // 여기서 ?에 해당하는 값들을 설정
            pStmt.setString(1, "nameValue");
            pStmt.setString(2, "idValue");
            pStmt.setDate(3, new Date(System.currentTimeMillis())); // 현재 날짜 설정, ENRDATE 컬럼은 날짜 형식이라고 가정
        } else {
            // 비로그인 상태인 경우
            q = "INSERT INTO YourTableName (NAME, ENRDATE, PHONENUMBER, INQUIRY) VALUES (?, ?, ?, ?)";
            pStmt = conn.prepareStatement(q);
            // 여기서 ?에 해당하는 값들을 설정
            pStmt.setString(1, "nameValue");
            pStmt.setString(2, "phoneNumberValue");
            pStmt.setDate(3, new Date(System.currentTimeMillis())); // 현재 날짜 설정, ENRDATE 컬럼은 날짜 형식이라고 가정
            pStmt.setString(4, "inquiryValue");
        }
        // 쿼리 실행
        pStmt.executeUpdate();
    }
}
