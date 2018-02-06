package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.tag.TagGroup;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDrama;
import cn.dajiahui.kid.ui.study.view.NoScrollViewPager;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;

/*
* 课本剧
*
* */
public class TextBookDramaActivity extends FxActivity implements ViewPager.OnPageChangeListener {


    private NoScrollViewPager mViewpager;
    private LinearLayout point_root;
    private List<BeTextBookDrama> mDatalist;
    private ImageView[] tips;//裝小点点的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("课本剧");
        onBack();


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_text_book_drama);
        initialize();
        mViewpager.setNoScroll(false);
        /*模拟数据源*/
        mDatalist = new ArrayList<>();

        BeTextBookDrama b1 = new BeTextBookDrama("课本剧1", KidConfig.getInstance().getPathTextbookPlayMp4() + "9be4e9ac79b22104096d7b78f8dd4bd9.mp4");
        BeTextBookDrama b2 = new BeTextBookDrama("课本剧2", "/storage/emulated/0/qqqq2.mp4");
        BeTextBookDrama b3 = new BeTextBookDrama("课本剧3", KidConfig.getInstance().getPathTextbookPlayMp4() + "9be4e9ac79b22104096d7b78f8dd4bd9.mp4");
        BeTextBookDrama b4 = new BeTextBookDrama("课本剧4", "/storage/emulated/0/qqqq4.mp4");

        mDatalist.add(b1);
        mDatalist.add(b2);
        mDatalist.add(b3);
        mDatalist.add(b4);

        setTips();
        TextBookDramAdapter textBookDramAdapter = new TextBookDramAdapter(getSupportFragmentManager());

        mViewpager.setAdapter(textBookDramAdapter);


        mViewpager.setOnPageChangeListener(this);

    }

    /*初始化*/
    private void initialize() {
        mViewpager = getView(R.id.viewpager);
        point_root = getView(R.id.point_root);
    }


    /*课本剧适配器*/
    class TextBookDramAdapter extends FragmentStatePagerAdapter {

        public TextBookDramAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mDatalist.size();
        }


        @Override
        public Fragment getItem(int position) {

            TextBookDramaFragment dramaFragment = new TextBookDramaFragment();
            Bundle bundle = new Bundle();

            bundle.putSerializable("BeTextBookDrama", mDatalist.get(position));
            dramaFragment.setArguments(bundle);
            return dramaFragment;

        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Logger.d("onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d("onPageSelected");
        setImageBackground(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Logger.d("onPageScrollStateChanged");
    }


    private void setTips() {
        //将点点加入到point_root中

        tips = new ImageView[mDatalist.size()];

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
            point_root.addView(imageView, layoutParams);
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

}
