package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2018/1/21.
 */

public class BeSaveAnswerCard implements Serializable {

    private HashMap<Integer, Object> PageMap;
    private String homework_id;
    private String is_complete;
    private List<BeAnswerCArd> mAnswerCardList;

    public BeSaveAnswerCard(HashMap<Integer, Object> pageMap, String homework_id, List<BeAnswerCArd> mAnswerCardList, String is_complete) {
        PageMap = pageMap;
        this.homework_id = homework_id;
        this.mAnswerCardList = mAnswerCardList;
        this.is_complete = is_complete;
    }

    public String getIs_complete() {
        return is_complete;
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

    public List<BeAnswerCArd> getmAnswerCardList() {
        return mAnswerCardList;
    }
}
