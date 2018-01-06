package cn.dajiahui.kid.ui.mine.personalinformation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxtx.framework.image.util.ImageUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.BottomDialog;

import java.io.File;
import java.util.ArrayList;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.util.DjhJumpUtil;


/*
 个人信息
* */
public class UserDetailsActivity extends FxActivity {
    public static final int PICSETSULT = 55;
    private ImageView userIcon;
    private TextView tv_name, tvSex, tvAge;
    private BottomDialog dialog;

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
        getView(R.id.re_user_name).setOnClickListener(onclick);
        getView(R.id.re_user_age).setOnClickListener(onclick);
        getView(R.id.re_user_sex).setOnClickListener(onclick);

        userIcon = getView(R.id.img_user_icon);
        tv_name = getView(R.id.tv_user_name_right);

        tvAge = getView(R.id.tv_user_age_right);
        tvSex = getView(R.id.tv_user_sex_right);
//        initData();
        dialog = new BottomDialog(context, R.layout.dialog_choosepic) {
            @Override
            public void initView() {
                rootView.findViewById(R.id.btn_cancle).setOnClickListener(onClick);
                rootView.findViewById(R.id.btn_takephoto).setOnClickListener(onClick);
                rootView.findViewById(R.id.btn_choose).setOnClickListener(onClick);
            }
        };

        initData();
    }

    private void initData() {
        BeUser user = UserController.getInstance().getUser();
//        if (user == null)
//            return;
//        GlideUtil.showRoundImage(context, user.getAvator(), userIcon, R.drawable.ico_default_user, mtrue);
//        tv_account.setText(user.getUserName());

        tv_name.setText("张三");
        tvAge.setText("2017-12-17");//TimeUtil.timeFormat(user.getBirthday(), TimeUtil.yyMD)
        tvSex.setText("男");

//        if (StringUtil.isSex(user.getSex())) {
//
//        } else {
//
//        }
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

                case R.id.re_user_name:
                    //点击用户姓名行
                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_name);
                    break;

                case R.id.re_user_sex:

                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_sex);
                    break;
                case R.id.re_user_age:

                    DjhJumpUtil.getInstance().startUserSetActivity(context, Constant.user_edit_age);
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
                    path = UserController.getInstance().getUserImageAnswer(UserDetailsActivity.this);//拍照存放路径
                    imagename = getImgName();
                    ImageUtil.takePhone(UserDetailsActivity.this, path, imagename, TAKE_CAMERA_PICTURE);
                    break;
                case R.id.btn_choose:
                    //相册
                    DjhJumpUtil.getInstance().startSelectPhotoActivity(context, Constant.Alum_phone_UserIcon);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //用户信息修改
            if (requestCode == DjhJumpUtil.getInstance().activtiy_UserSet)
//                initData();
                if (requestCode == DjhJumpUtil.getInstance().activtiy_SelectPhoto) {
                    //上传选择的照片
                    ArrayList<String> strings = data.getStringArrayListExtra(Constant.bundle_obj);
                    File file = new File(strings.get(0));
//                httpUserIcon(file);
                }
            if (requestCode == TAKE_CAMERA_PICTURE) {
                Bitmap map = ImageUtil.uriToBitmap(Uri.fromFile(new File(path + imagename)), context);
                int degree = ImageUtil.readPictureDegree(path + imagename);
                if (degree != 0) {
                    map = ImageUtil.rotaingImageView(degree, map);
                }
                ImageUtil.bitmapToFile(map, path + imagename, 4096);
                File file = new File(path + imagename);
//                httpUserIcon(file);
            }
        }
    }

    private String getImgName() {
        String imageName = "";
        imageName = System.currentTimeMillis() + ".jpg";
        return imageName;
    }

//    public void httpUserIcon(File file) {
//        showfxDialog(R.string.submiting);
//        RequestUtill.getInstance().uploadUserIcon(context, new ResultCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//                dismissfxDialog();
//                ToastUtil.showToast(context, ErrorCode.error(e));
//            }
//
//            @Override
//            public void onResponse(String response) {
//                dismissfxDialog();
//                HeadJson headJson = new HeadJson(response);
//                if (headJson.getstatus() == 1) {
//                    UserController.getInstance().getUser().setAvator(headJson.parsingString("avator"));
//                    PreferenceManager.getInstance().setCurrentUserAvatar(UserController.getInstance().getUser().getAvator());
//                    GlideUtil.showNoneImage(UserDetailsActivity.this, UserController.getInstance().getUser().getAvator(), userIcon, R.drawable.ico_default_user, mtrue);
////                    initData();
//                    ToastUtil.showToast(context, R.string.save_ok);
//                    setResult(PICSETSULT);
//                } else {
//                    ToastUtil.showToast(context, headJson.getMsg());
//                }
//            }
//        }, file, UserController.getInstance().getUserId());
//    }
}
