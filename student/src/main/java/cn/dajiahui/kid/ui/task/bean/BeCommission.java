package cn.dajiahui.kid.ui.task.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by Administrator on 2016/3/23.
 */
public class BeCommission extends BeanObj {

    private String content;
    private String foreignId;
    private String title;
    private String type;
    private String typeName;

    public String getContent() {
        return content;
    }

    public String getForeignId() {
        return foreignId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return "BeCommission{" +
                "content='" + content + '\'' +
                ", foreignId='" + foreignId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
