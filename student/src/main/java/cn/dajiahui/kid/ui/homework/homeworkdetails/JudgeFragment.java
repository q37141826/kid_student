package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.view.JudgeAnswerView;
import cn.dajiahui.kid.util.Logger;


/**
 * 判断题
 */
public class JudgeFragment extends BaseHomeworkFragment implements CheckHomework {


    private TextView tv_judge;
    private ImageView imgconment, img_play;//, imgtrue, imgfasle
    private SubmitJudgeFragment submit;

    private JudjeQuestionModle inbasebean;
    private String mediaUrl;
    private RelativeLayout answerRoot;
    private List<JudgeAnswerView> mAnswerViewList = new ArrayList();


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_judge, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (JudjeQuestionModle) bundle.get("JudgeQuestionModle");
        mediaUrl = inbasebean.getMedia();
    }


    private void addGroupImage(int size) {
        mAnswerViewList.clear();
        for (int i = 0; i < size; i++) {
            JudgeAnswerView judgeImagview = new JudgeAnswerView(getActivity(), inbasebean, i, submit, mAnswerViewList);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(200, 200);
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

        addGroupImage(inbasebean.getOptions().size());

          /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgconment);

        /*作业模式*/
        if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
            if (inbasebean.getIs_answer().equals("1")) {
                for (int i = 0; i < mAnswerViewList.size(); i++) {
                    if (mAnswerViewList.get(i) != null) {
                        /*回答正确  需要增加遮罩  */
                        if (inbasebean.getMy_answer().equals(inbasebean.getStandard_answer())) {
                            mAnswerViewList.get(i).setBackgroundResource(R.drawable.select_judge_image);
                            return;
                        } else {
                            mAnswerViewList.get(i).setBackgroundResource(R.drawable.noselect_judge_image);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitJudgeFragment) activity;
    }


    /*初始化数据*/
    private void initialize() {
        answerRoot = getView(R.id.choice_root);

        tv_judge = getView(R.id.tv_judge);
        imgconment = getView(R.id.img_conment);
        img_play = getView(R.id.img_play);
        img_play.setOnClickListener(onClick);



        /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (inbasebean.getIs_answer().equals("0")) {

        } else {// 更改两个选择按钮的样式
            /*获取我的答案 后台提供*/
            String my_answer = inbasebean.getMy_answer();
            /*我的答案与正确答案一致 回答正确*/
            if (my_answer.equals("1")) {

                //回答正确   正确答案的遮罩层是绿色的  而且右上角有绿色的对√图片
                /*正确按钮设置图片*/


                   /*正确按钮设置背景*/
//                imgtrue.setBackgroundResource();

            } else {   /*我的答案与正确答案不一致 回答错误*/
                //回答错误 自己回答的遮罩层是红色的    正确答案的遮罩层是绿色的  而且右上角有绿色的对√图片


//               imgtrue.setBackgroundResource();
            }


        }

    }


    private View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_play:
                    Toast.makeText(activity, "播放音频", Toast.LENGTH_SHORT).show();
                    if (!mediaUrl.equals("")) {
                        playMp3(mediaUrl);
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

            if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                /*确保回答了*/
                if (inbasebean.getAnswerflag().equals("true")) {
                    for (int i = 0; i < mAnswerViewList.size(); i++) {
                        /*翻頁回來之后保持之前选择的状态*/
                        int currentAnswerPosition = inbasebean.getCurrentAnswerPosition();
                        if (mAnswerViewList.get(currentAnswerPosition) != null) {
                            mAnswerViewList.get(currentAnswerPosition).setBackgroundResource(R.drawable.select_judge_image);
                        }
                    }
                }
            } else if (DoHomeworkActivity.sourceFlag.equals("Practice") && inbasebean.getAnswerflag().equals("true")) {

                /*进行正确错误答案对比机制*/
                /*进行UI的样式书写  待续 imgtrue、 imgfasle 加遮罩层*/
                 /*回答正确*/
                if (inbasebean.getPratice_answer().equals(inbasebean.getStandard_answer())) {
                    Logger.d("判断题-------------------------------------回答正确");
//                    imgtrue.setBackgroundResource(R.drawable.select_judge_image);
                } else {
                    Logger.d("判断题-------------------------------------回答错误");
//                    imgfasle.setBackgroundResource(R.drawable.select_judge_image);
                }
//                Logger.d("inbasebean.isanswer()" + inbasebean.isAnswer());

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

