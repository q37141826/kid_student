package cn.dajiahui.kid.ui.study.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.ChooseUtils;


/**
 * 发布课本作业的Utils适配器
 */
public class ApChooseUtils extends CommonAdapter<ChooseUtils> {


    private Context context;

    public ApChooseUtils(Context context, List<ChooseUtils> mDatas) {
        super(context, mDatas, R.layout.item_choiceutils);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, ChooseUtils item) {

        TextView itemcurrent = viewHolder.getView(R.id.itemcurrent);
        TextView Utilsname = viewHolder.getView(R.id.tv_utilName);
        TextView utilstudy = viewHolder.getView(R.id.tv_utilstudy);//学到这里
//        cn.dajiahui.kid.ui.homework.view.ProhibitMoveSeekbar seek_item = viewHolder.getView(R.id.seek_item);

        itemcurrent.setText((position + 1) + "");

        Utilsname.setText(item.getUtlisname());
//        seek_item.setProgress(Integer.parseInt(item.getSchedule()));

    }


}
