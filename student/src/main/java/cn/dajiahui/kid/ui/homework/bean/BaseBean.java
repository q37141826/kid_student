package cn.dajiahui.kid.ui.homework.bean;


import cn.dajiahui.kid.util.BeanObj;

/**
 * 基类模型
 */

public class BaseBean extends BeanObj {

    public BaseBean() {
    }

    private boolean answerflag;
    private int subjectype = 0;//当前题型
    private int currentpage = 0;//记录当前的页数
    private String audiourl = "";
    private boolean Whetheranswer;//是否作答（记录当前页面check）

    private String answer = "";//学生作答答案
    private String trueAnswer = "";//当前题正确答案

    private int eachposition;//每个题对应数据源的索引
    private int answernum;//答题数量
    private String nomber = "";//第几题


    public BaseBean(int subjectype, String trueAnswer, String nomber) {
        this.subjectype = subjectype;
        this.trueAnswer = trueAnswer;
        this.nomber = nomber;
    }


    public String getNomber() {
        return nomber;
    }

    public void setNomber(String nomber) {
        this.nomber = nomber;
    }

    public int getAnswernum() {
        return answernum;
    }

    public void setAnswernum(int answernum) {
        this.answernum = answernum;
    }


    public int getEachposition() {
        return eachposition;
    }

    public void setEachposition(int eachposition) {
        this.eachposition = eachposition;
    }


    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }


    public boolean getAnswerflag() {
        return answerflag;
    }

    public void setAnswerflag(boolean answerflag) {
        this.answerflag = answerflag;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getaudiourl() {
        return audiourl;
    }

    public void setaudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public boolean isWhetheranswer() {
        return Whetheranswer;
    }

    public void setWhetheranswer(boolean whetheranswer) {
        Whetheranswer = whetheranswer;
    }

    public int getSubjectype() {
        return subjectype;
    }

    public void setSubjectype(int subjectype) {
        this.subjectype = subjectype;
    }

    @Override
    public int getCurrentpage() {
        return currentpage;
    }

    @Override
    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "answerflag='" + answerflag + '\'' +
                ", subjectype=" + subjectype +
                ", currentpage=" + currentpage +
                ", audiourl='" + audiourl + '\'' +
                ", Whetheranswer=" + Whetheranswer +
                ", answer=" + answer +
                ", trueAnswer='" + trueAnswer + '\'' +
                '}';
    }
}
