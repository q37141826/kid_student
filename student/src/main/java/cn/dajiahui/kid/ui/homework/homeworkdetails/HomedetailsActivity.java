package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
*作业详情
* */
public class HomedetailsActivity extends FxActivity {

    private TextView tvhometime;
    private RatingBar rbscore;
    private TextView tvcorrectrate;
    private TextView tvcompletiontime;
    private Button mBtnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.homeworkdetails);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_homedetails);
        initialize();

        mBtnCheck.setOnClickListener(onClick);

    }

    private void initialize() {

        tvhometime = getView(R.id.tv_hometime);
        rbscore = (RatingBar) findViewById(R.id.rb_score);
        tvcorrectrate = getView(R.id.tv_correctrate);
        tvcompletiontime = getView(R.id.tv_completiontime);
        mBtnCheck = getView(R.id.btn_checkhomework);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_checkhomework:
                    DjhJumpUtil.getInstance().startBaseActivity(HomedetailsActivity.this, CheckHomeworkActivity.class);

                default:
                    break;
            }

        }
    };
}
