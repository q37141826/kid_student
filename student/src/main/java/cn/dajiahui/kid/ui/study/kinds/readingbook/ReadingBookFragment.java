package cn.dajiahui.kid.ui.study.kinds.readingbook;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.util.BaseUtil;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.io.File;
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
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.LazyLoadFragment;
import cn.dajiahui.kid.ui.study.view.PointReadView;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;


/**
 * 点读本
 */

/*点读本 读的句子的时间对不上 */

public class ReadingBookFragment extends LazyLoadFragment implements View.OnTouchListener,
        PointReadView.GetPointReadView,
        ReadingBookActivity.PlayAll {

    protected Bundle bundle;// 用于保存信息以及标志
    private Bitmap bitmapItem;
    public FrameLayout fr_read_show;
    public Bitmap ScaleBitmap;

    private Timer timer;
    private PointReadView currentPointReadView = null;
    private int readingFlag = 0;
    private TextView mTranslate;
    private boolean isPointRead = false;// false 点读 true 连读

    private BeReadingBookPageData beReadingBookPageData;
    private ImageView img_readbook_bg;
    public List<PointReadView> mPointReadViewList = new ArrayList<>();

    public ReadingBookFragment readingBookFragment;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        private double loadWidth, loadHeight, selfHeight, selfWidth;

        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0 && PlayMedia.getPlaying().mediaPlayer != null) {
                int endtime = msg.arg1;
                int currentPosition = PlayMedia.getPlaying().mediaPlayer.getCurrentPosition();
               /*实时在endtime区间内 停止音频播放*/
                if (((endtime - 200) < (currentPosition)) && ((currentPosition) < (endtime + 200))) {
                    PlayMedia.getPlaying().mediaPlayer.stop();
                    PlayMedia.getPlaying().mediaPlayer.reset();
                    /*播放完毕背景置成红色*/
                    currentPointReadView.setBackgroundResource(R.drawable.select_readingbook_bg_red);

                    if (isPointRead == false) {
                        readingFlag++;
                        continuousReading();
                        Logger.d("实时：" + readingFlag);
                        if (readingFlag > mPointReadViewList.size()) {
                            currentPointReadView = null;
                            readingFlag = 0;
                            return;
                        }
                    }
                }
                Logger.d("实时：" + currentPosition);
            }
            if (msg.what == 1) {
                ImgSelf_W_H imgW_h = (ImgSelf_W_H) msg.obj;
                selfWidth = imgW_h.getWidth();
                selfHeight = imgW_h.getHeight();
            }
            if (msg.what == 2) {
                ImgLoad_W_H imgW_h = (ImgLoad_W_H) msg.obj;
                loadWidth = imgW_h.getWidth();
                loadHeight = imgW_h.getHeight();
                Logger.d("加载之后的:  loadWidth" + loadWidth + "  loadHeight:" + loadHeight);
                Message message = Message.obtain();
                message.what = 3;
                handler.sendMessage(message);

            }
            if (msg.what == 3) {
                double phoneWidth = BaseUtil.getPhoneWidth(getActivity());
                Logger.d("phoneWidth:" + phoneWidth);

                Logger.d("selfWidth:" + selfWidth + "  selfHeight:" + selfHeight + "  loadWidth:" + loadWidth + "  loadHeight:" + loadHeight);


                for (int i = 0; i < beReadingBookPageData.getItem().size(); i++) {

                    PointReadView pointReadView = new PointReadView(getActivity(), readingBookFragment, i, beReadingBookPageData.getItem().get(i));
                    double v1 = Integer.parseInt(beReadingBookPageData.getItem().get(i).getWidth());
                    double vw = v1 * loadWidth / selfWidth;
                    double v2 = Integer.parseInt(beReadingBookPageData.getItem().get(i).getHeight());
                    double vh = v2 * loadHeight / selfHeight;
                    Logger.d("框框宽 getWidth :" + v1 + "  框框高getHeight:" + v2);
                    Logger.d("变形后------框框宽 getWidth :" + v1 + "  框框高getHeight:" + v2);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) vw, (int) vh);
                    double x = Double.parseDouble(beReadingBookPageData.getItem().get(i).getPoint_x());
                    double y = Double.parseDouble(beReadingBookPageData.getItem().get(i).getPoint_y());
                    double vx = x * loadWidth / selfWidth;
                    double vy = y * loadHeight / selfHeight;
                    Logger.d("变形后------框框X点  :" + x + "  框框Y点 :" + y);
                    params.setMargins((int) vx, (int) vy, 0, 0);
                    pointReadView.setLayoutParams(params);
                    mPointReadViewList.add(pointReadView);
                    fr_read_show.addView(pointReadView);
                }
            }
        }

    };
    private List<BeReadingBookPageData> page_data;
    private int position;
    private String media_url;

    /*下载点读本mp3*/
    private void downloadReadingBook() {
        Logger.d("下载----downloadKaraOkeMp3----");

        BeDownFile file = new BeDownFile(Constant.file_pointreading, media_url, "", KidConfig.getInstance().getPathTemp());

        new DownloadFile((ReadingBookActivity) getActivity(), file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                progressDialog.dismiss();
                Logger.d("fileurl:" + fileurl);
            }
        });
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (PlayMedia.getPlaying().mediaPlayer != null) {
                if (PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                    Message msg = Message.obtain();
                    if (currentPointReadView != null) {
                        msg.arg1 = Integer.parseInt(currentPointReadView.getBePlayReadingBook().getEnd_time()) * 1000;
                    }
                    msg.what = 0;
                    handler.sendMessage(msg); // 发送消息
                }
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }

    @Override
    protected int setContentView() {
        return R.layout.fr_read;
    }

    @Override
    protected void lazyLoad() {
        bundle = getArguments();
        initialize();//获取图片显示在ImageView后的宽高
        page_data = (List<BeReadingBookPageData>) bundle.getSerializable("page_data");
        position = bundle.getInt("position");
        beReadingBookPageData = page_data.get(position);

        media_url = beReadingBookPageData.getMedia_url();

        Glide.with(this)
                .load(beReadingBookPageData.getPage_url())
                .asBitmap()//强制Glide返回一个Bitmap对象
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        Logger.d("onException " + e.toString());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        int mReallyWidth = bitmap.getWidth();
                        int mReallyHeight = bitmap.getHeight();
                        Message msg = Message.obtain();
                        msg.obj = new ImgLoad_W_H((float) mReallyWidth, (float) mReallyHeight);
                        msg.what = 2;
                        handler.sendMessage(msg);
                        return false;
                    }
                }).into(img_readbook_bg);


        //参数2：延迟200毫秒发送，参数3：每隔200毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, 200, 200);

        }


         /*获取Mp3视频名称*/
        String sMp3 = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";

       /*判断mp3文件是否下载过*/
        if (!FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + sMp3)) {

            downloadReadingBook();
        }
        readingBookFragment = this;

        //获取图片真正的宽高
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

    /*初始化*/
    private void initialize() {
        fr_read_show = findViewById(R.id.fm_read_show);
        mTranslate = findViewById(R.id.tv_translate);
        img_readbook_bg = findViewById(R.id.img_readbook_bg);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

        Logger.d("-------------onStop()");
        if (PlayMedia.getPlaying().mediaPlayer != null &&
                PlayMedia.getPlaying().mediaPlayer.isPlaying()) {

            PlayMedia.getPlaying().mediaPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("-------------onPause()");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (PlayMedia.getPlaying().mediaPlayer != null &&
                PlayMedia.getPlaying().mediaPlayer.isPlaying()) {

            PlayMedia.getPlaying().mediaPlayer.pause();
        }
    }


    /*点读点击事件*/
    @Override
    public void getPointReadView(PointReadView pointReadView, BeReadingBookPageDataItem bePlayReadingBook) {

        Toast.makeText(getActivity(), "点读", Toast.LENGTH_SHORT).show();

        isPointRead = true;
        for (int i = 0; i < mPointReadViewList.size(); i++) {
            if (mPointReadViewList.get(i) == pointReadView) {
                this.currentPointReadView = pointReadView;
                this.currentPointReadView.setBackgroundResource(R.drawable.select_readingbook_bg_green);
                /*放中文翻译*/
                mTranslate.setText("翻译：" + this.currentPointReadView.getBePlayReadingBook().getChinese());
            } else {
                mPointReadViewList.get(i).setBackgroundResource(R.drawable.select_readingbook_bg_red);
            }

        }

        /*文件名以MD5加密*/
        String mp3Name = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";

        if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
            /*读取本地*/
            PlayMedia.getPlaying().Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, Integer.parseInt(bePlayReadingBook.getStart_time()) * 1000);

        } else {
             /*读取网络*/
            PlayMedia.getPlaying().Mp3seekTo(media_url, Integer.parseInt(bePlayReadingBook.getStart_time()));
        }

    }


    /*连读点击事件*/
    @Override
    public void playAll() {
        /*循环标记置0*/
        readingFlag = 0;
        isPointRead = false;
        /*保证播放环境*/
        if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
            PlayMedia.getPlaying().mediaPlayer.stop();
        }
        continuousReading();
    }

    /*连读*/
    public void continuousReading() {


        for (int i = 0; i < mPointReadViewList.size(); i++) {
            mPointReadViewList.get(i).setBackgroundResource(R.drawable.select_readingbook_bg_red);
            /*设置view不可点击 */
            mPointReadViewList.get(i).setClickable(false);
        }
        if (readingFlag < mPointReadViewList.size()) {

                  /*文件名以MD5加密*/
            String mp3Name = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";
            if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
               /*读取本地*/
                PlayMedia.getPlaying().Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, Integer.parseInt(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStart_time()));

            } else {
              /*读取网络*/
                PlayMedia.getPlaying().Mp3seekTo(media_url, Integer.parseInt(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStart_time()));
            }

            this.currentPointReadView = mPointReadViewList.get(readingFlag);
            /*放中文翻译*/
            mTranslate.setText("翻译：" + mPointReadViewList.get(readingFlag).getBePlayReadingBook().getChinese());
            Toast.makeText(getActivity(), mPointReadViewList.get(readingFlag) + "", Toast.LENGTH_SHORT).show();
            this.currentPointReadView.setBackgroundResource(R.drawable.select_readingbook_bg_green);


        } else {
            currentPointReadView = null;
            for (int i = 0; i < mPointReadViewList.size(); i++) {
                /*解除view不可点击 */
                mPointReadViewList.get(i).setClickable(true);
            }
            if (PlayMedia.getPlaying().mediaPlayer != null) {
                PlayMedia.getPlaying().mediaPlayer.stop();
                PlayMedia.getPlaying().mediaPlayer.release();
                PlayMedia.getPlaying().mediaPlayer = null;
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               /*计算落指点的x y 坐标（图片拉伸变形后）*/
                float fallX = bitmapItem.getWidth() / (v.getWidth() / event.getX());
                float fallY = bitmapItem.getHeight() / (v.getHeight() / event.getY());


//                Matrix matrix = new Matrix();
//                matrix.postScale(5, 5);// 缩放比例
//                ScaleBitmap = BitmapUtil.createBitmap(bitmapItem, (int) (fallX) - 100, (int) (fallY) - 50, 200, 100, matrix, true);
//                scaleimageView = new ImageView(getActivity());
//                scaleimageView.setImageBitmap(ScaleBitmap);
//
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 200);
//                params.leftMargin = (int) event.getX() - 250;
//                params.topMargin = (int) event.getY() - 100;
//
//                scaleimageView.setLayoutParams(params);
//                fr_read_show.addView(scaleimageView);
//
//                AnimUtil.magnifyingAnimation(scaleimageView).setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        fr_read_show.removeAllViews();
//                        scaleimageView = null;
//                        if (ScaleBitmap != null) {
//                            ScaleBitmap.recycle();
//                        }
//                        ((FrameLayout) rootView).refreshDrawableState();
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });

                break;

            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_UP:


                break;
            default:
                break;
        }
        return true;
    }

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

    /*加载之后的宽高*/
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
    }

    /*清空临时文件*/
    private void deleteTemp(String name) {
        FileUtil fileUtil = new FileUtil();
        fileUtil.deleteFile(new File(name));
    }
}

