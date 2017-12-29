package cn.dajiahui.kid.ui.video.util;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;

import com.fxtx.framework.util.ActivityUtil;

import cn.dajiahui.kid.ui.video.FullscreenVideoActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
/**
 * Created by Administrator on 2017/8/18.
 */
public class JCFullscreenPlayerStudent extends JCVideoPlayerStandard {
    public JCFullscreenPlayerStudent(Context context) {
        super(context);
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }
    public JCFullscreenPlayerStudent(Context context, AttributeSet attrs) {
        super(context, attrs);
        FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == fm.jiecao.jcvideoplayer_lib.R.id.back || v.getId()==fm.jiecao.jcvideoplayer_lib.R.id.fullscreen){
            ActivityUtil.getInstance().finishActivity(FullscreenVideoActivity.class);
        }
    }
}
