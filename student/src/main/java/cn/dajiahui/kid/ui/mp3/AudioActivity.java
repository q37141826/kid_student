package cn.dajiahui.kid.ui.mp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fxtx.framework.lamemp3.AudioListener;
import com.fxtx.framework.lamemp3.Mp3Audio;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;

import java.util.Timer;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.video.bean.BeVideo;
import fm.jiecao.jcvideoplayer_lib.JCUtils;


/**
 * Created by Administrator on 2017/8/28.
 */

public class AudioActivity extends FxActivity {
    private TextView tvTitle;
    private TextView tvSize;
    private TextView tvTime;
    private BeVideo beVideo;

    private ImageView imMp3;
    private TextView tvStartTime, tvEndTime;
    private SeekBar seekBar;
    private Mp3Audio mp3Audio;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_audio);
        tvTitle = getView(R.id.tvTitle);
        tvSize = getView(R.id.tvSize);
        tvTime = getView(R.id.tvTime);
        imMp3 = getView(R.id.im_mp3);
        tvStartTime = getView(R.id.tv_start_time);
        tvEndTime = getView(R.id.tv_end_time);
        seekBar = getView(R.id.seekBar);
        beVideo = (BeVideo) getIntent().getSerializableExtra(Constant.bundle_obj);
        imMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp3Audio.isPlayer()) {
                    mp3Audio.paushAudio();
                } else {
                    if (isStop) {
                        mp3Audio.getmPlayer().seekTo(0);
                        isStop = false;
                    }
                    mp3Audio.startAudio();
                }
            }
        });
        mp3Audio = new Mp3Audio(context, new AudioListener() {
            @Override
            public void onRecorderStop() {
            }

            @Override
            public void onRecorderStart() {
            }

            @Override
            public void onPlayStart() {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void paushAudio() {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onPlayStop() {
                mp3Audio.timer = new Timer();//停止后 重新创建计时器
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onDuration(int duration) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = duration;
                handler.sendMessage(message);
            }

            @Override
            public void onError(String error) {
                Message message = handler.obtainMessage();
                message.what = -1;
                message.obj = error;
                handler.sendMessage(message);
            }
        });
        mp3Audio.isDuration = true;
        mp3Audio.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                //缓冲进度
                seekBar.setSecondaryProgress(i);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mp3Audio != null && mp3Audio.getmPlayer() != null)
                    mp3Audio.getmPlayer().seekTo(seekBar.getProgress());
            }
        });

        mp3Audio.setOnPrepredListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                dismissfxDialog();
                setSeekBarInfo();
            }
        });
    }

    private boolean isInfoAudio = true; //判断是否获取了进度长度
    private boolean isStop = false;//是否播放完成

    private void setSeekBarInfo() {
        if (isInfoAudio && mp3Audio != null && mp3Audio.getmPlayer() != null) {
            isInfoAudio = false;
            int max = mp3Audio.getPlayDuration();
            if (max == 0) {
                isInfoAudio = true;
            }
            seekBar.setMax(max);
            tvEndTime.setText(JCUtils.stringForTime(max));
            tvTime.setText("时长：" + tvEndTime.getText().toString());
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case -1:
                    ToastUtil.showToast(context, msg.obj.toString());
                    imMp3.setImageResource(R.drawable.icon_pause);
                    break;
                case 0:
                    setSeekBarInfo();
                    seekBar.setProgress((int) msg.obj);
                    tvStartTime.setText(JCUtils.stringForTime((int) msg.obj));
                    break;
                case 1:
                    //开始
                    imMp3.setImageResource(R.drawable.icon_play);
                    break;
                case 3:
                    isStop = true;
                    seekBar.setProgress(0);
                    tvStartTime.setText("00:00");
                case 2:
                    //停止播放
                    imMp3.setImageResource(R.drawable.icon_pause);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        beVideo = (BeVideo) getIntent().getSerializableExtra(Constant.bundle_obj);
        tvTitle.setText(beVideo.title + "");
        tvSize.setText("大小：" + beVideo.size);
        showfxDialog();
        mp3Audio.initAudio(beVideo.url);
        mp3Audio.url = beVideo.url;
    }

    private boolean isPaush = false;

    @Override
    protected void onStop() {
        if (mp3Audio.isPlayer() && !isStop) {
            mp3Audio.paushAudio();
            isPaush = true;
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isPaush) {
            if (isStop) {
                mp3Audio.getmPlayer().seekTo(0);
                isStop = false;
            }
            mp3Audio.startAudio();
            isPaush = false;
        }
    }

    @Override
    protected void onDestroy() {
        mp3Audio.stopAudio();
        super.onDestroy();
    }
}
