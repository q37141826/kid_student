package com.fxtx.framework.ui.bean;

/**
 * Created by z on 2016/1/19.
 * 主要用于初始化 tab 属性
 */
public class BeTab {
    private int redid;
    private String data;//属性字段
    private String title;//
    private int radioBtn;
    private boolean isChecked;

    public BeTab(int redid, String data, String title, int radioBtn, boolean isChecked) {
        this.redid = redid;
        this.data = data;
        this.title = title;
        this.radioBtn = radioBtn;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getRedid() {
        return redid;
    }

    public void setRedid(int redid) {
        this.redid = redid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRadioBtn() {
        return radioBtn;
    }

    public void setRadioBtn(int radioBtn) {
        this.radioBtn = radioBtn;
    }
}

