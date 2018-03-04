package cn.dajiahui.kid.ui.study.kinds.cardpractice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fxtx.framework.file.FileUtil;

import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeCradPraticePageData;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.CardView;
import cn.dajiahui.kid.ui.study.view.LazyLoadFragment;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;


/**
 * 卡片练习
 */

public class CardPraticeFragment extends LazyLoadFragment {
    private final int MUSIC_STOP = 0;
    protected Bundle bundle;// 用于保存信息以及标志
    private BeCradPraticePageData beCradPratice;
    private ImageView imgpaly;
    private LinearLayout cardroot;
    private Timer timer;
    private CardView cardView;
    private NoticeCheckButton noticeCheckButton;

    private int centerX;
    private int centerY;
    private int depthZ = 400;
    private int duration = 600;
    private Rotate3dAnimation openAnimation;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == MUSIC_STOP) {
                if (timer != null) {
                    Logger.d("handleMessage");
                    cardView.setBackgroundResource(R.drawable.select_readingbook_bg_green);

                    //以旋转对象的中心点为旋转中心点，这里主要不要再onCreate方法中获取，因为视图初始绘制时，获取的宽高为0
                    centerX = cardView.getWidth() / 2;
                    centerY = cardView.getHeight() / 2;
                    if (openAnimation == null) {
                        initOpenAnim();
                        cardView.startAnimation(openAnimation);
                    }

                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                }

            }
        }


    };
    private int position;
    private String music_oss_url;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        noticeCheckButton = (NoticeCheckButton) activity;
    }

    /*启动计时器*/
    private void startTimer() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (PlayMedia.getPlaying().mediaPlayer != null) {
                        if (PlayMedia.getPlaying().setOnCompletionListener(0)) {
                            mHandler.sendEmptyMessage(MUSIC_STOP); // 发送消息
                            PlayMedia.getPlaying().complete = false;
                            Logger.d("启动计时器");
                            return;
                        }
                    }
                }
            }, 0, 200);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }

    @Override
    protected int setContentView() {
        return R.layout.fr_card;
    }

    @Override
    protected void lazyLoad() {
        Logger.d("lazyLoad:");
        bundle = getArguments();
        beCradPratice = (BeCradPraticePageData) bundle.getSerializable("BeCradPraticePageData");
        position = (int) bundle.get("position");
        noticeCheckButton.NoticeCheck(false, position);
        initialize();
        music_oss_url = beCradPratice.getMusic_oss_url();
        playMedia();
        startTimer();
        cardView = new CardView(getActivity(), beCradPratice);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.topMargin = 100;
        lp.leftMargin = 50;
        lp.rightMargin = 50;
        cardView.setLayoutParams(lp);
        cardView.setBackgroundResource(R.drawable.select_readingbook_bg_red);
        cardroot.addView(cardView);


    }



    @Override
    protected void stopLoad() {
        cardroot.removeAllViews();
        Logger.d("stopLoad:");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        PlayMedia.getPlaying().StopMp3();
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("-------------onPause()");
        PlayMedia.getPlaying().PauseMp3();
    }


    private void initialize() {
        imgpaly = findViewById(R.id.img_paly);
        cardroot = findViewById(R.id.card_root);
        imgpaly.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            playMedia();
            PlayMedia.getPlaying().setOnCompletionListener(1);
        }
    };


    /**
     * 卡牌文本介绍打开效果：注意旋转角度
     */
    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        openAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.linearLayout.setVisibility(View.GONE);
                cardView.imageView.setVisibility(View.VISIBLE);

                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                cardView.startAnimation(rotateAnimation);

                noticeCheckButton.NoticeCheck(true, -1);
            }
        });
    }

    /*播放音频*/
    private void playMedia() {
               /*文件名以MD5加密*/
        String mp3Name = MD5.getMD5(music_oss_url.substring(music_oss_url.lastIndexOf("/"))) + ".mp3";

        if (FileUtil.fileIsExists(KidConfig.getInstance().getPathCardPratice() + mp3Name)) {
            /*读取本地*/
            PlayMedia.getPlaying().StartMp3(KidConfig.getInstance().getPathCardPratice() + mp3Name);

        } else {
             /*读取网络*/
            PlayMedia.getPlaying().StartMp3(music_oss_url);
        }

    }

    /*通知活动按钮*/
    public interface NoticeCheckButton {
        public void NoticeCheck(boolean ischeck, int position);
    }
}

