package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/2/6.
 * <p>
 * 作业详情的答题卡
 */

public class BeAnswerSheet implements Serializable {
    private String id;
    private String is_answered;
    private String is_auto;    /*1代表自动提交  0 手动提交*/
    private String is_right;//1 正确
    private String my_answer;
    private String standard_answer;

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

    public String getStandard_answer() {
        return standard_answer;
    }

    public void setStandard_answer(String standard_answer) {
        this.standard_answer = standard_answer;
    }

    @Override
    public String toString() {
        return "BeAnswerSheet{" +
                "id='" + id + '\'' +
                ", is_answered='" + is_answered + '\'' +
                ", is_auto='" + is_auto + '\'' +
                ", is_right='" + is_right + '\'' +
                ", my_answer='" + my_answer + '\'' +
                ", standard_answer='" + standard_answer + '\'' +
                '}';
    }
}
