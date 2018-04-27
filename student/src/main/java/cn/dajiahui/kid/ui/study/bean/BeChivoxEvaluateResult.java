package cn.dajiahui.kid.ui.study.bean;

import java.util.List;

import cn.dajiahui.kid.util.BeanObj;

/**
 * 驰声测评英语句子返回的数据bean
 * Created by wangzhi on 2018/3/5.
 */

public class BeChivoxEvaluateResult extends BeanObj {
    private String overall; // 总分
    private String integrity; //完整度
    private Fluency fluency; //流利度
    private String accuracy; //准确度
    private Rhythm rhythm; //韵律性
    private List<Details> details; //得分详情


    public String getOverall() {
        return overall;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    public Fluency getFluency() {
        return fluency;
    }

    public void setFluency(Fluency fluency) {
        this.fluency = fluency;
    }

    public String getIntegrity() {
        return integrity;
    }

    public void setIntegrity(String integrity) {
        this.integrity = integrity;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public Rhythm getRhythm() {
        return rhythm;
    }

    public void setRhythm(Rhythm rhythm) {
        this.rhythm = rhythm;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }


}
