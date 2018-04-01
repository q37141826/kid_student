package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 填空题
 */


public class CompletionQuestionModle extends QuestionModle implements Serializable {


    private String options;//选项内容

    private String isFocusable = "";//editext焦点标记  yes 有焦点  no无焦点
    private String isShowRightAnswer = "";//显示正确答案 yes 显示正确答案  no不显示正确答案
    private String textcolor = "";//设置字体和边框颜色




    public String getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(String textcolor) {
        this.textcolor = textcolor;
    }

    public String getIsShowRightAnswer() {
        return isShowRightAnswer;
    }

    public void setIsShowRightAnswer(String isShowRightAnswer) {
        this.isShowRightAnswer = isShowRightAnswer;
    }

    public String getIsFocusable() {
        return isFocusable;
    }

    public void setIsFocusable(String isFocusable) {
        this.isFocusable = isFocusable;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
