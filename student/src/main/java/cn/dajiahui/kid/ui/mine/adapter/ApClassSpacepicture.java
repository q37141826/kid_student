package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;

import java.util.List;

import cn.dajiahui.kid.R;


/**
 * 我的班级的图片的适配器
 */
public class ApClassSpacepicture extends CommonAdapter<String> {

    public ApClassSpacepicture(Context context, List<String> mDatas) {
        super(context, mDatas, R.layout.item_classspacepicture);
    }


    @Override
    public void convert(ViewHolder viewHolder, final int position, String item) {

        ImageView item_picture = viewHolder.getView(R.id.item_picture);

        GlideUtil.showNoneImage(mContext, mDatas.get(position), item_picture);
    }
}
