package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;

/**
 * 作业列表
 */
public class ApAnswerCard extends CommonAdapter<QuestionModle> {

    public ApAnswerCard(Context context, List<QuestionModle> mDatas, int allNum) {
        super(context, mDatas, R.layout.item_answercard);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(ViewHolder viewHolder, int position, QuestionModle item) {
        TextView tv_circle = viewHolder.getView(R.id.tv_circle);

        tv_circle.setText(((item.getEachposition() + 1) + ""));

        if (item.getAnswerflag().equals("true")) {
            tv_circle.setBackgroundResource(R.drawable.answer_card_yesanswer_bg);

        } else {
            tv_circle.setBackgroundResource(R.drawable.answer_card_noanswer_bg);
        }
    }
}