package cn.dajiahui.kid.ui.study.kinds.practice.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxtx.framework.util.BaseUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;


/**
 * Created by lenovo on 2018/1/16.
 * 不可滑動的imageview
 */

@SuppressLint("AppCompatCustomView")
public class ExFixedImagview extends RelativeLayout {
    private Context context;
    private Activity activity;
    private SortQuestionModle inbasebean;
    private int pic;
    private int position;
    private List<String> mMineContentList;//我的答案
    private int widthPixels;


    /*未check*/
    @SuppressLint("ResourceAsColor")
    public ExFixedImagview(Context context, int pic, int position, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.activity= (Activity) context;
        this.inbasebean = inbasebean;
        this.pic = pic;
        this.position = position;
         /*屏幕宽度*/
        widthPixels = BaseUtil.getWidthPixels( (Activity) context);
        /*添加视图*/
        addImageView();
    }

    /*check之后*/
    public ExFixedImagview(Context context, int position, SortQuestionModle inbasebean, List<String> mMineContentList) {
        super(context);
        this.context = context;
        this.inbasebean = inbasebean;
        this.position = position;
        this.mMineContentList = mMineContentList;
       /*屏幕宽度*/
        widthPixels = BaseUtil.getWidthPixels( (Activity) context);
         /*添加视图*/
        addImageView();
    }


    /*添加视图*/
    private void addImageView() {
        LayoutParams params = new LayoutParams(widthPixels/5, widthPixels/5);

        /*未check*/
        if (inbasebean.isAnswer() == false) {
            /*未回答状态下获取 内容类型*/
            String content = inbasebean.getOptions().get(position).getContent();
            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(params);
                imageView.setImageResource(pic);
                this.addView(imageView);
            } else {
                TextView textView = new TextView(context);
                LayoutParams tparams = new LayoutParams(widthPixels/5, widthPixels/5);
                textView.setTextColor(getResources().getColor(R.color.blue));
                params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
                textView.setText(inbasebean.getOptions().get(position).getContent());
                textView.setLayoutParams(tparams);
                this.addView(textView);
            }
            this.setBackgroundColor(getResources().getColor(R.color.yellow_FEBF12));
        } else {/*check之后*/
            this.removeAllViews(); /*保险起见先清理所有view*/
            String content = mMineContentList.get(position);
            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {

                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(params);
                Glide.with(context).load(mMineContentList.get(position)).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                this.addView(imageView);

            } else {

                TextView textView = new TextView(context);
                LayoutParams tparams = new LayoutParams(widthPixels/5, widthPixels/5);
                textView.setTextColor(getResources().getColor(R.color.blue));
                params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
                textView.setText(content);
                textView.setLayoutParams(tparams);
                this.addView(textView);
            }
        }
    }

    public ExFixedImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


}
