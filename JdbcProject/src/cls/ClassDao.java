package cls;

import dbconn.DbConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClassDao {
    Connection conn = null;
    Statement stmt = null; // createStatement 방식
    PreparedStatement pStmt = null; // Prepared Statement 방식
    ResultSet rs = null; // database 로 부터 결과를 받는 변수
    Scanner sc = new Scanner(System.in);

    //SELECT 문 (조회)
    public List<ClassVo> ClassSelect() {
        List<ClassVo> list = new ArrayList<>();
        try {
            conn = DbConn.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM ClassTb";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int openNum = rs.getInt("OPENNUM");
                String title = rs.getString("TITLE");
                String teacher = rs.getString("TEACHER");
                String room = rs.getString("ROOM");
                int applicant = rs.getInt("APPLICANT");
                int max = rs.getInt("MAX");
                list.add(new ClassVo(openNum, title, teacher, room, applicant, max));
            }
            DbConn.close(rs);
            DbConn.close(stmt);
            DbConn.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public void classSelectPrn(List<ClassVo> list) {
        for (ClassVo e : list) {
            System.out.print(e.getOpennum() + ".");
            System.out.print(e.getTitle() + " ");
            System.out.print(e.getTeacher() + " ");
            System.out.print(e.getRoom() + " ");
            System.out.print("[" + e.getApplicant() + "]/");
            System.out.println("[" + e.getMax() + "]");
        }
    }


    public void applyForClass(int openNum, String userId, String userName) {
        try {
            // 데이터베이스 연결
            conn = DbConn.getConnection();

            // SQL 쿼리문 작성: 선택한 수업 번호에 해당하는 수업 정보 가져오기
            String selectSql = "SELECT * FROM ClassTb WHERE OPENNUM = ?";
            pStmt = conn.prepareStatement(selectSql);
            pStmt.setInt(1, openNum);
            ResultSet rs = pStmt.executeQuery();

            // 해당 수업 정보가 있는지 확인
            if (rs.next()) {
                int currentApplicant = rs.getInt("APPLICANT");
                int max = rs.getInt("MAX");
                String title = rs.getString("TITLE");
                String room = rs.getString("ROOM");

                // 현재 신청 인원이 최대 인원을 초과하지 않으면
                if (currentApplicant < max) {
                    // SQL 쿼리문 작성: 선택한 수업의 신청 인원을 1 증가시킴
                    String updateSql = "UPDATE ClassTb SET APPLICANT = APPLICANT + 1 WHERE OPENNUM = ?";
                    pStmt = conn.prepareStatement(updateSql);
                    pStmt.setInt(1, openNum); // SQL 쿼리문의 ? 위치에 수업 번호를 설정
                    int result = pStmt.executeUpdate();

                    // 업데이트 결과에 따라 메시지 출력
                    if (result > 0) {
                        System.out.println("수업 신청이 완료되었습니다.");

                        // SQL 쿼리문 작성: USERAPPLY 테이블에 수업 신청 정보 추가
                        String insertSql = "INSERT INTO APPLYUSER (ID, NAME, TITLE, ROOM) VALUES (?, ?, ?, ?)";
                        pStmt = conn.prepareStatement(insertSql);
                        pStmt.setString(1, userId);
                        pStmt.setString(2, userName); // 전달받은 userName 사용
                        pStmt.setString(3, title);
                        pStmt.setString(4, room);
                        pStmt.executeUpdate();
                    } else {
                        System.out.println("수업 신청에 실패했습니다. 다시 시도해주세요.");
                    }
                } else {
                    // 최대 인원을 초과한 경우 에러 메시지 출력
                    System.out.println("신청 인원이 초과되었습니다.");
                }
            } else {
                // 해당 수업이 존재하지 않는 경우 에러 메시지 출력
                System.out.println("해당 수업이 존재하지 않습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            DbConn.close(rs);
            DbConn.close(pStmt);
            DbConn.close(conn);
        }
    }
}
