package com.fxtx.framework.lamemp3;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

import com.fxtx.framework.text.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

/**
 * MP3录制集合类。提供了mp3的播放和录制功能
 *
 * @author Administrator
 */
public class Mp3Audio {
    private Mp3Recorder recorder; // 录制对象
    private MediaPlayer mPlayer;// 播放对象
    private Context context;
    private AudioListener listener; // 监听对象
    public Timer timer;

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener;

    public Mp3Audio(Context context, AudioListener listeners) {
        this.context = context;
        recorder = new Mp3Recorder(context, listeners);
        this.listener = listeners;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    /**
     * 设置录音最大时限 单位秒
     */
    public void setMp3MaxTimers(int timers) {
        recorder.setMaxTime(timers);
    }

    boolean isStop = false;

    //初始化录音
    public void initAudio(String filePath) {
        mPlayer = new MediaPlayer();
        timer = new Timer();
        mPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // 反馈 播放结束
                timer.cancel();// 结束计时
                mPlayer.stop();
                listener.onPlayStop();
                try {
                    mPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (filePath.startsWith("http")) { //如果是http开头的那么就是音频
                mPlayer.setDataSource(context, Uri.parse(filePath));
                if (onBufferingUpdateListener != null)
                    mPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
                if (onPreparedListener != null)
                    mPlayer.setOnPreparedListener(onPreparedListener);
                mPlayer.prepareAsync();

            } else {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    private MediaPlayer.OnPreparedListener onPreparedListener;

    /**
     * 设置初始化状态监听
     *
     * @param onPreparedListener
     */
    public void setOnPrepredListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    /**
     * 设置缓冲监听
     *
     * @param onBufferingUpdateListener
     */
    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
        this.onBufferingUpdateListener = onBufferingUpdateListener;
    }


    public void startAudio(String filepath) {
        initAudio(filepath);
        try {
            mPlayer.start();
            listener.onPlayStart();
            timer.schedule(new TimerRunning(), 0, 200);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        } catch (SecurityException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    public String url;

    public void startAudio() {
        if (mPlayer == null && !StringUtil.isEmpty(url)) {
            initAudio(url);
        }
        try {
            mPlayer.start();
            listener.onPlayStart();
            isStop = false;
            timer.schedule(new TimerRunning(), 0, 200);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        } catch (SecurityException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    public void paushAudio() {
        if (mPlayer != null) {
            mPlayer.pause();
            listener.paushAudio();
        }
    }

    public boolean isPlayer() {
        if (mPlayer != null)
            return mPlayer.isPlaying();
        else {
            return false;
        }
    }

    public int getAudioDuration() {
        return mPlayer != null ? mPlayer.getDuration() / 1000 : 0;
    }

    /**
     * ͣ停止播放
     */
    public void stopAudio() {
        if (timer != null) {
            timer.cancel();// 结束计时
        }
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        listener.onPlayStop();

    }

    /**
     * 开始录制
     */
    public void startRecorder() {
        recorder.startRecording();
    }

    /**
     * 停止录制
     */
    public void stopRecorder() {
        recorder.stopRecording();
    }

    public boolean isDuration = false; //控制返回的时间是否显示毫秒


    /**
     * 获取录制文件存放地址
     *
     * @return
     */
    public File getFile() {
        return recorder.getFile();
    }

    class TimerRunning extends java.util.TimerTask {
        @Override
        public void run() {
            if (listener != null && mPlayer != null) {
                if (isDuration) {
                    listener.onDuration(mPlayer.getCurrentPosition()); // 接口回调
                } else {
                    listener.onDuration(mPlayer.getCurrentPosition() / 1000); // 接口回调
                }
            }
        }
    }

    /**
     * 获取时间 录音文件的时间长度
     *
     * @return
     */
    public int getMaxDuration() {
        return recorder.getMaxDuration();
    }

    /**
     * 获取播放文件的时间长度
     *
     * @return
     */
    public int getPlayDuration() {
        return mPlayer.getDuration();
    }

}
