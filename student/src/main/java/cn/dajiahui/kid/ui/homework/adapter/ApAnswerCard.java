package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BaseBean;

/**
 * 作业列表
 */
public class ApAnswerCard extends CommonAdapter<BaseBean> {
    public ApAnswerCard(Context context, List<BaseBean> mDatas) {
        super(context, mDatas, R.layout.item_answercard);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(ViewHolder viewHolder, int position, BaseBean item) {
        TextView tv_circle = viewHolder.getView(R.id.tv_circle);

        tv_circle.setText(item.getNomber());

        if (item.getTrueAnswer().equals(item.getAnswer())) {
            tv_circle.setTextColor(R.color.white);
            tv_circle.setBackgroundResource(R.drawable.gb);
        }
        if (!item.getTrueAnswer().equals(item.getAnswer())) {
            tv_circle.setTextColor(R.color.white);
            tv_circle.setBackgroundResource(R.drawable.rb);
        }
        if (item.getAnswerflag() == false){
            tv_circle.setTextColor(R.color.black);
            tv_circle.setBackgroundResource(R.drawable.wb);
        }

    }
}