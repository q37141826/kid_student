package cn.dajiahui.kid.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fxtx.framework.ui.FxActivity;
import com.hyphenate.easeui.BuildConfig;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.chat.util.Utils;

public class ImageGridActivity extends FxActivity {
    private static final String TAG = "ImageGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.tab_fragment, new ImageGridFragment(), TAG);
            ft.commit();
        }
        onBackText();
        setfxTtitle(R.string.attach_video);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.em_image_grid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
