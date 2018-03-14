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
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionadapterItemModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.myinterface.SubmitEditext;
import cn.dajiahui.kid.ui.homework.view.HorizontalListView;


/**
 * 填空题
 */
public class CompletionFragment extends BaseHomeworkFragment implements CheckHomework, SubmitEditext, View.OnClickListener {

    private CompletionQuestionModle inbasebean;
    private SubmitCompletionFragment submit;
    private TextView tvcompletion, tv_schedule;
    private ImageView imgplay;
    private ImageView imgconment;
    private RelativeLayout horlistviewroot, stemroot;
    /////////////////
    private List<HorizontallListViewAdapter> mAllList = new ArrayList<>();//装每个HorizontalListView的适配器
    private List<HorizontalListView> mAllHorizontalListView = new ArrayList<>();//装每个HorizontalListView的适配器
    private Map<Integer, Map<Integer, String>> mAllMap = new HashMap<>();//存所有答案的集合（key： 第几个listview  val：listview对应的数据）

    private int mTop = 0;//初始距离上端
    private int mTvTop = 0;//初始距离上端
    private List<CompletionQuestionModle> mRightanswer = new ArrayList<>();//正确答案模型的集合


    private String mediaUrl;//音频地址

    private List<String> standardAnswerList = new ArrayList<>();//参考答案的集合
    private List<String> myAnswerList = new ArrayList<>();//我的答案的集合
    private Bundle bundle;

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

        /*成功上传答案之后 查看状态*/
        if (inbasebean.getIs_answered().equals("1")) {
            String my_answer = inbasebean.getMy_answer();
            if (!my_answer.equals("")) {
                /*多个空*/
                if (my_answer.contains("۞")) {
                    String[] strs = my_answer.split("۞");
                      /*截取我的答案  添加到mRightanswer集合*/
                    for (int i = 0, len = strs.length; i < len; i++) {
                        myAnswerList.add(strs[i].toString());
                    }
                } else {
                    myAnswerList.add(my_answer);
                }
            }
        }

        /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/
        String standard_answer = inbasebean.getStandard_answer();
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

                if (inbasebean.getIs_answered().equals("1") && !inbasebean.getMy_answer().equals("") && myAnswerList.size() > 0) {
                    CompletionQuestionadapterItemModle cqim = new CompletionQuestionadapterItemModle(String.valueOf(standardAnswerList.get(a).charAt(q)), String.valueOf(myAnswerList.get(a).charAt(q)));
                    /*如果所对应的值相等*/
                    if (String.valueOf(standardAnswerList.get(a).charAt(q)).equals(String.valueOf(myAnswerList.get(a).charAt(q)))) {
                        cqim.setShowItemRightColor(0);
                    } else {
                        cqim.setShowItemRightColor(1);
                    }
                    rightItemList.add(cqim);
                }

                rightList.add(rightItemList);
                completionQuestionModle.setShowRightList(rightList);

            }
            mRightanswer.add(completionQuestionModle);
        }

         /*判断是否已经上传后台 0 没答过题  1 答过题*/
        if (DoHomeworkActivity.sourceFlag.equals("HomeWork") && inbasebean.getIs_answered().equals("1")) {

            inbasebean.setIsFocusable("false");/*部顯示焦点*/
            inbasebean.setIsShowRightAnswer("yes");/*显示正确答案*/
        }

         /*添加题干*/
        addQuestionStem();

        /* size 填写有几道填空题 后台提供*/
        addHorizontalListView(standardAnswerList.size());

        /*初始化 每个adapter里的item的值*/
        for (int a = 0; a < standardAnswerList.size(); a++) {
            Map inputContainer = mAllList.get(a).getInputContainer();

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

            HorizontalListView horizontalListView = new HorizontalListView(getActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params.topMargin = mTop;
            params.leftMargin = 80;
            /*适配文本框的位置*/
            if (inbasebean.getIs_answered().equals("1")) {
                mTvTop += 300;
                mTop += 300;
            } else {
                mTvTop += 200;
                mTop += 200;
            }


            horizontalListView.setLayoutParams(params);
            Map<Integer, String> integerObjectMap = new HashMap();//每次都要new出来
            HorizontallListViewAdapter horizontallListViewAdapter = new HorizontallListViewAdapter(getActivity(), this, i, integerObjectMap, mRightanswer.get(i).getShowRightList(), inbasebean);
            horizontalListView.setAdapter(horizontallListViewAdapter);
            mAllList.add(horizontallListViewAdapter);
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
    }

    /*监听editext输入*/
    @Override

    public void submitEditextInfo(int selfposition) {

        inbasebean.setAnswerflag("true");

            /*填写答案之后，然后在翻页回来再修改答案的bug*/
        for (int i = 0; i < mAllList.size(); i++) {
            Map<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
            mAllMap.put(i, integerObjectMap);
        }
        mAllMap.put(selfposition, mAllList.get(selfposition).getInputContainer());// 获取每个适配的输入item的集合

        inbasebean.setmCompletionAllMap(mAllMap);

        submit.submitCompletionFragment(inbasebean);//通知activity这次的作答答案
    }

    /*是activity翻页后通知自己*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (CompletionQuestionModle) questionModle;
            /*作业翻页回来会走 submitHomework*/
//            if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                /*循环便利 所有适配器的集合 然后向适配器集合赋值 然后刷新adapter*/
                for (int i = 0; i < mAllList.size(); i++) {
                    Map<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
                    mAllList.get(i).setInputContainer(integerObjectMap);
                }
//            }
        }
    }


    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

