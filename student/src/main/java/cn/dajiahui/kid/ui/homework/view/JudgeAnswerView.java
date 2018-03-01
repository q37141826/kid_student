package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity;
import cn.dajiahui.kid.ui.homework.homeworkdetails.JudgeFragment;

/**
 * Created by lenovo on 2018/2/7.
 */

@SuppressLint("AppCompatCustomView")
public class JudgeAnswerView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private JudjeQuestionModle inbasebean;
    private int position;
    private JudgeFragment.SubmitJudgeFragment submit;
    private List<JudgeAnswerView> AnswerViewList;

    public JudgeAnswerView(Context context, JudjeQuestionModle inbasebean, int position, JudgeFragment.SubmitJudgeFragment submit, List<JudgeAnswerView> AnswerViewList) {
        super(context);
        this.context = context;
        this.inbasebean = inbasebean;
        this.position = position;
        this.submit = submit;
        this.AnswerViewList = AnswerViewList;
        this.setPadding(10, 10, 10, 10);
        String content = inbasebean.getOptions().get(position).getContent();

        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            /*添加图片*/
            addView(addContentPic());
        } else {
             /*添加文字*/
            addView(addContentText());
        }

        /*判断题  只有没答过题才注册点击事件*/
        if (inbasebean.getIs_answer().equals("0")) {
            this.setOnClickListener(this);
        }
        /*只有答过题之后 submit之后才添加遮罩*/
        if (inbasebean.getIs_answer().equals("1")) {

        }

    }


    public JudgeAnswerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    private ImageView addContentPic() {
        ImageView imageView = new ImageView(context);
           /*加载正确答案按钮图片*/
        Glide.with(context)
                .load(inbasebean.getOptions().get(position).getContent())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
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
            if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                inbasebean.setPratice_answer(inbasebean.getOptions().get(position).getVal());
            }
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
        } else {

            Toast.makeText(context, "已经答过题了", Toast.LENGTH_SHORT).show();
        }


    }


}
