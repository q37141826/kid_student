package cn.dajiahui.kid.ui.mine.personalinformation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.mine.FrSetName;
import cn.dajiahui.kid.ui.mine.FrSetPhone;
import cn.dajiahui.kid.ui.mine.FrSetPwd;

/**
 *
 * 修改信息接口
 */
public class UserSetActivity extends FxActivity {
    private FxFragment fragment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_set);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra(Constant.bundle_type, Constant.user_edit_user);
        //修改用户信息
        switch (type) {
            case Constant.user_edit_phone:
                //修改手机
                setfxTtitle(R.string.edit_phone);
                fragment = new FrSetPhone();
                break;
            case Constant.user_edit_pwd:
                //修改密码
                setfxTtitle(R.string.edit_pwd);
                fragment = new FrSetPwd();
                break;
            case Constant.user_edit_user:
                //修改用户名
                setfxTtitle(R.string.edit_user);
                fragment = new FrSetUser();
                break;
            case  Constant.user_edit_sign:
                setfxTtitle(R.string.edit_sign);
//                fragment = new FrSetSign();
                break;
            case Constant.user_edit_age:
                setfxTtitle(R.string.edit_birth);
                fragment = new FrSetBirth();
                break;
            case Constant.user_edit_email:
                setfxTtitle(R.string.edit_email);
//                fragment = new FrSetEmail();
                break;
            case Constant.user_edit_name:
                setfxTtitle(R.string.edit_name);
                fragment = new FrSetName();
                break;
            case Constant.user_edit_sex:
                setfxTtitle(R.string.edit_sex);
                fragment = new FrSetSex();
            default:
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(com.fxtx.framework.R.id.tab_fragment, fragment).commit();
        onBackText();
        onRightBtn(R.drawable.ico_updata, R.string.tv_submit);
    }

    @Override
    public void onRightBtnClick(View view) {
        super.onRightBtnClick(view);
        fragment.httpData();
    }
}
