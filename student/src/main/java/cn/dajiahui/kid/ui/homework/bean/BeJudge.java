package cn.dajiahui.kid.ui.homework.bean;

/**
 * 判断模型
 */

public class BeJudge extends BaseBean {

    private String answerflag;
    private int subjectype = 0;//当前题型
    private String audiourl;
    private boolean Whetheranswer;//是否作答（记录当前页面check）
    private boolean answer;//答案


    public String getAnswerflag() {
        return answerflag;
    }

    public void setAnswerflag(String answerflag) {
        this.answerflag = answerflag;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }
    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }


    public String getaudiourl() {
        return audiourl;
    }

    public void setaudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public boolean isWhetheranswer() {
        return Whetheranswer;
    }

    public void setWhetheranswer(boolean whetheranswer) {
        Whetheranswer = whetheranswer;
    }

    public int getSubjectype() {
        return subjectype;
    }

    public void setSubjectype(int subjectype) {
        this.subjectype = subjectype;
    }


}
