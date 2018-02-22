package cn.dajiahui.kid.ui.study.bean;

import java.util.List;

import cn.dajiahui.kid.ui.homework.bean.BeChoiceTeachingMaterialLists;
import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by MJ on 2018/1/22.
 * 教材选择
 */

public class BeChoiceTeachingMaterial extends BeanObj {
    private List<BeChoiceTeachingMaterialLists> lists;
    private String totalRows;


    public String getTotalRows() {
        return totalRows;
    }

    public List<BeChoiceTeachingMaterialLists> getLists() {
        return lists;
    }

    @Override
    public String toString() {
        return "BeChoiceTeachingMaterial{" +
                "lists=" + lists +
                ", totalRows='" + totalRows + '\'' +
                '}';
    }
}
