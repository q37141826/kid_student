package cn.dajiahui.kid.ui.mine.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by Mjj on 2016/5/11.
 */
public class BeHelpAll extends BeanObj {
    private String cmsName; // 名称
    private String isTarget; // 是否跳转 0不是 1是
    private String cmsUrl; // 跳转链接

    public String getCmsName() {
        return cmsName;
    }

    public String getIsTarget() {
        return isTarget;
    }

    public String getCmsUrl() {
        return cmsUrl;
    }
}
