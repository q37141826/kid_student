package cn.dajiahui.kid.ui.study.kinds.cardpractice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;
import com.squareup.okhttp.Request;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeCradPratice;
import cn.dajiahui.kid.ui.study.bean.BeCradPraticePageData;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;

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

    private String book_id;
    private String unit_id;
    private Bundle mCardPracticeBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(mCardPracticeBundle.getString("UNIT_NAME"));
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_card_practice);
        mCardPracticeBundle = getIntent().getExtras();
        book_id = mCardPracticeBundle.getString("BOOK_ID");
        unit_id = mCardPracticeBundle.getString("UNIT_ID");
        initialize();
        httpData();
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpCardPratice(CardPracticeActivity.this, callCardPratice, "6", "14");
    }

    private List<BeCradPraticePageData> page_data;
    /**
     * 卡片练习callback函数
     */
    ResultCallback callCardPratice = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("卡片练习：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeCradPratice beCradPratice = json.parsingObject(BeCradPratice.class);
                if (beCradPratice != null) {
                    page_data = beCradPratice.getPage_data();
                     /*获取Mp3名称*/
                    String sMp3 = MD5.getMD5(page_data.get(0).getMusic_oss_url().substring(page_data.get(0).getMusic_oss_url().lastIndexOf("/"))) + ".mp3";

                        /*判断mp3文件是否下载过*/
                    if (FileUtil.fileIsExists(KidConfig.getInstance().getPathCardPratice() + sMp3)) {
                        CardAdapter adapter = new CardAdapter(getSupportFragmentManager(), page_data);
                        mCardpager.setAdapter(adapter);

                    } else {
                        downloadCardPratice(page_data.get(0).getMusic_oss_url());
                    }


                }
            } else {
                ToastUtil.showToast(CardPracticeActivity.this, json.getMsg());
            }

        }

    };

    /*下載*/
    private void downloadCardPratice(String music_oss_url) {
        Logger.d("下载----downloadKaraOkeMp3----");

        BeDownFile file = new BeDownFile(Constant.file_card_pratice, music_oss_url, "", KidConfig.getInstance().getPathTemp());

        new DownloadFile(CardPracticeActivity.this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                CardAdapter adapter = new CardAdapter(getSupportFragmentManager(), page_data);
                mCardpager.setAdapter(adapter);
                progressDialog.dismiss();
                Logger.d("fileurl:" + fileurl);
            }
        });

    }

    /*初始化数据*/
    private void initialize() {
        mCardpager = getView(R.id.card_pager);
        tvname = getView(R.id.tv_name);
        tvnumber = getView(R.id.tv_number);
        btnnext = getView(R.id.btn_next);
        btnnext.setOnClickListener(onClick);
    }

    private BeCradPraticePageData beCradPraticePageData;

    /*
   *卡片练习适配器
   * */
    private class CardAdapter extends FragmentStatePagerAdapter {

        private List<BeCradPraticePageData> mCardList;


        private CardAdapter(FragmentManager fragmentManager, List<BeCradPraticePageData> mCardList) {
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
            currentPositinon = arg0;
            CardPraticeFragment fr = new CardPraticeFragment();
            Bundle bundle = new Bundle();
            beCradPraticePageData = mCardList.get(arg0);
            bundle.putSerializable("BeCradPraticePageData", beCradPraticePageData);
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

            if (tvnumber.getText().equals(page_data.size() + "/" + page_data.size())) {
                btnnext.setVisibility(View.GONE);
            }


        } else {
            Logger.d("2 ischeck:" + ischeck);
            btnnext.setBackgroundResource(R.color.gray);
            btnnext.setClickable(false);

            if (position >= 0) {
                tvnumber.setText((position + 1) + "/" + page_data.size());
            }

        }

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.d("currentPositinon:" + currentPositinon);
            mCardpager.setCurrentItem(currentPositinon);

        }
    };

}
