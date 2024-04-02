package qa;

public class QAVo {
    private String id; // 아이디
    private String question; // 질문
    private String answer; // 답변

    public QAVo(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }
public String getId() {
        return id;
}

public void setgetId(String id) {
    this.id = id;
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
