package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 传递答题卡data
 */

public class BeSerializableMap implements Serializable {

    private List<BaseBean> data;

    public List<BaseBean> getData() {
        return data;
    }

    public void setData(List<BaseBean> data) {
        this.data = data;
    }


    public BeSerializableMap(List<BaseBean> data) {
        this.data = data;
    }
}
