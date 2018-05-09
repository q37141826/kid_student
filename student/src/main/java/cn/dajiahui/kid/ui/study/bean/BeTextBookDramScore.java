package cn.dajiahui.kid.ui.study.bean;


public class BeTextBookDramScore {


    private boolean isScore = false;//是否打分标志
    private BeChivoxEvaluateResult beChivoxEvaluateResult;//池声打分数据

    public BeTextBookDramScore(boolean isScore, BeChivoxEvaluateResult beChivoxEvaluateResult) {
        this.isScore = isScore;
        this.beChivoxEvaluateResult = beChivoxEvaluateResult;
    }


    public boolean isScore() {
        return isScore;
    }

    public void setScore(boolean score) {
        isScore = score;
    }

    public BeChivoxEvaluateResult getBeChivoxEvaluateResult() {
        return beChivoxEvaluateResult;
    }
}
