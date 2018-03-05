package cn.dajiahui.kid.ui.mine.bean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/5.
 */

public class BeMineWorks {
    private String avatar;
    private List<BeMineWorksLists> lists;
    private String totalRows;

    public String getAvatar() {
        return avatar;
    }

    public List<BeMineWorksLists> getLists() {
        return lists;
    }

    public String getTotalRows() {
        return totalRows;
    }

    @Override
    public String toString() {
        return "BeMineWorks{" +
                "avatar='" + avatar + '\'' +
                ", lists=" + lists +
                ", totalRows='" + totalRows + '\'' +
                '}';
    }
}
