package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mj on 2018/2/7.
 * <p>
 * 答题卡提交时转的json
 */

public class ToAnswerCardJson implements Serializable {

    private List<BeSubmitAnswerCard> list;

    public ToAnswerCardJson(List<BeSubmitAnswerCard> beSubmitAnswerCardList) {

        list = beSubmitAnswerCardList;
    }
}
