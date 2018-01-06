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

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeJudge;
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
    private ViewPager mViewpager;
    private int subjectype = -1;//当前题型
    private MediaPlayer mediaPlayer;
    private List<String> list = new ArrayList<>();
    private int currentposition = 1;//当前页面的索引
    private BeJudge currentbeJudge;//当前页面的数据模型
    private Map<Integer, BeJudge> map = new HashMap();//保存每一页的页数和数据
    private BeJudge beJudge;
    private JudgeFragment fr1;
    private ChoiceFragment fr2;
    private SortFragment fr3;
    private LineFragment fr4;
    private CompletionFragment fr5;

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

        final List<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(1);
        data.add(5);
        data.add(4);
        data.add(2);
        data.add(3);
        data.add(2);
        data.add(5);
        data.add(4);
        data.add(5);
        data.add(3);
        data.add(2);
        data.add(5);
        data.add(4);
        seek.setMax(data.size());
        Adapter adapter = new Adapter(getSupportFragmentManager(), data);
        currentbeJudge = new BeJudge();//第一次建立模型
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                seek.setProgress(currentposition);
                mSchedule.setText(currentposition + "/" + data.size());
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }

            }

            @Override
            public void onPageSelected(int position) {
                currentposition = position + 1;

                if (map.get(currentposition) != null) {
                    currentbeJudge = map.get(currentposition);
                    fr1.submitHomework(currentbeJudge);
                    Logger.d("majin", "no创建新的模型");
                } else {
                    currentbeJudge = new BeJudge();
                    Logger.d("majin", "创建新的模型");
                }

                //每次翻页都会创建当前页面的模型
                Logger.d("majin", " onPageSelected position:" + currentposition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Logger.d("majin"," onPageScrollStateChanged state:"+state);
            }
        });
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
        DjhJumpUtil.getInstance().startBaseActivity(CheckHomeworkActivity.this, AnswerCardActivity.class);

    }


    @Override
    public void submitJudgeFragment(BeJudge beJudge) {
        currentbeJudge.setAnswer(beJudge.isAnswer());
        currentbeJudge.setAnswerflag("yes");


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

                        map.put(currentposition, currentbeJudge);

                        if (map.get(currentposition).isWhetheranswer() == true) {

                            Toast.makeText(context, "翻过页 答过题！", Toast.LENGTH_SHORT).show();
//                            if (map.get(currentposition).getSubjectype() == 1) {
                                Logger.d("majin", "传入f1:" +map.get(currentposition).getSubjectype() );
//                                fr1.submitHomework(map.get(currentposition));
//                            }
                            return;
                        }

                        if (map.get(currentposition).isWhetheranswer() == false && map.get(currentposition).getAnswerflag() != null) {
                            Toast.makeText(context, "保存第" + currentposition + "页数据", Toast.LENGTH_SHORT).show();
                            currentbeJudge.setWhetheranswer(true);

                        } else {
                            Toast.makeText(context, "请作答！", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    default:

                        break;

                }

            }
        };
    }


    /*适配器*/
    private class Adapter extends FragmentStatePagerAdapter {

        private List<Integer> data;

        private FragmentManager fragmentManager;
        private JudgeFragment currentFragment;

        private Adapter(FragmentManager fragmentManager, List<Integer> data) {
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
            subjectype = data.get(position);
//            Logger.d("majin", " onPageScrolled subjectype:" + subjectype);
            if (subjectype == 1) {

                beJudge = new BeJudge();//判断题模型（每建立一个碎片就创建一个模型）
                beJudge.setSubjectype(subjectype);//保存当前的题fr1 = new JudgeFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("beJudge", beJudge);
                fr1 = new JudgeFragment();
                fr1.setArguments(bundle);
                return fr1;
            } else if (subjectype == 2) {
                fr2 = new ChoiceFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("path", data.get(position));
                fr2.setArguments(bundle);
                return fr2;
            } else if (subjectype == 3) {
                fr3 = new SortFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("path", data.get(position));
                fr3.setArguments(bundle);
                return fr3;
            } else if (subjectype == 4) {
                fr4 = new LineFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("path", data.get(position));
                fr4.setArguments(bundle);
                return fr4;
            } else if (subjectype == 5) {
                fr5 = new CompletionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("path", data.get(position));
                fr5.setArguments(bundle);
                return fr5;
            }


            return null;
        }


        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
//            Logger.d("majin", "fragment destroyItem");
            //希望做一次垃圾回收
            System.gc();
        }


    }


}
