package cn.dajiahui.kid.ui.study.kinds.cardpractice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.fxtx.framework.chivox.ChivoxBasicActivity;
import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeChivoxEvaluateResult;
import cn.dajiahui.kid.ui.study.bean.BeCradPratice;
import cn.dajiahui.kid.ui.study.bean.BeCradPraticePageData;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.RotateAnimationTvSore;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;

/*
* 单词卡
* */
public class CardPracticeActivity extends ChivoxBasicActivity implements
        CardPraticeFragment.NoticeCheckButton {

    private cn.dajiahui.kid.ui.study.view.NoScrollViewPager mCardpager;
    private TextView tvname;
    private TextView tvnumber;
    private Button btnnext;
    private int currentPositinon = 0;
    private String book_id;
    private String unit_id;
    private Bundle mCardPracticeBundle;
    private TextView mScore;
    private RelativeLayout mEvaluationRoot, mPlayrecodingRoot, mRecordingRoot;
    private ImageView mPlayRecord;
    private ImageView mRecording;
    private BeChivoxEvaluateResult chivoxEvaluateResult;//解析弛声打分
    private File recordFile;//弛声录音地址

    private final int DELAYED_CHIVOX = 0;//弛声延迟

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(mCardPracticeBundle.getString("UNIT_NAME"));
        onBackText();
        initAIEngine();
    }

    /**
     * 驰声测评开始记录
     */
    @Override
    protected void record() {

    }

    @Override
    protected void recordStop() {
        if (engine.isRunning()) {
            dismissfxDialog();
            service.recordStop(engine);
        }

        isRecording = false;
    }

    /**
     * 驰声评分引擎初始化
     *
     * @param coreCreateParam
     */
    @Override
    protected void initCore(CoreCreateParam coreCreateParam) {

        service.initCore(this, coreCreateParam, new OnCreateProcessListener() {
            @Override
            public void onError(int arg0, ErrorCode.ErrorMsg errMSG) {
            }

            @Override
            public void onCompletion(int arg0, Engine aiengine) {
                engine = aiengine;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_card_practice);
        mCardPracticeBundle = getIntent().getExtras();
        book_id = mCardPracticeBundle.getString("BOOK_ID");
        unit_id = mCardPracticeBundle.getString("UNIT_ID");
        initialize();
        httpData();
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpCardPratice(CardPracticeActivity.this, callCardPratice, book_id, unit_id);
    }

    private List<BeCradPraticePageData> page_data;
    /**
     * 卡片练习callback函数
     */
    ResultCallback callCardPratice = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
//            Logger.d("卡片练习：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeCradPratice beCradPratice = json.parsingObject(BeCradPratice.class);
                if (beCradPratice != null) {
                    page_data = beCradPratice.getPage_data();
                     /*获取Mp3名称*/
                    String sMp3 = MD5.getMD5(page_data.get(0).getMusic_oss_url().substring(page_data.get(0).getMusic_oss_url().lastIndexOf("/"))) + ".mp3";

                        /*判断mp3文件是否下载过*/
                    if (FileUtil.fileIsExists(KidConfig.getInstance().getPathCardPratice() + sMp3)) {
                        CardAdapter adapter = new CardAdapter(getSupportFragmentManager());
                        mCardpager.setAdapter(adapter);

                    } else {
                        downloadCardPratice(page_data.get(0).getMusic_oss_url());
                    }


                }
            } else {
                ToastUtil.showToast(CardPracticeActivity.this, json.getMsg());
            }

        }

    };

    /*下載*/
    private void downloadCardPratice(String music_oss_url) {

        BeDownFile file = new BeDownFile(Constant.file_card_pratice, music_oss_url, "", KidConfig.getInstance().getPathTemp());

        new DownloadFile(CardPracticeActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                CardAdapter adapter = new CardAdapter(getSupportFragmentManager());
                mCardpager.setAdapter(adapter);
                progressDialog.dismiss();
            }
        });

    }

    /*初始化数据*/
    private void initialize() {
        mCardpager = getView(R.id.card_pager);
        tvname = getView(R.id.tv_name);
        tvnumber = getView(R.id.tv_number);
        btnnext = getView(R.id.btn_next);
        mPlayrecodingRoot = getView(R.id.playrecoding_root);
        mRecordingRoot = getView(R.id.img_recording_root);
        btnnext.setOnClickListener(onClick);


        mEvaluationRoot = getView(R.id.evaluation_root);
        mPlayRecord = getView(R.id.img_playrecoding);
        mRecording = getView(R.id.img_recording);
        mScore = getView(R.id.tv_score);
        mPlayrecodingRoot.setOnClickListener(onClick);
        mRecordingRoot.setOnClickListener(onClick);

    }

    private BeCradPraticePageData beCradPraticePageData;


    /*
   *卡片练习适配器
   * */
    private class CardAdapter extends FragmentStatePagerAdapter {


        private CardAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public int getCount() {
            return page_data.size();
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);

        }


        @Override
        public Fragment getItem(int arg0) {
            CardPraticeFragment fr = new CardPraticeFragment();
            Bundle bundle = new Bundle();
            beCradPraticePageData = page_data.get(arg0);
            bundle.putSerializable("BeCradPraticePageData", beCradPraticePageData);
            bundle.putInt("position", arg0);
            fr.setArguments(bundle);

            return fr;
        }

        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);

            //希望做一次垃圾回收
            System.gc();
        }
    }


    @Override
    public void NoticeCheck(boolean ischeck, int position, int mPlayNum) {
        if (ischeck) {
            btnnext.setBackgroundResource(R.color.yellow_FEBF12);
            btnnext.setClickable(true);

            if (mPlayNum == 2) {
                /*显示打分布局*/
                mEvaluationRoot.setVisibility(View.VISIBLE);
            }


        } else {
            btnnext.setBackgroundResource(R.color.gray);
            btnnext.setClickable(false);
            /*隐藏打分布局*/
            mEvaluationRoot.setVisibility(View.INVISIBLE);

            if (position >= 0) {
                currentPositinon = position;
                tvnumber.setText((position + 1) + "/" + page_data.size());
                /*最后一个 加1 然后弹框*/
                if (tvnumber.getText().equals(page_data.size() + "/" + page_data.size())) {
                    currentPositinon++;
                }
            }

        }

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_next:
                    mScore.setVisibility(View.INVISIBLE);
                    mScore.setText("");
                    currentPositinon++;
                    if (currentPositinon <= page_data.size()) {
                        mCardpager.setCurrentItem(currentPositinon);

                    } else {
                        FxDialog fxDialog = new FxDialog(CardPracticeActivity.this) {
                            @Override
                            public void onRightBtn(int flag) {
                                cleanEnvironment();
                                finishActivity();
                                dismiss();
                            }

                            @Override
                            public void onLeftBtn(int flag) {

                                dismiss();
                            }
                        };
                        fxDialog.setMessage("已经是最后一个,是否退出？");
                        fxDialog.show();
                    }


                    break;
                case R.id.playrecoding_root:
                    if (recordFile != null) {
                        PlayMedia.getPlaying().StartMp3(recordFile.getAbsolutePath());
                    }
                    break;
                case R.id.img_recording_root:
                    if (!isRecording) {/*开始录音*/
                        if (beCradPraticePageData != null) {
                            mRecording.setImageResource(R.drawable.card_record_on);
                           /* 通知评分引擎此次为英文句子评测 */
                            coretype = CoreType.en_sent_score;
//
                            recordingEvaluation();
                            isRecording = true;
                        }
                    } else {
                        /*修改录音按钮的背景*/
                        mRecording.setImageResource(R.drawable.card_record_off);
                        /*修改播放录音按钮的背景*/
                        mPlayrecodingRoot.setBackgroundResource(R.drawable.circle_bg_yellow);
                        /* 结束驰声录音 */
                        recordStop();
                    }

                    break;

            }


        }
    };

    /*录音测评*/
    private void recordingEvaluation() {

        if (currentPositinon < page_data.size()) {
                               /*参数是评分的语句*/
            recordStart(page_data.get(currentPositinon).getItem().get(0).getEnglish());
        } else {
                                /*解決数组越界*/
            recordStart(page_data.get(page_data.size() - 1).getItem().get(0).getEnglish());
        }
    }

    protected int counter = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case DELAYED_CHIVOX:
                    Logger.d("-------counter:" + counter);
                    if (counter < 3) {
                        recordStop();
                        recordingEvaluation(); // 重新开始录音
                    } else {
                        // 关闭进度条
                        // toast 提示用户系统繁忙
                        counter = 0;
                        dismissfxDialog();
                        /*修改录音按钮的背景*/
                        mRecording.setImageResource(R.drawable.card_record_off);
                        /*隐藏打分*/
                        mScore.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "系统繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                        isRecording = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };

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
//                                Logger.d("录音失败: ErrodCode:" + arg1.getErrorId() + "Desc :" + arg1.getDescription() + "\r\n" + "建议: " + arg1.getSuggest());
//                                ToastUtil.showToast(CardPracticeActivity.this, "录音失败: ErrodCode:" + arg1.getErrorId() + "Desc :" + arg1.getDescription() + "\r\n" + "建议: " + arg1.getSuggest());
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
                                //resultCode 1:错误 2:vad 3:sound 4:segment 5:evaluate
//                                Logger.d("resultCode:" + resultCode);
                                Logger.d("jsonResult:" + jsonResult);

                                switch (resultCode) {
//                                    case 3:
//                                        try {
//                                            JSONObject var1 = new JSONObject(jsonResult.getJsonText());
//                                            if (var1.has("sound_intensity")) {
//                                                Logger.d("sound_intensity:"  );
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
                                                mRecording.setImageResource(R.drawable.card_record_off);
                                                /*隐藏打分*/
                                                mScore.setVisibility(View.INVISIBLE);
                                                isRecording = false;

                                                if (var1.has("errId")) {
                                                    if (var1.getInt("errId") == 41030) {
                                                        Toast.makeText(context, "系统繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                                        break;
                                                    }
                                                    Toast.makeText(context, "系统繁忙，请稍后再试", Toast.LENGTH_SHORT).show(); // 暂定此文案，以后再根据情况再修改
                                                }
                                                break;
                                            }
                                        } catch (Exception var2) {
                                            var2.printStackTrace();
                                        }

                                        /*解析返回json*/
                                        recordFile = lastRecordFile.getRecordFile();
                                        parseChivoxJsonResult(jsonResult);
                                        /*获取评分分数*/
                                        final String overall = chivoxEvaluateResult.getOverall();

                                        mScore.setVisibility(View.VISIBLE);
                                        Logger.d("------------打分：" + overall);

                                        float cX = mScore.getWidth() / 2.0f;
                                        float cY = mScore.getHeight() / 2.0f;

                                        final RotateAnimationTvSore rotateAnim = new RotateAnimationTvSore(cX, cY, RotateAnimationTvSore.ROTATE_DECREASE);

                                        if (rotateAnim != null) {
                                            rotateAnim.setFillAfter(true);
                                            mScore.startAnimation(rotateAnim);
                                            rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                    mScore.setText(overall + "分");
                                                    mScore.clearAnimation();
                                                    rotateAnim.cancel();
                                                }

                                            });
                                        }
                                        break;
                                }
                            }
                        });

                    }

                    @Override
                    public void onBeforeLaunch(long duration) {
//                        Logger.d("duration: " + duration);
                    }

                    @Override
                    public void onRealTimeVolume(final double volume) {
                    }

                });
        /* 一秒钟查一下是否收到sound_intensity，如果没有需要重新开始录音 */
//        mHandler.sendEmptyMessageDelayed(DELAYED_CHIVOX, 1000);
//        counter++;
//        showfxDialog("请稍后...");
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

    @Override
    protected void onStop() {
        super.onStop();
//        Logger.d("CardPracticeActivity:-----------------------onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Logger.d("CardPracticeActivity:-----------------------onPause()");
    }

    /*清空环境*/
    private void cleanEnvironment() {
        /*录音文件夹*/
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathRecordingAudio()));

    }
}
