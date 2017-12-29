package cn.dajiahui.kid.ui.album.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/14.
 */
public class BePhotoEvalItem extends BeanObj {
    private String addTime;
    private String avator;
    private String content;
    private String parentId;
    private String pictureId;//一级回复id
    private String replyUserId;//被回复人id
    private String replyUserName;
    private String userId;//回复人
    private String userName;

    public void setContent(String content) {
        this.content = content;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getAddTime() {
        return addTime;
    }

    public String getAvator() {
        return avator;
    }

    public String getContent() {
        return content;
    }

    public String getParentId() {
        return parentId;
    }

    public String getPictureId() {
        return pictureId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
