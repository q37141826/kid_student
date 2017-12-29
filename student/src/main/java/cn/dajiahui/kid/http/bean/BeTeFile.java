package cn.dajiahui.kid.http.bean;


import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/31.
 * 附件
 */
public class BeTeFile extends BeanObj {
    private String addTime;
    private String addUserId;
    private String fileName;
    private String fileUrl;

    public String getAddTime() {
        return addTime;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
