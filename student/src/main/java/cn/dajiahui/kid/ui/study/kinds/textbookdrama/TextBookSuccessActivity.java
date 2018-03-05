package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.ui.study.bean.BeUpdateMIneWorks;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/*
*
* 课本剧制作成功后
*
* 课本剧和卡拉ok成功录制的activity共用一个
*
* 保存我的作品成功之后要显示  分享的view
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
    private RelativeLayout shareRoot;//分享父布局 //保存我的作品成功之后显示
    private String makeTextBookDrma;
    private String mineWorksTempPath;//我的作品临时文件夹
    private String sTextBookDrma;//文件名称

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
        shareRoot = getView(R.id.share_root);
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

                    mineWorksTempPath = beGoTextBookSuccess.getMineWorksTempPath();

                    if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                        showfxDialog("文件上传中，请稍等...");

                        String sTextBookDrma = mineWorksTempPath.substring(mineWorksTempPath.lastIndexOf("/") + 1);
                        /*复制文件到 PathMineWorks*/
                        FileUtil.copyFile(beGoTextBookSuccess.getMineWorksTempPath(), KidConfig.getInstance().getPathMineWorksTextBookDrama() + sTextBookDrma);

                        RequestUtill.getInstance().httpSaveMineWorks(TextBookSuccessActivity.this, callMineWorksUp,
                                beGoTextBookSuccess.getPage_id(),
                                String.valueOf(System.currentTimeMillis()),
                                KidConfig.getInstance().getPathMineWorksTextBookDrama() + sTextBookDrma,
                                KidConfig.getInstance().getPathMineWorksThumbnail() + sTextBookDrma.substring(0, sTextBookDrma.lastIndexOf(".")) + ".png");

                    } else if (makeTextBookDrma.equals("MakeKraoOke")) {


                    }

                    break;
                default:
                    break;

            }

        }
    };

    /*上传我的作品回调*/
    ResultCallback callMineWorksUp = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            Logger.d("作品上传失败" + e);
        }

        @Override
        public void onResponse(String response) {
            dismissfxDialog();
              /*显示分享布局  要分享成功后显示*/
            shareRoot.setVisibility(View.VISIBLE);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                BeUpdateMIneWorks beUpdateMIneWorks = json.parsingObject(BeUpdateMIneWorks.class);
                Logger.d("beUpdateMIneWorks:" + beUpdateMIneWorks.toString());

                if (makeTextBookDrma.equals("MakeTextBookDrma")) {

                } else if (makeTextBookDrma.equals("MakeKraoOke")) {
                    String sKraoOke = mineWorksTempPath.substring(mineWorksTempPath.lastIndexOf("/"));
                    /*复制文件到 PathMineWorks*/
                    FileUtil.copyFile(beGoTextBookSuccess.getMineWorksTempPath(), KidConfig.getInstance().getPathMineWorksKaraOke() + sKraoOke);
                }
                Toast.makeText(context, "保存至我的作品", Toast.LENGTH_SHORT).show();
            } else {
                ToastUtil.showToast(TextBookSuccessActivity.this, json.getMsg());
            }

            Logger.d("作品上传成功" + response);
        }

        @Override
        public void inProgress(float progress) {
            Logger.d("作品上传中" + progress);

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
