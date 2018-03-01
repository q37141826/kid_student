package cn.dajiahui.kid.ui.study.kinds.karaoke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.ui.study.bean.BeItem;
import cn.dajiahui.kid.ui.study.bean.BePageData;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.FfmpegUtil;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.TextBookSuccessActivity;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;
import cn.dajiahui.kid.util.RecorderUtil;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/*
* 制作卡拉ok
*
  */
public class MakeKraoOkeActivity extends FxActivity {

    private cn.dajiahui.kid.ui.study.kinds.karaoke.JCVideoPlayerMakeKaraOke mVideoplayer;
    private TextView tv_english;
    private TextView tv_chinese;
    private Button startRecord;
    private BePageData bePageData;
    Timer timer;
    private int mCurrentPosition = 0;//当前显示中英文数据源的角标
    private boolean isOKRecord = false;//完成录制标志
    private Map<Integer, Map<String, Object>> audiosList = new HashMap<>();//背景音+录音的list
    private RecorderUtil recorderUtil;//录音工具类
    private String sBackground;//Md5加密后的卡拉ok背景音的名字

    private BeGoTextBookSuccess beGoTextBookSuccess;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0 && mVideoplayer.getMediaPlayer() != null) {
                int startTime = msg.arg1;
                int currentPosition = mVideoplayer.getCurrentPosition();

               /*实时在start区间内 显示中英文*/
                if (((startTime - 600) < (currentPosition)) && ((currentPosition) < (startTime + 600))) {
                    mCurrentPosition++;
                    BeItem beItem = (BeItem) msg.obj;
                    tv_chinese.setText(beItem.getChinese());
                    tv_english.setText(beItem.getEnglish());
                }
            } else if (msg.what == 1) {
//
                /*录音倒计时*/
                int mRecordLength = msg.arg2;
                if (mRecordLength < -1) {
                    Toast.makeText(MakeKraoOkeActivity.this, "停止录音", Toast.LENGTH_SHORT).show();
                    Logger.d("录音倒计时:" + mRecordLength);
                    String RecordPath = recorderUtil.stopRecording();
                    startRecord.setText("完成录制");
                    isOKRecord = true;
                    timerRecoding.cancel();
                    timerRecoding = null;

                    /*添加整段录音文件*/
                    Map<String, Object> mRecordMap = new HashMap();
                    File video = new File(RecordPath);
                    mRecordMap.put("filePathName", video);
                    mRecordMap.put("startTime", "0");
                    audiosList.put(1, mRecordMap);

                    showfxDialog();//打开进度条

                    new FfmpegUtil(MakeKraoOkeActivity.this, mHandler).mixAudiosToVideo(
                            new File(KidConfig.getInstance().getPathKaraOkeNoSoundVideo() + "out_nosound_video.mp4"),
                            audiosList,
                            new File(KidConfig.getInstance().getPathMineWorksTemp() + bePageData.getTitle() + bePageData.getPage_id() + ".mp4"));//作品名称

                    Logger.d("录音地址：" + RecordPath);
                }
            } else if (msg.what == 3) {
                dismissfxDialog();
                beGoTextBookSuccess = new BeGoTextBookSuccess(KidConfig.getInstance().getPathMineWorksTemp() + bePageData.getTitle() + bePageData.getPage_id() + ".mp4",
                        "majin制作 卡拉ok  listen and say", "LAMAR", DateUtils.formatDate(new Date(), "M月d日 HH:mm"), "");
                Bundle bundle = new Bundle();
                bundle.putString("MakeFlag", "MakeKraoOke");
                bundle.putSerializable("BeGoTextBookSuccess", beGoTextBookSuccess);
                DjhJumpUtil.getInstance().startBaseActivityForResult(context, TextBookSuccessActivity.class, bundle, 2);

                Toast.makeText(context, "合成完毕！", Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_make_krao_oke);
        initialize();
        Bundle bundle = getIntent().getExtras();

        bePageData = (BePageData) bundle.getSerializable("BePageData");


                /*获取Mp4视频名称和背景音名称*/
        String sMp4 = MD5.getMD5(bePageData.getPage_url().substring(bePageData.getPage_url().lastIndexOf("/"))) + ".mp4";
        sBackground = MD5.getMD5(bePageData.getMusic_oss_name().substring(bePageData.getMusic_oss_name().lastIndexOf("/"))) + ".mp3";

        /*判断mp4文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeMp4() + sMp4)) {
            downloadKaraOkeMp4();
        } else {
            playVideo(KidConfig.getInstance().getPathKaraOkeMp4() + sMp4);
        }
        /*判断背景音文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground)) {
            downloadKaraOkeBackground();
        }
       /*实时获取mp4播放进度*/
        getCurrentVideoSchedule();

        Logger.d("" + bePageData.getItem().toString());
    }

    private void playVideo(String path) {
         /*应该判断本地文件夹是否有文件*/
        mVideoplayer.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_LIST);
        mVideoplayer.startVideo();
        mVideoplayer.onStatePreparingChangingUrl(0, 100);//暂时跳转到100毫秒的地方
        mVideoplayer.hideView();
    }

    /*下载卡拉okmp4*/
    private void downloadKaraOkeMp4() {

        BeDownFile file = new BeDownFile(Constant.file_kaoraok_mp4, bePageData.getPage_url(), "", KidConfig.getInstance().getPathTemp());
        new DownloadFile(MakeKraoOkeActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                progressDialog.dismiss();
                Logger.d("fileurl:" + fileurl);
                playVideo(fileurl);

            }
        });
    }

    /*下载卡拉okmp4*/
    private void downloadKaraOkeBackground() {
//        Logger.d("下载----downloadKaraOk背景音----" + beKaraOkPageData.getMusic_oss_name());

        BeDownFile file = new BeDownFile(Constant.file_kaoraok_bgAudio, bePageData.getMusic_oss_name(), "", KidConfig.getInstance().getPathTemp());

        new DownloadFile(MakeKraoOkeActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                progressDialog.dismiss();
                Logger.d("fileurl:" + fileurl);


            }
        });
    }

    private void initialize() {
        mVideoplayer = getView(R.id.videoplayer);
        tv_english = getView(R.id.tv_english);
        tv_chinese = getView(R.id.tv_chinese);
        startRecord = getView(R.id.btn_startRecord);
        startRecord.setOnClickListener(onClick);
        recorderUtil = new RecorderUtil();

    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            /*只有按视频返回键的时候才清理*/
            JCVideoPlayer.releaseAllVideos();
            return;
        }
        super.onBackPressed();
    }

    int onPauseCurrent = 0;

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoplayer != null) {
            onPauseCurrent = mVideoplayer.getCurrentPosition();
            mVideoplayer.onStatePause();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
                /*activity重新显示时要从新加载数据*/
        mVideoplayer.setUp(KidConfig.getInstance().getPathKaraOkeMp4() + MD5.getMD5(bePageData.getPage_url().substring(bePageData.getPage_url().lastIndexOf("/"))) + ".mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
        mVideoplayer.startVideo();
        /*跳到指定播放时间*/
        mVideoplayer.onStatePreparingChangingUrl(0, onPauseCurrent);

    }

    public void getCurrentVideoSchedule() {

        //参数2：延迟0毫秒发送，参数3：每隔1000毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (mVideoplayer.getMediaPlayer() != null) {
//                        if (mVideoplayer.getMediaPlayer().isPlaying()) {
//                        Logger.d("mCurrentPosition:" + mCurrentPosition);
//                        Logger.d("bePageData.getItem().size():" + bePageData.getItem().size());
                        if (mCurrentPosition < bePageData.getItem().size()) {
                            Message msg = Message.obtain();
                            msg.arg1 = Integer.parseInt((bePageData.getItem().get(mCurrentPosition).getTime_start())) * 1000;
                            msg.obj = bePageData.getItem().get(mCurrentPosition);
                            msg.what = 0;
                            mHandler.sendMessage(msg); // 发送消息
                        } else {
                                /*当前角标置0*/
                            mCurrentPosition = 0;
                        }
                    }
                }
//                }
            }, 1000, 1000);

        }
    }

    private Timer timerRecoding;
    private int mRecordLength = 0;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isOKRecord) {
             /*用ffmpeg合成音视频   合成成功之后 跳转至成功页面*/
                Toast.makeText(context, "完成录音！", Toast.LENGTH_SHORT).show();
                return;
            }

            /*首先关闭视频声音*/
            closeVideoviewSound();
            /*清空文件temp夹里的文件 不删除文件夹*/
            cleanEnvironment();
            /*分离视频 保存无声视频*/
            new FfmpegUtil(MakeKraoOkeActivity.this, mHandler).getNoSoundVideo(KidConfig.getInstance().getPathKaraOkeMp4() +
                            MD5.getMD5(bePageData.getPage_url().substring(bePageData.getPage_url().lastIndexOf("/"))) + ".mp4",
                    KidConfig.getInstance().getPathKaraOkeNoSoundVideo());


            /*混音的背景音*/
            Map<String, Object> mRecordMap = new HashMap();
            File video = new File(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground);
            mRecordMap.put("filePathName", video);
            mRecordMap.put("startTime", "0");
            audiosList.put(0, mRecordMap);

            recorderUtil.startRecording(bePageData.getTitle() + bePageData.getPage_id());

            mRecordLength = ((mVideoplayer.getDuration()) / 1000);
            Logger.d("录音总时间：" + mRecordLength);

            startMixA_To_VTimerRecoding();


            Toast.makeText(context, "开始录音", Toast.LENGTH_SHORT).show();

            /*测试*/
//
//              /*添加整段录音文件*/
//            Map<String, Object> mRecordMap1 = new HashMap();
//            File video1 = new File(KidConfig.getInstance().getPathRecordingAudio() + "kalaok2162.mp3");//录音地址
//            mRecordMap1.put("filePathName", video1);
//            mRecordMap1.put("startTime", "0");
//            audiosList.put(1, mRecordMap1);
//
////            showfxDialog();
//
//            Logger.d("audiosList.size()" + audiosList.size());
//            Logger.d("audiosList.tostring" + audiosList.toString());
//
//            new FfmpegUtil(MakeKraoOkeActivity.this, mHandler).mixAudiosToVideo(
//                    new File(KidConfig.getInstance().getPathKaraOkeNoSoundVideo() + "out_nosound_video.mp4"),
//                    audiosList,
//                    new File(KidConfig.getInstance().getPathMineWorksTemp() + bePageData.getTitle() + bePageData.getPage_id() + ".mp4"));//作品名称


        }


    };

    /*合成我的作品倒计时*/
    private void startMixA_To_VTimerRecoding() {

        if (timerRecoding == null) {
            timerRecoding = new Timer();
            timerRecoding.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    mRecordLength -= 1;
                    msg.arg2 = mRecordLength;
                    msg.what = 1;
                    mHandler.sendMessage(msg); // 发送消息
                }
            }, 0, 1000);
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

    /*清空环境*/
    private void cleanEnvironment() {
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathTemp()));
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMixAudios()));
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMineWorksTemp()));
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathKaraOkeNoSoundVideo()));
    }


    /*重新录制*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            Logger.d("重新录制");
            isOKRecord = false;
            startRecord.setText("开始录制");
            /*重新录制*/
            audiosList.clear();//清空装录音文件的集合
            mRecordLength = ((mVideoplayer.getDuration()) / 1000);
            Logger.d("录音总时间：" + mRecordLength);
            startMixA_To_VTimerRecoding();
        }

    }
}
