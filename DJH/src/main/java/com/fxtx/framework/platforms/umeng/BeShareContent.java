package com.fxtx.framework.platforms.umeng;

import java.io.Serializable;

/**
 * Created by zy on 2016/5/16.
 */
public class BeShareContent implements Serializable {

    private String shareTitle; //分享内容的标题
    private String shareContent;//分享内容介绍
    private String shareUrl;//分享时携带的网址
    private String sharePictureUrl;//分享时 图片路径的网络路径
    private String sharePictureUrlMin;//迷你
    private int thumbRes;//图片资源路径

    public int getThumbRes() {
        return thumbRes;
    }

    public String getSharePictureUrlMin() {
        return sharePictureUrlMin;
    }

    public void setSharePictureUrlMin(String sharePictureUrlMin) {
        this.sharePictureUrlMin = sharePictureUrlMin;
    }

    public void setThumbRes(int thumbRes) {
        this.thumbRes = thumbRes;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setSharePictureUrl(String sharePictureUrl) {
        this.sharePictureUrl = sharePictureUrl;
    }

    public String getShareTitle() {
        return shareTitle == null ? "" : shareTitle;
    }


    public String getShareContent() {
        return shareContent == null ? "" : shareContent;
    }


    public String getShareUrl() {
        return shareUrl == null ? "" : shareUrl;
    }

    public String getSharePictureUrl() {
        return sharePictureUrl;
    }

}
