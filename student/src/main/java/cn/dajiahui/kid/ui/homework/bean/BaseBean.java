package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 模型基类
 */

public class BaseBean extends BeanObj {

    @Override
    public int getCurrentpage() {
        return currentpage;
    }

    @Override
    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    private  int currentpage;

}
