package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeChivoxEvaluateResult;
import cn.dajiahui.kid.ui.study.bean.BeRadarChrt;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramScore;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageDataItem;
import cn.dajiahui.kid.ui.study.kinds.cardpractice.CardPracticeActivity;
import cn.dajiahui.kid.ui.study.kinds.cardpractice.ScoreDialog;
import cn.dajiahui.kid.ui.study.view.WebViewMod;
import cn.dajiahui.kid.ui.video.util.JCVideoPlayerStudent;

import static cn.dajiahui.kid.controller.Constant.CardPratice_ScoreUrl;

/**
 * Created by mj on 2018/1/31.
 * <p>
 * 课本剧中可滑动的卡片
 * <p>
 * <p>
 * 打分 之后显示小星星（隐藏进度条和 时间）待续完成
 */

public class TextBookDramaCardFragment extends FxFragment implements MakeTextBookDrmaActivity.RefreshWidget {


    private BeTextBookDramaPageDataItem beTextBookDramaPageDataItem;
    private TextView tvunit;
    private JCVideoPlayerStudent mVideoplayer;
    private RelativeLayout videoplayerroot;

    private String totalsize;
    private TextView tvcurrentnum;
    private TextView tvenglish;
    private TextView tvchinese;
    private cn.dajiahui.kid.ui.homework.view.ProhibitMoveSeekbar recordseek;
    private TextView tvtotaltime;
    private RatingBar ratingBar;
    private LinearLayout seekroot;
    private RelativeLayout mScoreRoot;
    private TextView mTvScore;

    private List<BeRadarChrt> mRadarChartList = new ArrayList<>();
    private WebViewMod mWebView;
    private BeChivoxEvaluateResult chivoxEvaluateResult;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_text_book_drama_card, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        beTextBookDramaPageDataItem = (BeTextBookDramaPageDataItem) bundle.get("BeTextBookDramaPageDataItem");
        int position = (int) bundle.get("position");
        /*通知碎片中的进度条*/

        totalsize = bundle.getString("size");
        tvenglish.setText(beTextBookDramaPageDataItem.getEnglish());
        tvchinese.setText(beTextBookDramaPageDataItem.getChinese());

        if (!isScore) {
            if (StringUtil.isNumericzidai(beTextBookDramaPageDataItem.getTime_end()) && StringUtil.isNumericzidai(beTextBookDramaPageDataItem.getTime_start())) {
                seekroot.setVisibility(View.VISIBLE);
                mScoreRoot.setVisibility(View.INVISIBLE);
                tvtotaltime.setText(((Integer.parseInt(beTextBookDramaPageDataItem.getTime_end()) - Integer.parseInt(beTextBookDramaPageDataItem.getTime_start())) / 1000) + "s");
                tvcurrentnum.setText("1/" + totalsize);
            } else {
                Toast.makeText(activity, "数据错误", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
//      RefreshWidget refreshWidget = (RefreshWidget) activity;
    }

    private boolean isScore = false;

    @Override
    public void refresgWidget(BeTextBookDramScore beTextBookDramScore, int position) {
        if (beTextBookDramScore != null) {

            isScore = beTextBookDramScore.isScore();
            /*评测过分数，显示小星星*/
            if (beTextBookDramScore.isScore()) {
                this.chivoxEvaluateResult = beTextBookDramScore.getBeChivoxEvaluateResult();
                markScore(beTextBookDramScore.getBeChivoxEvaluateResult());
            }
        }
        tvcurrentnum.setText(position + "/" + totalsize);

//        Logger.d("------------position：" + position);
    }

    private void initialize() {
        tvcurrentnum = getView(R.id.tv_currentnum);
        tvenglish = getView(R.id.tv_english);
        tvchinese = getView(R.id.tv_chinese);
        recordseek = getView(R.id.record_seek);
        tvtotaltime = getView(R.id.tv_totaltime);
        seekroot = getView(R.id.seek_root);
        ratingBar = getView(R.id.rb_score);

        mScoreRoot = getView(R.id.rb_score_root);
        mTvScore = getView(R.id.tv_score);
        mTvScore.setOnClickListener(onClick);
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.tv_score:

                    showScoreDialog();
                    break;

                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int arg1 = msg.arg1;
                recordseek.setProgress(allSecond);
                if (allSecond == arg1) {
                    allSecond = -1;
                    mProssTimer.cancel();
                    mProssTimer = null;
                }
            }

        }
    };

    Timer mProssTimer;
    int allSecond = -1;

    /*时间*/
    public void refreshTime(final int Second) {
        if (seekroot != null) {
            seekroot.setVisibility(View.VISIBLE);
            mScoreRoot.setVisibility(View.INVISIBLE);
            tvtotaltime.setText(Second + "s");
        }
    }

    /*刷新进度条*/
    public void refreshProgress(final int Second) {

        recordseek.setMax(Second);

        if (mProssTimer == null) {
            mProssTimer = new Timer();
            mProssTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    allSecond += 1;
                    msg.arg1 = Second;
                    msg.what = 1;
                    mHandler.sendMessage(msg); // 发送消息
                }
            }, 0, 1000);
        }
    }



    /*点亮小星星 打分*/
    public void markScore(BeChivoxEvaluateResult chivoxEvaluateResult) {
        this.chivoxEvaluateResult = chivoxEvaluateResult;
        seekroot.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        mScoreRoot.setVisibility(View.VISIBLE);
        ratingBar.setMax(100);
        ratingBar.setProgress(Integer.parseInt(chivoxEvaluateResult.getOverall()));
        mTvScore.setText(chivoxEvaluateResult.getOverall() + "分");
        if (Integer.parseInt(chivoxEvaluateResult.getOverall()) >= 60) {
            mTvScore.setBackgroundResource(R.drawable.card_pratice_score_green_bg);
        } else {
            mTvScore.setBackgroundResource(R.drawable.card_pratice_score_red_bg);
        }

    }

    public void showScoreDialog() {

        /*弹框退出*/
        ScoreDialog scoreDialog = new ScoreDialog(getActivity(), R.layout.dialog_card_score) {


            @Override
            public void initView() {
                /*关闭dialo*/
                rootView.findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dismiss();
                    }
                });

                TextView mScore = (TextView) rootView.findViewById(R.id.tv_score);
                mScore.setText(chivoxEvaluateResult.getOverall()+"分");

                mWebView = (WebViewMod) rootView.findViewById(R.id.webview);
                mWebView.setScrollContainer(false);
                mWebView.setVerticalScrollBarEnabled(false);
                mWebView.setHorizontalScrollBarEnabled(false);
                mWebView.loadUrl(CardPratice_ScoreUrl);
                mWebView.getSettings().setJavaScriptEnabled(true);//支持javaScript
                mWebView.setWebViewClient(new WebViewClient() {
                    //设置不用系统浏览器打开,直接显示在当前Webview
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {

                        super.onPageStarted(view, url, favicon);

                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        String s = new GsonUtil().getJsonElement(AppendRadarChartJson()).toString();
                        /*调取JS*/
                        evaluateJavascript("clientInit(" + s + ")");
                    }


                });

                /*设置文本*/
                settingText(rootView);
            }
        };
        scoreDialog.show();


    }

    /*拼接打分7个维度的json*/
    private List<BeRadarChrt> AppendRadarChartJson() {
        mRadarChartList.add(new BeRadarChrt("toneScore", chivoxEvaluateResult.getRhythm().getTone()));//声调得分
        mRadarChartList.add(new BeRadarChrt("fluency", chivoxEvaluateResult.getFluency().getOverall()));//流利度
        mRadarChartList.add(new BeRadarChrt("integrity", chivoxEvaluateResult.getIntegrity()));//完整度
        mRadarChartList.add(new BeRadarChrt("stressScore", chivoxEvaluateResult.getRhythm().getStress()));//重音得分
        mRadarChartList.add(new BeRadarChrt("wordScore", chivoxEvaluateResult.getDetails().get(0).getScore()));//单词得分
        mRadarChartList.add(new BeRadarChrt("rhythm", chivoxEvaluateResult.getRhythm().getOverall()));//韵律得分
        mRadarChartList.add(new BeRadarChrt("accuracy", chivoxEvaluateResult.getAccuracy()));//准确度
        return mRadarChartList;
    }

    /*渲染页面*/
    @SuppressLint("NewApi")
    private void evaluateJavascript(final String value) {
        mWebView.evaluateJavascript(value, new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String value) {

                Logger.d("onReceiveValue " + value);
                mRadarChartList.clear();
            }
        });
    }


    private void settingText(View rootView) {
        TextView integrity = (TextView) rootView.findViewById(R.id.integrity);
        int mIntegrity = Integer.parseInt(chivoxEvaluateResult.getIntegrity());
        /*完整度*/
        settingTextIntegrity(ContrastScore(mIntegrity), integrity);

        TextView accuracy = (TextView) rootView.findViewById(R.id.accuracy);
        int mAccuracy = Integer.parseInt(chivoxEvaluateResult.getAccuracy());
        /*准确性*/
        settingTextAccuracy(ContrastScore(mAccuracy), accuracy);

        /*流利度*/
        TextView overall = (TextView) rootView.findViewById(R.id.overall);
        int mOverall = Integer.parseInt(chivoxEvaluateResult.getFluency().getOverall());  /*准确性*/
        settingTextOverall(ContrastScore(mOverall), overall);


    }

    private int ContrastScore(int score) {

        if (0 <= score && score <= 54) {
            return 4;
        } else if (55 <= score && score <= 69) {
            return 3;
        } else if (70 <= score && score <= 85) {
            return 2;
        } else if (86 <= score && score <= 100) {
            return 1;
        }
        return 0;
    }

    /*完整度*/
    private void settingTextIntegrity(int condition, TextView integrity) {

        switch (condition) {

            case 1:
                integrity.setText(R.string.integrity_excellent);
                break;
            case 2:
                integrity.setText(R.string.integrity_good);
                break;
            case 3:
                integrity.setText(R.string.integrity_commonly);
                break;
            case 4:
                integrity.setText(R.string.integrity_difference);
                break;

            default:
                break;
        }
    }

    /*准确度*/
    private void settingTextAccuracy(int condition, TextView accuracy) {

        switch (condition) {

            case 1:
                accuracy.setText(R.string.accuracy_excellent);
                break;
            case 2:
                accuracy.setText(R.string.accuracy_good);
                break;
            case 3:
                accuracy.setText(R.string.accuracy_commonly);
                break;
            case 4:
                accuracy.setText(R.string.accuracy_difference);
                break;

            default:
                break;
        }
    }

    /*流利度*/
    private void settingTextOverall(int condition, TextView overall) {

        switch (condition) {

            case 1:
                overall.setText(R.string.overall_excellent);
                break;
            case 2:
                overall.setText(R.string.overall_good);
                break;
            case 3:
                overall.setText(R.string.overall_commonly);
                break;
            case 4:
                overall.setText(R.string.overall_difference);
                break;

            default:
                break;
        }
    }

}
