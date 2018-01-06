package cn.dajiahui.kid.ui.mine.bean;


import cn.dajiahui.kid.util.BeanObj;

/**
 * 通知
 */

public class BeNotice extends BeanObj {

    private String UpdateContent;//更新内容
    private String deadline;//更新时间

    public BeNotice(String updateContent, String deadline) {
        UpdateContent = updateContent;
        this.deadline = deadline;
    }

    public String getUpdateContent() {
        return UpdateContent;
    }

    public void setUpdateContent(String updateContent) {
        UpdateContent = updateContent;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


}
