package cn.dajiahui.kid.ui.study.bean;


import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 选择单元
 */

public class ChooseUtils extends BeanObj {

    private String book_type;
    private String id;
    private List<ChooseUtilsLists> lists;
    private String logo;
    private String name;
    private String org_id;
    private String series;
    private String authStatus;//激活码状态 authStatus   0:未激活, 1:已经激活  2:已经过期
    private String endtime;//截止时间


    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChooseUtilsLists> getLists() {
        return lists;
    }

    public void setLists(List<ChooseUtilsLists> lists) {
        this.lists = lists;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }


    @Override
    public String toString() {
        return "ChooseUtils{" +
                "book_type='" + book_type + '\'' +
                ", id='" + id + '\'' +
                ", lists=" + lists +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", org_id='" + org_id + '\'' +
                ", series='" + series + '\'' +
                ", authStatus='" + authStatus + '\'' +
                ", endtime='" + endtime + '\'' +
                '}';
    }
}
