package cn.dajiahui.kid.ui.homework.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeCommission;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ApUndoneList extends CommonAdapter<BeCommission> {
    public ApUndoneList(Context context, List<BeCommission> mDatas) {
        super(context, mDatas, R.layout.list_undone_item);
    }

    public void onRefreshList(List<BeCommission> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeCommission item) {
        TextView tv_title = viewHolder.getView(R.id.tv_undone_item_title);
        TextView tv_type = viewHolder.getView(R.id.undone_item_type);
        tv_title.setText(item.getTitle());
        tv_type.setText(item.getTypeName());
    }
}