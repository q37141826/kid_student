package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeHelp;

/**
 * Created by Mjj on 2016/5/11.
 */
public class ApHelp extends CommonAdapter<BeHelp> {

    public ApHelp(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_help);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeHelp item) {
        TextView textView = viewHolder.getView(R.id.tv_help);
        textView.setText(item.getCmsName());
    }
}
