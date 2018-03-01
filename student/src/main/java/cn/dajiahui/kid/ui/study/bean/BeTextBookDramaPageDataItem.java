package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/2/27.
 */

public class BeTextBookDramaPageDataItem implements Serializable {
    private String chinese;
    private String english;
    private String tag_index;
    private String time_end;
    private String time_start;


    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getTag_index() {
        return tag_index;
    }

    public void setTag_index(String tag_index) {
        this.tag_index = tag_index;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    @Override
    public String toString() {
        return "BeTextBookDramaPageDataItem{" +
                "chinese='" + chinese + '\'' +
                ", english='" + english + '\'' +
                ", tag_index='" + tag_index + '\'' +
                ", time_end='" + time_end + '\'' +
                ", time_start='" + time_start + '\'' +
                '}';
    }
}
