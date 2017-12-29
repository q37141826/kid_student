package cn.dajiahui.kid.ui.album.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.album.bean.BePhoto;

/**
 * Created by z on 2016/3/10.
 */
public class ApPhoto extends CommonAdapter<BePhoto> {
    public ApPhoto(Context context, List<BePhoto> mDatas) {
        super(context, mDatas, R.layout.album_photo_item);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BePhoto item) {
        ImageView view = viewHolder.getConvertView();
        view.setBackgroundResource(R.color.white);
        GlideUtil.showNoneImage(mContext, item.getThumbUrl(), view, R.drawable.ico_default, false);

    }
}
