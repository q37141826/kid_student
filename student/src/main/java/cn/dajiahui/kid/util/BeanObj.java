package cn.dajiahui.kid.util;

import java.io.Serializable;

/**
 * bena对象基类
 */
public class BeanObj implements Serializable {

    private String objectId;




    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }



    @Override
    public String toString() {
        return "BeanObj{" +
                "objectId='" + objectId + '\'' +
                '}';
    }
}
