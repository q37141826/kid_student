package cn.dajiahui.kid.ui.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fxtx.framework.adapter.CommonAdapter;
import com.fxtx.framework.adapter.ViewHolder;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeClassSpaceList;
import cn.dajiahui.kid.ui.mine.myclass.ShowPictureActivity;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.view.NoSlideGrildView;


/**
 * 班级空间
 */
public class ApClassSpace extends CommonAdapter<BeClassSpaceList> {

    private NoSlideGrildView grildview;
    private Context context;
    private Activity activity;

    public ApClassSpace(Context context, List<BeClassSpaceList> mDatas) {
        super(context, mDatas, R.layout.item_classspace);
        this.context = context;
        this.activity = (Activity) context;
    }


    @Override
    public void convert(ViewHolder viewHolder, final int position, final BeClassSpaceList item) {


        TextView tv_classname = viewHolder.getView(R.id.tv_classname);
        TextView tv_endtime = viewHolder.getView(R.id.tv_endtime);
        TextView tv_content = viewHolder.getView(R.id.tv_content);
        grildview = viewHolder.getView(R.id.grildview);

        ApClassSpacepicture apClassSpacepicture = new ApClassSpacepicture(this.context, item.getImg_url());

        grildview.setAdapter(apClassSpacepicture);
        /*图片item的点击事件*/
        grildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("IMG_URL", item.getImg_url().get(position));

                int location[] = new int[2];
                view.getLocationOnScreen(location);
                bundle.putInt("locationX", location[0]);
                bundle.putInt("locationY", location[1]);
                bundle.putInt("width", view.getWidth());
                bundle.putInt("height", view.getHeight());


                /*先跳转 在网络请请求获取数据*/
                DjhJumpUtil.getInstance().startBaseActivity(context, ShowPictureActivity.class, bundle, 0);
                activity.overridePendingTransition(0, 0);
            }
        });
        tv_classname.setText(item.getClass_name() + "班动态");
        tv_endtime.setText("发表于：" + DateUtils.timeHour(item.getCreated_at()));
        tv_content.setText(item.getContent());
    }
}
