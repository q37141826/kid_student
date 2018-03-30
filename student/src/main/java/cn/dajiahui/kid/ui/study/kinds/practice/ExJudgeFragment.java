package cn.dajiahui.kid.ui.study.kinds.practice;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExJudgeAnswerView;

import static cn.dajiahui.kid.ui.study.kinds.practice.DoPraticeActivity.screenWidth;


/**
 * 判断题
 */
public class ExJudgeFragment extends ExBaseHomeworkFragment implements CheckHomework {


    private TextView tv_judge, mSchedule;
    private ImageView imgconment, img_play;
    private SubmitJudgeFragment submit;
    private JudjeQuestionModle inbasebean;
    private String mediaUrl;
    private RelativeLayout answerRoot;
    private List<ExJudgeAnswerView> mAnswerViewList = new ArrayList();//选项vie的集合
    private Bundle bundle;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_judge, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
        inbasebean = (JudjeQuestionModle) bundle.get("JudgeQuestionModle");
        inbasebean.setEachposition(bundle.getInt("position"));
        mediaUrl = inbasebean.getMedia();
    }


    private void addGroupImage(int size) {
        mAnswerViewList.clear();
        for (int i = 0; i < size; i++) {
            ExJudgeAnswerView judgeImagview = new ExJudgeAnswerView(getActivity(), inbasebean, i, submit, mAnswerViewList);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth / 5, screenWidth / 5);
            if (i == 0) {
                lp.addRule(RelativeLayout.LEFT_OF, R.id.centerline);
                lp.rightMargin = 100;
            } else {
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.centerline);
                lp.leftMargin = 100;
            }
            judgeImagview.setLayoutParams(lp);
            mAnswerViewList.add(judgeImagview);
            answerRoot.addView(judgeImagview);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        tv_judge.setText(inbasebean.getTitle());

        mSchedule.setText(bundle.getString("currntQuestion"));
        /*添加选项图片*/
        addGroupImage(inbasebean.getOptions().size());

          /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgconment);

    }


    /*添加遮罩  */
    private void addMaskView(JudjeQuestionModle qm) {

        for (int i = 0; i < mAnswerViewList.size(); i++) {
            if (mAnswerViewList.get(i) != null) {
                mAnswerViewList.get(i).setInbasebean(qm);
                /*参考答案与当前view的值相同 就是设置黄色边框*/
                if (qm.getMy_answer().equals(inbasebean.getOptions().get(i).getVal())) {
                    /*黄色边框表示自己选的答案*/
                    mAnswerViewList.get(i).setBackgroundResource(R.drawable.select_judge_image);

                    /*回答正确就跳出循环 我的答案与参考答案相等就跳出循环*/
                    if (qm.getMy_answer().equals(inbasebean.getStandard_answer())) {
                        RelativeLayout reT = new RelativeLayout(getActivity());
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageView = new ImageView(getActivity());
                        imageView.setLayoutParams(params);
                        reT.addView(imageView);
                        imageView.setImageResource(R.drawable.answer_true);
                        reT.setBackgroundResource(R.drawable.answer_true_bg);
                        mAnswerViewList.get(i).addView(reT);

                    } else {
                        /*回答错误就把我的答案加上红色遮罩*/
                        RelativeLayout reF = new RelativeLayout(getActivity());
                        RelativeLayout.LayoutParams paramsF = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsF.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageViewF = new ImageView(getActivity());
                        imageViewF.setLayoutParams(paramsF);
                        reF.addView(imageViewF);
                        imageViewF.setImageResource(R.drawable.answer_false);
                        reF.setBackgroundResource(R.drawable.answer_false_bg);
                        mAnswerViewList.get(i).addView(reF);


                       /*把正确答案加上绿色遮罩 中间显示对号*/
                        RelativeLayout reT = new RelativeLayout(getActivity());
                        RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageViewT = new ImageView(getActivity());
                        imageViewT.setLayoutParams(paramsT);
                        reT.addView(imageViewT);
                        imageViewT.setImageResource(R.drawable.answer_true);
                        reT.setBackgroundResource(R.drawable.answer_true_bg);

                        /*判断当前的View的在集合中的索引 利用索引找到另一个view*/
                        int indexOf = mAnswerViewList.indexOf(mAnswerViewList.get(i));
                        if (indexOf == 0) {
                            mAnswerViewList.get(indexOf + 1).addView(reT);
                        } else if (indexOf == 1) {
                            mAnswerViewList.get(indexOf - 1).addView(reT);
                        }

                    }
                } else {
                      /*白色边框表示非选的答案*/
                    mAnswerViewList.get(i).setBackgroundResource(R.drawable.noselect_judge_image);
                }
            }
        }
    }


    /*初始化数据*/
    private void initialize() {
        answerRoot = getView(R.id.judge_root);
        tv_judge = getView(R.id.tv_judge);
        imgconment = getView(R.id.img_conment);
        mSchedule = getView(R.id.tv_schedule);
        img_play = getView(R.id.img_play);
        img_play.setOnClickListener(onClick);
        img_play.setBackground(animationDrawable);

    }


    private View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_play:

                    if (!mediaUrl.equals("")) {
                        playMp3(mediaUrl);
                    }

                    break;
                default:
                    break;
            }
        }
    };


    /*activityt通知判断题碎片当前*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            addMaskView((JudjeQuestionModle) questionModle);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitJudgeFragment) activity;
    }

    /*保存选择题接口*/
    public interface SubmitJudgeFragment {
        public void submitJudgeFragment(JudjeQuestionModle questionModle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }
}

