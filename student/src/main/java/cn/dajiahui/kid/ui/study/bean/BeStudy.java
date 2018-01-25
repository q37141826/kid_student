package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by lenovo on 2018/1/22.
 *
 * 自学首页左上角的
 * 图片
 * 教材名称
 * 单元名称
 */

public class BeStudy extends BeanObj implements Serializable{

    private  String  img_supplementary;
    private  String  tv_title;
    private  String  tv_unit;

    public BeStudy(String img_supplementary, String tv_title, String tv_unit) {
        this.img_supplementary = img_supplementary;
        this.tv_title = tv_title;
        this.tv_unit = tv_unit;
    }

    public String getImg_supplementary() {
        return img_supplementary;
    }

    public void setImg_supplementary(String img_supplementary) {
        this.img_supplementary = img_supplementary;
    }

    public String getTv_title() {
        return tv_title;
    }

    public void setTv_title(String tv_title) {
        this.tv_title = tv_title;
    }

    public String getTv_unit() {
        return tv_unit;
    }

    public void setTv_unit(String tv_unit) {
        this.tv_unit = tv_unit;
    }
}
