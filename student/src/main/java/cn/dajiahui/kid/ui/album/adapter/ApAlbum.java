package cn.dajiahui.kid.ui.album.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.album.bean.BeAlbum;

/**
 * Created by z on 2016/3/8.
 * 相册适配器 主要是FrAlbum中的适配
 */
public class ApAlbum extends CommonAdapter<BeAlbum> {

    public ApAlbum(Context context, List<BeAlbum> mDatas) {
        super(context, mDatas, R.layout.album_item_fr_class);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeAlbum item) {
        ImageView img = viewHolder.getView(R.id.imAlbum);
        TextView albumName = viewHolder.getView(R.id.tvTitle);
        GlideUtil.showNoneImage(mContext, item.getCoverUrl(), img, R.drawable.ico_default, true);
        albumName.setText(item.getName());
    }
}
