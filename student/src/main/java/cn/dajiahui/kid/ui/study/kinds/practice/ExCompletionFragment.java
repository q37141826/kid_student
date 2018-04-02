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
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.util.BaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.HorizontallListViewAdapter;
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
    private LinkedHashMap<Integer, LinkedHashMap<Integer, CompletionQuestionadapterItemModle>> mAllMap = new LinkedHashMap<>();//存所有答案的集合（key： 第几个listview  val：listview对应的数据）

    private int mTop = 0;//初始距离上端
    private int mTvTop = 0;//初始距离上端
    private List<CompletionQuestionModle> mRightanswer = new ArrayList<>();//正确答案模型的集合


    private String mediaUrl;//音频地址
    private Bundle bundle;
    private int screenWidth;

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

        /*添加题干*/
        addQuestionStem();
        /*添加布局*/
        addHorizontalListView(inbasebean.getmCompletionAllMap().size());

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

            mTvTop += screenWidth / 4;
            mTop += screenWidth / 4;

            horizontalListView.setLayoutParams(params);

            ExHorizontallListViewAdapter horizontallListViewAdapter = new ExHorizontallListViewAdapter(getActivity(), this, i, inbasebean);
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
        //获取屏幕宽度
        screenWidth = BaseUtil.getWidthPixels(getActivity());
    }

    /*监听editext输入*/
    @Override

    public void submitEditextInfo(int selfposition, LinkedHashMap<Integer,
            CompletionQuestionadapterItemModle> inputContainer,
                                  int position, String itemValue) {

        inbasebean.setAnswerflag("true");
        inbasebean.getmCompletionAllMap().get(selfposition).get(position).setShowItemMy(itemValue);
        submit.submitCompletionFragment(inbasebean);//通知activity这次的作答答案

    }

    /*Chexk 通知碎片*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (CompletionQuestionModle) questionModle;
            inbasebean.setIsFocusable("no");/*部顯示焦点*/
            inbasebean.setIsShowRightAnswer("yes");/*显示正确答案*/
            /*根据正确答案初始化我的答案*/
            /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/
            /*多个题*/

            String standard_answer = inbasebean.getStandard_answer();

            if (standard_answer.contains("۞")) {

                String[] strsTrue = standard_answer.split("۞");
                for (int i = 0, len = inbasebean.getmCompletionAllMap().size(); i < len; i++) {
                    LinkedHashMap<Integer, CompletionQuestionadapterItemModle> mItemMap = new LinkedHashMap<>();//每个横滑dadpter的数据
                    for (int b = 0; b < inbasebean.getmCompletionAllMap().get(i).size(); b++) {
                        String sTrue = String.valueOf(strsTrue[i].charAt(b));
                        String sMine = inbasebean.getmCompletionAllMap().get(i).get(b).getShowItemMy();

                        if (sTrue.equals(sMine)) {
                            mItemMap.put(b, new CompletionQuestionadapterItemModle(sTrue, sMine, 0));
                        } else {
                            mItemMap.put(b, new CompletionQuestionadapterItemModle(sTrue, sMine, 1));
                        }

                    }

                    inbasebean.getmCompletionAllMap().put(i, mItemMap);
                }
            }
            /*循环便利 所有适配器的集合 然后向适配器集合赋值 然后刷新adapter*/
            for (int i = 0; i < mAllAdapterList.size(); i++) {
                LinkedHashMap<Integer, CompletionQuestionadapterItemModle> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
                mAllAdapterList.get(i).setInputContainer(integerObjectMap, inbasebean);

            }


        }
    }

    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

