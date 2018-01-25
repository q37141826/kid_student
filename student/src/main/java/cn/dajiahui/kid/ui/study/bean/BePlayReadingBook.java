package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by lenovo on 2018/1/23.
 */

public class BePlayReadingBook {


    private String image_url;

    private int starttime;//点读开始时间
    private int endtime;//点读结束时间

    private int startX;//起点X
    private int startY;//起点Y
    private int width;//宽
    private int height;//高

    private String audio_url;//音频地址
    private String english;//英文
    private String chinese;//中文

    /*模拟构造*/
    public BePlayReadingBook(String audio_url,int starttime, int endtime, int startX, int startY, int width, int height) {
        this.audio_url=audio_url;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.starttime = starttime;
        this.endtime = endtime;
    }


    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    @Override
    public String toString() {
        return "BePlayReadingBook{" +
                "image_url='" + image_url + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", startX=" + startX +
                ", startY=" + startY +
                ", width=" + width +
                ", height=" + height +
                ", audio_url='" + audio_url + '\'' +
                ", english='" + english + '\'' +
                ", chinese='" + chinese + '\'' +
                '}';
    }
}
