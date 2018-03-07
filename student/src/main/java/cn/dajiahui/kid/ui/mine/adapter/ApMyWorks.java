package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorksLists;

/**
 * Created by majin on 2016/5/11.
 * 我的作品
 */
public class ApMyWorks extends CommonAdapter<BeMineWorksLists> {
    int selectorPosition = -3;
    public CheckBox checkBox;

    private Context context;

    public ApMyWorks(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_myworks);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder viewHolder, final int position, final BeMineWorksLists item) {
        checkBox = viewHolder.getView(R.id.checkbox);
        ImageView img = viewHolder.getView(R.id.img);
        TextView tv_worksname = viewHolder.getView(R.id.tv_worksname);
        TextView tv_workstime = viewHolder.getView(R.id.tv_workstime);

        GlideUtil.showNoneImage(mContext, item.getThumbnail(), img);
        tv_worksname.setText(item.getTitle());
//        tv_workstime.setText(DateUtils.time(item.getDate()));
        //显示checkBox
        checkBox.setChecked(mDatas.get(position).getBo());
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == -1) {
            checkBox.setVisibility(View.VISIBLE);
        } else if (selectorPosition == -2) {
            checkBox.setVisibility(View.GONE);
        }


    }


    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();
    }


}
