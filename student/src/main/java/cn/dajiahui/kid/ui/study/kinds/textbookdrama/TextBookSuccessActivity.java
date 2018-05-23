package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.platforms.umeng.BeShareContent;
import com.fxtx.framework.platforms.umeng.UmengShare;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.study.bean.BeGoTextBookSuccess;
import cn.dajiahui.kid.ui.study.bean.BePageDataMyWork;
import cn.dajiahui.kid.ui.study.bean.BePageDataWork;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageData;
import cn.dajiahui.kid.ui.study.bean.BeUpdateMIneWorks;
import cn.dajiahui.kid.ui.study.kinds.karaoke.MakeKraoOkeActivity;
import cn.dajiahui.kid.util.DateUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
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

    public static String CLOSE = "";//救急（后续更改）

    private cn.dajiahui.kid.ui.study.kinds.textbookdrama.JCVideoPlayerTextScuessBook mVideoplayer;
    private TextView tvniitname;//作品名称
    private ImageView imguser;//头像
    private TextView tv_username;//名字
    private TextView tvmaketime;//制作时间
    private TextView tvscore;//评分
    private RatingBar rbscore;//小星星
    private TextView tvshare;//
    private ImageView imgweixin;//微信
    private ImageView imgpengyouquan;//朋友圈
    private TextView tvrecordagain;//重新录制
    private TextView tvsavemineworks;//保存我的作品
    private BeGoTextBookSuccess beGoTextBookSuccess;
    private LinearLayout bottomRoot, socre_root, info_root;//底部重新录制，保存到我的作品的父布局
    private RelativeLayout shareRoot, shareRootNotice;//分享父布局 //保存我的作品成功之后显示
    private String makeTextBookDrma;
    private String mineWorksTempPath;//我的作品临时文件夹
    private RelativeLayout tv_savemineworks;
    private String look = "";//已经完成卡拉OK的录制 ，课本剧的录制
    private BePageDataWork beKaraOkPageData;
    private BeTextBookDramaPageData beTextBookDramaPageData;
    private BePageDataMyWork bePageDataMyWork;//作品详情（我的作品页面过来的）
    private UmengShare umengShare;
    private BeShareContent beShareContent;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_text_book_success);
        initialize();
        Intent intent = this.getIntent();
        makeTextBookDrma = intent.getStringExtra("MakeFlag");

        /*表示有显示底部按钮的标志 只有制作卡拉OK 课本剧的时候才显示*/
        if ("SHOW".equals(intent.getStringExtra("ShowBottom"))) {
            /*显示底部按钮*/
            bottomRoot.setVisibility(View.VISIBLE);
        }

        if (makeTextBookDrma.equals("MakeTextBookDrma")) {
            CLOSE = "MakeTextBookDrma";
            /*隐藏分享布局  要分享成功后显示*/
            shareRoot.setVisibility(View.GONE);
            shareRootNotice.setVisibility(View.VISIBLE);
            beGoTextBookSuccess = (BeGoTextBookSuccess) intent.getSerializableExtra("BeGoTextBookSuccess");
            int score = 0;
            for (int i = 0; i < beGoTextBookSuccess.getmScoreMap().size(); i++) {
                score += beGoTextBookSuccess.getmScoreMap().get(i);
            }
            int average = score / beGoTextBookSuccess.getmScoreMap().size();
            tvscore.setText(average + "分");
            rbscore.setMax(100);
            rbscore.setProgress(average);

            playVideo(beGoTextBookSuccess.getMineWorksTempPath());
            settingInfo(beGoTextBookSuccess.getMineWorksName(), UserController.getInstance().getUser().getAvatar(),
                    UserController.getInstance().getUser().getNickname(), (beGoTextBookSuccess.getMakeTime()) + "");


        } else if (makeTextBookDrma.equals("MakeKraoOke")) {
            CLOSE = "MakeKraoOke";
            /*显示分享布局  要分享成功后显示*/
            shareRoot.setVisibility(View.GONE);
            shareRootNotice.setVisibility(View.VISIBLE);

            beGoTextBookSuccess = (BeGoTextBookSuccess) intent.getSerializableExtra("BeGoTextBookSuccess");

            /*隐藏打分*/
            socre_root.setVisibility(View.GONE);
            playVideo(beGoTextBookSuccess.getMineWorksTempPath());
            settingInfo(beGoTextBookSuccess.getMineWorksName(), UserController.getInstance().getUser().getAvatar(),
                    UserController.getInstance().getUser().getNickname(), (beGoTextBookSuccess.getMakeTime()) + "");

        } else if (makeTextBookDrma.equals("seedetails")) {/*卡拉ok已经唱完 查看详情*/

            /*卡拉OK查看*/
            look = intent.getStringExtra("Look");

            if (look.equals("kalaok")) {/*已经唱完的kalaok*/
                beKaraOkPageData = (BePageDataWork) intent.getSerializableExtra("BePageDataWork");
                /*隐藏保存我的作品按钮*/
                tv_savemineworks.setVisibility(View.GONE);
                /*显示分享按钮*/
                shareRoot.setVisibility(View.VISIBLE);
                /*隐藏打分*/
                socre_root.setVisibility(View.GONE);
                /*设置名字和时间横向显示*/
                info_root.setHorizontalGravity(LinearLayout.HORIZONTAL);

                /*播放视频*/
                playVideo(beKaraOkPageData.getMy_work().getVideo());
                /*设置信息*/
                settingInfo(beKaraOkPageData.getMy_work().getTitle(), UserController.getInstance().getUser().getAvatar(),
                        UserController.getInstance().getUser().getNickname(), Long.parseLong(beKaraOkPageData.getMy_work().getDate()) * 1000 + "");//)
                /*设置分享信息*/
                setShareContent(beKaraOkPageData.getMy_work().getShare_url(), "魔耳英语作品分享", beKaraOkPageData.getMy_work().getThumbnail(), "我分享了" + beKaraOkPageData.getMy_work().getAuthor() + "的作品，快来看看吧！");
            } else if (look.equals("textbook")) {/*已经制作玩的课本剧*/
                /*数据模型*/
                beTextBookDramaPageData = (BeTextBookDramaPageData) intent.getSerializableExtra("BeTextBookDramaPageData");
                /*隐藏保存我的作品按钮*/
                tv_savemineworks.setVisibility(View.GONE);
                /*显示分享按钮*/
                shareRoot.setVisibility(View.VISIBLE);
                /*播放视频*/
                playVideo(beTextBookDramaPageData.getMy_work().getVideo());
                /*设置信息*/
                settingInfo(beTextBookDramaPageData.getMy_work().getTitle(), UserController.getInstance().getUser().getAvatar(),
                        UserController.getInstance().getUser().getNickname(), Long.parseLong(beTextBookDramaPageData.getMy_work().getDate()) * 1000 + "");//DateUtils.time(beKaraOkPageDataMyWork.getDate())
                /*获取平均分*/
                tvscore.setText(getAverage(beTextBookDramaPageData.getMy_work().getScore()) + "分");
                rbscore.setMax(100);
                rbscore.setProgress(getScore(getAverage(beTextBookDramaPageData.getMy_work().getScore())));

                /*设置分享信息*/
                setShareContent(beTextBookDramaPageData.getMy_work().getShare_url(), "魔耳英语作品分享", beTextBookDramaPageData.getMy_work().getThumbnail(), "我分享了" + beTextBookDramaPageData.getMy_work().getAuthor() + "的作品，快来看看吧！");
            }
        } else if (makeTextBookDrma.equals("LookTextBookDrma")) {/*由我的作品查看 课本剧*/
            bePageDataMyWork = (BePageDataMyWork) intent.getSerializableExtra("BePageDataMyWork");
            /*显示分享按钮*/
            shareRoot.setVisibility(View.VISIBLE);
            /*播放视频*/
            playVideo(bePageDataMyWork.getVideo());
            /*设置信息*/
            settingInfo(bePageDataMyWork.getTitle(), UserController.getInstance().getUser().getAvatar(),
                    UserController.getInstance().getUser().getNickname(), (Long.parseLong(bePageDataMyWork.getDate()) * 1000) + "");
            /*获取平均分*/
            tvscore.setText(getAverage(bePageDataMyWork.getScore()) + "分");
            rbscore.setMax(100);
            rbscore.setProgress(getScore(getAverage(bePageDataMyWork.getScore())));

            /*设置分享信息*/
            setShareContent(bePageDataMyWork.getShare_url(), "魔耳英语作品分享", bePageDataMyWork.getThumbnail(), "我分享了" + bePageDataMyWork.getAuthor() + "的作品，快来看看吧！");

        } else if (makeTextBookDrma.equals("LookKalaOk")) {/*由我的作品查看 kalaok*/
            bePageDataMyWork = (BePageDataMyWork) intent.getSerializableExtra("BePageDataMyWork");
            /*隐藏打分*/
            socre_root.setVisibility(View.GONE);
            /*显示分享按钮*/
            shareRoot.setVisibility(View.VISIBLE);
            /*播放视频*/
            playVideo(bePageDataMyWork.getVideo());
            /*设置信息*/
            settingInfo(bePageDataMyWork.getTitle(), UserController.getInstance().getUser().getAvatar(),
                    UserController.getInstance().getUser().getNickname(), (Long.parseLong(bePageDataMyWork.getDate()) * 1000 + ""));

            /*设置分享信息*/
            setShareContent(bePageDataMyWork.getShare_url(), "魔耳英语作品分享", bePageDataMyWork.getThumbnail(), "我分享了" + bePageDataMyWork.getAuthor() + "的作品，快来看看吧！");
        }
    }

    /*播放視頻*/
    private void playVideo(String videopath) {
        mVideoplayer.setUp(videopath, JCVideoPlayer.SCREEN_LAYOUT_LIST);
        mVideoplayer.startVideo();
        mVideoplayer.hideView();//隐藏不需要的view
    }

    /*設置信息*/
    private void settingInfo(String unitName, String imgUrl, String author, String makeTime) {
        tvniitname.setText(unitName);
        /*加载圆形图片*/
        GlideUtil.showRoundImage(TextBookSuccessActivity.this, imgUrl, imguser, R.drawable.ico_default_user, true);
        tv_username.setText(author);
        tvmaketime.setText(DateUtils.getYyyyMMDD(makeTime));
        Logger.d("作预览品时间----makeTime：" + makeTime);
    }

    /*初始化*/
    private void initialize() {
        mVideoplayer = getView(R.id.videoplayer);
        tvniitname = getView(R.id.tv_unitname);
        imguser = getView(R.id.img_user);
        tv_username = getView(R.id.tv_username);
        tvmaketime = getView(R.id.tv_maketime);
        tvscore = getView(R.id.tv_fraction);
        rbscore = getView(R.id.rb_score);
        tvshare = getView(R.id.tv_share);
        bottomRoot = getView(R.id.bottomRoot);
        shareRootNotice = getView(R.id.share_root_notice);
        shareRoot = getView(R.id.share_root);
        socre_root = getView(R.id.socre_root);
        info_root = getView(R.id.info_root);
        getView(R.id.img_weixin).setOnClickListener(onClick);
        getView(R.id.img_pengyouquan).setOnClickListener(onClick);
        getView(R.id.img_qq).setOnClickListener(onClick);
        getView(R.id.tv_recordagain).setOnClickListener(onClick);
        tv_savemineworks = getView(R.id.tv_savemineworks);
        tv_savemineworks.setOnClickListener(onClick);
        umengShare = new UmengShare();
        beShareContent = new BeShareContent();
    }

    /*设置分享信息*/
    private void setShareContent(String shareUrl, String shareTitle, String shareThumbRes, String shareConcent) {
        beShareContent.setShareUrl(shareUrl);
        beShareContent.setShareTitle(shareTitle);
        beShareContent.setThumbRes(shareThumbRes);
        beShareContent.setShareContent(shareConcent);

    }

    /*点击事件*/
    private View.OnClickListener onClick = new View.OnClickListener() {


        private StringBuffer append;

        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.img_weixin:
                    umengShare.share(TextBookSuccessActivity.this, beShareContent).setPlatform(SHARE_MEDIA.WEIXIN).share();
                    break;
                case R.id.img_pengyouquan:
                    umengShare.share(TextBookSuccessActivity.this, beShareContent).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                    break;
                case R.id.img_qq:
                    umengShare.share(TextBookSuccessActivity.this, beShareContent).setPlatform(SHARE_MEDIA.QQ).share();
                    break;

                case R.id.tv_recordagain:


                    if (!look.equals("")) {
                        if (look.equals("kalaok")) {/*跳转制作卡拉OK activity*/
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("BePageDataWork", beKaraOkPageData);
                            DjhJumpUtil.getInstance().startBaseActivity(TextBookSuccessActivity.this, MakeKraoOkeActivity.class, bundle, 0);
                            finishActivity();
                            break;
                        } else if (look.equals("textbook")) {/*跳转制作课本剧 activity*/

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("BeTextBookDramaPageData", beTextBookDramaPageData);
                            DjhJumpUtil.getInstance().startBaseActivity(TextBookSuccessActivity.this, MakeTextBookDrmaActivity.class, bundle, 0);
                            finishActivity();

                            break;
                        }
                    } else {
                        if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                            setResult(1);/*重新录制课本剧*/
                        } else if (makeTextBookDrma.equals("MakeKraoOke")) {
                            setResult(DjhJumpUtil.getInstance().activity_makekalaok_result);/*重新录制卡拉OK*/
                        }

                    }
                    finishActivity();

                    break;
                case R.id.tv_savemineworks:

                    mineWorksTempPath = beGoTextBookSuccess.getMineWorksTempPath();

                    if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                        showfxDialog("文件上传中，请稍等...");

                        String sTextBookDrma = mineWorksTempPath.substring(mineWorksTempPath.lastIndexOf("/") + 1);
                        /*复制文件到 PathMineWorks*/
                        FileUtil.copyFile(beGoTextBookSuccess.getMineWorksTempPath(), KidConfig.getInstance().getPathMineWorksTextBookDrama() + sTextBookDrma);

                        StringBuffer sb = new StringBuffer();
                        if (beGoTextBookSuccess.getmScoreMap().size() > 1) {
                            /*拼接分数 用逗号隔开，方便与以后拓展*/
                            for (int i = 1; i < beGoTextBookSuccess.getmScoreMap().size(); i++) {
                                append = sb.append("," + beGoTextBookSuccess.getmScoreMap().get(i));
                            }
                        } else {
                            append = sb;
                        }


                        /*上传卡本剧*/
                        RequestUtill.getInstance().httpSaveMineWorks(TextBookSuccessActivity.this, callMineWorksUp,
                                beGoTextBookSuccess.getPage_id(),
                                beGoTextBookSuccess.getMakeTime() / 1000,
                                KidConfig.getInstance().getPathMineWorksTextBookDrama() + sTextBookDrma,
                                KidConfig.getInstance().getPathMineWorksThumbnail() + sTextBookDrma.substring(0, sTextBookDrma.lastIndexOf(".")) + ".png",
                                beGoTextBookSuccess.getmScoreMap().get(0) + append.toString(),
                                "", beGoTextBookSuccess.getUserName());

                    } else if (makeTextBookDrma.equals("MakeKraoOke")) {

                        showfxDialog("文件上传中，请稍等...");

                        String sKraoOke = mineWorksTempPath.substring(mineWorksTempPath.lastIndexOf("/"));

                        /*复制文件到 PathMineWorks*/
                        FileUtil.copyFile(beGoTextBookSuccess.getMineWorksTempPath(), KidConfig.getInstance().getPathMineWorksKaraOke() + sKraoOke);

                        /*上传卡拉OK*/
                        RequestUtill.getInstance().httpSaveMineWorks(TextBookSuccessActivity.this, callMineWorksUp,
                                beGoTextBookSuccess.getPage_id(),
                                beGoTextBookSuccess.getMakeTime() / 1000,
                                KidConfig.getInstance().getPathMineWorksKaraOke() + sKraoOke,
                                KidConfig.getInstance().getPathMineWorksThumbnail() + sKraoOke.substring(0, sKraoOke.lastIndexOf(".")) + ".png", "", "",
                                beGoTextBookSuccess.getUserName());
                    }
//                    Toast.makeText(context, "保存至我的作品", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /*上传我的作品回调*/
    ResultCallback callMineWorksUp = new ResultCallback() {

        private BeUpdateMIneWorks beUpdateMIneWorks;

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
            shareRootNotice.setVisibility(View.GONE);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                beUpdateMIneWorks = json.parsingObject(BeUpdateMIneWorks.class);
                if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                    CLOSE = "MakeTextBookDrmaSuccess";
                    /*设置分享信息*/
                    setShareContent(beUpdateMIneWorks.getShareUrl(), "魔耳英语作品分享", beUpdateMIneWorks.getThumbnail(), beGoTextBookSuccess.getUserName() + "的魔耳英语课本剧配音，快来看看吧！");
                } else if (makeTextBookDrma.equals("MakeKraoOke")) {
                    CLOSE = "MakeKraoOkeSuccess";
                    /*设置分享信息*/
                    setShareContent(beUpdateMIneWorks.getShareUrl(), "魔耳英语作品分享", beUpdateMIneWorks.getThumbnail(), beGoTextBookSuccess.getUserName() + "的魔耳英语卡啦OK配音，快来看看吧！");
                }
                Logger.d("卡拉OK上传成功" + response);
                /*隐藏保存我的作品按钮*/
                tv_savemineworks.setVisibility(View.GONE);
            } else {
                ToastUtil.showToast(TextBookSuccessActivity.this, json.getMsg());
            }


        }

        @Override
        public void inProgress(float progress) {
//            Logger.d("作品上传中" + progress);

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
//        Logger.d("TextBookSuccessActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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
//        String my_score = beTextBookDramaPageData.getMy_work().getScore();
        if (!my_score.equals("")) {
            /*截取字符串*/
            for (int i = 0, len = my_score.split(",").length; i < len; i++) {
                String split = my_score.split(",")[i].toString();
                score += Integer.parseInt(split);
            }
            score = score / my_score.split(",").length;
        }
        return score;
    }


    /*监听返回键*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //监控/拦截/屏蔽返回键
            if (makeTextBookDrma.equals("MakeKraoOke")) {
                setResult(DjhJumpUtil.getInstance().activity_makekalaok_out);
            } else if (makeTextBookDrma.equals("MakeTextBookDrma")) {
                setResult(DjhJumpUtil.getInstance().activity_makebookdrame_out);
            }

            finishActivity();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }


}
