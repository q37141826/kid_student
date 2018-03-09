package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;

/**
 * Created by mj on 2018/2/15.
 * <p>
 * 解析教材选择的子集合
 */

public class BeChoiceTeachingMaterialLists  implements Serializable{


    private String count;
    private String name;
    private String org_id;
    private String series;

    public String getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public String getOrg_id() {
        return org_id;
    }

    public String getSeries() {
        return series;
    }

    @Override
    public String toString() {
        return "BeChoiceTeachingMaterialLists{" +
                "count='" + count + '\'' +
                ", name='" + name + '\'' +
                ", org_id='" + org_id + '\'' +
                ", series='" + series + '\'' +
                '}';
    }
}
