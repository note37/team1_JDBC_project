import cls.ApplyVo;
import cls.ClassDao;
import cls.ClassVo;
import enrolment.EnrolmentDao;
import enrolment.EnrolmentVo;
import qa.QAVo;
import qa.QADao;
import user.NotUser;
import user.NotUserDAO;
import user.User;
import user.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                e.printStackTrace();
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
                String selNum = sc.nextLine();
                int i = 1;
                switch (selNum) {
                    case "1":
                        List<EnrolmentVo> enrList = enrDao.allEnrList(user);
                        for (EnrolmentVo ev : enrList) System.out.println((i++)+ ". 이름 : "+ev.getName()+"  시간 : "+ev.getDate());
                        System.out.println();
                        continue;
                    case "2":
                        List<QAVo> qaList = qaDao.loadQA(user);
                        if(qaList.size() == 0 ) {
                            System.out.println("질문 없음");
                            continue;
                        }
                        System.out.println("========== 질문 목록을 보고 답변을 달 번호를 선택해 주세요. ==========");
                        for(QAVo qa : qaList) {
                            if(qa.getId()!=null) System.out.println((i++) + ". ID : " + qa.getId() + "  질문 내용 : " + qa.getQuestion());
                            else System.out.println((i++) + ". phoneNumber : " + qa.getPhonenumber() + "  질문 내용 : " + qa.getQuestion());
                        }
                        System.out.println();
                        System.out.print("답변 달 번호 : ");
                        String answerSel = sc.nextLine();
                        System.out.print("답변 내용 : ");
                        String answer = sc.nextLine();
                        int aiSel = Integer.parseInt(answerSel)-1;
                        QAVo selQA = qaList.get(aiSel);
                        selQA.setAnswer(answer);
                        if( 1 == qaDao.updateQA(selQA)) System.out.println("답변입력 완료");
                        else System.out.println("답변 입력 오류");
                        continue;
                    case "3":
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
                            System.out.print("[1]: 상담 예약  [2]: 상담 취소  [3]: 상담 조회  [4]: 나가기  :  ");
                            String num = sc.nextLine();
                            switch (num){
                                case "1":
                                    System.out.println("===== 상담 예약 =====");
                                    System.out.println("원하시는 방문 상담 날짜의 년도 입력(24/25): ");
                                    String yearStr = sc.nextLine();
                                    switch (yearStr){
                                        case "24":
                                            break;
                                        case "25":
                                            break;
                                        default:
                                            System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                            continue;
                                    }
                                    System.out.println("원하시는 방문 상담 날짜의 월 입력: ");
                                    String monthStr = sc.nextLine();
                                    switch (monthStr){
                                        case "1":
                                            monthStr = "01";
                                            break;
                                        case "2":
                                            monthStr = "02";
                                            break;
                                        case "3":
                                            monthStr = "03";
                                            break;
                                        case "4":
                                            monthStr = "04";
                                            break;
                                        case "5":
                                            monthStr = "05";
                                            break;
                                        case "6":
                                            monthStr = "06";
                                            break;
                                        case "7":
                                            monthStr = "07";
                                            break;
                                        case "8":
                                            monthStr = "08";
                                            break;
                                        case "9":
                                            monthStr = "09";
                                            break;
                                        case "10":
                                            monthStr = "10";
                                            break;
                                        case "11":
                                            monthStr = "11";
                                            break;
                                        case "12":
                                            monthStr = "12";
                                            break;
                                        default:
                                            System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                            continue;
                                    }
                                    System.out.println("원하시는 방문 상담 날짜의 일 입력: ");
                                    String dayStr = sc.nextLine();
                                    int day = Integer.parseInt(dayStr);
                                    if (day<1 || day>31){
                                        System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                        continue;
                                    }
                                    System.out.println("원하시는 방문 상담 시간 입력(11시/13시/15시) : ");
                                    String selectedTime = sc.nextLine();
                                    switch (selectedTime){
                                        case "11":
                                            break;
                                        case "13":
                                            break;
                                        case "15":
                                            break;
                                        default:
                                            System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                            continue;
                                    }
                                    String enrTime = yearStr + "/" + monthStr + "/" + dayStr + "/" + selectedTime;

                                    System.out.println("문의 내용 입력 : ");
                                    String txt = sc.nextLine();
                                    if (txt.length()>50){
                                        System.out.println("50자 이내로 입력해 주세요.");
                                        continue;
                                    }
                                    if (isUser == 1){
                                        int enrRst = enrDao.insertEnr(user, enrTime, txt);
                                        if (enrRst == 1) System.out.println("회원 상담 예약이 완료 되었습니다.");
                                        else System.out.println("이미 예약된 날짜입니다.");
                                    } else if (isUser == 0) {
                                        int enrRst = enrDao.insertEnr(nUser, enrTime, txt);
                                        if (enrRst == 1) System.out.println("비회원 상담 예약이 완료 되었습니다.");
                                        else System.out.println("이미 예약된 날짜입니다.");
                                    }else {
                                        System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                        continue;
                                    }
                                    break;
                                case "2":
                                    System.out.println("===== 상담 예약 취소 =====");
                                    System.out.print("취소 날짜 입력 (입력 예시 -> 24/04/3/11 )  :  ");
                                    String inputDate = sc.nextLine();
                                    if (isUser == 0){
                                        int cancelRst = enrDao.deleteEnr(nUser,inputDate);
                                        if(cancelRst != 1) System.out.println("해당 날짜에 예약된 상담이 없습니다.");
                                        else System.out.println("상담이 취소되었습니다.");
                                    }else {
                                        int cancelRst = enrDao.deleteEnr(user,inputDate);
                                        if(cancelRst != 1) System.out.println("해당 날짜에 예약된 상담이 없습니다.");
                                        else System.out.println("상담이 취소되었습니다.");
                                    }
                                    continue;
                                case "3":
                                    System.out.println("===== 상담 예약 조회 =====");
                                    System.out.println("현재 상담 예약을 조회합니다.");
                                    List<EnrolmentVo> list;
                                    if(isUser == 1 ){
                                        list = enrDao.allEnrList(user);
                                    }else {
                                        list = enrDao.allEnrList(nUser);
                                    }
                                    for(EnrolmentVo ev : list){
                                        System.out.println("[이름 : " + ev.getName()+"]" +
                                                "[예약 날짜 : " + ev.getDate() + "]" +
                                                "[전화 번호 : " + ev.getPhoneNumber() + "]" +
                                                "[문의 내용 : " + ev.getInquiry() + "]");
                                    }continue;
                                case "4":
                                    System.out.println("상담 종료합니다.");
                                    break;
                            }
                            break;
                        }
                        continue;
                    case 4:
                        while (true) {
                            System.out.println("\n☆★☆★☆★☆★KH정보교육원 수강 신청 메뉴☆★☆★☆★☆★\n");
                            System.out.print("메뉴 선택 : [1]수강 신청 [2]수강 취소 [3]나의 수강신청 조회 [4]나가기 : ");
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
                                    int isAc = dao.printAppliedClasses(appliedClasses);
                                    if(isAc == 0){
                                        System.out.print("취소하실 수업의 번호를 입력하세요 : ");
                                        int askAgain = sc.nextInt();
                                        dao.cancelAllAppliedClasses(appliedClasses.get(askAgain - 1));
                                        System.out.println("취소가 완료되었습니다.");
                                    }
                                    continue;

                                case 3:
                                    System.out.println("\n현재 " + user.getName() + "님이 신청하신 수업 목록입니다.");
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
                        // userQA.QA();
                        while (true) {
                            System.out.println("질문 유형을 선택하세요 : [1]자주하는 질문 [2]직접 질문 [3]질문 내역 [4]종료");
                            String i = sc.nextLine();
                            if (i.equals("1")) {
                                System.out.println("[1]국비교육이란? [2]수강 신청 및 비용 [3]수강방법 및 안내 [4]수료시 취업분야 [아무 입력시 종료됩니다.]");
                                String j = sc.nextLine();
                                if (j.equals("1")) {
                                    System.out.println("국비전액무료과정(국가기간전략산업훈련)은 우리나라의 중심이 되는 산업 분야에서 전문인력 양성이 필요하다고 생각되어 선정된 직종에 대하여 훈련비용을 지원함으로써 인력양성 및 관련 직종으로의 취업을 돕는 제도입니다.");
                                } else if (j.equals("2")) {
                                    System.out.println("수강신청은 학원의 개강일정에 맞추어 진행하시면되며 비용은 과목별로 상이합니다.");
                                } else if (j.equals("3")) {
                                    System.out.println("수강등록시 국비지원과정 지원서 작성 및 수강 등록을 위해 학원으로 내방하셔야하며 내일배움카드가 필요합니다.");
                                } else if (j.equals("4")) {
                                    System.out.println("개발자 양성과정 : JAVA 기반 웹개발자, APP 개발자, ERP/CRM 기업용 솔루션 개발자등");
                                    System.out.println("정보보안 전문가과정 : 정보보안 엔지니어, 컨설턴트, 모의해킹전문가, 정보보안프로그램 개발자등");
                                } else {
                                    break;
                                }
                            } else if (i.equals("2")) {

                                System.out.println("질문할 내용을 입력하세요 : ");
                                String question = sc.nextLine();
                                int rs  = qaDao.insertQA(user,question);
                                if(rs == 1) System.out.println("질문이 저장되었습니다");
                                else System.out.println("질문 저장 실패");

                            } else if (i.equals("3")) {
                                System.out.println("질문내역 확인");
                                List<QAVo> qaList = qaDao.loadQA(user);
                                for(QAVo q : qaList){
                                    System.out.println("Question : " + q.getQuestion() + " /  Answer : " + q.getAnswer());
                                }
                            } else if (i.equals("4")) {
                                System.out.println("질문을 종료합니다");
                                break;
                            } else {
                                System.out.println("잘못입력했습니다. 다시 입력하세요");
                            }
                        }
                        continue;
                    case 6:
                        return;
                }
            }
        }
        else{
            while(true){
                System.out.print("[1]상담  [2]Q&A  [3]로그아웃 : ");
                String selNum = sc.nextLine();
                switch (selNum) {
                    case "1":
                        while (true){
                            System.out.println("=============== 방문 상담 예약 ===============");
                            System.out.print("[1]: 상담 예약  [2]: 상담 취소  [3]: 상담 조회  [4]: 나가기  :  ");
                            String num = sc.nextLine();
                            switch (num){
                                case "1":
                                    System.out.println("===== 상담 예약 =====");
                                    System.out.println("원하시는 방문 상담 날짜의 년도 입력(24/25): ");
                                    String yearStr = sc.nextLine();
                                    switch (yearStr){
                                        case "24":
                                            break;
                                        case "25":
                                            break;
                                        default:
                                            System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                            continue;
                                    }
                                    System.out.println("원하시는 방문 상담 날짜의 월 입력: ");
                                    String monthStr = sc.nextLine();
                                    switch (monthStr){
                                        case "1":
                                            monthStr = "01";
                                            break;
                                        case "2":
                                            monthStr = "02";
                                            break;
                                        case "3":
                                            monthStr = "03";
                                            break;
                                        case "4":
                                            monthStr = "04";
                                            break;
                                        case "5":
                                            monthStr = "05";
                                            break;
                                        case "6":
                                            monthStr = "06";
                                            break;
                                        case "7":
                                            monthStr = "07";
                                            break;
                                        case "8":
                                            monthStr = "08";
                                            break;
                                        case "9":
                                            monthStr = "09";
                                            break;
                                        case "10":
                                            monthStr = "10";
                                            break;
                                        case "11":
                                            monthStr = "11";
                                            break;
                                        case "12":
                                            monthStr = "12";
                                            break;
                                        default:
                                            System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                            continue;
                                    }
                                    System.out.println("원하시는 방문 상담 날짜의 일 입력: ");
                                    String dayStr = sc.nextLine();
                                    int day = Integer.parseInt(dayStr);
                                    if (day<1 || day>31){
                                        System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                        continue;
                                    }
                                    System.out.println("원하시는 방문 상담 시간 입력(11시/13시/15시) : ");
                                    String selectedTime = sc.nextLine();
                                    switch (selectedTime){
                                        case "11":
                                            break;
                                        case "13":
                                            break;
                                        case "15":
                                            break;
                                        default:
                                            System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                            continue;
                                    }
                                    String enrTime = yearStr + "/" + monthStr + "/" + dayStr + "/" + selectedTime;

                                    System.out.println("문의 내용 입력 : ");
                                    String txt = sc.nextLine();
                                    if (txt.length()>50){
                                        System.out.println("50자 이내로 입력해 주세요.");
                                        continue;
                                    }
                                    if (isUser == 1){
                                        int enrRst = enrDao.insertEnr(user, enrTime, txt);
                                        if (enrRst == 1) System.out.println("회원 상담 예약이 완료 되었습니다.");
                                        else System.out.println("이미 예약된 날짜입니다.");
                                    } else if (isUser == 0) {
                                        int enrRst = enrDao.insertEnr(nUser, enrTime, txt);
                                        if (enrRst == 1) System.out.println("비회원 상담 예약이 완료 되었습니다.");
                                        else System.out.println("이미 예약된 날짜입니다.");
                                    }else {
                                        System.out.println("유효하지 않은 입력입니다. 다시 시도하세요.");
                                        continue;
                                    }
                                    break;
                                case "2":
                                    System.out.println("===== 상담 예약 취소 =====");
                                    System.out.print("취소 날짜 입력 (입력 예시 -> 24/04/3/11 )  :  ");
                                    String inputDate = sc.nextLine();
                                    if (isUser == 0){
                                        int cancelRst = enrDao.deleteEnr(nUser,inputDate);
                                        if(cancelRst != 1) System.out.println("해당 날짜에 예약된 상담이 없습니다.");
                                        else System.out.println("상담이 취소되었습니다.");
                                    }else {
                                        int cancelRst = enrDao.deleteEnr(user,inputDate);
                                        if(cancelRst != 1) System.out.println("해당 날짜에 예약된 상담이 없습니다.");
                                        else System.out.println("상담이 취소되었습니다.");
                                    }
                                    continue;
                                case "3":
                                    System.out.println("===== 상담 예약 조회 =====");
                                    System.out.println("현재 상담 예약을 조회합니다.");
                                    List<EnrolmentVo> list;
                                    if(isUser == 1 ){
                                        list = enrDao.allEnrList(user);
                                    }else {
                                        list = enrDao.allEnrList(nUser);
                                    }
                                    for(EnrolmentVo ev : list){
                                        System.out.println("[이름 : " + ev.getName()+"]" +
                                                "[예약 날짜 : " + ev.getDate() + "]" +
                                                "[전화 번호 : " + ev.getPhoneNumber() + "]" +
                                                "[문의 내용 : " + ev.getInquiry() + "]");
                                    }continue;
                                case "4":
                                    System.out.println("상담 종료합니다.");
                                    break;
                            }
                            break;
                        }
                        continue;
                    case "2":
                        while (true) {
                            System.out.println("질문 유형을 선택하세요 : [1]자주하는 질문 [2]직접 질문 [3]질문 내역 [4]종료");
                            String i = sc.nextLine();
                            if (i.equals("1")) {
                                System.out.println("[1]국비교육이란? [2]수강 신청 및 비용 [3]수강방법 및 안내 [4]수료시 취업분야 [아무 입력시 종료됩니다.]");
                                String j = sc.nextLine();
                                if (j.equals("1")) {
                                    System.out.println("국비전액무료과정(국가기간전략산업훈련)은 우리나라의 중심이 되는 산업 분야에서 전문인력 양성이 필요하다고 생각되어 선정된 직종에 대하여 훈련비용을 지원함으로써 인력양성 및 관련 직종으로의 취업을 돕는 제도입니다.");
                                } else if (j.equals("2")) {
                                    System.out.println("수강신청은 학원의 개강일정에 맞추어 진행하시면되며 비용은 과목별로 상이합니다.");
                                } else if (j.equals("3")) {
                                    System.out.println("수강등록시 국비지원과정 지원서 작성 및 수강 등록을 위해 학원으로 내방하셔야하며 내일배움카드가 필요합니다.");
                                } else if (j.equals("4")) {
                                    System.out.println("개발자 양성과정 : JAVA 기반 웹개발자, APP 개발자, ERP/CRM 기업용 솔루션 개발자등");
                                    System.out.println("정보보안 전문가과정 : 정보보안 엔지니어, 컨설턴트, 모의해킹전문가, 정보보안프로그램 개발자등");
                                } else {
                                    break;
                                }
                            } else if (i.equals("2")) {
                                System.out.println("질문할 내용을 입력하세요 : ");
                                String question = sc.nextLine();
                                int rs  = qaDao.insertQA(nUser,question);
                                if(rs == 1) System.out.println("질문이 저장되었습니다");
                                else System.out.println("질문 저장 실패");

                            } else if (i.equals("3")) {
                                System.out.println("질문내역 확인");
                                List<QAVo> qaList = qaDao.loadQA(nUser);
                                for(QAVo q : qaList){
                                    System.out.println("Question : " + q.getQuestion() + " /  Answer : " + q.getAnswer());
                                }
                            } else if (i.equals("4")) {
                                System.out.println("질문을 종료합니다");
                                break;
                            } else {
                                System.out.println("잘못입력했습니다. 다시 입력하세요");
                            }
                        }
                        continue;
                    case "3":
                        break;
                        }
                        break;
                }
            }
    }
}
