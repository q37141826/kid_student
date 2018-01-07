package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BaseBean;
import cn.dajiahui.kid.ui.homework.bean.BeSerializableMap;
import cn.dajiahui.kid.ui.homework.view.NoPreloadViewPager;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;

/*
* 检查作业
* */
public class CheckHomeworkActivity extends FxActivity implements JudgeFragment.SubmitJudgeFragment, JudgeFragment.GetMediaPlayer {

    private LinearLayout homeworkroot;
    private Button btncheck;
    private SeekBar seek;
    private TextView mSchedule;
    //    private ViewPager mViewpager;
    private cn.dajiahui.kid.ui.homework.view.NoPreloadViewPager mViewpager;
    private int subjectype = -1;//当前题型
    private MediaPlayer mediaPlayer;
    private int currentposition = 1;//当前页面的索引
    private BaseBean currentbeJudge;//当前页面的数据模型
    private Map<Integer, BaseBean> map = new HashMap();//保存每一页的页数和数据
    private List<Integer> pagelist = new ArrayList<>();//保存页数的集合（check过的页）

    private JudgeFragment fr1;
    private ChoiceFragment fr2;
    private SortFragment fr3;
    private LineFragment fr4;
    private CompletionFragment fr5;
    private List<BaseBean> data;

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
        currentbeJudge = new BaseBean();//判断题模型（每建立一个碎片就创建一个模型）
        initialize();

        data = new ArrayList<>();
        data.add(new BaseBean(1, "true", "1"));
        data.add(new BaseBean(1, "true", "2"));
        data.add(new BaseBean(1, "false", "3"));
        data.add(new BaseBean(1, "true", "4"));
        data.add(new BaseBean(1, "false", "5"));
        data.add(new BaseBean(1, "true", "6"));
        data.add(new BaseBean(1, "false", "7"));
        data.add(new BaseBean(1, "true", "8"));
        data.add(new BaseBean(1, "true", "9"));

        seek.setMax(data.size());
        Adapter adapter = new Adapter(getSupportFragmentManager(), data);
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
            data.set(map.get(pagelist.get(i)).getEachposition(), map.get(pagelist.get(i)));//插入对应选择的题

        }


        BeSerializableMap answerCard = new BeSerializableMap(data);
        Bundle bundle = new Bundle();
        bundle.putSerializable("answerCard", answerCard);
        bundle.putInt("answerNum", pagelist.size());

        DjhJumpUtil.getInstance().startBaseActivity(CheckHomeworkActivity.this, AnswerCardActivity.class, bundle, 1);

    }


    @Override
    public void submitJudgeFragment(BaseBean baseBean) {
        currentbeJudge.setAnswer(baseBean.getAnswer());//保存学作答答案
        currentbeJudge.setTrueAnswer(baseBean.getTrueAnswer());//保存当前题的正确答案
        currentbeJudge.setCurrentpage(currentposition);
        currentbeJudge.setEachposition(baseBean.getEachposition());//存储每个题对应数据源的索引
        currentbeJudge.setNomber(baseBean.getNomber());//第几题
        currentbeJudge.setSubjectype(baseBean.getSubjectype());//获取试题类型
        currentbeJudge.setAnswerflag(baseBean.getAnswerflag());//学生作答标记

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

                        if (map.get(currentposition) != null && map.get(currentposition).isWhetheranswer() == true && map.get(currentposition).getAnswerflag() == true) {
                            if (currentbeJudge.getSubjectype() == 1) {
                                fr1.submitHomework(currentbeJudge);
                            }
                            Toast.makeText(context, "已经check过第" + currentposition + "题了！", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        map.put(currentposition, currentbeJudge);

                        if (currentbeJudge.isWhetheranswer() == false && currentbeJudge.getAnswerflag() == true) {
                            Toast.makeText(context, "保存第" + currentposition + "题数据", Toast.LENGTH_SHORT).show();
                            pagelist.add(currentposition);
                            currentbeJudge.setWhetheranswer(true);//设置做答过题 true 答过 false 未作答

                            fr1.submitHomework(currentbeJudge);//通知碎片
                        } else {
                            Toast.makeText(context, "请作答第" + currentposition + "题", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    default:

                        break;

                }

            }
        };
    }

    /*viewpager滑动监听*/
    private NoPreloadViewPager.OnPageChangeListener onPageChangeListener = new NoPreloadViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentposition = position + 1;//当前题的页数

            if (map.get(currentposition) != null) {
                currentbeJudge = map.get(currentposition);
                if (currentbeJudge.getSubjectype() == 1) {
                    fr1.submitHomework(currentbeJudge);
                }
            } else {
                currentbeJudge = new BaseBean();
            }


            seek.setProgress(currentposition);
            mSchedule.setText(currentposition + "/" + data.size());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /*适配器*/
    private class Adapter extends FragmentStatePagerAdapter {

        private List<BaseBean> data1;

        private Adapter(FragmentManager fragmentManager, List<BaseBean> data) {
            super(fragmentManager);

            this.data1 = data;
        }

        @Override
        public int getCount() {
            return data1.size();
        }

        @Override
        public Fragment getItem(int position) {
            int i = position + 1;
            subjectype = data1.get(position).getSubjectype();

            if (subjectype == 1) {
                fr1 = new JudgeFragment();
                currentbeJudge.setEachposition(position);
                currentbeJudge.setSubjectype(subjectype);//保存当前的题型
                currentbeJudge.setTrueAnswer(data1.get(position).getTrueAnswer());//保存当前题的正确答案
                currentbeJudge.setNomber(data1.get(position).getNomber());//保存
                currentbeJudge.setAnswerflag(currentbeJudge.getAnswerflag());
                currentbeJudge.setAnswer(currentbeJudge.getAnswer());
                currentbeJudge.setWhetheranswer(currentbeJudge.isWhetheranswer());

                Bundle bundle = new Bundle();
                bundle.putSerializable("baseBean", currentbeJudge);
                fr1.setArguments(bundle);
                return fr1;
            } else if (subjectype == 2) {
                fr2 = new ChoiceFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("baseBean", currentbeJudge);
                fr2.setArguments(bundle);
                return fr2;
            } else if (subjectype == 3) {
                fr3 = new SortFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("baseBean", currentbeJudge);
                fr3.setArguments(bundle);
                return fr3;
            } else if (subjectype == 4) {
                fr4 = new LineFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("baseBean", currentbeJudge);
                fr4.setArguments(bundle);
                return fr4;
            } else if (subjectype == 5) {
                fr5 = new CompletionFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("baseBean", currentbeJudge);
                fr5.setArguments(bundle);
                return fr5;
            }

            return null;
        }


        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
            Logger.d("majin", "fragment destroyItem" + (position + 1));
            //希望做一次垃圾回收
            System.gc();
        }


    }


}
