package cn.dajiahui.kid.ui.album;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.album.adapter.ApAblumListview;
import cn.dajiahui.kid.ui.album.adapter.ApAlbumTitle;
import cn.dajiahui.kid.ui.album.bean.BeClassAlbum;

/**
 * Created by z on 2016/3/10.
 * 班级相册列表
 */
public class AlbumActivity extends FxActivity {
    private List<BeClassAlbum> albumList = new ArrayList<BeClassAlbum>();
    private MaterialRefreshLayout refresh;
    private TextView tvNUll;
    public ArrayList<ApAlbumTitle> ablunList = new ArrayList<>();
    private RecyclerView recyclerView;
    //    private ApAlbumRecyclerView adapter;
    private ApAblumListview adapter;
    private ListView listView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        setfxTtitle(R.string.class_album);
        showfxDialog();
        httpData();
    }
    
    @Override
    protected void initView() {
        setContentView(R.layout.activity_album);
        refresh = getView(R.id.refresh);
        tvNUll = getView(R.id.tv_null);
        tvNUll.setVisibility(View.GONE);
        recyclerView = getView(R.id.rv_recyclerview);
        listView = getView(R.id.listview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new ApAlbumRecyclerView(this, albumList);
//        recyclerView.setAdapter(adapter);
        adapter = new ApAblumListview(context, albumList);
        listView.setAdapter(adapter);
        initRefresh(refresh);
    }
    
    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNUll.setText(R.string.e_album_null);
        if (albumList.size() == 0)
            tvNUll.setVisibility(View.VISIBLE);
        tvNUll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfxDialog();
                httpData();
            }
        });
    }
    
    @Override
    public void httpData() {
        RequestUtill.getInstance().httpClassAlbumList(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                finishRefreshAndLoadMoer(refresh, 0);
                ToastUtil.showToast(context, ErrorCode.error(e));
            }
            
            @Override
            public void onResponse(String response) {
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    List<BeClassAlbum> temp = json.parsingListArray(new GsonType<List<BeClassAlbum>>() {
                    });
                    if (temp != null && temp.size() > 0) {
                        albumList.clear();
                        ablunList.clear();
                        albumList.addAll(temp);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
                dismissfxDialog();
                finishRefreshAndLoadMoer(refresh, json.getIsLastPage());
            }
        }, UserController.getInstance().getUserId());
    }
}
