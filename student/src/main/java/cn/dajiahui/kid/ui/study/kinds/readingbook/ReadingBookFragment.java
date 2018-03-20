package cn.dajiahui.kid.ui.study.kinds.readingbook;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.fxtx.framework.anim.AnimUtil;
import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.util.BitmapUtil;
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
import cn.dajiahui.kid.ui.study.view.PointReadView;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.MD5;


/**
 * 点读本
 */


public class ReadingBookFragment extends FxFragment implements
        PointReadView.GetPointReadView,
        ReadingBookActivity.PlayAll {

    protected Bundle bundle;// 用于保存信息以及标志
    public FrameLayout fr_read_show;
    private Timer timer;
    private PointReadView currentPointReadView = null;
    //    private int readingFlag = 0;
    private TextView mTranslate, mShow;
    //    private boolean isPointRead = false;// false 点读 true 连读
    private BeReadingBookPageData beReadingBookPageData;
    private ImageView img_readbook_bg;
    public List<PointReadView> mPointReadViewList = new ArrayList<>();
    public ReadingBookFragment readingBookFragment;
    private List<BeReadingBookPageData> page_data;
    private String media_url;
    private Boolean translateHide = false;//true 隐藏  false 显示

    private double loadWidth, loadHeight, selfHeight, selfWidth;
    private Bitmap mItemBitmap;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {


        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0 && PlayMedia.getPlaying().mediaPlayer != null) {
                int endtime = msg.arg1;
                int currentPosition = PlayMedia.getPlaying().mediaPlayer.getCurrentPosition();
               /*实时在endtime区间内 停止音频播放*/
                if (((endtime - 500) < (currentPosition)) && ((currentPosition) < (endtime + 500))) {
                    PlayMedia.getPlaying().mediaPlayer.stop();
                    PlayMedia.getPlaying().mediaPlayer.reset();
//                    /*播放完毕背景置成红色*/
//                    currentPointReadView.setBackgroundResource(R.drawable.select_readingbook_bg_red);

//                    if (isPointRead == false) {
//                        readingFlag++;
//                        continuousReading();
//                        if (readingFlag > mPointReadViewList.size()) {
//                            currentPointReadView = null;
//                            readingFlag = 0;
//                            return;
//                        }
//                    }
                }
//                Logger.d("实时：" + currentPosition);
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
                    PointReadView pointReadView = new PointReadView(getActivity(), readingBookFragment, i, beReadingBookPageData.getItem().get(i));
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
                    pointReadView.setmPointX((int) (xPoint * loadWidth / selfWidth) );
                    pointReadView.setmPointY((int) (yPoint * loadHeight / selfHeight) );
                    Logger.d(" (xPoint * loadWidth / selfWidth):" +(int) (xPoint * loadWidth / selfWidth) + "    (yPoint * loadHeight / selfHeight)" + (int)(yPoint * loadHeight / selfHeight));

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


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (PlayMedia.getPlaying().mediaPlayer != null) {
                if (PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                    Message msg = Message.obtain();
                    if (currentPointReadView != null) {
                        msg.arg1 = Integer.parseInt(currentPointReadView.getBePlayReadingBook().getEnd_time());
//                        Logger.d("实时获取结束时间" + currentPointReadView.getBePlayReadingBook().getEnd_time());
                    }
                    msg.what = 0;
                    handler.sendMessage(msg); // 发送消息
                }
            }
        }
    };


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
        //参数2：延迟200毫秒发送，参数3：每隔200毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, 200, 200);

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
        mTranslate = getView(R.id.tv_translate);
        mShow = getView(R.id.tv_show);
        img_readbook_bg = getView(R.id.img_readbook_bg);
        mShow.setOnClickListener(onClick);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (PlayMedia.getPlaying().mediaPlayer != null &&
                PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
            PlayMedia.getPlaying().mediaPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (PlayMedia.getPlaying().mediaPlayer != null &&
                PlayMedia.getPlaying().mediaPlayer.isPlaying()) {

            PlayMedia.getPlaying().mediaPlayer.pause();
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

    private Boolean allowPointRead = false;// 控制动画结束后再点击

    /*点读点击事件*/
    @Override
    public void getPointReadView(PointReadView pointReadView, BeReadingBookPageDataItem bePlayReadingBook, MotionEvent event) {
        mShow.setVisibility(View.VISIBLE);
//        Toast.makeText(getActivity(), "点读", Toast.LENGTH_SHORT).show();
        if (!allowPointRead) {
            for (int i = 0; i < mPointReadViewList.size(); i++) {
                if (mPointReadViewList.get(i) == pointReadView) {
                    this.currentPointReadView = pointReadView;
                    /*设置中文翻译*/
                    mTranslate.setText(this.currentPointReadView.getBePlayReadingBook().getChinese());
                    /*设置放大动画*/
                    Matrix matrix = new Matrix();
                    matrix.postScale(5, 5);// 缩放比例
                    final Bitmap mScaleBitmap = BitmapUtil.createBitmap(mItemBitmap, pointReadView.mPointX, pointReadView.mPointY, pointReadView.mPointViewWidth, pointReadView.mPointViewHeight, matrix, true);
                    final ImageView scaleimageView = new ImageView(getActivity());
                    scaleimageView.setImageBitmap(mScaleBitmap);

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(pointReadView.mPointViewWidth, pointReadView.mPointViewHeight);
                    /*设置动画显示位置*/
                    params.setMargins(pointReadView.mPointX, pointReadView.mPointY, 0, 0);

                    Logger.d("pointReadView.mPointX:" + pointReadView.mPointX + "  pointReadView.mPointY" + pointReadView.mPointY);

                    scaleimageView.setLayoutParams(params);
                   /*添加view到父布局上*/
                    fr_read_show.addView(scaleimageView);
                    allowPointRead = !allowPointRead;
                    AnimUtil.magnifyingAnimation(scaleimageView).
                            setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    fr_read_show.removeView(scaleimageView);
                                    allowPointRead = !allowPointRead;
                                    if (mScaleBitmap != null) {
                                        mScaleBitmap.recycle();
                                    }
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });

                }
            }

            /*文件名以MD5加密*/
            String mp3Name = MD5.getMD5(media_url.substring(media_url.lastIndexOf("/"))) + ".mp3";

            if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
            /*读取本地*/
                PlayMedia.getPlaying().Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, Integer.parseInt(bePlayReadingBook.getStart_time()));

            } else {
             /*读取网络*/
                PlayMedia.getPlaying().Mp3seekTo(media_url, Integer.parseInt(bePlayReadingBook.getStart_time()));
            }
        }
    }

    /*点击事件监听*/
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!translateHide) {
                mTranslate.setVisibility(View.INVISIBLE);
                mShow.setText("显示翻译");
                setPictureDirection(R.drawable.readbook_down, 2);

                translateHide = !translateHide;
            } else {
                mTranslate.setVisibility(View.VISIBLE);
                mShow.setText("隐藏翻译");
                setPictureDirection(R.drawable.readbook_up, 1);
                translateHide = !translateHide;
            }
        }
    };

    /*上设置图片方向 1向上 2.向下*/
    private void setPictureDirection(int picture, int dir) {
        switch (dir) {
            case 1:
                Drawable drawableUp = getActivity().getResources().getDrawable(picture);
                drawableUp.setBounds(0, 0, drawableUp.getMinimumWidth(), drawableUp.getMinimumHeight());
                mShow.setCompoundDrawables(null, null, drawableUp, null);
                break;
            case 2:
                Drawable drawableDown = getActivity().getResources().getDrawable(picture);
                drawableDown.setBounds(0, 0, drawableDown.getMinimumWidth(), drawableDown.getMinimumHeight());
                mShow.setCompoundDrawables(null, null, drawableDown, null);
                break;
            default:
                break;

        }

    }

    /*连读点击事件*/
    @Override
    public void playAll() {
//        /*循环标记置0*/
//        readingFlag = 0;
//        isPointRead = false;
//        /*保证播放环境*/
//        if (PlayMedia.getPlaying().mediaPlayer != null && PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
//            PlayMedia.getPlaying().mediaPlayer.stop();
//        }
//        continuousReading();
    }


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
//                PlayMedia.getPlaying().Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, Integer.parseInt(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStart_time()));
//
//            } else {
//              /*读取网络*/
//                PlayMedia.getPlaying().Mp3seekTo(media_url, Integer.parseInt(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStart_time()));
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
//            if (PlayMedia.getPlaying().mediaPlayer != null) {
//                PlayMedia.getPlaying().mediaPlayer.stop();
//                PlayMedia.getPlaying().mediaPlayer.release();
//                PlayMedia.getPlaying().mediaPlayer = null;
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

