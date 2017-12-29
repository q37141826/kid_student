package cn.dajiahui.kid.ui.notice.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.time.TimeUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.notice.bean.BeNotice;

/**
 * Created by z on 2016/3/7.
 * 通知对象
 */
public class ApNotice extends CommonAdapter<BeNotice> {
    public ApNotice(Context context, List<BeNotice> mDatas) {
        super(context, mDatas, R.layout.notice_item);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeNotice item) {
        TextView type = viewHolder.getView(R.id.tvType);
        TextView tvTitle = viewHolder.getView(R.id.tv_title);
        TextView tvMsg = viewHolder.getView(R.id.tv_msg);
        TextView tvTime = viewHolder.getView(R.id.tv_time);
        tvTime.setText(TimeUtil.timeFormat(item.getAddTime(), TimeUtil.yyMD));
        tvTitle.setText(item.getTitle());
        tvMsg.setText(item.getContent());
        switch (item.getType()) {
            case Constant.systemNotice:
                type.setText(R.string.system_notice);
                break;
            case Constant.schoolNotice:
                type.setText(R.string.school_notice);
                break;
            case Constant.classNotice:
                //班级通知
                type.setText(R.string.class_notice);
                break;
        }
    }
}
