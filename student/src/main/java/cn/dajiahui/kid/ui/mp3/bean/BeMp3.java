package cn.dajiahui.kid.ui.mp3.bean;


import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/5/13.
 * 播放对象
 */
public class BeMp3 extends BeanObj {
    private String filePath;//文件地址
    private String fileUrl;//文件网址
    private int duration;//文件播放长度

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
