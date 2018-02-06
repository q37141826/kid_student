package cn.dajiahui.kid.ui.chat.bean;

import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by wdj on 2016/4/5.
 */
public class BeConcactClass extends BeanObj {
    private String class_name;
    private List<BeConcact>student_list;
    private List<BeConcact>teacher_list;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public List<BeConcact> getStudent_list() {
        return student_list;
    }

    public void setStudent_list(List<BeConcact> student_list) {
        this.student_list = student_list;
    }

    public List<BeConcact> getTeacher_list() {
        return teacher_list;
    }

    public void setTeacher_list(List<BeConcact> teacher_list) {
        this.teacher_list = teacher_list;
    }
}
