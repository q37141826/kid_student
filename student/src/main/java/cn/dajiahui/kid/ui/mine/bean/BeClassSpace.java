package cn.dajiahui.kid.ui.mine.bean;

import android.graphics.Bitmap;

import cn.dajiahui.kid.util.BeanObj;


/**
 * 班级空间
 */

public class BeClassSpace extends BeanObj {

    private String myclass;
    private String mytime;
    private String comment;

    private String img1;
    private String img2;
    private String img3;

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public BeClassSpace(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public BeClassSpace(String myclass, String mytime, String comment) {
        this.myclass = myclass;
        this.mytime = mytime;
        this.comment = comment;
    }


    public BeClassSpace(String myclass, String mytime, String comment, String img1, String img2, String img3) {
        this.myclass = myclass;
        this.mytime = mytime;
        this.comment = comment;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
    }


    public String getMyclass() {
        return myclass;
    }

    public void setMyclass(String myclass) {
        this.myclass = myclass;
    }

    public String getMytime() {
        return mytime;
    }

    public void setMytime(String mytime) {
        this.mytime = mytime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }


}
