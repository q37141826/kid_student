package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by z on 2016/2/22.
 * 全部功能实体对象
 */
public class BeFunction {
    private int ids;
    private String name;
    private int imgId;//图片id
    private String type;//未读数量

    public BeFunction( int ids, int imgId,String name,String type) {
        this.type = type;
        this.ids = ids;
        this.name = name;
        this.imgId = imgId;
    }

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
    public String getType() {
        return type;
    }
}
