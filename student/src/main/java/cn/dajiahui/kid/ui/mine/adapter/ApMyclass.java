package cn.dajiahui.kid.ui.mine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeMyclassLists;

/**
 * 我的班级
 */
public class ApMyclass extends CommonAdapter<BeMyclassLists> {

    private Context context;


    public ApMyclass(Context context, List<BeMyclassLists> mDatas) {
        super(context, mDatas, R.layout.item_myclass);
        this.context = context;
    }


    @Override
    public void convert(ViewHolder viewHolder, final int position, final BeMyclassLists item) {
        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_classcode = viewHolder.getView(R.id.tv_classcode);
        TextView tv_school = viewHolder.getView(R.id.tv_school);


        tv_classname.setText(mDatas.get(position).getClass_name());
        tv_classcode.setText("班级码：" + mDatas.get(position).getCode());
        tv_school.setText("学校：" + mDatas.get(position).getSchool_name());

    }


}