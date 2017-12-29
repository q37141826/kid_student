package cn.dajiahui.kid.ui.album.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.time.TimeUtil;
import com.hyphenate.easeui.utils.EaseSmileUtils;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.album.bean.BePhotoEval;
import cn.dajiahui.kid.ui.album.bean.BePhotoEvalItem;

/**
 * Created by z on 2016/3/14.
 * 评价列表多级展示效果
 */
public class ApPhotoEval extends BaseExpandableListAdapter {
    private List<BePhotoEval> photoEvalList;
    private Context context;

    public ApPhotoEval(List<BePhotoEval> photoEvalList, Context context) {
        this.photoEvalList = photoEvalList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return photoEvalList != null ? photoEvalList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return photoEvalList.get(groupPosition).getList().size();
    }

    @Override
    public BePhotoEval getGroup(int groupPosition) {
        return photoEvalList.get(groupPosition);
    }

    @Override
    public BePhotoEvalItem getChild(int groupPosition, int childPosition) {
        return photoEvalList.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    //获取一级列表数据
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (RelativeLayout) RelativeLayout.inflate(
                    context, R.layout.photo_item_eval, null);
        }
        View v = ViewHolder.get(convertView, R.id.eval_line);
        if (groupPosition == 0)
            v.setVisibility(View.GONE);
        else
            v.setVisibility(View.VISIBLE);
        TextView tvTitle = ViewHolder.get(convertView, R.id.tvTitle);
        TextView tvMsg = ViewHolder.get(convertView, R.id.tvMsg);
        ImageView im = ViewHolder.get(convertView, R.id.im_user);
        BePhotoEval bean = getGroup(groupPosition);
        String sec = bean.getUserName() + ":" + bean.getContent();
        tvTitle.setText(EaseSmileUtils.getSmiledText(context, sec), TextView.BufferType.SPANNABLE);
        tvMsg.setText(TimeUtil.timeFormat(bean.getAddTime(), TimeUtil.yyMD));
        GlideUtil.showRoundImage(context, bean.getAvator(), im, R.drawable.ico_default_user, true);
        return convertView;
    }

    //设置二级列表数据
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (RelativeLayout) RelativeLayout.inflate(
                    context, R.layout.photo_item_eval_child, null);
        }
        TextView tvTitle = ViewHolder.get(convertView, R.id.tvTitle);
        TextView tvMsg = ViewHolder.get(convertView, R.id.tvMsg);
        ImageView im = ViewHolder.get(convertView, R.id.im_user);
        BePhotoEvalItem bean = getChild(groupPosition, childPosition);
        tvTitle.setText(Html.fromHtml(context.getString(R.string.reply, bean.getUserName(), bean.getReplyUserName())));
        tvTitle.append(EaseSmileUtils.getSmiledText(context, bean.getContent()));
        tvMsg.setText(TimeUtil.timeFormat(bean.getAddTime(), TimeUtil.yyMD));
        GlideUtil.showRoundImage(context, bean.getAvator(), im, R.drawable.ico_default_user, true);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
