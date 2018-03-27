package cn.dajiahui.kid.ui.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.adapter.ApChooseUtils;
import cn.dajiahui.kid.ui.study.bean.BeStudy;
import cn.dajiahui.kid.ui.study.bean.ChooseUtils;
import cn.dajiahui.kid.ui.study.bean.ChooseUtilsLists;
import cn.dajiahui.kid.util.DjhJumpUtil;

import static cn.dajiahui.kid.controller.Constant.GOCHOICETEACHINGMATERIAL;

/**
 * 学习
 */
public class FrStudy extends FxFragment implements ChoiceTeachingMaterialInfoActivity.Assignment {

    private ViewPager pager;
    private ImageView imgsupplementary;
    private TextView tvtitle;
    private TextView tvunit;
    private RelativeLayout tvchoiceMaterial;
    private ListView mListview;
    private LinearLayout tabfragment;
    private TextView tvNUll;
    private MaterialRefreshLayout refresh;
    private List<ChooseUtilsLists> mChooseUtilsList = new ArrayList();
    private ApChooseUtils apChooseUtils;
    private String mBookId;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_study, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
        tvNUll.setText("暂无数据");
          /*模拟数据*/
        if (!isCreateView) {
            isCreateView = true;
            studyHttp();
        }

        apChooseUtils = new ApChooseUtils(getActivity(), mChooseUtilsList);
        mListview.setAdapter(apChooseUtils);


        //选择单元学习
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("UNIT_NAME", mChooseUtilsList.get(position).getName());
                bundle.putString("BOOK_ID", mBookId);
                bundle.putString("UNIT_ID", mChooseUtilsList.get(position).getId());
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), StudyDetailsActivity.class, bundle, 0);
            }
        });


    }

    private boolean isRefresh = false;
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_choiceMaterial:
                    Bundle bundle = new Bundle();
                    DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), ChoiceTeachingMaterialActivity.class, bundle, GOCHOICETEACHINGMATERIAL);
                    isRefresh = true;
                    break;
                case R.id.im_user:


                    break;
                case R.id.tv_null:
                    studyHttp();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            studyHttp();
            isRefresh = !isRefresh;
        }

    }

    /*网络请求*/
    private void studyHttp() {
        mPageNum = 1;
        showfxDialog();
        httpData();
    }

    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpStudyHomePage(getActivity(), callStudyHomePage); // 请求取得教辅列表

    }

    /*自学首页*/
    ResultCallback callStudyHomePage = new ResultCallback() {


        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            finishRefreshAndLoadMoer(refresh, 0);
        }

        @Override
        public void onResponse(String response) {
            Logger.d("自学首页：" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                ChooseUtils chooseUtils = json.parsingObject(ChooseUtils.class);
                if (chooseUtils != null) {
                    GlideUtil.showNoneImage(getActivity(), chooseUtils.getLogo(), imgsupplementary, R.drawable.study_default);
                    tvtitle.setText(chooseUtils.getSeries());
                    tvunit.setText(chooseUtils.getName());
                    mBookId = chooseUtils.getId();
                    mChooseUtilsList.clear();
                    mChooseUtilsList.addAll(chooseUtils.getLists());
                    apChooseUtils.notifyDataSetChanged();
                } else {
                    mChooseUtilsList.clear();
                    apChooseUtils.notifyDataSetChanged();
                    tvtitle.setText("");
                    tvunit.setText("");
                }
            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, mPageNum); // 要自己判断是否为最后一页
        }
    };

    /*初始化*/
    private void initialize() {
        imgsupplementary = getView(R.id.img_supplementary);
        tvtitle = getView(R.id.tv_title);
        tvunit = getView(R.id.tv_unit);
        tvchoiceMaterial = getView(R.id.tv_choiceMaterial);
        mListview = getView(R.id.listview);
        tabfragment = getView(R.id.tab_fragment);
        tvNUll = getView(R.id.tv_null);
        refresh = getView(R.id.refresh);
        tvNUll.setOnClickListener(onClick);
        mListview.setEmptyView(tvNUll);
        initRefresh(refresh);
        tvchoiceMaterial.setOnClickListener(onClick);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void assignment(BeStudy beStudy) {
        tvtitle.setText(beStudy.getTv_title());
        tvunit.setText(beStudy.getTv_unit());
    }
}
