package cn.dajiahui.kid.ui.study.kinds.practice.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.study.kinds.practice.ExJudgeFragment;

/**
 * Created by mj on 2018/2/7.
 * <p>
 * 练习选项的view
 */

@SuppressLint("AppCompatCustomView")
public class ExJudgeAnswerView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private JudjeQuestionModle inbasebean;
    private int position;
    private ExJudgeFragment.SubmitJudgeFragment submit;
    private List<ExJudgeAnswerView> mAnswerViewList;

    public void setInbasebean(JudjeQuestionModle inbasebean) {
        this.inbasebean = inbasebean;
    }

    public ExJudgeAnswerView(Context context, JudjeQuestionModle inbasebean, int position, ExJudgeFragment.SubmitJudgeFragment submit, List<ExJudgeAnswerView> mAnswerViewList) {
        super(context);
        this.context = context;
        this.inbasebean = inbasebean;
        this.position = position;
        this.submit = submit;
        this.mAnswerViewList = mAnswerViewList;
        this.setPadding(10, 10, 10, 10);

        String content = inbasebean.getOptions().get(position).getContent();
        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            addView(addContentPic());
        } else {
            addView(addContentText());
        }
        /*注册点击事件*/
        this.setOnClickListener(this);
    }


    public ExJudgeAnswerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*添加图片*/
    private ImageView addContentPic() {
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(inbasebean.getOptions().get(position).getContent())
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        return imageView;
    }

    /*添加文字*/
    private TextView addContentText() {
        TextView textView = new TextView(context);
        textView.setText(inbasebean.getOptions().get(position).getContent());
        return textView;
    }

    @Override
    public void onClick(View v) {

        if (inbasebean.isAnswer() == false) {
            for (int i = 0; i < mAnswerViewList.size(); i++) {
                  /*我的答案选项添加黄色边框*/
                if (mAnswerViewList.get(i) == this) {
                    this.setBackgroundResource(R.drawable.select_judge_image);
                } else {
                    mAnswerViewList.get(i).setBackgroundResource(R.drawable.noselect_judge_image);
                }
            }

            inbasebean.setAnswerflag("true");//答题标记
            inbasebean.setMy_answer(inbasebean.getOptions().get(position).getVal());//作答答案

            /*判断回答正误*/
            if (inbasebean.getOptions().get(position).getVal().equals(inbasebean.getStandard_answer())) {
                inbasebean.setIs_right("1");
            } else {
                inbasebean.setIs_right("0");
            }

            submit.submitJudgeFragment(inbasebean);
        }
//        else {
//            Toast.makeText(context, "已经答过题了", Toast.LENGTH_SHORT).show();
//        }


    }


}
