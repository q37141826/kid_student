package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * 基础模型就取各种题型
 */

public class BeBaseModle implements Serializable {

    private String question_cate_id;//代表题干

    public String getQuestion_cate_id() {
        return question_cate_id;
    }

    public void setQuestion_cate_id(String question_cate_id) {
        this.question_cate_id = question_cate_id;
    }

}
