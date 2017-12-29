package com.fxtx.framework.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fxtx.framework.R;
import com.fxtx.framework.platforms.jpush.JpushUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.updata.OnUpdateListener;


/**
 * @author djh-zy
 * @version : 1
 * @CreateDate 2015年8月3日 下午4:21:49
 * @description : 登陆模块-
 */
public abstract class FxFirstActivity extends FxActivity {
    protected final int goWelcome = 1;
    protected final int goUpdate = 2;
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case goUpdate:
                    handlerUpdate();//创建版本更新对象

                    break;
                case goWelcome:
                    handlerWelcome();

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 版本更新 时操作
     */
    protected abstract void handlerUpdate();

    /**
     * 欢迎页时 操作
     */
    protected abstract void handlerWelcome();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView im = new ImageView(this);
        im.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        im.setLayoutParams(params);
        im.setBackgroundResource(R.drawable.ico_firstpager);
        setContentView(im);
        // 极光推送 设置
        mHandler.sendEmptyMessageDelayed(goUpdate, 100);

    }

    @Override
    public void setStatusBar(Toolbar title) {

    }

    @Override
    public void onBackPressed() {
    }

    /*监听器*/
    protected OnUpdateListener onUpdate = new OnUpdateListener() {

        @Override
        public void onUpdateSuccess() {
            finish();
        }

        @Override
        public void onUpdateError(String error) {
        }

        @Override
        public void onUpdateCancel(int type) {

            if (type == 0) {
                mHandler.sendEmptyMessage(goWelcome);
            } else if (type == 1) {
                finishActivity();
            } else if (type == 2) {
                mHandler.sendEmptyMessage(goWelcome); // 取消安装 就继续
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        JpushUtil.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JpushUtil.onPause(this);

    }
}
