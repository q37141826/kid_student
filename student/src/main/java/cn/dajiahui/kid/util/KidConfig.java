package cn.dajiahui.kid.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by mj on 2018/01/24.
 */

public class KidConfig {

    // Singleton

    private static KidConfig instance;

    private KidConfig() {

    }

    //写成静态单利
    public static KidConfig getInstance() {

        if (instance == null) {

            instance = new KidConfig();
        }
        return instance;
    }

    private String pathUserRoot;

    // Const Config
    private String envConfig = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String pathRoot = this.envConfig + File.separator + "kid_student" + File.separator;
    /*临时文件夹*/
    private String pathTemp = this.pathRoot + "temp" + File.separator;
    /*点读本*/
    private String pathPointRedaing = this.pathRoot + "PointReading" + File.separator;
    /*课本剧*/
    private String pathTextbookPlay = this.pathRoot + "TextbookPlay" + File.separator;
    /*课本剧*/
    private String pathNoSoundVideo = this.pathRoot + "NoSoundVideo" + File.separator;
    //录音
    private String pathRecordingAudio = this.pathRoot + "RecordingAudio" + File.separator;
    /*背景音*/
    private String pathBackgroundAudio = this.pathRoot + "BackgroundAudio" + File.separator;
    /*混音 mp3*/
    private String pathMixAudios = this.pathRoot + "MixAudios" + File.separator;
    /*我的作品临时文件夹*/
    private String pathMineWorksTemp = this.pathRoot + "MineWorksTemp" + File.separator;
    /*我的作品*/
    private String pathMineWorks = this.pathRoot + "MineWorks" + File.separator;


    public void init() {
        // App Init
        File folder = null;

        folder = new File(this.pathRoot);
        if (!folder.exists()) {
            folder.mkdir();
        }

        folder = new File(this.pathTemp);
        if (!folder.exists()) {
            folder.mkdir();
        }

        folder = new File(this.pathRecordingAudio);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathPointRedaing);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathTextbookPlay);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathBackgroundAudio);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathMixAudios);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathMineWorks);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathNoSoundVideo);
        if (!folder.exists()) {
            folder.mkdir();
        }
 folder = new File(this.pathMineWorksTemp);
        if (!folder.exists()) {
            folder.mkdir();
        }

    }


    public void initUserConfig(String userPhone) {

        //User Init
        this.pathUserRoot = this.pathRoot + userPhone + File.separator;

        File folder = null;
        folder = new File(this.pathUserRoot);
        if (!folder.exists()) {
            folder.mkdir();
        }

    }

    public String getPathRoot() {
        return pathRoot;
    }

    public String getPathUserRoot() {
        return pathUserRoot;
    }

    public String getPathTemp() {
        return pathTemp;
    }

    public String getPathPointRedaing() {
        return pathPointRedaing;
    }

    public String getPathTextbookPlay() {
        return pathTextbookPlay;
    }

    public String getPathRecordingAudio() {
        return pathRecordingAudio;
    }

    public String getPathBackgroundAudio() {
        return pathBackgroundAudio;
    }

    public String getPathMixAudios() {
        return pathMixAudios;
    }

    public String getPathMineWorks() {
        return pathMineWorks;
    }
    public String getPathNoSoundVideo() {
        return pathNoSoundVideo;
    }

    public String getPathMineWorksTemp() {
        return pathMineWorksTemp;
    }
}
