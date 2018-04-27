package cn.dajiahui.kid.ui.study.bean;

/*池声流利度*/
public class Fluency {
    private String overall;  //流利度

    public String getOverall() {
        return overall;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    @Override
    public String toString() {
        return "Fluency{" +
                "overall='" + overall + '\'' +
                '}';
    }
}
