package cn.dajiahui.kid.ui.homework.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/11.
 * <p>
 * 连线题模型
 */

public class LineQuestionModle extends QuestionModle implements Serializable {


    private BeLineOptions options;//选项内容

    /*连线题保存答案的坐标点*/
    private List<DrawPath> drawPathList = new ArrayList();

    public List<DrawPath> getDrawPathList() {
        return drawPathList;
    }

    public BeLineOptions getOptions() {
        return options;
    }

    public void setOptions(BeLineOptions options) {
        this.options = options;
    }

    public void setDrawPathList(List<DrawPath> drawPathList) {
        this.drawPathList = drawPathList;
    }
}
