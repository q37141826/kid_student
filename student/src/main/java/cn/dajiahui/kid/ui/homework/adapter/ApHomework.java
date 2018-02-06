package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;
import cn.dajiahui.kid.util.DateUtils;

/**
 * 作业列表
 */
public class ApHomework extends CommonAdapter<BeHomework> {

    public ApHomework(Context context, List<BeHomework> mDatas) {
        super(context, mDatas, R.layout.item_homework);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder viewHolder, int position, BeHomework item) {
        TextView tv_hometime = viewHolder.getView(R.id.tv_hometime);
        TextView task_endtime = viewHolder.getView(R.id.task_endtime);
        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_homename = viewHolder.getView(R.id.tv_homename);//书名
        TextView tv_homecontent = viewHolder.getView(R.id.tv_homecontent);//教材名字
        TextView tv_flag = viewHolder.getView(R.id.tv_flag);
        RatingBar rb_score = viewHolder.getView(R.id.rb_score);

        tv_classname.setText(item.getClass_name());
        tv_homename.setText(item.getBook_name());
        tv_homecontent.setText(item.getName());
//          tv_flag.setText(item.getName());
        rb_score.setMax(100);
        rb_score.setRating(20);

        tv_hometime.setText(DateUtils.time(item.getStart_time()) + "作业");//作业时间
        task_endtime.setText("截止时间：" + DateUtils.time(item.getEnd_time()));//作业时间


    }


}