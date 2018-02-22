package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeClassSpace;


/**
 * 班级空间
 */
public class ApClassSpace extends CommonAdapter<BeClassSpace> {

    public ApClassSpace(Context context, List<BeClassSpace> mDatas) {
        super(context, mDatas, R.layout.item_classspace);
    }


    @Override
    public void convert(ViewHolder viewHolder, final int position, BeClassSpace item) {

        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_endtime = viewHolder.getView(R.id.tv_endtime);
        TextView tv_content = viewHolder.getView(R.id.tv_content);

        ImageView img_1 = viewHolder.getView(R.id.img_1);
        ImageView img_2 = viewHolder.getView(R.id.img_2);
        ImageView img_3 = viewHolder.getView(R.id.img_3);

        tv_classname.setText(item.getMyclass());
        tv_endtime.setText(item.getMytime());
        tv_content.setText(item.getComment());
    }
}
