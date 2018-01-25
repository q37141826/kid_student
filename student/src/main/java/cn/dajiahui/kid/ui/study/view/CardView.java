package cn.dajiahui.kid.ui.study.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeCradPratice;

/**
 * Created by lenovo on 2018/1/25.
 * <p>
 * 卡片练习的view
 */

public class CardView extends RelativeLayout {

    private Context context;
    private BeCradPratice beCradPratice;
    public ImageView imageView;
    public TextView textView;

    @SuppressLint("ResourceAsColor")
    public CardView(Context context, BeCradPratice beCradPratice) {
        super(context);
        this.context = context;
        this.beCradPratice = beCradPratice;
        addImageView();
        this.setBackgroundColor(R.color.red);
        addTextView();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*添加图片*/
    @SuppressLint("ResourceType")
    private void addImageView() {
        imageView = new ImageView(context);
        LayoutParams lp = new LayoutParams(800, 300);
        imageView.setLayoutParams(lp);
        lp.topMargin = 300;
        lp.addRule(CENTER_IN_PARENT, TRUE);
        imageView.setId(R.string.card_imgage);
        Glide.with(context)
//                .load(beCradPratice.getImg_url())
                .load("http://img.zcool.cn/community/018d4e554967920000019ae9df1533.jpg@900w_1l_2o_100sh.jpg")
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        imageView.setVisibility(GONE);
        this.addView(imageView);

    }

    /*添加文字*/
    @SuppressLint("ResourceType")
    private void addTextView() {

        textView = new TextView(context);
        LayoutParams tparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(tparams);
        tparams.topMargin = 300;
        tparams.addRule(CENTER_IN_PARENT, TRUE);
        textView.setId(R.string.card_textview);
        textView.setText(beCradPratice.getChinese());

        this.addView(textView);
    }


}
