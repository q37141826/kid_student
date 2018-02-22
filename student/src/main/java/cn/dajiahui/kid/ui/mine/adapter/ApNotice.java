package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeNotice;


/**
 * 等待加入班级
 */
public class ApNotice extends CommonAdapter<BeNotice> {


    public ApNotice(Context context, List<BeNotice> mDatas) {
        super(context, mDatas, R.layout.item_notice);
    }


    @Override
    public void convert(ViewHolder viewHolder, int position, BeNotice item) {

        TextView tv_updatecontent = viewHolder.getView(R.id.tv_updatecontent);
        TextView tv_deadline = viewHolder.getView(R.id.tv_deadline);
        TextView tv_redpoint = viewHolder.getView(R.id.tv_redpoint);

//        if (position == 0) {
//            tv_redpoint.setVisibility(View.VISIBLE);
//        }
        tv_updatecontent.setText(item.getUpdateContent());
        tv_deadline.setText(item.getDeadline());


    }


}
