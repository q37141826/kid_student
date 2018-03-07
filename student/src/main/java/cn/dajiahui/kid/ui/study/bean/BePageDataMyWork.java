package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/3/6.
 */

public class BePageDataMyWork implements Serializable {
    private  String author;
    private  String book_id;
    private  String cate_id;
    private  String date;
    private  String description;
    private  String id;
    private  String page_id;
    private  String page_no;
    private  String score;
    private  String share_url;
    private  String thumbnail;
    private  String title;
    private  String unit_id;
    private  String user_id;
    private  String video;


    public String getAuthor() {
        return author;
    }

    public String getBook_id() {
        return book_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getPage_id() {
        return page_id;
    }

    public String getPage_no() {
        return page_no;
    }

    public String getScore() {
        return score;
    }

    public String getShare_url() {
        return share_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getVideo() {
        return video;
    }

    @Override
    public String toString() {
        return "BePageDataMyWork{" +
                "author='" + author + '\'' +
                ", book_id='" + book_id + '\'' +
                ", cate_id='" + cate_id + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", page_id='" + page_id + '\'' +
                ", page_no='" + page_no + '\'' +
                ", score='" + score + '\'' +
                ", share_url='" + share_url + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", unit_id='" + unit_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", video='" + video + '\'' +
                '}';
    }
}
