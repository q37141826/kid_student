package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/2/26.
 */

public class BeReadingBookPageDataItem implements Serializable {
    private String chinese;
    private String end_time;
    private String english;
    private String height;
    private String point_x;
    private String point_y;
    private String start_time;
    private String tag_index;
    private String width;

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPoint_x() {
        return point_x;
    }

    public void setPoint_x(String point_x) {
        this.point_x = point_x;
    }

    public String getPoint_y() {
        return point_y;
    }

    public void setPoint_y(String point_y) {
        this.point_y = point_y;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTag_index() {
        return tag_index;
    }

    public void setTag_index(String tag_index) {
        this.tag_index = tag_index;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "BeReadingBookPageDataItem{" +
                "chinese='" + chinese + '\'' +
                ", end_time='" + end_time + '\'' +
                ", english='" + english + '\'' +
                ", height='" + height + '\'' +
                ", point_x='" + point_x + '\'' +
                ", point_y='" + point_y + '\'' +
                ", start_time='" + start_time + '\'' +
                ", tag_index='" + tag_index + '\'' +
                ", width='" + width + '\'' +
                '}';
    }
}
