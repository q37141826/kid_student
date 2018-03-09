package cn.dajiahui.kid.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.ui.mine.about.AboutActivity;
import cn.dajiahui.kid.ui.mine.myclass.MyClassActivity;
import cn.dajiahui.kid.ui.mine.myworks.MyWorksActivity;
import cn.dajiahui.kid.ui.mine.notice.NoticeActivity;
import cn.dajiahui.kid.ui.mine.personalinformation.UserDetailsActivity;
import cn.dajiahui.kid.ui.mine.setting.SettingActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * 我的
 */
public class FrMine extends FxFragment {
    public static final int PICFPRRESULT = 9;
    private ImageView imSet;
    private TextView tv_userName, tv_sex, tv_studylength, tv_goodhomework; // 用户名
    public ImageView imUser;//头像
    private TextView tv_myclass;//我的班级
    private TextView tv_myworks;//我的作品
    private TextView tv_about;//关于
    private TextView tv_notice;//通知
    private TextView tv_setting;//设置
    private TextView tv_noticecount;//通知的小红点
    private RelativeLayout topView;//通知的小红点

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_mine, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
//        /*获取网络数据*/
//        mineHttp();
//        initData();


    }

//    /*我的网络请求*/
//    private void mineHttp() {
//        httpData();
//    }

//    @Override
//    public void httpData() {
//        super.httpData();
//        RequestUtill.getInstance().httpMine(getActivity(), callMine);
//
//    }

//    ResultCallback callMine = new ResultCallback() {
//
//
//        @Override
//        public void onError(Request request, Exception e) {
//            dismissfxDialog();
//
//        }
//
//        @Override
//        public void onResponse(String response) {
//            dismissfxDialog();
//            HeadJson json = new HeadJson(response);
//            if (json.getstatus() == 0) {
//
//
//            } else {
//                ToastUtil.showToast(getContext(), json.getMsg());
//            }
//
//        }
//
//    };

    private void initData() {
        BeUser user = UserController.getInstance().getUser();
        tv_userName.setText(user.getNickname());
        String gender = user.getGender();
        if (gender.equals("1")) {
            tv_sex.setText("女");
        } else {
            tv_sex.setText("男");
        }

        /*设置头像*/
        GlideUtil.showRoundImage(getActivity(), UserController.getInstance().getUser().getAvatar(), imUser, R.drawable.ico_default_user, true);

//        tv_studylength.setText("360min");
//        tv_goodhomework.setText("30");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.iv_user:    //用户头像
//                   DjhJumpUtil.getInstance().startSelectPhotoActivity(getActivity(), Constant.Alum_phone_UserIcon);
//                    break;

                case R.id.topView:
                case R.id.iv_edit:
                    DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), UserDetailsActivity.class, null, PICFPRRESULT);
                    break;
                case R.id.tvMyclass://我的班级
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MyClassActivity.class);
                    break;
                case R.id.tvMyworks: //我的作品
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MyWorksActivity.class);
                    break;
                case R.id.tvAbout: //关于
                  DjhJumpUtil.getInstance().startBaseActivity(getActivity(),  AboutActivity.class);
//                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), ChivoxMainActivity.class); // 驰声测试用
                    break;
                case R.id.tvNotice: //通知
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), NoticeActivity.class);

                    break;
                case R.id.tvSet_up: //设置
                    DjhJumpUtil.getInstance().startBaseActivity(getContext(), SettingActivity.class);
                    break;

                default:
                    break;
            }
        }
    };

    /*初始化*/
    private void initialize() {
        imUser = getView(R.id.iv_user);
        imUser.setOnClickListener(onClick);
        imSet = getView(R.id.iv_edit);
        imSet.setOnClickListener(onClick);
        tv_userName = getView(R.id.tv_user_name);
        tv_sex = getView(R.id.tv_sex);
        tv_studylength = getView(R.id.tv_studylength);
        tv_goodhomework = getView(R.id.tv_goodhomework);
        tv_myclass = getView(R.id.tvMyclass);
        tv_myclass.setOnClickListener(onClick);
        tv_myworks = getView(R.id.tvMyworks);
        tv_myworks.setOnClickListener(onClick);
        tv_about = getView(R.id.tvAbout);
        tv_about.setOnClickListener(onClick);
        tv_notice = getView(R.id.tvNotice);
        tv_notice.setOnClickListener(onClick);
        tv_setting = getView(R.id.tvSet_up);
        tv_noticecount = getView(R.id.tv_noticecount);
        topView = getView(R.id.topView);
        tv_setting.setOnClickListener(onClick);
        topView.setOnClickListener(onClick);
    }

}
