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
    private String classCode;

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
            classCode = edCode.getText().toString().trim();
            if (StringUtil.isEmpty(classCode)) {
                ToastUtil.showToast(context, R.string.input_code);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("classCode", classCode);
            DjhJumpUtil.getInstance().startBaseActivity(context, ClassDetailsActivity.class, bundle, 0);
            finishActivity();
        }
    };
}
