package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by Administrator on 2016/3/21.
 */
public class BeUserMessage extends BeanObj{
    private String imgUrl;//消息发布所有者的头像地址
    private String title;//消息发布的主题
    private String time;//消息发布的时间
    private String content;//消息发布的内容
    private String status;//消息发送的类别
    public BeUserMessage(){}
    public BeUserMessage(String imgUrl,String title,String time,String content,String status){
        this.imgUrl = imgUrl;
        this.title = title;
        this.time = time;
        this.content = content;
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
