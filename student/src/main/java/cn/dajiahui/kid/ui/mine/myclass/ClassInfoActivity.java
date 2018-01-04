package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.util.DjhJumpUtil;


/*
* 班级信息
* */
public class ClassInfoActivity extends FxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_class_info);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_class_info);

        TextView mClass = getView(R.id.tv_class);
        TextView mCode = getView(R.id.tv_code);
        TextView mCount = getView(R.id.tv_count);
        ImageView mHead = getView(R.id.img_head);
        Button mExitclass = getView(R.id.btn_exitclass);
        TextView mClassspace = getView(R.id.tv_classspace);

        mClassspace.setOnClickListener(onClick);
        mExitclass.setOnClickListener(onClick);


    }
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tool_right:
                    finishActivity();
                    break;
                case R.id.tv_classspace:
                    DjhJumpUtil.getInstance().startBaseActivity(ClassInfoActivity.this, ClassSpaceActivity.class);

                    break;
                case R.id.btn_exitclass:
                    Toast.makeText(context, "退出班级", Toast.LENGTH_SHORT).show();
                    break;


                default:
                    break;
            }
        }
    };
}
