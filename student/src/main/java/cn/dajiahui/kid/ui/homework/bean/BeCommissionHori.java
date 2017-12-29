package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by Administrator on 2016/3/24.
 */
public class BeCommissionHori extends BeanObj {
    public int count;
    private String foreignIds;
    public String type;
    private String typeName;
    public  BeCommissionHori(){}
    public BeCommissionHori(int count,String type,String typeName,String foreignIds){
        this.count = count;
        this.foreignIds = foreignIds;
        this.type = type;
        this.typeName = typeName;
    }

    public int getCount() {
        return count;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return "BeCommissionHori{" +
                "count=" + count +
                ", foreignIds='" + foreignIds + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public String getForeignIds() {
        return foreignIds;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setForeignIds(String foreignIds) {
        this.foreignIds = foreignIds;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
