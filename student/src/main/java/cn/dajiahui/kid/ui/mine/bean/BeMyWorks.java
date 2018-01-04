package cn.dajiahui.kid.ui.mine.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by majin on 2018/1/4.
 * 我的工作
 */

public class BeMyWorks extends BeanObj {


    private String picpath;//缩略图地址
    private String worksname;//作品名字
    private String workstime;//作品时间

    private boolean ischeck;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }


    public BeMyWorks(String worksname, String workstime) {
        this.worksname = worksname;
        this.workstime = workstime;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getWorksname() {
        return worksname;
    }

    public void setWorksname(String worksname) {
        this.worksname = worksname;
    }

    public String getWorkstime() {
        return workstime;
    }

    public void setWorkstime(String workstime) {
        this.workstime = workstime;
    }


}
