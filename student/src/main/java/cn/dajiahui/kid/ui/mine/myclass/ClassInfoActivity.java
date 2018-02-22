package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.bean.BeClassDetail;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;


/*
* 班级信息
* 功能：1.进入班级空间 2.退出班级
* */
public class ClassInfoActivity extends FxActivity {

    private String classId;
    private TextView mCode;
    private TextView mCount;
    private TextView mTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle classInfobundle = this.getIntent().getExtras();
        classId = (String) classInfobundle.get("CLASSID");
        setfxTtitle((String) classInfobundle.get("CLASSNAME"));
        onBackText();
        onRightBtn(R.string.mine_class_space);
        httpClassDetail();
    }

    /*进入班级空间点击事件*/
    @Override
    public void onRightBtnClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("CLASSID", classId);
        DjhJumpUtil.getInstance().startBaseActivity(ClassInfoActivity.this, ClassSpaceActivity.class, bundle, 0);
    }

    /*班级详情*/
    private void httpClassDetail() {
        RequestUtill.getInstance().httpClassDetail(this, callClassDetail, classId);
    }

    /*退出班级*/
    private void httpQuitClass() {
        RequestUtill.getInstance().httpQuitClass(ClassInfoActivity.this, callQuitClass, classId);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_class_info);
        mCode = getView(R.id.tv_code);
        mCount = getView(R.id.tv_count);
        mTeacher = getView(R.id.tv_teacher);
        getView(R.id.btn_exitclass).setOnClickListener(onClick);

    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tool_right:
                    finishActivity();
                    break;
                case R.id.btn_exitclass:
                    /*退出班级需要网络请求  接口未给出 */
                    httpQuitClass();
                    break;
                default:
                    break;
            }
        }
    };


    /*班级详情*/
    ResultCallback callClassDetail = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {

            Logger.d("退出班级 ：" + response);

            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {


            } else {
                ToastUtil.showToast(ClassInfoActivity.this, json.getMsg());
            }

        }

    };


    /*退出班级*/
    ResultCallback callQuitClass = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {

            Logger.d("班级详情：" + response);

            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeClassDetail beClassDetail = json.parsingObject(BeClassDetail.class);
                mTeacher.setText("老师：" + beClassDetail.getTeacher_name());
                mCount.setText("人数：" + beClassDetail.getStudents_num());
                mCode.setText("班级码：" + beClassDetail.getCode());

            } else {
                ToastUtil.showToast(ClassInfoActivity.this, json.getMsg());
            }

        }

    };
}
