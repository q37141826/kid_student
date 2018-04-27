package cn.dajiahui.kid.ui.homework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.adapter.ApHomework;
import cn.dajiahui.kid.ui.homework.bean.BeHomeWorkList;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;
import cn.dajiahui.kid.ui.homework.homeworkdetails.HomeWorkDetailsActivity;
import cn.dajiahui.kid.ui.homework.view.AddClassDialog;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.ui.mine.myclass.AddClassActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

import static android.app.Activity.RESULT_OK;

/**
 * 作业
 */
public class FrHomework extends FxFragment {

    private ListView mListview;
    private TextView tvNUll;
    private ApHomework apHomework;//作业列表适配器
    private List<BeHomework> mHomeWorklists = new ArrayList<BeHomework>();
    private MaterialRefreshLayout refresh;
    private AddClassDialog addClassDialog;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_homework, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = getView(R.id.tool_title);
        mListview = getView(R.id.listview);
        title.setText(R.string.tab_homework);
        tvNUll = getView(R.id.tv_null);
        tvNUll.setText("暂无作业");
        refresh = getView(R.id.refresh);
        tvNUll.setOnClickListener(onClick);
        mListview.setEmptyView(tvNUll);
        homeworkHttp();
        initRefresh(refresh);
        apHomework = new ApHomework(getActivity(), mHomeWorklists);//
        mListview.setAdapter(apHomework);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < apHomework.getPositionList().size(); i++) {
                    if (apHomework.getPositionList().get(i) == position) {
                        return;
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putString("homework_id", mHomeWorklists.get(position).getId());
                bundle.putString("starttime", mHomeWorklists.get(position).getStart_time());
                bundle.putString("UNIT_NAME", mHomeWorklists.get(position).getName());
                /*跳转作业详情 在网络请请求获取数据*/
                DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), HomeWorkDetailsActivity.class, bundle, DjhJumpUtil.getInstance().activtiy_HomeworkDetails);


            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /*网络请求*/
    public void homeworkHttp() {
        mPageNum = 1;
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpGetStudentHomeWork(getActivity(), callHomeWork, mPageSize, mPageNum);

    }

    /*fragment的隐藏于显示*/
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            if (mHomeWorklists.size() == 0) {
                homeworkHttp();
            }
        }
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
            switch (v.getId()) {
                case R.id.tv_null:
                    homeworkHttp();
                    break;
                case R.id.btn_cancle:
                    addClassDialog.dismiss();
                    break;

                case R.id.btn_choose:
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), AddClassActivity.class);
                    addClassDialog.dismiss();
                    break;
            }
        }
    };
    /**
     * 学生作业列表callback函数
     */
    ResultCallback callHomeWork = new ResultCallback() {

        @Override
        public void onError(Request request, Exception e) {
            Logger.d("做作业失败：" + e);
            dismissfxDialog();
            finishRefreshAndLoadMoer(refresh, 1);
        }

        @Override
        public void onResponse(String response) {
            Logger.d("做作业：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                if (mPageNum == 1) {
                    mHomeWorklists.clear();
                }

                BeHomeWorkList beHomeWorkList = json.parsingObject(BeHomeWorkList.class);
                if (beHomeWorkList != null) {
                    /*班级申请*/
                    if (!beHomeWorkList.getClass_status().equals("2")) {
                        /*弹框引导加入班级*/
                        addClassDialog = new AddClassDialog(getActivity(), R.layout.dialog_addclass) {
                            @Override
                            public void initView() {
                                Button buttonCancle = (Button) rootView.findViewById(R.id.btn_cancle);
                                buttonCancle.setOnClickListener(onClick);
                                Button buttonChoice = (Button) rootView.findViewById(R.id.btn_choose);
                                buttonChoice.setOnClickListener(onClick);

                            }
                        };
                        addClassDialog.show();

                    }
                    itemNumber = Integer.parseInt(beHomeWorkList.getTotalRows());
                    if (beHomeWorkList != null && beHomeWorkList.getLists().size() > 0) {
                        mPageNum++;
                        mHomeWorklists.addAll(beHomeWorkList.getLists());
                    }
                    apHomework.notifyDataSetChanged();

                }
            } else {
                ToastUtil.showToast(getContext(), json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, isLastPage());

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK://该结果码与FragmentActivity中是保持一致的
                //在这里获取你需要的数据
                homeworkHttp();
                break;
        }
    }
}
