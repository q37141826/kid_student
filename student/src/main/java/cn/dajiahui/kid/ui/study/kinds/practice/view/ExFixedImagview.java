package cn.dajiahui.kid.ui.study.kinds.practice.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxtx.framework.util.BaseUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cn.dajiahui.kid.controller.Constant.SortAnswerView_margin;


/**
 * Created by lenovo on 2018/1/16.
 * 不可滑動的imageview
 */

@SuppressLint("AppCompatCustomView")
public class ExFixedImagview extends RelativeLayout {
    private Context context;
    private Activity activity;
    private SortQuestionModle inbasebean;

    private int position;
    private List<String> mMineContentList;//我的答案

    private LayoutParams params;


    /*未check*/
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    public ExFixedImagview(Context context, int position, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.inbasebean = inbasebean;
        this.position = position;
        this.setBackgroundResource(R.drawable.sortview_default_bg);
        this.setPadding(SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin);
        params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        this.setBackgroundResource( R.color.gray_f4f4f4);
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
        this.setBackgroundResource(R.drawable.sortview_default_bg);
        this.setPadding(SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin);
        params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        /*添加视图*/
        addImageView();
    }


    /*添加视图*/
    private void addImageView() {

        /*未check*/
        if (inbasebean.isAnswer() == false) {
            /*未回答状态下获取 内容类型*/
            String content = inbasebean.getOptions().get(position).getContent();
            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                ShowTextViewUI((position + 1) + "");
            } else {
                ShowTextViewUI(inbasebean.getOptions().get(position).getContent());
            }

        } else {/*check之后*/
            this.removeAllViews(); /*保险起见先清理所有view*/
            String content = mMineContentList.get(position);
            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                ShowImageViewUI();
            } else {
                ShowTextViewUI(content);
            }
        }
    }

    /*显示文本*/
    private void ShowTextViewUI(String textComtent) {
        TextView textview = new TextView(context);
        textview.setText(textComtent);
        textview.setLayoutParams(params);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        textview.setTextColor(context.getResources().getColor(R.color.black));
        this.addView(textview);
    }

    /*显示图片*/
    private void ShowImageViewUI() {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(params);
        Glide.with(context).load(mMineContentList.get(position)).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        this.addView(imageView);
    }

    public ExFixedImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


}
