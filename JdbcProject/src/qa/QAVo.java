package qa;

public class QAVo {
    private String id; // 아이디
    private String phonenumber;
    private String question; // 질문
    private String answer; // 답변
    public QAVo() {}

    public QAVo(String val1, String question, String answer) {
        if(val1.contains("-")){
            this.phonenumber = val1;
        }else{
            this.id = val1;
        }
        this.question = question;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
