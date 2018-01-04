package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;

/*
* 班级详情
* */
public class ClassDetailsActivity extends FxActivity {

    private TextView tvclasscode;
    private TextView tvschool;
    private TextView tvaddclass;
    private TextView tvclassname;
    private TextView tvteacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.class_details);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_class_details2);
        initialize();
        tvaddclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "申请加入班级", Toast.LENGTH_SHORT).show();
                finishActivity();
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
}
