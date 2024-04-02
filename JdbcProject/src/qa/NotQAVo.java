package qa;

public class NotQAVo {
    private String phonenumber; // 비회원 전화번호
    private String question; // 질문
    private String answer; // 답변

    public NotQAVo(String phonenumber, String question, String answer) {
        this.phonenumber = phonenumber;
        this.question = question;
        this.answer = answer;
    }
public String getPhonenumber() {
        return phonenumber;
}

public void setgetPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
}

public String getQuestion() {
        return question;
}

public void setgetQuestion(String question) {
        this.question = question;
}

public String getAnswer() {
        return answer;
}

public void setgetAnswer(String answer) {
        this.answer = answer;
}

}
