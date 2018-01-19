package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApChoice;
import cn.dajiahui.kid.ui.homework.bean.BeChoiceOptions;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;


/**
 * 选择
 */

public class ChoiceFragment extends BaseHomeworkFragment implements CheckHomework {


    private cn.dajiahui.kid.ui.homework.view.OpenListView mListview;
    private ChoiceQuestionModle inbasebean;//当前
    private TextView tv_choice;

    private ImageView img_play;
    private SubmitChoiseFragment submit;
    private ApChoice apChoice;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_choice, null);}

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (ChoiceQuestionModle) bundle.get("ChoiceQuestionModle");}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
//        tv_choice.setText(inbasebean.getId() + "");

                  /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (inbasebean.getIs_answer().equals("0")) {
            List<BeChoiceOptions> options = inbasebean.getOptions();
            apChoice = new ApChoice(getActivity(), options);
            mListview.setAdapter(apChoice);
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "选择" + (position + 1) + "答案", Toast.LENGTH_SHORT).show();

//                    if (inbasebean.isAnswer() == false) {
                        inbasebean.setChoiceitemposition(position);//保存选择题当前item的索引 用于 翻页回来后指定某个item选择状态
                        apChoice.changeState(getActivity(), submit, position, inbasebean);

//                    } else {
//                        Toast.makeText(getActivity(), "已经答过题了", Toast.LENGTH_SHORT).show();

//                        return;
//                    }


                }
            });
        } else {

            /*上传答案回答过题了*/




        }


    }

    /*初始化*/
    private void initialize() {
        mListview = getView(R.id.listview);
        tv_choice = getView(R.id.tv_choice);
        img_play = getView(R.id.img_play);
           /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (inbasebean.getIs_answer().equals("0")) {
            img_play.setOnClickListener(onClick);
        }

    }

    private View.OnClickListener onClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_play:
                    Toast.makeText(activity, "播放音频", Toast.LENGTH_SHORT).show();
                    playMp3("/storage/emulated/0/test.mp3");

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
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (ChoiceQuestionModle) questionModle;
            if (inbasebean.getChoiceitemposition() >= 0) {
                /*刷新翻页回来后 上次答题情况*/
                apChoice.changeitemState(inbasebean, inbasebean.getChoiceitemposition(), mListview);
            }

        }
    }

    public interface SubmitChoiseFragment {
        public void submitChoiceFragment(ChoiceQuestionModle questionModle);
    }

}

