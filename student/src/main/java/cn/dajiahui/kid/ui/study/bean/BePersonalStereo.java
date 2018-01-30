package cn.dajiahui.kid.ui.study.bean;

/**
 * Created by majin on 2018/1/26.
 */

public class BePersonalStereo {

    private String title;
    private String audio_url;

    public BePersonalStereo(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }
}
