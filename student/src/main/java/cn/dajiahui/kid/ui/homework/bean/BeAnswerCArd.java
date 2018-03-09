package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/2/23.
 * <p>
 * 提交答题卡
 */

public class BeAnswerCArd implements Serializable {
    private String is_auto = "";
    private String is_right = "";
    private String my_answer = "";
    private String question_cate_id = "";
    private String question_id = "";

    private int current_num;//当前是第几题
    private String answerFlag = "";//作答标记
    private String is_complete;//是否完成

    /*正在答题时显示的答题卡*/
    public BeAnswerCArd(int current_num, String answerFlag) {
        this.current_num = current_num;
        this.answerFlag = answerFlag;

    }

    public BeAnswerCArd(
            String is_auto, String is_right,
            String my_answer, String question_cate_id,
            String question_id, int current_num) {

        this.is_auto = is_auto;
        this.is_right = is_right;
        this.my_answer = my_answer;
        this.question_cate_id = question_cate_id;
        this.question_id = question_id;
        this.current_num = current_num;
    }

    public int getCurrent_num() {
        return current_num;
    }

    public void setCurrent_num(int current_num) {
        this.current_num = current_num;
    }

    public String getAnswerFlag() {
        return answerFlag;
    }

    public void setAnswerFlag(String answerFlag) {
        this.answerFlag = answerFlag;
    }

    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }

    public String getIs_right() {
        return is_right;
    }

    public void setIs_right(String is_right) {
        this.is_right = is_right;
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

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }


}
