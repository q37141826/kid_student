package cn.dajiahui.kid.util;

/**
 * Created by z on 2016/5/11.
 */
public class BeJpush {
    private String sendId;
    private String objectId;
    private String userId;
    private String foreignId;
    private String type;

    @Override
    public String toString() {
        return "BeJpush{" +
                "sendId='" + sendId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", userId='" + userId + '\'' +
                ", foreignId='" + foreignId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getSendId() {
        return sendId;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getUserId() {
        return userId;
    }


    public String getForeignId() {
        return foreignId;
    }


    public String getType() {
        return type;
    }

}
