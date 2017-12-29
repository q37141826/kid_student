package cn.dajiahui.kid.ui.notice.bean;

import com.fxtx.framework.text.StringUtil;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/7.
 * 通知对象
 */
public class BeNotice extends BeanObj {
    private String addTime;
    private String classId;
    private String className;
    private String content;
    private String title;
    private String userId;
    private String userName;
    private int type;//类型 1系统通知，2学校通知 3班级通知

    public String getAddTime() {
        return addTime;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public String getTitle() {
        return title;
    }


    public String getUserId() {
        return userId;
    }


    public String getUserName() {
        return StringUtil.isEmpty(userName) ? "" : userName;
    }


    public int getType() {
        return type;
    }


    public String getContent() {
        return content;
    }
}
