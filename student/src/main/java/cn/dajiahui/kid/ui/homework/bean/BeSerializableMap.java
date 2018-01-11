package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 传递答题卡data
 */

public class BeSerializableMap implements Serializable {

    private List<QuestionModle> data;

    public List<QuestionModle> getData() {
        return data;
    }

    public void setData(List<QuestionModle> data) {
        this.data = data;
    }


    public BeSerializableMap(List<QuestionModle> data) {
        this.data = data;
    }
}
