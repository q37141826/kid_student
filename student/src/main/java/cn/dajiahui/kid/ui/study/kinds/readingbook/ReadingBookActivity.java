package cn.dajiahui.kid.ui.study.kinds.readingbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
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
import cn.dajiahui.kid.util.Logger;

/*
点读本
*
* */
public class ReadingBookActivity extends FxActivity {


    private ViewPager mReadViewPager;
    private RelativeLayout scoll;
    private PlayAll playAll;
    private Map<Integer, ReadingBookFragment> mReadingBookMap = new HashMap();
    private Bundle mReadingBookBundle;
    private String book_id;
    private String unit_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*标题是活的*/
        setfxTtitle(mReadingBookBundle.getString("UNIT_NAME"));
        onBackText();
//        HideNavigationBar();
        onBackText();
        onRightBtn(R.string.play);


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
        Logger.d("book_id:" + book_id);
        Logger.d("unit_id:" + unit_id);
        /*获取点读本资源*/
        httpData();
        initialize();

       /*监听viewpager滑动*/
        mReadViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                playAll = mReadingBookMap.get(position);
            }

            @Override
            public void onPageSelected(int position) {
                playAll = mReadingBookMap.get(position);
                if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                    PlayMedia.getPlaying().mediaPlayer.stop();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }


    @Override
    public void onRightBtnClick(View view) {
        playAll.playAll();
    }

    /*初始化*/
    private void initialize() {
        mReadViewPager = getView(R.id.read_pager);
        scoll = getView(R.id.scoll);
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

            bundle.putInt("position", position);
            fr.setArguments(bundle);

            return fr;
        }

        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
            mReadingBookMap.remove(position);
            //希望做一次垃圾回收
            System.gc();
        }
    }

    /*隐藏虚拟按键*/
    public void HideNavigationBar() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }

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

            } else {
                ToastUtil.showToast(ReadingBookActivity.this, json.getMsg());
            }

        }

    };

    public interface PlayAll {
        public void playAll();
    }
}
