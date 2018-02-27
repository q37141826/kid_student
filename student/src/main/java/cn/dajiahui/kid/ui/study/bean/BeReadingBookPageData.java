package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/2/26.
 */

public class BeReadingBookPageData implements Serializable{

    private List<BeReadingBookPageDataItem>  item;
    private String media_name;
    private String media_url;
    private String page_id;
    private String page_no;
    private String page_url;

    public List<BeReadingBookPageDataItem> getItem() {
        return item;
    }

    public void setItem(List<BeReadingBookPageDataItem> item) {
        this.item = item;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getPage_no() {
        return page_no;
    }

    public void setPage_no(String page_no) {
        this.page_no = page_no;
    }

    public String getPage_url() {
        return page_url;
    }

    public void setPage_url(String page_url) {
        this.page_url = page_url;
    }

    @Override
    public String toString() {
        return "BeReadingBookPageData{" +
                "item=" + item +
                ", media_name='" + media_name + '\'' +
                ", media_url='" + media_url + '\'' +
                ", page_id='" + page_id + '\'' +
                ", page_no='" + page_no + '\'' +
                ", page_url='" + page_url + '\'' +
                '}';
    }
}
