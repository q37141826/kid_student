package cn.dajiahui.kid.ui.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.BaseUtil;
import com.squareup.okhttp.Request;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.http.ShareHttp;
import cn.dajiahui.kid.ui.album.adapter.ApPhotoEval;
import cn.dajiahui.kid.ui.album.bean.BePhotoDetails;
import cn.dajiahui.kid.ui.album.bean.BePhotoEval;
import cn.dajiahui.kid.ui.album.bean.BePhotoEvalItem;


/**
 * Created by z on 2016/3/10.
 * 照片详情界面
 */
public class PhotoDetailsActivity extends FxActivity {
    private ImageView imUser;
    private TextView tvTitle, tvMsg, tvEval;
    private ImageView photo;
    private ExpandableListView listView;
    private String photoId;
    private List<BePhotoEval> evalList = new ArrayList<BePhotoEval>();
    private ApPhotoEval adapter;
    private String parentId;
    private String replyUserId;
    private int group = -1;
    private int num = 0;

    private String picUrl;
    private TextView tvNull;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_photo_details);
        View viewHead = getLayoutInflater().inflate(R.layout.photo_heade, null);

        viewHead.setOnClickListener(onClick);
        imUser = (ImageView) viewHead.findViewById(R.id.im_user);
        tvTitle = (TextView) viewHead.findViewById(R.id.tvTitle);
        tvNull = (TextView) viewHead.findViewById(R.id.list_dataNoTv);
        tvMsg = (TextView) viewHead.findViewById(R.id.tvMsg);
        photo = (ImageView) viewHead.findViewById(R.id.imPhoto);
        tvEval = (TextView) viewHead.findViewById(R.id.tvEval);
        adapter = new ApPhotoEval(evalList, context);
        listView = getView(R.id.listview);
        listView.addHeaderView(viewHead);
        //点击子类
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                BePhotoEvalItem item = evalList.get(groupPosition).getList().get(childPosition);//回复人
                //回复他
                group = groupPosition;
                sendData(item.getParentId(), item.getUserId(), item.getUserName());
                return true;
            }
        });
        //点击父类
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //回复他
                group = groupPosition;
                BePhotoEval item = evalList.get(groupPosition);
                sendData(item.getObjectId(), item.getUserId(), item.getUserName());
                return true;
            }
        });
        listView.setAdapter(adapter);
    }


    private void sendData(String parentId, String replyUserId, String replyName) {
        this.parentId = parentId;
        this.replyUserId = replyUserId;
        if (replyName == null) {
            group = -1;

        } else {

        }
    }

    /**
     * 展开数组
     */
    private void expandGroup() {
        // 展开所有
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendData("0", null, null);
        }
    };

    private void setViewTitile(BePhotoDetails details) {
        if (details == null)
            return;
        picUrl = details.getPicUrl();
        tvTitle.setText(details.getUserName());
        tvMsg.setText(details.getUserName() + " " + TimeUtil.timeFormat(details.getAddTime(), TimeUtil.yyMD));
        num = details.getCommentCount();
        tvEval.setText(getString(R.string.text_pingjia, details.getCommentCount()));
        if (details.getCommentCount() == 0) {
            tvNull.setVisibility(View.VISIBLE);
        }
        GlideUtil.showRoundImage(context, details.getAvator(), imUser, R.drawable.ico_default_user, true);
        GlideUtil.showNoneImage(context, details.getThumbUrl(), photo, R.drawable.ico_default, false);
        evalList.clear();
        evalList.addAll(details.getList());
        adapter.notifyDataSetChanged();
        expandGroup();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoId = getIntent().getStringExtra(Constant.bundle_id);
        onBackText();
        setfxTtitle(R.string.comments);
        onRightBtn(R.drawable.ico_share, R.string.shart);
        showfxDialog();
        httpData();
    }

    @Override
    public void onRightBtnClick(View view) {
        super.onRightBtnClick(view);
        //分享
        new ShareHttp(this, photoId).startShare();
    }

    @Override
    public void httpData() {
        RequestUtill.getInstance().httpPictureDetails(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {
                    BePhotoDetails details = json.parsingObject(BePhotoDetails.class);
                    setViewTitile(details);
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
            }
        }, UserController.getInstance().getUserId(), photoId);
    }



    //发表评论

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
