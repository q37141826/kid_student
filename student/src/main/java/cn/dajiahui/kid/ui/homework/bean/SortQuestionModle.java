package cn.dajiahui.kid.ui.homework.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 排序题模型
 */

public class SortQuestionModle extends QuestionModle {


    private List<BeSortOptions> options;//选项内容
    /*保存答案集合 key：左边的顺序 vul：对应右边的坐标点*/
    private Map<Integer, BeLocation> sortAnswerMap = new HashMap<>();



    public Map<Integer, BeLocation> getSortAnswerMap() {
        return sortAnswerMap;
    }

    public void setSortAnswerMap(Map<Integer, BeLocation> sortAnswerMap) {
        this.sortAnswerMap = sortAnswerMap;
    }

    public List<BeSortOptions> getOptions() {
        return options;
    }

    public void setOptions(List<BeSortOptions> options) {
        this.options = options;
    }
}
