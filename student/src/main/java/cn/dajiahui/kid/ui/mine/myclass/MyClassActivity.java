package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
* 我的班级
* */
public class MyClassActivity extends FxActivity {

    private LinearLayout mRoot;
    private TextView mClassname;
    private TextView mClasscode;
    private TextView mSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.my_class);
        onBackText();
        onRightBtn(R.drawable.ico_share, R.string.addclass);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_class);
        getView(R.id.lin_root).setOnClickListener(onClick);
        mClassname = getView(R.id.tv_classname);
        mClasscode = getView(R.id.tv_classcode);
        mSchool = getView(R.id.tv_classcode);


    }

    @Override
    public void onRightBtnClick(View view) {
        DjhJumpUtil.getInstance().startBaseActivity(context, AddClassActivity.class);

        Toast.makeText(context, "加入班级", Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lin_root:
                    DjhJumpUtil.getInstance().startBaseActivity(context,  ClassInfoActivity.class);
                    break;
                default:
                    break;
            }

        }
    };
}
