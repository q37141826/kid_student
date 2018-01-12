package cn.dajiahui.kid.ui.homework.bean;

import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 排序题模型
 */

public class SortQuestionModle  extends QuestionModle{

    private String book_id;
    private String id;
    private String media;//音频媒体文件
    private List<BeSortOptions> options;//选项内容
    private String org_id;
    private String question_stem;//代表题干
    private String school_id;
    private String standard_answer;//参考答案
    private String title;
    private String unit_id;

    public String getBook_id() {
        return book_id;
    }

    public String getId() {
        return id;
    }

    public String getMedia() {
        return media;
    }

    public List<BeSortOptions> getOptions() {
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
}
