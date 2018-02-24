package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.annotation.SuppressLint;
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
    //    private LinearLayout ;
    /////////////////
    private List<HorizontallListViewAdapter> mAllList = new ArrayList<>();//装每个HorizontalListView的适配器
    private List<HorizontalListView> mAllHorizontalListView = new ArrayList<>();//装每个HorizontalListView的适配器
    private Map<Integer, Map<Integer, String>> mAllMap = new HashMap<>();//存所有答案的集合（key： 第几个listview  val：listview对应的数据）

    private int mTop = 0;//初始距离上端
    private int mToptv = 0;//初始距离上端
    private List<CompletionQuestionModle> mRightanswer = new ArrayList<>();//正确答案的集合

    private List<String> myanswer = new ArrayList<>();//我的答案
    private String mediaUrl;

    private List<String> leterList = new ArrayList<>();


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


        /*模拟我的答案数据 是自己答过题之后器请求服务器之后拆解的我的答案*/

//        myanswer.add("m");
//        myanswer.add("a");
//        myanswer.add("c");
//        myanswer.add("t");



        /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/
        String standard_answer = inbasebean.getStandard_answer();
        String[] strs = standard_answer.split("۞");


       /*截取正确答案字符串  添加到mRightanswer集合*/
        for (int i = 0, len = strs.length; i < len; i++) {
            String split = strs[i].toString();
            leterList.add(split);
        }


        /*拆解每个单词的字母*/
        for (int a = 0; a < leterList.size(); a++) {

            /*填空题数据模型*/
            CompletionQuestionModle completionQuestionModle = new CompletionQuestionModle();

            for (int q = 0; q < leterList.get(a).length(); q++) {
                completionQuestionModle.setAnalysisAnswer(String.valueOf(leterList.get(a).charAt(q)));

//                if (myanswer.size() > 0) {
//                    completionQuestionModle.setAnalysisMineAnswer(myanswer.get(q));
//                }
                completionQuestionModle.setLetterNum(leterList.get(a).length());
            }

            mRightanswer.add(completionQuestionModle);

        }

         /*添加题干*/
        addQuestionStem();

        /* size 填写有几道填空题 后台提供*/
        addHorizontalListView(leterList.size());


//          /*判断是否已经上传后台 0 没答过题  1 答过题*/
//        if (DoHomeworkActivity.sourceFlag.equals("HomeWork") && inbasebean.getIs_answer().equals("1")) {
//
//            inbasebean.setIsFocusable("false");
//            inbasebean.setIsShowRightAnswer("yes");
//            for (int a = 0; a < myanswer.size(); a++) {
//                /*填空对应的答案相等*/
//                if (mRightanswer.get(a).getAnalysisAnswer().equals(myanswer.get(a))) {
//                    mRightanswer.get(a).setTextcolor("green");
//                }
//            }
//
//        }


    }

    /*添加填空题题干*/
    private void addQuestionStem() {
        TextView textView = new TextView(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 21);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        textView.setLayoutParams(params);
        textView.setTextSize(15);
        textView.setText(Html.fromHtml(inbasebean.getOptions()));
        stemroot.addView(textView);
    }

//    /*准备数据源是每个横划的listview的集合数据*/
//    private Map<Integer, CompletionQuestionModle> getData() {
//
//        Map<Integer, CompletionQuestionModle> map = new HashMap<>();
//
//        Logger.d("集和长度：" + mRightanswer.size());
//
//
//            /*不对*/
//        for (int i = 0; i < mRightanswer.size(); i++) {
//
//            for (int q = 0; q < leterList.get(i).length(); q++) {
//                CompletionQuestionModle com = new CompletionQuestionModle();
//                com.setAnalysisAnswer(mRightanswer.get(i).getAnalysisAnswer());
//                com.setAnalysisMineAnswer(mRightanswer.get(i).getAnalysisMineAnswer());
//                com.setTextcolor(mRightanswer.get(i).getTextcolor());
//
//                map.put(q, com);
//            }
//        }
//        return map;
//    }

    /*添加布局*/
    @SuppressLint("ResourceType")
    private void addHorizontalListView(int size) {

        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params1.weight = 1;
            params1.topMargin = mToptv;

            TextView tv = new TextView(getActivity());
            tv.setTextSize(15);
            mToptv += 150;

            horlistviewroot.addView(tv);

            HorizontalListView horizontalListView = new HorizontalListView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params.weight = 1;
            params.topMargin = mTop;
            params.leftMargin = 30;
            mTop += 150;

            horizontalListView.setLayoutParams(params);

            Map<Integer, String> integerObjectMap = new HashMap();//每次都要new出来

            HorizontallListViewAdapter horizontallListViewAdapter = new HorizontallListViewAdapter(getActivity(), this, i, integerObjectMap, mRightanswer.get(i).getLetterNum(), inbasebean);
            horizontalListView.setAdapter(horizontallListViewAdapter);
            mAllList.add(horizontallListViewAdapter);
            mAllHorizontalListView.add(horizontalListView);

            horlistviewroot.addView(horizontalListView);
        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.img_play:
                playMp3(inbasebean.getMedia());
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

            /*填写答案之后，然后在翻页回来再修改答案的bug*/
            for (int i = 0; i < mAllList.size(); i++) {
                Map<Integer, String> integerObjectMap = inbasebean.getmAllMap().get(i);
                mAllMap.put(i, integerObjectMap);
            }

            Logger.d("listview的数据：" + mAllList.get(selfposition).getInputContainer().toString());
            mAllMap.put(selfposition, mAllList.get(selfposition).getInputContainer());// 获取每个适配的输入item的集合
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
                Logger.d("-----------翻页回来之后的" + inbasebean.getmAllMap().toString());
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

//                myanswer.clear();
//               /*取出我的答案*/
//                for (int i = 0; i < inbasebean.getmAllMap().size(); i++) {
//                    Map<Integer, String> integerStringMap = inbasebean.getmAllMap().get(i);
//                    for (int w = 0; w < integerStringMap.size(); w++) {
//                        myanswer.add(integerStringMap.get(w));
//                    }
//                }

//                for (int i = 0; i < mAllList.size(); i++) {
//
//                    for (int a = 0; a < myanswer.size(); a++) {
//                    /*填空对应的答案相等*/
//                        if (mRightanswer.get(a).getAnalysisAnswer().equals(myanswer.get(a))) {
//                            mRightanswer.get(a).setTextcolor("green");
//                        }
//                    }
//
////                    mAllList.get(i).setmList(getData());
////                    mAllList.get(i).notifyDataSetChanged();
//
//                }


            }

        }
    }


    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

