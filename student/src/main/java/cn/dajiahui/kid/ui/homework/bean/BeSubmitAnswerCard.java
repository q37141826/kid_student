package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/2/7.
 * <p>
 * 提交答题卡
 */

public class BeSubmitAnswerCard implements Serializable {

    private String question_id;//当前题的id
    private String question_cate_id;//题型
    private String my_answer;//作答答案
    private String is_right;//是否正确
    private String is_auto;//自动提交（一共12道题  做到10道题就提交了  后两道题就自动提交  默认是1  自动提交时1）
    private String is_answered;//0 未回答  1 已回答


    public BeSubmitAnswerCard(String question_id, String question_cate_id, String my_answer, String is_right, String is_auto, String is_answered) {
        this.question_id = question_id;
        this.question_cate_id = question_cate_id;
        this.my_answer = my_answer;
        this.is_right = is_right;
        this.is_auto = is_auto;
        this.is_answered = is_answered;
    }

    public String getIs_answered() {
        return is_answered;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion_cate_id() {
        return question_cate_id;
    }

    public String getMy_answer() {
        return my_answer;
    }

    public String getIs_right() {
        return is_right;
    }

    public String getIs_auto() {
        return is_auto;
    }

    @Override
    public String toString() {
        return "BeSubmitAnswerCard{" +
                "question_id='" + question_id + '\'' +
                ", question_cate_id='" + question_cate_id + '\'' +
                ", my_answer='" + my_answer + '\'' +
                ", is_right='" + is_right + '\'' +
                ", is_auto='" + is_auto + '\'' +
                ", is_answered='" + is_answered + '\'' +
                '}';
    }
}
