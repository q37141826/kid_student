package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 作业
 */

public class BeHomework extends BeanObj {

    public BeHomework(String hometime, String endtime, String class_name, String completion, String homename, String homecontent, String homeflag) {
        this.hometime = hometime;
        this.endtime = endtime;
        this.class_name = class_name;
        this.completion = completion;
        this.homename = homename;
        this.homecontent = homecontent;
        this.homeflag = homeflag;
    }

    private String hometime;//作业时间
    private String endtime;//截止时间
    private String class_name;//班级名称
    private String completion;//小星星（完成度）

    private String homename;//作业名字
    private String homecontent;//内容
    private String homeflag;//作业标志：在（未开始，未完成，已完成，已过期）


    public String getHometime() {
        return hometime;
    }

    public void setHometime(String hometime) {
        this.hometime = hometime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getHomename() {
        return homename;
    }

    public void setHomename(String homename) {
        this.homename = homename;
    }

    public String getHomecontent() {
        return homecontent;
    }

    public void setHomecontent(String homecontent) {
        this.homecontent = homecontent;
    }

    public String getHomeflag() {
        return homeflag;
    }

    public void setHomeflag(String homeflag) {
        this.homeflag = homeflag;
    }


}
