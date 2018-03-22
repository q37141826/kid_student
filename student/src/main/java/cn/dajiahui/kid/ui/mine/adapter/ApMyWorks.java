package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.log.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorksLists;
import cn.dajiahui.kid.util.DateUtils;

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
        Logger.d("作品时间----item.getDate()："+item.getDate());
        tv_workstime.setText("作品时间：" + DateUtils.getYyyyMMDD(item.getDate()+"000"));

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

    public String getStrTime(String cc_time) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;
    }
}
