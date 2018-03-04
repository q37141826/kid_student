package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
* 课本剧和卡拉ok成功录制的activity共用一个
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
    private LinearLayout bottomRoot;//底部重新录制，保存到我的作品的父布局
    private String makeTextBookDrma;
    private String showBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_text_book_success);
        initialize();
        Intent intent = this.getIntent();
        makeTextBookDrma = intent.getStringExtra("MakeFlag");
        /*表示有显示底部按钮的标志 只有制作卡拉OK 课本剧的时候才显示*/
        if (intent.getStringExtra("ShowBottom").equals("SHOW")) {
            bottomRoot.setVisibility(View.VISIBLE);
        }

        if (makeTextBookDrma.equals("MakeTextBookDrma")) {
            beGoTextBookSuccess = (BeGoTextBookSuccess) intent.getSerializableExtra("BeGoTextBookSuccess");
        } else if (makeTextBookDrma.equals("MakeKraoOke")) {
            beGoTextBookSuccess = (BeGoTextBookSuccess) intent.getSerializableExtra("BeGoTextBookSuccess");
        }

        mVideoplayer.setUp(beGoTextBookSuccess.getMineWorksTempPath(), JCVideoPlayer.SCREEN_LAYOUT_LIST);
        mVideoplayer.onStatePreparingChangingUrl(0, 100);

        mVideoplayer.startVideo();
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
        bottomRoot = getView(R.id.bottomRoot);
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

                    if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                        setResult(1);
                    } else if (makeTextBookDrma.equals("MakeKraoOke")) {
                        setResult(2);
                    }

                    finishActivity();
                   /*再录一次*/
                    break;
                case R.id.tv_savemineworks:
                    Toast.makeText(context, "保存至我的作品", Toast.LENGTH_SHORT).show();

                    String mineWorksTempPath = beGoTextBookSuccess.getMineWorksTempPath();

                    if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                        Logger.d("课本剧------：" + mineWorksTempPath);

                        String sTextBookDrma = mineWorksTempPath.substring(mineWorksTempPath.lastIndexOf("/"));
                        /*复制文件到 PathMineWorks*/
                        FileUtil.copyFile(beGoTextBookSuccess.getMineWorksTempPath(), KidConfig.getInstance().getPathMineWorksTextBookDrama() + sTextBookDrma);

                    } else if (makeTextBookDrma.equals("MakeKraoOke")) {
                        Logger.d("卡拉ok -----：" + mineWorksTempPath);
                        String sKraoOke = mineWorksTempPath.substring(mineWorksTempPath.lastIndexOf("/"));
                       /*复制文件到 PathMineWorks*/
                        FileUtil.copyFile(beGoTextBookSuccess.getMineWorksTempPath(), KidConfig.getInstance().getPathMineWorksKaraOke() + sKraoOke);
                    }

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
