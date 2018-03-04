package cn.dajiahui.kid.ui.study.kinds.practice.myinterface;


import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExMoveImagview;

/**
 * Created by mj on 2018/1/17.
 */

public interface ExMoveLocation {
    /*可拖动视图的中心点x y*/
    public BeLocation submitCenterPoint(ExMoveImagview moveImagview, int position, float X, float Y);
}
