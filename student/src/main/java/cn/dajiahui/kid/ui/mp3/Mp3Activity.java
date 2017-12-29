package cn.dajiahui.kid.ui.mp3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fxtx.framework.lamemp3.AudioListener;
import com.fxtx.framework.lamemp3.Mp3Audio;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;
import com.fxtx.framework.widgets.TasksCompletedView;
import com.fxtx.framework.widgets.dialog.FxDialog;

import java.io.File;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.AttachmentFileUpdate;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnAttachmentUpdate;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.http.bean.BeTeFile;
import cn.dajiahui.kid.ui.mp3.bean.BeMp3;


/**
 * Created by z on 2016/5/13.
 */
//MP3的录制和播放界面
public class Mp3Activity extends FxActivity {
    private BeMp3 mp3;
    private TasksCompletedView vTask;
    private View buttonView; //底部按钮

    private ImageView imStart;
    private View oneBtn, twoBtn;
    private Mp3Audio audio;
    private boolean isAudio;//录音还是播放 true 录音，false 播放  当前播放状态
    private boolean isRecorder = false;//是否允许录制
    private int startType = 0;//  0 录制， 1 播放，2停止
    private File file;
    private FxDialog dialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mp3);
        vTask = getView(R.id.vTask);
        imStart = getView(R.id.im_btn);
        buttonView = getView(R.id.linearLayout2);
        oneBtn = getView(R.id.dialog_one);
        twoBtn = getView(R.id.dialog_two);
        imStart.setOnClickListener(onClick);
        oneBtn.setOnClickListener(onClick);
        twoBtn.setOnClickListener(onClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle("录音");
        onBackText();
        mp3 = (BeMp3) getIntent().getSerializableExtra(Constant.bundle_obj);
        isRecorder = getIntent().getBooleanExtra(Constant.bundle_type, false);
        audio = new Mp3Audio(context, new AudioListener() {
            @Override
            public void onRecorderStop() {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onRecorderStart() {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onPlayStart() {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void paushAudio() {

            }

            @Override
            public void onPlayStop() {
                handler.sendEmptyMessage(3);
            }

            @Override
            public void onDuration(int duration) {
                Message message = handler.obtainMessage();
                message.what = 4;
                message.obj = duration;
                handler.sendMessage(message);
            }


            @Override
            public void onError(String error) {
                handler.sendEmptyMessage(-1);
                Message message = handler.obtainMessage();
                message.what = -1;
                message.obj = error;
                handler.sendMessage(message);

            }
        });

        audio.setMp3MaxTimers(300);//最大时长  5分钟
        vTask.setmTotalProgress(300);
        if (mp3 != null) {
            isAudio = false;
            startType = 1;
            imStart.setImageResource(R.drawable.ico_mp3_b);
            if (!StringUtil.isEmpty(mp3.getFilePath())) {
                file = new File(mp3.getFilePath());
                if (!file.exists() || !file.isFile()) {
                    //执行下载方法
                    initDownMp3();
                } else {
                    audio.initAudio(file.getPath());
                    vTask.setProgress(mp3.getDuration());
                    if (isRecorder) {
                        buttonView.setVisibility(View.VISIBLE);
                    }
                    imStart.setImageResource(R.drawable.ico_mp3_b);
                }
            } else if (!StringUtil.isEmpty(mp3.getFileUrl())) {
                //执行下载方法
                initDownMp3();
            }
        }
        initData();
    }

    private void initDownMp3() {
        BeDownFile file = new BeDownFile(mp3.getObjectId(), Constant.file_mp3, mp3.getFileUrl(), "录音");
        new DownloadFile(this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl) {
                startType = 1;
                mp3.setFilePath(fileurl);
                audio.initAudio(fileurl);
                vTask.setProgress(audio.getAudioDuration());
                if (isRecorder) {
                    buttonView.setVisibility(View.VISIBLE);
                }
                Mp3Activity.this.file = new File(fileurl);
                imStart.setImageResource(R.drawable.ico_mp3_b);
            }
        });
    }

    @Override
    public void finishActivity() {
        if (startType == 2) {
            if (isAudio) {
                audio.stopRecorder();
            } else {
                audio.stopAudio();
            }
        }
        Intent intent = new Intent();
        intent.putExtra(Constant.bundle_obj, "");
        setResult(RESULT_OK, intent);
        super.finishActivity();
    }


    private void initData() {
        dialog = new FxDialog(this) {
            @Override
            public void onRightBtn(int flag) {
                startType = 0;
                if (flag == 1) {
                    if (audio.isPlayer()) {
                        audio.stopAudio();
                    }
                    if (file != null)
                        file.delete();
                    mp3 = null;
                    isAudio = true;
                    handler.sendEmptyMessage(3);
                } else

                {
                    if (file == null || !file.exists()) {
                        ToastUtil.showToast(context, "录音文件丢失，请重新录制");
                        vTask.setProgress(0);
                        return;
                    }
                    showfxDialog();

                    AttachmentFileUpdate fileUpdate = new AttachmentFileUpdate(file, context, new OnAttachmentUpdate() {
                        @Override
                        public void saveFile(BeTeFile files) {
                            dismissfxDialog();
                            if (file != null) {
                                Intent intent = new Intent();
                                intent.putExtra(Constant.bundle_obj, files.getFileUrl());
                                setResult(RESULT_OK, intent);
                                ActivityUtil.getInstance().finishActivity(Mp3Activity.class);
                                finishAnim();
                            }
                        }

                        @Override
                        public void error(String error) {
                            dismissfxDialog();
                            ToastUtil.showToast(context, error);
                        }

                    });
                    fileUpdate.startUpdate();
                }

                handler.sendEmptyMessage(3);
            }

            @Override
            public void onLeftBtn(int flag) {

            }
        }

        ;
    }

    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case -1:
                    //错误
                    ToastUtil.showToast(context, (String) msg.obj);
                    if (isAudio) {
                        startType = 0;
                        imStart.setImageResource(R.drawable.ico_mp3_re);
                        buttonView.setVisibility(View.GONE);
                    } else {
                        startType = 1;
                        imStart.setImageResource(R.drawable.ico_mp3_b);
                        if (isRecorder) {
                            buttonView.setVisibility(View.VISIBLE);
                        } else {
                            buttonView.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 0:
                    //录制结束
                    if (startType == 0) {
                        buttonView.setVisibility(View.GONE);
                        imStart.setImageResource(R.drawable.ico_mp3_re);
                    } else {
                        startType = 1;
                        imStart.setImageResource(R.drawable.ico_mp3_b);
                        buttonView.setVisibility(View.VISIBLE);
                    }
                    file = audio.getFile();
                    break;
                case 1:
                    isAudio = true;
                    startType = 2;
                    //开始录制
                    imStart.setImageResource(R.drawable.ico_mp3_t);
                    break;
                case 2:
                    isAudio = false;
                    //开始播放
                    startType = 2;
                    imStart.setImageResource(R.drawable.ico_mp3_t);
                    break;
                case 3:
                    //播放结束
                    if (isAudio) { //这样是为了处理按钮后界面的样式
                        startType = 0;
                        buttonView.setVisibility(View.GONE);
                        imStart.setImageResource(R.drawable.ico_mp3_re);
                        vTask.setProgress(0);
                    } else {
                        startType = 1;
                        imStart.setImageResource(R.drawable.ico_mp3_b);
                        if (isRecorder) {
                            buttonView.setVisibility(View.VISIBLE);
                        } else {
                            buttonView.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 4:
                    //播放或录制进度
                    vTask.setProgress((int) msg.obj);
                    break;

                default:
                    break;
            }
        }
    };

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_one:
                    //删除
                    dialog.setTitle(R.string.delete);
                    dialog.setMessage("是否要删除录音？");
                    dialog.setFloag(1);
                    dialog.show();
                    break;
                case R.id.dialog_two:
                    //确认
                    dialog.setTitle(R.string.prompt);
                    dialog.setMessage("是否要上传录音？");
                    dialog.setFloag(2);
                    dialog.show();
                    break;
                case R.id.im_btn:
                    //开始录音
                    if (startType == 0) {
                        audio.startRecorder();
                    } else if (startType == 1) {
                        if (file != null) {
                            audio.startAudio(file.getPath());
                            Log.d("majin", "file.getPath()"+file.getPath());
                        } else {
                            ToastUtil.showToast(context, "文件已损坏无法播放");
                        }
                    } else if (startType == 2) {
                        if (isAudio) {
                            audio.stopRecorder();
                        } else {
                            audio.stopAudio();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

}
