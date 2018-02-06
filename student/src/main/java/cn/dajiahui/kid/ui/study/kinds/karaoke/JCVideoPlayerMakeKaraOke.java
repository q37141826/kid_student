package cn.dajiahui.kid.ui.study.kinds.karaoke;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fxtx.framework.util.ActivityUtil;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/*

MakeTextBookDrmaActivity的播放器

*制作课本剧的播放器
*
* */
public class JCVideoPlayerMakeKaraOke extends JCVideoPlayerStandard {
    private OnDuration onDuration;

    public JCVideoPlayerMakeKaraOke(Context context) {
        super(context);
    }

    public JCVideoPlayerMakeKaraOke(Context context, AttributeSet attrs) {
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
                /*结束制作课本剧activity*/
                ActivityUtil.getInstance().finishActivity(MakeKraoOkeActivity.class);

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
        thumbImageView.setVisibility(GONE);//
        battery_level.setVisibility(GONE);
        fullscreenButton.setVisibility(GONE);//全屏按钮

        seetingloadingProgressBarUncheck();//禁止progressBar触摸
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


    @Override
    public int getDuration() {
        return JCMediaManager.instance().mediaPlayer.getDuration();
    }

    public MediaPlayer getMediaPlayer() {

        return JCMediaManager.instance().mediaPlayer;
    }


    @Override
    public void onStatePreparingChangingUrl(int urlMapIndex, int seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);

    }


    public void seetingloadingProgressBarUncheck() {
        /*设置进度条不能拖动*/
        progressBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    /*暂停时*/
    @Override
    public void onStatePause() {
        super.onStatePause();
        cancelDismissControlViewTimer();
        JCMediaManager.instance().mediaPlayer.pause();
    }


}