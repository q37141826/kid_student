package cn.dajiahui.kid.ui.mine.notice;

import android.os.Bundle;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.bean.BeNoticeLists;

/*通知详情
*
* */
public class NoticeDetailsActivity extends FxActivity {


    private TextView tvtitle;
    private TextView tvconment;
    private TextView tvime;
    private Bundle mNoticeBundle;
    private BeNoticeLists notice_item;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice_details2);
        initialize();
        onBackText();
        mNoticeBundle = getIntent().getExtras();
        notice_item = (BeNoticeLists) mNoticeBundle.getSerializable("NOTICE_ITEM");

        tvtitle.setText(notice_item.getTitle());
        tvconment.setText(notice_item.getContent());
        tvime.setText(notice_item.getCreated_at());

        httpData();
    }

    private void initialize() {
        tvtitle = getView(R.id.tv_title);
        tvconment = getView(R.id.tv_conment);
        tvime = getView(R.id.tv_ime);
    }


    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpNoticeRead(NoticeDetailsActivity.this,callReadNotice, notice_item.getId());
    }



    /**
     * 已读通知callback函数
     */
    ResultCallback callReadNotice = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();

        }

        @Override
        public void onResponse(String response) {
            Logger.d("已读通知：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
//                    noticeList.addAll(beHomeWork.getLists());
            } else {
                ToastUtil.showToast(NoticeDetailsActivity.this, json.getMsg());
            }
        }
    };
}
