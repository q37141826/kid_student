package cn.dajiahui.kid.ui.study.kinds.readingbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.bean.BeReadingBook;
import cn.dajiahui.kid.ui.study.bean.BeReadingBookPageData;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;

/*
点读本
*
* */
public class ReadingBookActivity extends FxActivity {


    private ViewPager mReadViewPager;

    private PointReadingSettings pointReadingSettings;
    private Map<Integer, ReadingBookFragment> mReadingBookMap = new HashMap();
    private Bundle mReadingBookBundle;
    private String book_id;
    private String unit_id;


    private RelativeLayout mCloseMenu;
    private LinearLayout mMenu;//侧边栏
    private Animation menuOutAnimation;
    private Animation menuInAnimation;
    private LinearLayout mtranslateRoot;
    private Switch mSwitch;
    private TextView mChinese, mEnglish;

    private Boolean showMenu = false;//显示侧边栏  true 显示  false 不显示

    private int showLanguage = 0;//0 中文 1 英文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*标题是活的*/
        setfxTtitle(mReadingBookBundle.getString("UNIT_NAME"));
        onBackText();
        onBackText();
        onRightBtn(R.string.pointReadingSettings);


    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpReadingBook(ReadingBookActivity.this, callReadingBook, book_id, unit_id);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_reading_book);
        mReadingBookBundle = getIntent().getExtras();
        book_id = mReadingBookBundle.getString("BOOK_ID");
        unit_id = mReadingBookBundle.getString("UNIT_ID");
        /*获取点读本资源*/
        httpData();
        initialize();

        /*监听viewpager滑动*/
        mReadViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pointReadingSettings = mReadingBookMap.get(position);
            }

            @Override
            public void onPageSelected(int position) {
                if (mMenu.getVisibility() == View.VISIBLE) {
                    closeMenu();
                }
                pointReadingSettings = mReadingBookMap.get(position);
                if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                    PlayMedia.getPlaying().mediaPlayer.stop();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }


    /*swich开关*/
    CompoundButton.OnCheckedChangeListener onCheckedChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                mtranslateRoot.setVisibility(View.VISIBLE);
                for (int i = 0; i < mReadingBookMap.size(); i++) {
                    mReadingBookMap.get(i).showmTranslateBottomroot();
                }
            } else {
                for (int i = 0; i < mReadingBookMap.size(); i++) {
                    mReadingBookMap.get(i).hidemTranslateBottomroot();
                }
                mtranslateRoot.setVisibility(View.INVISIBLE);

                closeMenu();
            }
        }
    };

    @Override
    public void onRightBtnClick(View view) {

        if (!showMenu) {
            openMenu();
        } else {
            closeMenu();
        }
    }


    /*初始化*/
    private void initialize() {
        mReadViewPager = getView(R.id.read_pager);

        mMenu = getView(R.id.menu);
        mSwitch = getView(R.id.switch_point);
        mChinese = getView(R.id.tv_chinese);
        mEnglish = getView(R.id.tv_english);
        mCloseMenu = getView(R.id.closeMenu);
        mtranslateRoot = getView(R.id.translate_root);

        mSwitch.setOnCheckedChangeListener(onCheckedChange);
        mtranslateRoot.setOnClickListener(onClick);
        mCloseMenu.setOnClickListener(onClick);
        mEnglish.setOnClickListener(onClick);
        mChinese.setOnClickListener(onClick);

        menuInAnimation = AnimationUtils.loadAnimation(this, R.anim.pointbook_slide_right_in);//进入
        menuOutAnimation = AnimationUtils.loadAnimation(this, R.anim.pointbook_slide_right_out);//退出

    }

    /*点击事件监听*/
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.closeMenu:
                    if (showMenu) {
                        closeMenu();
                    }
                    break;

                case R.id.tv_chinese:

                    mChinese.setBackgroundResource(R.drawable.round_bgyellow_febf12_homwwork_startstudy);
                    mEnglish.setBackgroundResource(R.drawable.round_bggray_333333_study_pointbook);
                    showLanguage = 0;
                    for (int i = 0; i < mReadingBookMap.size(); i++) {
                        mReadingBookMap.get(i).pointReadingSettings(0);
                    }
                    closeMenu();
                    break;
                case R.id.tv_english:
                    showLanguage = 1;
                    mChinese.setBackgroundResource(R.drawable.round_bggray_333333_study_pointbook);
                    mEnglish.setBackgroundResource(R.drawable.round_bgyellow_febf12_homwwork_startstudy);

                    for (int i = 0; i < mReadingBookMap.size(); i++) {
                        mReadingBookMap.get(i).pointReadingSettings(1);
                    }
                    closeMenu();
                    break;
                default:
                    break;
            }

        }
    };

    /*关闭侧边栏*/
    private void closeMenu() {
        mMenu.startAnimation(menuOutAnimation);
        mMenu.setVisibility(View.INVISIBLE);
        showMenu = !showMenu;
    }

    /*显示侧边栏*/
    private void openMenu() {
        mMenu.startAnimation(menuInAnimation);
        mMenu.setVisibility(View.VISIBLE);
        showMenu = !showMenu;
    }

    /*
     *点读适配器
     * */
    private class ReadAdapter extends FragmentStatePagerAdapter {

        private List<BeReadingBookPageData> page_data;

        private ReadAdapter(FragmentManager fragmentManager, List<BeReadingBookPageData> page_data) {
            super(fragmentManager);
            this.page_data = page_data;
        }

        @Override
        public int getCount() {
            return page_data.size();
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);

        }

        @Override
        public Fragment getItem(int position) {

            ReadingBookFragment fr = new ReadingBookFragment();
            mReadingBookMap.put(position, fr);
            Bundle bundle = new Bundle();
            bundle.putSerializable("page_data", (Serializable) page_data);
            bundle.putInt("showLanguage", showLanguage);
            bundle.putInt("position", position);

            fr.setArguments(bundle);

            return fr;
        }

        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
//            super.destroyItem(container, position, object);
//            mReadingBookMap.remove(position);
            //希望做一次垃圾回收
//            System.gc();
        }
    }

//    /*隐藏虚拟按键*/
//    public void HideNavigationBar() {
//        Window window = getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
//        window.setAttributes(params);
//    }

    /**
     * 点读本callback函数
     */
    ResultCallback callReadingBook = new ResultCallback() {

        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("点读本：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                BeReadingBook beReadingBook = json.parsingObject(BeReadingBook.class);
                List<BeReadingBookPageData> page_data = beReadingBook.getPage_data();

                ReadAdapter adapter = new ReadAdapter(getSupportFragmentManager(), page_data);
                mReadViewPager.setAdapter(adapter);
                mReadViewPager.setOffscreenPageLimit(page_data.size());//设置缓存view 的个数

            } else {
                ToastUtil.showToast(ReadingBookActivity.this, json.getMsg());
            }

        }

    };

    public interface PointReadingSettings {
        public void pointReadingSettings(int flag);//0 chinese 1  english
    }
}
