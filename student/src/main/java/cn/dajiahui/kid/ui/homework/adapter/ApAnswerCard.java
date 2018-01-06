package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeAnswerCard;

/**
 * 作业列表
 */
public class ApAnswerCard extends CommonAdapter<BeAnswerCard> {
    public ApAnswerCard(Context context, List<BeAnswerCard> mDatas) {
        super(context, mDatas, R.layout.item_answercard);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(ViewHolder viewHolder, int position, BeAnswerCard item) {
        TextView tv_circle = viewHolder.getView(R.id.tv_circle);
        tv_circle.setText(item.getanswercurrentnum() + "");
        if (item.getAnswertrue() == 1) {

            tv_circle.setTextColor(R.color.white);
            tv_circle.setBackgroundResource(R.drawable.gb);
        } else if (item.getAnswertrue() == 2) {
            tv_circle.setTextColor(R.color.white);
            tv_circle.setBackgroundResource(R.drawable.rb);
        } else if (item.getAnswertrue() == 3) {
            tv_circle.setTextColor(R.color.black);
            tv_circle.setBackgroundResource(R.drawable.wb);
        }
    }
}