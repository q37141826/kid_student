package cn.dajiahui.kid.ui.study.kinds.personalstereo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;

import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.util.Logger;


/*
* 随身听
* */
public class PersonalStereoActivity extends FxActivity {
    public static final int MUSIC_CURRENTTIME = 0;
    public static final int MUSIC_STOP = 1;

    private ImageView imgplay;
    private TextView tvunitname;
    private SeekBar pbDuration;
    private TextView tvTimeElapsed;
    private TextView tvDuration, tvcontent;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private int mOnpausePosition = 0;
    private Timer timer = null;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MUSIC_CURRENTTIME) {
                setProgress(false);

            }
            if (msg.what == MUSIC_STOP) {
                timer.cancel();
                pbDuration.setProgress(0);
                    /*播放完毕要置0*/
                mOnpausePosition = 0;
                timer = null;
                tvTimeElapsed.setText("00:00");
                return;
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setfxTtitle(11111);
        onBackText();
        initialize();

        PlayMedia.getPlaying().StartMp3("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");
        setProgress(true);//设置进度
        startTimer();//启动计时器

        tvcontent.setText("这里是内容");
    }


    /*设置进度*/
    private void setProgress(boolean totaltime) {

        /*设置当前时长*/
        int position = PlayMedia.getPlaying().mediaPlayer.getCurrentPosition();
        pbDuration.setProgress(position);
        tvTimeElapsed.setText(stringForTime(position));

        /*设置总时长*/
        if (totaltime == true) {
            int duration = PlayMedia.getPlaying().mediaPlayer.getDuration();
            tvDuration.setText(stringForTime(duration));
             /*设置进度条最大值*/
            pbDuration.setMax(PlayMedia.getPlaying().mediaPlayer.getDuration());
        }


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_personal_stereo);
    }


    /*初始化*/
    private void initialize() {
        imgplay = getView(R.id.img_play);
        tvunitname = getView(R.id.tv_unitname);
        pbDuration = getView(R.id.pbDuration);
        tvTimeElapsed = getView(R.id.tvTimeElapsed);
        tvDuration = getView(R.id.tvDuration);
        tvcontent = getView(R.id.tv_content);
        imgplay.setOnClickListener(onClick);
        pbDuration.setOnSeekBarChangeListener(seekListener);/*设置seek监听*/
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /*格式化时间*/
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        //   int haomiao=totalSeconds % 60%1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {//:%02d
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (PlayMedia.getPlaying().mediaPlayer != null) {

            PlayMedia.getPlaying().mediaPlayer.stop();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (PlayMedia.getPlaying().mediaPlayer != null) {
            PlayMedia.getPlaying().mediaPlayer.pause();

        }
    }

    /*点击事件*/
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.img_play:

                    if ((PlayMedia.getPlaying().mediaPlayer != null) && (PlayMedia.getPlaying().mediaPlayer.isPlaying())) {
                        PlayMedia.getPlaying().PauseMp3();
                        mOnpausePosition = PlayMedia.getPlaying().mediaPlayer.getCurrentPosition();
                        Logger.d("" + mOnpausePosition + PlayMedia.getPlaying().mediaPlayer.getCurrentPosition());

                    } else {

                        /*如果是播放完成*/
                        if (PlayMedia.getPlaying().setOnCompletionListener(0)) {
                            PlayMedia.getPlaying().StartMp3("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");
                            setProgress(true);
                            startTimer();//启动计时器
                            PlayMedia.getPlaying().complete = false;

                        } else {

                            if ((PlayMedia.getPlaying().mediaPlayer != null)) {
                                PlayMedia.getPlaying().mediaPlayer.seekTo(mOnpausePosition);
                                PlayMedia.getPlaying().mediaPlayer.start();
                                mOnpausePosition = 0;
                            }
                        }

                    }

                    break;
                default:
                    break;
            }
        }
    };

    /*seekbar监听*/
    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 进度条拖动播放进度的声音
            PlayMedia.getPlaying().mediaPlayer.seekTo(pbDuration.getProgress());
        }
    };

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
                            return;
                        }
                        if (PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                            mHandler.sendEmptyMessage(MUSIC_CURRENTTIME); // 发送消息
                        }
                    }
                }
            }, 0, 1000);
        }
    }
}
