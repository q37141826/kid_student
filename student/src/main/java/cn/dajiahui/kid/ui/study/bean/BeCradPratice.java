package cn.dajiahui.kid.ui.study.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/1/25.
 */

public class BeCradPratice implements Serializable {

    private String img_url;
    private String audio_url;
    private String english;
    private String chinese;


    public BeCradPratice(String img_url, String audio_url, String english, String chinese) {
        this.img_url = img_url;
        this.audio_url = audio_url;
        this.english = english;
        this.chinese = chinese;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
