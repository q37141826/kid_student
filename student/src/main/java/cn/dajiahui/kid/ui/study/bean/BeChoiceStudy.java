package cn.dajiahui.kid.ui.study.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * Created by lenovo on 2018/1/22.
 * <p>
 * 6 种学习模式
 */

public class BeChoiceStudy extends BeanObj {
    private String type;
    private String studypic;//图片
    private String studyseekschedule;//进度
    private String studyname;//名称
    private String studyratingBarschedule;//小星星

    public BeChoiceStudy(String type, String studypic, String studyseekschedule, String studyname, String studyratingBarschedule) {
        this.type = type;
        this.studypic = studypic;
        this.studyseekschedule = studyseekschedule;
        this.studyname = studyname;
        this.studyratingBarschedule = studyratingBarschedule;
    }

    public String getStudypic() {
        return studypic;
    }

    public void setStudypic(String studypic) {
        this.studypic = studypic;
    }

    public String getStudyseekschedule() {
        return studyseekschedule;
    }

    public void setStudyseekschedule(String studyseekschedule) {
        this.studyseekschedule = studyseekschedule;
    }

    public String getStudyname() {
        return studyname;
    }

    public void setStudyname(String studyname) {
        this.studyname = studyname;
    }

    public String getStudyratingBarschedule() {
        return studyratingBarschedule;
    }

    public void setStudyratingBarschedule(String studyratingBarschedule) {
        this.studyratingBarschedule = studyratingBarschedule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
