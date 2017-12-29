package cn.dajiahui.kid.ui.album.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2016/3/14.
 * 相片详情
 */
public class BePhotoDetails extends BePhoto {
    private String addTime;
    private String userName;
    private int commentCount;//回复数
    private List<BePhotoEval> list;
    private String avator;

    public BePhotoDetails() {

    }

    public String getAddTime() {
        return addTime;
    }

    public String getUserName() {
        return userName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public List<BePhotoEval> getList() {
        if (list == null) {
            list = new ArrayList<BePhotoEval>();
        }
        return list;
    }

    public String getAvator() {
        return avator;
    }
}
