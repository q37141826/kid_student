package com.fxtx.framework.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description : 头部信息解析
 */
public class HeadJson {
    private int status = -1;// 返回状态
    private String msg;// 错误消息
    private int isLastPage;// 列表对象时使用
    private JSONObject object;

    private final String strstatus = "status";
    private final String strMsg = "msg";
    private final String mObj = "data";
    private final String strIsLast = "isLastPage";


    public HeadJson(String object) {
        parsing(object);
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    /**
     * 0 返回正常
     *
     * @return
     */
    public int getstatus() {
        return status;
    }

    public void setstatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(int isLastPage) {
        this.isLastPage = isLastPage;
    }

    /**
     * 基本信息解析信息解析
     *
     * @param json
     */
    private void parsing(String json) {
        try {
            object = new JSONObject(json);
            if (!object.isNull(strstatus)) {
                status = object.getInt(strstatus);
            } else {
                msg = "数据格式解析错误";
            }
            if (!object.isNull(strIsLast)) {
                isLastPage = object.getInt(strIsLast);
            }
            if (!object.isNull(strMsg)) {
                msg = object.getString(strMsg);
            }
        } catch (JSONException e) {
            object = new JSONObject();
            msg = "数据格式解析错误";
            e.printStackTrace();
        }
    }

    /**
     * 获取 list 数组json
     */
    public <T> T parsingListArray(String key, GsonType type) {
        String s = "";
        if (status != -1 && !object.isNull(key)) {
            s = object.optJSONArray(key).toString();
            GsonUtil gson = new GsonUtil();
            return gson.getJsonList(s, type);
        }
        return null;
    }

    /**
     * 获取 对象
     */
    public <T> T parsingListArrayByIndex(String key, int index, Class<T> classs) {
        JSONArray jarray;
        try {
            String s = "";
            if (status != -1 && !object.isNull(key)) {
                jarray = object.optJSONArray(key);
                JSONObject jsonObject = (JSONObject) jarray.get(index);
                s = jsonObject.toString();
                GsonUtil gson = new GsonUtil();
                return gson.getJsonObject(s, classs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> T parsingListArray(GsonType type) {
        return parsingListArray("options", type);
    }

    /*解析K拉ok*/
    public <T> T parsingListArraydata(GsonType type) {
        return parsingListArray("page_data", type);
    }

    /**
     * 获取一个对象
     *
     * @return
     */
    public <T> T parsingObject(String objStr, Class<T> classs) {
        if (status != -1 && !object.isNull(objStr)) {
            GsonUtil gson = new GsonUtil();
            return gson.getJsonObject(object.optJSONObject(objStr).toString(), classs);
        }
        return null;
    }

    /**
     * 获取一个对象
     *
     * @return
     */
    public <T> T parsingObject(Class<T> classs) {
        if (status != -1 && !object.isNull(mObj)) {
            GsonUtil gson = new GsonUtil();
            return gson.getJsonObject(object.optJSONObject(mObj).toString(), classs);
        }
        return null;
    }

    public String parsingString(String key) {
        String value;
        if (!object.isNull(key)) {
            value = object.optString(key);
        } else {
            value = "";
        }
        return value;
    }

    public int parsingInt(String key) {
        int num;
        if (!object.isNull(key)) {
            num = object.optInt(key);
        } else {
            num = 0;
        }
        return num;
    }
}
