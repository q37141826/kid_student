package cn.dajiahui.kid.ui.study.bean;


import cn.dajiahui.kid.util.BeanObj;

/**
 * 选择单元
 */

public class ChooseUtils extends BeanObj {

    private String unitpic;//单元图片
    private String utlisname;//单元名称
    private String schedule;//单元进度

    public ChooseUtils(String unitpic, String utlisname, String schedule) {
        this.unitpic = unitpic;
        this.utlisname = utlisname;
        this.schedule = schedule;
    }

    public String getUnitpic() {
        return unitpic;
    }

    public void setUnitpic(String unitpic) {
        this.unitpic = unitpic;
    }

    public String getUtlisname() {
        return utlisname;
    }

    public void setUtlisname(String utlisname) {
        this.utlisname = utlisname;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
