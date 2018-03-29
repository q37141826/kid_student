package cn.dajiahui.kid.ui.study.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.log.Logger;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterialInfoLists;


/**
 * 选择教材详情页面
 */
public class ApTeachingMaterialIfo extends CommonAdapter<BeChoiceTeachingMaterialInfoLists> {

    private Context context;
    private String mBookId;

    public ApTeachingMaterialIfo(Context context, List<BeChoiceTeachingMaterialInfoLists> mDatas, String mBookId) {
        super(context, mDatas, R.layout.item_teachingmaterialinfo);
        this.mBookId = mBookId;
        this.context = context;
    }


    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    public void convert(ViewHolder viewHolder, int position, BeChoiceTeachingMaterialInfoLists item) {
        ImageView img_book = viewHolder.getView(R.id.img_book);
        TextView tv_unitname = viewHolder.getView(R.id.tv_unitname);
        TextView tv_state = viewHolder.getView(R.id.tv_state);
        Logger.d("item.getId():" + item.getId());
        Logger.d("mBookId:" + mBookId);

        if (mDatas.get(position).getId().equals(mBookId)) {
            tv_state.setText("继续学习");
            tv_state.setTextColor(mContext.getResources().getColor(R.color.white));
            tv_state.setBackgroundResource(R.drawable.round_bgyellow_febf12_homwwork_startstudy);

        } else {
            tv_state.setText("开始学习");
            tv_state.setBackgroundResource(R.drawable.round_bgyellow_febf12_homwwork_continuestudy);
            tv_state.setTextColor(mContext.getResources().getColor(R.color.yellow_FEBF12));
        }
        GlideUtil.showNoneImage(mContext, item.getLogo(), img_book);
        tv_unitname.setText(item.getName());

    }
}
