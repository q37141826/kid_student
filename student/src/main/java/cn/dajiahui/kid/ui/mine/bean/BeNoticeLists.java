package cn.dajiahui.kid.ui.mine.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/3/6.
 * 通知item的列表
 */

public class BeNoticeLists implements Serializable {

    private String content;
    private String created_at;
    private String id;
    private String is_read;
    private String title;
    private String type;


    public BeNoticeLists(String content, String created_at, String title, String id) {
        this.content = content;
        this.created_at = created_at;
        this.title = title;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getId() {
        return id;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
