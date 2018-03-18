package cn.dajiahui.kid.ui.mine.myclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.adapter.ApClassSpace;
import cn.dajiahui.kid.ui.mine.bean.BeClassSpace;
import cn.dajiahui.kid.ui.mine.bean.BeClassSpaceList;


/*
* 班级空间
* */
public class ClassSpaceActivity extends FxActivity {
    private ListView mListview;
    private MaterialRefreshLayout refresh;
    private List<BeClassSpaceList> classSpacesList = new ArrayList<>();
    private String classId;
    private ApClassSpace apClassSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_class_space);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_class_space);
        Bundle classSpacebundle = this.getIntent().getExtras();
        classId = (String) classSpacebundle.get("CLASS_ID");

        mListview = getView(R.id.listview);
        refresh = getView(R.id.refresh);
        TextView tvNUll = getView(R.id.tv_null);
        tvNUll.setOnClickListener(onClick);
        tvNUll.setText("暂无班级动态");
        mListview.setEmptyView(tvNUll);
        initRefresh(refresh);

        httpClassSpace();

        apClassSpace = new ApClassSpace(this, classSpacesList);
        mListview.setAdapter(apClassSpace);


    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_null:
                    showfxDialog();
                    httpData();
                    break;
                default:
                    break;
            }

        }
    };

    /*班级空间*/
    private void httpClassSpace() {
        httpData();
    }


    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpClassSpace(ClassSpaceActivity.this, callClassSpace, classId, mPageSize + "", mPageNum + "");
    }

    /*班级空间*/
    ResultCallback callClassSpace = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {

            Logger.d("学生端班级空间 ：" + response);

            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                BeClassSpace beClassSpace = json.parsingObject(BeClassSpace.class);
                List<BeClassSpaceList> list = beClassSpace.getList();
                classSpacesList.clear();
                classSpacesList.addAll(list);
                apClassSpace.notifyDataSetChanged();
            } else {
                ToastUtil.showToast(ClassSpaceActivity.this, json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, 1);
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
