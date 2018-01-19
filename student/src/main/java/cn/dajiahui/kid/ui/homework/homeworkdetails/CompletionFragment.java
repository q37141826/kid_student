package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.HorizontallListViewAdapter;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.myinterface.SubmitEditext;
import cn.dajiahui.kid.ui.homework.view.HorizontalListView;
import cn.dajiahui.kid.util.Logger;


/**
 * 填空题
 */
public class CompletionFragment extends BaseHomeworkFragment implements CheckHomework, SubmitEditext, View.OnClickListener {

    private CompletionQuestionModle inbasebean;
    private SubmitCompletionFragment submit;
    private TextView tvcompletion;
    private ImageView imgplay;
    private ImageView imgconment;
    private RelativeLayout relaroot, horlistviewroot;
    /////////////////
    private List<HorizontallListViewAdapter> mAllList = new ArrayList<>();//装每个HorizontalListView的适配器
    private List<HorizontalListView> mAllHorizontalListView = new ArrayList<>();//装每个HorizontalListView的适配器
    private Map<Integer, Map<Integer, Object>> mAllMap = new HashMap<>();//存所有答案的集合

    private int mTop = 0;//初始距离上端
    private Map<Integer, Object> integerObjectMap;
    private List<CompletionQuestionModle> rightanswer;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_completion, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
        /*模拟我的答案*/
        List<String> myanswer = new ArrayList<>();
        rightanswer = new ArrayList<>();
        myanswer.add("m");
        myanswer.add("a");
        myanswer.add("c");
        myanswer.add("t");


        /*正确答案*/
        String standard_answer = inbasebean.getStandard_answer();
        String[] strs = standard_answer.split(",");
       /*截取字符串*/
        for (int i = 0, len = strs.length; i < len; i++) {
            String split = strs[i].toString();
            CompletionQuestionModle completionQuestionModle = new CompletionQuestionModle();
            completionQuestionModle.setAnalysisAnswer(split);
            completionQuestionModle.setAnalysisMineAnswer(myanswer.get(i));
            rightanswer.add(completionQuestionModle);
        }


           /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (inbasebean.getIs_answer().equals("1")) {
//            Logger.d("majin", "rightanswer   ：" + rightanswer.toString());
//            Logger.d("majin", "myanswer   ：" + myanswer.toString());


        } else {
            inbasebean.setIsFocusable("false");
            inbasebean.setIsShowRightAnswer("yes");
            for (int a = 0; a < myanswer.size(); a++) {
                /*填空对应的答案相等*/
                if (rightanswer.get(a).getAnalysisAnswer().equals(myanswer.get(a))) {
                    Logger.d("majin", "相等的：" + a);
                    rightanswer.get(a).setTextcolor("green");
                }
            }
        }

        addHorizontalListView(2);

    }

    /*准备数据源*/
    private Map<Integer, CompletionQuestionModle> getData() {

        Map<Integer, CompletionQuestionModle> map = new HashMap<>();

        for (int i = 0; i < rightanswer.size(); i++) {
//            Logger.d("majin", "准备数据源：" + rightanswer.get(i).getAnalysisAnswer());
            CompletionQuestionModle com = new CompletionQuestionModle();
            com.setAnalysisAnswer(rightanswer.get(i).getAnalysisAnswer());
            com.setAnalysisMineAnswer(rightanswer.get(i).getAnalysisMineAnswer());
            com.setTextcolor(rightanswer.get(i).getTextcolor());
            map.put(i, com);
        }
        return map;

    }

    /*添加布局*/
    private void addHorizontalListView(int size) {

        for (int i = 0; i < size; i++) {
            HorizontalListView horizontalListView = new HorizontalListView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params.weight = 1;
            params.topMargin = mTop;
            params.leftMargin = 50;
            params.rightMargin = 50;
            mTop += 200;

            horizontalListView.setLayoutParams(params);
            /*沒有翻过页 要新new出来*/
            if (inbasebean.getmAllMap() == null) {
                integerObjectMap = new HashMap<Integer, Object>();

            } else {
                if (inbasebean.getmAllMap().get(i) != null) {
                    integerObjectMap = inbasebean.getmAllMap().get(i);
                } else {
                    integerObjectMap = new HashMap<Integer, Object>();
                }

            }
            HorizontallListViewAdapter horizontallListViewAdapter = new HorizontallListViewAdapter(getActivity(), this, i, integerObjectMap, getData(), inbasebean);
            horizontalListView.setAdapter(horizontallListViewAdapter);
            mAllList.add(horizontallListViewAdapter);
            mAllHorizontalListView.add(horizontalListView);
            horlistviewroot.addView(horizontalListView); //动态添加图片
        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.img_play:
                playMp3("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");


                break;


            default:
                break;
        }
    }

    @Override
    public void setArguments(Bundle bundle) {
        Logger.d("majin", "11111111111111");
        inbasebean = (CompletionQuestionModle) bundle.get("CompletionQuestionModle");
    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitCompletionFragment) activity;

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

    /*初始化*/
    private void initialize() {
        tvcompletion = getView(R.id.tv_completion);
        imgplay = getView(R.id.img_play);
        imgconment = getView(R.id.img_conment);
        relaroot = getView(R.id.relaroot);
        horlistviewroot = getView(R.id.horlistviewroot);
        imgplay.setOnClickListener(this);
    }

    /*监听editext输入*/
    @Override
    public void submitEditextInfo(int selfposition) {
        mAllMap.put(selfposition, mAllList.get(selfposition).getInputContainer());
        inbasebean.setmAllMap(mAllMap);
        submit.submitCompletionFragment(inbasebean);//通知activity这次的作答答案
    }

    /*是activity翻页后通知自己*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (CompletionQuestionModle) questionModle;
        }
    }


    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

