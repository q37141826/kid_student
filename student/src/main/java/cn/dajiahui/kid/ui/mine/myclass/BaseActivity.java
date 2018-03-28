package cn.dajiahui.kid.ui.mine.myclass;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import cn.dajiahui.kid.R;


/**
 * Created by moon.zhong on 2015/3/11.
 */
public abstract class BaseActivity extends ActionBarActivity {

    public Context context ;
    private FxProgressDialog progressDialog;
    protected final int PROGRESS_BACK = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this ;
        initBase() ;
    }

    private void initBase(){

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getIntentData() ;
        initView() ;

    }

    public void getIntentData(){

    }

    public abstract void initView() ;





    public void showToast(int id){
        showToast(id,Toast.LENGTH_SHORT);
    }

    public void showToast(int id ,int duration){
        Toast.makeText(context,id,duration).show()  ;
    }

    public void showToast(CharSequence msg){
        showToast(msg,Toast.LENGTH_SHORT);
    }

    public void showToast(CharSequence msg ,int duration){
        Toast.makeText(context,msg,duration).show() ;
    }

    public void showfxDialog() {
        showfxDialog(R.string.fx_login);
    }

    public void showfxDialog(Object title) {
        if (this.isFinishing())
            return;
        if (progressDialog == null) {
            progressDialog = new FxProgressDialog(this);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        //点击返回
                        dismissfxDialog(PROGRESS_BACK);
                        return true;
                    }
                    return false;
                }
            });
        }
        if (title != null) {
            if (title instanceof String) {
                progressDialog.setTextMsg((String) title);
            } else {
                progressDialog.setTextMsg((Integer) title);
            }
        }
        progressDialog.show();
    }

    protected void dismissfxDialog(int flag) {
        if (this.isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void dismissfxDialog() {
        dismissfxDialog(0);
    }
}
