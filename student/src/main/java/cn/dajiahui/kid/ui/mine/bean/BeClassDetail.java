package cn.dajiahui.kid.ui.mine.bean;

/**
 * Created by mj on 2018/2/20.
 * <p>
 * 班级详情
 */

public class BeClassDetail {

    private String class_name;//班级名称
    private String code;//班级码
    private String grade;//
    private String id;//班级ID
    private String max_students_num;//班容
    private String org_id;//
    private String school_id;//小区ID
    private String students_num;//学生数量
    private String teacher_id;//教师ID
    private String teacher_name;//教师名称


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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMax_students_num() {
        return max_students_num;
    }

    public void setMax_students_num(String max_students_num) {
        this.max_students_num = max_students_num;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getStudents_num() {
        return students_num;
    }

    public void setStudents_num(String students_num) {
        this.students_num = students_num;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    @Override
    public String toString() {
        return "BeClassDetail{" +
                "class_name='" + class_name + '\'' +
                ", code='" + code + '\'' +
                ", grade='" + grade + '\'' +
                ", id='" + id + '\'' +
                ", max_students_num='" + max_students_num + '\'' +
                ", org_id='" + org_id + '\'' +
                ", school_id='" + school_id + '\'' +
                ", students_num='" + students_num + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                ", teacher_name='" + teacher_name + '\'' +
                '}';
    }
}



