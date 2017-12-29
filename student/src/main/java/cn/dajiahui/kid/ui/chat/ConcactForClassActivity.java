package cn.dajiahui.kid.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.listview.PinnedHeaderListView;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.hyphenate.easeui.domain.EaseUser;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.chat.adapter.ApConcactForClass;
import cn.dajiahui.kid.ui.chat.bean.BeConcact;
import cn.dajiahui.kid.ui.chat.bean.BeConcactClass;
import cn.dajiahui.kid.ui.chat.db.DemoDBManager;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by wdj on 2016/4/5.
 */
public class ConcactForClassActivity extends FxActivity {
    private PinnedHeaderListView listviewConcact;
    private MaterialRefreshLayout refreshConcact;
    private TextView textNullConcact;
    private ApConcactForClass adapter;
    private List<BeConcactClass> datas = new ArrayList<BeConcactClass>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_concact_for_class);
        listviewConcact = getView(R.id.contact_pinnedListView);
        refreshConcact = getView(R.id.contact_refresh);
        textNullConcact = getView(R.id.tv_null);
        initRefresh(refreshConcact);
        listviewConcact.setEmptyView(textNullConcact);
        adapter = new ApConcactForClass(ConcactForClassActivity.this, datas);
        listviewConcact.setAdapter(adapter);
        listviewConcact.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                BeConcact beConcact = datas.get(section).getList().get(position);
                if(StringUtil.isEmpty(beConcact.getObjectId()))
                    return;
                if (beConcact.getObjectId().equals(UserController.getInstance().getUserId())) {
                    ToastUtil.showToast(context, R.string.Cant_chat_with_yourself);
                    return;
                }
                if (!StringUtil.isEmpty(beConcact.getHxId())) {
                    EaseUser user = new EaseUser(beConcact.getHxId());
                    user.setAvatar(beConcact.getAvator());
                    user.setNick(beConcact.getRealName());
                    DemoDBManager.getInstance().saveContact(user);
                    DjhJumpUtil.getInstance().startChatActivity(ConcactForClassActivity.this, beConcact.getHxId(), beConcact.getPhone());
                } else {
                    ToastUtil.showToast(ConcactForClassActivity.this, "环信账号不存在");
                }
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.chat_list);
        onBackText();
        showfxDialog();
        httpData();
    }

    @Override
    public void dismissfxDialog() {
        super.dismissfxDialog();
        textNullConcact.setText(R.string.chat_concact_no);
    }

    @Override
    public void httpData() {
        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(ConcactForClassActivity.this, ErrorCode.error(e));
                finishRefreshAndLoadMoer(refreshConcact, 0);
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                finishRefreshAndLoadMoer(refreshConcact, 1);
                HeadJson headJson = new HeadJson(response);
                if (headJson.getFlag() == 1) {
                    datas = headJson.parsingListArray(new GsonType<List<BeConcactClass>>() {
                    });
                    if (datas!=null)
                    adapter.reFreshList(ConcactForClassActivity.this, datas);
                } else {
                    ToastUtil.showToast(ConcactForClassActivity.this, headJson.getMsg());
                }
            }
        };
        RequestUtill.getInstance().httpContactDetail(ConcactForClassActivity.this, callback, UserController.getInstance().getUserId());
    }
}
