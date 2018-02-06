package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
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
    private Button btn_dohomework;
    private String homework_id;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_work_details);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String starttime = bundle.getString("starttime");
        setfxTtitle(DateUtils.time(starttime));//12月22日作业
        onBackText();
        initialize();

        homework_id = bundle.getString("homework_id");

        httpData();
        btn_dohomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestUtill.getInstance().httpGetStudentHomeWorkhomeworkContinue(HomeWorkDetailsActivity.this, callHomeWorkContinue, homework_id);
            }
        });
    }


    /*初始化*/
    private void initialize() {
        tvhomeworkname = getView(R.id.tv_homeworkname);
        tvendtime = getView(R.id.tv_endtime);
        tvcompletecount = getView(R.id.tv_completecount);
        btn_dohomework = getView(R.id.btn_dohomework);
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


            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeHomeWorkDetails beHomeWorkDetails = json.parsingObject(BeHomeWorkDetails.class);
                Logger.d("解析学生作业 :" + beHomeWorkDetails.toString());
                tvhomeworkname.setText(beHomeWorkDetails.getName());
                tvendtime.setText(DateUtils.time(beHomeWorkDetails.getEnd_time()));
//                Logger.d("beHomeWorkDetails.getCompelete_students():"+beHomeWorkDetails.getCompelete_students());
                tvcompletecount.setText(beHomeWorkDetails.getComplete_students() + "/" + beHomeWorkDetails.getAll_students());

            } else {
                ToastUtil.showToast(HomeWorkDetailsActivity.this, json.getMsg());
            }
        }
    };

    /**
     * 学生作业所有题callback函数
     */
    ResultCallback callHomeWorkContinue = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            Logger.d("callHomeWorkContinue  response :" + response.toString());
            if (json.getstatus() == 0) {

                Bundle bundle = new Bundle();
                bundle.putString("SourceFlag", "HomeWork");
                DjhJumpUtil.getInstance().startBaseActivity(HomeWorkDetailsActivity.this, DoHomeworkActivity.class, bundle, 0);
                finishActivity();
            } else {
                ToastUtil.showToast(HomeWorkDetailsActivity.this, json.getMsg());
            }
        }
    };
}
