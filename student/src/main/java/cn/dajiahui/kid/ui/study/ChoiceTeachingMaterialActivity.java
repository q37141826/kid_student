package cn.dajiahui.kid.ui.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.dajiahui.kid.ui.homework.bean.BeChoiceTeachingMaterialLists;
import cn.dajiahui.kid.ui.study.adapter.ApTeachingMaterial;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterial;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;

import static cn.dajiahui.kid.controller.Constant.CHOICETEACHINGMATERIAL;
import static cn.dajiahui.kid.controller.Constant.CHOICETEACHINGMATERIALRESULT;

/*
* 教材选择
* */
public class ChoiceTeachingMaterialActivity extends FxActivity {

    private ListView mListView;
    private List<BeChoiceTeachingMaterialLists> bookInfoList = new ArrayList<>();
    public int mPageSize = 10; //默认一页10个条目
    private MaterialRefreshLayout refresh;
    private int itemNumber = 0; // 总的数据数
    private ApTeachingMaterial apTeachingMaterial;//选择额教材适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.tab_study);
        onBackText();


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choice_teaching_material);

        mListView = getView(R.id.listview);
        TextView tvNUll = getView(R.id.tv_null);
        tvNUll.setText("暂无作业");
        refresh = getView(R.id.refresh);
        tvNUll.setOnClickListener(onClick);
        mListView.setEmptyView(tvNUll);
        initRefresh(refresh);

        if (!isCreateView) {
            isCreateView = true;
            ChoiceTeachingMaterialHttp();
        }


        apTeachingMaterial = new ApTeachingMaterial(ChoiceTeachingMaterialActivity.this, bookInfoList);

        mListView.setAdapter(apTeachingMaterial);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putString("UINT_TITLE", bookInfoList.get(position).getName());
                b.putString("ORG_ID", bookInfoList.get(position).getOrg_id());
                b.putString("SERIES", bookInfoList.get(position).getSeries());

                DjhJumpUtil.getInstance().startBaseActivityForResult(ChoiceTeachingMaterialActivity.this, ChoiceTeachingMaterialInfoActivity.class, b, CHOICETEACHINGMATERIAL);


            }


        });
    }


    /*网络请求*/
    private void ChoiceTeachingMaterialHttp() {
        mPageNum = 1;
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        RequestUtill.getInstance().httpChoiceTeachingMaterial(context, callGetWorkBookList, mPageSize, mPageNum); // 请求取得教辅列表
    }

    /**
     * 取得教辅列表的callback
     */
    ResultCallback callGetWorkBookList = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            finishRefreshAndLoadMoer(refresh, 0);
        }

        @Override
        public void onResponse(String response) {

            Logger.d("选择教材的返回数据：-------" + response);

            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                    /* 解析班级列表信息 */
                if (mPageNum == 1) {
                    bookInfoList.clear();
                }
                BeChoiceTeachingMaterial temp = json.parsingObject(BeChoiceTeachingMaterial.class);

                if (temp != null && temp.getLists().size() > 0) {
                    mPageNum++;
                    bookInfoList.addAll(temp.getLists());
                }

                apTeachingMaterial.notifyDataSetChanged();

            } else {
                ToastUtil.showToast(context, json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, isLastPage()); // 要自己判断是否为最后一页
        }
    };


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    /**
     * 判断是否为最后一页
     *
     * @return 0 不是最后一页 1 是最后一页
     */
    private int isLastPage() {
        int result = 0;

        if ((mPageNum - 1) * mPageSize >= itemNumber) {
            result = 1;
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOICETEACHINGMATERIAL && resultCode == CHOICETEACHINGMATERIALRESULT) {
            finishActivity();
        }

    }
}
