package cn.dajiahui.kid.ui.task.adapter;

import android.content.Context;
import android.widget.AbsListView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.widgets.badge.BadgeTextView;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.task.bean.BeCommissionHori;

/**
 * Created by wdj on 2016/4/1.
 */
public class ApHorUndoneList extends CommonAdapter<BeCommissionHori> {
    private AbsListView.LayoutParams params;

    public ApHorUndoneList(Context context, List<BeCommissionHori> datas, int width) {
        super(context, datas, R.layout.item_hor_undone);
        params = new AbsListView.LayoutParams(width / 5 - 10, width / 5 - 10);
    }

    public void onRefreshList(List<BeCommissionHori> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    @Override

    public int getCount() {
        return mDatas != null ? (mDatas.size() > 5 ? 5 : mDatas.size()) : 0;
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeCommissionHori item) {
        BadgeTextView text = viewHolder.getConvertView();
        text.setLayoutParams(params);
        text.setText(item.getTypeName());
        text.showTextBadge(item.getCount() + "");
    }
}
