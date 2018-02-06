package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mj on 2018/2/6.
 * <p>
 * 作业列表
 */

public class BeHomeWorkList implements Serializable {

    private List<BeHomework> lists;
    private String totalRows;


    public List<BeHomework> getLists() {
        return lists;
    }

    public String getTotalRows() {
        return totalRows;
    }

    @Override
    public String toString() {
        return "BeHomeWorkList{" +
                "lists=" + lists +
                ", totalRows='" + totalRows + '\'' +
                '}';
    }
}
