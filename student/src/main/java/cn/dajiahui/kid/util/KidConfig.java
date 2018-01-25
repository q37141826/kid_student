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
    /**/
    private String pathPointRedaing = this.pathRoot + "PointReading" + File.separator;

    //录音
    private String pathSoundRecording = this.pathRoot + "SoundRecording" + File.separator;


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

        folder = new File(this.pathSoundRecording);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathPointRedaing);
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

    public String getPathSoundRecording() {
        return pathSoundRecording;
    }

    public String getPathPointRedaing() {
        return pathPointRedaing;
    }


}
