package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.ui.FxActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.util.Logger;

/*
* 检查作业
* */
public class CheckHomeworkActivity extends FxActivity
        implements JudgeFragment.SubmitJudgeFragment,
        ChoiceFragment.SubmitChoiseFragment,
        JudgeFragment.GetMediaPlayer, SortFragment.SubmitSortFragment, LineFragment.SubmitLineFragment,
        CompletionFragment.SubmitCompletionFragment {

    private LinearLayout homeworkroot;
    private Button btncheck;
    private SeekBar seek;
    private TextView mSchedule;
    private ViewPager mViewpager;
    private String subjectype = "-1";//当前题型
    private MediaPlayer mediaPlayer;
    private int currentposition = 0;//当前页面的索引
    private List<Integer> pagelist = new ArrayList<>();//保存页数的集合（check过的页）
    private Map<Integer, BaseHomeworkFragment> frMap = new HashMap();
    private Map<Integer, Object> PageMap = new HashMap();//保存每一页的页数和数据

    private JudgeFragment fr1;//判断题
    private ChoiceFragment fr2;//选择题
    private SortFragment fr3;//排序题
    private LineFragment fr4;//连线题
    private CompletionFragment fr5;//填空题

    private List<QuestionModle> mdata;//模拟数据元
    private List<Object> mDatalist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.checkhomework);
        onBackText();
        onRightBtn(R.drawable.ic_launcher, R.string.AnswerCard);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_check_homework);
        initialize();
        mDatalist = new ArrayList<>();
            /*OK 判断 连线*/
        String qq = "{    \"data\": [        {            \"book_id\": 0,            \"id\": 4,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01089rty6ais.jpg\",                    \"label\": \"正确\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"错误\",                    \"type\": \"1\",                    \"val\": \"2\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 1,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 0,            \"standard_answer\": \"1\",            \"title\": \"第一个判断题\",            \"unit_id\": 0        },        {            \"book_id\": 8,            \"id\": 4,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"{3:7,2:8,1:6,4:5}\",            \"options\": {                \"left\": [                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109y5aih34p.png\",                        \"label\": \"头部label\",                        \"type\": \"1\",                        \"val\": \"1\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109sge2pcdz.png\",                        \"label\": \"颈部label\",                        \"type\": \"1\",                        \"val\": \"2\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099mnygtvk.png\",                        \"label\": \"胸部label\",                        \"type\": \"1\",                        \"val\": \"3\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099p4ryhvt.png\",                        \"label\": \"尾部label\",                        \"type\": \"1\",                        \"val\": \"4\"                    }                ],                \"right\": [                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01098rwcvhz7.png\",                        \"label\": \"head label\",                        \"type\": \"1\",                        \"val\": \"5\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109xvkfimpt.png\",                        \"label\": \"neck label\",                        \"type\": \"1\",                        \"val\": \"6\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109fxrt53uw.png\",                        \"label\": \"chest label\",                        \"type\": \"1\",                        \"val\": \"7\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109vp24bntc.png\",                        \"label\": \"foot label\",                        \"type\": \"1\",                        \"val\": \"8\"                    }                ]            },            \"org_id\": 100,            \"question_cate_id\": 4,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 3,            \"standard_answer\": \"{3:5,2:6,1:7,4:8}\",            \"title\": \"连线题的示例\",            \"unit_id\": 7        },        {            \"book_id\": 5,            \"id\": 3,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容1\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容2\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容3\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容4\",                    \"type\": \"1\",                    \"val\": \"4\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容5\",                    \"type\": \"1\",                    \"val\": \"5\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 3,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"3,5,4,1,2\",            \"title\": \"排序题示例\",            \"unit_id\": 6        },        {            \"book_id\": 5,            \"id\": 6,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项A\",                    \"type\": \"2\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项B\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项Cqeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项D\",                    \"type\": \"2\",                    \"val\": \"4\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 2,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"2\",            \"title\": \"选择题\",            \"unit_id\": 6        },        {            \"book_id\": 5,            \"id\": 5,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": \"<p>nice to [[m]] [[e]] [[e]] [[t]] you</p>\",            \"org_id\": 100,            \"question_cate_id\": 5,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"m,e,e,t\",            \"title\": \"填空题示例\",            \"unit_id\": 6        }    ],    \"msg\": \"成功\",    \"status\": \"0\"}";
        HeadJson headJson = new HeadJson(qq);

        /*解析数组*/
        mdata = headJson.parsingListArray("data", new GsonType<List<QuestionModle>>() {
        });
        seek.setMax(mdata.size()-1);
        seek.setProgress((currentposition ));
        mSchedule.setText((currentposition + 1) + "/" + mdata.size());
        Adapter adapter = new Adapter(getSupportFragmentManager(), mdata);
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(onPageChangeListener);

        for (int i = 0; i < mdata.size(); i++) {
            switch (mdata.get(i).getQuestion_cate_id()) {

                case Constant.Judje:
                    mDatalist.add(headJson.parsingListArrayByIndex("data", i, JudjeQuestionModle.class));
                    break;
                case Constant.Choice:
                    mDatalist.add(headJson.parsingListArrayByIndex("data", i, ChoiceQuestionModle.class));
                    break;
                case Constant.Sort:
                    mDatalist.add(headJson.parsingListArrayByIndex("data", i, SortQuestionModle.class));
                    break;
                case Constant.Line:
                    mDatalist.add(headJson.parsingListArrayByIndex("data", i, LineQuestionModle.class));
                    break;
                case Constant.Completion:
                    mDatalist.add(headJson.parsingListArrayByIndex("data", i, CompletionQuestionModle.class));
                    break;
                default:
                    break;

            }


        }


    }

    private void initialize() {
        homeworkroot = getView(R.id.homework_root);
        mViewpager = getView(R.id.viewpager);
        seek = getView(R.id.seek);
        mSchedule = getView(R.id.tv_schedule);
        btncheck = (Button) findViewById(R.id.btn_check);
        btncheck.setOnClickListener(onclick);
    }

    @Override
    public void onRightBtnClick(View view) {

//        for (int i = 0; i < pagelist.size(); i++) {
//            //获取模型的索引
//            mdata.set((QuestionModle)(PageMap.get(pagelist.get(i))).getEachposition(), PageMap.get(pagelist.get(i)));//插入对应选择的题
//        }
//        BeSerializableMap answerCard = new BeSerializableMap(mdata);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("answerCard", answerCard);
//        bundle.putInt("answerNum", pagelist.size());
//
//        DjhJumpUtil.getInstance().startBaseActivity(CheckHomeworkActivity.this, AnswerCardActivity.class, bundle, 1);

    }

    /*判断题回掉接口*/
    @Override
    public void submitJudgeFragment(JudjeQuestionModle questionModle) {
        JudjeQuestionModle qm = (JudjeQuestionModle) PageMap.get(currentposition);
        qm.setSubmitAnswer(questionModle.getSubmitAnswer());//保存学作答答案
        qm.setStandard_answer(questionModle.getStandard_answer());//保存当前题的正确答案
        qm.setCurrentpage(currentposition);//当前是第几页 wangzhi
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setId(questionModle.getId());//第几题
        qm.setSubjectype(questionModle.getSubjectype());//获取试题类型
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
    }

    /*选择题回掉接口*/
    @Override
    public void submitChoiceFragment(ChoiceQuestionModle questionModle) {

        ChoiceQuestionModle qm = (ChoiceQuestionModle) PageMap.get(currentposition);

        qm.setSubmitAnswer(questionModle.getSubmitAnswer());//保存学作答答案
        qm.setStandard_answer(questionModle.getStandard_answer());//保存当前题的正确答案
        qm.setCurrentpage(currentposition);//当前是第几页 wangzhi
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setId(questionModle.getId());//第几题
        qm.setSubjectype(questionModle.getSubjectype());//获取试题类型
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setChoiceanswer(questionModle.getChoiceanswer());//选择题答案

        qm.setChoiceitemposition(questionModle.getChoiceitemposition());
    }

    /*填空题回掉接口*/
    @Override
    public void submitCompletionFragment(CompletionQuestionModle questionModle) {

    }

    /*排序题回掉接口*/
    @Override
    public void submitSoreFragment(SortQuestionModle questionModle) {

    }

    /*连线题回调接口*/
    @Override
    public void submitLineFragment(LineQuestionModle questionModle) {

        LineQuestionModle qm = (LineQuestionModle) PageMap.get(currentposition);

//        Logger.d("majin", "1111111111111" + questionModle.getDrawPathList().toString());
        qm.setDrawPathList(questionModle.getDrawPathList());

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

                            return;
                        }

                        if (questionModle.isAnswer() == false && questionModle.getAnswerflag().equals("true")) {
                            Toast.makeText(context, "保存第" + (currentposition + 1) + "题数据", Toast.LENGTH_SHORT).show();
                            btncheck.setBackgroundResource(R.color.gray);
                            pagelist.add(currentposition);
                            questionModle.setAnswer(true);//设置提交答案  true 答过 false 未作答

                            if (questionModle.getSubjectype().equals("1")) {//判断题
                                JudgeFragment judgeFragment = (JudgeFragment) frMap.get((currentposition));
                                //通知判断题碎片
                                judgeFragment.submitHomework(questionModle);
                            } else if (questionModle.getSubjectype().equals("2")) {//选择题
                                ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get((currentposition));
                                //通知选择题碎片
                                choiceFragment.submitHomework(questionModle);
                            }
                            /*连线题*/
                            else if (questionModle.getSubjectype().equals("4")) {
                                LineFragment linFragment = (LineFragment) frMap.get((currentposition));
                                linFragment.submitHomework(questionModle);
                            }

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

            seek.setProgress(currentposition);
            mSchedule.setText((currentposition + 1) + "/" + mdata.size());
            /*滑动停止音频*/
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            if (PageMap.get(position) != null) {
                    /*翻页获取每页的数据模型*/
                QuestionModle questionModle = (QuestionModle) PageMap.get(position);
                Toast.makeText(context, "翻页是" + questionModle.getSubjectype(), Toast.LENGTH_SHORT).show();
               /*判断题*/
                if (questionModle.getSubjectype().equals("1")) {
                    Logger.d("majin", "翻页到判断题");
                    JudgeFragment judgeFragment = (JudgeFragment) frMap.get(position);
                    judgeFragment.submitHomework(questionModle);
                } else if (questionModle.getSubjectype().equals("2")) {
                    Logger.d("majin", "翻页到选择题");
                     /*选择题*/
                    ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get(position);
                    choiceFragment.submitHomework(questionModle);
                } else if (questionModle.getSubjectype().equals("4")) {
                   /*连线题*/
                    Logger.d("majin", "翻页到连线题");
                    LineFragment linFragment = (LineFragment) frMap.get((currentposition));
                    linFragment.submitHomework(questionModle);
                }
            } else {

                PageMap.put(position, new QuestionModle());
            }

            if (PageMap.get(currentposition) != null && ((QuestionModle) PageMap.get(currentposition)).isAnswer() == true
                    && ((QuestionModle) PageMap.get(currentposition)).getAnswerflag().equals("true")) {
                btncheck.setBackgroundResource(R.color.gray);
            } else {
                btncheck.setBackgroundResource(R.color.blue);
            }

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

            if (subjectype.equals(Constant.Judje)) {

                JudgeFragment fr1 = new JudgeFragment();
                JudjeQuestionModle questionModle = new JudjeQuestionModle();

                questionModle.setEachposition(position);//每个题对应数据源的索引
                questionModle.setSubjectype(subjectype);//保存当前的题型
                questionModle.setStandard_answer(((JudjeQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                questionModle.setId(((JudjeQuestionModle) mDatalist.get(position)).getId());//保存第几题

                if (PageMap.get(position) != null) {

                    questionModle.setAnswerflag(((JudjeQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    questionModle.setSubmitAnswer(((JudjeQuestionModle) PageMap.get(position)).getSubmitAnswer());//学生作答答案
                    questionModle.setAnswer(((JudjeQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("JudgeQuestionModle", (Serializable) mDatalist.get(position));
                fr1.setArguments(bundle);

                //保存实例（用于通知各个页面）
                frMap.put(position, fr1);
                PageMap.put(position, questionModle);
                return fr1;

            } else if (subjectype.equals(Constant.Choice)) {

                ChoiceFragment fr2 = new ChoiceFragment();
                ChoiceQuestionModle questionModle = new ChoiceQuestionModle();
                questionModle.setEachposition(position);//每个题对应数据源的索引
                questionModle.setSubjectype(subjectype);//保存当前的题型
                questionModle.setStandard_answer(((ChoiceQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                questionModle.setId((((ChoiceQuestionModle) mDatalist.get(position)).getId()));//保存

                if (PageMap.get(position) != null) {
                    questionModle.setAnswerflag(((ChoiceQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    questionModle.setSubmitAnswer(((ChoiceQuestionModle) PageMap.get(position)).getSubmitAnswer());//学生作答答案
                    questionModle.setAnswer(((ChoiceQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                    questionModle.setChoiceitemposition(((ChoiceQuestionModle) PageMap.get(position)).getChoiceitemposition());
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("ChoiceQuestionModle", (Serializable) mDatalist.get(position));
                fr2.setArguments(bundle);
                //保存实例（用于通知各个页面）
                frMap.put(position, fr2);
                PageMap.put(position, questionModle);

                return fr2;
            } else if (subjectype.equals(Constant.Sort)) {
                SortFragment fr3 = new SortFragment();
                SortQuestionModle questionModle = new SortQuestionModle();

                Bundle bundle = new Bundle();
                bundle.putSerializable("SortQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr3);
                PageMap.put(position, questionModle);
                fr3.setArguments(bundle);
                return fr3;
            } else if (subjectype.equals(Constant.Line)) {
                //连线
                LineFragment fr4 = new LineFragment();
                LineQuestionModle questionModle = new LineQuestionModle();
                questionModle.setEachposition(position);//每个题对应数据源的索引
                questionModle.setSubjectype(subjectype);//保存当前的题型
                questionModle.setStandard_answer(((LineQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                questionModle.setMy_answer(((LineQuestionModle) mDatalist.get(position)).getMy_answer());//向碎片传递我的答案

                if (PageMap.get(position) != null) {
                    questionModle.setAnswerflag(((LineQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    questionModle.setDrawPathList(((LineQuestionModle) PageMap.get(position)).getDrawPathList());
                    questionModle.setAnswer(((LineQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("LineQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr4);
                PageMap.put(position, questionModle);
                fr4.setArguments(bundle);
                return fr4;
            } else if (subjectype.equals(Constant.Completion)) {
                CompletionFragment fr5 = new CompletionFragment();
                CompletionQuestionModle questionModle = new CompletionQuestionModle();
                Logger.d("majin", " 填空:");
                Bundle bundle = new Bundle();
                bundle.putSerializable("CompletionQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr4);
                PageMap.put(position, questionModle);
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


}
