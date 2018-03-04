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
import cn.dajiahui.kid.ui.study.bean.BeCradPraticePageData;

/**
 * Created by lenovo on 2018/1/25.
 * <p>
 * 卡片练习的view
 */

public class CardView extends RelativeLayout {

    private Context context;
    private BeCradPraticePageData beCradPratice;
    public ImageView imageView;

    public RelativeLayout linearLayout;

    @SuppressLint("ResourceAsColor")
    public CardView(Context context, BeCradPraticePageData beCradPraticePageData) {
        super(context);
        this.context = context;
        this.beCradPratice = beCradPraticePageData;
        addImageView();
//        this.setBackgroundColor(getResources().getColor(R.color.gray));
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
                .load(beCradPratice.getPage_url())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        imageView.setVisibility(GONE);
        this.addView(imageView);

    }

    /*添加文字*/
    @SuppressLint("ResourceType")
    private void addTextView() {
        linearLayout = new RelativeLayout(context);

        TextView tvCh = new TextView(context);
        LayoutParams tparamsCh = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvCh.setLayoutParams(tparamsCh);
        tparamsCh.topMargin = 300;
        tparamsCh.addRule(CENTER_IN_PARENT, TRUE);
        tvCh.setId(R.string.card_textview);
        tvCh.setText(beCradPratice.getItem().get(0).getChinese());
        tvCh.setTextColor(getResources().getColor(R.color.red));

        linearLayout.addView(tvCh);

//        TextView tvEn = new TextView(context);
//        LayoutParams tparamsEn = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvCh.setLayoutParams(tparamsEn);
//        tparamsEn.topMargin = 300;
//        tparamsEn.addRule(CENTER_IN_PARENT, TRUE);
//        tvCh.setId(R.string.card_textview);
//        tvCh.setText(beCradPratice.getItem().get(0).getEnglish());
//        tvCh.setTextColor(getResources().getColor(R.color.red));
//
//        linearLayout.addView(tvEn);

        LayoutParams tparamRoot = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(tparamRoot);
//        tparamRoot.topMargin = 330;
        tparamRoot.addRule(CENTER_IN_PARENT, TRUE);

        this.addView(linearLayout);
    }


}
