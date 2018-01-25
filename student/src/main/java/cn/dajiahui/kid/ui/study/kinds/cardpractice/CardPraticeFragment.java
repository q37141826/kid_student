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

import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.LazyLoadFragment;
import cn.dajiahui.kid.ui.study.Rotate3dAnimation;
import cn.dajiahui.kid.ui.study.bean.BeCradPratice;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.CardView;
import cn.dajiahui.kid.util.Logger;


/**
 * 卡片练习
 */

public class CardPraticeFragment extends LazyLoadFragment {
    private final int MUSIC_STOP = 0;
    protected Bundle bundle;// 用于保存信息以及标志
    private BeCradPratice beCradPratice;
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
    //    private Rotate3dAnimation closeAnimation;
    private boolean isOpen = false;


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
//                        initCloseAnim();
                    }

                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

//                    //用作判断当前点击事件发生时动画是否正在执行
//                    if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
//                        return;
//                    }
//                    if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
//                        return;
//                    }
                    //判断动画执行
//                    if (isOpen) {
////                        cardView.startAnimation(closeAnimation);
//                    } else {
//                        cardView.startAnimation(openAnimation);
//
//                    }

                }

            }
        }


    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        noticeCheckButton = (NoticeCheckButton) activity;
    }

//    @Override
//    protected View initinitLayout(LayoutInflater inflater) {
//        bundle = getArguments();
//        beCradPratice = (BeCradPratice) bundle.getSerializable("BeCradPratice");
//
//        return inflater.inflate(R.layout.fr_card, null);
//    }

    /*启动计时器*/
    private void startTimer() {
        //参数2：延迟0毫秒发送，参数3：每隔1000毫秒秒发送一下
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
            }, 20, 20);
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
        Logger.d("setContentView:");
        return R.layout.fr_card;
    }

    @Override
    protected void lazyLoad() {
        Logger.d("lazyLoad:");
        bundle = getArguments();
        beCradPratice = (BeCradPratice) bundle.getSerializable("BeCradPratice");
        int position = (int) bundle.get("position");
        noticeCheckButton.NoticeCheck(false, position);
        initialize();
        Logger.d("进入播放：----------" + beCradPratice.getAudio_url());
        PlayMedia.getPlaying().StartMp3(beCradPratice.getAudio_url());
        startTimer();

        cardView = new CardView(getActivity(), beCradPratice);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        cardView.setLayoutParams(lp);

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

    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("-------------onPause()");
    }


    private void initialize() {
//        imgpaly = getView(R.id.img_paly);
//        cardroot = getView(R.id.card_root);
//        imgpaly.setOnClickListener(onClick);
        imgpaly = findViewById(R.id.img_paly);
        cardroot = findViewById(R.id.card_root);
        imgpaly.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.d("点击播放：----------" + beCradPratice.getAudio_url());
            PlayMedia.getPlaying().StartMp3(beCradPratice.getAudio_url());
            PlayMedia.getPlaying().setOnCompletionListener(1);
        }
    };

    /*通知活动按钮*/
    public interface NoticeCheckButton {
        public void NoticeCheck(boolean ischeck, int position);
    }

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
                cardView.textView.setVisibility(View.GONE);
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

//    /**
//     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
//     */
//    private void initCloseAnim() {
//        closeAnimation = new Rotate3dAnimation(360, 270, centerX, centerY, depthZ, true);
//        closeAnimation.setDuration(duration);
//        closeAnimation.setFillAfter(true);
//        closeAnimation.setInterpolator(new AccelerateInterpolator());
//        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                cardView.textView.setVisibility(View.GONE);
//                cardView.imageView.setVisibility(View.VISIBLE);
//
//                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(90, 0, centerX, centerY, depthZ, false);
//                rotateAnimation.setDuration(duration);
//                rotateAnimation.setFillAfter(true);
//                rotateAnimation.setInterpolator(new DecelerateInterpolator());
//                cardView.startAnimation(rotateAnimation);
//            }
//        });
//    }
}

