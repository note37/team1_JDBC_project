package qa;

import dbconn.DbConn;

import java.sql.DriverManager;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UserQA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("질문 유형을 선택하세요 : [1]자주하는 질문 [2]직접 질문 [3]질문 내역 [4]종료");
            int i = sc.nextInt();
            if (i == 1) {
                System.out.println("[1]국비교육이란? [2]Class 관련 질문 [3]수강방법 및 안내 [4]수료시 취업분야");
                int j = sc.nextInt();
                if (j == 1) {
                    System.out.println("[1]국비교육답변"); // 수정해야함
                } else if (j == 2) {
                    System.out.println("[2]Class 관련 질문 답변"); // 수정해야함
                } else if (j == 3) {
                    System.out.println("[3]수강방법 및 안내 답변"); // 수정해야함
                } else if (j == 4) {
                    System.out.println("[4]수료시 취업분야 답변"); // 수정해야함
                } else {
                    System.out.println("잘못 입력하였습니다");
                }
            } else if (i == 2) {

                System.out.println("질문할 내용을 입력하세요 : ");
                sc.nextLine();
                String question = sc.nextLine();

                // insert conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:xe","acos","1234");

                System.out.println("질문이 저장되었습니다");
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("question.txt"));
                    bw.write(question);
                    bw.close();

                } catch (IOException e) {
                    System.out.println("질문 저장 실패 : " + e.getMessage());
                }
            }

            else if (i == 3) {
                System.out.println("질문내역 확인"); // 과거에 한 질문을 어떻게 저장할 것이며 3번을 눌렀을경우 어떻게 불러올 것인가
                // 3번을 만들려면 db에 연결해서 불러와야함. 2번처럼 txt 파일로 질문내역들을 저장시켜놓고 해야할듯
                // db에 연결해서 txt 파일을 보낸다음 커밋해서.
            }
            else if (i == 4) {
                System.out.println("질문을 종료합니다");
                break;
            } else {
                System.out.println("잘못입력했습니다. 다시 입력하세요");
            }
        }
    }
}




