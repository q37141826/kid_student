package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by mj on 2018/2/23.
 * <p>
 * 开始学，继续学习
 */

public class BeChoiceTeachingMaterialInfoBook {

    private String book_id;
    private String book_type;
    private String created_at;
    private String id;
    private String org_id;
    private String updated_at;
    private String user_id;


    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
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

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "BeChoiceTeachingMaterialInfoBook{" +
                "book_id='" + book_id + '\'' +
                ", book_type='" + book_type + '\'' +
                ", created_at='" + created_at + '\'' +
                ", id='" + id + '\'' +
                ", org_id='" + org_id + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
