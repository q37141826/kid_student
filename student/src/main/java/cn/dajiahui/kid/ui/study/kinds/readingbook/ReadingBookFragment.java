package cn.dajiahui.kid.ui.study.kinds.readingbook;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.study.bean.BeReadingBookPageData;
import cn.dajiahui.kid.ui.study.bean.BeReadingBookPageDataItem;
import cn.dajiahui.kid.ui.study.view.PointReadView;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;


/**
 * 点读本
 */


public class ReadingBookFragment extends FxFragment implements
        PointReadView.GetPointReadView, ReadingBookActivity.PointReadingSettings {

    protected Bundle bundle;// 用于保存信息以及标志
    public FrameLayout fr_read_show;
    private Timer timer;
    private PointReadView currentPointReadView = null;
    private TextView mTranslate;
    private BeReadingBookPageData beReadingBookPageData;
    private ImageView img_readbook_bg;
    public List<PointReadView> mPointReadViewList = new ArrayList<>();
    public ReadingBookFragment readingBookFragment;
    private List<BeReadingBookPageData> page_data;
    private String media_url;
    private Boolean translateHide = false;//true 隐藏  false 显示
    private double loadWidth, loadHeight, selfHeight, selfWidth;
    private Bitmap mItemBitmap;
    private MediaPlayer mediaPlayer;

    //    private boolean showEnglish = false;//显示英文  false 不显示  true 显示中文
    private int showLanguage = 0;//0 中文 1 英文

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0 && mediaPlayer != null) {
                int endtime = msg.arg1;
                int currentPosition = mediaPlayer.getCurrentPosition();
                Logger.d("点读本实时音频：" + currentPosition);
                /*实时在endtime区间内 停止音频播放*/
                if (((endtime - 100) < (currentPosition)) && ((currentPosition) < (endtime + 200))) {
                    /*停止音频播放*/
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    timer.cancel();
                    timer = null;
                    /*清除动画*/
                    currentPointReadView.cleanAnimotion();
                }
            }
            if (msg.what == 1) {/*获取图片原始的尺寸*/

                ImgSelf_W_H imgW_h = (ImgSelf_W_H) msg.obj;
                selfWidth = imgW_h.getWidth();
                selfHeight = imgW_h.getHeight();

                //获取图片记载之后的尺寸
                Glide.with(ReadingBookFragment.this).load(beReadingBookPageData.getPage_url()).asBitmap()//强制Glide返回一个Bitmap对象
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap bitmap, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mItemBitmap = bitmap;
                                int mReallyWidth = bitmap.getWidth();
                                int mReallyHeight = bitmap.getHeight();
                                Message msg = Message.obtain();
                                msg.obj = new ImgLoad_W_H((float) mReallyWidth, (float) mReallyHeight);
                                msg.what = 2;
                                handler.sendMessage(msg);
                                return false;
                            }
                        }).into(img_readbook_bg);
            }
            if (msg.what == 2) {/*获取图片加载成功之后的尺寸*/
                ImgLoad_W_H imgW_h = (ImgLoad_W_H) msg.obj;
                loadWidth = imgW_h.getWidth();
                loadHeight = imgW_h.getHeight();
                Message message = Message.obtain();
                message.what = 3;
                handler.sendMessage(message);

            }
            if (msg.what == 3) {/*计算拉伸比例*/
                for (int i = 0; i < beReadingBookPageData.getItem().size(); i++) {
                    PointReadView pointReadView = new PointReadView(getActivity(), readingBookFragment, i, beReadingBookPageData.getItem().get(i), fr_read_show);
                    double width = Integer.parseInt(beReadingBookPageData.getItem().get(i).getWidth());
                    double height = Integer.parseInt(beReadingBookPageData.getItem().get(i).getHeight());
                    /*计算画框的宽高*/
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            (int) (width * loadWidth / selfWidth),
                            (int) (height * loadHeight / selfHeight));
                    /*设置点读view放大图片的宽高*/
                    pointReadView.setmPointViewWidth((int) (width * loadWidth / selfWidth));
                    pointReadView.setmPointViewHeight((int) (height * loadHeight / selfHeight));
                    /*计算框的位置 x y点坐标*/
                    double xPoint = Double.parseDouble(beReadingBookPageData.getItem().get(i).getPoint_x());
                    double yPoint = Double.parseDouble(beReadingBookPageData.getItem().get(i).getPoint_y());
                    /*设置点读View的x y点坐标*/
                    pointReadView.setmPointX((int) (xPoint * loadWidth / selfWidth));
                    pointReadView.setmPointY((int) (yPoint * loadHeight / selfHeight));
                    params.setMargins(
                            (int) (xPoint * loadWidth / selfWidth),
                            (int) (yPoint * loadHeight / selfHeight), 0, 0);
                    /*设置点读view（框框的位置）*/
                    pointReadView.setLayoutParams(params);
                    mPointReadViewList.add(pointReadView);
                    /*添加点读view到父布局上*/
                    fr_read_show.addView(pointReadView);

                }
            }
        }

    };
    private RelativeLayout mTranslateBottomroot;

    /*下载点读本mp3*/
    private void downloadReadingBook() {
        BeDownFile file = new BeDownFile(Constant.file_pointreading, media_url, "", KidConfig.getInstance().getPathTemp());

        new DownloadFile((ReadingBookActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                progressDialog.dismiss();
//                Logger.d("fileurl:" + fileurl);
            }
        });
    }

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_read, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();
        initialize();//获取图片显示在ImageView后的宽高

        page_data = (List<BeReadingBookPageData>) bundle.getSerializable("page_data");
        this.showLanguage = bundle.getInt("showLanguage", -1);
        Logger.d("showLanguage----------" + showLanguage);

        beReadingBookPageData = page_data.get(bundle.getInt("position"));
        loadImageView();
        media_url = beReadingBookPageData.getMedia_url();
        if (media_url.length() > 0) {
            /*获取Mp3视频名称*/
            String sMp3 = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";
            /*判断mp3文件是否下载过*/
            if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + sMp3)) {
                downloadReadingBook();
            }

        }
        readingBookFragment = this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }

    /*初始化*/
    private void initialize() {
        fr_read_show = getView(R.id.fm_read_show);
        mTranslateBottomroot = getView(R.id.translate_bottomroot);
        mTranslate = getView(R.id.tv_translate);
        img_readbook_bg = getView(R.id.img_readbook_bg);
        mTranslate.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                /*清除动画*/
                currentPointReadView.cleanAnimotion();
            }
        }
    }

    /*加载网络图片*/
    private void loadImageView() {
        //获取图片自己本身的尺寸
        Glide.with(this)
                .load(beReadingBookPageData.getPage_url())
                .asBitmap()//强制Glide返回一个Bitmap对象
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        int mSelfWidth = bitmap.getWidth();
                        int mSelfHeight = bitmap.getHeight();
                        Message msg = Message.obtain();
                        msg.obj = new ImgSelf_W_H((float) mSelfWidth, (float) mSelfHeight);
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });


    }


    /*点读点击事件*/
    @Override
    public void getPointReadView(PointReadView pointReadView, BeReadingBookPageDataItem bePlayReadingBook) {
        /*清除动画*/
        for (int i = 0; i < mPointReadViewList.size(); i++) {
            mPointReadViewList.get(i).cleanAnimotion();
        }
//        /*显示翻译*/
//        mShow.setVisibility(View.VISIBLE);

        if (this.showLanguage == 0) {
            /*设置中文翻译*/
            mTranslate.setText(pointReadView.getBePlayReadingBook().getChinese());
        } else {
            /*设置英文翻译*/
            mTranslate.setText(pointReadView.getBePlayReadingBook().getEnglish());
        }


        this.currentPointReadView = pointReadView;
        /*开启动画*/
        pointReadView.startAnimotion(mItemBitmap);
        /*播放音频文件*/
        if (!media_url.equals("")) {
            /*文件名以MD5加密*/
            String mp3Name = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";

            if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
                /*读取本地*/
                Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, Integer.parseInt(bePlayReadingBook.getStart_time()));

            } else {
                /*读取网络*/
                Mp3seekTo(media_url, Integer.parseInt(bePlayReadingBook.getStart_time()));
            }
        }
//        else {
//            Toast.makeText(getActivity(), "请在PC端上传音频文件", Toast.LENGTH_SHORT).show();
//        }

    }
    /*指定播放位置 毫秒*/

    public void Mp3seekTo(String mp3path, int starttime) {
//        Logger.d("播放地址：" + mp3path);
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mp3path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(starttime);
//            Logger.d("-----------跳转到开始时间：" + starttime);
//            Logger.d("-----------跳转到开始时间之后实时获取：" + mediaPlayer.getCurrentPosition());
            /*设置监听事件*/
            setListener();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setListener() {
        /*准备播放*/
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                //参数2：延迟200毫秒发送，参数3：每隔200毫秒秒发送一下
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            if (currentPointReadView != null) {
                                msg.arg1 = Integer.parseInt(currentPointReadView.getBePlayReadingBook().getEnd_time());
//                        Logger.d("实时获取结束时间" + currentPointReadView.getBePlayReadingBook().getEnd_time());
                            }
                            msg.what = 0;
                            handler.sendMessage(msg); // 发送消息

                        }
                    }, 200, 200);
                }
            }

        });
        /*播放完成*/

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Logger.d("播放完成：" + complete);
                /*释放资源*/
                mediaPlayer.stop();

            }
        });

        /*播放失败*/
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });


    }


    /*点读设置*/
    @SuppressLint("Range")
    @Override
    public void pointReadingSettings(int flag) {

        this.showLanguage = flag;

        if (currentPointReadView != null) {
            if (this.showLanguage == 0) {
                /*设置中文翻译*/
                mTranslate.setText(currentPointReadView.getBePlayReadingBook().getChinese());
            } else {
                /*设置英文翻译*/
                mTranslate.setText(currentPointReadView.getBePlayReadingBook().getEnglish());
            }
        }
    }


    public void showmTranslateBottomroot() {
        mTranslateBottomroot.setVisibility(View.VISIBLE);
    }

    public void hidemTranslateBottomroot() {
        mTranslateBottomroot.setVisibility(View.GONE);
    }

//    /*上设置图片方向 1向上 2.向下*/
//    private void setPictureDirection(int picture, int dir) {
//        switch (dir) {
//            case 1:
//                Drawable drawableUp = getActivity().getResources().getDrawable(picture);
//                drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
//                mShow.setCompoundDrawables(null, null, drawableUp, null);
//                break;
//            case 2:
//                Drawable drawableDown = getActivity().getResources().getDrawable(picture);
//                drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(), drawableDown.getMinimumHeight());
//                mShow.setCompoundDrawables(null, null, drawableDown, null);
//                break;
//            default:
//                break;
//
//        }
//
//    }


}


//    /*连读*/
//    public void continuousReading() {
//
//        for (int i = 0; i < mPointReadViewList.size(); i++) {
//            mPointReadViewList.get(i).setBackgroundResource(R.drawable.select_readingbook_bg_red);
//            /*设置view不可点击 */
//            mPointReadViewList.get(i).setClickable(false);
//        }
//        if (readingFlag < mPointReadViewList.size()) {
//
//                  /*文件名以MD5加密*/
//            String mp3Name = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";
//            if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
//               /*读取本地*/
//                  Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, Integer.parseInt(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStart_time()));
//
//            } else {
//              /*读取网络*/
//                  Mp3seekTo(media_url, Integer.parseInt(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStart_time()));
//            }
//
//            this.currentPointReadView = mPointReadViewList.get(readingFlag);
//            /*放中文翻译*/
//            mTranslate.setText(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getChinese());
////            Toast.makeText(getActivity(), mPointReadViewList.get(readingFlag) + "", Toast.LENGTH_SHORT).show();
//            this.currentPointReadView.setBackgroundResource(R.drawable.select_readingbook_bg_green);
//
//
//        } else {
//            currentPointReadView = null;
//            for (int i = 0; i < mPointReadViewList.size(); i++) {
//                /*解除view不可点击 */
//                mPointReadViewList.get(i).setClickable(true);
//            }
//            if (  mediaPlayer != null) {
//                  mediaPlayer.stop();
//                  mediaPlayer.release();
//                  mediaPlayer = null;
//            }
//        }
//    }


/*网络图片自己的宽高*/
class ImgSelf_W_H {
    double Width = 0;
    double height = 0;

    public ImgSelf_W_H(float width, float height) {
        Width = width;
        this.height = height;
    }

    public double getWidth() {
        return Width;
    }

    public double getHeight() {
        return height;
    }
}

/*加载完成的宽高*/
class ImgLoad_W_H {
    double Width = 0;
    double height = 0;

    public ImgLoad_W_H(float width, float height) {
        Width = width;
        this.height = height;
    }

    public double getWidth() {
        return Width;
    }

    public double getHeight() {
        return height;
    }

    /*清空临时文件*/
    private void deleteTemp(String name) {

        FileUtil.deleteFile(new File(name));
    }
}

