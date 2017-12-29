package cn.dajiahui.kid.ui.homework;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.homework.adapter.ApUndoneList;
import cn.dajiahui.kid.ui.homework.bean.BeCommission;

/**
 * Created by wdj on 2016/6/21.
 * 通知列表页面
 */
public class TypeCommissionActivity extends FxActivity {
    private ArrayList<BeCommission> datas = new ArrayList<>();
    private ListView listView;
    private TextView tv_Null;
    private ApUndoneList adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_type_commission);
        listView = getView(R.id.listview);
        tv_Null = getView(R.id.tv_null);
        listView.setEmptyView(tv_Null);
        listView.setOnItemClickListener(onItemClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datas = (ArrayList<BeCommission>) getIntent().getSerializableExtra(Constant.bundle_obj);
        adapter = new ApUndoneList(this, datas);
        listView.setAdapter(adapter);
        onBackText();
        setfxTtitle(getIntent().getStringExtra(Constant.bundle_type));
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BeCommission beCommission = datas.get(position);
            switch (beCommission.getType()) {
                case Constant.type_tz:
                    break;
                case Constant.type_zybz:
                case Constant.type_cp:

                    break;
                case Constant.type_pjjs:

                    break;
                default:
                    ToastUtil.showToast(context, "无效待办");
                    break;
            }
        }
    };



}
