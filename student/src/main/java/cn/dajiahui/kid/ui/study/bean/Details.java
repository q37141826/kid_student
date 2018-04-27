package cn.dajiahui.kid.ui.study.bean;

public class Details {
    private String score;//单词得分

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Details{" +
                "score='" + score + '\'' +
                '}';
    }
}
