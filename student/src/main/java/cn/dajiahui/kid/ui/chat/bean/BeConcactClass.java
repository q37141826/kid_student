package cn.dajiahui.kid.ui.chat.bean;

import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by wdj on 2016/4/5.
 */
public class BeConcactClass extends BeanObj {
    private String className;
    private String teacherIds;
    private List<BeConcact> list;

    public String getClassName() {
        return className;
    }

    public String getTeacherIds() {
        return teacherIds;
    }

    public List<BeConcact> getList() {
        return list;
    }


}
