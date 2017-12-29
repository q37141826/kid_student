package cn.dajiahui.kid.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.image.util.ImageUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.time.TimeUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.BottomDialog;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.ArrayList;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.chat.constant.PreferenceManager;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.util.DjhJumpUtil;

public class UserDetailsActivity extends FxActivity {
    private ImageView userIcon;
    private TextView tv_account, tv_name, tvPhone, tvSign, tvEmail, tvBirth,tvPwd,tvSex;
    private BottomDialog dialog;
    public static final int PICSETSULT = 55;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.user_message);
        onBackText();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_details);
        getView(R.id.re_user_icon).setOnClickListener(onclick);
        getView(R.id.re_user_account).setOnClickListener(onclick);
        getView(R.id.re_user_pwd).setOnClickListener(onclick);
        getView(R.id.re_user_phone).setOnClickListener(onclick);
        getView(R.id.re_user_name).setOnClickListener(onclick);
        getView(R.id.re_user_sign).setOnClickListener(onclick);
        getView(R.id.re_user_birth).setOnClickListener(onclick);
        getView(R.id.re_user_email).setOnClickListener(onclick);
        getView(R.id.re_user_sex).setOnClickListener(onclick);
        userIcon = getView(R.id.img_user_icon);
        tv_account = getView(R.id.tv_user_account_right);
        tv_name = getView(R.id.tv_user_name_right);
        tvPhone = getView(R.id.tv_user_phone);
        tvSign = getView(R.id.tv_user_sign_right);
        tvEmail = getView(R.id.tv_user_email_right);
        tvBirth = getView(R.id.tv_user_birth_right);
        tvPwd = getView(R.id.text_user_pwd);
        tvSex = getView(R.id.tv_user_sex_right);
        initData();
        dialog = new BottomDialog(context, R.layout.dialog_choosepic) {
            @Override
            public void initView() {
                rootView.findViewById(R.id.btn_cancle).setOnClickListener(onClick);
                rootView.findViewById(R.id.btn_takephoto).setOnClickListener(onClick);
                rootView.findViewById(R.id.btn_choose).setOnClickListener(onClick);
            }
        };
    }

    private void initData() {
        BeUser user = UserController.getInstance().getUser();
        if (user == null)
            return;
        GlideUtil.showRoundImage(context, user.getAvator(), userIcon, R.drawable.ico_default_user, true);
        tv_account.setText(user.getUserName());
        tvPhone.setText(user.getPhone());
        tv_name.setText(user.getRealName());
        tvSign.setText(user.getSignature());
        tvBirth.setText(TimeUtil.timeFormat(user.getBirthday(), TimeUtil.yyMD));
        tvEmail.setText(user.getEmail());
        if(StringUtil.isSex(user.getSex())) {
            tvSex.setText("男");
        }else {
            tvSex.setText("女");
        }
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_user_icon:
                    //点击用户头像行
                    if (dialog != null)
                        dialog.show();
                    break;
                case R.id.re_user_account:
                    //点击用户账户行
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_user);
                    break;
                case R.id.re_user_pwd:
                    //点击用户密码行
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_pwd);
                    break;
                case R.id.re_user_phone:
                    //点击用户手机号行
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_phone);
                    break;
                case R.id.re_user_name:
                    //点击用户姓名行
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_name);
                    break;
                case R.id.re_user_sign:
                    //点击用户个性签名
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_sign);
                    break;
                case R.id.re_user_birth:
                    //点击用户生日
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_birth);

                    break;
                case R.id.re_user_email:
                    //点击用户邮箱
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_email);
                    break;
                case R.id.re_user_sex:
                    //点击用户性别
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_sex);
                    break;
                default:
                    break;
            }
        }
    };
    public static int TAKE_CAMERA_PICTURE = 1000;
    private String path;
    private String imagename;
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            switch (v.getId()) {
                case R.id.btn_cancle:
                    //取消
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    break;
                case R.id.btn_takephoto:
                    //拍照
                    path =UserController.getInstance().getUserImageAnswer(UserDetailsActivity.this);//拍照存放路径
                    imagename = getImgName();
                    ImageUtil.takePhone(UserDetailsActivity.this, path, imagename, TAKE_CAMERA_PICTURE);
                    break;
                case R.id.btn_choose:
                    //相册
                    DjhJumpUtil.getInstance().startSelectPhotoActivity(context, Constant.Alum_phone_UserIcon);
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
            //用户信息修改
            if (requestCode == DjhJumpUtil.getInstance().activtiy_UserSet)
                initData();
            if (requestCode == DjhJumpUtil.getInstance().activtiy_SelectPhoto) {
                //上传选择的照片
                ArrayList<String> strings = data.getStringArrayListExtra(Constant.bundle_obj);
                File file = new File(strings.get(0));
                httpUserIcon(file);
            }
            if (requestCode == TAKE_CAMERA_PICTURE) {
                Bitmap map = ImageUtil.uriToBitmap(Uri.fromFile(new File(path + imagename)), context);
                int degree = ImageUtil.readPictureDegree(path + imagename);
                if (degree != 0) {
                    map = ImageUtil.rotaingImageView(degree, map);
                }
                ImageUtil.bitmapToFile(map, path + imagename, 4096);
                File file = new File(path+imagename);
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
                    GlideUtil.showNoneImage(UserDetailsActivity.this, UserController.getInstance().getUser().getAvator(), userIcon, R.drawable.ico_default_user, true);
                    initData();
                    ToastUtil.showToast(context, R.string.save_ok);
                    setResult(PICSETSULT);
                } else {
                    ToastUtil.showToast(context, headJson.getMsg());
                }
            }
        }, file, UserController.getInstance().getUserId());
    }
}
