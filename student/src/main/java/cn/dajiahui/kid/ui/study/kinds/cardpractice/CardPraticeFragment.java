package cn.dajiahui.kid.ui.study.kinds.cardpractice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.util.BaseUtil;

import java.io.IOException;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeCradPraticePageData;
import cn.dajiahui.kid.ui.study.view.CardView;
import cn.dajiahui.kid.ui.study.view.LazyLoadFragment;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;


/**
 * 卡片练习
 */

public class CardPraticeFragment extends LazyLoadFragment {

    protected Bundle bundle;// 用于保存信息以及标志
    private BeCradPraticePageData beCradPratice;
    private ImageView imgpaly;
    private TextView mScore;
    private RelativeLayout cardroot;
    private CardView cardView;
    private NoticeCheckButton noticeCheckButton;

    private int centerX;
    private int centerY;
    private int depthZ = 400;
    private int duration = 600;
    public MediaPlayer mediaPlayer = null;
    private int position;
    private String music_oss_url;

    private Rotate3dAnimation rotateAnimation;
    private Rotate3dAnimation openAnimation;
    private AnimationDrawable animationDrawable;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    if (openAnimation == null) {
                        /*设置绿色边框*/
                        cardView.setBackgroundResource(R.drawable.select_readingbook_bg_green);
                        //以旋转对象的中心点为旋转中心点，这里主要不要再onCreate方法中获取，因为视图初始绘制时，获取的宽高为0
                        centerX = cardView.getWidth() / 2;
                        centerY = cardView.getHeight() / 2;

                        /*设置动画*/
                        initOpenAnim();
                        cardView.startAnimation(openAnimation);
                    }

                    break;

                case 2:

                    cardView.linShort.setVisibility(View.VISIBLE);
                    cardView.tv_english.setVisibility(View.VISIBLE);
                    cardView.tv_chinese.setVisibility(View.VISIBLE);
                    cardView.imageView.setVisibility(View.GONE);

                    if (rotateAnimation == null) {
                        //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                        rotateAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ, false);
                        rotateAnimation.setDuration(duration);
                        rotateAnimation.setFillAfter(true);
                        rotateAnimation.setInterpolator(new DecelerateInterpolator());
                        cardView.startAnimation(rotateAnimation);

                        /*动画监听*/
                        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                mHandler.sendEmptyMessage(3);
                            }
                        });
                    }
                    break;
                case 3:
                    playMedia();
                    break;

                default:
                    break;

            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        noticeCheckButton = (NoticeCheckButton) activity;
    }

    int mPlayNum = 0;


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
        bundle = getArguments();
        beCradPratice = (BeCradPraticePageData) bundle.getSerializable("BeCradPraticePageData");
        position = (int) bundle.get("position");
        noticeCheckButton.NoticeCheck(false, position, mPlayNum);
        initialize();
        music_oss_url = beCradPratice.getMusic_oss_url();

        /*播放卡片音频*/
        playMedia();
        /*实例化卡片View*/
        cardView = new CardView(getActivity(), getActivity(), beCradPratice);

        /*卡片的View*/
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((BaseUtil.getWidthPixels(getActivity()) - 200) * 2) / 3);

        lp.leftMargin = 100;
        lp.rightMargin = 100;
        cardView.setLayoutParams(lp);
        cardView.setBackgroundResource(R.drawable.select_readingbook_bg_red);
        cardroot.addView(cardView);


    }


    @Override
    protected void stopLoad() {
        cardroot.removeAllViews();
        cardView.linShort.removeAllViews();
        cardView.removeAllViews();
        openAnimation.cancel();
        rotateAnimation.cancel();
        rotateAnimation.reset();
        openAnimation.reset();
        mediaPlayer.stop();
        mediaPlayer = null;
        cardView = null;
        //希望做一次垃圾回收
        System.gc();
        Logger.d(" CardPraticeFragment ----------------stopLoad:");
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        Logger.d(" CardPraticeFragment ----------------onDetach:");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Logger.d(" CardPraticeFragment ----------------onStop:");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Logger.d(" CardPraticeFragment ----------------onPause:");
    }


    /*初始化*/
    private void initialize() {
        imgpaly = findViewById(R.id.img_paly);
        cardroot = findViewById(R.id.card_root);
        imgpaly.setOnClickListener(onClick);
        /*设置动画*/
        settingRing();

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playMedia();
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

                mHandler.sendEmptyMessage(2);


            }
        });
    }

    /*播放音频*/
    private void playMedia() {
               /*文件名以MD5加密*/
        String mp3Name = MD5.getMD5(music_oss_url.substring(music_oss_url.lastIndexOf("/"))) + ".mp3";

        if (FileUtil.fileIsExists(KidConfig.getInstance().getPathCardPratice() + mp3Name)) {
            /*读取本地*/
            StartMp3(KidConfig.getInstance().getPathCardPratice() + mp3Name);

        } else {
             /*读取网络*/
            StartMp3(music_oss_url);
        }

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
              /*准备播放*/
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (animationDrawable != null && !animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                    mediaPlayer.start();
                }


            });
            /*播放完成*/
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (animationDrawable != null && animationDrawable.isRunning()) {
                        animationDrawable.stop();

                    }
                    mHandler.sendEmptyMessage(1);
                    /*释放资源*/
                    mediaPlayer.stop();
                    mPlayNum++;

                    if (mPlayNum == 2) {
                        noticeCheckButton.NoticeCheck(true, -1, mPlayNum);

                    }


                }
            });
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

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /*设置动画*/
    @SuppressLint("ResourceType")
    private void settingRing() {
        // 通过逐帧动画的资源文件获得AnimationDrawable示例
        animationDrawable = (AnimationDrawable) getResources().getDrawable(
                R.drawable.ring);
        imgpaly.setBackground(animationDrawable);

    }

    /*通知活动按钮*/
    public interface NoticeCheckButton {
        public void NoticeCheck(boolean ischeck, int position, int mPlayNum);
    }


}

