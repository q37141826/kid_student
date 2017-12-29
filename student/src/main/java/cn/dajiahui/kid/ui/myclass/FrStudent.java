package cn.dajiahui.kid.ui.myclass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;

/**
 * Created by z on 2016/3/7.
 *
 */
public class FrStudent extends FxFragment {
    private TextView tvNUll;

    private TextView tvCQCount, tvKKCount, tvQJCount, tvTKCount,tvCDCount,tvZTCount;
    private ListView listView;


    private MaterialRefreshLayout refresh;
    private String classId = "";

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_class_student, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classId = bundle.getString(Constant.bundle_id);
        tvNUll = getView(R.id.tv_null);
        tvCQCount = getView(R.id.tv_cq_count);
        tvKKCount = getView(R.id.tv_kk_count);
        tvQJCount = getView(R.id.tv_qj_count);
        tvTKCount = getView(R.id.tv_tk_count);
        tvCDCount = getView(R.id.tv_cd_count);
        tvZTCount = getView(R.id.tv_zt_count);
        refresh = getView(R.id.refresh);
        listView = getView(R.id.listView);
//        adapter = new ApAttenceDetail(getActivity(), datas);
//        listView.setAdapter(adapter);
        initRefresh(refresh);
        listView.setEmptyView(tvNUll);
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                finishRefreshAndLoadMoer(refresh,1);
                ToastUtil.showToast(getActivity(), ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);

            }
        };
        RequestUtill.getInstance().httpAttenceDetail(getActivity(), callback, UserController.getInstance().getUserId(), classId);
    }

    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNUll.setText(R.string.e_student_null);
        tvNUll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfxDialog();
                httpData();
            }
        });
    }
}
