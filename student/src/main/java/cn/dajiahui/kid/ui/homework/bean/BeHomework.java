package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 作业列表中的每个Item
 */

public class BeHomework extends BeanObj {

    private String book_id;
    private String book_name;
    private String class_id;
    private String class_name;
    private String correct_rate;
    private String end_time;//截止时间
    private String id;
    private String is_checked;//是否提交
    private String name;//单元名字
    private String start_time;//开始时间
    private String unit_id;
    private String is_complete;//未开始 0:进行中 1:已完成


    public String getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(String is_complete) {
        this.is_complete = is_complete;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getCorrect_rate() {
        return correct_rate;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getId() {
        return id;
    }

    public String getIs_checked() {
        return is_checked;
    }

    public String getName() {
        return name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getUnit_id() {
        return unit_id;
    }

    @Override
    public String toString() {
        return "BeHomework{" +
                "book_id='" + book_id + '\'' +
                ", book_name='" + book_name + '\'' +
                ", class_id='" + class_id + '\'' +
                ", class_name='" + class_name + '\'' +
                ", correct_rate='" + correct_rate + '\'' +
                ", end_time='" + end_time + '\'' +
                ", id='" + id + '\'' +
                ", is_checked='" + is_checked + '\'' +
                ", name='" + name + '\'' +
                ", start_time='" + start_time + '\'' +
                ", unit_id='" + unit_id + '\'' +
                '}';
    }
}
