package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by mj on 2018/2/23.
 */

public class ChooseUtilsLists {
    private String id;
    private String name;
    private String seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "ChooseUtilsLists{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }
}
