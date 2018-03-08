package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageData;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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
    private cn.dajiahui.kid.ui.study.kinds.karaoke.JCVideoPlayerStudentFragment mVideoplayer;
    private RelativeLayout videoplayerroot;
    private Button btn_look;
    private Bundle bundle;
    private List<BeTextBookDramaPageData> page_data;
    private int position;
    private RelativeLayout info_root;
    private ImageView img_head;
    private TextView tv_author;
    private TextView tv_score;
    private RatingBar rb_score;


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
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathTextbookPlayMp4() + mp4Name)) {
             /*网络下载Mp3*/
            downloadTextBookPlayBgAudio(beTextBookDramaPageData);
            /*网络下载Mp4*/
            downloadTextBookPlayData(beTextBookDramaPageData);
        } else {
            /*读取本地*/
            playTextBook(KidConfig.getInstance().getPathTextbookPlayMp4() + mp4Name);
        }

           /*已经制作完成课本剧 要显示分数*/
        if (beTextBookDramaPageData.getMy_work_status().equals("1")) {
            if (beTextBookDramaPageData.getMy_work() != null) {
            /*显示打分布局*/
                info_root.setVisibility(View.VISIBLE);
            /*加载圆形图片*/
                GlideUtil.showRoundImage(getActivity(), UserController.getInstance().getUser().getAvatar(), img_head, R.drawable.ico_default_user, true);
                tv_author.setText(UserController.getInstance().getUser().getNickname());
                tv_score.setText(getAverage(beTextBookDramaPageData.getMy_work().getScore()) + "分");/*获取平均分*/
                rb_score.setMax(100);
                rb_score.setProgress(getScore(getAverage(beTextBookDramaPageData.getMy_work().getScore())));
            }
        }
    }

    /*下载课本剧资源*/
    private void downloadTextBookPlayData(BeTextBookDramaPageData beTextBookDramaPageData) {

        BeDownFile file = new BeDownFile(Constant.file_textbookplay_mp4, beTextBookDramaPageData.getPage_url(), "", KidConfig.getInstance().getPathTemp());
        new DownloadFile((TextBookDramaActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
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
//                Logger.d("majin-------------课本剧背景音" + fileurl);
//                String mp4Name = fileurl.substring(fileurl.lastIndexOf("/"));

            }
        });
    }

    /*播放课本剧*/
    private void playTextBook(String path) {
        mVideoplayer.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_LIST);
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

        /*查看课本剧专用*/
        info_root = getView(R.id.info_root);
        img_head = getView(R.id.img_head);
        tv_author = getView(R.id.tv_author);
        tv_score = getView(R.id.tv_score);
        rb_score = getView(R.id.rb_score);


    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//              /*已经制作完成课本剧 跳转查看页面*/
            if (beTextBookDramaPageData.getMy_work_status().equals("1")) {

                bundle.putString("my_work_status", beTextBookDramaPageData.getMy_work_status());
                bundle.putString("MakeFlag", "seedetails");
                bundle.putString("ShowBottom", "SHOW");
                bundle.putString("Look", "textbook");
                bundle.putSerializable("BeTextBookDramaPageData", beTextBookDramaPageData);
                DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), TextBookSuccessActivity.class, bundle, 2);


            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("BeTextBookDramaPageData", beTextBookDramaPageData);
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MakeTextBookDrmaActivity.class, bundle, 0);
            }

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
//        JCMediaManager.instance().mediaPlayer.release();
    }

    /*评分算法 20分为一颗星*/
    private int getScore(int score) {
        if (0 == score) {
            return 0;
        } else if (0 < score && score <= 20) {
            return 20;
        } else if (20 < score && score <= 40) {
            return 40;
        } else if (40 < score && score <= 60) {
            return 60;
        } else if (60 < score && score <= 80) {
            return 80;
        } else if (80 < score && score <= 100) {
            return 100;
        }
        return 0;
    }

    /*计算平均分*/
    private int getAverage(String my_score) {
        int score = 0;
        if (my_score.length()>0) {
            /*截取字符串*/
            for (int i = 0, len = my_score.split(",").length; i < len; i++) {
                String split = my_score.split(",")[i].toString();
                score += Integer.parseInt(split);
            }
            score = score / my_score.split(",").length;
        }
        return score;
    }
}
