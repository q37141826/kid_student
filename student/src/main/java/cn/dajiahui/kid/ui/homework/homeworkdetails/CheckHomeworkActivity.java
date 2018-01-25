package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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
import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;

/*
* 检查作业
* */
public class CheckHomeworkActivity extends FxActivity
        implements JudgeFragment.SubmitJudgeFragment,
        ChoiceFragment.SubmitChoiseFragment,
        JudgeFragment.GetMediaPlayer, SortFragment.SubmitSortFragment, LineFragment.SubmitLineFragment,
        CompletionFragment.SubmitCompletionFragment {

    private SeekBar seek;
    private TextView mSchedule;
    private ViewPager mViewpager;
    private String subjectype = "";//当前题型
    private MediaPlayer mediaPlayer;
    private int currentposition = 0;//当前页面的索引
    private List<Integer> pagelist = new ArrayList<>();//保存页数的集合（check过的页）
    private Map<Integer, BaseHomeworkFragment> frMap = new HashMap();
    private HashMap<Integer, Object> PageMap = new HashMap();//保存每一页的页数和数据

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
            /* isanswer 1*/
//          String qq = "{    \"data\": [        {            \"book_id\": 8,            \"id\": 4,            \"is_answer\": 1,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"{3:7,2:8,1:6,4:5}\",            \"options\": {                \"left\": [                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109y5aih34p.png\",                        \"label\": \"头部label\",                        \"type\": \"1\",                        \"val\": \"1\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109sge2pcdz.png\",                        \"label\": \"颈部label\",                        \"type\": \"1\",                        \"val\": \"2\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099mnygtvk.png\",                        \"label\": \"胸部label\",                        \"type\": \"1\",                        \"val\": \"3\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099p4ryhvt.png\",                        \"label\": \"尾部label\",                        \"type\": \"1\",                        \"val\": \"4\"                    }                ],                \"right\": [                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01098rwcvhz7.png\",                        \"label\": \"head label\",                        \"type\": \"1\",                        \"val\": \"5\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109xvkfimpt.png\",                        \"label\": \"neck label\",                        \"type\": \"1\",                        \"val\": \"6\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109fxrt53uw.png\",                        \"label\": \"chest label\",                        \"type\": \"1\",                        \"val\": \"7\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109vp24bntc.png\",                        \"label\": \"foot label\",                        \"type\": \"1\",                        \"val\": \"8\"                    }                ]            },            \"org_id\": 100,            \"question_cate_id\": 4,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 3,            \"standard_answer\": \"{3:5,2:6,1:7,4:8}\",            \"title\": \"连线题的示例\",            \"unit_id\": 7        },        {            \"book_id\": 5,            \"id\": 5,            \"is_answer\": 1,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": \"<p>nice to [[m]] [[e]] [[e]] [[t]] you</p>\",            \"org_id\": 100,            \"question_cate_id\": 5,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"m,e,e,t\",            \"title\": \"填空题示例\",            \"unit_id\": 6        },        {            \"book_id\": 0,            \"id\": 4,            \"is_answer\": 1,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"1\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01089rty6ais.jpg\",                    \"label\": \"正确\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"错误\",                    \"type\": \"1\",                    \"val\": \"2\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 1,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 0,            \"standard_answer\": \"1\",            \"title\": \"第一个判断题\",            \"unit_id\": 0        },        {            \"book_id\": 5,            \"id\": 3,            \"is_answer\": 1,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"3,5,4,1,2\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容1\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容2\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容3\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容4\",                    \"type\": \"1\",                    \"val\": \"4\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容5\",                    \"type\": \"1\",                    \"val\": \"5\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 3,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"4,5,3,2,1\",            \"title\": \"排序题示例\",            \"unit_id\": 6        },        {            \"book_id\": 5,            \"id\": 6,            \"is_answer\": 1,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"1\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项A\",                    \"type\": \"2\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项B\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项Cqeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项D\",                    \"type\": \"2\",                    \"val\": \"4\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 2,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"2\",            \"title\": \"选择题\",            \"unit_id\": 6        }    ],    \"msg\": \"成功\",    \"status\": \"0\"}";
//          /*连线isanswer 0   填空 isanswer 0   */
         String qq = "{    \"data\": [        {            \"book_id\": 8,            \"id\": 4,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"{3:7,2:8,1:6,4:5}\",            \"options\": {                \"left\": [                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109y5aih34p.png\",                        \"label\": \"头部label\",                        \"type\": \"1\",                        \"val\": \"1\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109sge2pcdz.png\",                        \"label\": \"颈部label\",                        \"type\": \"1\",                        \"val\": \"2\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099mnygtvk.png\",                        \"label\": \"胸部label\",                        \"type\": \"1\",                        \"val\": \"3\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099p4ryhvt.png\",                        \"label\": \"尾部label\",                        \"type\": \"1\",                        \"val\": \"4\"                    }                ],                \"right\": [                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01098rwcvhz7.png\",                        \"label\": \"head label\",                        \"type\": \"1\",                        \"val\": \"5\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109xvkfimpt.png\",                        \"label\": \"neck label\",                        \"type\": \"1\",                        \"val\": \"6\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109fxrt53uw.png\",                        \"label\": \"chest label\",                        \"type\": \"1\",                        \"val\": \"7\"                    },                    {                        \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109vp24bntc.png\",                        \"label\": \"foot label\",                        \"type\": \"1\",                        \"val\": \"8\"                    }                ]            },            \"org_id\": 100,            \"question_cate_id\": 4,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 3,            \"standard_answer\": \"{3:5,2:6,1:7,4:8}\",            \"title\": \"连线题的示例\",            \"unit_id\": 7        },        {            \"book_id\": 5,            \"id\": 5,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": \"<p>nice to [[m]] [[e]] [[e]] [[t]] you</p>\",            \"org_id\": 100,            \"question_cate_id\": 5,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"m,e,e,t\",            \"title\": \"填空题示例\",            \"unit_id\": 6        },        {            \"book_id\": 0,            \"id\": 4,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01089rty6ais.jpg\",                    \"label\": \"正确\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"错误\",                    \"type\": \"1\",                    \"val\": \"2\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 1,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 0,            \"standard_answer\": \"1\",            \"title\": \"第一个判断题\",            \"unit_id\": 0        },        {            \"book_id\": 5,            \"id\": 3,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"3,5,4,1,2\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容1\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容2\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容3\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容4\",                    \"type\": \"1\",                    \"val\": \"4\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"内容5\",                    \"type\": \"1\",                    \"val\": \"5\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 3,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"4,5,3,2,1\",            \"title\": \"排序题示例\",            \"unit_id\": 6        },        {            \"book_id\": 5,            \"id\": 6,            \"is_answer\": 0,            \"is_auto\": \"\",            \"is_right\": \"\",            \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",            \"my_answer\": \"\",            \"options\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项A\",                    \"type\": \"2\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项B\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项Cqeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg\",                    \"label\": \"选项D\",                    \"type\": \"2\",                    \"val\": \"4\"                }            ],            \"org_id\": 100,            \"question_cate_id\": 2,            \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",            \"school_id\": 2,            \"standard_answer\": \"2\",            \"title\": \"选择题\",            \"unit_id\": 6        }    ],    \"msg\": \"成功\",    \"status\": \"0\"}";
         HeadJson headJson = new HeadJson(qq);

        /*解析数组*/
        mdata = headJson.parsingListArray("data", new GsonType<List<QuestionModle>>() {
        });
        seek.setMax(mdata.size() - 1);
        seek.setProgress((currentposition));
        mSchedule.setText((currentposition + 1) + "/" + mdata.size());
        Adapter adapter = new Adapter(getSupportFragmentManager(), mdata);
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(onPageChangeListener);

        /*解析json数据*/
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

    /*初始化*/
    private void initialize() {
        mViewpager = getView(R.id.viewpager);
        seek = getView(R.id.seek);
        mSchedule = getView(R.id.tv_schedule);
//        btncheck = (Button) findViewById(R.id.btn_check);
//        btncheck.setOnClickListener(onclick);
    }

    /*答题卡*/
    @Override
    public void onRightBtnClick(View view) {

        Intent intent = new Intent(CheckHomeworkActivity.this, AnswerCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("answerCard", new BeSaveAnswerCard(PageMap));
        intent.putExtras(bundle);
        startActivity(intent);

    }


    @Override
    public void getMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

//    /*点击事件*/
//    private View.OnClickListener onclick;
//
//    {
//        onclick = new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.btn_check:
//                        QuestionModle questionModle = (QuestionModle) PageMap.get(currentposition);
//
//                        PageMap.put(currentposition, questionModle);
//                        if (questionModle != null && questionModle.isAnswer() == true && questionModle.getAnswerflag().equals("true")) {
//
//                            return;
//                        }
//
//                        if (questionModle.isAnswer() == false && questionModle.getAnswerflag().equals("true")) {
//                            Toast.makeText(context, "保存第" + (currentposition + 1) + "题数据", Toast.LENGTH_SHORT).show();
//                            pagelist.add(currentposition);
//                            questionModle.setAnswer(true);//设置提交答案  true 答过 false 未作答
//                            /*判断*/
//                            if (questionModle.getQuestion_cate_id().equals(Constant.Judje)) {
//                                JudgeFragment judgeFragment = (JudgeFragment) frMap.get((currentposition));
//                                //通知判断题碎片
//                                judgeFragment.submitHomework(questionModle);
//                            }
//                            /*选择*/
//                            else if (questionModle.getQuestion_cate_id().equals(Constant.Choice)) {
//                                ChoiceFragment choiceFragment = (ChoiceFragment) frMap.get((currentposition));
//                                //通知选择题碎片
//                                choiceFragment.submitHomework(questionModle);
//                            }
//                            /*排序*/
//                            else if (questionModle.getQuestion_cate_id().equals(Constant.Sort)) {
//
//                                SortFragment sortFragment = (SortFragment) frMap.get((currentposition));
//                                sortFragment.submitHomework(questionModle);
//                            }
//                            /*连线题*/
//                            else if (questionModle.getQuestion_cate_id().equals(Constant.Line)) {
//                                LineFragment linFragment = (LineFragment) frMap.get((currentposition));
//                                linFragment.submitHomework(questionModle);
//                            }
//                            /*连线题*/
//                            else if (questionModle.getQuestion_cate_id().equals(Constant.Completion)) {
//
//                            }
//
//                        } else {
//                            Toast.makeText(context, "请作答第" + (currentposition + 1) + "题", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        break;
//                    default:
//
//                        break;
//
//                }
//
//            }
//        };
//    }

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

            if (subjectype.equals(Constant.Judje)) { /*判断*/
                JudgeFragment fr1 = new JudgeFragment();
                JudjeQuestionModle judgeModle = new JudjeQuestionModle();

                judgeModle.setEachposition(position);//每个题对应数据源的索引
                judgeModle.setQuestion_cate_id(subjectype);//保存当前的题型
                judgeModle.setIs_answer(((JudjeQuestionModle) mDatalist.get(position)).getIs_answer());
                judgeModle.setStandard_answer(((JudjeQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                judgeModle.setId(((JudjeQuestionModle) mDatalist.get(position)).getId());//保存第几题

//                Logger.d("JudjeQuestionModle getItem getAnswerflag. "+questionModle.getAnswerflag());
//                Logger.d("JudjeQuestionModle getItem getQuestion_cate_id. "+ ((JudjeQuestionModle) mDatalist.get(position)).getQuestion_cate_id());

                if (PageMap.get(position) != null) {
                    judgeModle.setAnswerflag(((JudjeQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    judgeModle.setSubmitAnswer(((JudjeQuestionModle) PageMap.get(position)).getSubmitAnswer());//学生作答答案
                    judgeModle.setJudgeAnswerFlag(((JudjeQuestionModle) PageMap.get(position)).getJudgeAnswerFlag());//获取判断题选择答案的标记（mtrue 正确 mfalse 错误）
//                  questionModle.setAnswer(((JudjeQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
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
                choiceModle.setIs_answer(((ChoiceQuestionModle) mDatalist.get(position)).getIs_answer());
                choiceModle.setStandard_answer(((ChoiceQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
//              questionModle.setId((((ChoiceQuestionModle) mDatalist.get(position)).getId()));//保存

                if (PageMap.get(position) != null) {
                    choiceModle.setAnswerflag(((ChoiceQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    choiceModle.setSubmitAnswer(((ChoiceQuestionModle) PageMap.get(position)).getSubmitAnswer());//学生作答答案
                    choiceModle.setChoiceitemposition(((ChoiceQuestionModle) PageMap.get(position)).getChoiceitemposition());//记录选择题选的索引
//                  questionModle.setAnswer(((ChoiceQuestionModle) PageMap.get(position)).isAnswer());//学生是否提交
                }
                Bundle choiceBundle = new Bundle();
                choiceBundle.putSerializable("ChoiceQuestionModle", (Serializable) mDatalist.get(position));
                fr2.setArguments(choiceBundle);
                frMap.put(position, fr2);
                PageMap.put(position, choiceModle);
                return fr2;

            } else if (subjectype.equals(Constant.Sort)) {/*排序*/
                SortFragment fr3 = new SortFragment();
                SortQuestionModle questionModle = new SortQuestionModle();

                questionModle.setEachposition(position);//每个题对应数据源的索引
                questionModle.setIs_answer(((SortQuestionModle) mDatalist.get(position)).getIs_answer());
                questionModle.setQuestion_cate_id(subjectype);
                questionModle.setStandard_answer(((SortQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案

//                questionModle.setId((((SortQuestionModle) mDatalist.get(position)).getId()));//保存

                if (PageMap.get(position) != null) {
                    questionModle.setAnswerflag(((SortQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    questionModle.setSortAnswerMap(((SortQuestionModle) PageMap.get(position)).getSortAnswerMap()); //学生作答答案
                }


                Bundle bundle = new Bundle();
                bundle.putSerializable("SortQuestionModle", (Serializable) mDatalist.get(position));
                frMap.put(position, fr3);
                PageMap.put(position, questionModle);
                fr3.setArguments(bundle);
                return fr3;

            } else if (subjectype.equals(Constant.Line)) {   /*连线*/
                LineFragment fr4 = new LineFragment();
                LineQuestionModle lineModle = new LineQuestionModle();

                lineModle.setEachposition(position);//每个题对应数据源的索引

                lineModle.setQuestion_cate_id(subjectype);//保存当前题型
                lineModle.setIs_answer(((LineQuestionModle) mDatalist.get(position)).getIs_answer());
                lineModle.setStandard_answer(((LineQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案

//                Logger.d("LineQuestionModle getItem getAnswerflag. "+questionModle.getAnswerflag());
//                Logger.d("LineQuestionModle getItem getQuestion_cate_id. "+ ((LineQuestionModle) mDatalist.get(position)).getQuestion_cate_id());

                if (PageMap.get(position) != null) {
                    lineModle.setAnswerflag(((LineQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    lineModle.setDrawPathList(((LineQuestionModle) PageMap.get(position)).getDrawPathList());//连线题保存答案的坐标点

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

//                Logger.d("CompletionFragment.subjectype"+subjectype);
                compleModle.setEachposition(position);//每个题对应数据源的索引
                compleModle.setQuestion_cate_id(subjectype);
                compleModle.setIs_answer(((CompletionQuestionModle) mDatalist.get(position)).getIs_answer());

//                questionModle.setStandard_answer(((CompletionQuestionModle) mDatalist.get(position)).getStandard_answer());//保存当前题的正确答案
                if (PageMap.get(position) != null) {
                    compleModle.setAnswerflag(((CompletionQuestionModle) PageMap.get(position)).getAnswerflag());//学生作答标记
                    compleModle.setmAllMap(((CompletionQuestionModle) PageMap.get(position)).getmAllMap());//保存自己答案的集合
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


//        qm.setQuestion_cate_id(questionModle.getQuestion_cate_id());

        qm.setSubmitAnswer(questionModle.getSubmitAnswer());//保存学作答答案
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setCurrentpage(currentposition+1);//当前是第几页

        qm.setJudgeAnswerFlag(questionModle.getJudgeAnswerFlag());//获取判断题选择答案的标记（mtrue 正确 mfalse 错误）

//        Logger.d("判断题回调接口.getAnswerflag()"+questionModle.getAnswerflag());
//        Logger.d("判断题回调接口.getQuestion_cate_id()"+questionModle.getQuestion_cate_id());

//        qm.setStandard_answer(questionModle.getStandard_answer());//保存当前题的正确答案
//        qm.setId(questionModle.getId());//第几题
    }

    /*选择题回调接口*/
    @Override
    public void submitChoiceFragment(ChoiceQuestionModle questionModle) {
        ChoiceQuestionModle qm = (ChoiceQuestionModle) PageMap.get(currentposition);


//        qm.setQuestion_cate_id(questionModle.getQuestion_cate_id());

        qm.setSubmitAnswer(questionModle.getSubmitAnswer());//保存学学生作答答案
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setCurrentpage(currentposition+1);//当前是第几页

        qm.setChoiceitemposition(questionModle.getChoiceitemposition());//保存选择题的索引 用于翻页回来后更新选项状态

//        Logger.d("ChoiceQuestionModle.getAnswerflag()" + questionModle.getAnswerflag());
//        Logger.d("ChoiceQuestionModle.getQuestion_cate_id()" + questionModle.getQuestion_cate_id());
//        qm.setId(questionModle.getId());//第几题
//        qm.setStandard_answer(questionModle.getStandard_answer());//保存当前题的正确答案
    }


    /*排序题回调接口*/
    @Override
    public void submitSoreFragment(SortQuestionModle questionModle) {
        SortQuestionModle qm = (SortQuestionModle) PageMap.get(currentposition);


        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setCurrentpage(currentposition+1);//当前是第几页



    }

    /*连线题回调接口*/
    @Override
    public void submitLineFragment(LineQuestionModle questionModle) {

        LineQuestionModle qm = (LineQuestionModle) PageMap.get(currentposition);

        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setCurrentpage(currentposition+1);//当前是第几页

        qm.setDrawPathList(questionModle.getDrawPathList());//连线题保存答案的坐标点  （带到答题卡活动就报错）

//        Logger.d("连线题回调接口.getAnswerflag()"+questionModle.getAnswerflag());
//        Logger.d("连线题回调接口.getQuestion_cate_id()"+questionModle.getQuestion_cate_id());

    }

    /*填空题回掉接口*/
    @Override
    public void submitCompletionFragment(CompletionQuestionModle questionModle) {
        CompletionQuestionModle qm = (CompletionQuestionModle) PageMap.get(currentposition);


        qm.setEachposition(questionModle.getEachposition());//存储每个题对应数据源的索引
        qm.setAnswerflag(questionModle.getAnswerflag());//学生作答标记
        qm.setCurrentpage(currentposition+1);//当前是第几页
        //保存当前页面填空题的答案（用于翻页回来后 查找当前页的数据）
        for (int i = 1; i < questionModle.getmAllMap().size(); i++) {
            qm.getmAllMap().put(i, questionModle.getmAllMap().get(i));
        }

//        Logger.d("CompletionQuestionModle.getAnswerflag()" + questionModle.getAnswerflag());
//        Logger.d("CompletionQuestionModle.getQuestion_cate_id()" + questionModle.getQuestion_cate_id());

    }
}
