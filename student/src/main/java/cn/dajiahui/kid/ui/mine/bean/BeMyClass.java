package cn.dajiahui.kid.ui.mine.bean;

import java.util.List;

/**
 * Created by mj on 2018/2/20.
 * <p>
 * 我的班级列表
 */

public class BeMyClass {
    private List<BeMyclassLists> lists;
    private String totalRows;


    public List<BeMyclassLists> getLists() {
        return lists;
    }

    public void setLists(List<BeMyclassLists> lists) {
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
        return "BeMyClass{" +
                "lists=" + lists +
                ", totalRows='" + totalRows + '\'' +
                '}';
    }
}



