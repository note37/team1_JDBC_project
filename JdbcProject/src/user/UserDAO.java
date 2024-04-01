package user;

import java.sql.*;
import java.util.Scanner;

public class UserDAO {
    public static void main(String[] args) throws SQLException {
        UserDAO uDao = new UserDAO();
        int i = uDao.findPassword("test2");
        System.out.println(i);
    }
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;


    // logIn -> ID, Password 입력
    // return 0 : ID 없음 / 1 : 성공 / 2 : 비밀 번호 틀림
    public int signIn(String id,String pw) throws SQLException {
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "acos", "1234");
        stmt = conn.createStatement();
        String q = "SELECT id, password FROM usertb WHERE id = '"+id+"'";
        rs = stmt.executeQuery(q);
        int rrs;
        if(rs.next()){
            // id 있는 경우
            if(rs.getString("password").equals(pw)){
                rrs = 1; // 로그인 성공
            }else{
                rrs = 2; //비밀 번호 다름
            }
        }else{
            // select 문으로 id를 찾지 못했을 때 -> id가 없는것 or 잘 못 입력한 것
            rrs = 0;
        }
        rs.close();
        stmt.close();
        conn.close();
        return rrs;
    }

    //회원가입 , ID 중복 확인 먼저 한 뒤 Password, 이름, 본인확인 질문, 답변 입력
    // return 0 -> ID 중복, return 1 -> 아이디 생성
    public int signUp(String id) throws SQLException{
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "acos", "1234");
        stmt = conn.createStatement();
        String q = "SELECT id, password FROM usertb WHERE id = '"+id+"'";
        int rrs = 0;
        rs = stmt.executeQuery(q);
        if(!rs.next()){
            // 없는 경우
            // console 에서 사용할 구현부
            Scanner sc = new Scanner(System.in);
            String pw;
            String name;
            String question;
            String answer;
            while(true){
                try {
                    System.out.print("비밀번호 입력 : ");
                    pw = sc.nextLine();
                    char[] pwArr = pw.toCharArray();
                    int pwVal = 0;
                    for(char c : pwArr){
                        if(c < 48) {
                            pwVal = 0;
                            break;
                        }
                        else if(c>=58 && c<65) {
                            pwVal = 0;
                            break;
                        }
                        else if(c>=91 && c<97) {
                            pwVal = 0;
                            break;
                        }
                        else if(c>122){
                            pwVal = 0;
                            break;
                        }
                        pwVal = 1;
                    }
                    if(pwVal == 0) {
                        System.out.println("비밀 번호는 영어와 숫자로만 입력 바랍니다.");
                        continue;
                    }
                    System.out.print("이름 입력 : ");
                    name = sc.nextLine();
                    if(name.length()>10){
                        System.out.println("이름은 10자 이내로 입력 바랍니다.");
                        continue;
                    }
                    System.out.println("본인확인 질문 선택 \n" +
                            "[1]꿈이 뭔가요?\n" +
                            "[2]취미가 뭔가요?\n" +
                            "[3]가장 좋아하는 음식이 뭔가요?");
                    System.out.print("번호 입력 : ");
                    int selectQ = sc.nextInt();
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
                    answer = sc.nextLine();
                    if(answer.length() > 10){
                        System.out.println("답변은 10자 이내로 입력 바랍니다.");
                        continue;
                    }
                    break;
                }catch (Exception e){
                    System.out.println("값을 잘못 입력하셨습니다.");
                }
            }
//            q = "INSERT INTO usertb VALUES ('"+id+"',"
//                    +"'"+pw+"','"+name+"','"+question+"','"+answer+"')";
//            int insertRs = stmt.executeUpdate(q);
//            if(insertRs == 1) rrs = 1;
//            else rrs = 0;
            q = "INSERT INTO usertb VALUES(?,?,?,?,?)";
            try {
                pstmt = conn.prepareStatement(q);
                pstmt.setString(1,id);
                pstmt.setString(2,pw);
                pstmt.setString(3,name);
                pstmt.setString(4,question);
                pstmt.setString(5,answer);
                rrs = pstmt.executeUpdate();
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        rs.close();
        stmt.close();
        pstmt.close();
        conn.close();
        return rrs;
    }

    
    //비밀번호 찾기, ID값 받고 본인확인 질문 보여준 뒤 답변 받고 맞으면
    //새 비밀번호 입력 후 변경
    // return 0 -> 아이디 없음, return 1 -> 수정완료
    public int findPassword(String id) throws SQLException{
        conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "acos", "1234");
        String q = "SELECT * FROM usertb WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,id);
        rs = pstmt.executeQuery();
        String password;
        int rrs = 0;
        if(rs.next()){
            String question = rs.getString("question");
            System.out.println("본인확인 질문 : "+question);
            Scanner sc = new Scanner(System.in);
            System.out.print("답 : ");
            String answer = sc.nextLine();
            if(rs.getString("answer").equals(answer)){
                while(true) {
                    System.out.print("비밀번호 재설정 : ");
                    password = sc.nextLine();
                    char[] pwArr = password.toCharArray();
                    int pwVal = 0;
                    for(char c : pwArr){
                        if(c < 48) {
                            pwVal = 0;
                            break;
                        }
                        else if(c>=58 && c<65) {
                            pwVal = 0;
                            break;
                        }
                        else if(c>=91 && c<97) {
                            pwVal = 0;
                            break;
                        }
                        else if(c>122){
                            pwVal = 0;
                            break;
                        }
                        pwVal = 1;
                    }
                    if(pwVal == 0) {
                        System.out.println("비밀 번호는 영어와 숫자로만 입력 바랍니다.");
                        continue;
                    }
                    break;
                }
                q = "UPDATE usertb SET password = ? WHERE id = ?";
                pstmt = conn.prepareStatement(q);
                pstmt.setString(1, password);
                pstmt.setString(2,id);
                rrs = pstmt.executeUpdate();
            }
        }else{
            rrs = 0;
        }
        rs.close();
        pstmt.close();
        conn.close();
        return rrs;
    }
}