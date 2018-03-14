package cn.dajiahui.kid.ui.mine.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.adapter.ApNotice;
import cn.dajiahui.kid.ui.mine.bean.BeNotice;
import cn.dajiahui.kid.ui.mine.bean.BeNoticeLists;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;


/*
* 通知
* */
public class NoticeActivity extends FxActivity {

    private ListView mListview;
    private List<BeNoticeLists> noticeList = new ArrayList();
    private ApNotice apNotice;
    private TextView mTvnull;
    private MaterialRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_notice);
        onBackText();
        onRightBtn(R.string.clean_notice);
    }

    /*清空通知*/
    @Override
    public void onRightBtnClick(View view) {
        httpCleanNotice();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice);
        mListview = getView(R.id.listview);
        mTvnull = getView(R.id.tv_null);
        refresh = getView(R.id.refresh);
        mTvnull.setText(R.string.e_notice_null);
        initRefresh(refresh);
        mTvnull.setVisibility(View.VISIBLE);
        mListview.setEmptyView(mTvnull);

        apNotice = new ApNotice(this, noticeList);
        mListview.setAdapter(apNotice);

         /*监听点击事件*/
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BeNoticeLists beNoticeItem = new BeNoticeLists(noticeList.get(position).getContent(), DateUtils.time(noticeList.get(position).getCreated_at()),
                        noticeList.get(position).getTitle(), noticeList.get(position).getId());
                Bundle bundle = new Bundle();
                bundle.putSerializable("NOTICE_ITEM", beNoticeItem);

                DjhJumpUtil.getInstance().startBaseActivity(NoticeActivity.this, NoticeDetailsActivity.class, bundle, 0);
            }
        });
    }


    /*网络请求*/
    private void noticekHttp() {
        mPageNum = 1;
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        super.httpData();

        RequestUtill.getInstance().httpNotice(NoticeActivity.this, callNotice, mPageSize, mPageNum);

    }

    /*清空*/
    private void httpCleanNotice() {
        RequestUtill.getInstance().httpCleanNotice(NoticeActivity.this, callClearNotice);
    }


    @Override
    protected void onResume() {
        super.onResume();
        noticekHttp();
    }

    /**
     * 通知callback函数
     */
    ResultCallback callNotice = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            finishRefreshAndLoadMoer(refresh, 1);
        }

        @Override
        public void onResponse(String response) {
            Logger.d("摩尔通知：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                if (mPageNum == 1) {
                    noticeList.clear();
                }
                BeNotice beHomeWork = json.parsingObject(BeNotice.class);
                if (beHomeWork != null) {
                    itemNumber = Integer.parseInt(beHomeWork.getTotalRows());
                    if (beHomeWork.getLists().size() > 0) {
                        mPageNum++;
                        noticeList.addAll(beHomeWork.getLists());
                    }
                    apNotice.notifyDataSetChanged();
                }
            } else {
                ToastUtil.showToast(NoticeActivity.this, json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, isLastPage());
        }

    };

    /**
     * 清空通知callback函数
     */
    ResultCallback callClearNotice = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();

        }

        @Override
        public void onResponse(String response) {
//            Logger.d("清空摩尔通知：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                noticeList.clear();
                apNotice.notifyDataSetChanged();
            } else {
                ToastUtil.showToast(NoticeActivity.this, json.getMsg());
            }
        }
    };

    /**
     * 判断是否为最后一页
     *
     * @return 0 不是最后一页 1 是最后一页
     */
    public int itemNumber = 0;

    public int isLastPage() {
        int result = 0;

        if ((mPageNum - 1) * mPageSize >= itemNumber) {
            result = 1;
        }

        return result;
    }
}
