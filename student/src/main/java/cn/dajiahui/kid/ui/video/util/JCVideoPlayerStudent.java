package cn.dajiahui.kid.ui.video.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import com.fxtx.framework.util.ActivityUtil;

import cn.dajiahui.kid.ui.mine.myworks.VideoActivity;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JCVideoPlayerStudent extends JCVideoPlayerStandard {
    private OnDuration onDuration;

    public JCVideoPlayerStudent(Context context) {
        super(context);
    }

    public JCVideoPlayerStudent(Context context, AttributeSet attrs) {
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
                    /*我的作品*/
                ActivityUtil.getInstance().finishActivity(VideoActivity.class);

            }
        }
    }


    @Override
    public void setProgressAndText(int progress, int position, int duration) {
        super.setProgressAndText(progress, position, duration);
        if (onDuration != null) {
            onDuration.onDuration(totalTimeTextView.getText().toString());
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

    }

    public void hideBackButton() {
        backButton.setVisibility(GONE);//隐藏退出
    }

    public MediaPlayer getMediaPlayer() {

        return JCMediaManager.instance().mediaPlayer;
    }


//    public void onPause() {
//        getMediaPlayer().pause();
//        changeUiToPauseShow();
//        cancelDismissControlViewTimer();
//    }


    @Override
    public void startVideo() {
        super.startVideo();
    }
}