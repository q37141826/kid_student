package cn.dajiahui.kid.http;


import cn.dajiahui.kid.http.bean.BeTeFile;

/**
 * Created by z on 2016/4/1.
 */
public interface OnAttachmentUpdate  {
    void saveFile(BeTeFile file);
    void error(String error);
}
