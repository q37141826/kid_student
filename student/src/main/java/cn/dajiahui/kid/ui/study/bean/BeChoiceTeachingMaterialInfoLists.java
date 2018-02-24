package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by mj on 2018/2/23.
 * <p>
 * 选择教材系列下的列表集合
 */

public class BeChoiceTeachingMaterialInfoLists {

    private String book_type;
    private String id;
    private String logo;
    private String name;
    private String org_id;
    private String series;
    private String updated_at;


    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "BeChoiceTeachingMaterialInfoLists{" +
                "book_type='" + book_type + '\'' +
                ", id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", org_id='" + org_id + '\'' +
                ", series='" + series + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
