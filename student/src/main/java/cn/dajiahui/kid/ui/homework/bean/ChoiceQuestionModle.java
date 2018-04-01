package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 选择题
 */

public class ChoiceQuestionModle extends QuestionModle implements Serializable {



    private List<BeChoiceOptions> options;//选项内容


    public List<BeChoiceOptions> getOptions() {
        return options;
    }

    public void setOptions(List<BeChoiceOptions> options) {
        this.options = options;
    }
}
