package cn.dajiahui.kid.ui.study.kinds.karaoke;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BePageData;
import cn.dajiahui.kid.ui.study.view.LazyLoadFragment;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by mj on 2018/2/05.
 * <p>
 * kalaOk  fragment
 */

public class KaraOkeFragment extends LazyLoadFragment {


    private TextView tvunit;
    private cn.dajiahui.kid.ui.study.kinds.karaoke.JCVideoPlayerStudentFragment mVideoplayer;
    private RelativeLayout videoplayerroot;
    private Button btn_look;
    private BePageData beKaraOkPageData;


    @Override
    protected int setContentView() {
        return R.layout.fr_kara_ok;
    }


    @Override
    protected void lazyLoad() {
        Bundle bundle = getArguments();
          /*先获取数据*/
        beKaraOkPageData = (BePageData) bundle.get("BePageData");
        initialize();
        /*获取Mp4视频名称和背景音名称*/
        String sMp4 = MD5.getMD5(beKaraOkPageData.getPage_url().substring(beKaraOkPageData.getPage_url().lastIndexOf("/"))) + ".mp4";
        String sBackground = MD5.getMD5(beKaraOkPageData.getMusic_oss_name().substring(beKaraOkPageData.getMusic_oss_name().lastIndexOf("/"))) + ".mp3";

        /*判断mp4文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeMp4() + sMp4)) {
            downloadKaraOkeMp4();
        } else {
            playVideo(KidConfig.getInstance().getPathKaraOkeMp4() + sMp4);
        }
        /*判断背景音文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground)) {
            downloadKaraOkeBackground();
        }

        tvunit.setText(beKaraOkPageData.getTitle());


    }

    private void playVideo(String path) {
         /*应该判断本地文件夹是否有文件*/
        mVideoplayer.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_LIST);
        mVideoplayer.startVideo();
        mVideoplayer.hideView();
        mVideoplayer.hideBackButton();
    }

    /*下载卡拉okmp4*/
    private void downloadKaraOkeMp4() {
        Logger.d("下载----downloadKaraOkeMp4----" + beKaraOkPageData.getPage_url());

        BeDownFile file = new BeDownFile(Constant.file_kaoraok_mp4, beKaraOkPageData.getPage_url(), "", KidConfig.getInstance().getPathTemp());

        new DownloadFile((KaraOkeActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                progressDialog.dismiss();
                Logger.d("fileurl:" + fileurl);
                playVideo(fileurl);

            }
        });
    }

    /*下载卡拉okmp4*/
    private void downloadKaraOkeBackground() {
//        Logger.d("下载----downloadKaraOk背景音----" + beKaraOkPageData.getMusic_oss_name());

        BeDownFile file = new BeDownFile(Constant.file_kaoraok_bgAudio, beKaraOkPageData.getMusic_oss_name(), "", KidConfig.getInstance().getPathTemp());

        new DownloadFile((KaraOkeActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                progressDialog.dismiss();
                Logger.d("fileurl:" + fileurl);


            }
        });
    }


    /*初始化*/
    private void initialize() {
        tvunit = findViewById(R.id.tv_unit);
        mVideoplayer = findViewById(R.id.videoplayer);
        videoplayerroot = findViewById(R.id.videoplayerroot);
        btn_look = findViewById(R.id.btn_look);

        btn_look.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("BePageData", beKaraOkPageData);
            DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MakeKraoOkeActivity.class, bundle, 0);

        }
    };

    public void continueStartVideo() {
        mVideoplayer.startVideo();
    }

    int onPauseCurrent = 0;

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoplayer != null) {
            onPauseCurrent = mVideoplayer.getCurrentPosition();
            mVideoplayer.onStatePause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


}