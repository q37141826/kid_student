package cn.dajiahui.kid.ui.study.kinds.practice;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
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
import cn.dajiahui.kid.ui.homework.bean.BeAnswerCArd;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.util.Logger;

/*
* 做练习Activity
* */
public class DoPraticeActivity extends FxActivity
        implements ExJudgeFragment.SubmitJudgeFragment,
        ExChoiceFragment.SubmitChoiseFragment,
        ExJudgeFragment.GetMediaPlayer, ExSortFragment.SubmitSortFragment, ExLineFragment.SubmitLineFragment,
        ExCompletionFragment.SubmitCompletionFragment {

    private SeekBar seek;
    private TextView mSchedule;
    private cn.dajiahui.kid.ui.study.view.NoScrollViewPager mViewpager;
    private String subjectype = "";//当前题型
    private MediaPlayer mediaPlayer;

    private int praticeCurrentPosition = 0;//当前页面的索引
    private Map<Integer, ExBaseHomeworkFragment> frMap = new HashMap();//保存每个不同类型的Fragment
    private HashMap<Integer, Object> PageMap = new HashMap();//保存每一页的页数和数据

    private List<QuestionModle> mdata;/*数据源*/
    private List<Object> mDatalist;
    private Button btncheck;
    private String book_id;
    private String unit_id;
    private List<BeAnswerCArd> mAnswerCardList = new ArrayList();

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
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), judjeQuestionModle.getId(), i));
                                break;
                            case Constant.Choice:
                                ChoiceQuestionModle choiceQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), ChoiceQuestionModle.class);
                                mDatalist.add(choiceQuestionModle);
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), choiceQuestionModle.getId(), i));
//                                Logger.d("选择：" + jsonArray.get(i).toString());
                                break;
                            case Constant.Sort:
//                                Logger.d("排序：" + jsonArray.get(i).toString());
                                SortQuestionModle sortQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), SortQuestionModle.class);
                                mDatalist.add(sortQuestionModle);
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), sortQuestionModle.getId(), i));
                                break;
                            case Constant.Line:
//                            Logger.d("连线：" + jsonArray.get(i).toString());
                                LineQuestionModle lineQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), LineQuestionModle.class);
                                mDatalist.add(lineQuestionModle);
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), lineQuestionModle.getId(), i));
                                break;
                            case Constant.Completion:
//                            Logger.d("填空：" + jsonArray.get(i).toString());
                                CompletionQuestionModle completionQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), CompletionQuestionModle.class);
                                mDatalist.add(completionQuestionModle);
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), completionQuestionModle.getId(), i));
                                break;
                            default:
                                break;

                        }
                    }

//                    seek.setMax(mdata.size() - 1);
//                    seek.setProgress((praticeCurrentPosition));
//                    mSchedule.setText((praticeCurrentPosition + 1) + "/" + mdata.size());
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
//        seek = getView(R.id.seek);
//        mSchedule = getView(R.id.tv_schedule);
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
                        QuestionModle questionModle = (QuestionModle) PageMap.get(praticeCurrentPosition);

                        PageMap.put(praticeCurrentPosition, questionModle);
                        if (questionModle != null && questionModle.isAnswer() == true && questionModle.getAnswerflag().equals("true")) {
                            praticeCurrentPosition++;
                           /*跳转下一题*/
                            mViewpager.setCurrentItem(praticeCurrentPosition);
//                            seek.setProgress((praticeCurrentPosition));
//                            if (praticeCurrentPosition + 1 == mdata.size()) {
//                                mSchedule.setText(mdata.size() + "/" + mdata.size());
//                                return;
//                            } else {
//                                mSchedule.setText((praticeCurrentPosition + 1) + "/" + mdata.size());
//                            }
                            changeBtnY();

                            return;
                        }

                        if (questionModle != null && questionModle.isAnswer() == false && questionModle.getAnswerflag().equals("true")) {
//                            Toast.makeText(context, "保存第" + (praticeCurrentPosition + 1) + "题数据", Toast.LENGTH_SHORT).show();
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

//                                SortQuestionModle sortQuestionModle = (SortQuestionModle) mDatalist.get(praticeCurrentPosition);
                                        /*判断是否做完题 */
                                for (int i = 0; i < questionModle.getInitSortMyanswerList().size(); i++) {
                                    if (questionModle.getInitSortMyanswerList().get(i).equals("")) {
                                        questionModle.setAnswer(false);
                                        Toast.makeText(context, "答题未完成，请继续...", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(context, "答题未完成，请继续...", Toast.LENGTH_SHORT).show();
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
        FragmentManager fragmentManager;

        private ExetriceAdapter(FragmentManager fragmentManager, List<QuestionModle> data) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
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
                JudjeQuestionModle judgeModle = new JudjeQuestionModle();
                judgeModle.setQuestion_cate_id(subjectype);//保存当前的题型
                judgeModle.setStandard_answer(((JudjeQuestionModle) mDatalist.get(position)).getStandard_answer());//保存判断题的正确答案

                Bundle judgeBundle = new Bundle();
                judgeBundle.putSerializable("JudgeQuestionModle", (Serializable) mDatalist.get(position));
                judgeBundle.putInt("position", position);
                fr1.setArguments(judgeBundle);
                frMap.put(position, fr1);
                PageMap.put(position, judgeModle);
                return fr1;

            } else if (subjectype.equals(Constant.Choice)) { /*选择*/

                ExChoiceFragment fr2 = new ExChoiceFragment();
                ChoiceQuestionModle choiceModle = new ChoiceQuestionModle();
                choiceModle.setQuestion_cate_id(subjectype);//保存当前的题型
                choiceModle.setStandard_answer(((ChoiceQuestionModle) mDatalist.get(position)).getStandard_answer());//保存选择题的正确答案

                Bundle choiceBundle = new Bundle();
                choiceBundle.putSerializable("ChoiceQuestionModle", (Serializable) mDatalist.get(position));
                choiceBundle.putInt("position", position);
                fr2.setArguments(choiceBundle);
                frMap.put(position, fr2);
                PageMap.put(position, choiceModle);
                return fr2;

            } else if (subjectype.equals(Constant.Sort)) {/*排序*/
                ExSortFragment fr3 = new ExSortFragment();
                SortQuestionModle sortModle = new SortQuestionModle();
                sortModle.setQuestion_cate_id(subjectype);
                sortModle.setStandard_answer(((SortQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案

                Bundle sortBundle = new Bundle();
                sortBundle.putInt("position", position);
                sortBundle.putSerializable("SortQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr3);
                PageMap.put(position, sortModle);
                fr3.setArguments(sortBundle);
                return fr3;

            } else if (subjectype.equals(Constant.Line)) {/*连线*/
                ExLineFragment fr4 = new ExLineFragment();
                LineQuestionModle lineModle = new LineQuestionModle();

                lineModle.setQuestion_cate_id(subjectype);//保存当前题型
                lineModle.setStandard_answer(((LineQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案

                Bundle linebBundle = new Bundle();
                linebBundle.putSerializable("LineQuestionModle", (Serializable) mDatalist.get(position));
                linebBundle.putInt("position", position);
                frMap.put(position, fr4);
                PageMap.put(position, lineModle);
                fr4.setArguments(linebBundle);
                return fr4;

            } else if (subjectype.equals(Constant.Completion)) {/*填空*/
                ExCompletionFragment fr5 = new ExCompletionFragment();
                CompletionQuestionModle compleModle = new CompletionQuestionModle();

                compleModle.setQuestion_cate_id(subjectype);
                compleModle.setStandard_answer(((CompletionQuestionModle) mDatalist.get(position)).getStandard_answer());//填空题要传入正确答案

                Bundle compleBundle = new Bundle();
                compleBundle.putSerializable("CompletionQuestionModle", (Serializable) mDatalist.get(position));
                compleBundle.putInt("position", position);
                frMap.put(position, fr5);
                PageMap.put(position, compleModle);
                fr5.setArguments(compleBundle);
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
        JudjeQuestionModle qm = (JudjeQuestionModle) PageMap.get(questionModle.getEachposition());
        praticeCurrentPosition = questionModle.getEachposition();
        qm.setStandard_answer(questionModle.getStandard_answer());//保存参考答案
        qm.setMy_answer(questionModle.getMy_answer());//保存学作答答案
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setIs_right(questionModle.getIs_right());//回答正误


    }

    /*选择题回调接口*/
    @Override
    public void submitChoiceFragment(ChoiceQuestionModle questionModle) {

        ChoiceQuestionModle qm = (ChoiceQuestionModle) PageMap.get(questionModle.getEachposition());
        praticeCurrentPosition = questionModle.getEachposition();
        qm.setStandard_answer(questionModle.getStandard_answer());//学生参考答案
        qm.setMy_answer(questionModle.getMy_answer());//保存学学生作答答案
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记

    }


    /*排序题回调接口*/
    @Override
    public void submitSoreFragment(SortQuestionModle questionModle) {

        praticeCurrentPosition = questionModle.getEachposition();
        SortQuestionModle qm = (SortQuestionModle) PageMap.get(praticeCurrentPosition);
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setOptions(questionModle.getOptions());
        qm.setInitSortMyanswerList(questionModle.getInitSortMyanswerList());//我的答案的集合（Val值 用于上传服务器）

    }

    /*连线题回调接口*/
    @Override
    public void submitLineFragment(LineQuestionModle questionModle) {

        praticeCurrentPosition = questionModle.getEachposition();
        LineQuestionModle qm = (LineQuestionModle) PageMap.get(praticeCurrentPosition);
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setInitLineMyanswerMap(questionModle.getInitLineMyanswerMap());

    }

    /*填空题回掉接口*/
    @Override
    public void submitCompletionFragment(CompletionQuestionModle questionModle) {
        praticeCurrentPosition = questionModle.getEachposition();
        CompletionQuestionModle qm = (CompletionQuestionModle) PageMap.get(praticeCurrentPosition);
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记

        //保存当前页面填空题的答案（用于翻页回来后 查找当前页的数据 i是从0开始）
        for (int i = 0; i < questionModle.getmCompletionAllMap().size(); i++) {
            qm.getmCompletionAllMap().put(i, questionModle.getmCompletionAllMap().get(i));
        }
    }


//    boolean isClickable = true;//true
//
//    /* 改变按钮状态*/
//    private void changeBtn() {
//        if (isClickable) {
//            changeBtnN();
//        } else {
//            changeBtnY();
//        }
//
//    }


    /*练习 改变按钮的颜色*/
    public void changeBtnN() {
//        isClickable = false;
        btncheck.setText("NEXT");
        btncheck.setBackgroundResource(R.color.gray);
    }

    /*练习 改变按钮的颜色*/
    public void changeBtnY() {
//        isClickable = true;
        btncheck.setText("Check");
        btncheck.setBackgroundResource(R.color.yellow_FEBF12);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
