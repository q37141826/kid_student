package cn.dajiahui.kid.ui.homework.bean;

import java.util.List;

/**
 * Created by mj on 2018/2/6.
 * <p>
 * 作业详情初级页面
 */

public class BeHomeWorkDetails {


    private String all_students;
    private List<BeAnswerSheet> answer_sheet;
    private String complete_students;//学生完成人数
    private String complete_time;//完成时间
    private String correct_rate;//单个学生的准确率
    private String end_time;//结束时间
    private String is_complete;//是否完成
    private String name;
    private String question_cnt;
    private String start_time;
    private String unit_id;


    public String getAll_students() {
        return all_students;
    }

    public List<BeAnswerSheet> getAnswer_sheet() {
        return answer_sheet;
    }

    public String getComplete_students() {
        return complete_students;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public String getCorrect_rate() {
        return correct_rate;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getIs_complete() {
        return is_complete;
    }

    public String getName() {
        return name;
    }

    public String getQuestion_cnt() {
        return question_cnt;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getUnit_id() {
        return unit_id;
    }

    @Override
    public String toString() {
        return "BeHomeWorkDetails{" +
                "all_students='" + all_students + '\'' +
                ", answer_sheet=" + answer_sheet +
                ", compelete_students='" +  complete_students + '\'' +
                ", complete_time='" + complete_time + '\'' +
                ", correct_rate='" + correct_rate + '\'' +
                ", end_time='" + end_time + '\'' +
                ", is_complete='" + is_complete + '\'' +
                ", name='" + name + '\'' +
                ", question_cnt='" + question_cnt + '\'' +
                ", start_time='" + start_time + '\'' +
                ", unit_id='" + unit_id + '\'' +
                '}';
    }
}
