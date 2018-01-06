package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeJudge;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.util.Logger;


/**
 * 判断题
 */
public class JudgeFragment extends BaseHomeworkFragment implements CheckHomework {


    private TextView tv1;
    private ImageView imgconment, img_play, imgtrue, ingfasle;
    private SubmitJudgeFragment submit;
    private GetMediaPlayer Mp3;
    private boolean ischeck = false;
    private MediaPlayer mediaPlayer;
    private BeJudge inbeJudge;
//    private BaseBean beJudge;//activity传过来的模型


    @Override
    protected View initinitLayout(LayoutInflater inflater) {

        return inflater.inflate(R.layout.fr_judge, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbeJudge = (BeJudge) bundle.get("beJudge");


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        mediaPlayer = new MediaPlayer();

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitJudgeFragment) activity;
        Mp3 = (GetMediaPlayer) activity;
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
//        Log.d("majin", " ReadFragment onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
//        mediaPlayer.stop();
//        Log.d("majin", " ReadFragment onPause  ;
    }


    private void initialize() {
        tv1 = getView(R.id.tv_1);
        imgconment = getView(R.id.img_conment);
        imgtrue = getView(R.id.img_true);
        ingfasle = getView(R.id.img_fasle);
        img_play = getView(R.id.img_play);

        imgtrue.setOnClickListener(onClick);
        ingfasle.setOnClickListener(onClick);
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
                        mediaPlayer.setDataSource("/storage/emulated/0/test.mp3");
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        Mp3 = (GetMediaPlayer) getActivity();
                        Mp3.getMediaPlayer(mediaPlayer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.img_true:

                    if (inbeJudge.isWhetheranswer() == false) {
                        Toast.makeText(activity, "正确", Toast.LENGTH_SHORT).show();
                        inbeJudge.setAnswer(true);
                        submit.submitJudgeFragment(inbeJudge);
                    } else {
                        Toast.makeText(activity, "已经答过题了", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.img_fasle:
                    if (inbeJudge.isWhetheranswer() == false) {//实力不对
                        Toast.makeText(activity, "错误", Toast.LENGTH_SHORT).show();
                        inbeJudge.setAnswer(false);
                        submit.submitJudgeFragment(inbeJudge);
                    } else {
                        Toast.makeText(activity, "已经答过题了", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.tv_1:
                    if (!ischeck) {
                        tv1.setText("被点击1！");
                        ischeck = !ischeck;
                    } else {
                        tv1.setText("被点击2！");
                        ischeck = !ischeck;
                    }
                    submit = (SubmitJudgeFragment) getActivity();

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void submitHomework(BeJudge beJudge) {
        Logger.d("majin", "传入f1的:" +beJudge.isWhetheranswer() );
        inbeJudge.setWhetheranswer(beJudge.isWhetheranswer());

    }

    public interface SubmitJudgeFragment {
        public void submitJudgeFragment(BeJudge beJudge);
    }

    public interface GetMediaPlayer {
        public void getMediaPlayer(MediaPlayer mediaPlayer);
    }
}

