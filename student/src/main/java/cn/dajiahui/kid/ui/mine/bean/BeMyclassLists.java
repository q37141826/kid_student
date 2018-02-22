package cn.dajiahui.kid.ui.mine.bean;

/**
 * Created by majin on 2018/2/20.
 * <p>
 * 我的班级列表
 */

public class BeMyclassLists {

    private  String class_name;//班级名称
    private  String code;//班级码
    private  String id;//班级ID
    private  String school_id;//学校ID
    private  String school_name;//学校名称
    private  String students_num;//学生数量
    private  String teacher_name;//教师名称


    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getStudents_num() {
        return students_num;
    }

    public void setStudents_num(String students_num) {
        this.students_num = students_num;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    @Override
    public String toString() {
        return "BeMyclassLists{" +
                "class_name='" + class_name + '\'' +
                ", code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", school_id='" + school_id + '\'' +
                ", school_name='" + school_name + '\'' +
                ", students_num='" + students_num + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                '}';
    }
}
