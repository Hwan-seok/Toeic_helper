package org.androidtown.my_project.Activity.Mypage;

public class ListViewItem {
    private int Number ;
    private String ProblemStr ;
    private String option1 ;
    private String option2 ;
    private String option3 ;
    private String option4 ;
    private String answer ;

    public void setNumber(int number){
        Number = number;
    }

    public void setProblemStr(String problemStr) {
        ProblemStr = problemStr;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getNumber() {
        return this.Number;
    }

    public String getProblemStr() {
        return this.ProblemStr;
    }

    public String getOption1() {
        return this.option1;
    }

    public String getOption2() {
        return this.option2;
    }

    public String getOption3() {
        return this.option3;
    }

    public String getOption4() {
        return this.option4;
    }

    public String getAnswer() {
        return this.answer;
    }
}
