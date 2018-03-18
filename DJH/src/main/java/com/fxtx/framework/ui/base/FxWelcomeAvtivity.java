package com.fxtx.framework.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fxtx.framework.R;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.viewpage.CirclePageIndicator;


/**
 * @author djh-zy
 * @version :1
 * @description :
 */

public abstract class FxWelcomeAvtivity extends FxActivity {
    private ViewPager pager;
    private Integer views[];
    private CirclePageIndicator mIndicator;
    private boolean isType;
    private boolean canJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = initWelcome();
        initData();
        isType = getIntent().getBooleanExtra("welcome", false);
    }

    @Override
    public void setStatusBar(Toolbar title) {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_welcome);
        pager = getView(R.id.wl_pager);
        mIndicator = getView(R.id.wl_indicator);
        if (!hineIndicator())
            mIndicator.setVisibility(View.GONE);
    }

    public boolean hineIndicator() {
        return true;
    }


    @Override
    protected void finishAnim() {
        if (isType) {
            overridePendingTransition(0, R.anim.base_slide_left_out);
        } else {
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
    }


    public void initData() {
        FistViewAdapter adapter = new FistViewAdapter(
                this.getSupportFragmentManager());
        pager.setAdapter(adapter);
        if (mIndicator.getVisibility() == View.VISIBLE)
            mIndicator.setViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == views.length - 1 && positionOffset == 0
                        && positionOffsetPixels == 0) {
                    @SuppressLint("RestrictedApi")
                    WelcomeFr fragment = (WelcomeFr) getSupportFragmentManager().getFragments().get(views.length - 1);
                    welcomeClick(fragment);
//                    if (canJump) {
//                        // 事件执行一次后进行重置,避免事件多次触发
//                        canJump = false;
//                        if (isType) {
//                            finishActivity();
//                        } else {
//                            welcomeClick();
//                        }
//                        finishActivity();
//                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 判断是否是划动状态且是最后一页
//                if (state == ViewPager.SCROLL_STATE_DRAGGING
//                        && pager.getCurrentItem() == views.length - 1) {
//
//
////                    canJump = true;
//                } else {
////                    canJump = false;
//                }
            }
        });
    }

    /**
     * 处理  滑动到最后的时候处理事件
     */
    public abstract void welcomeClick(WelcomeFr fragment);

    /**
     * 处理 初始化 数组
     *
     * @return
     */
    protected abstract Integer[] initWelcome();

    public class FistViewAdapter extends FragmentPagerAdapter {

        public FistViewAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public Fragment getItem(int arg0) {
            WelcomeFr fr = new WelcomeFr();
            Bundle bundle = new Bundle();
            bundle.putInt("id", views[arg0]);
            fr.setArguments(bundle);
            return fr;
        }
    }
}