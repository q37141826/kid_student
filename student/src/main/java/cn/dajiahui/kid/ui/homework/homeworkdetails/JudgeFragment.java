package cn.dajiahui.kid.ui.homework.homeworkdetails;

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
import com.fxtx.framework.log.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.view.JudgeAnswerView;

import static cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity.screenWidth;


/**
 * 判断题
 */
public class JudgeFragment extends BaseHomeworkFragment implements CheckHomework {


    private List<JudgeAnswerView> mAnswerViewList = new ArrayList();//判断题shituview的集合
    private JudjeQuestionModle inbasebean;//数据模型
    private TextView tv_judge, tv_schedule;
    private ImageView imgconment, img_play;
    private SubmitJudgeFragment submit;
    private RelativeLayout answerRoot;
    private String mediaUrl;
    private Bundle bundle;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_judge, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
        inbasebean = (JudjeQuestionModle) bundle.get("JudgeQuestionModle");
        mediaUrl = inbasebean.getMedia();
    }

    /*动态添加选择视图*/
    private void addGroupImage(int size) {
        mAnswerViewList.clear();
        for (int i = 0; i < size; i++) {
            JudgeAnswerView judgeImagview = new JudgeAnswerView(getActivity(), inbasebean, i, submit, mAnswerViewList);
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
        tv_schedule.setText(bundle.getString("currntQuestion"));
        /*加载选择视图*/
        addGroupImage(inbasebean.getOptions().size());

        /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgconment);

        /*判断条件是已完成状态下  添加遮罩*/
        if (inbasebean.getIs_complete().equals("1")) {
            addMaskView();
        }
    }


    /*添加遮罩  添加遮罩  */
    private void addMaskView() {

        for (int i = 0; i < mAnswerViewList.size(); i++) {
            if (mAnswerViewList.get(i) != null) {
                /*未作答 直接显示正确答案*/
                if (inbasebean.getMy_answer().equals("")) {
                    /*直接显示参考答案*/
                    if (mAnswerViewList.get(i).val.equals(inbasebean.getStandard_answer())) {
                        mAnswerViewList.get(i).addView(ShowRightMaskView());

                    }

                } else {
                    if (inbasebean.getMy_answer().equals(mAnswerViewList.get(i).val)) {

                        /*回答正确就跳出循环 我的答案与参考答案相等就跳出循环*/
                        if (inbasebean.getMy_answer().equals(inbasebean.getStandard_answer())) {
                            mAnswerViewList.get(i).addView(ShowRightMaskView());

                        } else {
                            /*回答错误就把我的答案加上红色遮罩*/
                            mAnswerViewList.get(i).addView(ShowWrongMaskView());

                            /*判断当前的View的在集合中的索引 利用索引找到另一个view*/
                            int indexOf = mAnswerViewList.indexOf(mAnswerViewList.get(i));
                            if (indexOf == 0) {
                                mAnswerViewList.get(indexOf + 1).addView(ShowRightMaskView());
                            } else if (indexOf == 1) {
                                mAnswerViewList.get(indexOf - 1).addView(ShowRightMaskView());
                            }
                        }
                    }
                }
            }
        }
    }

    /*显示正确答案遮罩*/
    private RelativeLayout ShowRightMaskView() {
        RelativeLayout reT = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(params);
        reT.addView(imageView);
        imageView.setImageResource(R.drawable.answer_true);
        reT.setBackgroundResource(R.drawable.answer_true_bg);

        return reT;
    }

    /*显示错误答案遮罩*/
    private RelativeLayout ShowWrongMaskView() {
        /*回答错误就把我的答案加上红色遮罩*/
        RelativeLayout reF = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams paramsF = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsF.addRule(RelativeLayout.CENTER_IN_PARENT);
        ImageView imageViewF = new ImageView(getActivity());
        imageViewF.setLayoutParams(paramsF);
        reF.addView(imageViewF);
        imageViewF.setImageResource(R.drawable.answer_false);
        reF.setBackgroundResource(R.drawable.answer_false_bg);

        return reF;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitJudgeFragment) activity;
    }


    /*初始化数据*/
    private void initialize() {
        answerRoot = getView(R.id.judge_root);
        tv_judge = getView(R.id.tv_judge);
        tv_schedule = getView(R.id.tv_schedule);
        imgconment = getView(R.id.img_conment);
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
                    }else {
                        audioDialog.show();
                    }

                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (JudjeQuestionModle) questionModle;


            if (inbasebean.getAnswerflag().equals("true")) {
                for (int i = 0; i < mAnswerViewList.size(); i++) {
                    /*翻頁回來之后保持之前选择的状态*/
                    int currentAnswerPosition = inbasebean.getCurrentAnswerPosition();
                    if (mAnswerViewList.get(currentAnswerPosition) != null) {
                        mAnswerViewList.get(currentAnswerPosition).setBackgroundResource(R.drawable.select_judge_image);
                    }
                }
            }
        }

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

