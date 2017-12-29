package cn.dajiahui.kid.ui.task.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.task.bean.BeMessageType;

/**
 * Created by wdj on 2016/3/28.
 */
public class ApClasssify extends BaseAdapter {
    private Context context;
    private List<BeMessageType>datas;
    private int position= 0;
    public ApClasssify(Context context,List<BeMessageType>datas){
        this.context = context;
        this.datas = datas;
    }
    public void reFreshItem(int position){
        this.position = position;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return datas!=null?datas.size():0;
    }

    @Override
    public BeMessageType getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(context, convertView, parent, R.layout.simple_spinner_item, 0);
        TextView tv = holder.getView(R.id.tv_classify_item);
        if (this.position == position){
            tv.setBackgroundColor(context.getResources().getColor(R.color.blue_line));
        }else {
            tv.setBackgroundColor(Color.WHITE);
        }
        tv.setText(datas.get(position).getDesc());
        return holder.getConvertView();
    }
}
