package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

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
    public FixedImagview(Context context, int pic, int position, List<String> mContentList, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        ImageView imageView = new ImageView(context);
        LayoutParams iparams = new LayoutParams(150, 150);
        imageView.setLayoutParams(iparams);

        if (inbasebean.getIs_answered().equals("0")) {
            imageView.setImageResource(pic);
            this.addView(imageView);
            this.setBackgroundColor(getResources().getColor(R.color.yellow_FEBF12));
        } else {
            if (mContentList.size() > 0) {
                String content = inbasebean.getOptions().get(position).getContent();
                if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                    Glide.with(context).load(mContentList.get(position)).asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                    this.addView(imageView);
                } else {
                    TextView textView = new TextView(context);
                    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
                    textView.setLayoutParams(params);
                    textView.setText(mContentList.get(position));
                    this.addView(textView);
                }
            }

        }
    }

    public FixedImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


}
