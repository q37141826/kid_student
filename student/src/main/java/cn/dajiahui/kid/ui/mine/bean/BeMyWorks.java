package cn.dajiahui.kid.ui.mine.bean;

import java.io.Serializable;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by majin on 2018/1/4.
 * 我的作品
 */

public class BeMyWorks extends BeanObj implements Serializable {


    private String picpath;//缩略图地址
    private String worksLocalPath;//我的作品本地地址
    private String worksName;//我的作品名称
    private String workstime;//作品时间

    private boolean ischeck;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }


    public BeMyWorks(String worksname, String worksLocalPath, String workstime) {
        this.worksName=worksname;
        this.worksLocalPath = worksLocalPath;
        this.workstime = workstime;
    }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getWorksLocalPath() {
        return worksLocalPath;
    }

    public void setWorksLocalPath(String worksLocalPath) {
        this.worksLocalPath = worksLocalPath;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public String getWorkstime() {
        return workstime;
    }



    public String getWorksName() {
        return worksName;
    }

    @Override
    public String toString() {
        return "BeMyWorks{" +
                "picpath='" + picpath + '\'' +
                ", worksLocalPath='" + worksLocalPath + '\'' +
                ", worksName='" + worksName + '\'' +
                ", workstime='" + workstime + '\'' +
                ", ischeck=" + ischeck +
                '}';
    }
}
