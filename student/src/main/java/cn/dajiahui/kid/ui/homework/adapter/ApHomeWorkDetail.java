package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeAnswerSheet;

/**
 * 作业详情
 */
public class ApHomeWorkDetail extends CommonAdapter<BeAnswerSheet> {

    public ApHomeWorkDetail(Context context, List<BeAnswerSheet> mDatas) {
        super(context, mDatas, R.layout.item_answercard);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(ViewHolder viewHolder, int position, BeAnswerSheet item) {
        TextView tv_circle = viewHolder.getView(R.id.tv_circle);
        tv_circle.setText((position + 1) + "");
        /*首先判断是否答过题*/
        if (item.getIs_answered().equals("1")) {//自己作答
            if (item.getIs_right().equals("1")) {//1是正确 0是错误
                tv_circle.setBackgroundResource(R.drawable.homewor_true_bg_green);
            } else {
                tv_circle.setBackgroundResource(R.drawable.homewor_false_bg_red);
            }
        } else {
            /*未作答*/
            tv_circle.setBackgroundResource(R.drawable.answer_card_noanswer_bg);
        }


    }
}