package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeNoticeLists;
import cn.dajiahui.kid.util.DateUtils;


/**
 * 通知
 */
public class ApNotice extends CommonAdapter<BeNoticeLists> {


    public ApNotice(Context context, List<BeNoticeLists> mDatas) {
        super(context, mDatas, R.layout.item_notice);
    }


    @Override
    public void convert(ViewHolder viewHolder, int position, BeNoticeLists item) {

        TextView tv_updatecontent = viewHolder.getView(R.id.tv_updatecontent);
        TextView tv_deadline = viewHolder.getView(R.id.tv_deadline);
        TextView tv_redpoint = viewHolder.getView(R.id.tv_redpoint);

        if (mDatas.get(position).getIs_read().equals("0")) {
            tv_redpoint.setVisibility(View.VISIBLE);
        } else {
            tv_redpoint.setVisibility(View.INVISIBLE);
        }

        tv_updatecontent.setText(item.getTitle());
        tv_deadline.setText(DateUtils.time(item.getCreated_at()));


    }


}
