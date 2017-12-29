package cn.dajiahui.kid.ui.task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.image.util.ImageUtil;
import com.fxtx.framework.json.GsonType;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.BottomDialog;
import com.fxtx.framework.widgets.refresh.MaterialRefreshLayout;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.chat.constant.PreferenceManager;
import cn.dajiahui.kid.ui.task.adapter.ApClasssify;
import cn.dajiahui.kid.ui.task.adapter.ApUserIndex;
import cn.dajiahui.kid.ui.task.bean.BeMessage;
import cn.dajiahui.kid.ui.task.bean.BeMessageType;
import cn.dajiahui.kid.ui.mine.UserDetailsActivity;

import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * 用户日志界面
 */
public class UserIndexActivity extends FxActivity {
    private ImageView img_user_icon;
    private TextView tv_name, tv_sign;
    private ListView listview;
    private ApUserIndex adapter;//消息适配器
    private MaterialRefreshLayout refreshLayout;
    private TextView tv_choose, tvNull;
    private ApClasssify adapter_apinner;//消息类型适配器
    private PopupWindow popupWindow;
    private int position = 0;
    private BottomDialog dialog;
    private List<BeMessageType> lists = new ArrayList<BeMessageType>();//消息类型
    private List<BeMessage> datas = new ArrayList<BeMessage>();
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBackText();
        toolbar.setBackgroundResource(R.color.transparent);
        httpMessageType();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_index);
        refreshLayout = getView(R.id.refresh_index_list);
        img_user_icon = getView(R.id.im_user_index);
        img_user_icon.setOnClickListener(onclick);
        tv_name = getView(R.id.tv_user_name);
        tv_sign = getView(R.id.tv_user_talk);
        tvNull = getView(R.id.tv_null);
        tv_sign.setText(UserController.getInstance().getUser().getSignature());
        tv_name.setOnClickListener(onclick);
        tv_choose = getView(R.id.tv_user_index_choose);
        tv_choose.setOnClickListener(onclick);
        int b = tv_choose.getWidth();
        initRefresh(refreshLayout);
        listview = getView(R.id.lv_user_index);
        listview.setEmptyView(tvNull);
        ininData();
        adapter = new ApUserIndex(this, datas);
        listview.setAdapter(adapter);
        dialog = new BottomDialog(context, R.layout.dialog_choosepic) {
            @Override
            public void initView() {
                rootView.findViewById(R.id.btn_cancle).setOnClickListener(onclick);
                rootView.findViewById(R.id.btn_takephoto).setOnClickListener(onclick);
                rootView.findViewById(R.id.btn_choose).setOnClickListener(onclick);
            }
        };
        listview.setOnItemClickListener(onItemClick);
    }

    private void ininData() {
        GlideUtil.showRoundImage(this, UserController.getInstance().getUser().getAvator(), img_user_icon, R.drawable.ico_default_user, true);
        if (StringUtil.isSex(UserController.getInstance().getUser().getSex())) {
            tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ico_boy, 0);
        } else {
            tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ico_girl, 0);
        }
        tv_name.setText(UserController.getInstance().getUser().getRealName());
        //spinner适配数据
        adapter_apinner = new ApClasssify(context, lists);
        initPopup();
    }

    private String path;
    private String imagename;
    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.im_user_index:
                    //点击用户头像
                    if (dialog != null)
                        dialog.show();
                    break;
                case R.id.tv_user_index_choose:
                    //点击选项列表
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        return;
                    }
                    if (popupWindow == null) {
                        initPopup();
                    }
                    popupWindow.showAsDropDown(tv_choose, 20, -10);
                    if (adapter_apinner != null) {
                        adapter_apinner.reFreshItem(getMyPosition((String) tv_choose.getTag()));
                    }
                    break;
                case R.id.btn_cancle:
                    //取消
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    break;
                case R.id.btn_takephoto:
                    //拍照
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    path = UserController.getInstance().getUserImageAnswer(UserIndexActivity.this);//拍照存放路径
                    imagename = getImgName();
                    ImageUtil.takePhone(UserIndexActivity.this, path, imagename, UserDetailsActivity.TAKE_CAMERA_PICTURE);
                    break;
                case R.id.btn_choose:
                    //相册
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    DjhJumpUtil.getInstance().startSelectPhotoActivity(context, Constant.Alum_phone_UserIcon);
                    break;
                default:
                    break;
            }
        }
    };

    private String getImgName() {
        String imageName = "";
        imageName = System.currentTimeMillis() + ".jpg";
        return imageName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DjhJumpUtil.getInstance().activtiy_SelectPhoto) {
                //上传选择的照片
                ArrayList<String> strings = data.getStringArrayListExtra(Constant.bundle_obj);
                File file = new File(strings.get(0));
                httpUserIcon(file);
            }
            if (requestCode == UserDetailsActivity.TAKE_CAMERA_PICTURE) {
                Bitmap map = ImageUtil.uriToBitmap(Uri.fromFile(new File(path + imagename)), context);
                int degree = ImageUtil.readPictureDegree(path + imagename);
                if (degree != 0) {
                    map = ImageUtil.rotaingImageView(degree, map);
                }
                ImageUtil.bitmapToFile(map, path + imagename, 4096);
                File file = new File(path + imagename);
                httpUserIcon(file);
            }
        }
    }

    public void httpUserIcon(File file) {
        showfxDialog(R.string.submiting);
        RequestUtill.getInstance().uploadUserIcon(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson headJson = new HeadJson(response);
                if (headJson.getFlag() == 1) {
                    UserController.getInstance().getUser().setAvator(headJson.parsingString("avator"));
                    PreferenceManager.getInstance().setCurrentUserAvatar(UserController.getInstance().getUser().getAvator());
                    GlideUtil.showRoundImage(UserIndexActivity.this, UserController.getInstance().getUser().getAvator(), img_user_icon, R.drawable.ico_default_user, true);
                    ToastUtil.showToast(context, R.string.save_ok);
                    setResult(UserDetailsActivity.PICSETSULT);
                } else {
                    ToastUtil.showToast(context, headJson.getMsg());
                }
            }
        }, file, UserController.getInstance().getUserId());
    }

    private int getMyPosition(String s) {
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                if (StringUtil.isEmpty(s))
                    return 0;
                else {
                    if (StringUtil.sameStr(s, lists.get(i).getCode()))
                        return i;
                }
            }
        }
        return 0;
    }

    private void initPopup() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        View contentView = View.inflate(UserIndexActivity.this, R.layout.view_popup_layout, null);
        popupWindow = new PopupWindow(contentView, metrics.widthPixels * 3 / 10, metrics.heightPixels / 2);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setFocusable(true);
        ListView listView = (ListView) contentView.findViewById(R.id.listview);
        listView.setAdapter(adapter_apinner);
        adapter_apinner.reFreshItem(position);
        listView.setOnItemClickListener(onitemclick);
    }

    private AdapterView.OnItemClickListener onitemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPageNum = 1;
            tv_choose.setText(lists.get(position).getDesc());
            tv_choose.setTag(lists.get(position).getCode());
            if (popupWindow != null) popupWindow.dismiss();
            type = lists.get(position).getCode();
            showfxDialog();
            httpData();
        }
    };

    /**
     * 通知列表点击
     */
    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UserIndexActivity.this.position = position;
            BeMessage message = datas.get(position);
            if ("0".equals(message.getIsRead()) || null == message.getIsRead())
                httpreadmsg(message.getObjectId(), position);
            switch (message.getType()) {
                case Constant.type_bjsq:
                case Constant.type_xygl:
                    break;
                case Constant.type_tz:
                case Constant.type_tzhf:
                case Constant.type_tzpl:
                    // 通知 通知评论 通知回复
                    break;
                case Constant.type_xc:
                case Constant.type_xcpl:
                case Constant.type_xchf:
                    // 相册 相册评论 相册回复
                    DjhJumpUtil.getInstance().startPhotoDetails(context, message.getForeignId() + "");
                    break;
                case Constant.type_zybz:
                case Constant.type_cp:
                case Constant.type_cptj:
                case Constant.type_zytj:
                case Constant.type_cjzy:
                case Constant.type_zypz:
                case Constant.type_cppz:
                    httpPaperStatus(message.getForeignId() + "");
                    break;
                case Constant.type_pjjs:
                    //评价教师
//                    DjhJumpUtil.getInstance().startOpTeacherActivity(context, UserController.getInstance().getUserId(), message.getForeignId() + "", UserController.getInstance().getUser().getUserName(), false);
                    break;
                case Constant.type_wdpj:
                    // 我的评价
                    break;
                case Constant.type_zb:
                case Constant.type_jk:
                    // 转班   结课
                    break;
                case Constant.type_ycbj:
                    // 移除班级         无操作
                    break;
                case Constant.type_sktx:
                    // 上课提醒         无操作
                    break;
            }
        }
    };



    /**
     * 获取消息类型
     */
    private void httpMessageType() {
        showfxDialog();
        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson headJson = new HeadJson(response);
                if (headJson.getFlag() == 1) {
                    List<BeMessageType> temp = headJson.parsingListArray(new GsonType<List<BeMessageType>>() {
                    });
                    if (temp != null && temp.size() > 0) {
                        lists.clear();
                        lists.addAll(temp);
                        tv_choose.setText(lists.get(0).getDesc());
                        tv_choose.setTag(lists.get(0).getCode());
                        adapter_apinner.notifyDataSetChanged();
                        type = "";
                        showfxDialog();
                        httpData();
                    }
                } else {
                    ToastUtil.showToast(context, headJson.getMsg());
                }
            }
        };
        RequestUtill.getInstance().httpMessageType(context, "2", UserController.getInstance().getUserId(), callback);
    }

    /**
     * 根据查询条件获取该类型的消息
     */
    @Override
    public void httpData() {

        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                finishRefreshAndLoadMoer(refreshLayout, 0);
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getFlag() == 1) {

                    List<BeMessage> temp = json.parsingListArray(new GsonType<List<BeMessage>>() {
                    });
                    if (temp != null) {
                        if (mPageNum == 1) {
                            datas.clear();
                        }
                        datas.addAll(temp);
                        adapter.notifyDataSetChanged();
                    }
                    mPageNum++;
                } else {
                    ToastUtil.showToast(context, json.getMsg());
                }
                finishRefreshAndLoadMoer(refreshLayout, json.getIsLastPage());
            }
        };
        RequestUtill.getInstance().httpMessageByType(context, UserController.getInstance().getUserId(), type, callback, "15", mPageNum + "");
    }

    private void httpreadmsg(String caseMsg, final int position) {
        ResultCallback callback = new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showToast(context, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                HeadJson headJson = new HeadJson(response);
                if (headJson.getFlag() == 1) {
                    datas.get(position).setIsRead("1");
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(context, headJson.getMsg());
                }
            }
        };
        RequestUtill.getInstance().httpReadMsg(context, callback, caseMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissfxDialog();
    }

    public void httpPaperStatus(final String paperId) {

    }

    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
        tvNull.setText(R.string.e_log_null);
        tvNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfxDialog();
                httpData();
            }
        });
    }
}
