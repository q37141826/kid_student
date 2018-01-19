package cn.dajiahui.kid.ui.homework.myinterface;


import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.homework.view.MoveImagview;

/**
 * Created by lenovo on 2018/1/17.
 */

public interface MoveLocation {
    /*可拖动视图的中心点x y*/
    public BeLocation submitCenterPoint(MoveImagview moveImagview, float X, float Y);
}
