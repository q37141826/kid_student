package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by lenovo on 2018/1/10.
 * 解析 排序题 options
 */

public class options extends BeanObj {
    private String content;
    private String label;
    private String type;
    private String val;

    public void setContent(String content) {
        this.content = content;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVal(String val) {
        this.val = val;
    }

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
        return "options{" +
                "content='" + content + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", val='" + val + '\'' +
                '}';
    }
}
