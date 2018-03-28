package cn.dajiahui.kid.ui.study.kinds.practice;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;

/*
* 做练习Activity
* */
public class DoPraticeActivity extends FxActivity
        implements ExJudgeFragment.SubmitJudgeFragment,
        ExChoiceFragment.SubmitChoiseFragment,
        ExJudgeFragment.GetMediaPlayer, ExSortFragment.SubmitSortFragment, ExLineFragment.SubmitLineFragment,
        ExCompletionFragment.SubmitCompletionFragment {

    private cn.dajiahui.kid.ui.study.view.NoScrollViewPager mViewpager;
    private String subjectype = "";//当前题型
    private MediaPlayer mediaPlayer;

    private int praticeCurrentPosition = 0;//当前页面的索引
    private Map<Integer, ExBaseHomeworkFragment> frMap = new HashMap();//保存每个不同类型的Fragment

    private List<QuestionModle> mdata;/*数据源*/
    private List<Object> mDatalist;
    private Button btncheck;
    private String book_id;
    private String unit_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle("练习");
        onBackText();
        Bundle mDoHomeworkbundle = getIntent().getExtras();
        book_id = mDoHomeworkbundle.getString("BOOK_ID");
        unit_id = mDoHomeworkbundle.getString("UNIT_ID");
        btncheck.setVisibility(View.VISIBLE);
        mViewpager.setNoScroll(true);//练习禁止滑动
        httpData();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_do_pratices);
        initialize();
        mDatalist = new ArrayList<>();
    }


    @Override
    public void httpData() {
        super.httpData();
            /*练习请求*/
        RequestUtill.getInstance().httpExercise(DoPraticeActivity.this, callExercise, book_id, unit_id);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 练习callback函数
     */
    ResultCallback callExercise = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            Logger.d("练习失败json：" + e.toString());
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("练习返回：" + response);
            dismissfxDialog();

            HeadJson headJson = new HeadJson(response);
            if (headJson.getstatus() == 0) {
                try {
                    JSONArray jsonArray = headJson.getObject().getJSONObject("data").getJSONArray("question_list");
                    mdata = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<QuestionModle>>() {
                    }.getType());

                      /*解析json数据*/
                    for (int i = 0; i < mdata.size(); i++) {
                        switch (mdata.get(i).getQuestion_cate_id()) {
                            case Constant.Judje:
//                                Logger.d("判断：" + jsonArray.get(i).toString());
                                JudjeQuestionModle judjeQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), JudjeQuestionModle.class);
                                mDatalist.add(judjeQuestionModle);
                                break;
                            case Constant.Choice:
                                ChoiceQuestionModle choiceQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), ChoiceQuestionModle.class);
                                mDatalist.add(choiceQuestionModle);
//                                Logger.d("选择：" + jsonArray.get(i).toString());
                                break;
                            case Constant.Sort:
//                                Logger.d("排序：" + jsonArray.get(i).toString());
                                SortQuestionModle sortQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), SortQuestionModle.class);
                                mDatalist.add(sortQuestionModle);
                                break;
                            case Constant.Line:
//                            Logger.d("连线：" + jsonArray.get(i).toString());
                                LineQuestionModle lineQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), LineQuestionModle.class);
                                mDatalist.add(lineQuestionModle);
                                break;
                            case Constant.Completion:
//                            Logger.d("填空：" + jsonArray.get(i).toString());
                                CompletionQuestionModle completionQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), CompletionQuestionModle.class);
                                mDatalist.add(completionQuestionModle);
                                break;
                            default:
                                break;

                        }
                    }

                    ExetriceAdapter adapter = new ExetriceAdapter(getSupportFragmentManager(), mdata);
                    mViewpager.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showToast(DoPraticeActivity.this, headJson.getMsg());
            }
        }
    };

    /*初始化*/
    private void initialize() {
        mViewpager = getView(R.id.viewpager);
        btncheck = (Button) findViewById(R.id.btn_check);
        btncheck.setOnClickListener(onclick);
    }


    @Override
    public void getMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /*点击事件*/
    private View.OnClickListener onclick;

    {
        onclick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_check:
                        if (praticeCurrentPosition < mDatalist.size()) {
                            QuestionModle questionModle = (QuestionModle) mDatalist.get(praticeCurrentPosition);

                            if (questionModle != null && questionModle.isAnswer() == true && questionModle.getAnswerflag().equals("true")) {

                                praticeCurrentPosition++;

                                 /*跳转下一题*/
                                mViewpager.setCurrentItem(praticeCurrentPosition);

                                changeBtnY();
                                return;
                            }

                            if (questionModle != null && questionModle.isAnswer() == false && questionModle.getAnswerflag().equals("true")) {
                                questionModle.setAnswer(true);//设置提交答案  true 答过 false 未作答
                            /*判断*/
                                if (questionModle.getQuestion_cate_id().equals(Constant.Judje)) {
                                    ExJudgeFragment exJudgeFragment = (ExJudgeFragment) frMap.get((praticeCurrentPosition));
                                    //通知判断题碎片
                                    exJudgeFragment.submitHomework(questionModle);

                                }
                            /*选择*/
                                else if (questionModle.getQuestion_cate_id().equals(Constant.Choice)) {
                                    ExChoiceFragment exChoiceFragment = (ExChoiceFragment) frMap.get((praticeCurrentPosition));
                                    //通知选择题碎片
                                    exChoiceFragment.submitHomework(questionModle);

                                }
                            /*排序*/
                                else if (questionModle.getQuestion_cate_id().equals(Constant.Sort)) {

                                    ExSortFragment exSortFragment = (ExSortFragment) frMap.get((praticeCurrentPosition));

                                /*判断是否做完题 */
                                    for (int i = 0; i < questionModle.getInitSortMyanswerList().size(); i++) {
                                        if (questionModle.getInitSortMyanswerList().contains("㊒")) {
                                            questionModle.setAnswer(false);
                                            return;
                                        }
                                    }

                                    exSortFragment.submitHomework(questionModle);

                                }
                            /*连线题*/
                                else if (questionModle.getQuestion_cate_id().equals(Constant.Line)) {
                                    ExLineFragment linFragment = (ExLineFragment) frMap.get(praticeCurrentPosition);
                                    Map<String, String> myanswerMap = questionModle.getInitLineMyanswerMap();
                                    LineQuestionModle lineQuestionModle = (LineQuestionModle) mDatalist.get(praticeCurrentPosition);

                                    if (myanswerMap.size() == lineQuestionModle.getOptions().getRight().size()) {
                                        linFragment.submitHomework(questionModle);
                                    } else {
                                        questionModle.setAnswer(false);
//                                    Toast.makeText(context, "答题未完成，请继续...", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }
                            /*填空题*/
                                else if (questionModle.getQuestion_cate_id().equals(Constant.Completion)) {

                                    ExCompletionFragment exCompletionFragment = (ExCompletionFragment) frMap.get(praticeCurrentPosition);
                                    exCompletionFragment.submitHomework(questionModle);
                                }

                              /*改变按钮的颜色 变成灰色 再点击就是下一个题*/
                                changeBtnN();
                            } else {
                                return;
//                            Toast.makeText(context, "请作答第" + (praticeCurrentPosition + 1) + "题", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    default:
                        break;

                }


            }


        };
    }


    /*适配器*/
    private class ExetriceAdapter extends FragmentStatePagerAdapter {

        private List<QuestionModle> data;

        private ExetriceAdapter(FragmentManager fragmentManager, List<QuestionModle> data) {
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
                ExJudgeFragment fr1 = new ExJudgeFragment();

                Bundle judgeBundle = new Bundle();
                judgeBundle.putSerializable("JudgeQuestionModle", (Serializable) mDatalist.get(position));
                judgeBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                judgeBundle.putInt("position", position);
                fr1.setArguments(judgeBundle);
                frMap.put(position, fr1);

                return fr1;

            } else if (subjectype.equals(Constant.Choice)) { /*选择*/
                ExChoiceFragment fr2 = new ExChoiceFragment();

                Bundle choiceBundle = new Bundle();
                choiceBundle.putSerializable("ChoiceQuestionModle", (Serializable) mDatalist.get(position));
                choiceBundle.putInt("position", position);
                choiceBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                fr2.setArguments(choiceBundle);
                frMap.put(position, fr2);
                return fr2;

            } else if (subjectype.equals(Constant.Sort)) {/*排序*/
                ExSortFragment fr3 = new ExSortFragment();

                Bundle sortBundle = new Bundle();
                sortBundle.putSerializable("SortQuestionModle", (Serializable) mDatalist.get(position));
                sortBundle.putInt("position", position);
                sortBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                frMap.put(position, fr3);
                fr3.setArguments(sortBundle);
                return fr3;

            } else if (subjectype.equals(Constant.Line)) {/*连线*/
                ExLineFragment fr4 = new ExLineFragment();

                Bundle linebBundle = new Bundle();
                linebBundle.putSerializable("LineQuestionModle", (Serializable) mDatalist.get(position));
                linebBundle.putInt("position", position);
                linebBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                frMap.put(position, fr4);
                fr4.setArguments(linebBundle);
                return fr4;

            } else if (subjectype.equals(Constant.Completion)) {/*填空*/
                ExCompletionFragment fr5 = new ExCompletionFragment();

                Bundle comBundle = new Bundle();
                comBundle.putSerializable("CompletionQuestionModle", (Serializable) mDatalist.get(position));
                comBundle.putInt("position", position);
                comBundle.putString("currntQuestion", (position + 1) + "/" + data.size());
                frMap.put(position, fr5);

                fr5.setArguments(comBundle);
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
    public void submitJudgeFragment(JudjeQuestionModle Jqm) {
        praticeCurrentPosition = Jqm.getEachposition();
        mDatalist.set(Jqm.getEachposition(), Jqm);
        /*判断题 高亮显示check按钮 改变按钮颜色*/
        if (!Jqm.getMy_answer().equals("")) {
            changeBtnBgYellow();
        }
    }

    /*选择题回调接口*/
    @Override
    public void submitChoiceFragment(ChoiceQuestionModle Cqm) {
        praticeCurrentPosition = Cqm.getEachposition();
        mDatalist.set(Cqm.getEachposition(), Cqm);
        /*选择 高亮显示check按钮 改变按钮颜色*/
        if (!Cqm.getMy_answer().equals("")) {
            changeBtnBgYellow();
        }
    }


    /*排序题回调接口*/
    @Override
    public void submitSoreFragment(SortQuestionModle Sqm) {
        praticeCurrentPosition = Sqm.getEachposition();
        mDatalist.set(Sqm.getEachposition(), Sqm);
         /*排序 高亮显示check按钮 改变按钮颜色*/
        for (int i = 0; i < Sqm.getInitSortMyanswerList().size(); i++) {
            if (!Sqm.getInitSortMyanswerList().contains("㊒")) {
                changeBtnBgYellow();
            }
        }
    }

    /*连线题回调接口*/
    @Override
    public void submitLineFragment(LineQuestionModle Lqm) {
        praticeCurrentPosition = Lqm.getEachposition();
        mDatalist.set(Lqm.getEachposition(), Lqm);
        /*连线题 高亮显示check按钮 改变按钮颜色*/
        if (Lqm.getInitLineMyanswerMap().size() == ((LineQuestionModle) mDatalist.get(praticeCurrentPosition)).getOptions().getRight().size()) {
            changeBtnBgYellow();
        }
    }

    /*填空题回掉接口*/
    @Override
    public void submitCompletionFragment(CompletionQuestionModle Comq) {
        praticeCurrentPosition = Comq.getEachposition();
        mDatalist.set(Comq.getEachposition(), Comq);

        int mHNum = 0;//㊒字计数
        for (int i = 0; i < Comq.getmCompletionAllMap().size(); i++) {
            if (Comq.getmCompletionAllMap().get(i) != null) {
                Map<Integer, String> integerStringMap = Comq.getmCompletionAllMap().get(i);
                for (int m = 0; m < integerStringMap.size(); m++) {
                    String s = integerStringMap.get(m);
                    if (s.equals("㊒")) {
                        mHNum++;
                    }
                }
            } else {
                mHNum++;
            }
//            Map<Integer, String> integerObjectMap = inbasebean.getmCompletionAllMap().get(i);
        }

        if (mHNum == 0) {
            changeBtnBgYellow();
        }

    }


    /*练习 改变按钮的颜色*/
    public void changeBtnN() {
        /*最后一题显示END*/
        if (praticeCurrentPosition + 1 == mdata.size()) {
            btncheck.setText("END");
            changeBtnBgGray();
            btncheck.setClickable(false);
        } else {
            btncheck.setText("NEXT");
            changeBtnBgYellow();

        }
    }

    /*练习 改变按钮的颜色*/
    public void changeBtnY() {
        btncheck.setText("Check");
        changeBtnBgGray();

    }

    /*练习设置check按钮为黄色*/
    public void changeBtnBgYellow() {
        btncheck.setBackgroundResource(R.color.yellow_FEBF12);
    }

    /*练习设置check按钮为灰*/
    public void changeBtnBgGray() {
        btncheck.setBackgroundResource(R.color.gray_DBDBDB);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

}
