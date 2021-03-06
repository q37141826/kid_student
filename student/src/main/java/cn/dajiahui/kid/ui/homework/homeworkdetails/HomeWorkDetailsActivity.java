package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.ParseUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.adapter.ApHomeWorkDetail;
import cn.dajiahui.kid.ui.homework.bean.BeAnswerSheet;
import cn.dajiahui.kid.ui.homework.bean.BeHomeWorkDetails;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
 *
 *做作业初级界面-
 *
 * */
public class HomeWorkDetailsActivity extends FxActivity {

    private TextView tvhomeworkname;//作业名称
    private LinearLayout mLinscoreState;//正确 错误  未做布局
    private TextView tvcompletecount;
    private RelativeLayout rela_score;//打分布局
    private LinearLayout linhomework_detail;//, line_nocomplete，line_complete,
    private TextView tv_correct_rate;//homework_time, tv_completetime,
    private TextView homework_timename, tv_time;//做作业，继续作业： 截止时间    查看作业： 完成时间
    private GridView grildview;
    private Button btn_dohomework;
    private String homework_id;
    private String unit_name;
    private String is_complete;
    private RatingBar rb_score;
    private List<BeAnswerSheet> mBeAnswerSheetList = new ArrayList<>();
    private ApHomeWorkDetail apHomeWorkDetail;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_work_details);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        initialize();
        Bundle bundle = getIntent().getExtras();
        unit_name = bundle.getString("UNIT_NAME");
        homework_id = bundle.getString("homework_id");

        apHomeWorkDetail = new ApHomeWorkDetail(HomeWorkDetailsActivity.this, mBeAnswerSheetList);
        grildview.setAdapter(apHomeWorkDetail);
        /*网络请求*/
        httpData();
        btn_dohomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("SourceFlag", "HomeWork");
                bundle.putString("homework_id", homework_id);
                bundle.putString("UNIT_NAME", unit_name);
                bundle.putString("IS_COMPLETE", is_complete);
                /*先跳转 在网络请请求获取数据*/
                DjhJumpUtil.getInstance().startBaseActivityForResult(HomeWorkDetailsActivity.this, DoHomeworkActivity.class, bundle, DjhJumpUtil.getInstance().activtiy_DoHomework);
//                finishActivity();
            }
        });

        /*直接查看单个的做题情况*/
        grildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("homework_id", homework_id);
                bundle.putString("UNIT_NAME", unit_name);
                bundle.putString("IS_COMPLETE", is_complete);
                /*先跳转 在网络请请求获取数据*/
                DjhJumpUtil.getInstance().startBaseActivity(HomeWorkDetailsActivity.this, DoHomeworkActivity.class, bundle, 0);
                finishActivity();
            }
        });
    }


    /*初始化*/
    private void initialize() {
        linhomework_detail = getView(R.id.linhomework_detail);
        rela_score = getView(R.id.rela_score);
        tvhomeworkname = getView(R.id.tv_homeworkname);
        mLinscoreState = getView(R.id.lin_scorestate);
        tvcompletecount = getView(R.id.tv_completecount);
        btn_dohomework = getView(R.id.btn_dohomework);
        grildview = getView(R.id.grildview);
        homework_timename = getView(R.id.homework_timename);
        tv_time = getView(R.id.tv_time);
        tv_correct_rate = getView(R.id.tv_correct_rate);
        rb_score = getView(R.id.rb_score);
        rb_score.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
    }

    @Override
    public void httpData() {
        super.httpData();
        showfxDialog();

        RequestUtill.getInstance().httpGetStudentHomeWorkDetails(HomeWorkDetailsActivity.this, callHomeWorkDetails, homework_id);

    }

    /**
     * 学生作业列表详情callback函数
     */
    ResultCallback callHomeWorkDetails = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("作业详情response:" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeHomeWorkDetails beHomeWorkDetails = json.parsingObject(BeHomeWorkDetails.class);
                is_complete = beHomeWorkDetails.getIs_complete();
                if (beHomeWorkDetails != null) {
                    /*测试*/
                    if (beHomeWorkDetails.getIs_complete().equals("-1")) {
                        /*设置title*/
                        setfxTtitle(beHomeWorkDetails.getName());
                        btn_dohomework.setText("开始做作业");
//                      /*作业名称*/
                        tvhomeworkname.setText(beHomeWorkDetails.getName());
                        /*截止时间*/
                        homework_timename.setText("截止时间");
                        tv_time.setText(DateUtils.EndHomeWorktime(beHomeWorkDetails.getEnd_time()));
                        /*完成人数*/
                        tvcompletecount.setText(beHomeWorkDetails.getComplete_students() + "/" + beHomeWorkDetails.getAll_students());

                        rela_score.setVisibility(View.GONE);
                    } else if (beHomeWorkDetails.getIs_complete().equals("1")) {
                        /*设置title*/
                        setfxTtitle(beHomeWorkDetails.getName());
                        btn_dohomework.setText("查看作业");
                        /*作业名称*/
                        tvhomeworkname.setText(beHomeWorkDetails.getName());
                        /*完成时间*/
                        homework_timename.setText("完成时间");
                        tv_time.setText(DateUtils.EndHomeWorktime(beHomeWorkDetails.getComplete_time()));
                        /*完成人数*/
                        tvcompletecount.setText(beHomeWorkDetails.getComplete_students() + "/" + beHomeWorkDetails.getAll_students());

                        String correct_rate = beHomeWorkDetails.getCorrect_rate();
                        double v = Double.parseDouble(correct_rate) * 100;
                        tv_correct_rate.setText("正确率：" + (int) v + "%");//正确率
                        rb_score.setMax(100);
                        /*打分的分数 */
                        rb_score.setProgress(getScore((int) (ParseUtil.parseFloat(beHomeWorkDetails.getCorrect_rate()) * 100)));
                        /*显示完成后的答题卡情况*/
                        mLinscoreState.setVisibility(View.VISIBLE);
                        linhomework_detail.setVisibility(View.VISIBLE);
                        mBeAnswerSheetList.addAll(beHomeWorkDetails.getAnswer_sheet());
                        apHomeWorkDetail.notifyDataSetChanged();
                    } else if (beHomeWorkDetails.getIs_complete().equals("0")) {
                        setfxTtitle(beHomeWorkDetails.getName());

                        btn_dohomework.setText("继续做作业");

                        btn_dohomework.setText("开始做作业");
                        /*作业名称*/
                        tvhomeworkname.setText(beHomeWorkDetails.getName());
                        /*截止时间*/
                        homework_timename.setText("截止时间");
                        tv_time.setText(DateUtils.EndHomeWorktime(beHomeWorkDetails.getEnd_time()));
                        /*完成人数*/
                        tvcompletecount.setText(beHomeWorkDetails.getComplete_students() + "/" + beHomeWorkDetails.getAll_students());
                    }
                }

            } else {
                ToastUtil.showToast(HomeWorkDetailsActivity.this, json.getMsg());
            }


        }
    };


    /*评分算法 20分为一颗星*/
    private int getScore(int score) {
        if (0 == score) {
            return 0;
        } else if (0 < score && score <= 20) {
            return 20;
        } else if (20 < score && score <= 40) {
            return 40;
        } else if (40 < score && score <= 60) {
            return 60;
        } else if (60 < score && score <= 80) {
            return 80;
        } else if (80 < score && score <= 100) {
            return 100;
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*上传答案之后刷新数据*/
        if (requestCode == DjhJumpUtil.getInstance().activtiy_DoHomework && resultCode == DjhJumpUtil.getInstance().activity_answerCardSubmit_dohomework) {
            httpData();
        }

    }

    /*监听返回键*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //监控/拦截/屏蔽返回键
            setResult(  DjhJumpUtil.getInstance().activtiy_HomeworkDetails_back );
            finishActivity();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /*左上角返回*/
    @Override
    public void onBackPressed() {
        setResult( DjhJumpUtil.getInstance().activtiy_HomeworkDetails_back );
        finishActivity();
    }
}
