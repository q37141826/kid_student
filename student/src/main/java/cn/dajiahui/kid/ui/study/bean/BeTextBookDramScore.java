package cn.dajiahui.kid.ui.study.bean;



public class BeTextBookDramScore {


    private boolean isScore = false;//是否打分标志
    private int score = 0;//具体分数


    public BeTextBookDramScore(boolean isScore, int score) {
        this.isScore = isScore;
        this.score = score;
    }

    public boolean isScore() {
        return isScore;
    }

    public void setScore(boolean score) {
        isScore = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
