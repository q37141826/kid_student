package cn.dajiahui.kid.ui.study.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeChoiceStudy;


/**
 * 选择6种学习模式
 */
public class ApChoiceStudy extends CommonAdapter<BeChoiceStudy> {


    private ImageView itemimage;
    private cn.dajiahui.kid.ui.homework.view.ProhibitMoveSeekbar seekitem;
    private TextView tvtypename;



    public ApChoiceStudy(Context context, List<BeChoiceStudy> mDatas) {
        super(context, mDatas, R.layout.item_choicestudy);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeChoiceStudy item) {

        itemimage = viewHolder.getView(R.id.itemimage);
//        seekitem = viewHolder.getView(R.id.seek_item);
        tvtypename = viewHolder.getView(R.id.tv_typename);

        tvtypename.setText(item.getStudyname());
        itemimage.setImageResource(item.getStudypic());
//        seekitem.setProgress(Integer.parseInt(item.getStudyseekschedule()));

    }


}
