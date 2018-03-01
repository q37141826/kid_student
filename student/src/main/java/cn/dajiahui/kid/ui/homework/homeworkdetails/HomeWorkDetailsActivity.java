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
    private String unit_name;
    private String is_complete;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_work_details);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String starttime = bundle.getString("starttime");
        unit_name = bundle.getString("UNIT_NAME");
        is_complete = bundle.getString("IS_COMPLETE");
        homework_id = bundle.getString("homework_id");
        setfxTtitle(DateUtils.time(starttime));
        onBackText();
        initialize();
        if (is_complete.equals("0")) {
            btn_dohomework.setText("继续做作业");
        } else {
            btn_dohomework.setText("开始做作业");
        }

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
//        JSONArray jsonArray = headJson.getObject().getJSONObject("data").getJSONArray("question_list");
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
                tvhomeworkname.setText(beHomeWorkDetails.getName());
                tvendtime.setText(DateUtils.time(beHomeWorkDetails.getEnd_time()));
                tvcompletecount.setText(beHomeWorkDetails.getComplete_students() + "/" + beHomeWorkDetails.getAll_students());

            } else {
                ToastUtil.showToast(HomeWorkDetailsActivity.this, json.getMsg());
            }
        }
    };


}
