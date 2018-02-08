package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
    private RelativeLayout horlistviewroot, stemroot;
    /////////////////
    private List<HorizontallListViewAdapter> mAllList = new ArrayList<>();//装每个HorizontalListView的适配器
    private List<HorizontalListView> mAllHorizontalListView = new ArrayList<>();//装每个HorizontalListView的适配器
    private Map<Integer, Map<Integer, String>> mAllMap = new HashMap<>();//存所有答案的集合（key： 第几个listview  val：listview对应的数据）

    private int mTop = 0;//初始距离上端
    private List<CompletionQuestionModle> rightanswer = new ArrayList<>();//正确答案的集合

    private List<String> myanswer = new ArrayList<>();//我的答案
    private String mediaUrl;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_completion, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
        tvcompletion.setText(inbasebean.getTitle());
       /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgconment);

        /*模拟我的答案数据*/
        myanswer.add("m");
        myanswer.add("a");
        myanswer.add("c");
        myanswer.add("t");



        /*解析正确答案（后台获取的正确答案）*/
        String standard_answer = inbasebean.getStandard_answer();
        String[] strs = standard_answer.split(",");
       /*截取正确答案字符串  添加到rightanswer集合*/
        for (int i = 0, len = strs.length; i < len; i++) {
            String split = strs[i].toString();
            CompletionQuestionModle completionQuestionModle = new CompletionQuestionModle();
            completionQuestionModle.setAnalysisAnswer(split);
            completionQuestionModle.setAnalysisMineAnswer(myanswer.get(i));
            rightanswer.add(completionQuestionModle);
        }


           /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (DoHomeworkActivity.sourceFlag.equals("HomeWork") && inbasebean.getIs_answer().equals("1")) {

            inbasebean.setIsFocusable("false");
            inbasebean.setIsShowRightAnswer("yes");
            for (int a = 0; a < myanswer.size(); a++) {
                /*填空对应的答案相等*/
                if (rightanswer.get(a).getAnalysisAnswer().equals(myanswer.get(a))) {
                    rightanswer.get(a).setTextcolor("green");
                }
            }

        }
        /*添加题干*/
        addQuestionStem();

         /* size 填写有几道填空题 后台提供*/
        addHorizontalListView(1);
    }

    /*添加填空题题干*/
    private void addQuestionStem() {
        TextView textView = new TextView(getActivity());
        textView.setText("1." + Html.fromHtml( inbasebean.getOptions()));
        stemroot.addView(textView);
    }

    /*准备数据源*/
    private Map<Integer, CompletionQuestionModle> getData() {

        Map<Integer, CompletionQuestionModle> map = new HashMap<>();

        for (int i = 0; i < rightanswer.size(); i++) {
//            Logger.d( "准备数据源：" + rightanswer.get(i).getAnalysisAnswer());
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

            Map<Integer, String> integerObjectMap = new HashMap();//每次都要new出来

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

    /*先走setArguments 在走onPageSelected中的函数 最后走 submitHomework*/
    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (CompletionQuestionModle) bundle.get("CompletionQuestionModle");
        mediaUrl = inbasebean.getMedia();
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
        horlistviewroot = getView(R.id.horlistviewroot);
        stemroot = getView(R.id.stemroot);
        imgplay.setOnClickListener(this);
    }

    /*监听editext输入*/
    @Override
    public void submitEditextInfo(int selfposition) {
        if (inbasebean.isAnswer() == false) {
            inbasebean.setAnswerflag("true");
            mAllMap.put(selfposition, mAllList.get(selfposition).getInputContainer());//mAllList.get(selfposition).getInputContainer() 获取每个适配的editext的数据集合
            inbasebean.setmAllMap(mAllMap);
            submit.submitCompletionFragment(inbasebean);//通知activity这次的作答答案
        }
    }

    /*是activity翻页后通知自己*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (CompletionQuestionModle) questionModle;
            /*作业翻页回来会走 submitHomework*/
            if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
//                Logger.d("-----------翻页回来之后的" + inbasebean.getmAllMap().toString());
                /*循环便利 所有适配器的集合 然后向适配器集合赋值 然后刷新adapter*/
                for (int i = 0; i < mAllList.size(); i++) {
                    Map<Integer, String> integerObjectMap = inbasebean.getmAllMap().get(i);
                    mAllList.get(i).setInputContainer(integerObjectMap);

                }

              /*判断是否已经上传后台 0 没答过题  1 答过题*/
            }
            /*练习check之后会走 submitHomework*/
            else if (DoHomeworkActivity.sourceFlag.equals("Practice") && inbasebean.getAnswerflag().equals("true")) {

                Logger.d("-------33---- " + inbasebean.getmAllMap().toString());

                myanswer.clear();
               /*取出我的答案*/
                for (int i = 0; i < inbasebean.getmAllMap().size(); i++) {
                    Map<Integer, String> integerStringMap = inbasebean.getmAllMap().get(i);
                    for (int w = 0; w < integerStringMap.size(); w++) {
                        myanswer.add(integerStringMap.get(w));
                    }
                }

                Logger.d("myanswer:" + myanswer.toString());

                Logger.d("rightanswer:" + rightanswer.toString());


                for (int i = 0; i < mAllList.size(); i++) {

                    ;

                    for (int a = 0; a < myanswer.size(); a++) {
                    /*填空对应的答案相等*/
                        if (rightanswer.get(a).getAnalysisAnswer().equals(myanswer.get(a))) {
                            rightanswer.get(a).setTextcolor("green");
                        }
                    }

                    mAllList.get(i).setmList(getData());
                    mAllList.get(i).notifyDataSetChanged();

                }


            }

        }
    }


    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

