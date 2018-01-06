package com.fxtx.framework.ui.base;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fxtx.framework.R;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.BaseUtil;

/**
 * Created by z on 2015/9/21.
 */
public class WebActivity extends FxActivity {

    public TextView tvTitle;
    public WebView webView;
    public String url;
    public String title;
    public TextView tvNull;
    public Boolean type = false;
    public String id;
    public AudioManager mAudioManager;
    public boolean isPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setfxTtitle(title);
        onBackText();
        if (!StringUtil.isEmpty(id)) {
//            onRightBtn(0, R.string.fx_for_details);
        }
    }

    public void setInfoLayout() {
        setContentView(R.layout.activity_web);
    }

    @Override
    protected void initView() {
        setInfoLayout();
        url = getIntent().getStringExtra("_object");
        title = getIntent().getStringExtra("_title");
        type = getIntent().getBooleanExtra("_type", false);
        id = getIntent().getStringExtra("_id");
        webView = getView(R.id.wb);
        tvNull = getView(R.id.tv_null);
        if (StringUtil.isEmpty(url)) {
            tvNull.setVisibility(View.VISIBLE);
            tvNull.setText("地址不正确，访问失败");
        } else {
            if (type) {
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setSupportZoom(true);//支持缩放
            }
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //关闭webview中缓存
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            webView.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
            webView.requestFocusFromTouch();
            infiWebView();
            webView.loadUrl(url);
            BaseUtil.clipboard(context, url);
        }
    }

    public void infiWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    dismissfxDialog();
                } else {
                    // 加载中
                    showfxDialog();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        webView.reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        webView.onPause();
        requestAudioFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.resumeTimers();
        webView.destroy();
        mAudioManager.abandonAudioFocus(audioFocusChangeListener);
    }


    private void requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
        }
    }

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (isPause && focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                requestAudioFocus();
            }
        }
    };
}
