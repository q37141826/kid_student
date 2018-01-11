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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeSerializableMap;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;

/*
* 检查作业
* */
public class CheckHomeworkActivity extends FxActivity implements JudgeFragment.SubmitJudgeFragment, ChoiceFragment.SubmitChoiseFragment, JudgeFragment.GetMediaPlayer {

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
    private Map<Integer, QuestionModle> PageMap = new HashMap();//保存每一页的页数和数据

    private JudgeFragment fr1;//判断题
    private ChoiceFragment fr2;//选择题
    private SortFragment fr3;//排序题
    private LineFragment fr4;//连线题
    private CompletionFragment fr5;//填空题


    private List<QuestionModle> mdata;//模拟数据元


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
             /*解析数组*/
        String zz = "{    \"mdata\": [        {            \"book_id\": 0,            \"id\": 1,            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01089rty6ais.jpg\",                    \"label\": \"正确\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"错误\",                    \"type\": \"1\",                    \"val\": \"2\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 1,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 0,            \"standard_answer\": \"1\",            \"title\": \"第一个判断题\",            \"unit_id\": 0        },        {            \"book_id\": 5,            \"id\": 6,            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项A\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项B\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项C\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项D\",                    \"type\": \"1\",                    \"val\": \"4\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 2,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"2\",            \"title\": \"选择题\",            \"unit_id\": 6        },        {            \"book_id\": 5,            \"id\": 3,            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容1\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容2\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容3\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容4\",                    \"type\": \"1\",                    \"val\": \"4\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容5\",                    \"type\": \"1\",                    \"val\": \"5\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 3,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"3,5,4,1,2\",            \"title\": \"排序题示例\",            \"unit_id\": 6        },        {            \"book_id\": 0,            \"id\": 3,            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01089rty6ais.jpg\",                    \"label\": \"正确\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"错误\",                    \"type\": \"1\",                    \"val\": \"2\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 1,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 0,            \"standard_answer\": \"1\",            \"title\": \"第一个判断题\",            \"unit_id\": 0        },        {            \"book_id\": 0,            \"id\": 4,            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01089rty6ais.jpg\",                    \"label\": \"正确\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"错误\",                    \"type\": \"1\",                    \"val\": \"2\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 1,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 0,            \"standard_answer\": \"1\",            \"title\": \"第一个判断题\",            \"unit_id\": 0        },        {            \"book_id\": 5,            \"id\": 5,            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项A\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项B\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项C\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项D\",                    \"type\": \"1\",                    \"val\": \"4\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 2,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"2\",            \"title\": \"选择题\",            \"unit_id\": 6        }    ],    \"msg\": \"成功\",    \"status\": \"0\"}";

        HeadJson headJson = new HeadJson(zz);
        /*解析数组*/
        mdata = headJson.parsingListArray("mdata", new GsonType<List<QuestionModle>>() {
        });
        mdata.toString();

        seek.setMax(mdata.size());
        mSchedule.setText((currentposition + 1) + "/" + mdata.size());
        Adapter adapter = new Adapter(getSupportFragmentManager(), mdata);
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(onPageChangeListener);
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

        for (int i = 0; i < pagelist.size(); i++) {
            //获取模型的索引
            mdata.set(PageMap.get(pagelist.get(i)).getEachposition(), PageMap.get(pagelist.get(i)));//插入对应选择的题

        }

        BeSerializableMap answerCard = new BeSerializableMap(mdata);
        Bundle bundle = new Bundle();
        bundle.putSerializable("answerCard", answerCard);
        bundle.putInt("answerNum", pagelist.size());

        DjhJumpUtil.getInstance().startBaseActivity(CheckHomeworkActivity.this, AnswerCardActivity.class, bundle, 1);

    }

    /*判断题回掉接口*/
    @Override
    public void submitJudgeFragment(QuestionModle questionModle) {

        QuestionModle qm = PageMap.get(currentposition);
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
    public void submitChoiceFragment(QuestionModle questionModle) {

//        Logger.d("majin", "当前选择答案：" + questionModle.getChoiceanswer());

        QuestionModle qm = PageMap.get(currentposition);

        qm.setSubmitAnswer(questionModle.getSubmitAnswer());//保存学作答答案
        qm.setStandard_answer(questionModle.getStandard_answer());//保存当前题的正确答案
        qm.setCurrentpage(currentposition);//当前是第几页 wangzhi
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setId(questionModle.getId());//第几题
        qm.setSubjectype(questionModle.getSubjectype());//获取试题类型
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setChoiceanswer(questionModle.getChoiceanswer());//选择题答案
        Logger.d("majin", "保存当前选择索引：" + questionModle.getChoiceitemposition());
        qm.setChoiceitemposition(questionModle.getChoiceitemposition());
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
                        QuestionModle questionModle = PageMap.get(currentposition);
                        PageMap.put(currentposition, questionModle);
                        if (questionModle != null && questionModle.isisAnswer() == true && questionModle.getAnswerflag().equals("true")) {

                            return;
                        }

                        if (questionModle.isisAnswer() == false && questionModle.getAnswerflag().equals("true")) {
                            Toast.makeText(context, "保存第" + (currentposition + 1) + "题数据", Toast.LENGTH_SHORT).show();
                            btncheck.setBackgroundResource(R.color.gray);
                            pagelist.add(currentposition);
                            questionModle.setisAnswer(true);//设置提交答案  true 答过 false 未作答

                            if (questionModle.getSubjectype().equals("1")) {//判断题
                                JudgeFragment judgeFragment = (JudgeFragment) frMap.get((currentposition));
                                //通知判断题碎片
                                judgeFragment.submitHomework(questionModle);
                            } else if (questionModle.getSubjectype().equals("2")) {//选择题
                                ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get((currentposition));
                                //通知选择题碎片
                                choiceFragment.submitHomework(questionModle);
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

            if (PageMap.get(position) != null) {
                    /*翻页获取每页的数据模型*/
                QuestionModle questionModle = PageMap.get(position);
               /*判断题*/
                if (questionModle.getSubjectype().equals("1")) {
                    JudgeFragment judgeFragment = (JudgeFragment) frMap.get(position);
                    judgeFragment.submitHomework(questionModle);
                } else if (questionModle.getSubjectype().equals("2")) {
                     /*选择题*/
                    ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get(position);
                    choiceFragment.submitHomework(questionModle);
                }
            } else {

                PageMap.put(position, new QuestionModle());
            }

            if (PageMap.get(currentposition) != null && PageMap.get(currentposition).isisAnswer() == true && PageMap.get(currentposition).getAnswerflag().equals("true")) {
                btncheck.setBackgroundResource(R.color.gray);
            } else {
                btncheck.setBackgroundResource(R.color.blue);
            }
            seek.setProgress(currentposition);
            mSchedule.setText((currentposition + 1) + "/" + mdata.size());
            if (mediaPlayer != null) {//滑动停止音频
                mediaPlayer.stop();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /*适配器*/
    private class Adapter extends FragmentStatePagerAdapter {

        private List<QuestionModle> data1;
        FragmentManager fragmentManager;

        private Adapter(FragmentManager fragmentManager, List<QuestionModle> data) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
            this.data1 = data;
        }

        @Override
        public int getCount() {
            return data1.size();
        }

        @Override
        public Fragment getItem(int position) {
            //获取题型
            subjectype = data1.get(position).getQuestion_cate_id();

            Logger.d("majin", "getItem() subjectype = " + subjectype);

            if (subjectype.equals("1")) {
                JudgeFragment fr1 = new JudgeFragment();
                QuestionModle questionModle = new QuestionModle();
                questionModle.setEachposition(position);//每个题对应数据源的索引
                questionModle.setSubjectype(subjectype);//保存当前的题型
                questionModle.setStandard_answer(data1.get(position).getStandard_answer());//保存当前题的正确答案
                questionModle.setId(data1.get(position).getId());//保存第几题

                if (PageMap.get(position) != null) {
                    questionModle.setAnswerflag(PageMap.get(position).getAnswerflag());//学生作答标记
                    questionModle.setSubmitAnswer(PageMap.get(position).getSubmitAnswer());//学生作答答案
                    questionModle.setisAnswer(PageMap.get(position).isisAnswer());//学生是否提交
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("JudgeQuestionModle", data1.get(position));
                fr1.setArguments(bundle);

                //保存实例（用于通知各个页面）
                frMap.put(position, fr1);
                PageMap.put(position, questionModle);
                return fr1;

            } else if (subjectype.equals("2")) {

                ChoiceFragment fr2 = new ChoiceFragment();
                QuestionModle questionModle = new QuestionModle();

                questionModle.setEachposition(position);//每个题对应数据源的索引
                questionModle.setSubjectype(subjectype);//保存当前的题型
                questionModle.setStandard_answer(data1.get(position).getStandard_answer());//保存当前题的正确答案
                questionModle.setId(data1.get(position).getId());//保存

                if (PageMap.get(position) != null) {
                    questionModle.setAnswerflag(PageMap.get(position).getAnswerflag());//学生作答标记
                    questionModle.setSubmitAnswer(PageMap.get(position).getSubmitAnswer());//学生作答答案
                    questionModle.setisAnswer(PageMap.get(position).isisAnswer());//学生是否提交
                    questionModle.setChoiceitemposition(PageMap.get(position).getChoiceitemposition());
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("ChoiceQuestionModle", data1.get(position));
                fr2.setArguments(bundle);
                //保存实例（用于通知各个页面）
                frMap.put(position, fr2);
                PageMap.put(position, questionModle);

                return fr2;
            } else if (subjectype.equals("3")) {
                SortFragment fr3 = new SortFragment();
                QuestionModle questionModle = new QuestionModle();


                Bundle bundle = new Bundle();
                bundle.putSerializable("baseBean", data1.get(position));
                fr3.setArguments(bundle);
                return fr3;
            } else if (subjectype.equals("4")) {
                fr4 = new LineFragment();
                Bundle bundle = new Bundle();
//                bundle.putSerializable("baseBean", PageModle);
                fr4.setArguments(bundle);
                return fr4;
            } else if (subjectype.equals("5")) {
                fr5 = new CompletionFragment();
                Bundle bundle = new Bundle();
//                bundle.putSerializable("baseBean", PageModle);
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
            Logger.d("majin", "fragment destroyItem" + (position + 1));
            //希望做一次垃圾回收
            System.gc();
        }


    }


}
