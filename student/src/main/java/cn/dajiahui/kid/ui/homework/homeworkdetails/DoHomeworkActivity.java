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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
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
import cn.dajiahui.kid.ui.homework.bean.BeDohomeWork;
import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.util.Logger;

/*
* 做作业Activity
* */
public class DoHomeworkActivity extends FxActivity
        implements JudgeFragment.SubmitJudgeFragment,
        ChoiceFragment.SubmitChoiseFragment,
        JudgeFragment.GetMediaPlayer, SortFragment.SubmitSortFragment, LineFragment.SubmitLineFragment,
        CompletionFragment.SubmitCompletionFragment {
    public static String sourceFlag;//区别是练习还是作业

    private SeekBar seek;
    private TextView mSchedule;
    private cn.dajiahui.kid.ui.study.view.NoScrollViewPager mViewpager;
    private String subjectype = "";//当前题型
    private MediaPlayer mediaPlayer;
    private int currentposition = 0;//当前页面的索引
    private int praticeCurrentPosition = 0;//当前页面的索引
    private List<Integer> pagelist = new ArrayList<>();//保存页数的集合（check过的页）
    private Map<Integer, BaseHomeworkFragment> frMap = new HashMap();//保存每个不同类型的Fragment
    private HashMap<Integer, Object> PageMap = new HashMap();//保存每一页的页数和数据

    private List<QuestionModle> mdata;//模拟数据元
    private List<Object> mDatalist;

    private Button btncheck;
    private String homework_id;
    private BeDohomeWork beDohomeWork;
    private List<BeAnswerCArd> mAnswerCardList = new ArrayList();
    private String is_complete = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
          /* sourceFlag=Practice 练习  sourceFlag=HomeWork作业*/
        sourceFlag = intent.getStringExtra("SourceFlag");
        mViewpager.setNoScroll(false);//作业可以滑动
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

    @Override
    protected void onRestart() {
        super.onRestart();
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
            Logger.d("作业返回json：" + response);
            dismissfxDialog();
            HeadJson headJson = new HeadJson(response);
            if (headJson.getstatus() == 0) {
                beDohomeWork = headJson.parsingObject(BeDohomeWork.class);
                try {
                    JSONArray jsonArray = headJson.getObject().getJSONObject("data").getJSONArray("question_list");
                    mdata = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<QuestionModle>>() {
                    }.getType());

//                    Logger.d("mdata:" + mdata.toString());
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
//                                Logger.d("连线：" + jsonArray.get(i).toString());
                                LineQuestionModle lineQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), LineQuestionModle.class);
                                mDatalist.add(lineQuestionModle);
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), lineQuestionModle.getId(), i));
                                break;
                            case Constant.Completion:
//                                Logger.d("填空：" + jsonArray.get(i).toString());
                                CompletionQuestionModle completionQuestionModle = new Gson().fromJson(jsonArray.get(i).toString(), CompletionQuestionModle.class);
                                mDatalist.add(completionQuestionModle);
                                mAnswerCardList.add(new BeAnswerCArd("1", "", "", mdata.get(i).getQuestion_cate_id(), completionQuestionModle.getId(), i));
                                break;
                            default:
                                break;

                        }
                    }

                    seek.setMax(mdata.size() - 1);
                    seek.setProgress((currentposition));
                    mSchedule.setText((currentposition + 1) + "/" + mdata.size());
                    Adapter adapter = new Adapter(getSupportFragmentManager(), mdata);
                    mViewpager.setAdapter(adapter);
                    mViewpager.setOnPageChangeListener(onPageChangeListener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showToast(DoHomeworkActivity.this, headJson.getMsg());
            }
        }
    };

    /*初始化*/
    private void initialize() {

        mViewpager = getView(R.id.viewpager);
        seek = getView(R.id.seek);
        mSchedule = getView(R.id.tv_schedule);
        btncheck = (Button) findViewById(R.id.btn_check);
        btncheck.setOnClickListener(onclick);


    }

    private FxDialog submitDialog;

    /*半路退出答题*/
    @Override
    public void onBackShowProgress(View view) {
        super.onRightBtnClick(view);
//        if (beDohomeWork.getIs_complete().equals("1")) {
//            finishActivity();
//
//        } else {
//            show();
//        }
        finishActivity();
    }

    /*答题卡*/
    @Override
    public void onRightBtnClick(View view) {
        Intent intent = new Intent(DoHomeworkActivity.this, AnswerCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("answerCard", new BeSaveAnswerCard(PageMap, homework_id, mAnswerCardList, is_complete));
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
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
                        QuestionModle questionModle = (QuestionModle) PageMap.get(currentposition);

                        PageMap.put(currentposition, questionModle);
                        if (questionModle != null && questionModle.isAnswer() == true && questionModle.getAnswerflag().equals("true")) {
                           /*跳转下一题*/
                            mViewpager.setCurrentItem(praticeCurrentPosition);
//                            if (praticeCurrentPosition == mDatalist.size()) {
                            changeBtnY();
//                            }
                            return;
                        }

                        if (questionModle != null && questionModle.isAnswer() == false && questionModle.getAnswerflag().equals("true")) {
                            Toast.makeText(context, "保存第" + (currentposition + 1) + "题数据", Toast.LENGTH_SHORT).show();
                            pagelist.add(currentposition);
                            questionModle.setAnswer(true);//设置提交答案  true 答过 false 未作答
                            /*判断*/
                            if (questionModle.getQuestion_cate_id().equals(Constant.Judje)) {
                                JudgeFragment judgeFragment = (JudgeFragment) frMap.get((currentposition));
                                //通知判断题碎片
                                judgeFragment.submitHomework(questionModle);

                            }
                            /*选择*/
                            else if (questionModle.getQuestion_cate_id().equals(Constant.Choice)) {
                                ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get((currentposition));
                                //通知选择题碎片
                                choiceFragment.submitHomework(questionModle);

                            }
                            /*排序*/
                            else if (questionModle.getQuestion_cate_id().equals(Constant.Sort)) {

                                SortFragment sortFragment = (SortFragment) frMap.get((currentposition));
                                sortFragment.submitHomework(questionModle);

                            }
                            /*连线题*/
                            else if (questionModle.getQuestion_cate_id().equals(Constant.Line)) {
                                LineFragment linFragment = (LineFragment) frMap.get(currentposition);
                                linFragment.submitHomework(questionModle);


                            }
                          /*填空题*/
                            else if (questionModle.getQuestion_cate_id().equals(Constant.Completion)) {

                                CompletionFragment completionFragment = (CompletionFragment) frMap.get(currentposition);
                                completionFragment.submitHomework(questionModle);
                            }

                            /*改变按钮的颜色 变成灰色 再点击就是下一个题*/
                            changeBtnN();
                        } else {
                            Toast.makeText(context, "请作答第" + (currentposition + 1) + "题", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;

                }


            }


        };
    }

    /*viewpager滑动监听*/
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentposition = position;//当前题的页数
            sourceFlag = "HomeWork";
            seek.setProgress(currentposition);
            mSchedule.setText((currentposition + 1) + "/" + mdata.size());
            /*滑动停止音频*/
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            if (PageMap.get(position) != null) {

                    /*翻页获取每页的数据模型*/
                QuestionModle questionModle = (QuestionModle) PageMap.get(position);

                switch (questionModle.getQuestion_cate_id()) {
                    case Constant.Judje:/*判断题*/

                        JudjeQuestionModle jude = (JudjeQuestionModle) PageMap.get(position);
                        JudgeFragment judgeFragment = (JudgeFragment) frMap.get(position);
                        judgeFragment.submitHomework(jude);

                        break;
                    case Constant.Choice:/*选择题*/

                        ChoiceQuestionModle choice = (ChoiceQuestionModle) PageMap.get(position);
                        ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get(position);
                        choiceFragment.submitHomework(choice);

                        break;
                    case Constant.Sort:/*排序题*/
                        SortQuestionModle sort = (SortQuestionModle) PageMap.get(position);
                        SortFragment sortFragment = (SortFragment) frMap.get((currentposition));
                        sortFragment.submitHomework(sort);

                        break;
                    case Constant.Line:/*连线题*/

                        LineQuestionModle line = (LineQuestionModle) PageMap.get(position);
                        LineFragment linFragment = (LineFragment) frMap.get((currentposition));
                        linFragment.submitHomework(line);

                        break;
                    case Constant.Completion:/*填空*/

                        CompletionQuestionModle complete = (CompletionQuestionModle) PageMap.get(position);
                        CompletionFragment completionFragment = (CompletionFragment) frMap.get((currentposition));
                        completionFragment.submitHomework(complete);

                        break;

                    default:
                        break;
                }


            } else {
                PageMap.put(position, new QuestionModle());
            }

//            if (PageMap.get(currentposition) != null && ((QuestionModle) PageMap.get(currentposition)).isAnswer() == true
//                    && ((QuestionModle) PageMap.get(currentposition)).getAnswerflag().equals("true")) {
//                btncheck.setBackgroundResource(R.color.gray);
//            } else {
//                btncheck.setBackgroundResource(R.color.blue);
//            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /*适配器*/
    private class Adapter extends FragmentStatePagerAdapter {

        private List<QuestionModle> data;
        FragmentManager fragmentManager;

        private Adapter(FragmentManager fragmentManager, List<QuestionModle> data) {
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
            praticeCurrentPosition = position;
            if (subjectype.equals(Constant.Judje)) { /*判断*/
                JudgeFragment fr1 = new JudgeFragment();
                JudjeQuestionModle judgeModle = new JudjeQuestionModle();

                judgeModle.setEachposition(position);//每个题对应数据源的索引
                judgeModle.setQuestion_cate_id(subjectype);//保存当前的题型
                judgeModle.setIs_answered(((JudjeQuestionModle) mDatalist.get(position)).getIs_answered());
                judgeModle.setStandard_answer(((JudjeQuestionModle) mDatalist.get(position)).getStandard_answer());//保存判断题的正确答案
                judgeModle.setId(((JudjeQuestionModle) mDatalist.get(position)).getId());//判断题的ID

                if (PageMap.get(position) != null) {
                    judgeModle.setAnswerflag(((JudjeQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    judgeModle.setMy_answer(((JudjeQuestionModle) PageMap.get(position)).getMy_answer());//学生作答答案
                    judgeModle.setAnswer(((JudjeQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交(练习)
                    judgeModle.setCurrentAnswerPosition(((JudjeQuestionModle) PageMap.get(position)).getCurrentAnswerPosition());//当前选择答案的索引
                }

                Bundle judgeBundle = new Bundle();
                judgeBundle.putSerializable("JudgeQuestionModle", (Serializable) mDatalist.get(position));
                fr1.setArguments(judgeBundle);
                frMap.put(position, fr1);
                PageMap.put(position, judgeModle);
                return fr1;
            } else if (subjectype.equals(Constant.Choice)) { /*选择*/

                ChoiceFragment fr2 = new ChoiceFragment();
                ChoiceQuestionModle choiceModle = new ChoiceQuestionModle();

                choiceModle.setEachposition(position);//每个题对应数据源的索引
                choiceModle.setQuestion_cate_id(subjectype);
                choiceModle.setIs_answered(((ChoiceQuestionModle) mDatalist.get(position)).getIs_answered());
                choiceModle.setStandard_answer(((ChoiceQuestionModle) mDatalist.get(position)).getStandard_answer());//保存选择题的正确答案
                choiceModle.setId(((ChoiceQuestionModle) mDatalist.get(position)).getId());//选择题的ID

                if (PageMap.get(position) != null) {
                    choiceModle.setAnswerflag(((ChoiceQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    choiceModle.setMy_answer(((ChoiceQuestionModle) PageMap.get(position)).getMy_answer());//学生作答答案
                    choiceModle.setChoiceitemposition(((ChoiceQuestionModle) PageMap.get(position)).getChoiceitemposition());//记录选择题选的索引
                    choiceModle.setAnswer(((ChoiceQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }
                Bundle choiceBundle = new Bundle();
                choiceBundle.putSerializable("ChoiceQuestionModle", (Serializable) mDatalist.get(position));
                fr2.setArguments(choiceBundle);
                frMap.put(position, fr2);
                PageMap.put(position, choiceModle);
                return fr2;

            } else if (subjectype.equals(Constant.Sort)) {/*排序*/
                SortFragment fr3 = new SortFragment();
                SortQuestionModle sortModle = new SortQuestionModle();

                sortModle.setEachposition(position);//每个题对应数据源的索引
                sortModle.setIs_answered(((SortQuestionModle) mDatalist.get(position)).getIs_answered());
                sortModle.setQuestion_cate_id(subjectype);
                sortModle.setStandard_answer(((SortQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                sortModle.setId((((SortQuestionModle) mDatalist.get(position)).getId()));//排序题ID

                if (PageMap.get(position) != null) {
                    sortModle.setAnswerflag(((SortQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    sortModle.setSortAnswerMap(((SortQuestionModle) PageMap.get(position)).getSortAnswerMap()); //学生作答答案
                    sortModle.setAnswer(((SortQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }


                Bundle bundle = new Bundle();
                bundle.putSerializable("SortQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr3);
                PageMap.put(position, sortModle);
                fr3.setArguments(bundle);
                return fr3;

            } else if (subjectype.equals(Constant.Line)) {   /*连线*/
                LineFragment fr4 = new LineFragment();
                LineQuestionModle lineModle = new LineQuestionModle();

                lineModle.setEachposition(position);//每个题对应数据源的索引
                lineModle.setQuestion_cate_id(subjectype);//保存当前题型
                lineModle.setIs_answered(((LineQuestionModle) mDatalist.get(position)).getIs_answered());
                lineModle.setStandard_answer(((LineQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                lineModle.setId((((LineQuestionModle) mDatalist.get(position)).getId()));//连线题ID

                if (PageMap.get(position) != null) {
                    lineModle.setAnswerflag(((LineQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    lineModle.setDrawPathList(((LineQuestionModle) PageMap.get(position)).getDrawPathList());//连线题保存答案的坐标点
                    lineModle.setAnswer(((LineQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }

                Bundle linebBundle = new Bundle();
                linebBundle.putSerializable("LineQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr4);
                PageMap.put(position, lineModle);
                fr4.setArguments(linebBundle);
                return fr4;

            } else if (subjectype.equals(Constant.Completion)) {  /*填空*/
                CompletionFragment fr5 = new CompletionFragment();
                CompletionQuestionModle compleModle = new CompletionQuestionModle();

                compleModle.setEachposition(position);//每个题对应数据源的索引
                compleModle.setQuestion_cate_id(subjectype);
                compleModle.setStandard_answer(((CompletionQuestionModle) mDatalist.get(position)).getStandard_answer());//填空题要传入正确答案
                compleModle.setIs_answer(((CompletionQuestionModle) mDatalist.get(position)).getIs_answer());
                compleModle.setId(((CompletionQuestionModle) mDatalist.get(position)).getId());//填空题的ID

                if (PageMap.get(position) != null) {
                    compleModle.setAnswerflag(((CompletionQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    compleModle.setmAllMap(((CompletionQuestionModle) PageMap.get(position)).getmAllMap());//保存自己答案的集合
                    compleModle.setAnswer(((CompletionQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("CompletionQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr5);
                PageMap.put(position, compleModle);
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
        JudjeQuestionModle qm = (JudjeQuestionModle) PageMap.get(currentposition);

        qm.setStandard_answer(questionModle.getStandard_answer());
        qm.setMy_answer(questionModle.getMy_answer());//保存学作答答案
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setCurrentpage(currentposition + 1);//当前是第几页
        qm.setIs_right(questionModle.getIs_right());
        qm.setCurrentAnswerPosition(questionModle.getCurrentAnswerPosition());

    }

    /*选择题回调接口*/
    @Override
    public void submitChoiceFragment(ChoiceQuestionModle questionModle) {
        ChoiceQuestionModle qm = (ChoiceQuestionModle) PageMap.get(currentposition);

        qm.setStandard_answer(questionModle.getStandard_answer());
        qm.setMy_answer(questionModle.getMy_answer());//保存学学生作答答案
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setCurrentpage(currentposition + 1);//当前是第几页
        qm.setIs_right(questionModle.getIs_right());

        qm.setChoiceitemposition(questionModle.getChoiceitemposition());//保存选择题的索引 用于翻页回来后更新选项状态


    }


    /*排序题回调接口*/
    @Override
    public void submitSoreFragment(SortQuestionModle questionModle) {
        SortQuestionModle qm = (SortQuestionModle) PageMap.get(currentposition);

        qm.setStandard_answer(questionModle.getStandard_answer());
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setCurrentpage(currentposition + 1);//当前是第几页
        qm.setInitMyanswerList(questionModle.getInitMyanswerList());//我的答案的集合（Val值 用于上传服务器）

    }

    /*连线题回调接口*/
    @Override
    public void submitLineFragment(LineQuestionModle questionModle) {

        LineQuestionModle qm = (LineQuestionModle) PageMap.get(currentposition);
        qm.setStandard_answer(questionModle.getStandard_answer());
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setCurrentpage(currentposition + 1);//当前是第几页
        qm.setMyanswerMap(questionModle.getMyanswerMap());
        qm.setDrawPathList(questionModle.getDrawPathList());//连线题保存答案的坐标点


    }

    /*填空题回掉接口*/
    @Override
    public void submitCompletionFragment(CompletionQuestionModle questionModle) {
        CompletionQuestionModle qm = (CompletionQuestionModle) PageMap.get(currentposition);

        qm.setStandard_answer(questionModle.getStandard_answer());
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setCurrentpage(currentposition + 1);//当前是第几页
        //保存当前页面填空题的答案（用于翻页回来后 查找当前页的数据 i是从0开始）
        for (int i = 0; i < questionModle.getmAllMap().size(); i++) {
            qm.getmAllMap().put(i, questionModle.getmAllMap().get(i));
        }
    }

    boolean isClickable = true;//true

    /* 改变按钮状态*/
    private void changeBtn() {
        if (isClickable) {
            changeBtnN();
        } else {
            changeBtnY();
        }

    }


    /*练习 改变按钮的颜色*/
    public void changeBtnN() {
        isClickable = false;
        btncheck.setText("NEXT");
        btncheck.setBackgroundResource(R.color.gray);
    }

    /*练习 改变按钮的颜色*/
    public void changeBtnY() {
        isClickable = true;
        btncheck.setText("Check");
        btncheck.setBackgroundResource(R.color.yellow_FEBF12);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sourceFlag = "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*点击答题卡回传*/
        if (requestCode == 0 && resultCode == 1) {
            int current_num = data.getIntExtra("current_num", -1);
            mViewpager.setCurrentItem(current_num);
        }
         /*点击submit回传*/
        else if (requestCode == 0 && resultCode == 2) {
            finishActivity();
        }
    }

    /*监听虚拟按键*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //监控/拦截/屏蔽返回键
//              /*作业模式下*/
//            if (beDohomeWork.getIs_complete().equals("1")) {
//                finishActivity();
//            } else {
//                show();
//            }
            finishActivity();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }


//    private void show() {
//
//        submitDialog = new FxDialog(context) {
//            @Override
//            public void onRightBtn(int flag) {
//                // 参数需要自己判断 is_complete   -1:未开始 0:进行中 1:已完成
//                SubmitHomeWorkAnswer submitHomeWorkAnswer = new SubmitHomeWorkAnswer(DoHomeworkActivity.this, new BeSaveAnswerCard(PageMap, homework_id, mAnswerCardList, is_complete));
//                submitHomeWorkAnswer.submitAnswerCard();
//                submitDialog.dismiss();
//                finishActivity();
//            }
//
//
//            @Override
//            public void onLeftBtn(int flag) {
//
//                finishActivity();
//                submitDialog.dismiss();
//            }
//        };
//        submitDialog.getTitle().setVisibility(View.GONE);
//        submitDialog.setMessage("确定要提交试卷么？");
//        submitDialog.show();
//
//    }

}
