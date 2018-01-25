package cn.dajiahui.kid.ui.study.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterial;


/**
 * 选择教材
 */
public class ApTeachingMaterial extends CommonAdapter<BeChoiceTeachingMaterial> {

    public ApTeachingMaterial(Context context, List<BeChoiceTeachingMaterial> mDatas) {
        super(context, mDatas, R.layout.item_teachingmaterial);
    }


    @Override
    public void convert(ViewHolder viewHolder, int position, BeChoiceTeachingMaterial item) {
        TextView tv_book = viewHolder.getView(R.id.tv_book);
        TextView tv_count = viewHolder.getView(R.id.tv_count);

        tv_book.setText(item.getTeachingMaterialName());
        tv_count.setText(item.getTeachingMaterialCount());

    }
}
