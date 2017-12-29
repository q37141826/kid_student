package com.fxtx.framework.image.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fxtx.framework.R;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.log.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class GirdItemAdapter extends BaseAdapter {
    private final int VIEW_TYPE = 2;
    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private int max;

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static ArrayList<String> mSelectedImage = new ArrayList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    private Context context;

    private List<String> mDatas = new ArrayList<String>();//所有的图片


    public GirdItemAdapter(Context context, List<String> mDatas, String dirPath, int max) {
        super();
        this.context = context;
        this.mDatas = mDatas;
        this.mDirPath = dirPath;
        this.max = max;
    }

    public void changeData(List<String> mDatas, String dirPath) {
        this.mDatas = mDatas;
        this.mDirPath = dirPath;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mDatas.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == TYPE_1) {
            //Type1
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_item2, null);
                holder = new ViewHolder();
                holder.id_item_image = (ImageView) convertView.findViewById(R.id.grid2_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.id_item_image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhotoSelectedListener.takePhoto();
                }
            });
        } else {
            //Type2
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
                holder = new ViewHolder();
                holder.id_item_image = (ImageView) convertView.findViewById(R.id.id_item_image);
                holder.id_item_select = (ImageButton) convertView.findViewById(R.id.id_item_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.id_item_select.setBackgroundResource(R.drawable.ico_im_not);
            String pad = mDirPath + "/" + mDatas.get(position - 1);
            GlideUtil.showImageFile(context, pad, holder.id_item_image, R.drawable.pictures_no);
            holder.id_item_image.setColorFilter(null);
            //设置ImageView的点击事件
            holder.id_item_image.setOnClickListener(new MyOnClickListener(holder, position));
            /**
             * 已经选择过的图片，显示出选择过的效果
             */
            if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(position - 1))) {
                holder.id_item_select.setImageResource(R.drawable.ico_im_ok);
                holder.id_item_image.setColorFilter(Color.parseColor("#77000000"));
            } else {
                holder.id_item_select.setImageResource(R.drawable.ico_im_not);
                holder.id_item_image.setColorFilter(Color.parseColor("#00000000"));
            }
        }
        return convertView;
    }

    class MyOnClickListener implements OnClickListener {
        ViewHolder holder;
        int position;

        MyOnClickListener(ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // 已经选择过该图片
            if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(position - 1))) {
                mSelectedImage.remove(mDirPath + "/" + mDatas.get(position - 1));
                holder.id_item_select.setImageResource(R.drawable.ico_im_not);
                holder.id_item_image.setColorFilter(null);
            } else {// 未选择该图片
                if (mSelectedImage.size() >= max) {
                    ToastUtil.showToast(context, context.getString(R.string.photo_max, max));
                    return;
                }
                mSelectedImage.add(mDirPath + "/" + mDatas.get(position - 1));
                holder.id_item_select.setImageResource(R.drawable.ico_im_ok);
                holder.id_item_image.setColorFilter(Color.parseColor("#77000000"));
            }
            onPhotoSelectedListener.photoClick(mSelectedImage);
        }

    }

    class ViewHolder {
        ImageView id_item_image;
        ImageButton id_item_select;
    }

    public OnPhotoSelectedListener onPhotoSelectedListener;

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener onPhotoSelectedListener) {
        this.onPhotoSelectedListener = onPhotoSelectedListener;
    }

    public interface OnPhotoSelectedListener {
        void photoClick(List<String> number);

        void takePhoto();
    }

}
