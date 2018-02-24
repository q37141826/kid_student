package cn.dajiahui.kid.ui.study.bean;

import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by MJ on 2018/1/22.
 * 教材选择详情
 */

public class BeChoiceTeachingMaterialInfo extends BeanObj {
    private List<BeChoiceTeachingMaterialInfoLists> lists;
    private String totalRows;

    public List<BeChoiceTeachingMaterialInfoLists> getLists() {
        return lists;
    }

    public void setLists(List<BeChoiceTeachingMaterialInfoLists> lists) {
        this.lists = lists;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }


    @Override
    public String toString() {
        return "BeChoiceTeachingMaterialInfo{" +
                "lists=" + lists +
                ", totalRows='" + totalRows + '\'' +
                '}';
    }
}

