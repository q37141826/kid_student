package cn.dajiahui.kid.ui.homework.bean;


/**
 * 基类模型
 */

public class QuestionModle extends BeBaseModle {

    public QuestionModle() {
    }

    /////////////////////////////////////////////////////
    private int currentpage = 0;//记录当前的页数
    private String answerflag = "";
    private String subjectype = "";//当前题型
    private String audiourl = "";
    private boolean isAnswer;//是否作答（记录当前页面check）
    private String submitAnswer = "";//学生作答答案
    private String rightAnswer = "";//当前题正确答案
    private int eachposition;//每个题对应数据源的索引
    private String nomber = "";//第几题

    private int choiceitemshowpic;//选择题item显示的图片源
    private String choiceitemtype = "";//选择答案类型 1 文字 2 图片
    private String choiceanswer = "";//选择题显示的答案（后台提供）

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

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
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

    public String getNomber() {
        return nomber;
    }

    public void setNomber(String nomber) {
        this.nomber = nomber;
    }

    public int getChoiceitemshowpic() {
        return choiceitemshowpic;
    }

    public void setChoiceitemshowpic(int choiceitemshowpic) {
        this.choiceitemshowpic = choiceitemshowpic;
    }



    public String getChoiceanswer() {
        return choiceanswer;
    }

    public void setChoiceanswer(String choiceanswer) {
        this.choiceanswer = choiceanswer;
    }

    public int getChoiceitemposition() {
        return choiceitemposition;
    }

    public void setChoiceitemposition(int choiceitemposition) {
        this.choiceitemposition = choiceitemposition;
    }
}