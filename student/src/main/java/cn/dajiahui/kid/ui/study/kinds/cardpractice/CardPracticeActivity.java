package cn.dajiahui.kid.ui.study.kinds.cardpractice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeCradPratice;
import cn.dajiahui.kid.util.Logger;

/*
* 单词卡
* */
public class CardPracticeActivity extends FxActivity implements
        CardPraticeFragment.NoticeCheckButton {

    private cn.dajiahui.kid.ui.study.view.NoScrollViewPager mCardpager;
    private TextView tvname;
    private TextView tvnumber;
    private Button btnnext;

    private int currentPositinon = 0;
    private List<BeCradPratice> mCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setfxTtitle("111111111");
        onBackText();


        initialize();

        /*模拟数据*/
        mCardList = new ArrayList<>();

        BeCradPratice b1 = new BeCradPratice("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg", "/storage/emulated/0/testsmall.mp3", "", "卡片练习1");
        BeCradPratice b2 = new BeCradPratice("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg", "/storage/emulated/0/testsmall2.mp3", "", "卡片练习2");
        BeCradPratice b3 = new BeCradPratice("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg", "/storage/emulated/0/testsmall.mp3", "", "卡片练习3");
        BeCradPratice b4 = new BeCradPratice("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg", "/storage/emulated/0/testsmall2.mp3", "", "卡片练习4");
        BeCradPratice b5 = new BeCradPratice("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg", "/storage/emulated/0/testsmall.mp3", "", "卡片练习5");
        BeCradPratice b6 = new BeCradPratice("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01086vr8ufyg.jpg", "/storage/emulated/0/testsmall2.mp3", "", "卡片练习6");

        mCardList.add(b1);
        mCardList.add(b2);
        mCardList.add(b3);
        mCardList.add(b4);
        mCardList.add(b5);
        mCardList.add(b6);


        CardAdapter adapter = new CardAdapter(getSupportFragmentManager(), mCardList);
        mCardpager.setAdapter(adapter);


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_card_practice);
    }

    /*初始化数据*/
    private void initialize() {
        mCardpager = getView(R.id.card_pager);
        tvname = getView(R.id.tv_name);
        tvnumber = getView(R.id.tv_number);
        btnnext = getView(R.id.btn_next);
        btnnext.setOnClickListener(onClick);
    }


    /*
   *卡片练习适配器
   * */
    private class CardAdapter extends FragmentStatePagerAdapter {

        private List<BeCradPratice> mCardList;

        private CardAdapter(FragmentManager fragmentManager, List<BeCradPratice> mCardList) {
            super(fragmentManager);
            this.mCardList = mCardList;
        }

        @Override
        public int getCount() {
            return mCardList.size();
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);

        }


        @Override
        public Fragment getItem(int arg0) {
            Logger.d("arg0:" + arg0);
            currentPositinon = arg0;
            CardPraticeFragment fr = new CardPraticeFragment();
            Bundle bundle = new Bundle();
            BeCradPratice beCradPratice = mCardList.get(arg0);
            bundle.putSerializable("BeCradPratice", beCradPratice);
            bundle.putInt("position", arg0);
            fr.setArguments(bundle);

            return fr;
        }

        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);

            //希望做一次垃圾回收
            System.gc();
        }
    }

    @Override
    public void NoticeCheck(boolean ischeck, int position) {
        if (ischeck) {
            Logger.d("1 ischeck:" + ischeck);
            btnnext.setBackgroundResource(R.color.yellow_FEBF12);
            btnnext.setClickable(true);

        } else {
            Logger.d("2 ischeck:" + ischeck);
            btnnext.setBackgroundResource(R.color.gray);
            btnnext.setClickable(false);

            if (position >= 0) {
                tvnumber.setText((position + 1) + "/" + mCardList.size());
            }

        }

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.d("currentPositinon:" + currentPositinon);
            mCardpager.setCurrentItem(currentPositinon);


            Toast.makeText(context, "next", Toast.LENGTH_SHORT).show();

        }
    };

}
