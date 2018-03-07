package cn.dajiahui.kid.ui.mine.bean;


import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 通知
 */

public class BeNotice extends BeanObj {

    private String totalRows;
    private List<BeNoticeLists> lists;

    public String getTotalRows() {
        return totalRows;
    }

    public List<BeNoticeLists> getLists() {
        return lists;
    }
}
