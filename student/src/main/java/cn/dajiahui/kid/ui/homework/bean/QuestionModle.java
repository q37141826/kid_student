package cn.dajiahui.kid.ui.homework.bean;


import java.io.Serializable;

/**
 * 基类模型
 */

public class QuestionModle extends BeBaseModle  implements Serializable {

    public QuestionModle() {
    }

    /*答题卡使用*/
    public QuestionModle(int currentpage, String answerflag) {
        this.currentpage = currentpage;
        this.answerflag = answerflag;
    }

    /////////////////////////////////////////////////////
    private int currentpage = 0;//记录当前的页数
    private String answerflag = "";//学生作答标记（是否 作答 true false ）
    private String subjectype = "";//当前题型

    private boolean isAnswer;//是否作答
    private String submitAnswer = "";//学生作答答案
    private String rightAnswer = "";//当前题正确答案
    private int eachposition;//每个题对应数据源的索引


    private int choiceitemposition = -1;//记录选择题选的索引（用于翻页回来设置指定选项处于备选状态）


    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public String getAnswerflag() {
        return answerflag;
    }

    public void setAnswerflag(String answerflag) {
        this.answerflag = answerflag;
    }

    public String getSubjectype() {
        return subjectype;
    }

    public void setSubjectype(String subjectype) {
        this.subjectype = subjectype;
    }


    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public String getSubmitAnswer() {
        return submitAnswer;
    }

    public void setSubmitAnswer(String submitAnswer) {
        this.submitAnswer = submitAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getEachposition() {
        return eachposition;
    }

    public void setEachposition(int eachposition) {
        this.eachposition = eachposition;
    }


    public int getChoiceitemposition() {
        return choiceitemposition;
    }

    public void setChoiceitemposition(int choiceitemposition) {
        this.choiceitemposition = choiceitemposition;
    }


    @Override
    public String toString() {
        return "QuestionModle{" +
                "currentpage=" + currentpage +
                ", answerflag='" + answerflag + '\'' +
                ", subjectype='" + subjectype + '\'' +
                ", isAnswer=" + isAnswer +
                ", submitAnswer='" + submitAnswer + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", eachposition=" + eachposition +


                ", choiceitemposition=" + choiceitemposition +
                '}';
    }
}