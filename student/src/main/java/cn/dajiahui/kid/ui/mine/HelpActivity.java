package cn.dajiahui.kid.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.adapter.ApHelp;
import cn.dajiahui.kid.ui.mine.bean.BeHelp;

/**
 * Created by z on 2016/2/25.
 * <p/>
 * 使用帮助
 */
public class HelpActivity extends FxActivity {

    protected TextView tvNull;
    protected ListView listView;
    protected List<BeHelp> helpList = new ArrayList<BeHelp>();
    protected ApHelp adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_help);
        tvNull = getView(R.id.tv_null);
        listView = getView(R.id.help_listview);
        listView.setEmptyView(tvNull);
        adapter = new ApHelp(context, helpList);
//        listView.setOnItemClickListener(itemListener);
        listView.setAdapter(adapter);
        httpData();
    }

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle("使用帮助");
        onBackText();
    }

    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNull.setText(R.string.not_data);
        tvNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfxDialog();
                httpData();
            }
        });
    }

    @Override
    public void httpData() {
        showfxDialog();
        RequestUtill.getInstance().httpHelp(context, "2", new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    List<BeHelp> temp = json.parsingListArray(new GsonType<List<BeHelp>>() {
                    });
                    if (temp != null && temp.size() > 0) {
                        helpList.clear();
                        helpList.addAll(temp);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        });
    }
}
