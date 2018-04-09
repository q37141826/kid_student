package cn.dajiahui.kid.ui.homework.bean;


import java.io.Serializable;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基类模型
 */

public class QuestionModle implements Serializable {

    private String answerflag = "";//学生作答标记（是否 作答 true false ）
    private int eachposition;//每个题对应数据源的索引
    private int choiceitemposition = -1;//记录选择题选的索引（用于翻页回来设置指定选项处于备选状态）
    private boolean Answer = false;//练习模块中  是否作答
    private int currentAnswerPosition = -1;
    /*排序题我的答案*/
    private List<String> initSortMyanswerList = new ArrayList<>();//我的答案的集合（val值）;//初始我的答案集合（用于获取我的答案顺序）
    /*连线题我的答案集合*/
    private Map<String, String> initLineMyanswerMap = new LinkedHashMap<>();//连线题我的答案集合（val对应）
    /*填空题我的答案集合 作业*/
    private LinkedHashMap<Integer, LinkedHashMap<Integer, CompletionQuestionadapterItemModle>> mCompletionAllMap = new LinkedHashMap<>();

    /*连线题我的答案集合*/
    private Map<Integer, Integer> exinitLineMyanswerMap = new LinkedHashMap<>();//连线题我的答案集合（val对应）

    /*各种题型公共拥有*/
    private String id;
    private String is_answered;//0 未回答  1 已回答
    private String is_auto;//0手动 1自动
    private String is_complete;//完成状态：-1：未开始， 0：进行中， 1：已完成
    private String is_right;//1正确 0错误
    private String media;//音频地址
    private String my_answer;//我的答案
    private String question_cate_id;//题干ID
    private String question_stem;//题干的内容（图片）
    private String standard_answer;//题干的内容（图片）
    private String title;//标题


    /*Set Get 方法*/
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

    public boolean isAnswer() {
        return Answer;
    }

    public void setAnswer(boolean answer) {
        Answer = answer;
    }

    public List<String> getInitSortMyanswerList() {
        return initSortMyanswerList;
    }

    public void setInitSortMyanswerList(List<String> initSortMyanswerList) {
        this.initSortMyanswerList = initSortMyanswerList;
    }

    public Map<String, String> getInitLineMyanswerMap() {
        return initLineMyanswerMap;
    }

    public void setInitLineMyanswerMap(Map<String, String> initLineMyanswerMap) {
        this.initLineMyanswerMap = initLineMyanswerMap;
    }

    public Map<Integer, Integer> getExinitLineMyanswerMap() {
        return exinitLineMyanswerMap;
    }

    public void setExinitLineMyanswerMap(Map<Integer, Integer> exinitLineMyanswerMap) {
        this.exinitLineMyanswerMap = exinitLineMyanswerMap;
    }

    public LinkedHashMap<Integer, LinkedHashMap<Integer, CompletionQuestionadapterItemModle>> getmCompletionAllMap() {
        return mCompletionAllMap;
    }

    public void setmCompletionAllMap(LinkedHashMap<Integer, LinkedHashMap<Integer, CompletionQuestionadapterItemModle>> mCompletionAllMap) {
        this.mCompletionAllMap = mCompletionAllMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_answered() {
        return is_answered;
    }

    public void setIs_answered(String is_answered) {
        this.is_answered = is_answered;
    }

    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }

    public String getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(String is_complete) {
        this.is_complete = is_complete;
    }

    public String getIs_right() {
        return is_right;
    }

    public void setIs_right(String is_right) {
        this.is_right = is_right;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMy_answer() {
        return my_answer;
    }

    public void setMy_answer(String my_answer) {
        this.my_answer = my_answer;
    }

    public String getQuestion_cate_id() {
        return question_cate_id;
    }

    public void setQuestion_cate_id(String question_cate_id) {
        this.question_cate_id = question_cate_id;
    }

    public String getQuestion_stem() {
        return question_stem;
    }

    public void setQuestion_stem(String question_stem) {
        this.question_stem = question_stem;
    }

    public String getStandard_answer() {
        return standard_answer;
    }

    public void setStandard_answer(String standard_answer) {
        this.standard_answer = standard_answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrentAnswerPosition() {
        return currentAnswerPosition;
    }

    public void setCurrentAnswerPosition(int currentAnswerPosition) {
        this.currentAnswerPosition = currentAnswerPosition;
    }


}