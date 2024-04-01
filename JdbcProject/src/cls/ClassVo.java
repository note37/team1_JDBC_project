package cls;

public class ClassVo {
    private String title; //수업 이름
    private String teacher; //강사 이름
    private String room; //강의장
    private int applicant; //신청 인원
    private int max; //제한 인원
    private int opennum; //수업 번호
    private String applicantId;
    public ClassVo(int opennum, String title, String teacher, String room, int applicant, int max) {
        this.opennum = opennum;
        this.title = title;
        this.teacher = teacher;
        this.room = room;
        this.applicant = applicant;
        this.max = max;
        this.applicantId = applicantId;
    }
    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
    public int getOpennum() {
        return opennum;
    }

    public String getTitle() {
        return title;
    }

    public void setOpennum(int opennum) {
        this.opennum = opennum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getApplicant() {
        return applicant;
    }

    public void setApplicant(int applicant) {
        this.applicant = applicant;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
