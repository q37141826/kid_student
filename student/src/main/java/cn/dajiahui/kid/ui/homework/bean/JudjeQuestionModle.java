package cn.dajiahui.kid.ui.homework.bean;

import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 题模型
 */

public class JudjeQuestionModle extends QuestionModle {


    private List<BeJudjeOptions> options;//选项内容

    public void setOptions(List<BeJudjeOptions> options) {
        this.options = options;
    }

    public List<BeJudjeOptions> getOptions() {
        return options;
    }
}
