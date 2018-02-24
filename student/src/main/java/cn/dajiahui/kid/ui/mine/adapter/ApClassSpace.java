package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeClassSpaceList;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.view.NoSlideGrildView;


/**
 * 班级空间
 */
public class ApClassSpace extends CommonAdapter<BeClassSpaceList> {

    private NoSlideGrildView grildview;
    private Context context;

    public ApClassSpace(Context context, List<BeClassSpaceList> mDatas) {
        super(context, mDatas, R.layout.item_classspace);
        this.context = context;
    }


    @Override
    public void convert(ViewHolder viewHolder, final int position, BeClassSpaceList item) {


        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_endtime = viewHolder.getView(R.id.tv_endtime);
        TextView tv_content = viewHolder.getView(R.id.tv_content);
        grildview = viewHolder.getView(R.id.grildview);

        ApClassSpacepicture apClassSpacepicture = new ApClassSpacepicture(this.context, item.getImg_url());

        grildview.setAdapter(apClassSpacepicture);

        tv_classname.setText(item.getClass_name() + "班动态");
        tv_endtime.setText("发表于：" + DateUtils.timeHour(item.getCreated_at()));
        tv_content.setText(item.getContent());
    }
}
