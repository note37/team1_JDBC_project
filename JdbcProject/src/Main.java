import cls.ApplyVo;
import cls.ClassDao;
import cls.ClassVo;
import dbconn.DbConn;
import enrolment.EnrolmentDao;
import enrolment.EnrolmentVo;
import qa.QADao;
import qa.QAVo;
import user.NotUser;
import user.NotUserDAO;
import user.User;
import user.UserDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static int startNum = 0;
    static Scanner sc = new Scanner(System.in);
    static User user = null;
    static NotUser nUser = null;
    static int isUser;
    static ClassDao dao = new ClassDao();
    static UserDAO uDao = new UserDAO();
    static NotUserDAO nuDao = new NotUserDAO();
    static EnrolmentDao enrDao = new EnrolmentDao();
    static QADao qaDao = new QADao();
    static String name;
    public static void main(String[] args) throws SQLException {
        while(true) {
            try {
                System.out.println();
                System.out.println("========================================KH 정보 교육원========================================");
                System.out.print("[1]회원 로그인  [2]비회원 로그인  [3]회원 가입  [4]비밀 번호 찾기 [5]종료 : ");
                String  signInSel = sc.nextLine();
                UserDAO uDao;
                switch (signInSel){
                    case "1" :
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
                                    isUser = 1;
                                    name = user.getName();
                                    break;
                                case 2:
                                    System.out.println("비밀 번호가 틀렸습니다.");
                                    continue;
                                default:
                                    System.out.println("!예기치 않은 오류!");
                                    continue;
                            }
                            step1();
                        continue;
                    case "2" :
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
                                    isUser = 0;
                                    name = nUser.getName();
                                    break;
                                default:
                                    System.out.println("예기치 않은 오류!");
                                    continue;
                            }
                            break;
                        }
                        step1();
                        continue;
                    case "3":
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
                    case "4":
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
                    case "5":
                        return;
                    default:
                        System.out.println("1, 2, 3, 4, 5 번 중에 선택해 주세요.");
                }
            }
            catch (Exception e){
                System.out.println("잘못 입력하셨습니다. 다시 입력해 주세요.");
            }
        }
    }

    public static void step1() throws SQLException {
        System.out.println();
        System.out.println("○●○●○●○●○●○●    '"+ name +"'님 어서오세요    ○●○●○●○●○●○●");
        if(isUser==1 && user.getId().equals("master")){
            // 운영자 계정일 때
            while(true) {
                System.out.print("[1]상담 확인 [2]Q&A 확인 [3]로그아웃 : ");
                int selNum = sc.nextInt();
                sc.nextLine();
                int i = 1;
                switch (selNum) {
                    case 1:
                        List<EnrolmentVo> enrList = enrDao.allEnrList();
                        for (EnrolmentVo ev : enrList) System.out.println((i++)+ ". 이름 : "+ev.getName()+"  시간 : "+ev.getDate());
                        System.out.println();
                        continue;
                    case 2:
                        System.out.print("[1]회원 질문 보기  [2]비회원 질문 보기  : ");
                        String qaSel = sc.nextLine();
                        if(qaSel.equals("1")) { //회원 질문 답변
                            while(true) {
                                System.out.println("========== 질문 목록을 보고 답변을 달 번호를 선택해 주세요.(나가기 : q) ==========");
                                String isQuit = sc.nextLine().toLowerCase();
                                if(isQuit.equals("q")) break;
                                List<QAVo> qaList = qaDao.questionSelect();
                                for (QAVo qa : qaList)
                                    System.out.println((i++) + ". ID : " + qa.getId() + "  질문 내용 : " + qa.getQuestion());
                                System.out.println();

                            }
                        }else if(qaSel.equals("2")) { // 비회원 질문 답변
                            while(true) {
                                System.out.println("========== 질문 목록을 보고 답변을 달 번호를 선택해 주세요.(나가기 : q) ==========");
                                String isQuit = sc.nextLine().toLowerCase();
                                if(isQuit.equals("q")) break;
                            }
                        }
                        continue;
                    case 3:
                        break;

                }
                break;
            }
        } else if(isUser == 1){
            while(true){
                System.out.print("[1]회원 탈퇴  [2]내 정보 수정  [3]상담  [4]수강  [5]Q&A  [6]로그아웃 : ");
                int selNum = sc.nextInt();
                sc.nextLine();
                switch (selNum) {
                    case 1:
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
                            System.out.print("무엇을 변경하겠습니까? [1]이름  [2]비밀번호  [3]본인확인 답변 : ");
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
                        uDao.updateUser(user);
                        System.out.println("○●○●○●○●○●○●변경되었습니다.○●○●○●○●○●○●");
                        continue;
                    case 3:
                        while (true){
                        System.out.println("=============== 방문 상담 예약 ===============");
                        System.out.print("[1] : 상담 예약\n[2] : 상담 취소\n [3] : 상담 조회\n [4] : 나가기");
                        int num = sc.nextInt();
                        switch (num){
                            case 1:
                                System.out.println("===== 상담 예약 =====");
                                System.out.println("원하시는 방문 상담 날짜를 입력해 주세요 : ");
                                String enrDate = sc.next();
                                System.out.println("문의 내용 : ");
                                String txt = sc.next();
                                if (isUser == 1){
                                    int enrRst = enrDao.insertEnr(user, enrDate, txt);
                                }


                            case 2:
                                System.out.println("===== 상담 예약 취소 =====");
                                System.out.println("예약하신 이름 : ");
                            case 3:
                            case 4:
                        }
                    }
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
                                    continue;
                                case 2:
                                    System.out.println("\n현재 " + user.getName() + "님이 신청한 수업 목록입니다.");
                                    List<ApplyVo> appliedClasses = dao.getAppliedClasses(user.getId());
                                    dao.printAppliedClasses(appliedClasses);
                                    System.out.print("취소하실 강의에 번호를 입력하세요 : ");
                                    int askAgain = sc.nextInt();

                                    dao.cancelAllAppliedClasses(appliedClasses.get(askAgain-1));
                                    System.out.println("취소가 완료되었습니다.");

                                    continue;

                                case 3:
                                    System.out.println("\n현재 " + user.getName() + "님이 신청한 수업 목록입니다.");
                                    List<ApplyVo> appliedClasses1 = dao.getAppliedClasses(user.getId());
                                    dao.printAppliedClasses(appliedClasses1);
                                    continue;
                                case 4:
                                    break;

                                default:
                                    System.out.println("메뉴를 다시 선택하세요");
                            }
                            break;
                        }
                        continue;
                    case 5:
                    case 6:
                        return;
                }
            }
        }
        else{
            while(true){
                System.out.print("[1]상담  [2]Q&A  [3]로그아웃 : ");
                int selNum = sc.nextInt();
                sc.nextLine();
                switch (selNum) {
                    case 1:
                    case 2:
                        }
                        break;
                }
            }
    }
}
