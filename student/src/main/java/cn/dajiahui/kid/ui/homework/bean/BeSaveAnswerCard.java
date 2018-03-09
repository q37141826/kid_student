package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mj on 2018/1/21.
 * <p>
 * 做作业--》答题卡
 */

public class BeSaveAnswerCard implements Serializable {

    private String homework_id;
    private String is_complete;
    private List<Object> mDatalist;//所有数据的模型集合


    public BeSaveAnswerCard(List<Object> mDatalist, String homework_id,   String is_complete) {
        this.mDatalist = mDatalist;
        this.homework_id = homework_id;
        this.is_complete = is_complete;
    }
    public String getIs_complete() {
        return is_complete;
    }

    public String getHomework_id() {
        return homework_id;
    }

    public List<Object> getmDatalist() {
        return mDatalist;
    }


}
