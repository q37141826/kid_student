package cn.dajiahui.kid;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.fxtx.framework.http.OkHttpClientManager;
import com.fxtx.framework.platforms.umeng.UmengShare;
import com.umeng.socialize.utils.ContextUtil;

import java.util.concurrent.TimeUnit;

import cn.dajiahui.kid.ui.chat.constant.ImHelper;

/**
 * 自定义Application
 */
public class DjhStudentApp extends Application {
    private static DjhStudentApp instance;
    @Override
    public void onCreate() {
        UmengShare.initSharePlat();
        ContextUtil.setContext(this);
        ImHelper.getInstance().init(this);
        OkHttpClientManager.getInstance().getOkHttpClient().setReadTimeout(100000,TimeUnit.MILLISECONDS);//读的时间
        OkHttpClientManager.getInstance().getOkHttpClient().setWriteTimeout(100000,TimeUnit.MILLISECONDS);//写的时间
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(100000, TimeUnit.MILLISECONDS); //设置超时时间
        instance = this;
        MultiDex.install(this) ;
        super.onCreate();
    }
    public static DjhStudentApp getInstance() {
        if (instance == null) {
            synchronized (DjhStudentApp.class) {
                if (instance == null) {
                    instance = new DjhStudentApp();
                }
            }
        }
        return instance;
    }
}
