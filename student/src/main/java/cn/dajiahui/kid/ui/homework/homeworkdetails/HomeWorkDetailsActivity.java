package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setfxTtitle(R.string.checkhomework);//12月22日作业
        onBackText();
        initialize();


        btn_dohomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("SourceFlag", "HomeWork");
                DjhJumpUtil.getInstance().startBaseActivity(HomeWorkDetailsActivity.this, DoHomeworkActivity.class,bundle,0);
                finishActivity();
            }
        });
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home_work_details);

    }

    /*初始化*/
    private void initialize() {
        tvhomeworkname = getView(R.id.tv_homeworkname);
        tvendtime = getView(R.id.tv_endtime);
        tvcompletecount = getView(R.id.tv_completecount);
        btn_dohomework = getView(R.id.btn_dohomework);
    }


}
