package cn.dajiahui.kid.ui.study.kinds.karaoke;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.tag.TagGroup;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.bean.BeKaraOk;
import cn.dajiahui.kid.ui.study.view.NoScrollViewPager;
import cn.dajiahui.kid.util.Logger;

/*
*
*K啦Ok
* */
public class KaraOkeActivity extends FxActivity implements ViewPager.OnPageChangeListener {

    private NoScrollViewPager mViewpager;
    private LinearLayout pointroot;
    private ImageView[] tips;//裝小点点的数组
    private BeKaraOk beKaraOk;//网络请求数据源
    private KaraoKeOkAdapter karaoKeOkAdapter;
    private String book_id;
    private String unit_id;
    private String unit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setfxTtitle(unit_name);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_kara_oke);
        Bundle mKaraOkebundle = getIntent().getExtras();
        book_id = mKaraOkebundle.getString("BOOK_ID");
        unit_id = mKaraOkebundle.getString("UNIT_ID");
        unit_name = mKaraOkebundle.getString("UNIT_NAME");
        initialize();
        httpData();

    }

    private Map<Integer, KaraOkeFragment> map = new HashMap<>();
    private int mCurrentPosition = 0;//当前fragmentment的索引

    /*课本剧适配器*/
    class KaraoKeOkAdapter extends FragmentStatePagerAdapter {

        BeKaraOk beKaraOk;

        public KaraoKeOkAdapter(FragmentManager fm, BeKaraOk beKaraOk) {
            super(fm);
            this.beKaraOk = beKaraOk;
        }

        @Override
        public int getCount() {

            return beKaraOk.getPage_data().size();
        }


        @Override
        public Fragment getItem(int position) {

            KaraOkeFragment karaOkeFragment = new KaraOkeFragment();
            map.put(position, karaOkeFragment);
            Bundle bundle = new Bundle();

            bundle.putSerializable("BePageData", beKaraOk.getPage_data().get(position));
            karaOkeFragment.setArguments(bundle);
            return karaOkeFragment;

        }

        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
            map.remove(position);

            //希望做一次垃圾回收
            System.gc();
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position);
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("KaraOkeActivity: onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (map.get(mCurrentPosition) != null) {
//            map.get(mCurrentPosition).continueStartVideo();
        }
    }

    @Override
    public void httpData() {
        //网络请求
        RequestUtill.getInstance().httpGetKaraOke(context, KaraOkeCallback, "6", "14"); // K啦ok数据申请
    }

    /**
     * 卡拉ok  callback函数
     */
    ResultCallback KaraOkeCallback = new ResultCallback() {

        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("K拉Okresponse:" + response);
            dismissfxDialog();

            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                beKaraOk = json.parsingObject(BeKaraOk.class);
                karaoKeOkAdapter = new KaraoKeOkAdapter(getSupportFragmentManager(), beKaraOk);
                mViewpager.setAdapter(karaoKeOkAdapter);
                mViewpager.setOnPageChangeListener(KaraOkeActivity.this);
                karaoKeOkAdapter.notifyDataSetChanged();
                setTips();

            } else {
                ToastUtil.showToast(context, json.getMsg());
            }
        }
    };

    private void initialize() {
        mViewpager = (NoScrollViewPager) findViewById(R.id.viewpager);
        pointroot = (LinearLayout) findViewById(R.id.point_root);
        mViewpager.setNoScroll(false);
    }

    private void setTips() {
        //将点点加入到point_root中

        tips = new ImageView[beKaraOk.getPage_data().size()];

        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(5, 5));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);

            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);

            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(TagGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            pointroot.addView(imageView, layoutParams);
        }
    }


    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

//
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME) {//点击的是返回键
//            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {//按键的按下事件
//                Toast.makeText(getApplicationContext(), "dispatchKeyEvent--Down", Toast.LENGTH_SHORT).show();
////				 return false;
//            } else if (event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0) {//按键的抬起事件
//                Toast.makeText(getApplicationContext(), "dispatchKeyEvent--UP", Toast.LENGTH_SHORT).show();
////				 return false;
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }

}
