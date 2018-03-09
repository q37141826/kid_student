package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/1/11.
 * 判断题 Options
 */

public class BeChoiceOptions  implements Serializable{
    private String content;
    private String label;//后台的选择题的答案
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

    @Override
    public String toString() {
        return "BeChoiceOptions{" +
                "content='" + content + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", val='" + val + '\'' +
                '}';
    }
}
