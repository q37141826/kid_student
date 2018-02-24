package cn.dajiahui.kid.ui.study.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterialInfoLists;


/**
 * 选择教材详情页面
 */
public class ApTeachingMaterialIfo extends CommonAdapter<BeChoiceTeachingMaterialInfoLists> {

    private Context context;

    public ApTeachingMaterialIfo(Context context, List<BeChoiceTeachingMaterialInfoLists> mDatas) {
        super(context, mDatas, R.layout.item_teachingmaterialinfo);

        this.context = context;
    }


    @Override
    public void convert(ViewHolder viewHolder, int position, BeChoiceTeachingMaterialInfoLists item) {
        ImageView img_book = viewHolder.getView(R.id.img_book);
        TextView tv_unitname = viewHolder.getView(R.id.tv_unitname);
        TextView tv_state = viewHolder.getView(R.id.tv_state);

        GlideUtil.showNoneImage(mContext, item.getLogo(), img_book);
        tv_unitname.setText(item.getName());

    }
}
