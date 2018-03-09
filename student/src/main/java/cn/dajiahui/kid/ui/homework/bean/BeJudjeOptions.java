package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/1/11.
 * 判断题 Options
 */

public class BeJudjeOptions implements Serializable {
    private String content;
    private String label;
    private String type;
    private String val;

    public String getContent() {
        return content;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public String getVal() {
        return val;
    }
}
