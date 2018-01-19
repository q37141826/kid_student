package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;


/**
 * Created by lenovo on 2018/1/16.
 * 不可滑動的imageview
 */

@SuppressLint("AppCompatCustomView")
public class FixedImagview extends RelativeLayout {
    private Context context;

    @SuppressLint("ResourceAsColor")
    public FixedImagview(Context context, int pic, int position,SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        ImageView imageView = new ImageView(context);
        LayoutParams iparams = new LayoutParams(150, 150);
        imageView.setLayoutParams(iparams);
//        Glide.with(context)
//                .load(inbasebean.getOptions().get(position).getContent())
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
        imageView.setImageResource(R.drawable.wof);
        this.addView(imageView);
        TextView textView = new TextView(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        textView.setLayoutParams(params);
        textView.setText("第" + (position + 1) + "个");
        this.addView(textView);
        this.setBackgroundColor(R.color.yellow);

    }

    public FixedImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


}
