package cn.dajiahui.kid.ui.chivox;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.chivox.media.OnReplayListener;
import com.fxtx.framework.chivox.ChivoxBasicActivity;

import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.chivox.config.StringConfig;

// 驰声测试用
public class WordSentPred extends ChivoxBasicActivity implements View.OnClickListener {
//    protected VoiceLineView voiceLineView;
    protected Button btnWord;
    protected Button btnSent;
    protected Button btnRecord;
    protected Button btnPred;
    protected TextView tvReftext;
    protected TextView tvResult;
    protected TextView tvOverall;
    protected TextView tvWordPhoneFromciba;
    protected Button btnReplay;
    protected String lastRecordAudioPath = "";

    protected float predPrecision;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.wordsentpred_layout);
        super.onCreate(savedInstanceState);
        initView();
        setCoreType();
        setRefText();
        initAIEngine();
    }

    @Override
    protected void initView() {
        btnWord = (Button) findViewById(R.id.btnWord);
        btnSent = (Button) findViewById(R.id.btnSent);
        btnPred = (Button) findViewById(R.id.btnPred);
        btnRecord = (Button) findViewById(R.id.btnRecord);
        btnReplay = (Button) findViewById(R.id.btnReplay);
        tvReftext = (TextView) findViewById(R.id.textRefText);
        tvResult = (TextView) findViewById(R.id.textResult);
        tvOverall = (TextView) findViewById(R.id.tvOverall);
        tvWordPhoneFromciba = (TextView) findViewById(R.id.tvWordPhoneFromciba);
//        voiceLineView = (VoiceLineView) findViewById(R.id.voicLine);
        tvResult.setMovementMethod(new ScrollingMovementMethod());
        btnWord.setOnClickListener(this);
        btnSent.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
        btnReplay.setOnClickListener(this);
        btnPred.setOnClickListener(this);
        //评分引擎初始化成功之前，按钮为不可点击的状态
        btnPred.setClickable(false);
        btnWord.setClickable(false);
        btnSent.setClickable(false);
        btnRecord.setClickable(false);
        btnReplay.setClickable(false);
    }

    @Override
    protected void setCoreType() {
        coretype = CoreType.en_word_score;
    }

    @Override
    protected void setRefText() {
        tvReftext.setText(StringConfig.refTextWord[new Random().nextInt(StringConfig.refTextWord.length)]);
    }

    @Override
    protected void initCore(CoreCreateParam coreCreateParam) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        service.initCore(this, coreCreateParam, new OnCreateProcessListener() {

            @Override
            public void onError(int arg0, ErrorCode.ErrorMsg errMSG) {
                Log.d("inside initCore", errMSG.getReason());
            }

            @Override
            public void onCompletion(int arg0, Engine aiengine) {

                engine = aiengine;
                Log.d(TAG, "Engine created :" + engine);
                //初始化成功，将按钮设置为可点击状态״̬
                btnWord.setClickable(true);
                btnSent.setClickable(true);
                btnPred.setClickable(true);
                btnRecord.setClickable(true);
                btnReplay.setClickable(true);
                //初始化成功，将按钮设置为可点击状态״̬
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"引擎初始化成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnWord) {
            tvReftext.setText(StringConfig.refTextWord[new Random().nextInt(StringConfig.refTextWord.length)]);
            coretype = CoreType.en_word_score;
        } else if (v == btnSent) {
            tvReftext.setText(StringConfig.refTextSent[new Random().nextInt(StringConfig.refTextSent.length)]);
            coretype = CoreType.en_sent_score;
        } else if (v == btnPred) {
            tvReftext.setText(StringConfig.refTextpred[0]);
            coretype = CoreType.en_pred_score;
            predPrecision = (float) 0.5;
        } else if (v == btnRecord) {
            isRecording = !isRecording;
            record();
        } else if (v == btnReplay) {
            isReplaying = true;
            replay();
        }
    }

    //判断是否在录音，触发相应事件
    protected void record() {
        tvResult.setText("");
        if (isRecording) {
            btnRecord.setText(R.string.StopRecord);
//            voiceLineView.setVisibility(View.VISIBLE);
            recordStart();
        } else {
            btnRecord.setText(R.string.StartRecord);
//            voiceLineView.setVisibility(View.INVISIBLE);
            recordStop();
        }
    }

    //开始录音
    public void recordStart() {

        final String refText = tvReftext.getText().toString();
        //boolean isVadInUsed = isVadLoad;
        Log.d(TAG, "isVadLoaded: "+ isVadLoad);
        //传入coreType
        CoreLaunchParam coreLaunchParam = new CoreLaunchParam(isOnline, coretype, refText, isVadLoad);
        //不传coreType
        //CoreLaunchParam coreLaunchParam = new CoreLaunchParam(isOnline,null,refText,isVadLoad);
        coreLaunchParam.getRequest().setRank(Rank.rank100);
        coreLaunchParam.setVadEnable(false);
        try {
            Log.d("WordSentPred","coreLaunchParam: "+coreLaunchParam.getCoreLaunchParams());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //段落评分的评分精度，不设置默认为1，可以设置为0.5
        /*if(coretype==CoreType.en_pred_score) {
            coreLaunchParam.setPrecision(predPrecision);
        }*/
        service.recordStart(this, engine,-1, coreLaunchParam,
                new OnLaunchProcessListener() {

                    @Override
                    public void onError(int arg0, final ErrorCode.ErrorMsg arg1) {
                        Log.d("inside Error", "ErrorId : " + arg1.getErrorId() + "Reason : " + arg1.getReason());
                        Log.d("inside Error", "Desc : " + arg1.getDescription() + "Suggest : " + arg1.getSuggest());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResult.setText("录音失败: ErrodCode:" + arg1.getErrorId() + "Desc :" + arg1.getDescription() + "\r\n" + "建议: " + arg1.getSuggest());
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
                                tvResult.setText("resultCode: "+resultCode+"--jsonResult: "+jsonResult.toString());
                                //Log.d("Response",);
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                                Log.d("recordStart",df.format(new Date())+"-- Get Result."+"resultCode: "+resultCode+"--jsonResult: "+jsonResult.toString());
                            }
                        });
                    }

                    @Override
                    public void onBeforeLaunch(long duration) {
                        Log.d(TAG,"duration: "+duration);
                    }

                    @Override
                    public void onRealTimeVolume(final double volume) {
                        runOnUiThread(new Runnable() {

                            public void run() {
//                                voiceLineView.setVolume((int) volume);
                            }
                        });
                    }
                });
    }

    //ֹͣ结束录音
    protected void recordStop() {
        Log.d(TAG, "engine isRunning " + engine.isRunning());


        if (engine.isRunning()) {
            service.recordStop(engine);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Log.d("recordStart",df.format(new Date())+"-- Record Stop.");
        }

        isRecording = false;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnRecord.setText(R.string.StartRecord);
//                voiceLineView.setVisibility(View.INVISIBLE);
            }
        });
    }

    //判断是否在回放录音，触发相应事件
    private void replay() {
        if (null != lastRecordFile) {
            if ("回放录音".equals(btnReplay.getText())) {

                btnReplay.setText("结束回放");
                replayStart();
            } else {
                btnReplay.setText("回放录音");
                replayStop();
            }
        }

    }

    //开始回放录音
    private void replayStart() {
        File audioFile = lastRecordFile.getRecordFile();
        service.replayStart(this, audioFile, new OnReplayListener() {

            @Override
            public void onError(int arg0, ErrorCode.ErrorMsg arg1) {

            }

            @Override
            public void onAfterReplay(int arg0) {
                isReplaying = false;
                btnReplay.setText("回放录音");
            }

            @Override
            public void onBeforeReplay(long arg0) {
            }
        });
    }

    //结束回放
    private void replayStop() {
        service.replayStop();
    }
}
