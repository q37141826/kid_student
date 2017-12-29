package com.fxtx.framework.image.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.R;
import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.ImageFloder;
import com.fxtx.framework.image.util.GlideUtil;

import java.util.List;

/**
 * 相册列表类
 *
 * @author Administrator
 */
public class ImageFloderAdapter extends CommonAdapter<ImageFloder> {
    public ImageFloderAdapter(Context context, List<ImageFloder> mDatas) {
	super(context, mDatas, R.layout.list_dir_item);
    }

    public void changeData(List<ImageFloder> list) {
	this.mDatas = list;
	notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, ImageFloder item) {
	ImageView dirItemIcon = viewHolder.getView(R.id.id_dir_choose);
	ImageView dirItemImage = viewHolder.getView(R.id.id_dir_item_image);
	TextView dirItemName = viewHolder.getView(R.id.id_dir_item_name);
	TextView dirItemNum = viewHolder.getView(R.id.id_dir_item_count);
	GlideUtil.showImageFile(mContext, item.getFirstImagePath(), dirItemImage, R.drawable.pictures_no);
	dirItemName.setText(item.getName());
	dirItemNum.setText(item.getCount() + "张");
	if (item.isSelected())
	    dirItemIcon.setVisibility(View.VISIBLE);
	else
	    dirItemIcon.setVisibility(View.INVISIBLE);
    }
}
