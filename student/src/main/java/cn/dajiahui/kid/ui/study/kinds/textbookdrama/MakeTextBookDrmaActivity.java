package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.ui.FxActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDrama;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaoptions;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.NoScrollViewPager;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.RecorderUtil;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/*
*
* 制作课本剧
* 添加录音
* 把混音只做到无声视频 合成视频
*
* */
public class MakeTextBookDrmaActivity extends FxActivity implements ViewPager.OnPageChangeListener {

    private BeTextBookDrama bookDrama;
    private JCVideoPlayerTextBook mVideoplayer;
    private NoScrollViewPager mViewpager;
    private ImageView imgrecording;
    private ImageView imgplayrecoding;
    private List<BeTextBookDramaoptions> mDataList;
    private Timer timer;//关闭视频计时
    private Timer timerRecoding;//录音的计时
    private Timer showSubmitTimer;//显示提交按钮
    private int mCurrentPosition = 0;
    private RecorderUtil recorderUtil;//录音工具类

    private int mRecordLength = 0;
    private StringBuilder mFormatBuilder = new StringBuilder();
    private Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    private Map<Integer, Map<String, Object>> audiosList = new HashMap<>();//录音的list
    private Map<Integer, String> mPlayRecordMap = new HashMap<>();//有录音情况下 翻页时播放录音的集合
    private AudioManager audioManager;
    private TextView submit;//提交按钮

    private BeGoTextBookSuccess beGoTextBookSuccess;       /*向合成功后进入的activity带的数据*/

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (mVideoplayer.getMediaPlayer() != null) {
                        int currentPosition = mVideoplayer.getCurrentPosition();
                       /*实时在endtime区间内 停止音频播放*/
                        if (((msg.arg1 - 500) < (currentPosition)) && ((currentPosition) < (msg.arg1 + 500))) {
                            mVideoplayer.getMediaPlayer().pause();
//                            mVideoplayer.onStatePause();
                        }
                    }
                    break;

                case 1:
                    if (mRecordLength < -1) {
                        /*录音结束 添加文件到map集合*/
                        String RecordPath = recorderUtil.stopRecording();
                        Logger.d("录音地址：" + RecordPath);
                        Map<String, Object> mRecordMap = new HashMap();
                        File video = new File(RecordPath);
                        mRecordMap.put("filePathName", video);
                        mRecordMap.put("startTime", stringForTime(mDataList.get(mCurrentPosition).getStart_time()));
                        audiosList.put((mCurrentPosition + 1), mRecordMap);

                        mPlayRecordMap.put(mCurrentPosition, RecordPath);
                        openSound();
                        timerRecoding.cancel();
                        timerRecoding = null;
                        mViewpager.setNoScroll(false);//打开滑动
                        recorderUtil.cleanFileName();//清空录音地址
                        Toast.makeText(context, "停止录音", Toast.LENGTH_SHORT).show();
                    }

                    Logger.d("錄音時間：" + mRecordLength);
                    break;

                case 2:
                   /*判断是否已经全部配音*/
                    if (audiosList.size() == mDataList.size()) {
                        submit.setVisibility(View.VISIBLE);
                        showSubmitTimer.cancel();
                        showSubmitTimer = null;
                    }
                    break;
                case 3:
                    /*制作视频完毕后关闭进度条*/
                    Logger.d("关闭进度条");
                    dismissfxDialog();
                    Bundle bundle = new Bundle();


                    bundle.putSerializable("BeGoTextBookSuccess", beGoTextBookSuccess);
                    DjhJumpUtil.getInstance().startBaseActivityForResult(context, TextBookSuccessActivity.class, bundle, 0);

                    Toast.makeText(context, "合成成功！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_make_text_book_drma);
        initialize();

        recorderUtil = new RecorderUtil();



        /*模拟数据*/
        mDataList = new ArrayList<>();
        BeTextBookDramaoptions b1 = new BeTextBookDramaoptions(4000, 5000, "片段1", "pianduan1");
        BeTextBookDramaoptions b2 = new BeTextBookDramaoptions(10532, 11532, "片段2", "pianduan2");
        BeTextBookDramaoptions b3 = new BeTextBookDramaoptions(14532, 15532, "片段3", "pianduan3");

        mDataList.add(b1);
        mDataList.add(b2);
        mDataList.add(b3);

        bookDrama = (BeTextBookDrama) this.getIntent().getSerializableExtra("BeTextBookDrama");


        mVideoplayer.setUp(bookDrama.getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        mVideoplayer.startVideo();
        /*跳到指定播放时间*/
        mVideoplayer.onStatePreparingChangingUrl(0, mDataList.get(0).getStart_time());
        mVideoplayer.hideView();//隐藏不需要的view


        TextBookDramCardAdapter textBookDramCardAdapter = new TextBookDramCardAdapter(getSupportFragmentManager());

        mViewpager.setAdapter(textBookDramCardAdapter);
        mViewpager.setNoScroll(false);
        mViewpager.setOnPageChangeListener(this);


        startVideoPauseTimer();

        startShowSubmitTimer();


    }

    /*显示提交按钮的timer*/
    private void startShowSubmitTimer() {
  /*显示提交按钮的计时器*/
        if (showSubmitTimer == null) {
            showSubmitTimer = new Timer();

            showSubmitTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 2;
                    mHandler.sendMessage(msg); // 发送消息

                }
            }, 1000, 1000);

        }
    }

    /**/
    private void startVideoPauseTimer() {
        //参数2：延迟0毫秒发送，参数3：每隔1000毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (mVideoplayer.getMediaPlayer() != null) {
                        if (mVideoplayer.getMediaPlayer().isPlaying()) {
                            Message msg = Message.obtain();

                            msg.arg1 = mDataList.get(mCurrentPosition).getEnd_time();
                            msg.what = 0;
                            mHandler.sendMessage(msg); // 发送消息
                        }
                    }
                }
            }, 1000, 1000);

        }

    }


    private void initialize() {
        mVideoplayer = getView(R.id.videoplayer);
        mViewpager = getView(R.id.viewpager);
        imgrecording = getView(R.id.img_recording);
        imgplayrecoding = getView(R.id.img_playrecoding);
        submit = getView(R.id.submit);

        imgrecording.setOnClickListener(onClick);
        imgplayrecoding.setOnClickListener(onClick);
        submit.setOnClickListener(onClick);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }


    private Map<Integer, TextBookDramaCardFragment> mTextBookDramaCardMap = new HashMap<>();

    /*课本剧适配器*/
    class TextBookDramCardAdapter extends FragmentStatePagerAdapter {


        public TextBookDramCardAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }


        @Override
        public Fragment getItem(int position) {

            TextBookDramaCardFragment dramaFragment = new TextBookDramaCardFragment();
            mTextBookDramaCardMap.put(position, dramaFragment);
            Bundle bundle = new Bundle();
            bundle.putString("size", mDataList.size() + "");
            bundle.putSerializable("BeTextBookDramaoptions", mDataList.get(position));
            dramaFragment.setArguments(bundle);
            return dramaFragment;

        }


        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
            mTextBookDramaCardMap.remove(position);

            //希望做一次垃圾回收
            System.gc();
        }
    }

    /*刷新控件*/
    public interface RefreshWidget {

        public void refresgWidget(int position);

    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Logger.d("MakeTextBookDrmaActivity onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dismissfxDialog();
        Logger.d("MakeTextBookDrmaActivity onRestart()");
        /*activity重新显示时要从新加载数据*/
        mVideoplayer.setUp(bookDrama.getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
        mVideoplayer.startVideo();
        /*跳到指定播放时间*/
        mVideoplayer.onStatePreparingChangingUrl(0, mDataList.get(mCurrentPosition).getStart_time());
        startVideoPauseTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Logger.d("MakeTextBookDrmaActivity onStop");
    }

    @Override
    protected void onDestroy() {
        JCVideoPlayer.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        mCurrentPosition = position;
        /*打开视频声音*/
        openVideoviewSound();
        int start_time = mDataList.get(position).getStart_time();
        int end_time = mDataList.get(position).getEnd_time();
        mVideoplayer.videoSeekTo(start_time);
        mVideoplayer.getMediaPlayer().start();
        /*打开系统声音*/
        openSound();
         /*关闭正在播放的录音片段*/
        closeSoundRecording();

        RefreshWidget refreshWidget = (RefreshWidget) mTextBookDramaCardMap.get(position);
        refreshWidget.refresgWidget(position + 1);

        Logger.d("position:----" + position + "    start_time--:" + start_time + "    end_time--:" + end_time);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.img_recording:
                    if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                        Toast.makeText(context, "播放录音时不能配音", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    closeSound();//关闭系统声音
                    mViewpager.setNoScroll(true);//禁止滑动（录音时）
                    recorderUtil.startRecording(mDataList.get(mCurrentPosition).getEnglish());
                    mRecordLength = ((mDataList.get(mCurrentPosition).getEnd_time() - mDataList.get(mCurrentPosition).getStart_time()) / 1000);
                    if (timerRecoding == null) {
                        timerRecoding = new Timer();
                        timerRecoding.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                mRecordLength -= 1;
                                msg.arg1 = mRecordLength;
                                msg.what = 1;
                                mHandler.sendMessage(msg); // 发送消息
                            }
                        }, 1000, 1000);
                    }

                    Toast.makeText(context, "开始录音", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.img_playrecoding:

                    if (mPlayRecordMap.get(mCurrentPosition) != null) {
                        mVideoplayer.videoSeekTo(mDataList.get(mCurrentPosition).getStart_time());
                        mVideoplayer.getMediaPlayer().start();
                        Logger.d("录音地址：" + mPlayRecordMap.get(mCurrentPosition));
                               /*关闭视频的声音*/
                        closeVideoviewSound();
                        PlayMedia.getPlaying().StartMp3(mPlayRecordMap.get(mCurrentPosition));

                        startVideoPauseTimer();
                    }
                    break;
                case R.id.submit:
                    showfxDialog("视频合成中");
//                    judgeFile();
                    cleanEnvironment();
//                    /*分离视频 保存无声视频*/
                    new FfmpegUtil(MakeTextBookDrmaActivity.this, mHandler).getNoSoundVideo(bookDrama.getVideo_url());

                    /*混音的背景音*/
                    Map<String, Object> mRecordMap = new HashMap();
                    File video = new File(KidConfig.getInstance().getPathBackgroundAudio() + "backgroundAudio.mp3");
                    mRecordMap.put("filePathName", video);
                    mRecordMap.put("startTime", "0");
                    audiosList.put(0, mRecordMap);
                    /*准备传入的数据*/
                    beGoTextBookSuccess = new BeGoTextBookSuccess(KidConfig.getInstance().getPathMineWorksTemp() + "listen_and_Say.mp4", "listen and  Say", "", "LAMAR", DateUtils.formatDate(new Date(), "M月d日 HH:mm"), "90");

                    new FfmpegUtil(MakeTextBookDrmaActivity.this, mHandler).mixAudiosToVideo(new File(KidConfig.getInstance().getPathNoSoundVideo() + "out_nosound_video.mp4"), audiosList,
                            new File(beGoTextBookSuccess.getMineWorksTempPath()));//作品名称

//                    Bundle bundle = new Bundle();
//                    BeGoTextBookSuccess beGoTextBookSuccess = new BeGoTextBookSuccess(KidConfig.getInstance().getPathMineWorks() + "minework.mp4", "listen and  Say", "", "LAMAR", DateUtils.formatDate(new Date(), "M月d日 HH:mm"), "90");
//                    bundle.putSerializable("BeGoTextBookSuccess", beGoTextBookSuccess);
//                    DjhJumpUtil.getInstance().startBaseActivityForResult(context, TextBookSuccessActivity.class, bundle, 0);

                    break;
                default:
                    break;
            }


        }
    };

    /*重新录制*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0 && resultCode == 1) {
            Logger.d("重新录制");
            submit.setVisibility(View.INVISIBLE);
            /*重新录制*/
            audiosList.clear();//清空装录音文件的集合
            mPlayRecordMap.clear();//清空翻页时播放录音文件的集合
            startShowSubmitTimer();
        }


    }


    /*清空环境*/
    private void cleanEnvironment() {
        FileUtil fileUtil = new FileUtil();
        fileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMixAudios()));
        fileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMineWorksTemp()));
        fileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathNoSoundVideo()));


    }

    public void judgeFile() {
        File video = new File(KidConfig.getInstance().getPathTextbookPlay() + "out_nosound_video.mp4");
        if (!video.exists()) {
            Logger.d("video001a null");
        }

        File audio001 = new File(KidConfig.getInstance().getPathRecordingAudio() + mDataList.get(0).getEnglish() + ".mp3");
        if (!audio001.exists()) {
            Logger.d("audio001 null");
        }

        File audio002 = new File(KidConfig.getInstance().getPathRecordingAudio() + mDataList.get(1).getEnglish() + ".mp3");
        if (!audio002.exists()) {
            Logger.d("audio002 null");
        }
        File audio003 = new File(KidConfig.getInstance().getPathRecordingAudio() + mDataList.get(2).getEnglish() + ".mp3");
        if (!audio003.exists()) {
            Logger.d("audio003 null");
        }
    }

    /*格式化时间*/
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
//        int haomiao = totalSeconds % 60 % 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
//        if (hours > 0) {//:%02d
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
//        } else {
//            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//        }
    }

    public void closeSound() {
        /*判断视频是否是播放状态 yes 关闭声音*/
        if (mVideoplayer.getMediaPlayer().isPlaying()) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);//关闭系统声音
        }
    }

    public void openSound() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);//打开系统声音
    }

    /*关闭播放的音频*/
    public void closeSoundRecording() {
        if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
            PlayMedia.getPlaying().mediaPlayer.stop();
        }
    }

    /*关闭视频的声音*/
    public void closeVideoviewSound() {
        mVideoplayer.getMediaPlayer().setVolume(0, 0);
    }

    /*打开视频的声音*/
    public void openVideoviewSound() {
        mVideoplayer.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
        mVideoplayer.getMediaPlayer().setVolume(1, 1);

    }
}
