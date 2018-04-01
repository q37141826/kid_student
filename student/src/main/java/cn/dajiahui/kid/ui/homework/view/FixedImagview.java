package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cn.dajiahui.kid.controller.Constant.SortAnswerView_margin;
import static cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity.screenWidth;
import static cn.jpush.android.api.JPushInterface.a.i;


/**
 * Created by lenovo on 2018/1/16.
 * 不可滑動的imageview
 */

@SuppressLint("AppCompatCustomView")
public class FixedImagview extends RelativeLayout {
    private Context context;
    private LayoutParams params;
    private int position;
    private SortQuestionModle inbasebean;
    private List<String> mMineContentList;//我的答案的内容
    private List<String> mRightContentList;//正确答案的内容

    public FixedImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ResourceAsColor")
    public FixedImagview(Context context, int position, List<String> mMineContentList,
                         List<String> mRightContentList, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.position = position;
        this.inbasebean = inbasebean;
        this.mMineContentList = mMineContentList;
        this.mRightContentList = mRightContentList;
        this.setBackgroundResource(R.drawable.sortview_default_bg);
        this.setPadding(SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin);
        params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        /*首先判断是否作答*/
        switch (inbasebean.getIs_complete()) {
            /*未开始*/
            case "-1":
                ShowNoCompleteUI();
                break;
            /*进行中*/
            case "0":
                ShowNoCompleteUI();

                break;
            /*已完成*/
            case "1":
                ShowCompleteUI();
                AddMaskView();
                break;
            default:
                break;

        }
    }

    /*添加遮罩*/
    private void AddMaskView() {
        RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
        ImageView imageViewT = new ImageView(context);
        imageViewT.setLayoutParams(paramsT);
        if (mMineContentList.size() > 0 && mRightContentList.size() > 0 &&
                mRightContentList.get(position).equals(mMineContentList.get(position))) {
            /*正确答案 添加遮罩*/
            imageViewT.setBackgroundResource(R.drawable.answer_true_bg);
        } else {
            /*错误答案 添加遮罩*/
            imageViewT.setBackgroundResource(R.drawable.answer_false_bg);
        }
        this.addView(imageViewT);
    }


    /*显示未完成视图*/
    private void ShowNoCompleteUI() {
        ShowTextViewUI((position + 1) + "");
    }

    /*显示完成视图*/
    private void ShowCompleteUI() {

        if (mMineContentList.size() > 0) {
            String content = inbasebean.getOptions().get(position).getContent();
            /*答案是图片*/
            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                ShowImageViewUI();
            } else {
                /*答案是文字*/
                ShowTextViewUI(mMineContentList.get(position));
            }
        } else {
            ShowTextViewUI((position + 1) + "");
        }
    }

    /*显示文本*/
    private void ShowTextViewUI(String textComtent) {
        TextView textview = new TextView(context);
        textview.setText(textComtent);
        textview.setLayoutParams(params);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        textview.setTextColor(context.getResources().getColor(R.color.gray_9c9c9c));
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


}
