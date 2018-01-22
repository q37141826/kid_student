package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 传递答题卡data
 */

public class BeSerializableMap implements Serializable {

    private   Map<Integer, Object> PageMap;

    public BeSerializableMap(Map<Integer, Object> pageMap) {
        PageMap = pageMap;
    }

    public Map<Integer, Object> getPageMap() {
        return PageMap;
    }

    public void setPageMap(Map<Integer, Object> pageMap) {
        PageMap = pageMap;
    }
}
