package cn.dajiahui.kid.ui.chat.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.widgets.listview.SectionedBaseAdapter;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.chat.bean.BeConcactClass;

/**
 * Created by wdj on 2016/4/5.
 * 学生端联系人的列表适配器
 */
public class ApConcactForClass extends SectionedBaseAdapter {
    private Context context;
    private List<BeConcactClass> datas;

    public ApConcactForClass(Context context, List<BeConcactClass> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void reFreshList(Context context, List<BeConcactClass> datas) {
        this.context = context;
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int section, int position) {
        return datas.get(section).getList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public int getCountForSection(int section) {
        return datas!=null?datas.get(section).getList()!=null?datas.get(section).getList().size():0:0;
    }

    /**
     * 子列表
     *
     * @param section
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(context, convertView, parent, R.layout.item_contact_for_class_child, 1);
        ImageView userAvtor = holder.getView(R.id.user_avtor);
        TextView userName = holder.getView(R.id.user_name);
        if (datas.get(section).getList().get(position).getAvator() != null) {
            GlideUtil.showRoundImage(context, datas.get(section).getList().get(position).getAvator(), userAvtor, R.drawable.ico_default_user, true);
        }
        userName.setText(datas.get(section).getList().get(position).getRealName());
        return holder.getConvertView();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(context, convertView, parent, R.layout.contact_item_first, 0);
        TextView tvName = holder.getView(R.id.contact_item_first);
        tvName.setText(datas.get(section).getClassName());
        return holder.getConvertView();
    }
}
