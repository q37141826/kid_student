package cn.dajiahui.kid.ui.study.mediautil;

import android.media.MediaPlayer;

import java.io.IOException;

import cn.dajiahui.kid.util.Logger;

/**
 * Created by lenovo on 2018/1/22.
 * <p>
 * 播放音频
 */

public class PlayMedia {

    private static PlayMedia playMedia;
    public boolean complete = false;//播放完成标志
    public MediaPlayer mediaPlayer = new MediaPlayer();

    public PlayMedia() {

    }


    //静态单利模式
    public static PlayMedia getPlaying() {

        if (playMedia == null) {
            playMedia = new PlayMedia();

        }
        return playMedia;
    }

    /*指定播放位置 毫秒*/

    public void Mp3seekTo(String mp3path, int starttime) {
        Logger.d("播放地址：" + mp3path);
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mp3path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(starttime);
            /*设置监听事件*/
            setListener();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*停止mp3 释放资源*/
    public void StopMp3() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }


    /*暂停mp3*/
    public void PauseMp3() {
        mediaPlayer.pause();
    }

    /*开始播放*/
    public void StartMp3(String mp3Url) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mp3Url);
            mediaPlayer.prepare();
            setListener();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void setListener() {
        /*准备播放*/
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


            @Override
            public void onPrepared(MediaPlayer mp) {

                mediaPlayer.start();
            }


        });
        /*播放完成*/
        setOnCompletionListener(0);

            /*播放失败*/
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()

        {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Logger.d("播放失败！");
                return false;
            }
        });
            /*进度监听*/
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()

        {
            @Override
            public void onSeekComplete(MediaPlayer mp) {


            }
        });

    }

    /*i=0  i=1是卡片练习next之后应该把complete=false  0和1就是区别卡片练习的翻转动画的条件（complete）*/
    /*监听setOnCompletionListener  返回false时就不可以翻转动画*/
    public boolean setOnCompletionListener(final int i) {

                /*播放完成*/
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                complete = true;
                /*释放资源*/
                mediaPlayer.stop();

                if (i == 1) complete = false;
            }
        });

        return complete;
    }
}
