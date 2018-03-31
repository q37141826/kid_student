package cn.dajiahui.kid.ui.study.kinds.practice;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionadapterItemModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.myinterface.SubmitEditext;
import cn.dajiahui.kid.ui.study.kinds.practice.adapter.ExHorizontallListViewAdapter;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExHorizontalListView;


/**
 * 填空题
 */
public class ExCompletionFragment extends ExBaseHomeworkFragment implements CheckHomework, SubmitEditext, View.OnClickListener {

    private CompletionQuestionModle inbasebean;
    private SubmitCompletionFragment submit;
    private TextView tvcompletion, tv_schedule;
    private ImageView imgplay;
    private ImageView imgconment;
    private RelativeLayout horlistviewroot, stemroot;
    /////////////////
    private List<ExHorizontallListViewAdapter> mAllAdapterList = new ArrayList<>();//装每个HorizontalListView的适配器
    private List<ExHorizontalListView> mAllHorizontalListView = new ArrayList<>();//装每个HorizontalListView的适配器
    private LinkedHashMap<Integer, LinkedHashMap<Integer, String>> mAllMap = new LinkedHashMap<>();//存所有答案的集合（key： 第几个listview  val：listview对应的数据）

    private int mTop = 0;//初始距离上端
    private int mTvTop = 0;//初始距离上端
    private List<CompletionQuestionModle> mRightanswer = new ArrayList<>();//正确答案模型的集合


    private String mediaUrl;//音频地址

    private List<String> standardAnswerList = new ArrayList<>();//参考答案的集合
    private List<String> myAnswerList = new ArrayList<>();//我的答案的集合
    private Bundle bundle;
    private String standard_answer;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_completion, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
        tvcompletion.setText(inbasebean.getTitle());
        tv_schedule.setText(bundle.getString("currntQuestion"));
       /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgconment);

        /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/
        standard_answer = inbasebean.getStandard_answer();
        String[] strs = standard_answer.split("۞");

       /*截取正确答案字符串  添加到mRightanswer集合*/
        for (int i = 0, len = strs.length; i < len; i++) {
            String split = strs[i].toString();
            standardAnswerList.add(split);
        }

        /*拆解正确答案每个单词的字母*/
        for (int a = 0; a < standardAnswerList.size(); a++) {
            /*填空题数据模型*/
            CompletionQuestionModle completionQuestionModle = new CompletionQuestionModle();
            List<List<CompletionQuestionadapterItemModle>> rightList = new ArrayList();
            for (int q = 0; q < standardAnswerList.get(a).length(); q++) {

                List<CompletionQuestionadapterItemModle> rightItemList = new ArrayList();

                CompletionQuestionadapterItemModle cqim = new CompletionQuestionadapterItemModle();
                rightItemList.add(cqim);
                rightList.add(rightItemList);
                completionQuestionModle.setShowRightList(rightList);

            }
            mRightanswer.add(completionQuestionModle);
        }

         /*添加题干*/
        addQuestionStem();

        /* size 填写有几道填空题 后台提供*/
        addHorizontalListView(standardAnswerList.size());

        /*初始化 每个adapter里的item的值*/
        for (int a = 0; a < standardAnswerList.size(); a++) {
            Map inputContainer = mAllAdapterList.get(a).getInputContainer();

            for (int q = 0; q < standardAnswerList.get(a).length(); q++) {
                inputContainer.put(q, "㊒");
            }
        }
    }

    /*添加填空题题干*/
    private void addQuestionStem() {
        TextView textView = new TextView(getActivity());
        textView.setTextSize(15);
        textView.setText(Html.fromHtml(inbasebean.getOptions()));
        stemroot.addView(textView);
    }


    /*添加布局*/
    @SuppressLint("ResourceType")
    private void addHorizontalListView(int size) {

        for (int i = 0; i < size; i++) {
            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            RelativeLayout.LayoutParams tvparams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            TextView textView = new TextView(getActivity());
            textView.setText((i + 1) + ".");
            textView.setTextSize(25);
            tvparams.topMargin = mTvTop;
            textView.setLayoutParams(tvparams);

            ExHorizontalListView horizontalListView = new ExHorizontalListView(getActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params.topMargin = mTop;
            params.leftMargin = 80;
            /*适配文本框的位置*/
            mTvTop += 300;
            mTop += 300;

            horizontalListView.setLayoutParams(params);
            Map<Integer, String> integerObjectMap = new HashMap();//每次都要new出来
            ExHorizontallListViewAdapter horizontallListViewAdapter = new ExHorizontallListViewAdapter(getActivity(), this, i, integerObjectMap, mRightanswer.get(i).getShowRightList(), inbasebean);
            horizontalListView.setAdapter(horizontallListViewAdapter);
            mAllAdapterList.add(horizontallListViewAdapter);
            mAllHorizontalListView.add(horizontalListView);

            relativeLayout.addView(textView);
            relativeLayout.addView(horizontalListView);

            horlistviewroot.addView(relativeLayout);
        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.img_play:
                playMp3(mediaUrl);
                break;
            default:
                break;
        }
    }

    /*先走setArguments 在走onPageSelected中的函数 最后走 submitHomework*/
    @Override
    public void setArguments(Bundle bundle) {

        this.bundle = bundle;
        inbasebean = (CompletionQuestionModle) bundle.get("CompletionQuestionModle");
        inbasebean.setEachposition(bundle.getInt("position"));
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
        tv_schedule = getView(R.id.tv_schedule);
        horlistviewroot = getView(R.id.horlistviewroot);
        stemroot = getView(R.id.stemroot);
        imgplay.setOnClickListener(this);
        imgplay.setBackground(animationDrawable);
    }

    /*监听editext输入*/
    @Override

    public void submitEditextInfo(int selfposition,LinkedHashMap<Integer, String> inputContainer ) {
//        Logger.d("确认输入----------------：" + selfposition);
        inbasebean.setAnswerflag("true");

        /*填写答案之后，然后在翻页回来再修改答案的bug*/
        for (int i = 0; i < mAllAdapterList.size(); i++) {
            LinkedHashMap<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
            mAllMap.put(i, integerObjectMap);
        }
        mAllMap.put(selfposition, mAllAdapterList.get(selfposition).getInputContainer());// 获取每个适配的输入item的集合

        inbasebean.setmCompletionAllMap(mAllMap);

        submit.submitCompletionFragment(inbasebean);//通知activity这次的作答答案
    }

    /*Chexk 通知碎片*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (CompletionQuestionModle) questionModle;
            inbasebean.setIsFocusable("no");/*部顯示焦点*/
            inbasebean.setIsShowRightAnswer("yes");/*显示正确答案*/

            myAnswerList.clear();
            mRightanswer.clear();


            /*获取我的答案*/
            for (int i = 0; i < mAllAdapterList.size(); i++) {
                Map<Integer, String> integerStringMap = inbasebean.getmCompletionAllMap().get(i);

                StringBuffer buffer = new StringBuffer();
                for (int q = 0; q < integerStringMap.size(); q++) {
                    buffer.append(integerStringMap.get(q));
                }
                myAnswerList.add(buffer.toString());
            }
            /*拼接的我的答案*/
//            Logger.d("拼接的我的答案--myAnswerList:" + myAnswerList);

            for (int a = 0; a < standardAnswerList.size(); a++) {
               /*填空题数据模型*/
                CompletionQuestionModle completionQuestionModle = new CompletionQuestionModle();
                List<List<CompletionQuestionadapterItemModle>> rightList = new ArrayList();
                for (int q = 0; q < standardAnswerList.get(a).length(); q++) {
                    List<CompletionQuestionadapterItemModle> rightItemList = new ArrayList();
                    CompletionQuestionadapterItemModle cqim = new CompletionQuestionadapterItemModle(
                            String.valueOf(standardAnswerList.get(a).charAt(q)),
                            String.valueOf(myAnswerList.get(a).charAt(q)));
                    /*如果所对应的值相等*/
                    if (String.valueOf(standardAnswerList.get(a).charAt(q)).
                            equals(String.valueOf(myAnswerList.get(a).charAt(q)))) {
                        cqim.setShowItemRightColor(0);
                    } else {
                        cqim.setShowItemRightColor(1);
                    }

                    rightItemList.add(cqim);
                    rightList.add(rightItemList);

                    completionQuestionModle.setShowRightList(rightList);

                }
                mRightanswer.add(completionQuestionModle);
            }

           /*循环便利 所有适配器的集合 然后向适配器集合赋值 然后刷新adapter*/
            for (int i = 0; i < mAllAdapterList.size(); i++) {
                LinkedHashMap<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
                mAllAdapterList.get(i).setInputContainer(integerObjectMap, inbasebean, mRightanswer.get(i).getShowRightList());

            }


        }
    }

    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

