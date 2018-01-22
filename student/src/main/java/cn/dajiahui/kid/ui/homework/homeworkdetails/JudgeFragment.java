package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;


/**
 * 判断题
 */
public class JudgeFragment extends BaseHomeworkFragment implements CheckHomework {


    private TextView tv_judge;
    private ImageView imgconment, img_play, imgtrue, imgfasle;
    private SubmitJudgeFragment submit;

    private JudjeQuestionModle inbasebean;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_judge, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (JudjeQuestionModle) bundle.get("JudgeQuestionModle");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        /*加载正确答案按钮图片*/
        Glide.with(getActivity())
                .load(inbasebean.getOptions().get(0).getContent())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgtrue);
         /*加载错错误答案按钮图片*/
        Glide.with(getActivity())
                .load(inbasebean.getOptions().get(1).getContent())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgfasle);

        if (inbasebean.getIs_answer().equals("1")) {
            /*回答正确*/
            if (inbasebean.getMy_answer().equals(inbasebean.getStandard_answer())) {
                imgtrue.setBackgroundResource(R.drawable.select_judge_image);
            } else {
                imgfasle.setBackgroundResource(R.drawable.select_judge_image);
            }


        }
//        tv_judge.setText(inbasebean.getId() + "");
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitJudgeFragment) activity;
    }


    /*初始化数据*/
    private void initialize() {
        tv_judge = getView(R.id.tv_judge);
        imgconment = getView(R.id.img_conment);
        imgtrue = getView(R.id.img_true);
        imgfasle = getView(R.id.img_fasle);
        img_play = getView(R.id.img_play);
        /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (inbasebean.getIs_answer().equals("0")) {
            imgtrue.setOnClickListener(onClick);
            imgfasle.setOnClickListener(onClick);
            img_play.setOnClickListener(onClick);
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
                    playMp3("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");

                    break;
                case R.id.img_true:
                    if (inbasebean != null) {
                        /*正确 错误答案添加背景遮罩*/
                        imgtrue.setBackgroundResource(R.drawable.select_judge_image);
                        imgfasle.setBackgroundResource(R.drawable.noselect_judge_image);
//                        if (inbasebean.isAnswer() == false) {

                        Toast.makeText(activity, "正确", Toast.LENGTH_SHORT).show();
                        inbasebean.setJudgeAnswerFlag("mtrue");//正确回答标志
                        inbasebean.setSubmitAnswer("true");//学生答题答案
                        inbasebean.setAnswerflag("true");
                        submit.submitJudgeFragment(inbasebean);
//                        } else {
//                            Toast.makeText(activity, "已经答过题了", Toast.LENGTH_SHORT).show();
//                        }
                    }
                    break;
                case R.id.img_fasle:
                    if (inbasebean != null) {
                        imgtrue.setBackgroundResource(R.drawable.noselect_judge_image);
                        imgfasle.setBackgroundResource(R.drawable.select_judge_image);
//                        if (inbasebean.isAnswer() == false) {
                        Toast.makeText(activity, "错误", Toast.LENGTH_SHORT).show();
                        inbasebean.setJudgeAnswerFlag("mfalse");//错误回答标志
                        inbasebean.setSubmitAnswer("false");//学生答题答案
                        inbasebean.setAnswerflag("true");
                        submit.submitJudgeFragment(inbasebean);
//                        } else {
//                        Toast.makeText(activity, "已经答过题了", Toast.LENGTH_SHORT).show();
//                        }
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

            /*确保回答了*/
            if (inbasebean.getAnswerflag().equals("true")) {
                if (inbasebean.getJudgeAnswerFlag().equals("mtrue")) {
                    imgtrue.setBackgroundResource(R.drawable.select_judge_image);
                } else if (inbasebean.getJudgeAnswerFlag().equals("mfalse")) {
                    imgfasle.setBackgroundResource(R.drawable.select_judge_image);
                }

//                if (inbasebean.isAnswer() == true &&
//                        inbasebean.getSubmitAnswer().equals(inbasebean.getStandard_answer())) {
//                } else {
//
//                }
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

