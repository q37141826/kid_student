package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.dajiahui.kid.ui.mine.adapter.ApMyclass;
import cn.dajiahui.kid.ui.mine.bean.BeMyClass;
import cn.dajiahui.kid.ui.mine.bean.BeMyclassLists;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
* 我的班级
* */
public class MyClassActivity extends FxActivity {

    private LinearLayout mRoot;
    private TextView mClassname;
    private TextView mClasscode;
    private TextView mSchool;
    private ListView mListview;
    private MaterialRefreshLayout refresh;
    private TextView tvNUll;
    private List<BeMyclassLists> myClassLists = new ArrayList<BeMyclassLists>();
    private ApMyclass apMyclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.my_class);
        onBackText();
        onRightBtn(R.string.addclass);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_class);

        mClassname = getView(R.id.tv_classname);
        mClasscode = getView(R.id.tv_classcode);
        mListview = getView(R.id.listview);
        refresh = getView(R.id.refresh);
        tvNUll = getView(R.id.tv_null);
        tvNUll.setOnClickListener(onClick);
        tvNUll.setText("暂无班级");
        mListview.setEmptyView(tvNUll);
        initRefresh(refresh);
        mSchool = getView(R.id.tv_classcode);

        /*我的班级适配器*/
        apMyclass = new ApMyclass(this, myClassLists);
        mListview.setAdapter(apMyclass);

        /*我的班级*/
        myclassHttp();
        /*跳转班级详情*/
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("CLASS_ID",  myClassLists.get(position).getId());
                bundle.putString("CLASS_NAME", myClassLists.get(position).getClass_name());

                DjhJumpUtil.getInstance().startBaseActivity(MyClassActivity.this, ClassInfoActivity.class, bundle, 0);

            }
            });
        }


        @Override
        protected void onRestart () {
            super.onRestart();
        /*我的班级*/
            myclassHttp();
        }

    /*我的网络请求*/

    private void myclassHttp() {
        mPageNum = 1;
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpMyClass(this, callMyClass, mPageSize + "", mPageNum + "");
    }


    ResultCallback callMyClass = new ResultCallback() {



        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeMyClass myClass = json.parsingObject(BeMyClass.class);
                myClassLists.clear();
                myClassLists.addAll(myClass.getLists());
                apMyclass.notifyDataSetChanged();

            } else {
                ToastUtil.showToast(MyClassActivity.this, json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, 1);
        }

    };

    @Override
    public void onRightBtnClick(View view) {
        DjhJumpUtil.getInstance().startBaseActivity(context, AddClassActivity.class);

        Toast.makeText(context, "加入班级", Toast.LENGTH_SHORT).show();
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
}
