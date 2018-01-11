package cn.dajiahui.kid.ui.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.album.adapter.ApPhoto;
import cn.dajiahui.kid.ui.album.bean.BePhoto;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by z on 2016/3/10.
 * 图片列表界面
 */
public class PhotoActivity extends FxActivity {
    private GridView gridview;
    private MaterialRefreshLayout refresh;
    private TextView tvNUll;
    private ArrayList<BePhoto> photos = new ArrayList<BePhoto>();//相册列表对象
    private ApPhoto adapter;
    private String albumId;//相册id
    private String albumTitle;//相册标题

    @Override
    protected void initView() {
        setContentView(R.layout.activity_photo_list);
        refresh = getView(R.id.refresh);
        tvNUll = getView(R.id.tv_null);
        gridview = getView(R.id.gridview);
        initRefresh(refresh);
        adapter = new ApPhoto(context, photos);
        gridview.setAdapter(adapter);
        gridview.setEmptyView(tvNUll);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DjhJumpUtil.getInstance().startPhotoPageActivity(context, (ArrayList<BePhoto>) photos, position, true);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DjhJumpUtil.getInstance().activity_PhotoPage) {
                mPageNum = 1;
                showfxDialog();
                httpData();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumId = getIntent().getStringExtra(Constant.bundle_id);
        albumTitle = getIntent().getStringExtra(Constant.bundle_title);
        setfxTtitle(albumTitle);
        onBackText();
        showfxDialog();
        httpData();
    }

    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNUll.setText(R.string.e_photo_null);
        tvNUll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageNum = 1;
                showfxDialog();
                httpData();
            }
        });
    }

    @Override
    public void httpData() {
        RequestUtill.getInstance().httpPictureList(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                finishRefreshAndLoadMoer(refresh, 0);
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 1) {
                    if (mPageNum == 1) {
                        photos.clear();
                    }
                    List<BePhoto> temp = json.parsingListArray(new GsonType<List<BePhoto>>() {
                    });
                    if (temp != null && temp.size() > 0) {
                        mPageNum++;
                        photos.addAll(temp);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
//                finishRefreshAndLoadMoer(refresh, json.getstatus());
            }
        }, albumId, UserController.getInstance().getUserId(), mPageNum, "30");
    }

}
