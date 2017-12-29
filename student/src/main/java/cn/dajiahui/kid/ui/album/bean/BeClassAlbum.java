package cn.dajiahui.kid.ui.album.bean;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by z on 2016/3/11.
 */
public class BeClassAlbum extends BeanObj {
    private String className;
    private List<BeAlbum> list;

    public String getClassName() {
        return className;
    }

    public List<BeAlbum> getList() {
        if (list == null) {
            list = new ArrayList<BeAlbum>();
        }
        return list;
    }
}
