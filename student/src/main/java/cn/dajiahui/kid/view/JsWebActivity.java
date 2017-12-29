package cn.dajiahui.kid.view;

import android.webkit.JavascriptInterface;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * Created by z on 2017/3/31.
 * Js 工具方法
 */
public abstract class JsWebActivity extends FxActivity {
    @JavascriptInterface
    public void answerQuestion(String questionId) {
    }

    @JavascriptInterface
    public void answerTextToImgQuestion(String questionId) {
    }

    @JavascriptInterface
    public void playMp4(String url) {
        DjhJumpUtil.getInstance().startFullscreenVideoActivity(this, url);
    }

    @JavascriptInterface
    public void answerMp3Question(String questionId) {

    }

    @JavascriptInterface
    public void correctQuestion(String detailId) {

    }

    @JavascriptInterface
    public void submitWrong() {
    }

    @JavascriptInterface
    public void removeImg() {
    }

    /**
     * 开启加载动画
     */
    @JavascriptInterface
    public void loadStart() {
        showfxDialog();
    }

    /**
     * 结束加载动画
     */
    @JavascriptInterface
    public void loadFinish() {
        dismissfxDialog();
    }


    /**
     * 打开新的界面显示
     *
     * @param title
     * @param url
     */
    @JavascriptInterface
    public void toWebView(String title, String url) {

    }
    
    @JavascriptInterface
    public void submitPaper() {
    }
}
