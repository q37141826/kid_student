package cn.dajiahui.kid.ui.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.time.TimeUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeNoticeLists;


/**
 * 通知
 */
public class ApNotice extends CommonAdapter<BeNoticeLists> {


    public ApNotice(Context context, List<BeNoticeLists> mDatas) {
        super(context, mDatas, R.layout.item_notice);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void convert(ViewHolder viewHolder, int position, BeNoticeLists item) {

        TextView tv_updatecontent = viewHolder.getView(R.id.tv_updatecontent);
        TextView tv_deadline = viewHolder.getView(R.id.tv_deadline);
        TextView tv_redpoint = viewHolder.getView(R.id.tv_redpoint);

        if (mDatas.get(position).getIs_read().equals("0")) {
            tv_redpoint.setVisibility(View.VISIBLE);
        } else {
            tv_redpoint.setVisibility(View.INVISIBLE);
            tv_updatecontent.setTextColor(mContext.getResources().getColor(R.color.gray_666666));
            tv_deadline.setTextColor(mContext.getResources().getColor(R.color.gray_666666));
        }

        tv_updatecontent.setText(item.getTitle());
        tv_deadline.setText(TimeUtil.stampToString(item.getCreated_at(), TimeUtil.YYYYMD));


    }

}

