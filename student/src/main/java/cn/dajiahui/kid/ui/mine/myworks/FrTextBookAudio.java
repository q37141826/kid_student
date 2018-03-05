package cn.dajiahui.kid.ui.mine.myworks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.adapter.ApMyWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorksLists;
import cn.dajiahui.kid.ui.mine.myinterface.ShowbtnDelete;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;

/**
 * 课本剧碎片（我的作品）
 */
public class FrTextBookAudio extends FxFragment implements ShowbtnDelete {


    private ListView mListview;

    private TextView tvNUll;
    private List<BeMineWorksLists> mTextBooklists = new ArrayList<>();

    private MaterialRefreshLayout refresh;
    private ApMyWorks apMyWorks;
    private File[] mVideos;
    private RelativeLayout delete_view;
    private Button btn_delete;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_textbookaudio, null);
    }


    /*得到数据源*/
    private void getWorkSource() {
        File folder = new File(KidConfig.getInstance().getPathMineWorksTextBookDrama());

//        mVideos = folder.listFiles();

//        for (int i = 0; i < mVideos.length; i++) {
//            //获取当前文件名字
//            /*1參數我的作品本地路径*/
//            BeMyWorks beMyWorks = new BeMyWorks(mVideos[i].getName(), KidConfig.getInstance().getPathMineWorksTextBookDrama() + mVideos[i].getName(), "2018.2.21");
//            Logger.d(mVideos[i].getName());
//            Logger.d(KidConfig.getInstance().getPathMineWorksTextBookDrama() + mVideos[i].getName());
//            mTextBooklists.add(beMyWorks);
//        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListview = getView(R.id.listview);
        /*全选按钮*/
        delete_view = getView(R.id.delete_view);
        btn_delete = getView(R.id.btn_delete);

        tvNUll = getView(R.id.tv_null);
        tvNUll.setText("暂无课本剧");
        refresh = getView(R.id.refresh);


//        tvNUll.setOnClickListener(onClick);
        refresh.setHeadView(false);
        apMyWorks = new ApMyWorks(getActivity(), mTextBooklists);
        mListview.setAdapter(apMyWorks);

        getWorkSource();

        if (mTextBooklists.size() == 0) {
            mListview.setEmptyView(tvNUll);
        } else {
            apMyWorks.notifyDataSetChanged();
            Logger.d(mTextBooklists.toString());

        }

//
//        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Bundle bundle = new Bundle();
//
////                bundle.putSerializable("WORKSDATA", mTextBooklists.get(position));
//
//                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), VideoActivity.class, bundle, 0);
//
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();
        httpData();
    }

    /*获取列表*/
    @Override
    public void httpData() {
        super.httpData();
        RequestUtill.getInstance().httpGetMineWorksTextBookDrama(getActivity(), callMineWorksTextBook, 10, pagNum);

    }

    @Override
    public void showbtnDelete(int position) {
        if (position == 1) {
            delete_view.setVisibility(View.VISIBLE);
            apMyWorks.changeState(-1);
        } else {
            delete_view.setVisibility(View.GONE);
            apMyWorks.changeState(-2);
        }
        Toast.makeText(activity, "显示删除按钮课本剧", Toast.LENGTH_SHORT).show();
    }

    /*获取我的作品课本剧*/
    ResultCallback callMineWorksTextBook = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
            Logger.d("获取课本剧" + response);
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeMineWorks beMineWorks = json.parsingObject(BeMineWorks.class);
                if (beMineWorks != null) {
                    mTextBooklists.addAll(beMineWorks.getLists());
                    apMyWorks.notifyDataSetChanged();
                }

            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }


        }


    };
}
