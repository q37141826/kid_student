package cn.dajiahui.kid.ui.homework.bean;

import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by lenovo on 2018/1/10.
 */

public class BeBaseModle extends BeanObj {

    private String book_id;
    private String id;//区别第几题
    private String media;//音频媒体文件
    private List<options> options;//选项内容
    private String org_id;
    private String question_stem;//代表题干
    private String school_id;
    private String standard_answer;//参考答案
    private String title;
    private String unit_id;
    private String question_cate_id;


    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setOptions(List<cn.dajiahui.kid.ui.homework.bean.options> options) {
        this.options = options;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public void setQuestion_stem(String question_stem) {
        this.question_stem = question_stem;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public void setStandard_answer(String standard_answer) {
        this.standard_answer = standard_answer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public void setQuestion_cate_id(String question_cate_id) {
        this.question_cate_id = question_cate_id;
    }


    public String getQuestion_cate_id() {
        return question_cate_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getId() {
        return id;
    }

    public String getMedia() {
        return media;
    }

    public List<cn.dajiahui.kid.ui.homework.bean.options> getOptions() {
        return options;
    }

    public String getOrg_id() {
        return org_id;
    }

    public String getQuestion_stem() {
        return question_stem;
    }

    public String getSchool_id() {
        return school_id;
    }

    public String getStandard_answer() {
        return standard_answer;
    }

    public String getTitle() {
        return title;
    }

    public String getUnit_id() {
        return unit_id;
    }

    @Override
    public String toString() {
        return "BeBaseModle{" +
                "book_id='" + book_id + '\'' +
                ", id='" + id + '\'' +
                ", media='" + media + '\'' +
                ", options=" + options +
                ", org_id='" + org_id + '\'' +
                ", question_stem='" + question_stem + '\'' +
                ", school_id='" + school_id + '\'' +
                ", standard_answer='" + standard_answer + '\'' +
                ", title='" + title + '\'' +
                ", unit_id='" + unit_id + '\'' +
                '}';
    }
}
