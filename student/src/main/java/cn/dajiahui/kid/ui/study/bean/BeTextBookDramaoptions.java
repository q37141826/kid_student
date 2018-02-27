package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/1/31.
 * <p>
 * 课本剧里的list
 */

public class BeTextBookDramaoptions implements Serializable {

    private int start_time;//起始时间
    private int end_time;    //终止时间
    private String english;//	英文
    private String chinese;    //中文翻译

    public BeTextBookDramaoptions(int start_time, int end_time, String english, String chinese) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.english = english;
        this.chinese = chinese;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
