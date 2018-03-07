package cn.dajiahui.kid.ui.mine.myworks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.adapter.ViewHolder;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.adapter.ApMyWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorksLists;
import cn.dajiahui.kid.ui.mine.myinterface.ShowbtnDelete;
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
    private RelativeLayout delete_view;
    private Button btn_delete;
    private CheckBox allCheck;

    private List<String> mIdList = new ArrayList<>();//删除作品Id的集合

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_textbookaudio, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListview = getView(R.id.listview);
        /*全选按钮*/
        delete_view = getView(R.id.delete_view);
        btn_delete = getView(R.id.btn_delete);
        allCheck = getView(R.id.allCheck);

        tvNUll = getView(R.id.tv_null);
        tvNUll.setText("暂无课本剧");
        refresh = getView(R.id.refresh);
        initRefresh(refresh);
        mListview.setEmptyView(tvNUll);
        tvNUll.setOnClickListener(onClick);
        apMyWorks = new ApMyWorks(getActivity(), mTextBooklists);
        mListview.setAdapter(apMyWorks);

        /*全选*/
        allCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCheck.setChecked(true);
                btn_delete.setBackgroundResource(R.color.red);
                for (int i = 0; i < mTextBooklists.size(); i++) {
                    if (!mTextBooklists.get(i).getBo()) {
                        // 改变boolean
                        mTextBooklists.get(i).setBo(true);
                        if (!mIdList.contains(mTextBooklists.get(i).getId())) {
                            mIdList.add(mTextBooklists.get(i).getId());
                        }
                    }
                }
                // 刷新
                apMyWorks.notifyDataSetChanged();

            }
        });


        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (delete_view.getVisibility() == View.VISIBLE) {


                    // 取得ViewHolder对象
                    ViewHolder viewHolder = (ViewHolder) view.getTag();
                    CheckBox checkBox = viewHolder.getView(R.id.checkbox);
                    // 改变CheckBox的状态
                    checkBox.toggle();
                    // 将CheckBox的选中状况记录下来
                    mTextBooklists.get(position).setBo(checkBox.isChecked());

                    // 调整选定条目
                    if (checkBox.isChecked() == true) {
                        mIdList.add(mTextBooklists.get(position).getId());
                     /*改变底部按钮为全选*/
                        if (mIdList.size() == mTextBooklists.size()) {
                            allCheck.setChecked(true);
                        }
                    } else {
                        mIdList.remove(mTextBooklists.get(position).getId());
                        allCheck.setChecked(false);
                    }
                /*设置删除按钮颜色*/
                    if (mIdList.size() == 0) {
                        btn_delete.setBackgroundResource(R.color.gray);
                    } else {
                        btn_delete.setBackgroundResource(R.color.red);
                    }
                } else {

                    Toast.makeText(getActivity(), "播放视频", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
//
////                bundle.putSerializable("WORKSDATA", mTextBooklists.get(position));
//
//                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), VideoActivity.class, bundle, 0);

                }
            }
        });

        /*执行删除*/
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIdList.size() > 0) {
                    Logger.d("mIdList---" + mIdList.toString());
                 /*删除操作后隐藏  checkbox*/
                    delete_view.setVisibility(View.GONE);
                    apMyWorks.changeState(-2);
                    MyWorksActivity activity = (MyWorksActivity) getActivity();
                    activity.setShowcheckboxTextbook(false);
                }
            }
        });

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
        RequestUtill.getInstance().httpGetMineWorksTextBookDrama(getActivity(), callMineWorksTextBook, mPageSize, mPageNum);

    }

    @Override
    public void showbtnDelete(int position) {
        if (position == 1) {
            delete_view.setVisibility(View.VISIBLE);
           /*清空集合*/
            mIdList.clear();
            /*刷新整个Addapter*/
            for (int i = 0; i < mTextBooklists.size(); i++) {
                // 改变boolean
                mTextBooklists.get(i).setBo(false);
                if (mIdList.contains(mTextBooklists.get(i).getId())) {
                    mIdList.remove(mTextBooklists.get(i).getId());
                }
            }
            allCheck.setChecked(false);
            apMyWorks.changeState(-1);
               /*设置删除按钮颜色*/
            btn_delete.setBackgroundResource(R.color.gray);
        } else {
            delete_view.setVisibility(View.GONE);
            apMyWorks.changeState(-2);
        }
//        Toast.makeText(activity, "显示删除按钮课本剧", Toast.LENGTH_SHORT).show();
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
//            Logger.d("获取课本剧" + response);
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                if (mPageNum == 1) {
                    mTextBooklists.clear();
                }
                BeMineWorks beMineWorks = json.parsingObject(BeMineWorks.class);
                itemNumber = Integer.parseInt(beMineWorks.getTotalRows());
                if (beMineWorks != null && beMineWorks.getLists().size() > 0) {
                    mPageNum++;
                    mTextBooklists.addAll(beMineWorks.getLists());
                }
                apMyWorks.notifyDataSetChanged();
            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }
            finishRefreshAndLoadMoer(refresh, isLastPage());
        }
    };


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
