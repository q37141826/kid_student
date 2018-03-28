package cn.dajiahui.kid.ui.study.kinds.karaoke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.ui.study.bean.BeItem;
import cn.dajiahui.kid.ui.study.bean.BePageDataWork;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.FfmpegUtil;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.TextBookSuccessActivity;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;
import cn.dajiahui.kid.util.RecorderUtil;

/*
* 制作卡拉ok
*
*/
public class MakeKraoOkeActivity extends FxActivity {

    private VideoView mVideoView;
    private TextView tv_english;
    private TextView tv_chinese;
    private Button startRecord;//, mRestartRecord
    private BePageDataWork bePageDataWork;
    private Timer timer;
    private int mCurrentPosition = 0;//当前显示中英文数据源的角标
    private boolean isOKRecord = false;//完成录制标志
    private boolean isOKOnclick = false;//开始录制按钮的标志（点击开始录制之后 未录制完成就不可以再继续点击）
    //    private Map<Integer, Map<String, Object>> audiosList = new HashMap<>();//背景音+录音的list
    private RecorderUtil recorderUtil;//录音工具类
    private String sBackground;//Md5加密后的卡拉ok背景音的名字
    private String mVideoName;/*卡拉ok视频名称*/
    private BeGoTextBookSuccess beGoTextBookSuccess;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0 && mCurrentMp != null) {
                int startTime = msg.arg1;
                if (mVideoView.isPlaying()) {
                    int currentPosition = mCurrentMp.getCurrentPosition();
//                    Logger.d("startTime:" + startTime);
                    /*实时在start区间内 显示中英文*/
                    if (((startTime - 600) < (currentPosition)) && ((currentPosition) < (startTime + 600))) {
                        mCurrentPosition++;
                        BeItem beItem = (BeItem) msg.obj;
                        tv_chinese.setText(beItem.getChinese());
                        tv_english.setText(beItem.getEnglish());
                    }
                }
            } else if (msg.what == 1) {
                /*录音倒计时*/
                int mRecordLength = msg.arg2;
                if (mRecordLength < -1) {
//                    Toast.makeText(MakeKraoOkeActivity.this, "停止录音", Toast.LENGTH_SHORT).show();
//                    Logger.d("录音倒计时:" + mRecordLength);
                    String RecordPath = recorderUtil.stopRecording();
                    Logger.d("RecordPath:" + RecordPath);
                    startRecord.setText("完成录制");
                    isOKRecord = true;
                    timerRecoding.cancel();
                    timerRecoding = null;

//                    /*添加整段录音文件*/
//                    Map<String, Object> mRecordMap = new HashMap();
//                    File video = new File(RecordPath);
//                    mRecordMap.put("filePathName", video);
//                    mRecordMap.put("startTime", "0");
//                    audiosList.put(1, mRecordMap);

                    showfxDialog("视频制作中，请稍等...");//打开进度条

//                    new FfmpegUtil(MakeKraoOkeActivity.this, mHandler, bePageDataWork.getPage_id()).mixAudiosToVideo(
//                            new File(KidConfig.getInstance().getPathKaraOkeMp4() + mVideoName),
//                            audiosList,
//                            new File(KidConfig.getInstance().getPathMineWorksTemp() + "KraoOke" + bePageDataWork.getPage_id() + ".mp4"));//作品名称

                    /*自己录音直接替换视频的音轨*/
                    new FfmpegUtil(MakeKraoOkeActivity.this, mHandler, bePageDataWork.getPage_id()).
                            audioVideoSyn(new File(KidConfig.getInstance().getPathKaraOkeMp4() + mVideoName),
                                    new File(RecordPath), new File(KidConfig.getInstance().getPathMineWorksTemp() + "KraoOke" + bePageDataWork.getPage_id() + ".mp4"));
//                    Logger.d("录音地址：" + RecordPath);
                }
            } else if (msg.what == 3) {
                dismissfxDialog();
                beGoTextBookSuccess = new BeGoTextBookSuccess(
                        KidConfig.getInstance().getPathMineWorksTemp() + "KraoOke" + bePageDataWork.getPage_id() + ".mp4",
                        bePageDataWork.getPage_id(),
                        bePageDataWork.getMusic_name() + bePageDataWork.getPage_id(),
                        UserController.getInstance().getUser().getAvatar(),
                        UserController.getInstance().getUser().getNickname(),
                        (System.currentTimeMillis()));


                Bundle bundle = new Bundle();
                bundle.putString("MakeFlag", "MakeKraoOke");
                bundle.putString("ShowBottom", "SHOW");
                bundle.putSerializable("BeGoTextBookSuccess", beGoTextBookSuccess);
                DjhJumpUtil.getInstance().startBaseActivityForResult(context, TextBookSuccessActivity.class, bundle, DjhJumpUtil.getInstance().activity_makekalaok_request);
                isOKOnclick = !isOKOnclick;
                /*打开声音视频*/
                openVideoviewSound();
//                Toast.makeText(context, "合成完毕！", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void initView() {
        setContentView(R.layout.activity_make_krao_oke);

        initialize();
        Bundle bundle = getIntent().getExtras();
        bePageDataWork = (BePageDataWork) bundle.getSerializable("BePageDataWork");

        /*获取Mp4视频名称和背景音名称*/
        mVideoName = MD5.getMD5(bePageDataWork.getPage_url().substring(bePageDataWork.getPage_url().lastIndexOf("/"))) + ".mp4";
        sBackground = MD5.getMD5(bePageDataWork.getMusic_oss_name().substring(bePageDataWork.getMusic_oss_name().lastIndexOf("/"))) + ".mp3";

        /*默认显示第一句中英文*/
        tv_chinese.setText(bePageDataWork.getItem().get(mCurrentPosition).getChinese());
        tv_english.setText(bePageDataWork.getItem().get(mCurrentPosition).getEnglish());

        /*判断mp4文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeMp4() + mVideoName)) {
            downloadKaraOkeMp4();
        }
        /*判断背景音文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground)) {
            downloadKaraOkeBackground();
        }

    }


    private void playVideo(String path) {
        /*设置本地路径*/
        mVideoView.setVideoPath(path);
        //播放完成回调
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                 /*实时获取视频进度*/
                getCurrentVideoSchedule();
                /*获取音频总时长Mp3*/
                mRecordLength = ((mVideoView.getDuration()) / 1000);
                /*监听停止录音 录音倒计时*/
                startMixA_To_VTimerRecoding();
                mCurrentMp = mp;
                /*关闭原声视频声音*/
                closeVideoviewSound();
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Logger.d("播放完成了");
            }
        });
        //开始播放视频
        mVideoView.start();
    }

    /*下载卡拉okmp4*/
    private void downloadKaraOkeMp4() {
        BeDownFile file = new BeDownFile(Constant.file_kaoraok_mp4, bePageDataWork.getPage_url(), "", KidConfig.getInstance().getPathTemp());
        new DownloadFile(MakeKraoOkeActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
//                Logger.d("fileurl:" + fileurl);
                }
            }
        });
    }

    /*下载卡拉okmp4*/
    private void downloadKaraOkeBackground() {
        BeDownFile file = new BeDownFile(Constant.file_kaoraok_bgAudio, bePageDataWork.getMusic_oss_name(), "", KidConfig.getInstance().getPathTemp());
        new DownloadFile(MakeKraoOkeActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

//      Logger.d("fileurl:" + fileurl);


            }
        });
    }

    /*初始哈*/
    private void initialize() {
        mVideoView = getView(R.id.makeVideoView);
        tv_english = getView(R.id.tv_english);
        tv_chinese = getView(R.id.tv_chinese);
        startRecord = getView(R.id.btn_startRecord);
//        mRestartRecord = getView(R.id.btn_RestartRecord);
        startRecord.setOnClickListener(onClick);
//        mRestartRecord.setOnClickListener(onClick);
        recorderUtil = new RecorderUtil();
//        mVideoView.setMediaController(new MediaController(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*停止录音*/
        recorderUtil.stopRecording();
        /*暂停播放的背景音*/
        PlayMedia.getPlaying().mediaPlayer.stop();
        /*停止播放视频*/
        mVideoView.stopPlayback();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerRecoding != null) {
            timerRecoding.cancel();
            timerRecoding = null;
        }
        isOKOnclick = !isOKOnclick;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("MakeKraoOkeActivity---------------onDestroy");
        /*应该刷新课卡拉OK的数据*/


         /*暂停播放的背景音*/
        PlayMedia.getPlaying().mediaPlayer.stop();
        mVideoView.stopPlayback();
        if (recorderUtil != null) {
            recorderUtil.stopRecording();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerRecoding != null) {
            timerRecoding.cancel();
            timerRecoding = null;
        }
    }

    private MediaPlayer mCurrentMp;

    /*关闭视频的声音*/
    public void closeVideoviewSound() {
        mCurrentMp.setVolume(0, 0);/*关闭视频声音*/
    }

    /*打开视频的声音*/
    public void openVideoviewSound() {
//        mCurrentMp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mCurrentMp.setVolume(1, 1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void getCurrentVideoSchedule() {

        //参数2：延迟0毫秒发送，参数3：每隔1000毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mCurrentPosition < bePageDataWork.getItem().size()) {
                        Message msg = Message.obtain();
                        if (StringUtil.isNumericzidai((bePageDataWork.getItem().get(mCurrentPosition).getTime_start()))) {
                            msg.arg1 = Integer.parseInt((bePageDataWork.getItem().get(mCurrentPosition).getTime_start()));
                            msg.obj = bePageDataWork.getItem().get(mCurrentPosition);
                            msg.what = 0;
                            mHandler.sendMessage(msg); // 发送消息
                        }
                    } else {
                        /*当前角标置0*/
                        mCurrentPosition = 0;
                    }
                }

            }, 0, 1000);

        }
    }

    private Timer timerRecoding;
    private int mRecordLength = 0;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_startRecord:

                    if (isOKRecord) {
                        return;
                    }
                    if (!isOKOnclick) {
                /*清空文件temp夹里的文件 不删除文件夹*/
                        cleanEnvironment();
//               /*混音的背景音*/
//                Map<String, Object> mRecordMap = new HashMap();
//                File video = new File(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground);
//                mRecordMap.put("filePathName", video);
//                mRecordMap.put("startTime", "0");
//                audiosList.put(0, mRecordMap);
                        /*开始录音*/
                        recorderUtil.startRecording(bePageDataWork.getPage_no() + bePageDataWork.getPage_id());
                        /*播放原声视频Mp4*/
                        playVideo(KidConfig.getInstance().getPathKaraOkeMp4() + mVideoName);
//                      Logger.d("背景音：" + KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground);
                        /*播放背景音Mp3*/
                        PlayMedia.getPlaying().StartMp3(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground);
                        isOKOnclick = !isOKOnclick;
                        startRecord.setText("录制中");
                        startRecord.setBackgroundColor(getResources().getColor(R.color.gray_DCDCDC));
//                       mRestartRecord.setBackgroundColor(getResources().getColor(R.color.yellow_FEBF12));
//                      Toast.makeText(context, "开始录音", Toast.LENGTH_SHORT).show();

                    }

                    break;
                default:
                    break;

            }

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
//                    Logger.d("录音总时长：" + mRecordLength);
                    msg.arg2 = mRecordLength;
                    msg.what = 1;
                    mHandler.sendMessage(msg); // 发送消息
                }
            }, 0, 1000);
        }
    }

//    /*关闭视频的声音*/
//    public void closeVideoviewSound() {
//        mVideoView.getMediaPlayer().setVolume(0, 0);
//    }
//
//    /*打开视频的声音*/
//    public void openVideoviewSound() {
//        mVideoView.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mVideoView.getMediaPlayer().setVolume(1, 1);
//
//    }

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
      /*重新录制*/
        if (requestCode == DjhJumpUtil.getInstance().activity_makekalaok_request && resultCode == DjhJumpUtil.getInstance().activity_makekalaok_result) {
            isOKRecord = false;
            isOKOnclick = !isOKOnclick;
            /*改变录音按钮颜色*/
            startRecord.setBackgroundColor(getResources().getColor(R.color.yellow_FEBF12));
            startRecord.setText("开始录制");
        }

        /*制作成功后按返回键 与 左上角退出视频*/
        if (requestCode == DjhJumpUtil.getInstance().activity_makekalaok_request && resultCode == DjhJumpUtil.getInstance().activity_makekalaok_out) {

            finishActivity();
        }
    }


}