package cn.dajiahui.kid.ui.homework.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.text.ParseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;
import cn.dajiahui.kid.util.DateUtils;

/**
 * 作业列表
 */
public class ApHomework extends CommonAdapter<BeHomework> {

    private Context context;
    public List<Integer> positionList = new ArrayList<>();

    public List<Integer> getPositionList() {
        return positionList;
    }

    public ApHomework(Context context, List<BeHomework> mDatas) {
        super(context, mDatas, R.layout.item_homework);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder viewHolder, final int position, final BeHomework item) {
        TextView tv_hometime = viewHolder.getView(R.id.tv_hometime);
        TextView task_endtime = viewHolder.getView(R.id.task_endtime);
        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_homename = viewHolder.getView(R.id.tv_homename);//书名
        TextView tv_homecontent = viewHolder.getView(R.id.tv_homecontent);//教材名字
        TextView tv_dohomework = viewHolder.getView(R.id.tv_flag);
        RatingBar rb_score = viewHolder.getView(R.id.rb_score);

        rb_score.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        tv_classname.setText(item.getClass_name());
        tv_homename.setText(item.getBook_name());
        tv_homecontent.setText(item.getName());

        /*打分的小星星*/
        rb_score.setMax(100);
        /*打分的分数 */
        rb_score.setProgress(getScore((int) (ParseUtil.parseFloat(item.getCorrect_rate()) * 100)));
        tv_hometime.setText(DateUtils.time(item.getStart_time()) + "作业");//作业时间
        task_endtime.setText("截止时间：" + DateUtils.time(item.getEnd_time()));//作业时间

        if (TimeCompare(item)) {
            if (item.getIs_checked().equals("0")) {
                tv_dohomework.setText("做作业");
            } else {
                tv_dohomework.setText("查看");
            }
            tv_dohomework.setBackgroundResource(R.drawable.round_bgyellow_homwwork);

        } else {
            tv_dohomework.setText("已过期");
            tv_dohomework.setBackgroundResource(R.drawable.round_bggray_homwwork);

            positionList.add(position);
        }


    }

    /*比较时间*/
    private boolean TimeCompare(BeHomework item) {
        try {
            //格式化时间
            SimpleDateFormat mCurrentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            String date1 = mCurrentTime.format(new Date());
            int i = Integer.parseInt(item.getEnd_time());
            String date2 = mCurrentTime.format(new Date(i * 1000L));

            Date beginTime = mCurrentTime.parse(date1);
            Date endTime = mCurrentTime.parse(date2);
            //判断是否大于0天
            if (((endTime.getTime() - beginTime.getTime()) / (24 * 60 * 60 * 1000)) >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    /*评分算法 20分为一颗星*/
    private int getScore(int score) {
        if (0 == score) {
            return 0;
        } else if (0 < score && score <= 20) {
            return 20;
        } else if (20 < score && score <= 40) {
            return 40;
        } else if (40 < score && score <= 60) {
            return 60;
        } else if (60 < score && score <= 80) {
            return 80;
        } else if (80 < score && score <= 100) {
            return 100;
        }
        return 0;
    }

}