package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageData;
import cn.dajiahui.kid.ui.video.util.JCVideoPlayerStudent;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by mj on 2018/1/31.
 * <p>
 * 课本剧首页Fragment
 * <p>
 * 逻辑：点击视频播放按钮可以播放当前课本剧
 * <p>
 * <p>
 * 点击查看按钮要判断是否有自己的作品  有：播放自己作品  无 ：播放原音作品
 */

public class TextBookDramaFragment extends FxFragment {


    private BeTextBookDramaPageData beTextBookDramaPageData;
    private TextView tvunit;
    private JCVideoPlayerStudent mVideoplayer;
    private RelativeLayout videoplayerroot;
    private Button btn_look;
    private Bundle bundle;
    private List<BeTextBookDramaPageData> page_data;

    private int position;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_text_book_drama, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();
        initialize();
        page_data = (List<BeTextBookDramaPageData>) bundle.get("page_data");
        position = bundle.getInt("position");
        beTextBookDramaPageData = page_data.get(position);
        String page_url = beTextBookDramaPageData.getPage_url();
        tvunit.setText(beTextBookDramaPageData.getTitle());

        /*文件名以MD5加密*/
        String mp4Name = MD5.getMD5(page_url.substring(page_url.lastIndexOf("/"))) + ".mp4";
        if (FileUtil.fileIsExists(KidConfig.getInstance().getPathTextbookPlayMp4() + mp4Name)) {
            /*读取本地*/
            playTextBook(KidConfig.getInstance().getPathTextbookPlayMp4() + mp4Name);

        } else {
             /*网络下载Mp3*/
            downloadTextBookPlayBgAudio(beTextBookDramaPageData);
            /*网络下载Mp4*/
            downloadTextBookPlayData(beTextBookDramaPageData);

        }
    }

    /*下载课本剧资源*/
    private void downloadTextBookPlayData(BeTextBookDramaPageData beTextBookDramaPageData) {

        BeDownFile file = new BeDownFile(Constant.file_textbookplay_mp4, beTextBookDramaPageData.getPage_url(), "", KidConfig.getInstance().getPathTemp());
        new DownloadFile((TextBookDramaActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                Logger.d("majin-------------课本剧" + fileurl);
                String mp4Name = fileurl.substring(fileurl.lastIndexOf("/"));
                playTextBook(KidConfig.getInstance().getPathTextbookPlayMp4() + mp4Name.substring(1).toString());
                    /*关闭下载dialog*/
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    /*下载课本剧背景音资源*/
    private void downloadTextBookPlayBgAudio(BeTextBookDramaPageData beTextBookDramaPageData) {

        BeDownFile file = new BeDownFile(Constant.file_textbookplay_bgAudio, beTextBookDramaPageData.getMusic_oss_name(), "", KidConfig.getInstance().getPathTemp());
        new DownloadFile((TextBookDramaActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                Logger.d("majin-------------课本剧背景音" + fileurl);
//                String mp4Name = fileurl.substring(fileurl.lastIndexOf("/"));

            }
        });
    }

    /*播放课本剧*/
    private void playTextBook(String textbookPath) {
        mVideoplayer.setUp(textbookPath, JCVideoPlayer.SCREEN_LAYOUT_LIST);
        mVideoplayer.startVideo();
        mVideoplayer.hideView();
        mVideoplayer.hideBackButton();
    }

    /*初始化*/
    private void initialize() {
        tvunit = getView(R.id.tv_unit);
        mVideoplayer = getView(R.id.videoplayer);
        videoplayerroot = getView(R.id.videoplayerroot);
        btn_look = getView(R.id.btn_look);
        btn_look.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("BeTextBookDramaPageData", beTextBookDramaPageData);

            DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MakeTextBookDrmaActivity.class, bundle, 0);

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        JCMediaManager.instance().mediaPlayer.release();
    }


}
