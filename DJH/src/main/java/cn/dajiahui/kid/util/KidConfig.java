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
    /*课本剧无声视频*/
    private String pathTextbookPlayNoSoundVideo = this.pathTextbookPlay + "TextbookPlayNoSoundVideo" + File.separator;
    /*课本剧背景音*/
    private String pathTextbookPlayBackgroundAudio = this.pathTextbookPlay + "TextbookPlayBackgroundAudio" + File.separator;
    /*课本剧Mp4*/
    private String pathTextbookPlayMp4 = this.pathTextbookPlay + "TextbookPlayMp4" + File.separator;

    /*混音 mp3*/
    private String pathMixAudios = this.pathRoot + "MixAudios" + File.separator;
    //录音
    private String pathRecordingAudio = this.pathRoot + "RecordingAudio" + File.separator;
    /*我的作品临时文件夹*/
    private String pathMineWorksTemp = this.pathRoot + "MineWorksTemp" + File.separator;


    /*卡拉ok*/
    private String pathKaraOke = this.pathRoot + "KaraOke" + File.separator;
    /*卡拉ok的Mp3 背景音*/
    private String pathKaraOkeBackgroundAudio = this.pathKaraOke + "KaraOkeBackgroundAudio" + File.separator;
    /*卡拉ok的Mp4*/
    private String pathKaraOkeMp4 = this.pathKaraOke + "KaraOkeMp4" + File.separator;
    /*卡拉ok的无声Mp4*/
    private String pathKaraOkeNoSoundVideo = this.pathKaraOke + "KaraOkeNoSoundVideo" + File.separator;

    /*随身听*/
    private String pathPersonalStereo = this.pathRoot + "PersonalStereo" + File.separator;
    /*卡片练习*/
    private String pathCardPratice = this.pathRoot + "CardPratice" + File.separator;


    /*我的作品*/
    private String pathMineWorks = this.pathRoot + "MineWorks" + File.separator;
    /*我的作品中的课本剧*/
    private String pathMineWorksTextBookDrama = this.pathMineWorks + "TextBookDrama" + File.separator;
    /*我的作品中的卡拉ok*/
    private String pathMineWorksKaraOke = this.pathMineWorks + "KaraOke" + File.separator;
    /*我的作品缩略图*/
    private String pathMineWorksThumbnail = this.pathMineWorks + "Thumbnail" + File.separator;

    /*班级空间图片*/
    private String pathClassSpace = this.pathRoot + "ClassSpace" + File.separator;

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
        folder = new File(this.pathTextbookPlayBackgroundAudio);
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
        folder = new File(this.pathTextbookPlayNoSoundVideo);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathMineWorksTemp);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathKaraOke);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathKaraOkeBackgroundAudio);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathKaraOkeMp4);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathMineWorksTextBookDrama);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathMineWorksKaraOke);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathTextbookPlayMp4);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathKaraOkeNoSoundVideo);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathPersonalStereo);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathCardPratice);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathMineWorksThumbnail);
        if (!folder.exists()) {
            folder.mkdir();
        }
        folder = new File(this.pathClassSpace);
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

    public String getPathTextbookPlayBackgroundAudio() {
        return pathTextbookPlayBackgroundAudio;
    }

    public String getPathMixAudios() {
        return pathMixAudios;
    }

    public String getPathMineWorks() {
        return pathMineWorks;
    }

    public String getPathTextbookPlayNoSoundVideo() {
        return pathTextbookPlayNoSoundVideo;
    }

    public String getPathMineWorksTemp() {
        return pathMineWorksTemp;
    }

    public String getPathKaraOke() {
        return pathKaraOke;
    }

    public String getPathKaraOkeBackgroundAudio() {
        return pathKaraOkeBackgroundAudio;
    }

    public String getPathKaraOkeMp4() {
        return pathKaraOkeMp4;
    }

    public String getPathMineWorksTextBookDrama() {
        return pathMineWorksTextBookDrama;
    }

    public String getPathMineWorksKaraOke() {
        return pathMineWorksKaraOke;
    }

    public String getPathTextbookPlayMp4() {
        return pathTextbookPlayMp4;
    }

    public String getPathKaraOkeNoSoundVideo() {
        return pathKaraOkeNoSoundVideo;
    }

    public String getPathPersonalStereo() {
        return pathPersonalStereo;
    }

    public String getPathCardPratice() {
        return pathCardPratice;
    }

    public String getPathMineWorksThumbnail() {
        return pathMineWorksThumbnail;
    }

    public String getPathClassSpace() {
        return pathClassSpace;
    }
}
