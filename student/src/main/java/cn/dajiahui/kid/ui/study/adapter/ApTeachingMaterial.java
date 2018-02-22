package cn.dajiahui.kid.ui.study.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeChoiceTeachingMaterialLists;


/**
 * 选择教材
 */
public class ApTeachingMaterial extends CommonAdapter<BeChoiceTeachingMaterialLists> {

    public ApTeachingMaterial(Context context, List<BeChoiceTeachingMaterialLists> mDatas) {
        super(context, mDatas, R.layout.item_teachingmaterial);
    }


    @Override
    public void convert(ViewHolder viewHolder, int position, BeChoiceTeachingMaterialLists item) {
        TextView tv_book = viewHolder.getView(R.id.tv_book);
        TextView tv_count = viewHolder.getView(R.id.tv_count);

        tv_book.setText(item.getName());
        tv_count.setText("共计" + item.getCount() + "本");

    }
}
