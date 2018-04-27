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

    private String class_status;//加入班级的状态   0: 没任何班级及其申请  1:没有班级，但有班级申请,  2:已经有班级


    public List<BeHomework> getLists() {
        return lists;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public String getClass_status() {
        return class_status;
    }

    @Override
    public String toString() {
        return "BeHomeWorkList{" +
                "lists=" + lists +
                ", totalRows='" + totalRows + '\'' +
                '}';
    }
}
