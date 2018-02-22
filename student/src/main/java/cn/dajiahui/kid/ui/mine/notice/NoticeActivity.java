package cn.dajiahui.kid.ui.mine.notice;

import android.os.Bundle;
import android.view.View;
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
import cn.dajiahui.kid.ui.homework.bean.BeHomeWorkList;
import cn.dajiahui.kid.ui.mine.adapter.ApNotice;
import cn.dajiahui.kid.ui.mine.bean.BeNotice;


/*
* 通知
* */
public class NoticeActivity extends FxActivity {

    private ListView mListview;
    private List<BeNotice> noticeList = new ArrayList();
    private ApNotice apNotice;
    private TextView mTvnull;
    private MaterialRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_notice);
        onBackText();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice);
        mListview = getView(R.id.listview);
        mTvnull = getView(R.id.tv_null);
        refresh = getView(R.id.refresh);
        mTvnull.setText(R.string.e_notice_null);
        mTvnull.setVisibility(View.VISIBLE);
        mListview.setEmptyView(mTvnull);
        refresh.setHeadView(false);

//        httpData();

        for (int i = 0; i < 20; i++) {
            noticeList.add(new BeNotice("更新内容：" + i, "更新时间201" + i));
        }

        apNotice = new ApNotice(this, noticeList);

        mListview.setAdapter(apNotice);

    }


    @Override
    public void httpData() {
        super.httpData();
        /*接口未定*/
        RequestUtill.getInstance().httpNotice(NoticeActivity.this, callNotice);

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
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeHomeWorkList beHomeWorkList = json.parsingObject(BeHomeWorkList.class);
                noticeList.clear();
//                noticeList.addAll(beHomeWorkList.getLists());
                apNotice.notifyDataSetChanged();
            } else {
                ToastUtil.showToast(NoticeActivity.this, json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, 1);
        }

    };
}
