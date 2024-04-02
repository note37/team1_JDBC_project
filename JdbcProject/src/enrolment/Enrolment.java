package enrolment;

import java.util.Scanner;

// 수강신청
public class Enrolment {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("상담 신청[1] 상담 취소[2] 상담 조회[3] 나가기[4]");
        while (true){
            int num = sc.nextInt();
            switch (num){
                case 1:
                    Scanner a = new Scanner(System.in);
                    System.out.println("상담 신청 하실 수강을 선택해 주세요. [1]");
                case 2:
                    System.out.println("2번 수업을 신청 하시겠습니까?");
                case 3:
                    System.out.println("3번 수업을 신청 하시겠습니까?");
                case 4:
                    System.out.println("4번 수업을 신청 하시겠습니까?");
            }
        }
    }
}
