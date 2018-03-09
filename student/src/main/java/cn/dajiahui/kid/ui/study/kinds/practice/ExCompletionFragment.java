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
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionadapterItemModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.myinterface.SubmitEditext;
import cn.dajiahui.kid.ui.study.kinds.practice.adapter.ExHorizontallListViewAdapter;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExHorizontalListView;
import cn.dajiahui.kid.util.Logger;


/**
 * 填空题
 */
public class ExCompletionFragment extends ExBaseHomeworkFragment implements CheckHomework, SubmitEditext, View.OnClickListener {

    private CompletionQuestionModle inbasebean;
    private SubmitCompletionFragment submit;
    private TextView tvcompletion;
    private ImageView imgplay;
    private ImageView imgconment;
    private RelativeLayout horlistviewroot, stemroot;
    /////////////////
    private List<ExHorizontallListViewAdapter> mAllAdapterList = new ArrayList<>();//装每个HorizontalListView的适配器
    private List<ExHorizontalListView> mAllHorizontalListView = new ArrayList<>();//装每个HorizontalListView的适配器
    private Map<Integer, Map<Integer, String>> mAllMap = new HashMap<>();//存所有答案的集合（key： 第几个listview  val：listview对应的数据）

    private int mTop = 0;//初始距离上端
    private int mToptv = 0;//初始距离上端
    private List<CompletionQuestionModle> mRightanswer = new ArrayList<>();//正确答案模型的集合


    private String mediaUrl;

    private List<String> standardAnswerList = new ArrayList<>();//参考答案的集合
    private List<String> myAnswerList = new ArrayList<>();//我的答案的集合
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
       /*加载内容图片*/
        Glide.with(getActivity()).load(inbasebean.getQuestion_stem()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgconment);

//        String my_answer = inbasebean.getMy_answer();
//        if (!my_answer.equals("")) {
//                /*多个空*/
//            if (my_answer.contains("۞")) {
//                String[] strs = my_answer.split("۞");
//                      /*截取我的答案  添加到mRightanswer集合*/
//                for (int i = 0, len = strs.length; i < len; i++) {
//                    myAnswerList.add(strs[i].toString());
//                }
//            } else {
//                myAnswerList.add(my_answer);
//            }
//        }


        /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/

        String[] strs = standard_answer.split("۞");

       /*截取正确答案字符串  添加到standardAnswerList集合*/
        for (int i = 0, len = strs.length; i < len; i++) {
            String split = strs[i].toString();
            standardAnswerList.add(split);
        }

        Logger.d("aaaaaaaaaaaa-----standardAnswerList" + standardAnswerList);
        for (int a = 0; a < standardAnswerList.size(); a++) {
            /*填空题数据模型*/
            CompletionQuestionModle completionQuestionModle = new CompletionQuestionModle();
            List<List<CompletionQuestionadapterItemModle>> rightList = new ArrayList();

            for (int q = 0; q < standardAnswerList.get(a).length(); q++) {
                List<CompletionQuestionadapterItemModle> rightItemList = new ArrayList();
                rightList.add(rightItemList);
                completionQuestionModle.setShowRightList(rightList);
            }
            mRightanswer.add(completionQuestionModle);
        }
         /*添加题干*/
        addQuestionStem();

        /* size 填写有几道填空题 后台提供*/
        addHorizontalListView(standardAnswerList.size());

        /*初始化适配器的inputContainer存editext值的集合的*/
        for (int a = 0; a < standardAnswerList.size(); a++) {
            for (int q = 0; q < standardAnswerList.get(a).length(); q++) {
                mAllAdapterList.get(a).getInputContainer().put(q, "");
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
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params1.weight = 1;
            params1.topMargin = mToptv;

            TextView tv = new TextView(getActivity());
            tv.setTextSize(15);
            mToptv += 150;

            horlistviewroot.addView(tv);

            ExHorizontalListView horizontalListView = new ExHorizontalListView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
            params.weight = 1;
            params.topMargin = mTop;
            params.leftMargin = 30;
            mTop += 200;

            horizontalListView.setLayoutParams(params);

            Map<Integer, String> integerObjectMap = new HashMap();//每次都要new出来
            ExHorizontallListViewAdapter horizontallListViewAdapter = new ExHorizontallListViewAdapter(getActivity(), this, i, integerObjectMap, mRightanswer.get(i).getShowRightList(), inbasebean);
            horizontalListView.setAdapter(horizontallListViewAdapter);
            mAllAdapterList.add(horizontallListViewAdapter);
            mAllHorizontalListView.add(horizontalListView);

            horlistviewroot.addView(horizontalListView);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_play) {
            playMp3(mediaUrl);
        }
    }

    /*先走setArguments 在走onPageSelected中的函数 最后走 submitHomework*/
    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (CompletionQuestionModle) bundle.get("CompletionQuestionModle");
        inbasebean.setEachposition(bundle.getInt("position"));
        standard_answer = inbasebean.getStandard_answer();

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
            for (int i = 0; i < mAllAdapterList.size(); i++) {
                Map<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
                mAllMap.put(i, integerObjectMap);
            }
//            Logger.d("zzzzzzin:" + mAllAdapterList.get(selfposition).getInputContainer());
            mAllMap.put(selfposition, mAllAdapterList.get(selfposition).getInputContainer());// 获取每个适配的输入item的集合
            inbasebean.setmCompletionAllMap(mAllMap);

            submit.submitCompletionFragment(inbasebean);//通知activity这次的作答答案
        }
    }

    /*是activity翻页后通知自己*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (CompletionQuestionModle) questionModle;
            inbasebean.setIsFocusable("false");/*部顯示焦点*/
            inbasebean.setIsShowRightAnswer("yes");/*显示正确答案*/
            standardAnswerList.clear();
            myAnswerList.clear();
            mRightanswer.clear();

           /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/
            String[] strs = standard_answer.split("۞");
           /*截取正确答案字符串  添加到standardAnswerList集合*/
            for (int i = 0, len = strs.length; i < len; i++) {
                String split = strs[i].toString();
                standardAnswerList.add(split);
            }
            /*获取我的答案*/
            for (int i = 0; i < mAllAdapterList.size(); i++) {
                Map<Integer, String> integerStringMap = inbasebean.getmCompletionAllMap().get(i);

                Logger.d("integerStringMap:" + integerStringMap);
                StringBuffer buffer = new StringBuffer();
                for (int q = 0; q < integerStringMap.size(); q++) {
                    if (integerStringMap.get(q).equals("")) {
                        buffer.append(" ");
                    } else {
                        buffer.append(integerStringMap.get(q));
                    }
                }
                myAnswerList.add(buffer.toString());
            }
//            Logger.d("myAnswerList:" + myAnswerList);
////            Logger.d("myAnswerList:" + myAnswerList.toString());
////            Logger.d("standardAnswerList:" + standardAnswerList.toString());

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
//                    Logger.d("cqim:" + cqim.toString());
//                    Logger.d("rightItemList:" + rightItemList.toString());
                    completionQuestionModle.setShowRightList(rightList);

                }
                mRightanswer.add(completionQuestionModle);
            }

//                /*循环便利 所有适配器的集合 然后向适配器集合赋值 然后刷新adapter*/
            for (int i = 0; i < mAllAdapterList.size(); i++) {
                Map<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
                mAllAdapterList.get(i).setInputContainer(integerObjectMap, inbasebean, mRightanswer.get(i).getShowRightList());
            }


        }
    }


    /**/
    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

