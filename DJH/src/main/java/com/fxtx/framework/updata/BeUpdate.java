package com.fxtx.framework.updata;

import com.fxtx.framework.text.ParseUtil;

/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年8月31日 上午10:56:40
 * @description : 版本更新对象
 */
public class BeUpdate {
    private String codeNumber;// 版本号
    private String downloadUrl;// 地址
    private String content;// 更新内容
    private String forceUpdateFlag;// 是否强制更新

    public int getCodeNumber() {
        return ParseUtil.parseInt(codeNumber);
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getContent() {
        return content;
    }

    public String isForceUpdateFlag() {
        return forceUpdateFlag;
    }


}
