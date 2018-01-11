package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApChoice;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.options;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;


/**
 * 选择
 */

public class ChoiceFragment extends BaseHomeworkFragment implements CheckHomework {


    private ListView mListview;
    private QuestionModle inbasebean;
    private TextView tvtest, tv_1;
    private ChoiceFragment.GetMediaPlayer Mp3;
    private ImageView img_play;
    private SubmitChoiseFragment submit;
    private ApChoice apChoice;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_choice, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (QuestionModle) bundle.get("ChoiceQuestionModle");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        tv_1.setText(inbasebean.getId() + "");

        List<options> options = inbasebean.getOptions();

        apChoice = new ApChoice(getActivity(), options);
        mListview.setAdapter(apChoice);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "选择" + (position + 1) + "答案", Toast.LENGTH_SHORT).show();

                if (inbasebean.isisAnswer() == false) {
                    inbasebean.setChoiceitemposition(position);
                    apChoice.changeState(getActivity(), submit, position, inbasebean);

                } else {
                    Toast.makeText(getActivity(), "已经答过题了", Toast.LENGTH_SHORT).show();

                    return;
                }


            }
        });
    }

    /*初始化*/
    private void initialize() {
        mListview = getView(R.id.listview);
        tvtest = getView(R.id.choice);
        tv_1 = getView(R.id.tv_1);
        img_play = getView(R.id.img_play);
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
                        Mp3 = (BaseHomeworkFragment.GetMediaPlayer) getActivity();
                        Mp3.getMediaPlayer(mediaPlayer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitChoiseFragment) activity;

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

    @Override
    public void submitHomework(QuestionModle questionModle) {
        if (questionModle != null) {
            inbasebean = questionModle;
            if (inbasebean.getChoiceitemposition() >= 0) {
                apChoice.changeitemState(inbasebean, questionModle.getChoiceitemposition(),mListview);
            }

        }

    }

    public interface SubmitChoiseFragment {
        public void submitChoiceFragment(QuestionModle questionModle);
    }


}

