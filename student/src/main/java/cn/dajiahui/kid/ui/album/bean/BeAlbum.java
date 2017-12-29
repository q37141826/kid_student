package cn.dajiahui.kid.ui.album.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/7.
 * 相册对象
 */
public class BeAlbum extends BeanObj {
    private String addUserId;//添加人
    private String classId;
    private String name;
    private String coverUrl;

    public String getAddUserId() {
        return addUserId;
    }

    public String getClassId() {
        return classId;
    }

    public String getName() {
        return name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
