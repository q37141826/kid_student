package cn.dajiahui.kid.ui.video;

import android.support.v7.widget.Toolbar;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.video.bean.BeVideo;
import cn.dajiahui.kid.ui.video.util.JCFullscreenPlayerStudent;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2017/8/18.
 * 视频全屏播放效果
 */
public class FullscreenVideoActivity extends FxActivity {
    @Override
    protected void initView() {
        BeVideo  beVideo = (BeVideo) getIntent().getSerializableExtra(Constant.bundle_obj);
        JCFullscreenPlayerStudent.startFullscreen(this, JCFullscreenPlayerStudent.class, beVideo.url, "");
    }

    public void setStatusBar(Toolbar title) {
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            finishActivity();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
