package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeSerializableMap;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
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
    private List<QuestionModle> listdata;
    private BeSerializableMap answerCard;
    private GridView grildview;

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
        Intent intent = getIntent();
        int flags = intent.getFlags();

//        if (flags == 3) {
//            answerCard = (BeSerializableMap) intent.getSerializableExtra("answerCard");
//            listdata = answerCard.getData();
//            grildview.setVisibility(View.VISIBLE);
//            ApAnswerCard apAnswerCard = new ApAnswerCard(context, listdata);
//            grildview.setAdapter(apAnswerCard);
//            mBtnCheck.setText("在练一次");
//        }

        mBtnCheck.setOnClickListener(onClick);

    }

    private void initialize() {

        tvhometime = getView(R.id.tv_hometime);
        rbscore = (RatingBar) findViewById(R.id.rb_score);
        tvcorrectrate = getView(R.id.tv_correctrate);
        tvcompletiontime = getView(R.id.tv_completiontime);
        mBtnCheck = getView(R.id.btn_checkhomework);
        grildview = getView(R.id.grildview);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_checkhomework:
                    DjhJumpUtil.getInstance().startBaseActivity(HomedetailsActivity.this, CheckHomeworkActivity.class);
                    finishActivity();
                default:
                    break;
            }

        }
    };
}
