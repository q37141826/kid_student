package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.BaseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionadapterItemModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
 * 做作业Activity
 * */
public class DoHomeworkActivity extends FxActivity
        implements JudgeFragment.SubmitJudgeFragment,
        ChoiceFragment.SubmitChoiseFragment,
        JudgeFragment.GetMediaPlayer, SortFragment.SubmitSortFragment, LineFragment.SubmitLineFragment,
        CompletionFragment.SubmitCompletionFragment {

    private cn.dajiahui.kid.ui.study.view.NoScrollViewPager mViewpager;
    private String subjectype = "";//当前题型
    private MediaPlayer mediaPlayer;
    private int currentposition = 0;//当前页面的索引
    private Map<Integer, BaseHomeworkFragment> frMap = new HashMap();//保存每个不同类型的Fragment

    private ArrayList<Object> mDatalist;//所有数据的模型集合
    private String homework_id;
    private String is_complete = "";
    private int mItemPosition;//查看作业页面的item的position
    public static int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        mViewpager.setNoScroll(false);//作业可以滑动
        mItemPosition = intent.getIntExtra("position", -1);
        homework_id = intent.getStringExtra("homework_id");
        is_complete = intent.getStringExtra("IS_COMPLETE");
        setfxTtitle(intent.getStringExtra("UNIT_NAME"));
        onRightBtn(R.string.AnswerCard);
        httpData();
        onBackTextShowProgress();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_do_homework);
        initialize();
        mDatalist = new ArrayList<>();
    }


    @Override
    public void httpData() {
        super.httpData();
        /*作业请求*/
        RequestUtill.getInstance().httpGetStudentHomeWorkhomeworkContinue(DoHomeworkActivity.this, callHomeWorkContinue, homework_id);
    }

    /**
     * 学生作业所有题callback函数
     */
    ResultCallback callHomeWorkContinue = new ResultCallback() {

        @Override
        public void onError(Request request, Exception e) {
            Logger.d("作业返回失败json：" + e.toString());
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
//            Logger.d("作业返回json：" + response);
            dismissfxDialog();
            HeadJson headJson = new HeadJson(response);
            if (headJson.getstatus() == 0) {

                try {
                    JSONArray jsonArray = headJson.getObject().getJSONObject("data").getJSONArray("question_list");
                    List<QuestionModle> mdata = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<QuestionModle>>() {
                    }.getType());

                    /*解析json数据*/
                    for (int i = 0; i < mdata.size(); i++) {

                        switch (mdata.get(i).getQuestion_cate_id()) {
                            case Constant.Judje:
                                Logger.d("判断：" + jsonArray.get(i).toString());
                                JudjeQuestionModle judjeQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), JudjeQuestionModle.class);
                                mDatalist.add(judjeQuestionModle);
                                /*未完成时初始化  分为 两种情况 -1 未开始  0进行中 */
                                if (!judjeQuestionModle.getIs_complete().equals("1")) {
                                    if (!judjeQuestionModle.getIs_answered().equals("1")) {//未回答时
                                        judjeQuestionModle.setMy_answer("");
                                    } else {//已经回答
                                        judjeQuestionModle.setMy_answer(judjeQuestionModle.getMy_answer());
                                    }
                                }
                                break;
                            case Constant.Choice:
                                Logger.d("选择：" + jsonArray.get(i).toString());
                                ChoiceQuestionModle choiceQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), ChoiceQuestionModle.class);
                                mDatalist.add(choiceQuestionModle);

                                /*未完成时初始化  分为 两种情况 -1 未开始  0进行中 */
                                if (!choiceQuestionModle.getIs_complete().equals("1")) {
                                    if (!choiceQuestionModle.getIs_answered().equals("1")) {//未回答时
                                        choiceQuestionModle.setMy_answer("");
                                    } else {//已经回答
                                        choiceQuestionModle.setMy_answer(choiceQuestionModle.getMy_answer());
                                    }
                                }

                                break;
                            case Constant.Sort:
                                Logger.d("排序：" + jsonArray.get(i).toString());
                                SortQuestionModle sortQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), SortQuestionModle.class);
                                mDatalist.add(sortQuestionModle);

                                /*未完成时初始化*/
                                if (!sortQuestionModle.getIs_complete().equals("1")) {
                                    if (!sortQuestionModle.getIs_answered().equals("1")) {
                                        /*初始化排序答案*/
                                        for (int s = 0; s < sortQuestionModle.getOptions().size(); s++) {
                                            if (sortQuestionModle.getInitSortMyanswerList().size() < sortQuestionModle.getOptions().size()) {
                                            sortQuestionModle.getInitSortMyanswerList().add("");//未回答
                                            }
                                        }
                                    } else {
                                        /*初始化排序答案*/
                                        String[] split = sortQuestionModle.getMy_answer().split(",");
                                        for (int s = 0; s < split.length; s++) {
                                            sortQuestionModle.getInitSortMyanswerList().add(split[s].toString());//已经回答
                                        }
                                    }
                                }
                                break;
                            case Constant.Line:
                                Logger.d("连线：" + jsonArray.get(i).toString());
                                LineQuestionModle lineQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), LineQuestionModle.class);
                                mDatalist.add(lineQuestionModle);
                                /*未完成时初始化*/
                                if (!lineQuestionModle.getIs_complete().equals("1")) {

                                    if (!lineQuestionModle.getIs_answered().equals("1")) {
                                        /*初始化连线答案*/
                                        for (int l = 0; l < lineQuestionModle.getOptions().getLeft().size(); l++) {
                                            lineQuestionModle.getInitLineMyanswerMap().put((l + 1) + "", "");//未回答
                                        }
                                    } else {
                                        Map map = jsonToObject(lineQuestionModle.getMy_answer());
                                        for (int l = 1; l <= map.size(); l++) {
                                            lineQuestionModle.getInitLineMyanswerMap().put((l) + "", map.get(l) + "");//已经回答
                                        }
                                    }
                                }
                                break;
                            case Constant.Completion:
                                Logger.d("填空：" + jsonArray.get(i).toString());
                                CompletionQuestionModle completionQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), CompletionQuestionModle.class);
                                /*初始化填空答案*/
                                mDatalist.add(completionQuestionModle);

                                /*未完成时初始化*/
                                if (!completionQuestionModle.getIs_complete().equals("1")) {

                                    if (!completionQuestionModle.getIs_answered().equals("1")) {//未完成
                                        /*根据正确答案初始化我的答案*/
                                        /*解析正确答案（后台获取的正确答案）۞    分隔单词  然后自己拆分一个单词几个字母*/
                                        String standard_answer = completionQuestionModle.getStandard_answer();
                                        /*多个题*/
                                        if (standard_answer.contains("۞")) {
                                            String[] strs = standard_answer.split("۞");
                                            for (int a = 0; a < strs.length; a++) {
                                                /*填空题数据模型*/
                                                LinkedHashMap<Integer, CompletionQuestionadapterItemModle> mItemMap = new LinkedHashMap<>();//每个横滑dadpter的数据
                                                for (int b = 0; b < strs[a].length(); b++) {
                                                    mItemMap.put(b, new CompletionQuestionadapterItemModle("㊒"));
                                                }
                                                completionQuestionModle.getmCompletionAllMap().put(a, mItemMap);
                                            }

                                        } else {
                                            /*没有۞   只有一个横滑的adapterview*/
                                            LinkedHashMap<Integer, CompletionQuestionadapterItemModle> mItemMap = new LinkedHashMap<>();//每个横滑dadpter的数据
                                            for (int a = 0; a < standard_answer.length(); a++) {
                                                mItemMap.put(a, new CompletionQuestionadapterItemModle("㊒"));
                                            }
                                            completionQuestionModle.getmCompletionAllMap().put(0, mItemMap);
                                        }
                                    } else {//已经完成

                                        String my_answer = completionQuestionModle.getMy_answer();
                                        /*多个题*/
                                        if (my_answer.contains("۞")) {
                                            String[] strs = my_answer.split("۞");
                                            for (int a = 0; a < strs.length; a++) {
                                                /*填空题数据模型*/
                                                LinkedHashMap<Integer, CompletionQuestionadapterItemModle> mItemMap = new LinkedHashMap<>();//每个横滑dadpter的数据
                                                for (int b = 0; b < strs[a].length(); b++) {
                                                    mItemMap.put(b, new CompletionQuestionadapterItemModle(strs[a]));
                                                }
                                                completionQuestionModle.getmCompletionAllMap().put(a, mItemMap);
                                            }

                                        } else {
                                            /*没有۞   只有一个横滑的adapterview*/
                                            LinkedHashMap<Integer, CompletionQuestionadapterItemModle> mItemMap = new LinkedHashMap<>();//每个横滑dadpter的数据
                                            for (int a = 0; a < my_answer.length(); a++) {
                                                mItemMap.put(a, new CompletionQuestionadapterItemModle(String.valueOf(my_answer.charAt(a))));
                                            }
                                            completionQuestionModle.getmCompletionAllMap().put(0, mItemMap);
                                        }
                                    }
                                }
                                break;
                            default:
                                break;

                        }

                    }
                    /*作业适配器*/
                    HomeWorkAdapter homeWorkAdapter = new HomeWorkAdapter(getSupportFragmentManager(), mdata);
                    mViewpager.setAdapter(homeWorkAdapter);
                    mViewpager.setOnPageChangeListener(onPageChangeListener);

                    /*跳转单个题型的题*/
                    if (mItemPosition != -1) {
                        mViewpager.setCurrentItem(mItemPosition);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showToast(DoHomeworkActivity.this, headJson.getMsg());
            }
        }
    };

    /*json转map*/
    public Map jsonToObject(String jsonStr) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<Integer, Integer> outMap = new HashMap<Integer, Integer>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            try {
                if (!jsonObj.getString(name).equals(""))
                    outMap.put(Integer.parseInt(name), Integer.parseInt(jsonObj.getString(name)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return outMap;
    }

    /*初始化*/
    private void initialize() {
        mViewpager = getView(R.id.viewpager);
        //获取屏幕宽度
        screenWidth = BaseUtil.getWidthPixels(this);
    }

    /*半路退出答题*/
    @Override
    public void onBackShowProgress(View view) {
        super.onRightBtnClick(view);
        finishActivity();
    }

    /*答题卡*/
    @Override
    public void onRightBtnClick(View view) {
        if (is_complete.equals("1")) { /*完成答题状态*/
//            Logger.d("is_complete:" + is_complete);

            /*答题状态*/
            Bundle bundle = new Bundle();
            bundle.putSerializable("ANSWER_CARD_COMPLETE", new BeSaveAnswerCard(mDatalist, homework_id, is_complete));
            /*跳转答题卡*/
            DjhJumpUtil.getInstance().startBaseActivityForResult(DoHomeworkActivity.this, AnswerCardCompleteActivity.class, bundle, DjhJumpUtil.getInstance().activity_answerCardComplete);

        } else {
            /*答题状态*/
            Bundle bundle = new Bundle();
            bundle.putSerializable("ANSWER_CARD", new BeSaveAnswerCard(mDatalist, homework_id, is_complete));
            /*跳转答题卡*/
            DjhJumpUtil.getInstance().startBaseActivityForResult(DoHomeworkActivity.this, AnswerCardActivity.class, bundle, DjhJumpUtil.getInstance().activity_answerCard);
        }

    }


    @Override
    public void getMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }


    /*viewpager滑动监听*/
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentposition = position;//当前题的页数


            /*滑动停止音频*/
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            QuestionModle questionModle = (QuestionModle) mDatalist.get(position);

            switch (questionModle.getQuestion_cate_id()) {
                case Constant.Judje:/*判断题*/

                    JudjeQuestionModle jude = (JudjeQuestionModle) mDatalist.get(position);
                    if ((frMap.get((position)) != null)) {
                        JudgeFragment judgeFragment = (JudgeFragment) frMap.get(position);
                        judgeFragment.submitHomework(jude);
                    }

                    break;
                case Constant.Choice:/*选择题*/

                    ChoiceQuestionModle choice = (ChoiceQuestionModle) mDatalist.get(position);
                    if ((frMap.get((position)) != null)) {
                        ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get(position);
                        choiceFragment.submitHomework(choice);
                    }

                    break;
                case Constant.Sort:/*排序题*/

                    SortQuestionModle sort = (SortQuestionModle) mDatalist.get(position);
                    if ((frMap.get((position)) != null)) {
                        SortFragment sortFragment = (SortFragment) frMap.get((position));
                        sortFragment.submitHomework(sort);
                    }
                    break;
                case Constant.Line:/*连线题*/

                    LineQuestionModle line = (LineQuestionModle) mDatalist.get(position);
                    if ((frMap.get((position)) != null)) {
                        LineFragment linFragment = (LineFragment) frMap.get((position));
                        linFragment.submitHomework(line);
                    }
                    break;
                case Constant.Completion:/*填空*/

                    CompletionQuestionModle complete = (CompletionQuestionModle) mDatalist.get(position);
                    if ((frMap.get((position)) != null)) {
                        CompletionFragment completionFragment = (CompletionFragment) frMap.get((position));
                        completionFragment.submitHomework(complete);
                    }
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /*适配器*/
    private class HomeWorkAdapter extends FragmentStatePagerAdapter {

        private List<QuestionModle> data;

        private HomeWorkAdapter(FragmentManager fragmentManager, List<QuestionModle> data) {
            super(fragmentManager);

            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            //获取题型
            subjectype = data.get(position).getQuestion_cate_id();
            if (subjectype.equals(Constant.Judje)) { /*判断*/

                JudgeFragment fr1 = new JudgeFragment();
                Bundle judgeBundle = new Bundle();
                judgeBundle.putSerializable("JudgeQuestionModle", (Serializable) mDatalist.get(position));
                judgeBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                fr1.setArguments(judgeBundle);
                frMap.put(position, fr1);

                return fr1;
            } else if (subjectype.equals(Constant.Choice)) { /*选择*/

                ChoiceFragment fr2 = new ChoiceFragment();
                Bundle choiceBundle = new Bundle();
                choiceBundle.putSerializable("ChoiceQuestionModle", (Serializable) mDatalist.get(position));
                choiceBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                fr2.setArguments(choiceBundle);
                frMap.put(position, fr2);
                return fr2;

            } else if (subjectype.equals(Constant.Sort)) {/*排序*/

                SortFragment fr3 = new SortFragment();

                Bundle sortBundle = new Bundle();
                sortBundle.putSerializable("SortQuestionModle", (Serializable) mDatalist.get(position));
                sortBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                frMap.put(position, fr3);
                fr3.setArguments(sortBundle);
                return fr3;

            } else if (subjectype.equals(Constant.Line)) {   /*连线*/

                LineFragment fr4 = new LineFragment();

                Bundle linebBundle = new Bundle();
                linebBundle.putSerializable("LineQuestionModle", (Serializable) mDatalist.get(position));
                linebBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                frMap.put(position, fr4);
                fr4.setArguments(linebBundle);
                return fr4;

            } else if (subjectype.equals(Constant.Completion)) {  /*填空*/

                CompletionFragment fr5 = new CompletionFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("CompletionQuestionModle", (Serializable) mDatalist.get(position));
                bundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                frMap.put(position, fr5);

                fr5.setArguments(bundle);
                return fr5;


            }
            return null;
        }


        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
            frMap.remove(position);

            //希望做一次垃圾回收
            System.gc();
        }


    }

    /*判断题回调接口*/
    @Override
    public void submitJudgeFragment(JudjeQuestionModle questionModle) {
        mDatalist.set(currentposition, questionModle);

    }

    /*选择题回调接口*/
    @Override
    public void submitChoiceFragment(ChoiceQuestionModle questionModle) {

        mDatalist.set(currentposition, questionModle);
    }


    /*排序题回调接口*/
    @Override
    public void submitSoreFragment(SortQuestionModle questionModle) {

        mDatalist.set(currentposition, questionModle);
    }

    /*连线题回调接口*/
    @Override
    public void submitLineFragment(LineQuestionModle questionModle) {

        mDatalist.set(currentposition, questionModle);
    }

    /*填空题回掉接口*/
    @Override
    public void submitCompletionFragment(CompletionQuestionModle questionModle) {

        mDatalist.set(currentposition, questionModle);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*点击答题卡回传*/
        if (requestCode == DjhJumpUtil.getInstance().activity_answerCard && resultCode == DjhJumpUtil.getInstance().activity_todohomework) {
            int current_num = data.getIntExtra("current_num", -1);
            mViewpager.setCurrentItem(current_num);
        }
        /*点击submit回传*/
        else if (requestCode == DjhJumpUtil.getInstance().activity_answerCard && resultCode == DjhJumpUtil.getInstance().activity_answerCardSubmit) {

            setResult(DjhJumpUtil.getInstance().activity_answerCardSubmit_dohomework);
            finishActivity();

        } else if (requestCode == DjhJumpUtil.getInstance().activity_answerCardComplete && resultCode == DjhJumpUtil.getInstance().activity_todohomework) {
            int current_num = data.getIntExtra("current_num", -1);
            mViewpager.setCurrentItem(current_num);
        }
    }

    /*监听虚拟按键*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //监控/拦截/屏蔽返回键

            finishActivity();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
