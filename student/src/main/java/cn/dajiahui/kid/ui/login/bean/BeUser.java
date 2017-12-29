package cn.dajiahui.kid.ui.login.bean;

import com.fxtx.framework.text.StringUtil;

import java.util.ArrayList;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/17.
 * 用户对象
 */
public class BeUser extends BeanObj {
    private String access_token;
    private String avator;
    private String email;
    private String orgId;

    @Override
    public String toString() {
        return "BeUser{" +
                "access_token='" + access_token + '\'' +
                ", avator='" + avator + '\'' +
                ", email='" + email + '\'' +
                ", orgId='" + orgId + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", receiveMsgTel='" + receiveMsgTel + '\'' +
                ", sex='" + sex + '\'' +
                ", userName='" + userName + '\'' +
                ", userType='" + userType + '\'' +
                ", hxId='" + hxId + '\'' +
                ", hxPwd='" + hxPwd + '\'' +
                ", birthday='" + birthday + '\'' +
                ", signature='" + signature + '\'' +
                ", pwd='" + pwd + '\'' +
                ", classCount=" + classCount +
                ", authList=" + authList +
                ", loadUrl='" + loadUrl + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", cname='" + cname + '\'' +
                '}';
    }

    private String phone;
    private String realName;
    private String receiveMsgTel;
    private String sex;
    private String userName;
    private String userType;//用户类型 1教师 2学生
    private String hxId;
    private String hxPwd;
    private String birthday;
    private String signature;
    private String pwd;
    private int classCount; //班级数
    private ArrayList<String> authList = new ArrayList<String>();
    private String loadUrl;
    private String logoUrl;
    private String cname;
    
    public ArrayList<String> getAuthList() {
        return authList;
    }
    
    public int getClassCount() {
        return classCount;
    }
    
    public String getPwd() {
        return pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public String getBirthday() {
        return birthday;
    }
    
    public String getSignature() {
        return "";
    }
    
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public String getHxId() {
        return hxId;
    }
    
    public String getHxPwd() {
        return hxPwd;
    }
    
    public String getAvator() {
        return avator;
    }
    
    public void setAvator(String avator) {
        this.avator = avator;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOrgId() {
        return orgId;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRealName() {
        if (StringUtil.isEmpty(realName))
            return getUserName();
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getReceiveMsgTel() {
        return receiveMsgTel;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getUserName() {
        if (StringUtil.isEmpty(userName))
            userName = "";
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public String getAccess_token() {
        return access_token;
    }
    
    public void setId(String objectId) {
        setObjectId(objectId);
    }
    
    public String getLoadUrl() {
        return loadUrl;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public String getOrglName() {
        return cname;
    }
}
