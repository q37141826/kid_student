package cn.dajiahui.kid.ui.homework;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;

/**
 * 作业
 */
public class FrHomework extends FxFragment {


    private Toolbar toolbar;
    private ViewPager mViewPager;
    private ImageView imgCursor;
    private LinearLayout option;
    private int textViewW = 0;// 页卡标题的宽度
    private int currIndex = 0;// 当前页卡编号
    private TextView staydone;
    private TextView done;
    private TextView staycorrect;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_task, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = getView(R.id.toolbar);

        TextView title = getView(R.id.tool_title);
        title.setText(R.string.tab_task);
//        onRightBtn(R.drawable.ico_add, R.string.addclass);
//        initControl();
//        InitImageView();

    }

    private void initControl() {
//        imgCursor = getView(R.id.cursor);

//        option = getView(R.id.task_option);
//        mViewPager = getView(R.id.task_content);
//        mViewPager.setOffscreenPageLimit(2);/* 预加载页面 */
//        mViewPager.setAdapter(new TaskTabAdapter(getFragmentManager()));
//        mViewPager.setCurrentItem(0);
//
//        mViewPager.setOnPageChangeListener(new FrOnPageChangeListener());
//
//
//        staydone = getView(R.id.task_staydone);
//        done = getView(R.id.task_done);
//        staycorrect = getView(R.id.task_staycorrect);
//
//        staydone.setOnClickListener(new TabOnClickListener(0));
//        done.setOnClickListener(new TabOnClickListener(1));
//        staycorrect.setOnClickListener(new TabOnClickListener(2));



    }

//    /* 标题点击监听 */
//    private class TabOnClickListener implements View.OnClickListener {
//        private int index = 0;
//
//        public TabOnClickListener(int i) {
//            index = i;
//        }
//
//        @Override
//        public void onClick(View v) {
//            mViewPager.setCurrentItem(index);
//        }
//    }

    /* 页卡切换监听 */
//    private class FrOnPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageSelected(int arg0) {
//            if (textViewW == 0) {
//                textViewW = staydone.getWidth();
//            }
//            Animation animation = new TranslateAnimation(textViewW * currIndex,
//                    textViewW * arg0, 0, 0);
//            currIndex = arg0;
//            animation.setFillAfter(true);/* True:图片停在动画结束位置 */
//            animation.setDuration(300);
//            imgCursor.startAnimation(animation);
//            setTextTitleSelectedColor(arg0);
//            setImageViewWidth(textViewW);
//
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//        }
//    }
//
//
//    /* 设置标题文本的颜色 */
//    private void setTextTitleSelectedColor(int arg0) {
//        int count = mViewPager.getChildCount();
//        for (int i = 0; i < count; i++) {
//            TextView mTextView = (TextView) option.getChildAt(i);
//            if (arg0 == i) {
//                mTextView.setTextColor(0xffc80000);
//            } else {
//                mTextView.setTextColor(0xff969696);
//            }
//        }
//    }

    /* 设置图片宽度 */
//    private void setImageViewWidth(int width) {
//        if (width != imgCursor.getWidth()) {
//            LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) imgCursor.getLayoutParams();
//            laParams.width = width;
//            Log.d("majin", "width:" + width);
//            imgCursor.setLayoutParams(laParams);
//        }
//    }

    /* 初始化动画 */
//    private void InitImageView() {
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(0, 0);
//        imgCursor.setImageMatrix(matrix);// 设置动画初始位置
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//
//    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /*各种作业内容适配器  复用*/
//    private class TaskTabAdapter extends FragmentPagerAdapter {
//        ArrayList arrayList = new ArrayList();
//
//
//        private TaskTabAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//            arrayList.add(1);
//            arrayList.add(2);
//            arrayList.add(3);
//        }
//
//        @Override
//        public int getCount() {
//            return arrayList.size();
//        }
//
//        @Override
//        public Fragment getItem(int arg0) {
//
//            FrHomeworkTab fr = new FrHomeworkTab();
//            Bundle bundle = new Bundle();
//            bundle.putString("TV", arrayList.get(arg0) + "");
//            fr.setArguments(bundle);
//
//            return fr;
//        }
//
//        @Override/*销毁的是销毁当前的页数*/
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            //如果注释这行，那么不管怎么切换，page都不会被销毁
//            super.destroyItem(container, position, object);
//            //希望做一次垃圾回收
//            System.gc();
//        }


//    }

    /*添加班级*/
    private void onRightBtn(int drawableId, int textId) {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_right);
            tv.setText(textId);
            tv.setVisibility(View.VISIBLE);
            tv.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}
