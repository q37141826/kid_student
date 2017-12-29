package cn.dajiahui.kid.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.TasksCompletedView;

import java.lang.ref.WeakReference;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.MainActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;


/**
 * Created by wdj on 2017/5/24.
 */

public class LogoActivity extends FxActivity {
    private ImageView imgLogo;
    private TasksCompletedView completedView;
    private long max = 3 * 1000;
    private long cur = 0l;
    private MyHandler myhandle;
    private RelativeLayout relaroot;

    @Override
    protected void initView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logo);
        relaroot = getView(R.id.rela_root);
        completedView = getView(R.id.vTask);
        completedView.setShowText(false);
        imgLogo = getView(R.id.img_logo);
        // FIXME: 2017/10/19 majin 修改学生端Logo定制页需求
        //设置图片的位置
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(
                imgLogo.getLayoutParams());
        margin.setMargins(0, 0, 0, 260);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        imgLogo.setLayoutParams(layoutParams);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relaroot.setBackgroundResource(R.drawable.ico_firstpager);
        GlideUtil.showNoneImage(context, UserController.getInstance().getUser().getLoadUrl(), imgLogo);
        myhandle = new MyHandler(this);
        myhandle.sendEmptyMessageDelayed(1, 50);

    }

    @Override
    public void setStatusBar(Toolbar title) {
    }

    @Override
    protected void onDestroy() {
        if (myhandle != null) {
            myhandle.removeMessages(1);
            myhandle.removeMessages(2);
            myhandle = null;
        }
        super.onDestroy();

    }

    private class MyHandler extends Handler {
        private WeakReference<LogoActivity> weak;
        private LogoActivity activity;

        public MyHandler(LogoActivity activity) {
            this.activity = activity;
            weak = new WeakReference<LogoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://刷新进度
                    cur += 100;
                    if (cur <= max) {
                        myhandle.sendEmptyMessageDelayed(1, 50);
                        activity.completedView.setProgress((int) (cur * 100 / max));
                    } else
                        myhandle.sendEmptyMessage(2);
                    break;
                case 2://跳转界面
                    DjhJumpUtil.getInstance().startBaseActivity(context, MainActivity.class);
                    finishActivity();
                    break;
            }

        }
    }
}
