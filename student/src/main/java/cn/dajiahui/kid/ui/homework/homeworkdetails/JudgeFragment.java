package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.util.Logger;


/**
 * 判断题
 */
public class JudgeFragment extends BaseHomeworkFragment implements CheckHomework {


    private TextView tv1;
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

        tv1.setText(inbasebean.getId() + "");
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitJudgeFragment) activity;
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
//        Log.d("majin", " ReadFragment onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
//        Log.d("majin", " ReadFragment onPause  ;
    }


    private void initialize() {
        tv1 = getView(R.id.tv_1);
        imgconment = getView(R.id.img_conment);
        imgtrue = getView(R.id.img_true);
        imgfasle = getView(R.id.img_fasle);
        img_play = getView(R.id.img_play);

        imgtrue.setOnClickListener(onClick);
        imgfasle.setOnClickListener(onClick);
        tv1.setOnClickListener(onClick);
        img_play.setOnClickListener(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_play:
                    Toast.makeText(activity, "播放音频", Toast.LENGTH_SHORT).show();
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        Mp3 = (BaseHomeworkFragment.GetMediaPlayer) getActivity();
                        Mp3.getMediaPlayer(mediaPlayer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.img_true:
                    if (inbasebean != null) {
                        if (inbasebean.isAnswer() == false) {
                            Toast.makeText(activity, "正确", Toast.LENGTH_SHORT).show();
                            inbasebean.setSubmitAnswer("true");
                            inbasebean.setAnswerflag("true");//答题标志
                            submit.submitJudgeFragment(inbasebean);
                        } else {
                            Toast.makeText(activity, "已经答过题了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.img_fasle:
                    if (inbasebean != null) {
                        if (inbasebean.isAnswer() == false) {
                            Toast.makeText(activity, "错误", Toast.LENGTH_SHORT).show();
                            inbasebean.setSubmitAnswer("false");
                            inbasebean.setAnswerflag("true");
                            submit.submitJudgeFragment(inbasebean);
                        } else {
                            Toast.makeText(activity, "已经答过题了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case R.id.tv_1:


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

                if (inbasebean.isAnswer() == true &&
                        inbasebean.getSubmitAnswer().equals(inbasebean.getStandard_answer())) {
                    imgtrue.setImageResource(R.color.btn_green_noraml);
                    imgfasle.setImageResource(R.color.red);
                    Logger.d("majin", "回答正确");

                } else {
                    imgtrue.setImageResource(R.color.red);
                    imgfasle.setImageResource(R.color.btn_green_noraml);
                    Logger.d("majin", "回答错误");
                }
            }
        }

    }

    public interface SubmitJudgeFragment {
        public void submitJudgeFragment(JudjeQuestionModle questionModle);
    }
}

