package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lenovo on 2018/1/21.
 */

public class BeSaveAnswerCard implements Serializable {

    private HashMap<Integer, Object> PageMap;
    private String homework_id;
    private int allNum = 0;//一共多少道题

    public BeSaveAnswerCard(HashMap<Integer, Object> pageMap, String homework_id, int allNum) {
        PageMap = pageMap;
        this.homework_id = homework_id;
        this.allNum = allNum;
    }

    public String getHomework_id() {
        return homework_id;
    }

    public HashMap<Integer, Object> getPageMap() {
        return PageMap;
    }

    public void setPageMap(HashMap<Integer, Object> pageMap) {
        PageMap = pageMap;
    }

    public int getAllNum() {
        return allNum;
    }
}
