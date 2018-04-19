package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.homeworkdetails.JudgeFragment;
import cn.dajiahui.kid.ui.study.view.CompositionTextView;

import static cn.dajiahui.kid.controller.Constant.JudgeAnswerView_margin;

/**
 * Created by mj on 2018/2/7.
 * <p>
 * 选择题自定义view视图
 */

@SuppressLint("AppCompatCustomView")
public class JudgeAnswerView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private JudjeQuestionModle inbasebean;
    private int position;
    private JudgeFragment.SubmitJudgeFragment submit;
    private List<JudgeAnswerView> AnswerViewList;
    public String val;

    public JudgeAnswerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public JudgeAnswerView(Context context, JudjeQuestionModle inbasebean, int position, JudgeFragment.SubmitJudgeFragment submit, List<JudgeAnswerView> AnswerViewList) {
        super(context);
        this.context = context;
        this.inbasebean = inbasebean;
        this.position = position;
        this.submit = submit;
        this.AnswerViewList = AnswerViewList;
        this.val = inbasebean.getOptions().get(position).getVal();
        this.setPadding(JudgeAnswerView_margin, JudgeAnswerView_margin, JudgeAnswerView_margin, JudgeAnswerView_margin);
        this.setBackgroundResource(R.drawable.noselect_judge_image);



        /*首先判断是否作答*/
        switch (inbasebean.getIs_complete()) {
            /*未开始*/
            case "-1":
                this.setOnClickListener(this);
                ShowNoCompleteUI();
                break;
            /*进行中*/
            case "0":
                this.setOnClickListener(this);
                ShowNoCompleteUI();

                break;
            /*已完成*/
            case "1":
                ShowCompleteUI();
//                AddMaskView();
                break;
            default:
                break;

        }


    }

//    /*添加遮罩*/
//    private void AddMaskView() {
//        /*未作答 直接显示正确答案*/
//        if (inbasebean.getMy_answer().equals("㊒")) {
//            /*直接显示参考答案*/
//            if (val.equals(inbasebean.getStandard_answer())) {
//                ShowRightMaskView();
//            }
//        } else {
//
//            /*回答正确*/
//            if (val.equals(inbasebean.getStandard_answer())) {
//                /*添加正确答案遮罩*/
//                ShowRightMaskView();
//            } else {
//                /*添加错误答案遮罩*/
//                ShowWrongMaskView();
//            }
//
//        }
//
//
//    }

    /*已完成*/
    private void ShowCompleteUI() {
        String content = inbasebean.getOptions().get(position).getContent();

        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            /*添加图片*/
            addView(ShowImageViewUI(inbasebean.getOptions().get(position).getContent()));
        } else {
            /*添加文字*/
            addView(ShowTextViewUI(inbasebean.getOptions().get(position).getContent()));
        }

    }

    /*未开始*/
    private void ShowNoCompleteUI() {
        String content = inbasebean.getOptions().get(position).getContent();

        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            /*添加图片*/
            addView(ShowImageViewUI(inbasebean.getOptions().get(position).getContent()));
        } else {
            /*添加文字*/
            addView(ShowTextViewUI(inbasebean.getOptions().get(position).getContent()));
        }

    }

    /*显示文本*/
    private TextView ShowTextViewUI(String textComtent) {
        ShowYellowShapFrame();
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        final CompositionTextView textView = new CompositionTextView(context);
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.gray_333333));
        textView.setText(textComtent);

        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //使用完必须撤销监听，否则，会一直不停的不定时的测量，这比较耗性能
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (textView.isOverFlowed()) {
                    // 文字超过一行
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER | Gravity.TOP);

                } else {
                    // 文字没有超过
                    textView.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                }

            }

        });
        return textView;
    }

    /*显示图片*/
    private ImageView ShowImageViewUI(String imgUrl) {
        ShowYellowShapFrame();
        ImageView imageView = new ImageView(context);
        /*加载正确答案按钮图片*/
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        return imageView;
    }

    /*我的答案添加黄色边框*/
    private void ShowYellowShapFrame() {
        /*我的答案添加黄色边框 */
        if (val.equals(inbasebean.getMy_answer())) {
            this.setBackgroundResource(R.drawable.select_judge_image);
        } else {
            /*白色边框表示非选的答案*/
            this.setBackgroundResource(R.drawable.noselect_judge_image);
        }
    }

    @Override
    public void onClick(View v) {
        if (inbasebean.isAnswer() == false) {

            for (int i = 0; i < AnswerViewList.size(); i++) {
                /*正确 错误答案添加背景遮罩*/
                if (AnswerViewList.get(i) == this) {
                    this.setBackgroundResource(R.drawable.select_judge_image);
                } else {
                    AnswerViewList.get(i).setBackgroundResource(R.drawable.noselect_judge_image);
                }
            }

            inbasebean.setCurrentAnswerPosition(position);//保存当前选择的position （用于翻页回来之后恢复选择状态使用）
            inbasebean.setAnswerflag("true");//答题标记
            inbasebean.setMy_answer(inbasebean.getOptions().get(position).getVal());//作答答案

            /*选择正确*/
            if (inbasebean.getOptions().get(position).getVal().equals(inbasebean.getStandard_answer())) {
                inbasebean.setIs_right("1");
            } else {
                inbasebean.setIs_right("0");
            }

            submit.submitJudgeFragment(inbasebean);
        }


    }


//    /*显示正确答案遮罩*/
//    private void ShowRightMaskView() {
//        RelativeLayout reT = new RelativeLayout(context);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        ImageView imageView = new ImageView(context);
//        imageView.setLayoutParams(params);
//        reT.addView(imageView);
//        imageView.setImageResource(R.drawable.answer_true);
//        reT.setBackgroundResource(R.drawable.answer_true_bg);
//        this.addView(reT);
//
//    }
//
//    /*显示错误答案遮罩*/
//    private void ShowWrongMaskView() {
//        /*回答错误就把我的答案加上红色遮罩*/
//        RelativeLayout reF = new RelativeLayout(context);
//        RelativeLayout.LayoutParams paramsF = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsF.addRule(RelativeLayout.CENTER_IN_PARENT);
//        ImageView imageViewF = new ImageView(context);
//        imageViewF.setLayoutParams(paramsF);
//        reF.addView(imageViewF);
//        imageViewF.setImageResource(R.drawable.answer_false);
//        reF.setBackgroundResource(R.drawable.answer_false_bg);
//        this.addView(reF);
//    }


}
