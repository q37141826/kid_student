package cn.dajiahui.kid.ui.homework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.adapter.ApHomework;
import cn.dajiahui.kid.ui.homework.bean.BeHomeWorkList;
import cn.dajiahui.kid.ui.homework.homeworkdetails.HomeWorkDetailsActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * 作业
 */
public class FrHomework extends FxFragment {


    private ListView mListview;
    private BeHomeWorkList beHomeWorkList;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_homework, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = getView(R.id.tool_title);
        mListview = getView(R.id.mlistview);
        title.setText(R.string.tab_homework);
        httpData();


        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("homework_id", beHomeWorkList.getLists().get(position).getId());
                bundle.putString("starttime", beHomeWorkList.getLists().get(position).getStart_time());
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), HomeWorkDetailsActivity.class, bundle, 0);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void httpData() {
        super.httpData();
        showfxDialog();
        RequestUtill.getInstance().httpGetStudentHomeWork(getActivity(), callHomeWork, "100", "1"); // 根据班级码查询班级

    }

    /**
     * 学生作业列表callback函数
     */
    ResultCallback callHomeWork = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                beHomeWorkList = json.parsingObject(BeHomeWorkList.class);
                ApHomework apHomework = new ApHomework(getActivity(), beHomeWorkList.getLists());
                mListview.setAdapter(apHomework);
            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }
        }
    };
}
