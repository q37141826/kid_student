package cn.dajiahui.kid.ui.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.notice.adapter.ApNotice;
import cn.dajiahui.kid.ui.notice.bean.BeNotice;


/**
 * Created by z on 2016/2/3.
 * 通知Fragment
 */
public class FrNotice extends FxFragment {
    private TextView tvNUll;
    private ListView listview;
    private ApNotice adapter;
    private List<BeNotice> lists = new ArrayList<BeNotice>();
    private MaterialRefreshLayout refresh;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_notice, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNUll = getView(R.id.tv_null);
        refresh = getView(R.id.refresh);
        tvNUll = getView(R.id.tv_null);
        listview = getView(R.id.listview);
        listview.setEmptyView(tvNUll);
        initRefresh(refresh);
        adapter = new ApNotice(getContext(), lists);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DjhJumpUtil.getInstance().startNoticeDetails(getContext(), adapter.getItem(position).getObjectId());
            }
        });


        listview.setEmptyView(tvNUll);
        if (!isCreateView) {
            isCreateView = true;
            indexHttp();
        }
        //标题
        TextView title = getView(R.id.tool_title);
//        title.setText(R.string.tab_notice);

    }

    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNUll.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ico_null, 0, 0);
        tvNUll.setText(R.string.e_msg_null);
        tvNUll.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_null) {
                indexHttp();
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //显示
            if (lists.size() == 0) {
                indexHttp();
            }
        }
    }

    public void indexHttp() {
        pagNum = 1;
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        RequestUtill.getInstance().httpNoticeList(getContext(), new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showToast(getContext(), ErrorCode.error(e));
                dismissfxDialog();
                finishRefreshAndLoadMoer(refresh, 1);
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    List<BeNotice> temp = json.parsingListArray(new GsonType<List<BeNotice>>() {
                    });
                    if (temp != null && temp.size() > 0) {
                        lists.clear();
                        lists.addAll(temp);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(getContext(), json.getMsg());
                }
                finishRefreshAndLoadMoer(refresh, 1);
            }
        }, UserController.getInstance().getUserId(), pagNum, "20");
    }
}
