package cn.dajiahui.kid.ui.homework.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基类模型
 */

public class QuestionModle extends BeBaseModle implements Serializable {

    public QuestionModle() {
    }

    /*答题卡使用*/
    public QuestionModle(int currentpage) {
        this.currentpage = currentpage;

    }

    /////////////////////////////////////////////////////
    private int currentpage = 0;//记录当前的页数
    private String answerflag = "";//学生作答标记（是否 作答 true false ）
    private int eachposition;//每个题对应数据源的索引
    private int choiceitemposition = -1;//记录选择题选的索引（用于翻页回来设置指定选项处于备选状态）
    private int allTotal = 0;//所有题的数量
    private String SourceFlag = "";//来源标记  区别是作业还是练习
    private String is_answered;//0 未回答  1 已回答
    private boolean Answer = false;//练习模块中  是否作答

    /*排序题我的答案*/
    private List<String> initMyanswerList = new ArrayList<>();//我的答案的集合（val值）

    /*连线题我的答案集合*/
    private Map<String, String> myanswerMap = new HashMap<>();//我的答案集合（val对应）


    public Map<String, String> getMyanswerMap() {
        return myanswerMap;
    }

    public void setMyanswerMap(Map<String, String> myanswerMap) {
        this.myanswerMap = myanswerMap;
    }


    public List<String> getInitMyanswerList() {
        return initMyanswerList;
    }

    public void setInitMyanswerList(List<String> initMyanswerList) {
        this.initMyanswerList = initMyanswerList;
    }

    public String getIs_answered() {
        return is_answered;
    }

    public void setIs_answered(String is_answered) {
        this.is_answered = is_answered;
    }

    private int currentAnswerPosition = -1;

    public int getCurrentAnswerPosition() {
        return currentAnswerPosition;
    }

    public void setCurrentAnswerPosition(int currentAnswerPosition) {
        this.currentAnswerPosition = currentAnswerPosition;
    }

    public void setAnswer(boolean answer) {
        Answer = answer;
    }

    public boolean isAnswer() {
        return Answer;
    }

    public String getSourceFlag() {
        return SourceFlag;
    }

    public void setSourceFlag(String sourceFlag) {
        SourceFlag = sourceFlag;
    }

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

//
//    public String getSubmitAnswer() {
//        return submitAnswer;
//    }
//
//    public void setSubmitAnswer(String submitAnswer) {
//        this.submitAnswer = submitAnswer;
//    }

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

    public int getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(int allTotal) {
        this.allTotal = allTotal;
    }


}