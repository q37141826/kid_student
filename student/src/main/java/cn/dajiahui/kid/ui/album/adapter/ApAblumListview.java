package cn.dajiahui.kid.ui.album.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.album.AlbumActivity;
import cn.dajiahui.kid.ui.album.bean.BeAlbum;
import cn.dajiahui.kid.ui.album.bean.BeClassAlbum;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by wdj on 2017/8/5.
 */

public class ApAblumListview extends CommonAdapter<BeClassAlbum> {
    public ApAblumListview(Context context, List<BeClassAlbum> mDatas) {
        super(context, mDatas, R.layout.album_item_class);
    }
    
    @Override
    public void convert(ViewHolder viewHolder, int position, BeClassAlbum item) {
        TextView tvTitle = viewHolder.getView(R.id.tvTitle);
        tvTitle.setText(item.getClassName());
        GridView gridView = viewHolder.getView(R.id.gridview);
        ApAlbumTitle adapter = new ApAlbumTitle(mContext, item.getList(), position);
        ((AlbumActivity) mContext).ablunList.add(adapter);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemClickListener);
    }
    
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BeAlbum album = mDatas.get((int) id).getList().get(position);
            DjhJumpUtil.getInstance().startPhotoActivity(mContext, album.getObjectId(), album.getName());
        }
    };
}
