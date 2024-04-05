package qa;

import dbconn.DbConn;
import user.NotUser;
import user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QADao {
    public void QA() throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("질문 유형을 선택하세요 : [1]자주하는 질문 [2]직접 질문 [3]질문 내역 [4]종료");
            int i = sc.nextInt();
            if (i == 1) {
                System.out.println("[1]국비교육이란? [2]수강 신청 및 비용 [3]수강방법 및 안내 [4]수료시 취업분야");
                int j = sc.nextInt();
                if (j == 1) {
                    System.out.println("국비전액무료과정(국가기간전략산업훈련)은 우리나라의 중심이 되는 산업 분야에서 전문인력 양성이 필요하다고 생각되어 선정된 직종에 대하여 훈련비용을 지원함으로써 인력양성 및 관련 직종으로의 취업을 돕는 제도입니다.");
                } else if (j == 2) {
                    System.out.println("수강신청은 학원의 개강일정에 맞추어 진행하시면되며 비용은 과목별로 상이합니다.");
                } else if (j == 3) {
                    System.out.println("수강등록시 국비지원과정 지원서 작성 및 수강 등록을 위해 학원으로 내방하셔야하며 내일배움카드가 필요합니다.");
                } else if (j == 4) {
                    System.out.println("개발자 양성과정 : JAVA 기반 웹개발자, APP 개발자, ERP/CRM 기업용 솔루션 개발자등");
                    System.out.println("정보보안 전문가과정 : 정보보안 엔지니어, 컨설턴트, 모의해킹전문가, 정보보안프로그램 개발자등");
                } else {
                    System.out.println("잘못 입력하였습니다");
                }
            } else if (i == 2) {

                System.out.println("질문할 내용을 입력하세요 : ");
                sc.nextLine();
                String question = sc.nextLine();

                System.out.println("질문이 저장되었습니다");
                try {

                    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.17:1521:xe", "acos", "teamone");
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO QAuser (id, question) VALUES (?, ?)");
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();
                    System.out.println("질문이 데이터베이스에 저장되었습니다");
                } catch (Exception e) {
                    System.out.println("질문 저장 실패 : " + e.getMessage());
                }

            } else if (i == 3) {
                sc.nextLine();
                System.out.println("질문내역 확인");
                QADao userqa1 = new QADao();
                String id = sc.nextLine();
                System.out.println(userqa1.loadQA(new User()));

            } else if (i == 4) {
                System.out.println("질문을 종료합니다");
                break;
            } else {
                System.out.println("잘못입력했습니다. 다시 입력하세요");
            }
        }
    }

    public List<QAVo> loadQA(User user) throws SQLException {
        String rrs = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<QAVo> qaList = new ArrayList<QAVo>();
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.17:1521:xe", "acos", "teamone");
            stmt = conn.createStatement();
            //아이디가 master면 where을 주지말고 그게 아니면 아래 where절 넣어서
            String q;
            if (user.getId().equals("master")) {
                q = "SELECT * from qatb WHERE answer is null";
            } else {
                q = "SELECT * from qatb WHERE id = '" + user.getId() + "'";
            }
            rs = stmt.executeQuery(q);
                if(user.getId().equals("master")) {
                    while(rs.next()) {
                        QAVo qvo;
                        if(rs.getString("id").equals(""))
                            qvo = new QAVo(rs.getString("phonenumber"), rs.getString("question"), "");
                        else qvo = new QAVo(rs.getString("id"), rs.getString("question"), "");
                        qaList.add(qvo);
                    }
                } else {
                    while(rs.next()) {
                        QAVo qvo = new QAVo(rs.getString("id"), rs.getString("question"),rs.getString("answer"));
                        qaList.add(qvo);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DbConn.close(rs);
        DbConn.close(stmt);
        DbConn.close(conn);
        return qaList;
    }

    public List<QAVo> loadQA(NotUser nUser) throws SQLException {
        String rrs = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<QAVo> qaList = new ArrayList<QAVo>();
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.17:1521:xe", "acos", "teamone");
            stmt = conn.createStatement();
            //아이디가 master면 where을 주지말고 그게 아니면 아래 where절 넣어서
            String q = "SELECT * from qatb WHERE id = '" + nUser.getPhoneNumber()+ "'";
            rs = stmt.executeQuery(q);
            while(rs.next()) {
                QAVo qvo = new QAVo(rs.getString("phonenumber"), rs.getString("question"),rs.getString("answer"));
                qaList.add(qvo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DbConn.close(rs);
        DbConn.close(stmt);
        DbConn.close(conn);
        return qaList;
    }

    public int updateQA(QAVo qav) throws SQLException{
        Connection conn = DbConn.getConnection();
        PreparedStatement pstmt;
        String q;
        if(!qav.getId().equals("")) {
            q = "UPDATE qatb SET answer = ? WHERE id = ? AND question = ?";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1,qav.getAnswer());
            pstmt.setString(2,qav.getId());
            pstmt.setString(3,qav.getQuestion());
        }else{
            q = "UPDATE qatb SET answer = ? WHERE phonenumber = ? AND question = ?";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1, qav.getAnswer());
            pstmt.setString(2,qav.getPhonenumber());
            pstmt.setString(3,qav.getQuestion());
        }
        int rs = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rs;
    }

    //1성공
    public int insertQA(User user, String question) throws SQLException{
        Connection conn = DbConn.getConnection();
        PreparedStatement pstmt= null;
        String q = "INSERT INTO qatb(id,question) VALUES(?,?)";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,user.getId());
        pstmt.setString(2,question);
        int rs = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rs;
    }

    //1 성공
    public int insertQA(NotUser nUser, String question) throws SQLException{
        Connection conn = DbConn.getConnection();
        PreparedStatement pstmt= null;
        String q = "INSERT INTO qatb(phonenumber,question) VALUES(?,?)";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,nUser.getPhoneNumber());
        pstmt.setString(2,question);
        int rs = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rs;
    }

    public void deleteQA(User user, String question) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConn.getConnection();
            String deleteSql = "DELETE FROM qatb WHERE ID = ? AND QUESTION = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, user.getId());
            pstmt.setString(2, question);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(pstmt);
            DbConn.close(conn);
        }
    }

    public void deleteQA(NotUser notUser, String question) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConn.getConnection();
            String deleteSql = "DELETE FROM qatb WHERE phonenumber = ? AND QUESTION = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setString(1, notUser.getPhoneNumber());
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


