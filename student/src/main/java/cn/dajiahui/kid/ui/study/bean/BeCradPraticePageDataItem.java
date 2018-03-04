package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/3/1.
 */

public class BeCradPraticePageDataItem implements Serializable {
    String chinese;
    String english;

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

    @Override
    public String toString() {
        return "BeCradPraticePageDataItem{" +
                "chinese='" + chinese + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
