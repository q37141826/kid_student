package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeMyWorks;

/**
 * Created by majin on 2016/5/11.
 * 我的作品
 */
public class ApMyWorks extends CommonAdapter<BeMyWorks> {
    int selectorPosition = -3;
    private CheckBox checkBox;

    private Context context;

    public ApMyWorks(Context context, List mDatas) {
        super(context, mDatas, R.layout.item_myworks);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder viewHolder, final int position, final BeMyWorks item) {
        checkBox = viewHolder.getView(R.id.checkbox);
        ImageView img = viewHolder.getView(R.id.img);
        TextView tv_worksname = viewHolder.getView(R.id.tv_worksname);
        TextView tv_workstime = viewHolder.getView(R.id.tv_workstime);

        tv_worksname.setText(item.getWorksName().substring(0, item.getWorksName().indexOf(".mp4")));
        tv_workstime.setText(item.getWorkstime());
        Bitmap videoBitmap = getVideoBitmap(item.getWorksLocalPath());
        img.setImageBitmap(videoBitmap);
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == -1) {
            checkBox.setVisibility(View.VISIBLE);
        } else if (selectorPosition == -2) {
            checkBox.setVisibility(View.GONE);
        }

//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkBox.isChecked()) {
//
//                    mDatas.get(position).setIscheck(true);
//
//                } else {
//
//                    mDatas.get(position).setIscheck(true);
//                    checkBox.setChecked(!mDatas.get(position).ischeck());
//                }
//
//            }
//        });
    }

    public Bitmap getVideoBitmap(String path) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            Bitmap frameAtTime = retriever.getFrameAtTime();
            return frameAtTime;
        } catch (Exception e) {
            return null;
        } finally {
            retriever.release();
        }

    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();
    }


}
