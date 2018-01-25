package cn.dajiahui.kid.ui.study.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by MJ on 2018/1/22.
 * 教材选择
 */

public class BeChoiceTeachingMaterial extends BeanObj {
    private String teachingMaterialName;
    private String teachingMaterialCount;

    public BeChoiceTeachingMaterial(String teachingMaterialName, String teachingMaterialCount) {
        this.teachingMaterialName = teachingMaterialName;
        this.teachingMaterialCount = teachingMaterialCount;
    }

    public String getTeachingMaterialName() {
        return teachingMaterialName;
    }

    public void setTeachingMaterialName(String teachingMaterialName) {
        this.teachingMaterialName = teachingMaterialName;
    }

    public String getTeachingMaterialCount() {
        return teachingMaterialCount;
    }

    public void setTeachingMaterialCount(String teachingMaterialCount) {
        this.teachingMaterialCount = teachingMaterialCount;
    }
}
