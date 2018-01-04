package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.util.DjhJumpUtil;


/**
 * 添加班级
 */
public class AddClassActivity extends FxActivity {
    private EditText edCode;
    private TextView tvSearch;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_class);
        edCode = getView(R.id.ed_code);
        tvSearch = getView(R.id.tv_search);
        tvSearch.setOnClickListener(onclick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        setfxTtitle(R.string.addclass);
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String classCode = edCode.getText().toString().trim();
            if (StringUtil.isEmpty(classCode)) {
                ToastUtil.showToast(context, R.string.input_code);
                return;
            }
            httpAddClass(classCode);
        }
    };

    public void httpAddClass(String classNo) {

        DjhJumpUtil.getInstance().startBaseActivity(context,  ClassDetailsActivity.class);
        finishActivity();
//        showfxDialog();
//        ResultCallback callback = new ResultCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//                dismissfxDialog();
//                ToastUtil.showToast(context, ErrorCode.error(e));
//            }
//
//            @Override
//            public void onResponse(String response) {
//                dismissfxDialog();
//                HeadJson json = new HeadJson(response);
//                if (json.getstatus()  == 1) {
//                    //跳转到我的班级界面
////                    DjhJumpUtil.getInstance().startBaseActivity(context, AuditListActivity.class);
//                    ActivityUtil.getInstance().finishActivity(AddClassActivity.class);
////                    ActivityUtil.getInstance().finishActivity(AuditListActivity.class);
//                } else {
//                    ToastUtil.showToast(context, json.getMsg());
//                }
//            }
//        };
//        RequestUtill.getInstance().httpAddClassAply(context, UserController.getInstance().getUserId(), classNo, callback);
    }
}
