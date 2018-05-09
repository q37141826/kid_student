package cn.dajiahui.kid.ui.myclass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chatui.ImConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.MainActivity;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.ui.chat.constant.PreferenceManager;
import cn.dajiahui.kid.ui.chat.db.InviteMessgeDao;
import cn.dajiahui.kid.ui.homework.adapter.ApHomework;
import cn.dajiahui.kid.ui.homework.bean.BeHomeWorkList;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;
import cn.dajiahui.kid.ui.homework.homeworkdetails.HomeWorkDetailsActivity;
import cn.dajiahui.kid.ui.homework.view.AddClassDialog;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.ui.mine.adapter.ApMyclass;
import cn.dajiahui.kid.ui.mine.bean.BeMyClass;
import cn.dajiahui.kid.ui.mine.bean.BeMyclassLists;
import cn.dajiahui.kid.ui.mine.myclass.AddClassActivity;
import cn.dajiahui.kid.ui.mine.myclass.ClassInfoActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by z on 2016/2/3.
 * 班级
 */
public class FrClass extends FxFragment {

    private LinearLayout mRoot;
    private TextView mClassname;
    private TextView mClasscode;
    private TextView mSchool;
    private ListView mListview;
    private MaterialRefreshLayout refresh;
    private TextView tvNUll;
    private List<BeMyclassLists> myClassLists = new ArrayList<BeMyclassLists>();
    private ApMyclass apMyclass;

    private int itemNumber = 0; // 班级数据数


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_class, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = getView(R.id.tool_title);
        title.setText("我的班级");
        toolbar = getView(R.id.toolbar);
        onRightBtn(R.string.audit_class);

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
        apMyclass = new ApMyclass(getActivity(), myClassLists);
        mListview.setAdapter(apMyclass);

        /*我的班级*/
        myclassHttp();
        /*跳转班级详情*/
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("CLASS_ID", myClassLists.get(position).getId());
                bundle.putString("CLASS_NAME", myClassLists.get(position).getClass_name());

                DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), ClassInfoActivity.class, bundle, DjhJumpUtil.getInstance().activtiy_ClassInfo);

            }
        });
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
        RequestUtill.getInstance().httpMyClass(getActivity(), callMyClass, mPageSize + "", mPageNum + "");
    }


    /*我的班級回掉函數*/
    ResultCallback callMyClass = new ResultCallback() {

        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            finishRefreshAndLoadMoer(refresh, 0);
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                if (mPageNum == 1) {
                    myClassLists.clear();
                }
                BeMyClass myClass = json.parsingObject(BeMyClass.class);
                itemNumber = Integer.parseInt(myClass.getTotalRows());
                if (myClass != null && myClass.getLists().size() > 0) {
                    mPageNum++;
                    myClassLists.addAll(myClass.getLists());
                }

                apMyclass.notifyDataSetChanged();

            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, isLastPage()); // 要自己判断是否为最后一页
        }

    };


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


    /**
     * 判断是否为最后一页
     *
     * @return 0 不是最后一页 1 是最后一页
     */
    public int isLastPage() {
        int result = 0;

        if ((mPageNum - 1) * mPageSize >= itemNumber) {
            result = 1;
        }

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DjhJumpUtil.getInstance().activtiy_ClassInfo && resultCode == RESULT_OK) { // 布置作业成功返回
            myclassHttp();
        }
    }


    protected Toolbar toolbar;

    @SuppressLint("ResourceAsColor")
    protected void onRightBtn(int textId) {
        if (toolbar != null) {
            TextView tv = getView(R.id.tool_right);
            tv.setText(textId);

            tv.setVisibility(View.VISIBLE);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), AddClassActivity.class);
                }
            });
        }
    }


}
