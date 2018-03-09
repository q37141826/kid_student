package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApChoice;
import cn.dajiahui.kid.ui.homework.bean.BeChoiceOptions;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.util.Logger;


/**
 * 选择
 */

public class ChoiceFragment extends BaseHomeworkFragment implements CheckHomework {


    private ListView mListview;
    private ChoiceQuestionModle inbasebean;//当前
    private TextView tv_choice;
    private ImageView img_play, img_conment;
    private SubmitChoiseFragment submit;
    private ApChoice apChoice;
    private String mediaUrl;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_choice, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (ChoiceQuestionModle) bundle.get("ChoiceQuestionModle");
        mediaUrl = inbasebean.getMedia();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        tv_choice.setText(inbasebean.getTitle());

       /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_conment);

        List<BeChoiceOptions> options = inbasebean.getOptions();
        apChoice = new ApChoice(getActivity(), options, inbasebean);

        /*listview的点击事件*/
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "选择" + (position + 1) + "答案", Toast.LENGTH_SHORT).show();
                inbasebean.setChoiceitemposition(position);//保存选择题当前item的索引 用于 翻页回来后指定某个item选择状态
              /*刷新ui*/
                apChoice.changeState(getActivity(), submit, position, inbasebean);

            }
        });

        /*设置适配器*/
        mListview.setAdapter(apChoice);
        /*设置listview的高度*/
        setHeight();
    }

    /*设置高度*/
    public void setHeight() {
        int height = 0;
        int count = apChoice.getCount();
        for (int i = 0; i < count; i++) {
            View temp = apChoice.getView(i, null, mListview);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.mListview.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height;
        mListview.setLayoutParams(params);
    }


    /*初始化*/
    private void initialize() {
        mListview = getView(R.id.listview);
        tv_choice = getView(R.id.tv_choice);
        img_play = getView(R.id.img_play);
        img_conment = getView(R.id.img_conment);
        img_play.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_play:
                    Toast.makeText(activity, "播放音频", Toast.LENGTH_SHORT).show();
                    playMp3(mediaUrl);

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

            if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                if (inbasebean.getChoiceitemposition() >= 0) {
                /*刷新翻页回来后 上次答题情况*/
                    apChoice.changeitemState(inbasebean.getChoiceitemposition(), mListview);
                }
            } else if (DoHomeworkActivity.sourceFlag.equals("Practice")) {


                  /*更新UI  进行对错判断机制*/

                  /*进行UI的样式书写  待续 imgtrue、 imgfasle 加遮罩层*/
                 /*回答正确*/
                if (inbasebean.getChoiceanswer().equals(inbasebean.getStandard_answer())) {
                    Logger.d("判断题-------------------------------------回答正确");
                } else {
                    Logger.d("判断题-------------------------------------回答错误");

                }
                Logger.d("inbasebean.getSubmitAnswer()" + inbasebean.getChoiceanswer());
            }


        }
    }

    public interface SubmitChoiseFragment {
        public void submitChoiceFragment(ChoiceQuestionModle questionModle);
    }

}

