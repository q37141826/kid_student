package cn.dajiahui.kid.ui.study.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeCradPraticePageData;

/**
 * Created by mj on 2018/1/25.
 * <p>
 * 卡片练习的view
 */

public class CardView extends RelativeLayout {

    private Context context;
    Activity activity;
    private BeCradPraticePageData beCradPratice;

    public ImageView imageView;
    public LinearLayout linShort;

    public CompositionTextView tv_english;
    public CompositionTextView tv_chinese;


    @SuppressLint("ResourceAsColor")
    public CardView(Context context, Activity activity, BeCradPraticePageData beCradPraticePageData) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.beCradPratice = beCradPraticePageData;
        addImageView();
        addTextView();
    }

    private void addTextView() {

        LinearLayout.LayoutParams tparamRoot = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tparamRoot.gravity = CENTER_IN_PARENT;

        String chinese = beCradPratice.getItem().get(0).getChinese();
        LayoutInflater inflater = LayoutInflater.from(context);
        linShort = (LinearLayout) inflater.inflate(R.layout.card_pratice_textview, null);
        linShort.setLayoutParams(tparamRoot);
        tv_english = (CompositionTextView) linShort.findViewById(R.id.tv_english);
        tv_chinese = (CompositionTextView) linShort.findViewById(R.id.tv_chinese);
        linShort.setLayoutParams(tparamRoot);

        tv_english.setText(beCradPratice.getItem().get(0).getEnglish());
        tv_chinese.setText(beCradPratice.getItem().get(0).getChinese());

        tv_english.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //使用完必须撤销监听，否则，会一直不停的不定时的测量，这比较耗性能
                tv_english.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (tv_english.isOverFlowed()) {
                    // 文字超过一行
                    tv_english.setGravity(Gravity.LEFT | Gravity.CENTER | Gravity.TOP);
                    tv_english.setMovementMethod(ScrollingMovementMethod.getInstance());
                } else {
                    // 文字没有超过
                    tv_english.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                }
                tv_english.setText(beCradPratice.getItem().get(0).getEnglish());
            }

        });

        tv_chinese.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //使用完必须撤销监听，否则，会一直不停的不定时的测量，这比较耗性能
                tv_chinese.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (tv_chinese.isOverFlowed()) {
                    // 文字超过一行
                    tv_chinese.setGravity(Gravity.LEFT | Gravity.CENTER | Gravity.TOP);
                    tv_chinese.setMovementMethod(ScrollingMovementMethod.getInstance());
                } else {
                    // 文字没有超过
                    tv_chinese.setGravity(Gravity.CENTER | Gravity.TOP);
                }
                tv_chinese.setText(beCradPratice.getItem().get(0).getChinese());
            }

        });


        if (chinese.equals("")) {
            tv_chinese.setVisibility(GONE);
        }
        linShort.setVisibility(INVISIBLE);
        this.addView(linShort);

    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*添加图片*/
    @SuppressLint("ResourceType")
    private void addImageView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        imageView = (ImageView) inflater.inflate(R.layout.card_pratice_imageview, null);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        Glide.with(context)
                .load(beCradPratice.getPage_url())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        this.addView(imageView);
    }

}
