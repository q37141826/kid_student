package cn.dajiahui.kid.ui.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import cn.dajiahui.kid.ui.study.adapter.ApTeachingMaterialIfo;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterialInfo;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterialInfoBook;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterialInfoLists;
import cn.dajiahui.kid.ui.study.bean.BeStudy;
import cn.dajiahui.kid.util.Logger;

import static cn.dajiahui.kid.controller.Constant.CHOICETEACHINGMATERIALRESULT;

/*
* 选择教材详情
* 选择教材二级界面
* */
public class ChoiceTeachingMaterialInfoActivity extends FxActivity {
    private ListView mListView;
    private BeChoiceTeachingMaterialInfo beChoiceTeachingMaterialInfo;
    private Bundle myBundelForGetName;
    public Assignment assignment;
    private MaterialRefreshLayout refresh;
    private String org_id;//机构ID
    private String series;//系列ID
    private int CHOICEBOOK_OK = 1;
    private List<BeChoiceTeachingMaterialInfoLists> mInfoListsList = new ArrayList<>();
    private ApTeachingMaterialIfo apTeachingMaterialList;//选择教材系列下列表的适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从Intent 中获取数据
        myBundelForGetName = this.getIntent().getExtras();
        setfxTtitle(myBundelForGetName.getString("UINT_TITLE"));
        org_id = myBundelForGetName.getString("ORG_ID");
        series = myBundelForGetName.getString("SERIES");
        onBackText();
        httpData();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choice_teaching_material_info);

        refresh = getView(R.id.refresh);
        mListView = getView(R.id.listview);
        initRefresh(refresh);

        apTeachingMaterialList = new ApTeachingMaterialIfo(ChoiceTeachingMaterialInfoActivity.this, mInfoListsList);
        mListView.setAdapter(apTeachingMaterialList);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private String book_id;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book_id = mInfoListsList.get(position).getId();


                RequestUtill.getInstance().ChoiceTeachingMaterialBook(context, callChoiceTeachingMaterialBook, org_id, book_id);
//                BeStudy beStudy = new BeStudy("", list.get(position).getTeachingMaterialInfoName(), myBundelForGetName.getString("unit"));
//                assignment.assignment(beStudy);

//                finishActivity();
                Toast.makeText(context, "开始学习", Toast.LENGTH_SHORT).show();

            }

        });
    }


    @Override
    public void httpData() {
        RequestUtill.getInstance().httpChoiceTeachingMaterialInfo(context, callChoiceTeachingMaterialList, org_id, series, mPageSize + "", mPageNum + "");

    }

    /**
     * 选择教材，开始学习/继续学习callback
     */
    ResultCallback callChoiceTeachingMaterialBook = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            finishRefreshAndLoadMoer(refresh, 0);
        }

        @Override
        public void onResponse(String response) {

            Logger.d("选择教材系列下的列表Book的返回数据：-------" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeChoiceTeachingMaterialInfoBook beChoiceTeachingMaterialInfoBook = json.parsingObject(BeChoiceTeachingMaterialInfoBook.class);

                setResult(CHOICETEACHINGMATERIALRESULT);
                finishActivity();
            } else {
                ToastUtil.showToast(context, json.getMsg());
            }

        }
    };
    /**
     * 选择教材系列下的列表callback
     */
    ResultCallback callChoiceTeachingMaterialList = new ResultCallback() {
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

                BeChoiceTeachingMaterialInfo beChoiceTeachingMaterialInfo = json.parsingObject(BeChoiceTeachingMaterialInfo.class);
                mInfoListsList.clear();
                mInfoListsList.addAll(beChoiceTeachingMaterialInfo.getLists());
                apTeachingMaterialList.notifyDataSetChanged();
            } else {
                ToastUtil.showToast(context, json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, mPageNum); // 要自己判断是否为最后一页
        }
    };


    /*给左自学左上角属性赋值*/
    public interface Assignment {
        public void assignment(BeStudy beStudy);
    }
}

