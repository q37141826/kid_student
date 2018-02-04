package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import com.fxtx.framework.util.ActivityUtil;

import cn.dajiahui.kid.util.Logger;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/*
*制作课本剧成功的播放器
*
* */
public class JCVideoPlayerTextScuessBook extends JCVideoPlayerStandard {
    private OnDuration onDuration;

    public JCVideoPlayerTextScuessBook(Context context) {
        super(context);
    }

    public JCVideoPlayerTextScuessBook(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        backButton.setVisibility(View.VISIBLE);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == fm.jiecao.jcvideoplayer_lib.R.id.back) {
            if (currentScreen != SCREEN_WINDOW_FULLSCREEN) {
                ActivityUtil.getInstance().finishActivity(TextBookSuccessActivity.class);

                Logger.d("视频返回键");
            }

        }
    }


    public void setOnDuration(OnDuration onDuration) {
        this.onDuration = onDuration;
    }

    public interface OnDuration {
        void onDuration(String duration);
    }

    /*隐藏电池的视图*/
    public void hideView() {

        batteryTimeLayout.setVisibility(GONE);//隐藏电池
        retryTextView.setVisibility(GONE);//
        clarity.setVisibility(GONE);//
        thumbImageView.setVisibility(GONE);//
        battery_level.setVisibility(GONE);
        fullscreenButton.setVisibility(GONE);//全屏按钮
    }

    public void videoSeekTo(int time) {
        if (JCMediaManager.instance().mediaPlayer != null) {

            JCMediaManager.instance().mediaPlayer.seekTo(time);
        }
    }

    public int getCurrentPosition() {

        int currentPosition = JCMediaManager.instance().mediaPlayer.getCurrentPosition();

        return currentPosition;
    }

    public MediaPlayer getMediaPlayer() {

        return JCMediaManager.instance().mediaPlayer;
    }


    @Override
    public void onStatePreparingChangingUrl(int urlMapIndex, int seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);

    }



    /*暂停时*/
    @Override
    public void onStatePause() {
        super.onStatePause();
        cancelDismissControlViewTimer();
        JCMediaManager.instance().mediaPlayer.pause();
    }


}