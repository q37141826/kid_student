package cn.dajiahui.kid.ui.homework.bean;

/**
 * Created by lenovo on 2018/1/12.
 * 保存连线题的答案 逻辑
 */

public class BeAnswer {

    private  String leftAnswer;
    private  String rightAnswer;


    public BeAnswer(String leftAnswer, String rightAnswer) {
        this.leftAnswer = leftAnswer;
        this.rightAnswer = rightAnswer;
    }

    public String getLeftAnswer() {
        return leftAnswer;
    }

    public void setLeftAnswer(String leftAnswer) {
        this.leftAnswer = leftAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
