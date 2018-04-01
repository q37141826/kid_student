package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.widgets.dialog.FxDialog;

import cn.dajiahui.kid.ui.study.kinds.karaoke.KaraOkeActivity;
import cn.dajiahui.kid.ui.study.kinds.karaoke.MakeKraoOkeActivity;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/*
*制作课本剧成功的播放器
*
* */
public class JCVideoPlayerTextScuessBook extends JCVideoPlayerStandard {
    private OnDuration onDuration;
    private Context context;


    public JCVideoPlayerTextScuessBook(Context context) {
        super(context);
        this.context = context;

    }

    public JCVideoPlayerTextScuessBook(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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
                /*制作卡拉ok未上传服务器退出*/
                if (TextBookSuccessActivity.CLOSE.equals("MakeKraoOke")) {
//                    TextBookSuccessActivity.mHandler.sendEmptyMessage(1);
                    FxDialog fxDialog = new FxDialog(context ) {
                        @Override
                        public void onRightBtn(int flag) {
                               /*弹框作品未保存是否退出*/
                            ActivityUtil.getInstance().finishActivity(TextBookSuccessActivity.class);
                            ActivityUtil.getInstance().finishActivity(MakeKraoOkeActivity.class);
                            dismiss();
                        }

                        @Override
                        public void onLeftBtn(int flag) {

                            dismiss();
                        }
                    };
                    fxDialog.setMessage("作品未保存,是否退出？");
                    fxDialog.show();
                }
                /*制作课本剧未上传服务器退出*/
                else if (TextBookSuccessActivity.CLOSE.equals("MakeTextBookDrma")) {

                    FxDialog fxDialog = new FxDialog(context) {
                        @Override
                        public void onRightBtn(int flag) {
                               /*弹框作品未保存是否退出*/
                            ActivityUtil.getInstance().finishActivity(TextBookSuccessActivity.class);
                            ActivityUtil.getInstance().finishActivity(MakeTextBookDrmaActivity.class);
                            dismiss();
                        }

                        @Override
                        public void onLeftBtn(int flag) {

                            dismiss();
                        }
                    };
                    fxDialog.setMessage("作品未保存,是否退出？");
                    fxDialog.show();

                }
                /*制作卡拉ok上传服务器成功然后退出*/
                else if (TextBookSuccessActivity.CLOSE.equals("MakeKraoOkeSuccess")) {

                    ActivityUtil.getInstance().finishActivity(KaraOkeActivity.class);
                    ActivityUtil.getInstance().finishActivity(MakeKraoOkeActivity.class);
                    ActivityUtil.getInstance().finishActivity(TextBookSuccessActivity.class);
                }
                  /*制作课本剧未上传服务器成功然后退出*/
                else if (TextBookSuccessActivity.CLOSE.equals("MakeTextBookDrmaSuccess")) {
                    ActivityUtil.getInstance().finishActivity(TextBookDramaActivity.class);
                    ActivityUtil.getInstance().finishActivity(MakeTextBookDrmaActivity.class);
                    ActivityUtil.getInstance().finishActivity(TextBookSuccessActivity.class);
                } else {
                    ActivityUtil.getInstance().finishActivity(TextBookSuccessActivity.class);
                }
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
//        JCMediaManager.instance().mediaPlayer.pause();
    }


}