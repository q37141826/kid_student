package cn.dajiahui.kid.ui.mine.bean;

import java.io.Serializable;
import java.util.List;

import cn.dajiahui.kid.util.BeanObj;


/**
 * Created by mj on 2017/12/28.
 */

public class BeClassSpace extends BeanObj implements Serializable {

    private List<BeClassSpaceList> list;
    private String totalRows;




    public List<BeClassSpaceList> getList() {
        return list;
    }

    public void setList(List<BeClassSpaceList> list) {
        this.list = list;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }




}
