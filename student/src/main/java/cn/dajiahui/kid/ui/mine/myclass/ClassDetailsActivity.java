package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.bean.BeClass;

/*
* 班级详情
* */
public class ClassDetailsActivity extends FxActivity {

    private TextView tvclasscode;
    private TextView tvschool;
    private TextView tvaddclass;
    private TextView tvclassname;
    private TextView tvteacher;
    private String classCode;
    private BeClass classInfo;
    private String classID;
    private LinearLayout re_task_second;
    private TextView mTvNUll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.class_details);
        onBackText();
        classCode = getIntent().getStringExtra("classCode");
        showfxDialog();
        httpData();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_class_details);
        initialize();
        tvaddclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpData();
//                Toast.makeText(context, "申请加入班级", Toast.LENGTH_SHORT).show();
                  /*添加班級后跳转页面待定*/
//                finishActivity();
            }
        });
    }

    /*初始化*/
    private void initialize() {
        tvaddclass = getView(R.id.tv_addclass);
        tvclasscode = getView(R.id.tv_classcode);
        tvschool = getView(R.id.tv_school);
        tvclassname = getView(R.id.tv_class_name);
        tvteacher = getView(R.id.tv_teacher);

        re_task_second = getView(R.id.re_task_second);
        mTvNUll = getView(R.id.tv_null);
    }


    @Override
    public void httpData() {
        //网络请求
        if (classID == null) {
            RequestUtill.getInstance().httpSearchClass(context, callSearch, classCode); // 根据班级码查询班级
        } else {
            if (!classID.equals("")) {
                RequestUtill.getInstance().httpApplyClass(context, callApply, classID); // 申请加入班级
            }
        }
    }

    /**
     * 查询班级callback函数
     */
    ResultCallback callSearch = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            Logger.d("班级详情失败：" + e);
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            Logger.d("班级详情：" + response);
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                /* 解析班级信息 */
                classInfo = json.parsingObject(BeClass.class);
                if (classInfo != null) {
                    switch (classInfo.getStatus()) {

                        case 0:
                            tvaddclass.setText("已申请");
                            break;
                        case 1:
                            tvaddclass.setText("加入班级");

                            break;
                        case 2:
                            tvaddclass.setText("待审核");
                            break;
                        case 3:
                            tvaddclass.setText("加入班级");

                            break;
                        case 5:
                            tvaddclass.setText("已经满班");
                            break;

                    }
                    re_task_second.setVisibility(View.VISIBLE);
                    tvclasscode.setText("班级码：" + classInfo.getCode());
                    tvschool.setText("学校：" + classInfo.getSchool_name());
                    tvclassname.setText(classInfo.getClass_name());
                    tvteacher.setText("老师：" + classInfo.getTeacher_name());

                    classID = classInfo.getId();
                }

            } else {
                ToastUtil.showToast(context, json.getMsg());
                finishActivity();
            }
        }
    };

    /**
     * 申请加入班级callback函数
     */
    ResultCallback callApply = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("申请加入班级：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
//                Toast.makeText(context, "申请加入班级", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "发送成功，等待老师确认！", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finishActivity();
            } else {
                ToastUtil.showToast(context, json.getMsg());
            }
        }
    };


    /*监听返回键*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //监控/拦截/屏蔽返回键
            setResult(RESULT_OK);
            finishActivity();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /*左上角返回*/
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finishActivity();
    }
}
