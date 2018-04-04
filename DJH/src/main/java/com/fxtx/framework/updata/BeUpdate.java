package com.fxtx.framework.updata;

import com.fxtx.framework.text.ParseUtil;

/**
 * @author djh-mj
 * @version :1
 * @CreateDate 2018年4月4日
 * @description : 版本更新对象
 */
public class BeUpdate {

    private String apk_url;//下载网址
    private String app_id;//设备ID
    private String is_update;//0 不升级 1.提示升级 2.强制升级
    private String title;//标题
    private String update_msg;//更新信息
    private String version_code;//版本（自己上传的是versionname）


    public String getApk_url() {
        return apk_url;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getIs_update() {
        return is_update;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdate_msg() {
        return update_msg;
    }

    public String getVersion_code() {
        return version_code;
    }
}
