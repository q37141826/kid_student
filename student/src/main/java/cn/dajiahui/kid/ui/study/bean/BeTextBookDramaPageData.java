package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/2/27.
 */

public class BeTextBookDramaPageData implements Serializable {

    private  List<BeTextBookDramaPageDataItem> item;
    private String music_name;
    private String music_oss_name;
    private String page_id;
    private String page_no;
    private String page_url;
    private String title;

    public List<BeTextBookDramaPageDataItem> getItem() {
        return item;
    }

    public void setItem(List<BeTextBookDramaPageDataItem> item) {
        this.item = item;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusic_oss_name() {
        return music_oss_name;
    }

    public void setMusic_oss_name(String music_oss_name) {
        this.music_oss_name = music_oss_name;
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
        return "BeTextBookDramaPageData{" +
                "item=" + item +
                ", music_name='" + music_name + '\'' +
                ", music_oss_name='" + music_oss_name + '\'' +
                ", page_id='" + page_id + '\'' +
                ", page_no='" + page_no + '\'' +
                ", page_url='" + page_url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
