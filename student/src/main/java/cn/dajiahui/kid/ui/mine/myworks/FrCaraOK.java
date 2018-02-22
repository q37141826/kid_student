package cn.dajiahui.kid.ui.mine.myworks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApHomework;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;
import cn.dajiahui.kid.ui.homework.homeworkdetails.HomeWorkDetailsActivity;
import cn.dajiahui.kid.ui.mine.myinterface.ShowbtnDelete;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * 卡拉OK碎片（我的作品）
 */
public class FrCaraOK extends FxFragment implements ShowbtnDelete {


    private ListView mListview;

    private TextView tvNUll;
    private ApHomework apHomework;//作业列表适配器

    private List<BeHomework> lists = new ArrayList<BeHomework>();
    private MaterialRefreshLayout refresh;
    private RelativeLayout delete_view;
    private Button btn_delete;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_caraok, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

              /*全选按钮*/
        delete_view = getView(R.id.delete_view);
        btn_delete = getView(R.id.btn_delete);
        mListview = getView(R.id.listview);

        tvNUll = getView(R.id.tv_null);
        tvNUll.setText("暂无作品");
        refresh = getView(R.id.refresh);
        tvNUll.setOnClickListener(onClick);
        mListview.setEmptyView(tvNUll);
        refresh.setHeadView(false);

        apHomework = new ApHomework(getActivity(), lists);//
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
                bundle.putString("homework_id", lists.get(position).getId());
                bundle.putString("starttime", lists.get(position).getStart_time());
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), HomeWorkDetailsActivity.class, bundle, 0);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void showbtnDelete(int position) {
        if (position == 1) {
            delete_view.setVisibility(View.VISIBLE);
            Toast.makeText(activity, "显示删除按钮卡拉OK", Toast.LENGTH_SHORT).show();
        } else {
            delete_view.setVisibility(View.GONE);
        }

    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_null:
//                    homeworkHttp();
                    break;
            }
        }
    };

}
