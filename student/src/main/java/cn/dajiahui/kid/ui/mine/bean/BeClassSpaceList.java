package cn.dajiahui.kid.ui.mine.bean;

import java.util.List;

/**
 * Created by mj on 2018/2/22.
 */

public class BeClassSpaceList {

    private String class_name;
    private String content;
    private String created_at;
    private String id;
    private String pic_cnt;
    private List<String> img_url;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic_cnt() {
        return pic_cnt;
    }

    public void setPic_cnt(String pic_cnt) {
        this.pic_cnt = pic_cnt;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    //    public String getClass_name() {
//        return class_name;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public String getCreated_at() {
//        return created_at;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getPic_cnt() {
//        return pic_cnt;
//    }
//
//    public List<BeClassSpaceListimgurl> getImg_url() {
//        return img_url;
//    }
//
//    @Override
//    public String toString() {
//        return "BeClassSpaceList{" +
//                "class_name='" + class_name + '\'' +
//                ", content='" + content + '\'' +
//                ", created_at='" + created_at + '\'' +
//                ", id='" + id + '\'' +
//                ", pic_cnt='" + pic_cnt + '\'' +
//                ", img_url=" + img_url +
//                '}';
//    }
}
