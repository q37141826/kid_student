package cn.dajiahui.kid.ui.study.kinds.karaoke;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BePageDataWork;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.TextBookSuccessActivity;
import cn.dajiahui.kid.ui.study.view.LazyLoadFragment;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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
    private BePageDataWork beKaraOkPageData;
    private LinearLayout singok_root;


    @Override
    protected int setContentView() {
        return R.layout.fr_kara_ok;
    }

    @Override
    protected void lazyLoad() {
        Bundle bundle = getArguments();
        /*先获取数据*/
        beKaraOkPageData = (BePageDataWork) bundle.get("BePageData");
        initialize();
        /*获取Mp4视频名称和背景音名称*/
        String sMp4 = MD5.getMD5(beKaraOkPageData.getPage_url().substring(beKaraOkPageData.getPage_url().lastIndexOf("/"))) + ".mp4";
        String sBackground = MD5.getMD5(beKaraOkPageData.getMusic_oss_name().substring(beKaraOkPageData.getMusic_oss_name().lastIndexOf("/"))) + ".mp3";

        /*判断mp4文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeMp4() + sMp4)) {
            downloadKaraOkeMp4();
        } else {
            Logger.d("播放本地卡拉OK ：" + KidConfig.getInstance().getPathKaraOkeMp4() + sMp4);
            playVideo(KidConfig.getInstance().getPathKaraOkeMp4() + sMp4);
        }
        /*判断背景音文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathKaraOkeBackgroundAudio() + sBackground)) {
            downloadKaraOkeBackground();
        }

        tvunit.setText(beKaraOkPageData.getTitle());

        /*已经唱完*/
        if (beKaraOkPageData.getMy_work_status().equals("1")) {
            singok_root.setVisibility(View.VISIBLE);
        }

    }

    /*播放卡拉OK*/
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
                /*下载成功后播放*/
                playVideo(fileurl);

            }
        });
    }

    /*下载卡拉ok背景音*/
    private void downloadKaraOkeBackground() {
//        Logger.d("下载----downloadKaraOk背景音----" + beKaraOkPageData.getMusic_oss_name());

        BeDownFile file = new BeDownFile(Constant.file_kaoraok_bgAudio, beKaraOkPageData.getMusic_oss_name(), "", KidConfig.getInstance().getPathTemp());

        new DownloadFile((KaraOkeActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

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
        singok_root = findViewById(R.id.singok_root);

        btn_look.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*点击跳转之后停止所有视频*/
            JCVideoPlayerStandard.releaseAllVideos();
            Bundle bundle = new Bundle();
            /*已经唱完 跳转查看作品页面*/
            if (beKaraOkPageData.getMy_work_status().equals("1")) {
                bundle.putString("my_work_status", beKaraOkPageData.getMy_work_status());
                bundle.putString("MakeFlag", "seedetails");
                bundle.putString("ShowBottom", "SHOW");
                bundle.putString("Look", "kalaok");
                bundle.putSerializable("BePageDataWork", beKaraOkPageData);
                DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), TextBookSuccessActivity.class, bundle, 2);

            } else {
                /*跳转制作卡拉OK页面*/
                bundle.putSerializable("BePageDataWork", beKaraOkPageData);
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MakeKraoOkeActivity.class, bundle, 0);
            }

        }
    };


    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


}
