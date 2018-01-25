package cn.dajiahui.kid.ui.study.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by MJ on 2018/1/22.
 * 教材选择详情
 */

public class BeChoiceTeachingMaterialInfo extends BeanObj {
    private String teachingMaterialInfopic;
    private String teachingMaterialInfoName;
    private String teachingMaterialInfoState;


    public BeChoiceTeachingMaterialInfo(String teachingMaterialInfopic, String teachingMaterialInfoName) {
        this.teachingMaterialInfopic = teachingMaterialInfopic;
        this.teachingMaterialInfoName = teachingMaterialInfoName;

    }

    public String getTeachingMaterialInfopic() {
        return teachingMaterialInfopic;
    }

    public void setTeachingMaterialInfopic(String teachingMaterialInfopic) {
        this.teachingMaterialInfopic = teachingMaterialInfopic;
    }

    public String getTeachingMaterialInfoName() {
        return teachingMaterialInfoName;
    }

    public void setTeachingMaterialInfoName(String teachingMaterialInfoName) {
        this.teachingMaterialInfoName = teachingMaterialInfoName;
    }

    public String getTeachingMaterialInfoState() {
        return teachingMaterialInfoState;
    }

    public void setTeachingMaterialInfoState(String teachingMaterialInfoState) {
        this.teachingMaterialInfoState = teachingMaterialInfoState;
    }
}

