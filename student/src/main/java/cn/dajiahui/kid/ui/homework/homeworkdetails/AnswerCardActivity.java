package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BaseBean;
import cn.dajiahui.kid.ui.homework.bean.BeSerializableMap;
import cn.dajiahui.kid.util.Logger;

/*
* 答题卡
* */
public class AnswerCardActivity extends FxActivity {

    private TextView tvtrue;
    private TextView tvfalse;
    private TextView tvnoanswer;
    private TextView tvanswer;
    private GridView grildview;
    private ArrayList<Integer> pagelist;

    private Map<Integer, BaseBean> answerCardMap;
    //    private List<BeAnswerCard> list;
    private int answernum;
    private List<BaseBean> listdata;

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
        BeSerializableMap answerCard = (BeSerializableMap) intent.getSerializableExtra("answerCard");
        listdata = answerCard.getData();

        Logger.d("majin", "  集合 " + listdata.toString());

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
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

//                case
                default:
                    break;
            }

        }
    };
}
