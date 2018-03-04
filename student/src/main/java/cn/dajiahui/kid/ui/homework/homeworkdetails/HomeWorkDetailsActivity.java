package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
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
import cn.dajiahui.kid.util.Logger;

/*
*
*做作业初级界面-
*
* */
public class HomeWorkDetailsActivity extends FxActivity {

    private TextView tvhomeworkname;
    private TextView tvendtime;
    private TextView tvcompletecount;
    private LinearLayout line_complete, line_nocomplete, linhomework_detail;
    private TextView homework_time, tv_completetime, tv_correct_rate;
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
        String starttime = bundle.getString("starttime");
        unit_name = bundle.getString("UNIT_NAME");
        homework_id = bundle.getString("homework_id");
        setfxTtitle(DateUtils.time(starttime));

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
                DjhJumpUtil.getInstance().startBaseActivity(HomeWorkDetailsActivity.this, DoHomeworkActivity.class, bundle, 0);
                finishActivity();
            }
        });
    }


    /*初始化*/
    private void initialize() {
        linhomework_detail = getView(R.id.linhomework_detail);
        line_nocomplete = getView(R.id.line_nocomplete);
        line_complete = getView(R.id.line_complete);
        tvhomeworkname = getView(R.id.tv_homeworkname);
        tvendtime = getView(R.id.tv_endtime);
        tvcompletecount = getView(R.id.tv_completecount);
        btn_dohomework = getView(R.id.btn_dohomework);
        grildview = getView(R.id.grildview);
        homework_time = getView(R.id.homework_time);
        tv_completetime = getView(R.id.tv_completetime);
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
                        btn_dohomework.setText("开始做作业");
                        showHomeworkDetail(beHomeWorkDetails);
                    } else if (beHomeWorkDetails.getIs_complete().equals("1")) {
                        setfxTtitle("作业详情");
                        line_complete.setVisibility(View.VISIBLE);
                        linhomework_detail.setVisibility(View.VISIBLE);
                        homework_time.setText(DateUtils.time(beHomeWorkDetails.getStart_time()) + "作业");//作业时间
                        tv_completetime.setText("完成时间：" + DateUtils.time(beHomeWorkDetails.getComplete_time()));//完成时间
                        tv_correct_rate.setText("正确率：" + beHomeWorkDetails.getCorrect_rate() + "%");//正确率
                        rb_score.setMax(100);

                        /*小星星打分 分数有问题*/
//                        rb_score.setProgress(Integer.parseInt(beHomeWorkDetails.getCorrect_rate())*100);

                        mBeAnswerSheetList.addAll(beHomeWorkDetails.getAnswer_sheet());
                        btn_dohomework.setText("查看作业");
                        apHomeWorkDetail.notifyDataSetChanged();
                    } else if (beHomeWorkDetails.getIs_complete().equals("0")) {
                        btn_dohomework.setText("继续做作业");
                        showHomeworkDetail(beHomeWorkDetails);
                    }
                }

            } else {
                ToastUtil.showToast(HomeWorkDetailsActivity.this, json.getMsg());
            }


        }
    };

    private void showHomeworkDetail(BeHomeWorkDetails beHomeWorkDetails) {
        line_nocomplete.setVisibility(View.VISIBLE);
        tvhomeworkname.setText(beHomeWorkDetails.getName());
        tvendtime.setText(DateUtils.time(beHomeWorkDetails.getEnd_time()));
        tvcompletecount.setText(beHomeWorkDetails.getComplete_students() + "/" + beHomeWorkDetails.getAll_students());
    }
}
