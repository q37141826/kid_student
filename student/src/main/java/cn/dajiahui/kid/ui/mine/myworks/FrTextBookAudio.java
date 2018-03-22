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

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.mine.adapter.ApMyWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMineWorksLists;
import cn.dajiahui.kid.ui.mine.myinterface.ShowbtnDelete;
import cn.dajiahui.kid.ui.study.bean.BePageDataMyWork;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.TextBookSuccessActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

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
    private CheckBox allCheck;//全选按钮

    private List<String> mIdList = new ArrayList<>();//删除作品Id的集合

    private int mCheckNum = 0;//删除选择的数量

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
//        tvNUll.setOnClickListener(onClick);
        apMyWorks = new ApMyWorks(getActivity(), mTextBooklists);
        mListview.setAdapter(apMyWorks);

        /*全选*/
        allCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allCheck.isChecked() == false) {
                    allCheck.setChecked(false);
                    btn_delete.setBackgroundResource(R.color.gray);

                    for (int i = 0; i < mTextBooklists.size(); i++) {
                        mTextBooklists.get(i).setBo(false);
                    }

                } else {
                    allCheck.setChecked(true);
                    btn_delete.setBackgroundResource(R.color.red);

                    for (int i = 0; i < mTextBooklists.size(); i++) {
                        mTextBooklists.get(i).setBo(true);
                    }
                }
                // 刷新
                apMyWorks.notifyDataSetChanged();
            }
        });

        /*单选*/
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (delete_view.getVisibility() == View.VISIBLE) {

                    if (mTextBooklists.get(position).getBo() == true) {
                        mTextBooklists.get(position).setBo(false);
                    } else {
                        mTextBooklists.get(position).setBo(true);
                    }

//                    Iterator it = mTextBooklists.iterator();
//
//                    while (it.hasNext()) {
//                        // 得到对应集合元素
//                        BeMineWorksLists g = (BeMineWorksLists) it.next();
//                        // 判断
//                        if (g.getBo()) {
//                            allCheck.setChecked(false);
//                        } else {
////                            mCheckNum++;
//                        }
//
//                    }
                    apMyWorks.notifyDataSetChanged();
//                    Logger.d("mCheckNum:" + mCheckNum + "   mTextBooklists.size():" + mTextBooklists.size());
//
//                    if (mCheckNum == mTextBooklists.size()) {
//                        btn_delete.setBackgroundResource(R.color.red);
//                        allCheck.setChecked(true);
//                    } else {
//                        btn_delete.setBackgroundResource(R.color.gray);
//                        allCheck.setChecked(false);
//                    }
                } else {
                    getTexBookDetails(mTextBooklists.get(position).getId());
                }

            }

        });

        /*执行删除*/
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIdList.clear();
                /*遍历 模型集合 找出true 就是选中的    false 就是未选中的*/
                for (int i = 0; i < mTextBooklists.size(); i++) {
                    if (mTextBooklists.get(i).getBo() == true) {
                        mIdList.add(mTextBooklists.get(i).getId());
                    }
                }
//                Logger.d("删除学生ID：" + mIdList.toString());
                if (mIdList.size() > 0) {
                    FxDialog dialog = new FxDialog(getActivity()) {
                        @Override
                        public void onRightBtn(int flag) {

                            Iterator it = mTextBooklists.iterator();
                            while (it.hasNext()) {
                                // 得到对应集合元素
                                BeMineWorksLists g = (BeMineWorksLists) it.next();
                                // 判断
                                if (g.getBo()) {
                                    // 从集合中删除上一次next方法返回的元素
                                    it.remove();
                                }
                            }

                            showbtnDelete(2);

                            deleteMyworks();
                            mIdList.clear();
                            // 刷新
                            apMyWorks.notifyDataSetChanged();
                        }

                        @Override
                        public void onLeftBtn(int flag) {


                        }
                    };
                    dialog.setTitle(R.string.prompt);
                    dialog.setMessage(R.string.prompt_delete);
                    dialog.show();

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

    /*删除我的作品*/
    private void deleteMyworks() {
        RequestUtill.getInstance().httpDeleteMineWorks(getActivity(), callDeleteTextBook, mIdList);
    }

    /*获取作详情*/
    private void getTexBookDetails(String work_id) {
        showfxDialog();
        RequestUtill.getInstance().httpGetTextBookDetails(getActivity(), callTextBookDetails, work_id);
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
            Logger.d("获取课本剧" + response);
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

    /*删除我的作品*/
    ResultCallback callDeleteTextBook = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
//            Logger.d("获取课本剧删除OK" + response);
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {


            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }

        }
    };

    /*获取作品详情*/
    ResultCallback callTextBookDetails = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
//            Logger.d("获取作品详情" + response);
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BePageDataMyWork bePageDataMyWork = json.parsingObject(BePageDataMyWork.class);
                if (bePageDataMyWork != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("MakeFlag", "LookTextBookDrma");
                    bundle.putSerializable("BePageDataMyWork", bePageDataMyWork);
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), TextBookSuccessActivity.class, bundle, 0);
                }
            } else {
                ToastUtil.showToast(getActivity(), json.getMsg());
            }

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
