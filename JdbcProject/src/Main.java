import cls.ClassDao;
import cls.ClassVo;
import dbconn.DbConn;
import user.NotUser;
import user.NotUserDAO;
import user.User;
import user.UserDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        User user = null;
        ClassDao dao = new ClassDao();
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
                            inputID = sc.nextLine();
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
                            inputID = sc.nextLine();
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
                switch (selNum) {
                    case 1:
                        UserDAO uDao = new UserDAO();
                        int wdRst = uDao.withDraw(user.getId(), user.getPassword());
                        if (wdRst == 1) {
                            System.out.println("삭제 되었습니다.");
                            break;
                        } else if (wdRst == 0) {
                            System.out.println("본인확인 답변이 틀렸습니다.");
                            continue;
                        } else {
                            System.out.println("삭제가 취소되었습니다.");
                            continue;
                        }
                    case 2:
                        while (true){
                            System.out.print("무엇을 변경하겠습니까? [1]이름  [2]비밀번호  [3]본인확인 답변");
                            int updateUserSel = sc.nextInt();
                            sc.nextLine();
                            switch (updateUserSel) {
                                case 1:
                                    System.out.print("변경할 이름 입력 : ");
                                    String updateName = sc.nextLine();
                                    if(updateName.getBytes().length > 30){
                                        System.out.println("이름이 너무 길어요");
                                        continue;
                                    }
                                    user.setName(updateName);
                                    break;
                                case 2:
                                    System.out.print("변경할 비밀번호 입력 : ");
                                    String updatePw = sc.nextLine();
                                    if(updatePw.getBytes().length>15){
                                        System.out.println("비밀번호가 너무 길어요");
                                        continue;
                                    }
                                    user.setPassword(updatePw);
                                    break;
                                case 3:
                                    System.out.println("본인확인 질문 선택 \n" +
                                            "[1]꿈이 뭔가요?\n" +
                                            "[2]취미가 뭔가요?\n" +
                                            "[3]가장 좋아하는 음식이 뭔가요?");
                                    System.out.print("번호 입력 : ");
                                    int selectQ = sc.nextInt();
                                    String question = "";
                                    switch (selectQ){
                                        case 1 : question = "꿈이 뭔가요?"; break;
                                        case 2 : question = "취미가 뭔가요?"; break;
                                        case 3 : question = "가장 좋아하는 음식이 뭔가요?"; break;
                                        default:
                                            System.out.println("1~3번 중 선택 바랍니다.");
                                            continue;
                                    }
                                    sc.nextLine();
                                    System.out.print("답변 입력 : ");
                                    String answer = sc.nextLine();
                                    if(answer.length() > 10){
                                        System.out.println("답변은 10자 이내로 입력 바랍니다.");
                                        continue;
                                    }
                                    user.setQuestion(question);
                                    user.setAnswer(answer);
                                    break;
                                default:
                                    System.out.println("잘 못 입력하셨습니다.");
                                    continue;
                            }
                            break;
                        }
                        System.out.println();
                        System.out.println("=============== 변경되었습니다. ===============");
                        System.out.println("유저 이름 : " + user.getName());
                        continue;
                    case 3:
                    case 4:
                        while (true) {
                            System.out.println("\n☆★☆★☆★☆★KH정보교육원 수강 신청 메뉴☆★☆★☆★☆★\n");
                            System.out.print("메뉴 선택 : [1]수강신청 [2]수강 취소 [3]나의 수강신청 조회 [4]나가기 : ");
                            int sel = sc.nextInt();
                            switch (sel) {
                                case 1:
                                    System.out.println("\n현재 개설된 수업 목록입니다.");
                                    System.out.println("--------------------------");
                                    List<ClassVo> list = dao.ClassSelect();
                                    dao.classSelectPrn(list);
                                    System.out.println();
                                    System.out.print("어떤 수업을 신청하시겠습니까?(번호로 입력하세요) : ");
                                    int choice = sc.nextInt();
                                    dao.applyForClass(choice, user.getId(), user.getName());
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    return;


                                default:
                                    System.out.println("메뉴를 다시 선택하세요");
                            }


                        }
                    case 5:
                    case 6:
                        System.out.println("프로그램을 종료합니다.");
                        return;
                }
            }
        }
    }
}