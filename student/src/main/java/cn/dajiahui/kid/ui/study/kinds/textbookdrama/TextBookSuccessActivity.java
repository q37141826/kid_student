package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/*
*
* 课本剧制作成功后
*
* */
public class TextBookSuccessActivity extends FxActivity {

    private cn.dajiahui.kid.ui.study.kinds.textbookdrama.JCVideoPlayerTextScuessBook mVideoplayer;
    private TextView tvniitname;//作品名称
    private ImageView imguser;//头像
    private TextView tv_username;//名字
    private TextView tvmaketime;//制作时间
    private TextView tvfraction;//评分
    private RatingBar rbscore;//小星星
    private TextView tvshare;//
    private ImageView imgweixin;//微信
    private ImageView imgpengyouquan;//朋友圈
    private TextView tvrecordagain;//重新录制
    private TextView tvsavemineworks;//保存我的作品
    private BeGoTextBookSuccess beGoTextBookSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_text_book_success);
        initialize();
        beGoTextBookSuccess = (BeGoTextBookSuccess) this.getIntent().getSerializableExtra("BeGoTextBookSuccess");
        mVideoplayer.setUp(beGoTextBookSuccess.getMineWorksTempPath(), JCVideoPlayer.SCREEN_LAYOUT_LIST);
//        mVideoplayer.setOnDuration(new JCVideoPlayerTextScuessBook.OnDuration() {
//            @Override
//            public void onDuration(String duration) {
//                Logger.d("duration" + duration);
//            }
//        });
        mVideoplayer.startVideo();
        mVideoplayer.onStatePreparingChangingUrl(0, 0);
        mVideoplayer.hideView();//隐藏不需要的view

        tvniitname.setText(beGoTextBookSuccess.getMineWorksName());
        /*加载圆形图片*/
        GlideUtil.showRoundImage(TextBookSuccessActivity.this, beGoTextBookSuccess.getUserUrl(), imguser, R.drawable.ico_default_user, true);
        tv_username.setText(beGoTextBookSuccess.getUserName());
        tvmaketime.setText(beGoTextBookSuccess.getMakeTime());
        tvfraction.setText(beGoTextBookSuccess.getScore());

    }

    /*初始化*/
    private void initialize() {

        mVideoplayer = getView(R.id.videoplayer);
        tvniitname = getView(R.id.tv_unitname);
        imguser = getView(R.id.img_user);
        tv_username = getView(R.id.tv_username);
        tvmaketime = getView(R.id.tv_maketime);
        tvfraction = getView(R.id.tv_fraction);
        rbscore = getView(R.id.rb_score);
        tvshare = getView(R.id.tv_share);
        getView(R.id.img_weixin).setOnClickListener(onClick);
        getView(R.id.img_pengyouquan).setOnClickListener(onClick);
        getView(R.id.tv_recordagain).setOnClickListener(onClick);
        getView(R.id.tv_savemineworks).setOnClickListener(onClick);
    }


    /*点击事件*/
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.img_weixin:
                    Toast.makeText(context, "分享至微信", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.img_pengyouquan:
                    Toast.makeText(context, "分享至朋友圈", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.tv_recordagain:
                    setResult(1);
                    finishActivity();
                   /*再录一次*/
                    break;
                case R.id.tv_savemineworks:

                    Toast.makeText(context, "保存至我的作品", Toast.LENGTH_SHORT).show();
                    /*复制文件到 PathMineWorks*/
                    FileUtil.copy(KidConfig.getInstance().getPathMineWorksTemp(), KidConfig.getInstance().getPathMineWorks());
                    break;
                default:
                    break;

            }

        }
    };

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            /*只有按视频返回键的时候才清理*/
            JCVideoPlayer.releaseAllVideos();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoplayer.onStatePause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mVideoplayer.startVideo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d("TextBookSuccessActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
