package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BeAnswerCard;

/*
* 答题卡
* */
public class AnswerCardActivity extends FxActivity {

    private TextView tvtrue;
    private TextView tvfalse;
    private TextView tvnoanswer;
    private TextView tvanswer;
    private GridView grildview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.AnswerCard);
        onBackText();
        initialize();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_answer_card);
        initialize();
        List<BeAnswerCard> list = new ArrayList<>();

        list.add(new BeAnswerCard(1, 1));
        list.add(new BeAnswerCard(2, 2));
        list.add(new BeAnswerCard(3, 3));
        list.add(new BeAnswerCard(2, 4));
        list.add(new BeAnswerCard(3, 5));
        list.add(new BeAnswerCard(1, 6));
        list.add(new BeAnswerCard(1, 7));
        list.add(new BeAnswerCard(2, 8));
        list.add(new BeAnswerCard(1, 9));
        list.add(new BeAnswerCard(1, 10));


        ApAnswerCard apAnswerCard = new ApAnswerCard(context, list);
        grildview.setAdapter(apAnswerCard);

    }

    private void initialize() {
        tvtrue = getView(R.id.tv_true);
        tvfalse = getView(R.id.tv_false);
        tvnoanswer = getView(R.id.tv_noanswer);
        tvanswer = getView(R.id.tv_answer);
        grildview = getView(R.id.grildview);
    }
}
