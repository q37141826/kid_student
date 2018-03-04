package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/3/1.
 */

public class BeCradPraticePageData implements Serializable  {
    List<BeCradPraticePageDataItem> item;
    String music_name;
    String music_oss_url;
    String page_id;
    String page_no;
    String page_url;
    String title;

    public List<BeCradPraticePageDataItem> getItem() {
        return item;
    }

    public void setItem(List<BeCradPraticePageDataItem> item) {
        this.item = item;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusic_oss_url() {
        return music_oss_url;
    }

    public void setMusic_oss_url(String music_oss_url) {
        this.music_oss_url = music_oss_url;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BeCradPraticePageData{" +
                "item=" + item +
                ", music_name='" + music_name + '\'' +
                ", music_oss_url='" + music_oss_url + '\'' +
                ", page_id='" + page_id + '\'' +
                ", page_no='" + page_no + '\'' +
                ", page_url='" + page_url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
