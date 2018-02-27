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
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.tag.TagGroup;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDrama;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageData;
import cn.dajiahui.kid.ui.study.view.NoScrollViewPager;
import cn.dajiahui.kid.util.Logger;

/*
* 课本剧
*
* */
public class TextBookDramaActivity extends FxActivity implements ViewPager.OnPageChangeListener {


    private NoScrollViewPager mViewpager;
    private LinearLayout point_root;
    //    private List<BeTextBookDrama> mDatalist;
    private ImageView[] tips;//裝小点点的数组

    private String book_id;
    private String unit_id;
    private Bundle mTextBookDramaBundle;
    List<BeTextBookDramaPageData> page_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("课本剧");
        onBack();


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_text_book_drama);
        mTextBookDramaBundle = getIntent().getExtras();
        book_id = mTextBookDramaBundle.getString("BOOK_ID");
        unit_id = mTextBookDramaBundle.getString("UNIT_ID");
        initialize();
        httpData();
        mViewpager.setNoScroll(false);

//        /*模拟数据源*/
//        mDatalist = new ArrayList<>();
//
//        BeTextBookDrama b1 = new BeTextBookDrama("课本剧1", KidConfig.getInstance().getPathTextbookPlayMp4() + "38e1ad62eb24bd971a62a79ed1b533db.mp4");
//        BeTextBookDrama b2 = new BeTextBookDrama("课本剧2", "/storage/emulated/0/qqqq2.mp4");
//        BeTextBookDrama b3 = new BeTextBookDrama("课本剧3", KidConfig.getInstance().getPathTextbookPlayMp4() + "9be4e9ac79b22104096d7b78f8dd4bd9.mp4");
//        BeTextBookDrama b4 = new BeTextBookDrama("课本剧4", "/storage/emulated/0/qqqq4.mp4");
//
//        mDatalist.add(b1);
//        mDatalist.add(b2);
//        mDatalist.add(b3);
//        mDatalist.add(b4);




    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpTextBookDrama(TextBookDramaActivity.this, callTextBookDrama, book_id, unit_id);

    }

    /**
     * 点读本callback函数
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

                page_data = beTextBookDrama.getPage_data();
                /*下载成功后 加载 viewpager*/
                TextBookDramAdapter textBookDramAdapter = new TextBookDramAdapter(getSupportFragmentManager(), page_data);

                mViewpager.setAdapter(textBookDramAdapter);
                setTips();

                mViewpager.setOnPageChangeListener(TextBookDramaActivity.this);
//                downloadTextBookPlayData(beTextBookDrama);
            } else {
                ToastUtil.showToast(TextBookDramaActivity.this, json.getMsg());
            }

        }

    };

//    /*下载课本剧资源*/
//    private void downloadTextBookPlayData(BeTextBookDrama beTextBookDrama) {
//        BeDownFile file = new BeDownFile(Constant.file_textbookplay_mp4, beTextBookDrama.getPage_data().get(0).getPage_url(), "", KidConfig.getInstance().getPathTemp());
//        new DownloadFile(this, file, false, new OnDownload() {
//            @Override
//            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
//
//                    /*关闭下载dialog*/
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//
//                Logger.d("majin-------------课本剧" + fileurl);
//            }
//        });
//    }

    /*初始化*/
    private void initialize() {
        mViewpager = getView(R.id.viewpager);
        point_root = getView(R.id.point_root);
    }


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
            Bundle bundle = new Bundle();

            bundle.putSerializable("page_data", (Serializable) page_data);

            bundle.putInt("position", position);
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
