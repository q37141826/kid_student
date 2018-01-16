package cn.dajiahui.kid.ui.homework.bean;

import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 选择题
 */

public class ChoiceQuestionModle extends QuestionModle{


    private String book_id;
    private String id;
    private String media;//音频媒体文件
    private List<BeChoiceOptions> options;//选项内容
    private String org_id;
    private String question_stem;//代表题干
    private String school_id;
    private String standard_answer;//参考答案
    private String title;
    private String unit_id;
    private String is_answer;//是否作答
    private String my_answer;//已经上传的作答答案
    private String is_right;//是否正确
    private String is_auto;//

    public String getIs_answer() {
        return is_answer;
    }

    public void setIs_answer(String is_answer) {
        this.is_answer = is_answer;
    }

    public String getMy_answer() {
        return my_answer;
    }

    public void setMy_answer(String my_answer) {
        this.my_answer = my_answer;
    }

    public String getIs_right() {
        return is_right;
    }

    public void setIs_right(String is_right) {
        this.is_right = is_right;
    }

    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
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

    public List<BeChoiceOptions> getOptions() {
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

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setOptions(List<BeChoiceOptions> options) {
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

}
