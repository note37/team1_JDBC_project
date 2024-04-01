package qa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class UserQA {
    public static void main(String[] args) throws SQLException{
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

                // insert conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.17:xe","acos","1234");

                System.out.println("질문이 저장되었습니다");
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("question.txt"));
                    bw.write(question);
                    bw.close();

                    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.17:1521:xe", "acos", "1234");
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO QAuser (id, question) VALUES (?, ?)");
                    pstmt.setString(1, "test");
                    pstmt.setString(2, question);
                    pstmt.executeUpdate();
                    pstmt.close();
                    conn.close();
                    System.out.println("질문이 데이터베이스에 저장되었습니다");
                } catch (IOException | SQLException e) {
                    System.out.println("질문 저장 실패 : " + e.getMessage());
                }

            }

            else if (i == 3) {
                sc.nextLine();
                System.out.println("질문내역 확인");
                UserQA userqa1 = new UserQA();
                String id = sc.nextLine();
                System.out.println(userqa1.loadQA(id));

            }
            else if (i == 4) {
                System.out.println("질문을 종료합니다");
                break;
            } else {
                System.out.println("잘못입력했습니다. 다시 입력하세요");
            }
        }
    }
    public String loadQA(String id) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","acos","1234");
        Statement stmt = conn.createStatement();
        String q = "SELECT question, answer from QAuser WHERE id = '" + id + "'";
        ResultSet rs = stmt.executeQuery(q);
        String rrs;
        if(rs.next()) {
            String qt = rs.getString("question");
            String aw = rs.getString("answer");
            rrs = qt + "에 대한 답변 : " + aw;

        } else {
            rrs = "해당 아이디의 질문 내역이 없습니다";
        } return rrs;
    }
}



