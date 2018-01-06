package cn.dajiahui.kid.util;

import java.io.Serializable;

/**
 * bena对象基类
 */
public class BeanObj implements Serializable {
    private int currentpage;//当前页面的模型


    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    @Override
    public String toString() {
        return "BeanObj{" +
                "objectId='" + objectId + '\'' +
                '}';
    }
}
