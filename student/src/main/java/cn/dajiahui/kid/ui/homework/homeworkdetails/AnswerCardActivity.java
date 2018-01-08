package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BaseBean;
import cn.dajiahui.kid.ui.homework.bean.BeSerializableMap;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
* 答题卡
* */
public class AnswerCardActivity extends FxActivity {
    private int ANSWERCARD = 3;
    private TextView tvtrue;
    private TextView tvfalse;
    private TextView tvnoanswer;
    private TextView tvanswer;
    private GridView grildview;
    private Button mBtnsubmit;
    private int answernum;
    private List<BaseBean> listdata;
    private BeSerializableMap answerCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.AnswerCard);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_answer_card);
        initialize();

        Intent intent = getIntent();
        answernum = intent.getIntExtra("answerNum", 0);
        //取所有check过的数据
        answerCard = (BeSerializableMap) intent.getSerializableExtra("answerCard");
        listdata = answerCard.getData();

        tvanswer.setText(answernum + "/" + listdata.size());
        ApAnswerCard apAnswerCard = new ApAnswerCard(context, listdata);
        grildview.setAdapter(apAnswerCard);

    }

    private void initialize() {
        tvtrue = getView(R.id.tv_true);
        tvfalse = getView(R.id.tv_false);
        tvnoanswer = getView(R.id.tv_noanswer);
        tvanswer = getView(R.id.tv_answer);
        grildview = getView(R.id.grildview);
        mBtnsubmit = getView(R.id.btn_submit);
        mBtnsubmit.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("answerCard", answerCard);

                    DjhJumpUtil.getInstance().startBaseActivity(AnswerCardActivity.this, HomedetailsActivity.class, bundle, ANSWERCARD);
                    ActivityUtil.getInstance().finishActivity(CheckHomeworkActivity.class);//结束指定的activity
                    finishActivity();
                    break;

                default:
                    break;
            }

        }
    };
}
