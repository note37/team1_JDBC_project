import dbconn.DbConn;
import user.NotUser;
import user.NotUserDAO;
import user.User;
import user.UserDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        User user = null;
        NotUser nUser;
        System.out.println();
        System.out.println("========================================KH 정보 교육원========================================");
        while(true) {
            try {
                System.out.print("[1]회원 로그인  [2]비회원 로그인  [3]회원 가입  [4]비밀 번호 찾기 : ");
                int signInSel = sc.nextInt();
                sc.nextLine();
                UserDAO uDao;
                switch (signInSel){
                    case 1 :
                        uDao = new UserDAO();
                        while(true) {
                            System.out.print("아이디 입력 : ");
                            String inputID = sc.nextLine();
                            if(inputID.getBytes().length>15){
                                System.out.println("너무 길어요");
                                continue;
                            }
                            System.out.print("비밀번호 입력 : ");
                            String password = sc.nextLine();
                            int signInRst = uDao.signIn(inputID, password);
                            switch (signInRst) {
                                case 0:
                                    System.out.println("없는 아이디 입니다.");
                                    continue;
                                case 1:
                                    System.out.println("로그인 성공");
                                    user = uDao.userInfo(inputID, password);
                                    break;
                                case 2:
                                    System.out.println("비밀 번호가 틀렸습니다.");
                                    continue;
                                default:
                                    System.out.println("!예기치 않은 오류!");
                                    continue;
                            }
                            break;
                        }
                        break;
                    case 2 :
                        NotUserDAO nuDao = new NotUserDAO();
                        while(true) {
                            System.out.print("휴대전화 번호 입력 : ");
                            String inputPh = sc.nextLine();
                            System.out.print("이름 입력 : ");
                            String inputName = sc.nextLine();
                            int notUserSignInRst = nuDao.signIn(inputPh, inputName);
                            switch (notUserSignInRst) {
                                case 0:
                                    System.out.println("번호와 이름이 다릅니다.");
                                    continue;
                                case 1:
                                    System.out.println("비회원 로그인 성공");
                                    nUser = nuDao.getInfo(inputPh);
                                    break;
                                default:
                                    System.out.println("예기치 않은 오류!");
                                    continue;
                            }
                            break;
                        }
                        break;
                    case 3:
                        uDao = new UserDAO();
                        while(true) {
                            System.out.print("아이디 입력 : ");
                            String inputID = sc.nextLine();
                            if (inputID.getBytes().length > 15) {
                                System.out.println("너무 길어요");
                                continue;
                            }
                            int signUpRst = uDao.signUp(inputID);
                            switch (signUpRst) {
                                case 0:
                                    System.out.println("이미 존재하는 아이디 입니다!");
                                    continue;
                                case 1:
                                    System.out.println("아이디 생성 완료");
                                    break;
                            }
                            break;
                        }
                        continue;
                    case 4:
                        uDao = new UserDAO();
                        while(true){
                            System.out.print("비밀번호를 찾을 아이디 입력 : ");
                            String inputID = sc.nextLine();
                            int findPwRst = uDao.findPassword(inputID);
                            if( findPwRst == 0) System.out.println("아이디가 없습니다.");
                            else{
                                System.out.println("변경되었습니다.");
                                break;
                            }
                        }
                        continue;
                    default:
                        System.out.println("1, 2, 3, 4 번 중에 선택해 주세요.");
                        continue;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("잘못 입력하셨습니다. 다시 입력해 주세요.");
            }
            break;
        }
        // 로그인 페이지 끝, 다음 선택 페이지
        // 회원 탈퇴, 정보 수정, 상담 예약, 수강 신청, Q&A 선택
        System.out.println();
        System.out.println("===============현재 접속중인 계정 : "+ user.getId()+" ===============");
        if(user.getId().equals("operator")){
            // 운영자 계정일 때
            
        }else{
            // 일반 사용자 일 때
            while(true){
                System.out.println("[1]회원 탈퇴  [2]내 정보 수정  [3]상담  [4]수강  [5]Q&A  [6]종료");
                int selNum = sc.nextInt();
                sc.nextLine();
                switch (selNum){
                    case 1:
                        UserDAO uDao = new UserDAO();
                        int wdRst = uDao.withDraw(user.getId(),user.getPassword());
                        if(wdRst == 1){
                            System.out.println("삭제 되었습니다.");
                            break;
                        }
                        else if(wdRst == 0){
                            System.out.println("본인확인 답변이 틀렸습니다.");
                            continue;
                        }else{
                            System.out.println("삭제가 취소되었습니다.");
                            continue;
                        }
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        System.out.println("프로그램을 종료합니다.");
                        return;
                }
            }
        }
    }
}