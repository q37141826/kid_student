package cn.dajiahui.kid.ui.homework.adapter;

import android.content.Context;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;

/**
 * 作业列表
 */
public class ApHomework extends CommonAdapter<BeHomework> {
    public ApHomework(Context context, List<BeHomework> mDatas) {
        super(context, mDatas, R.layout.item_homework);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeHomework item) {
        TextView tv_hometime = viewHolder.getView(R.id.tv_hometime);
        TextView task_endtime = viewHolder.getView(R.id.task_endtime);
        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_homename = viewHolder.getView(R.id.tv_homename);
        TextView tv_homecontent = viewHolder.getView(R.id.tv_homecontent);
        TextView tv_flag = viewHolder.getView(R.id.tv_flag);
        RatingBar rb_score = viewHolder.getView(R.id.rb_score);

        tv_hometime.setText(item.getHometime());
        task_endtime.setText(item.getEndtime());
        tv_classname.setText(item.getClass_name());
        tv_homename.setText(item.getHomename());
        tv_homecontent.setText(item.getHomecontent());
        tv_flag.setText(item.getHomeflag());
    }
}