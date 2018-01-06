//package cn.dajiahui.kid.ui.notice.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.fxtx.framework.adapter.CommonAdapter;
//import com.fxtx.framework.adapter.ViewHolder;
//
//import java.util.List;
//
//import cn.dajiahui.kid.R;
//import cn.dajiahui.kid.http.bean.BeTeFile;
//import cn.dajiahui.kid.util.TeacherUtil;
//
///**
// * Created by z on 2016/3/31.
// * 教研总结附件适配器
// */
//public class ApTeNote extends CommonAdapter<BeTeFile> {
//    public ApTeNote(Context context, List<BeTeFile> mDatas) {
//        super(context, mDatas, R.layout.teaching_item_file);
//    }
//
//    public boolean showImg = mtrue;
//
//    @Override
//    public void convert(ViewHolder viewHolder, int position, BeTeFile item) {
//        TextView textView = viewHolder.getView(R.id.tvTitle);
//        textView.setText(item.getFileName());
//        ImageView im = viewHolder.getView(R.id.im_user);
//        im.setImageResource(TeacherUtil.setFileType(item.getFileName()));
//        textView.setTag(item);
//        if (showImg) {
//            im.setVisibility(View.VISIBLE);
//        } else {
//            im.setVisibility(View.GONE);
//        }
//        textView.setOnClickListener(onClick);
//    }
//
//    private View.OnClickListener onClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };
//
//    /**
//     * 设置是否显示文件图片
//     */
//    public void isShowImg(boolean flag) {
//        showImg = flag;
//    }
//}