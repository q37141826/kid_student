package cn.dajiahui.kid.ui.chat.bean;

import com.fxtx.framework.text.StringUtil;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by wdj on 2016/4/5.()
 * 学生端联系人列表
 */
public class BeConcact extends BeanObj {
    private String avator;
    private String hxId;
    private String hxPwd;
    private String realName;
    private String sex;
    private String userName;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public String getAvator() {
        return avator;
    }

    public String getHxId() {
        return hxId;
    }

    public String getHxPwd() {
        return hxPwd;
    }

    public String getRealName() {
        return StringUtil.isEmpty(realName)?userName:realName;
    }

    public String getSex() {
        return sex;
    }

    public String getUserName() {
        return userName;
    }

}
