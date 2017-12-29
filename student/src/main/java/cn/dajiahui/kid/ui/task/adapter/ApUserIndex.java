package cn.dajiahui.kid.ui.task.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.task.bean.BeMessage;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ApUserIndex extends CommonAdapter<BeMessage> {
    private List<Drawable> drawables = new ArrayList<Drawable>();

    public ApUserIndex(Context context, List<BeMessage> data) {
        super(context, data, R.layout.item_user_index);
        initData();
    }

    private void initData() {
        Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.ico_grsy_ablum);
        Drawable drawable2 = mContext.getResources().getDrawable(R.drawable.ico_grsy_ding);
        drawables.add(drawable1);//相册
        drawables.add(drawable2);//铃声
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, BeMessage item) {
        ImageView img = viewHolder.getView(R.id.im_user_index_item);//用户头像
        TextView title = viewHolder.getView(R.id.tv_user_title_item);//消息标题
        TextView time = viewHolder.getView(R.id.tv_user_content_item);//消息时间
        TextView content = viewHolder.getView(R.id.tv_user_content);//消息内容
        TextView status = viewHolder.getView(R.id.tv_user_status);//消息类别
        GlideUtil.showRoundImage(mContext, item.getAvator(), img, R.drawable.ico_default_user, true);
        title.setText(item.getTitle());
        time.setText(TimeUtil.timeFormat(item.getAddtime() + "", TimeUtil.YYYYMMDDHHMM));
        if (item.getContent() == null || item.getContent() == "" || item.getType().equals("xc")) {
            content.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            content.setText(item.getContent());
        }

        Drawable drab = getdrawableId(item.getType());
        status.setText(item.getTypeName());
        drab.setBounds(0, 0, drab.getMinimumWidth(), drab.getMinimumHeight());
        status.setCompoundDrawables(drab, null, null, null);
        if ("1".equals(item.getIsRead())) {
            status.setBackgroundResource(R.drawable.sp_gray_tv);
        } else if ("0".equals(item.getIsRead()) || null == item.getIsRead()) {
            status.setBackgroundResource(R.drawable.sp_blue_tv);
        }
    }

    public Drawable getdrawableId(String type) {
        Drawable drab = drawables.get(1);
        if (StringUtil.sameStr(type, Constant.type_xc)) {
            drab = drawables.get(0);
        } else {

        }
        return drab;
    }
}
