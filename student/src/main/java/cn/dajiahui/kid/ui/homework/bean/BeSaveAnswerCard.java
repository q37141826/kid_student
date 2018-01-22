package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lenovo on 2018/1/21.
 */

public class BeSaveAnswerCard implements Serializable {

    private HashMap<Integer, Object> PageMap;

    public BeSaveAnswerCard(HashMap<Integer, Object> pageMap) {
        PageMap = pageMap;
    }

    public HashMap<Integer, Object> getPageMap() {
        return PageMap;
    }

    public void setPageMap(HashMap<Integer, Object> pageMap) {
        PageMap = pageMap;
    }
}
