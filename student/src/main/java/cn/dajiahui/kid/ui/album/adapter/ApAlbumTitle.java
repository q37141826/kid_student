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
 * Created by z on 2016/3/11.
 * 带标题的 相册展示
 */
public class ApAlbumTitle extends CommonAdapter<BeAlbum> {
    private int index;

    public ApAlbumTitle(Context context, List<BeAlbum> mDatas, int index) {
        super(context, mDatas, R.layout.album_item_title);
        this.index = index;

    }

    @Override
    public long getItemId(int position) {
        return index;
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeAlbum item) {
        TextView tvTitle = viewHolder.getView(R.id.tvTitle);
        ImageView image = viewHolder.getView(R.id.imAlbum);
        image.setBackgroundResource(R.color.white);
        tvTitle.setText(item.getName());
        GlideUtil.showNoneImage(mContext, item.getCoverUrl(), image, R.drawable.ico_default, true);
    }
}
