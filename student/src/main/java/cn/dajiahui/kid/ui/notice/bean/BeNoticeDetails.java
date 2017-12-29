package cn.dajiahui.kid.ui.notice.bean;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.ui.album.bean.BePhotoEval;

/**
 * Created by z on 2016/3/17.
 * 通知消息 详情
 */
public class BeNoticeDetails extends BeNotice {
    private int allCount;//全部通知人数
    private int commentCount;//回复数
    private int isReadCount;//已阅数
    private String isRead;//是否已读；
    private List<BePhotoEval> list;//回复人

    public String getIsRead() {
        return isRead;
    }

    public int getAllCount() {
        return allCount;
    }

    public int getIsReadCount() {
        return isReadCount;
    }


    public List<BePhotoEval> getList() {
        return list == null ? new ArrayList<BePhotoEval>() : list;
    }

    public int getCommentCount() {
        return commentCount;
    }
}
