package cn.dajiahui.kid.ui.login.bean;

/**
 * Created by majin on 2018/1/10.
 * <p>
 * 注册 登录返回第三方信息
 */

public class BeUserThrid {

    private String third;//第三方
    private String easemob_username;//环信用户名
    private String easemob_passwd;//环信密码
    private String created_at;
    private String easemob_nikname;
    private String id;
    private String jpush_alias;

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getEasemob_username() {
        return easemob_username;
    }

    public void setEasemob_username(String easemob_username) {
        this.easemob_username = easemob_username;
    }

    public String getEasemob_passwd() {
        return easemob_passwd;
    }

    public void setEasemob_passwd(String easemob_passwd) {
        this.easemob_passwd = easemob_passwd;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEasemob_nikname() {
        return easemob_nikname;
    }

    public void setEasemob_nikname(String easemob_nikname) {
        this.easemob_nikname = easemob_nikname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJpush_alias() {
        return jpush_alias;
    }

    public void setJpush_alias(String jpush_alias) {
        this.jpush_alias = jpush_alias;
    }

    @Override
    public String toString() {
        return "BeUserThrid{" +
                "third='" + third + '\'' +
                ", easemob_username='" + easemob_username + '\'' +
                ", easemob_passwd='" + easemob_passwd + '\'' +
                ", created_at='" + created_at + '\'' +
                ", easemob_nikname='" + easemob_nikname + '\'' +
                ", id='" + id + '\'' +
                ", jpush_alias='" + jpush_alias + '\'' +
                '}';
    }
}
