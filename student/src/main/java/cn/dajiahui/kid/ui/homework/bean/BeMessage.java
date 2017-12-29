package cn.dajiahui.kid.ui.homework.bean;


import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by wdj on 2016/4/8.
 */
public class BeMessage extends BeanObj {
    private String avator;//用户头像
    private String content;//内容
    private int foreignId;//关联id
    private String nickName;//发送人昵称
    private String realName;//发送人真实名称
    private String title;//班级标题
    private String type;//通知类型
    private String typeName;//通知类型名称
    private long addTime;//时间
    private String isRead;//状态是否已读 0未读 1已读

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsRead() {
        return isRead;
    }

    public String getTypeName() {
        return typeName;
    }

    public long getAddtime() {
        return addTime;
    }

    public String getAvator() {
        return avator;
    }

    public String getContent() {
        return content;
    }

    public int getForeignId() {
        return foreignId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getRealName() {
        return realName;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
