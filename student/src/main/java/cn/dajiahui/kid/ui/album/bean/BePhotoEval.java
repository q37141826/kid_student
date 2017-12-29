package cn.dajiahui.kid.ui.album.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2016/3/14.
 */
public class BePhotoEval extends BePhotoEvalItem {
    private List<BePhotoEvalItem> list;

    public List<BePhotoEvalItem> getList() {
        if (list == null) {
            list = new ArrayList<BePhotoEvalItem>();
        }
        return list;
    }
}
