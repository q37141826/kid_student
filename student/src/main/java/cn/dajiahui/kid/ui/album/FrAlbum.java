package cn.dajiahui.kid.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.album.adapter.ApAlbum;
import cn.dajiahui.kid.ui.album.bean.BeAlbum;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by z on 2016/3/7.
 * 相册信息界面
 */
public class FrAlbum extends FxFragment {
    private GridView gridview;
    private MaterialRefreshLayout refresh;
    private TextView tvNUll;
    private List<BeAlbum> ablums = new ArrayList<BeAlbum>();
    private ApAlbum adapter;
    private String classId;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_class_photo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classId = bundle.getString(Constant.bundle_id);
        refresh = getView(R.id.refresh);
        tvNUll = getView(R.id.tv_null);
        gridview = getView(R.id.gridview);
        initRefresh(refresh);
        adapter = new ApAlbum(getContext(), ablums);
        gridview.setAdapter(adapter);
        gridview.setEmptyView(tvNUll);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeAlbum album = adapter.getItem(position);
                DjhJumpUtil.getInstance().startPhotoActivity(getContext(), album.getObjectId(), album.getName());
            }
        });
        showfxDialog();
        httpData();
    }


    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNUll.setText(R.string.e_album_null);
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
//        RequestUtill.getInstance().httpMyClassAlbumList(getContext(), new ResultCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//                dismissfxDialog();
//                finishRefreshAndLoadMoer(refresh, 0);
//                ToastUtil.showToast(getContext(), ErrorCode.error(e));
//            }
//
//            @Override
//            public void onResponse(String response) {
//                dismissfxDialog();
//                HeadJson json = new HeadJson(response);
//                if (json.getstatus()  == 1) {
//                    if (pagNum == 1) {
//                        ablums.clear();
//                    }
//                    List<BeAlbum> temp = json.parsingListArray(new GsonType<List<BeAlbum>>() {
//                    });
//                    if (temp != null && temp.size() > 0) {
//                        pagNum++;
//                        ablums.addAll(temp);
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    ToastUtil.showToast(getContext(), json.getMsg());
//                }
//                finishRefreshAndLoadMoer(refresh, 1);
//            }
//        }, classId, pagNum, "30");
    }
}
