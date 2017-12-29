package cn.dajiahui.kid.ui.album.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/10.
 */
public class BePhoto extends BeanObj {
    private String addUserId;
    private String classId;
    private String originalUrl;//原图
    private int isLike;// 0未赞1已赞
    private int likeCount;//被赞次数
    private String thumbUrl;//缩略图 200*200
    private String picUrl;//800*800
    private boolean isUpdate;//上传成功
    public BePhoto() {

    }

    public BePhoto(String originalUrl, String thumbUrl, String picUrl) {
        this.originalUrl = originalUrl;
        this.thumbUrl = thumbUrl;
        this.picUrl = picUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public String getClassId() {
        return classId;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public int getIsLike() {
        return isLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public void setCount(int isLike) {
        if (isLike == 0) {
            likeCount--;
        } else {
            likeCount++;
        }
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
}
