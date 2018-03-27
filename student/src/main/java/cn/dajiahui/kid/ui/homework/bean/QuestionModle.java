package cn.dajiahui.kid.ui.homework.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基类模型
 */

public class QuestionModle extends BeBaseModle implements Serializable {

    /////////////////////////////////////////////////////
    private int currentpage = 0;//记录当前的页数
    private String answerflag = "";//学生作答标记（是否 作答 true false ）
    private int eachposition;//每个题对应数据源的索引
    private int choiceitemposition = -1;//记录选择题选的索引（用于翻页回来设置指定选项处于备选状态）

    private String SourceFlag = "";//来源标记  区别是作业还是练习
    private String is_answered;//0 未回答  1 已回答
    private boolean Answer = false;//练习模块中  是否作答

    /*排序题我的答案*/
    private List<String> initSortMyanswerList = new ArrayList<>();//我的答案的集合（val值）;//初始我的答案集合（用于获取我的答案顺序）

    /*连线题我的答案集合*/
    private Map<String, String> initLineMyanswerMap = new LinkedHashMap<>();//连线题我的答案集合（val对应）

    private Map<Integer, Map<Integer, String>> mCompletionAllMap = new HashMap<>();//填空題我的答案


    public Map<Integer, Map<Integer, String>> getmCompletionAllMap() {
        return mCompletionAllMap;
    }

    public void setmCompletionAllMap(Map<Integer, Map<Integer, String>> mCompletionAllMap) {
        this.mCompletionAllMap = mCompletionAllMap;
    }
    public Map<String, String> getInitLineMyanswerMap() {
        return initLineMyanswerMap;
    }

    public void setInitLineMyanswerMap(Map<String, String> initLineMyanswerMap) {
        this.initLineMyanswerMap = initLineMyanswerMap;
    }

    public List<String> getInitSortMyanswerList() {
        return initSortMyanswerList;
    }

    public void setInitSortMyanswerList(List<String> initSortMyanswerList) {
        this.initSortMyanswerList = initSortMyanswerList;
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


}