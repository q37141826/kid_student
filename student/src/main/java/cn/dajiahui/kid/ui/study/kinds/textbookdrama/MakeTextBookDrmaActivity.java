package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.chivox.core.CoreType;
import com.chivox.core.Engine;
import com.chivox.core.OnCreateProcessListener;
import com.chivox.core.OnLaunchProcessListener;
import com.chivox.cube.output.JsonResult;
import com.chivox.cube.output.RecordFile;
import com.chivox.cube.param.CoreCreateParam;
import com.chivox.cube.param.CoreLaunchParam;
import com.chivox.cube.pattern.Rank;
import com.chivox.cube.util.constant.ErrorCode;
import com.fxtx.framework.chivox.ChivoxBasicActivity;
import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.text.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.study.bean.BeChivoxEvaluateResult;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageData;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageDataItem;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.NoScrollViewPager;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/*
*
* 制作课本剧
* 添加录音
* 把混音只做到无声视频 合成视频
*
* */
public class MakeTextBookDrmaActivity extends ChivoxBasicActivity implements ViewPager.OnPageChangeListener {

    private BeTextBookDramaPageData bookDrama;
    private VideoView mVideoView;
    private NoScrollViewPager mViewpager;
    private ImageView imgrecording;
    private ImageView imgplayrecoding;
    private List<BeTextBookDramaPageDataItem> mDataList;
    private Timer timer;//关闭视频计时
    private Timer timerRecoding;//录音的计时
    private Timer showSubmitTimer;//显示提交按钮
    private int mCurrentPosition = 0;//当前碎片的索引
    private int mRecordLength = 0;//音频片段长度
    private StringBuilder mFormatBuilder = new StringBuilder();
    private Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    private Map<Integer, Map<String, Object>> audiosList = new HashMap<>();//背景音+录音的list
    private Map<Integer, String> mPlayRecordMap = new HashMap<>();//有录音情况下 翻页时播放录音的集合
    private TextView submit;//提交按钮
    private BeGoTextBookSuccess beGoTextBookSuccess; /*向合成功后进入的activity带的数据*/
    private Map<Integer, TextBookDramaCardFragment> mTextBookDramaCardMap = new HashMap<>();//保存当前Fragment的map
    private BeChivoxEvaluateResult chivoxEvaluateResult;//解析弛声打分
    private Map<Integer, Integer> mScoreMap = new HashMap<>();//当前碎片弛声打分的集合

    private final int DELAYED_CHIVOX = 4;//弛声延迟
    protected int counter = 0;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int currentPosition = mVideoView.getCurrentPosition();
                       /*实时在endtime区间内 停止音频播放*/
                    if (((msg.arg1 - 100) < (currentPosition)) && ((currentPosition) < (msg.arg1 + 500))) {
                        mVideoView.pause();
                    }

                    break;
                case 1:
                    if (mRecordLength < -1) {
                        /* 结束驰声录音 */
                        recordStop();
//                        Toast.makeText(context, "结束录音", Toast.LENGTH_SHORT).show();
                    }
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
                    dismissfxDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("MakeFlag", "MakeTextBookDrma");
                    bundle.putString("ShowBottom", "SHOW");
                    beGoTextBookSuccess.setmScoreMap(mScoreMap);
                    bundle.putSerializable("BeGoTextBookSuccess", beGoTextBookSuccess);
                    DjhJumpUtil.getInstance().startBaseActivityForResult(context, TextBookSuccessActivity.class, bundle, DjhJumpUtil.getInstance().activity_makebookdrame);
                    break;

                case DELAYED_CHIVOX:
                    Logger.d("-------counter:" + counter);
                    if (counter < 3) {
                        recordStop();
                        recordStart(mDataList.get(mCurrentPosition).getEnglish()); // 重新开始录音
                    } else {
                        // 关闭进度条
                        // toast 提示用户系统繁忙
                        counter = 0;
                        dismissfxDialog();
                        /*修改录音按钮的背景*/
//                        mRecording.setImageResource(R.drawable.card_record_off);

                        Toast.makeText(context, "系统繁忙，请稍后...", Toast.LENGTH_SHORT).show();
                        isRecording = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private String mVideoName;//要播放视频的内容

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.wordsentpred_layout);
        super.onCreate(savedInstanceState);
        initAIEngine();
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_make_text_book_drma);
        initialize();
        bookDrama = (BeTextBookDramaPageData) this.getIntent().getSerializableExtra("BeTextBookDramaPageData");
        mDataList = bookDrama.getItem();
         /*文件名以MD5加密*/
        mVideoName = MD5.getMD5(bookDrama.getPage_url().substring(bookDrama.getPage_url().lastIndexOf("/"))) + ".mp4";
        playVideo(KidConfig.getInstance().getPathTextbookPlayMp4() + mVideoName);

        TextBookDramCardAdapter textBookDramCardAdapter = new TextBookDramCardAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(textBookDramCardAdapter);
        mViewpager.setNoScroll(false);
        mViewpager.setOnPageChangeListener(this);

        /*暂停视频播放计时*/
        startVideoPauseTimer();
        /*显示提交按钮*/
        startShowSubmitTimer();


    }

    private MediaPlayer mCurrentMp;

    private void playVideo(String path) {
        /*设置本地路径*/
        mVideoView.setVideoPath(path);
        //播放完成回调
        mVideoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (StringUtil.isNumericzidai(mDataList.get(mCurrentPosition).getTime_end())) {
                    mp.seekTo(Integer.parseInt(mDataList.get(0).getTime_start()));
                    mp.start();
                } else {
                    Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mCurrentMp = mp;
                return false;
            }
        });
        //开始播放视频
    }

    /*视频完成监听*/
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Logger.d("播放完成了");

        }
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
            }, 0, 300);

        }
    }

    private void startVideoPauseTimer() {
        //参数2：延迟0毫秒发送，参数3：每隔1000毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (StringUtil.isNumericzidai(mDataList.get(mCurrentPosition).getTime_end())) {
                        Message msg = Message.obtain();
                        msg.arg1 = Integer.parseInt(mDataList.get(mCurrentPosition).getTime_end());
                        msg.what = 0;
                        mHandler.sendMessage(msg); // 发送消息

                    }
                }
            }, 0, 300);

        }

    }


    private void initialize() {
        mVideoView = getView(R.id.makeVideoView);
        mViewpager = getView(R.id.viewpager);
        imgrecording = getView(R.id.img_recording);
        imgplayrecoding = getView(R.id.img_playrecoding);
        submit = getView(R.id.submit);
        imgrecording.setOnClickListener(onClick);
        imgplayrecoding.setOnClickListener(onClick);
        submit.setOnClickListener(onClick);
        mVideoView.setMediaController(new MediaController(this));

    }


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

            bundle.putSerializable("BeTextBookDramaPageDataItem", mDataList.get(position));
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dismissfxDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

        if (StringUtil.isNumericzidai(mDataList.get(position).getTime_start())) {
            int start_time = Integer.parseInt(mDataList.get(position).getTime_start());

            int end_time = Integer.parseInt(mDataList.get(position).getTime_end());
            //      Logger.d("position:----" + position + "  videoSeekTo  start_time--:" + start_time + "    end_time--:" + end_time);
            mVideoView.seekTo(start_time);
            mVideoView.start();
        }
        /*关闭正在播放的录音片段*/
        closeSoundRecording();
         /*打开视频声音*/
        openVideoviewSound();


        RefreshWidget refreshWidget = (RefreshWidget) mTextBookDramaCardMap.get(position);
        refreshWidget.refresgWidget(position + 1);


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

                    if (StringUtil.isNumericzidai(mDataList.get(mCurrentPosition).getTime_start()) && StringUtil.isNumericzidai(mDataList.get(mCurrentPosition).getTime_end())) {
                    /*点击录音同时视频要跳转到当前录音片段开始的时间点*/
                        mVideoView.seekTo(Integer.parseInt(mDataList.get(mCurrentPosition).getTime_start()));
                    /*关闭视频声音*/
                        closeVideoviewSound();
                    /*禁止滑动（录音时）*/
                        mViewpager.setNoScroll(true);

                    /* 通知评分引擎此次为英文句子评测 */
                        coretype = CoreType.en_sent_score;
                    /*参数是评分的语句*/
                        recordStart(mDataList.get(mCurrentPosition).getEnglish());
                        isRecording = true;
                    /*播放视频*/
                        mVideoView.start();
                    /*获取当前片段录音的长度*/
                        mRecordLength = ((Integer.parseInt(mDataList.get(mCurrentPosition).getTime_end()) - Integer.parseInt(mDataList.get(mCurrentPosition).getTime_start())) / 1000);
                   /*通知碎片中的进度条*/
                        mTextBookDramaCardMap.get(mCurrentPosition).refreshProgress(mRecordLength);
                    /*监听时间录音倒计时*/
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
                            }, 0, 1000);
                        }
                    }
//                    Toast.makeText(context, "开始录音", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.img_playrecoding:

                    if (mPlayRecordMap.get(mCurrentPosition) != null) {
                        /*点击录音要跳转到当前片段的开始时间*/
                        mVideoView.seekTo(Integer.parseInt(mDataList.get(mCurrentPosition).getTime_start()));
                        /*关闭视频的声音*/
                        closeVideoviewSound();
                        PlayMedia.getPlaying().StartMp3(mPlayRecordMap.get(mCurrentPosition));

                        startVideoPauseTimer();
                    }
                    break;
                case R.id.submit:
                    showfxDialog("视频合成中");
                    cleanEnvironment();
                    /*混音的背景音*/
                    Map<String, Object> mRecordMap = new HashMap();
                      /*文件名以MD5加密*/
                    String mp3Name = MD5.getMD5(bookDrama.getMusic_oss_name().substring(bookDrama.getMusic_oss_name().lastIndexOf("/"))) + ".mp3";

                    File video = new File(KidConfig.getInstance().getPathTextbookPlayBackgroundAudio() + mp3Name);
                    mRecordMap.put("filePathName", video);
                    mRecordMap.put("startTime", "0");
                    audiosList.put(0, mRecordMap);
                    /*准备传入的数据*/
                    beGoTextBookSuccess = new BeGoTextBookSuccess(KidConfig.getInstance().getPathMineWorksTemp() + "TextBook" + bookDrama.getPage_id() + ".mp4",
                            bookDrama.getPage_id(), bookDrama.getTitle() + ".mp4",
                            UserController.getInstance().getUser().getAvatar(),
                            UserController.getInstance().getUser().getNickname(),
                            (System.currentTimeMillis()));

                    /*1.原音视频 2. 混音音频 3. 输出合成视频*/
                    new FfmpegUtil(MakeTextBookDrmaActivity.this, mHandler, bookDrama.getPage_id()).mixAudiosToVideo(
                            new File(KidConfig.getInstance().getPathTextbookPlayMp4() + mVideoName), audiosList,
                            new File(beGoTextBookSuccess.getMineWorksTempPath()));//作品名称
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
        if (requestCode == DjhJumpUtil.getInstance().activity_makebookdrame && resultCode == 1) {
            Logger.d("重新录制");
            submit.setVisibility(View.INVISIBLE);
            /*重新录制*/
            audiosList.clear();//清空装录音文件的集合
            mPlayRecordMap.clear();//清空翻页时播放录音文件的集合
            startShowSubmitTimer();
        }
        /*制作成功后按返回键 与 左上角退出视频*/
        if (requestCode == DjhJumpUtil.getInstance().activity_makebookdrame && resultCode == DjhJumpUtil.getInstance().activity_makebookdrame_out) {
            finishActivity();
        }

    }

    /*清空环境*/
    private void cleanEnvironment() {
        /*混音文件夹*/
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMixAudios()));
        /*我的作品临时文件夹*/
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMineWorksTemp()));
        /*无声视频文件夹*/
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathTextbookPlayNoSoundVideo()));

    }


    /*格式化时间*/
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        Logger.d("格式化时间：  " + mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString());
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    /*关闭播放的音频*/
    public void closeSoundRecording() {
        if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
            PlayMedia.getPlaying().mediaPlayer.stop();
        }
    }

    /*关闭视频的声音*/
    public void closeVideoviewSound() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mCurrentMp.setVolume(0, 0);/*关闭视频声音*/
        }
    }

    /*打开视频的声音*/
    public void openVideoviewSound() {
//        mCurrentMp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (mCurrentMp != null) {
            mCurrentMp.setVolume(1, 1);
        }
    }

    /**
     * 驰声评分引擎初始化
     *
     * @param coreCreateParam
     */
    @Override
    protected void initCore(CoreCreateParam coreCreateParam) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        service.initCore(this, coreCreateParam, new OnCreateProcessListener() {

            @Override
            public void onError(int arg0, ErrorCode.ErrorMsg errMSG) {
//                Log.d("log", "engine error " + errMSG.getReason());
            }

            @Override
            public void onCompletion(int arg0, Engine aiengine) {
                engine = aiengine;
//                Log.d("wlog", "Engine created :" + engine);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "引擎初始化成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    /**
     * 驰声测评开始记录
     */
    @Override
    protected void record() {

    }

    /**
     * 驰声开始录音
     */
    public void recordStart(String contrastSentence) {

//        final String refText = "Hello, my baby."; //tvReftext.getText().toString(); // 取得要读的内容
        //boolean isVadInUsed = isVadLoad;
//        Logger.d("isVadLoaded: " + isVadLoad);
        //传入coreType
        CoreLaunchParam coreLaunchParam = new CoreLaunchParam(isOnline, coretype, contrastSentence, isVadLoad);
        //不传coreType
        //CoreLaunchParam coreLaunchParam = new CoreLaunchParam(isOnline,null,refText,isVadLoad);
        coreLaunchParam.getRequest().setRank(Rank.rank100); // 设置为百分制
        coreLaunchParam.setVadEnable(false);
        coreLaunchParam.setSoundIntensityEnable(true); // 让服务器返回SoundIntensity

        //        try {
//            Log.d("log","coreLaunchParam: "+coreLaunchParam.getCoreLaunchParams());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        //段落评分的评分精度，不设置默认为1，可以设置为0.5
        /*if(coretype==CoreType.en_pred_score) {
            coreLaunchParam.setPrecision(predPrecision);
        }*/
        long duration = 10000;//设置10秒录音时间
        service.recordStart(this, engine, duration, coreLaunchParam,
                new OnLaunchProcessListener() {

                    @Override
                    public void onError(int arg0, final ErrorCode.ErrorMsg arg1) {
//                        Logger.d("ErrorId : " + arg1.getErrorId() + "Reason : " + arg1.getReason());
//                        Logger.d("Desc : " + arg1.getDescription() + "Suggest : " + arg1.getSuggest());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ToastUtil.showToast(MakeTextBookDrmaActivity.this, "录音失败: ErrodCode:" + arg1.getErrorId() + "Desc :" + arg1.getDescription() + "\r\n" + "建议: " + arg1.getSuggest());
                            }
                        });
                    }

                    @Override
                    public void onAfterLaunch(final int resultCode,
                                              final JsonResult jsonResult, RecordFile recordfile) {
                        lastRecordFile = recordfile;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                switch (resultCode) {
//                                    case 3:
//                                        try {
//                                            JSONObject var1 = new JSONObject(jsonResult.getJsonText());
//                                            if (var1.has("sound_intensity")) {
//                                                mHandler.removeMessages(DELAYED_CHIVOX);
//                                                counter = 0;
//                                                dismissfxDialog();
//
//                                            }
//
//                                        } catch (Exception var2) {
//                                            var2.printStackTrace();
//                                        }
//                                        break;

                                    case 5:
                                        try {
                                            JSONObject var1 = new JSONObject(jsonResult.getJsonText());
                                            if (!var1.has("result")) {
                                                 /*修改录音按钮的背景*/
//                                                mRecording.setImageResource(R.drawable.card_record_off);
                                                /*隐藏打分*/
//                                                mScore.setVisibility(View.INVISIBLE);
                                                isRecording = false;

                                                if (var1.has("errId")) {
                                                    if (var1.getInt("errId") == 41030) {
                                                        Toast.makeText(context, "系统繁忙，请稍后...", Toast.LENGTH_SHORT).show();
                                                        break;
                                                    }
                                                    Toast.makeText(context, "系统繁忙，请稍后...", Toast.LENGTH_SHORT).show(); // 暂定此文案，以后再根据情况再修改
                                                }
                                                break;
                                            }
                                        } catch (Exception var2) {
                                            var2.printStackTrace();
                                        }

                                        File recordFile = lastRecordFile.getRecordFile();
                                        Logger.d("停止录音：" + recordFile.getAbsolutePath());

                                        Map<String, Object> mRecordMap = new HashMap();
                                        File video = lastRecordFile.getRecordFile();
                                        mRecordMap.put("filePathName", video);
                                        mRecordMap.put("startTime", stringForTime(Integer.parseInt(mDataList.get(mCurrentPosition).getTime_start())));
                                        audiosList.put((mCurrentPosition + 1), mRecordMap);
                                        mPlayRecordMap.put(mCurrentPosition, video.getAbsolutePath());
                                        timerRecoding.cancel();
                                        timerRecoding = null;
                                        mViewpager.setNoScroll(false);//打开滑动

                                           /*打开视频声音*/
                                        openVideoviewSound();
                                        if (jsonResult != null)
                                            parseChivoxJsonResult(jsonResult);
                                            /*获取评分分数*/
                                        String overall = chivoxEvaluateResult.getOverall();
                                        Logger.d("打分-----" + overall);
                                          /*通知碎片中的小星星*/
                                        mTextBookDramaCardMap.get(mCurrentPosition).markScore(Integer.parseInt(overall));
                                        mScoreMap.put(mCurrentPosition, Integer.parseInt(overall));

                                        break;
                                }


                            }
                        });

                    }

                    @Override
                    public void onBeforeLaunch(long duration) {
                        Logger.d("duration: " + duration);
                    }

                    @Override
                    public void onRealTimeVolume(final double volume) {
                    }
                });
    }

    /**
     * ֹͣ结束驰声录音
     */

    @Override
    protected void recordStop() {

//        Toast.makeText(context, "结束评分", Toast.LENGTH_SHORT).show();
        if (engine.isRunning()) {
            service.recordStop(engine);
        }

        isRecording = false;
    }

    /**
     * 解析评测结果
     */
    protected void parseChivoxJsonResult(JsonResult jsonResult) {
        JSONObject object = null;
        try {
            object = new JSONObject(jsonResult.toString());
            GsonUtil gson = new GsonUtil();
            if (object.has("result")) // 在此处做判断，包含字段的话再继续从JSON中获取code字段的内容
            {
                chivoxEvaluateResult = gson.getJsonObject(object.optJSONObject("result").toString(), BeChivoxEvaluateResult.class);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
