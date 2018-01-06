package cn.dajiahui.kid.ui.homework.bean;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 答题卡
 */

public class BeAnswerCard extends BeanObj {

    public BeAnswerCard(int answertrue, int answercurrentnum) {
        this.answertrue = answertrue;
        this.answercurrentnum = answercurrentnum;
    }

    private int answertrue;// 正确
    private int answerfalse;//错误
    private int noanswer;//错误

    private int answercurrentnum;//当前答题数


    public BeAnswerCard(int answertrue, int answerfalse, int noanswer, int answercurrentnum) {
        this.answertrue = answertrue;
        this.answerfalse = answerfalse;
        this.noanswer = noanswer;
        this.answercurrentnum = answercurrentnum;
    }

    public int getAnswertrue() {
        return answertrue;
    }

    public void setAnswertrue(int answertrue) {
        this.answertrue = answertrue;
    }

    public int getAnswerfalse() {
        return answerfalse;
    }

    public void setAnswerfalse(int answerfalse) {
        this.answerfalse = answerfalse;
    }

    public int getNoanswer() {
        return noanswer;
    }

    public void setNoanswer(int noanswer) {
        this.noanswer = noanswer;
    }

    public int getanswercurrentnum() {
        return answercurrentnum;
    }

    public void setanswercurrentnum(int answercurrentnum) {
        this.answercurrentnum = answercurrentnum;
    }


}
