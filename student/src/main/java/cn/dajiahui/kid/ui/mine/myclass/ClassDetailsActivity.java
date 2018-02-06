package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
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
        setContentView(R.layout.activity_class_details2);
        initialize();
        tvaddclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpData();
//                Toast.makeText(context, "申请加入班级", Toast.LENGTH_SHORT).show();
//                finishActivity();
            }
        });
    }

    private void initialize() {
        tvclasscode = getView(R.id.tv_classcode);
        tvschool = getView(R.id.tv_school);
        tvaddclass = getView(R.id.tv_addclass);
        tvclassname = getView(R.id.tv_class_name);
        tvteacher = getView(R.id.tv_teacher);
    }

    @Override
    public void httpData() {
        //网络请求
        if (classID == null) {
            RequestUtill.getInstance().httpSearchClass(context, callSearch, classCode); // 根据班级码查询班级
        } else {
            if (!classID.equals("")) {
                RequestUtill.getInstance().httpApplyClass(context, callApply, classID); // 根据班级码查询班级
            }
        }
    }

    /**
     * 查询班级callback函数
     */
    ResultCallback callSearch = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                    /* 解析班级信息 */
                classInfo = json.parsingObject(BeClass.class);
                if (classInfo != null) {
                    tvclasscode.setText(classInfo.getCode());
                    tvschool.setText(classInfo.getSchool_name());
                    if (!classInfo.getIs_in_class().equals("0")) {
                        tvaddclass.setVisibility(View.INVISIBLE);
                    }
                    tvclassname.setText(classInfo.getClass_name());
                    tvteacher.setText(classInfo.getTeacher_name());
                    classID = classInfo.getId();
                }

            } else {
                ToastUtil.showToast(context, json.getMsg());
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
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                Toast.makeText(context, "申请加入班级", Toast.LENGTH_SHORT).show();
                finishActivity();
            } else {
                ToastUtil.showToast(context, json.getMsg());
            }
        }
    };
}
