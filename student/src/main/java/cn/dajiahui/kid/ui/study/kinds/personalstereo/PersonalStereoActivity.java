package cn.dajiahui.kid.ui.study.kinds.personalstereo;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BePersonalStereo;
import cn.dajiahui.kid.ui.study.bean.BePersonalStereoPageData;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;


/*
 * 随身听
 * */
public class PersonalStereoActivity extends FxActivity {
    public static final int MUSIC_CURRENTTIME = 0;
    public static final int MUSIC_STOP = 1;

    private ImageView imgplay;
    private TextView mTypeName, mAudioTitle, mPrevious, mNext, mTypeNAme;
    private SeekBar pbDuration;
    private TextView tvTimeElapsed;
    private TextView tvDuration, tvcontent;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private int mOnpausePosition = 0;
    private Timer timer = null;

    private String book_id;
    private String unit_id;
    private MediaPlayer mediaPlayer;
    private AnimationDrawable animationDrawable;
    private Boolean completePlay = false;//播放完成标志 false未完成  true 已完成


    private int mCurrentNum = 0;//当前页面

    private List<BePersonalStereoPageData> mPageData;//数据源


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
        onBackText();
        Bundle mPersonalStereoBundle = getIntent().getExtras();
        setfxTtitle(mPersonalStereoBundle.getString("UNIT_NAME"));
        book_id = mPersonalStereoBundle.getString("BOOK_ID");
        unit_id = mPersonalStereoBundle.getString("UNIT_ID");
        initialize();
        httpData();
        /*设置动画*/
        settingRing();
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpPersonalStereo(PersonalStereoActivity.this, callPersonalStereo, book_id, unit_id);
    }

    /**
     * callback函数
     */
    ResultCallback callPersonalStereo = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("随身听：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BePersonalStereo bePersonalStereo = json.parsingObject(BePersonalStereo.class);
                if (bePersonalStereo != null) ;
                if (bePersonalStereo.getPage_data().size() > 0) {
                    mPageData = bePersonalStereo.getPage_data();
                    mPageData.get(mCurrentNum);
                    if (mPageData.get(mCurrentNum) != null) {
                        settingInfo();
                        if (!FileUtil.fileIsExists(AudioPath())) {
                            showfxDialog("音频下载中，请稍后...");
                            /*网络下载*/
                            downloadPersonalStereo(mPageData.get(0).getMusic_oss_name());
                            mNext.setTextColor(getResources().getColor(R.color.gray_DBDBDB));
                            mNext.setClickable(false);//下一个按钮不可点击

                        } else {
                            /*读取本地*/
                            startAudio(AudioPath());
                            mNext.setTextColor(getResources().getColor(R.color.gray_333333));
                            mNext.setClickable(true);//下一个按钮不可点击
                        }

                    }
                }
            } else {
                ToastUtil.showToast(PersonalStereoActivity.this, json.getMsg());
            }

        }

    };

    /*随身听mp3*/
    private void downloadPersonalStereo(String fileUrl) {

        BeDownFile file = new BeDownFile(Constant.file_personal_stereo, fileUrl, "", KidConfig.getInstance().getPathTemp());

        new DownloadFile(PersonalStereoActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                if (mPageData.size() > 1) {
                    /*读取本地*/
                    startAudio(AudioPath());
                    dismissfxDialog();
                    /*下载成功后下载下一个*/
                    String mp3Name = MD5.getMD5(mPageData.get(1).getMusic_oss_name().
                            substring(mPageData.get(1).getMusic_oss_name().lastIndexOf("/"))) + ".mp3";
                    if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathPersonalStereo() + mp3Name)) {
                        /*下载 下一个*/
                        downloadPersonalStereo(mPageData.get(1).getMusic_oss_name());
                    } else {
                        mNext.setTextColor(getResources().getColor(R.color.gray_333333));
                        mNext.setClickable(true);//下一个按钮可点击
                    }
                }
            }
        });
    }

    /*随身听mp3*/
    private void Preloading(String fileUrl) {

        BeDownFile file = new BeDownFile(Constant.file_personal_stereo, fileUrl, "", KidConfig.getInstance().getPathTemp());

        new DownloadFile(PersonalStereoActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                mNext.setClickable(true);
                mNext.setTextColor(getResources().getColor(R.color.gray_333333));

            }
        });
    }

    /*设置进度*/
    private void setProgress(boolean totaltime) {
        if (mediaPlayer != null) {
            /*设置当前时长*/
            int position = mediaPlayer.getCurrentPosition();
            pbDuration.setProgress(position);
            tvTimeElapsed.setText(stringForTime(position));

            /*设置总时长*/
            if (totaltime == true) {
                int duration = mediaPlayer.getDuration();
                tvDuration.setText(stringForTime(duration));
                /*设置进度条最大值*/
                pbDuration.setMax(mediaPlayer.getDuration());
            }
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_personal_stereo);
    }

    /*初始化*/
    private void initialize() {
        imgplay = getView(R.id.img_play);
        mTypeName = getView(R.id.tv_unitname);
        mAudioTitle = getView(R.id.tv_title);
        mPrevious = getView(R.id.previous);
        mNext = getView(R.id.next);
        pbDuration = getView(R.id.pbDuration);
        mTypeNAme = getView(R.id.audio_content);
        tvTimeElapsed = getView(R.id.tvTimeElapsed);
        tvDuration = getView(R.id.tvDuration);
        tvcontent = getView(R.id.tv_content);
        imgplay.setOnClickListener(onClick);
        mPrevious.setOnClickListener(onClick);
        mNext.setOnClickListener(onClick);
        pbDuration.setOnSeekBarChangeListener(seekListener);/*设置seek监听*/
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    /*格式化时间*/
    private String stringForTime(int timeMs) {
        int hours = (timeMs % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        int minutes = (timeMs % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = timeMs % (1000 * 60) / 1000;
        int haomiao = timeMs % 1000;

        return new Formatter(new StringBuilder(), Locale.getDefault()).format("%02d:%02d", minutes, seconds).toString();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {

            mediaPlayer.stop();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.pause();

        }
    }

    /*获取本地音频路径*/
    private String AudioPath() {
        String mp3Name = "";
        if (mCurrentNum < mPageData.size()) {
            /*文件名以MD5加密*/
            mp3Name = MD5.getMD5(mPageData.get(mCurrentNum).getMusic_oss_name().
                    substring(mPageData.get(mCurrentNum).getMusic_oss_name().lastIndexOf("/"))) + ".mp3";
        }
        return KidConfig.getInstance().getPathPersonalStereo() + mp3Name;
    }

    /*点击事件*/
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.img_play:
                    /*播放器处于播放状态*/
                    if ((mediaPlayer != null) && (mediaPlayer.isPlaying())) {
                        stopAnimation();
                        PauseMp3();
                        mOnpausePosition = mediaPlayer.getCurrentPosition();
                        completePlay = false;
                    } else {

                        /*判断本地是否存在*/
                        if (FileUtil.fileIsExists(AudioPath())) {

                            /*播放器处于未播放状态*/
                            if (completePlay) {/*如果是播放完成*/
                                /*读取本地*/
                                startAudio(AudioPath());
                                setProgress(true);
                                completePlay = false;

                            } else {
                                if ((mediaPlayer != null)) {
                                    mediaPlayer.seekTo(mOnpausePosition);
                                    mediaPlayer.start();
                                    startAnimation();
                                }
                            }
                        }
                    }

                    break;

                /*上一个*/
                case R.id.previous:
                    stopAudio();
                    if (mCurrentNum > 0) {
                        /*读取本地*/
                        startAudio(AudioPath());
                        mCurrentNum = mCurrentNum - 1;
                        settingInfo();
                        mNext.setTextColor(getResources().getColor(R.color.gray_333333));
                        mPrevious.setTextColor(getResources().getColor(R.color.gray_333333));
                        if (mCurrentNum == 0) {
                            mPrevious.setTextColor(getResources().getColor(R.color.gray_DBDBDB));
                            mPrevious.setClickable(false);
                        }
                    }

                    break;
                /*下一个*/
                case R.id.next:
                    stopAudio();
                    if (mCurrentNum + 1 < mPageData.size()) {
                        mCurrentNum = mCurrentNum + 1;
                        settingInfo();
                        /*读取本地*/
                        startAudio(AudioPath());
                        mNext.setTextColor(getResources().getColor(R.color.gray_333333));
                        mPrevious.setTextColor(getResources().getColor(R.color.gray_333333));

                        if (mCurrentNum + 1 == mPageData.size()) {
                            mNext.setTextColor(getResources().getColor(R.color.gray_DBDBDB));
                            mNext.setClickable(false);
                        }
                    }

                    if (mCurrentNum + 1 < mPageData.size() && mPageData.get(mCurrentNum + 1) != null) {

                        String mp3Name = MD5.getMD5(mPageData.get(mCurrentNum + 1).getMusic_oss_name().
                                substring(mPageData.get(mCurrentNum + 1).getMusic_oss_name().lastIndexOf("/"))) + ".mp3";
                        /*判断本地不存在 */
                        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathPersonalStereo() + mp3Name)) {
                            Preloading(mPageData.get(mCurrentNum + 1).getMusic_oss_name());
                            mNext.setTextColor(getResources().getColor(R.color.gray_DBDBDB));
                            mNext.setClickable(false);
                        } else {
                            mNext.setTextColor(getResources().getColor(R.color.gray_333333));
                            mNext.setClickable(true);
                        }

                    }

                    break;
                default:
                    break;
            }
        }


    };

    private void stopAudio() {
        mNext.setClickable(true);
        mPrevious.setClickable(true);
        tvTimeElapsed.setText("00:00");
        tvDuration.setText("00:00");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            /*结束动画*/
            stopAnimation();
            /*释放资源*/
            mediaPlayer.stop();
        }
    }

    private void settingInfo() {
        mAudioTitle.setText(mPageData.get(mCurrentNum).getTitle());
        tvcontent.setText(mPageData.get(mCurrentNum).getInfo());
        mTypeName.setText(mPageData.get(mCurrentNum).getType_name());

    }

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
            mediaPlayer.seekTo(pbDuration.getProgress());
            mOnpausePosition = mediaPlayer.getCurrentPosition();
            completePlay = false;
            startTimer();
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
                    if (mediaPlayer != null) {
                        if (completePlay) {
                            mHandler.sendEmptyMessage(MUSIC_STOP); // 发送消息
                            return;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mHandler.sendEmptyMessage(MUSIC_CURRENTTIME); // 发送消息
                        }
                    }
                }
            }, 0, 1000);
        }
    }

    /*设置动画*/
    @SuppressLint("ResourceType")
    private void settingRing() {
        // 通过逐帧动画的资源文件获得AnimationDrawable示例
        animationDrawable = (AnimationDrawable) getResources().getDrawable(
                R.drawable.ring);
        imgplay.setBackground(animationDrawable);

    }

    /*开始动画*/
    private void startAnimation() {
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    /*结束动画*/
    private void stopAnimation() {
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    /*开始播放*/
    private void startAudio(String mp3Url) {

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
                    /*开启动画*/
                    startAnimation();
                    startTimer();//启动计时器
                    mediaPlayer.start();
                }


            });
            /*播放完成*/
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    completePlay = true;
                    /*停止动画*/
                    stopAnimation();
                    /*释放资源*/
                    mediaPlayer.stop();

                }
            });
            /*播放失败*/
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return true;
                }
            });
            /*进度监听*/
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {


                }
            });

            setProgress(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*暂停mp3*/
    public void PauseMp3() {
        mediaPlayer.pause();
    }
}
