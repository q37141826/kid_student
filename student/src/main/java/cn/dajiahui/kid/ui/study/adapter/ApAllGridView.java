package cn.dajiahui.kid.ui.study.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.widgets.badge.Badgeable;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.BadgeController;
import cn.dajiahui.kid.ui.study.bean.BeFunction;
import cn.dajiahui.kid.util.StudentUtil;

/**
 * Created by z on 2016/2/22.
 * 全部功能数据适配器
 */
public class ApAllGridView extends CommonAdapter<BeFunction> {

    public ApAllGridView(Context context, List<BeFunction> mDatas) {
        super(context, mDatas, R.layout.fun_item_grid);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeFunction item) {
        ImageView image = viewHolder.getView(R.id.grid_im);
        TextView tv = viewHolder.getView(R.id.grid_tv);
        image.setImageResource(item.getImgId());
        tv.setText(item.getName());
        viewHolder.getConvertView().setBackgroundResource(StudentUtil.getDriverBg(position));
        StudentUtil.setBadge((Badgeable) viewHolder.getConvertView(), BadgeController.getInstance().getTypeBadge(item.getType()));
    }
}
