package cn.dajiahui.kid.ui.mine.myworks;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.video.bean.BeVideo;
import cn.dajiahui.kid.ui.video.util.JCVideoPlayerStudent;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * 视频播放类
 */
public class VideoActivity extends FxActivity {
    JCVideoPlayerStudent jcVideoPlayerStandard;
    private TextView tvTitle;


    private BeVideo beVideo;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_video);
//        beVideo = (BeVideo) getIntent().getSerializableExtra(Constant.bundle_obj);
//        if (StringUtil.isEmpty(beVideo.url)) {
//            ToastUtil.showToast(context, "数据异常");
//            finishActivity();
//        }
        jcVideoPlayerStandard = getView(R.id.videoplayer);
        ////设置全屏显示时 为横屏显示 替换了他自己设置的 根据重力感应处理的逻辑哦
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        tvTitle = getView(R.id.tvTitle);
        String title = "播放视频";
        if (StringUtil.isEmpty(title)) {
            title = "";
        }

        jcVideoPlayerStandard.setUp("/storage/emulated/0/test.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST, title);
        jcVideoPlayerStandard.setOnDuration(new JCVideoPlayerStudent.OnDuration() {
            @Override
            public void onDuration(String duration) {

            }
        });
        jcVideoPlayerStandard.startVideo();


//

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setStatusBar(Toolbar title) {
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
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
