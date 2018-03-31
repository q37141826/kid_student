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

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.tag.TagGroup;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDrama;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageData;
import cn.dajiahui.kid.ui.study.view.NoScrollViewPager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/*
* 课本剧
*
* */
public class TextBookDramaActivity extends FxActivity implements ViewPager.OnPageChangeListener {


    private NoScrollViewPager mViewpager;
    private LinearLayout point_root;
    private ImageView[] tips;//裝小点点的数组

    private String book_id;
    private String unit_id;
    private Bundle mTextBookDramaBundle;
    private List<BeTextBookDramaPageData> page_data;
    private String unit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(unit_name);
        onBack();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_text_book_drama);
        /*播放器不保存进度*/
        JCVideoPlayer.SAVE_PROGRESS = false;
        mTextBookDramaBundle = getIntent().getExtras();
        book_id = mTextBookDramaBundle.getString("BOOK_ID");
        unit_id = mTextBookDramaBundle.getString("UNIT_ID");
        unit_name = mTextBookDramaBundle.getString("UNIT_NAME");
        initialize();
        httpData();
        mViewpager.setNoScroll(false);
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpTextBookDrama(TextBookDramaActivity.this, callTextBookDrama, book_id, unit_id);

    }

    /**
     * 课本剧callback函数
     */
    ResultCallback callTextBookDrama = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("课本剧：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeTextBookDrama beTextBookDrama = json.parsingObject(BeTextBookDrama.class);
                if (beTextBookDrama != null) {
                    page_data = beTextBookDrama.getPage_data();
                    TextBookDramAdapter textBookDramAdapter = new TextBookDramAdapter(getSupportFragmentManager(), page_data);
                    mViewpager.setAdapter(textBookDramAdapter);
                    if (page_data.size() > 1) {
                     /*设置小点点*/
                        setTips();
                    }


                    mViewpager.setOnPageChangeListener(TextBookDramaActivity.this);
                }
            } else {
                ToastUtil.showToast(TextBookDramaActivity.this, json.getMsg());
            }

        }

    };


    /*初始化*/
    private void initialize() {
        mViewpager = getView(R.id.viewpager);
        point_root = getView(R.id.point_root);
    }

    private Map<Integer, TextBookDramaFragment> map = new HashMap<>();

    /*课本剧适配器*/
    class TextBookDramAdapter extends FragmentStatePagerAdapter {
        List<BeTextBookDramaPageData> page_data;

        public TextBookDramAdapter(FragmentManager fm, List<BeTextBookDramaPageData> page_data) {
            super(fm);
            this.page_data = page_data;
        }

        @Override
        public int getCount() {
            return page_data.size();
        }


        @Override
        public Fragment getItem(int position) {

            TextBookDramaFragment dramaFragment = new TextBookDramaFragment();
            map.put(position, dramaFragment);
            Bundle bundle = new Bundle();
            bundle.putSerializable("page_data", (Serializable) page_data);
            bundle.putInt("position", position);
            dramaFragment.setArguments(bundle);
            return dramaFragment;

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
        Logger.d("onPageSelected");
        setImageBackground(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Logger.d("onPageScrollStateChanged");
    }


    private void setTips() {
        //将点点加入到point_root中

        tips = new ImageView[page_data.size()];

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
