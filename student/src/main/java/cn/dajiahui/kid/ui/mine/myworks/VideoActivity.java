package cn.dajiahui.kid.ui.mine.myworks;

import android.content.pm.ActivityInfo;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.bean.BeMyWorks;
import cn.dajiahui.kid.ui.video.util.JCVideoPlayerStudent;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * 视频播放类
 */
public class VideoActivity extends FxActivity {
    JCVideoPlayerStudent jcVideoPlayerStandard;
    private BeMyWorks beMyWorks;
    private TextView tv_videoname, tv_time, tv_score, tv_friendCircle, tv_friends;
    private RatingBar rb_score;
    private ImageView img_head;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_video);
        beMyWorks = (BeMyWorks) getIntent().getSerializableExtra("WORKSDATA");
//        if (StringUtil.isEmpty(beVideo.url)) {
//            ToastUtil.showToast(context, "数据异常");
//            finishActivity();
//        }
        initialize();

        if (beMyWorks != null) {
            tv_videoname.setText(beMyWorks.getWorksName());
            tv_time.setText(beMyWorks.getWorkstime());
        }

        //设置全屏显示时 为横屏显示 替换了他自己设置的 根据重力感应处理的逻辑哦
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        String title = "播放视频";
        if (StringUtil.isEmpty(title)) {
            title = "";
        }

        jcVideoPlayerStandard.setUp(beMyWorks.getWorksLocalPath(), JCVideoPlayer.SCREEN_LAYOUT_LIST, title);
        jcVideoPlayerStandard.startVideo();

    }

    private void initialize() {
        jcVideoPlayerStandard = getView(R.id.videoplayer);
        tv_videoname = getView(R.id.tv_videoname);
        tv_score = getView(R.id.tv_score);
        tv_time = getView(R.id.tv_time);
        rb_score = getView(R.id.rb_score);
        img_head = getView(R.id.img_head);
        tv_friendCircle = getView(R.id.tv_friendCircle);
        tv_friends = getView(R.id.tv_friends);
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
