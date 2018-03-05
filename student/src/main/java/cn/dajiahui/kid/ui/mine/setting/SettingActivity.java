package cn.dajiahui.kid.ui.mine.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.hyphenate.EMCallBack;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.chat.constant.ImHelper;
import cn.dajiahui.kid.ui.login.LoginActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * 设置
 */
public class SettingActivity extends FxActivity {

    private TextView defealt_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_setting);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        getView(R.id.tvSecurity).setOnClickListener(onClick);
        getView(R.id.tvFixpass).setOnClickListener(onClick);
        getView(R.id.tvclean).setOnClickListener(onClick);
        getView(R.id.btn_Exit).setOnClickListener(onClick);
        defealt_tel = getView(R.id.defealt_tel);

        defealt_tel.setText( UserController.getInstance().getUser().getTelnum());
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSecurity:
                    DjhJumpUtil.getInstance().startBaseActivity(SettingActivity.this, SetPhoneActivity.class);
                    break;
                case R.id.tvFixpass:
                    DjhJumpUtil.getInstance().startBaseActivity(SettingActivity.this, SetPassActivity.class);
                    break;
                case R.id.tvclean:
                    Toast.makeText(context, "清除缓存", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btn_Exit:
                    //退出登录
                    FxDialog dialog = new FxDialog(SettingActivity.this) {
                        @Override
                        public void onRightBtn(int flag) {
                            loginOut();
                        }

                        @Override
                        public void onLeftBtn(int flag) {

                        }
                    };
                    dialog.setTitle(R.string.prompt);
                    dialog.setMessage(R.string.login_edit);
                    dialog.show();
                    break;

                default:
                    break;
            }
        }
    };


    //退出登录
    public void loginOut() {
        showfxDialog(R.string.logout);
        if (UserController.getInstance().getUserAuth().isMsn) {
            ImHelper.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    //退出成功
                    dismissfxDialog();
                    DjhJumpUtil.getInstance().startBaseActivity(SettingActivity.this, LoginActivity.class);
                    UserController.getInstance().exitLogin(SettingActivity.this);
                    finishActivity();
                }

                @Override
                public void onError(int i, String s) {
                    dismissfxDialog();
                    ToastUtil.showToast(SettingActivity.this, s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        } else {
            dismissfxDialog();
            DjhJumpUtil.getInstance().startBaseActivity(SettingActivity.this, LoginActivity.class);
            UserController.getInstance().exitLogin(SettingActivity.this);
            finishActivity();
        }
    }

    private void httpAboutUs() {
        showfxDialog();
        RequestUtill.getInstance().httpAboutUs(SettingActivity.this, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog();
                ToastUtil.showToast(SettingActivity.this, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                dismissfxDialog();
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 0) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject obj2 = obj.getJSONObject("info");
                        String url = obj2.getString("aboutUrl");
//                        DjhJumpUtil.getInstance().startWebActivity(SettingActivity.this, getString(R.string.text_about), url, mfalse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showToast(SettingActivity.this, json.getMsg());
                }
            }
        });
    }


}
