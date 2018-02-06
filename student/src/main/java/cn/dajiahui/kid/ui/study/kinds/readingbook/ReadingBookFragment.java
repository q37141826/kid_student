package cn.dajiahui.kid.ui.study.kinds.readingbook;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.ui.FxFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BePlayReadingBook;
import cn.dajiahui.kid.ui.study.mediautil.PlayMedia;
import cn.dajiahui.kid.ui.study.view.PointReadView;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;


/**
 * 点读本
 */


public class ReadingBookFragment extends FxFragment implements View.OnTouchListener,
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


    private List<PointReadView> mPointReadViewList;


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
        }

    };


    @Override
    protected View initinitLayout(LayoutInflater inflater) {

        bundle = getArguments();
        String path = (String) bundle.get("path");
        if (!path.equals("")) {
            bitmapItem = BitmapFactory.decodeFile(path);
        }
        return inflater.inflate(R.layout.fr_read, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fr_read_show = getView(R.id.fm_read_show);
        mTranslate = getView(R.id.tv_translate);
        (rootView).setBackground(new BitmapDrawable(this.bitmapItem));

//      ((LinearLayout) rootView).setOnTouchListener(this);
        List<PointReadView> mPointReadViewList = new ArrayList<>();
        /*模拟数据*/
        List<BePlayReadingBook> mData = new ArrayList<>();//
        BePlayReadingBook b1 = new BePlayReadingBook("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3", 10000, 13000, 100, 100, 300, 200);
        BePlayReadingBook b2 = new BePlayReadingBook("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3", 15000, 19000, 400, 400, 300, 200);
        BePlayReadingBook b3 = new BePlayReadingBook("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3", 20000, 25000, 700, 700, 300, 200);
        mData.add(b1);
        mData.add(b2);
        mData.add(b3);


        for (int i = 0; i < mData.size(); i++) {

            PointReadView pointReadView = new PointReadView(getActivity(), this, i, mData.get(i));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mData.get(i).getWidth(), mData.get(i).getHeight());
            params.setMargins(mData.get(i).getStartX(), mData.get(i).getStartY(), 0, 0);
            pointReadView.setLayoutParams(params);
            mPointReadViewList.add(pointReadView);
            fr_read_show.addView(pointReadView);

        }
        this.mPointReadViewList = mPointReadViewList;

        //参数2：延迟300毫秒发送，参数3：每隔300毫秒秒发送一下
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, 200, 200);

        }


    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (PlayMedia.getPlaying().mediaPlayer != null) {
                if (PlayMedia.getPlaying().mediaPlayer.isPlaying()) {
                    Message msg = Message.obtain();
                    if (currentPointReadView != null) {
                        msg.arg1 = currentPointReadView.getBePlayReadingBook().getEndtime();
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
        bitmapItem.recycle();

        System.gc();
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
    public void getPointReadView(PointReadView pointReadView, BePlayReadingBook bePlayReadingBook) {

        isPointRead = true;
        for (int i = 0; i < mPointReadViewList.size(); i++) {
            if (mPointReadViewList.get(i) == pointReadView) {
                this.currentPointReadView = pointReadView;
                this.currentPointReadView.setBackgroundResource(R.drawable.select_readingbook_bg_green);
                /*放中文翻译*/
                mTranslate.setText("翻译："+this.currentPointReadView.getBePlayReadingBook().getStartX() + "");
            } else {
                mPointReadViewList.get(i).setBackgroundResource(R.drawable.select_readingbook_bg_red);
            }

        }

        /*文件名以MD5加密*/
        String mp3Name = MD5.getMD5(bePlayReadingBook.getAudio_url().substring(bePlayReadingBook.getAudio_url().lastIndexOf("/"))) + ".mp3";

        if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
            /*读取本地*/
            PlayMedia.getPlaying().Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, bePlayReadingBook.getStarttime());

        } else {
             /*读取网络*/
            PlayMedia.getPlaying().Mp3seekTo(bePlayReadingBook.getAudio_url(), bePlayReadingBook.getStarttime());
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
            String mp3Name = MD5.getMD5(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getAudio_url().substring(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getAudio_url().lastIndexOf("/"))) + ".mp3";


            if (FileUtil.fileIsExists(KidConfig.getInstance().getPathPointRedaing() + mp3Name)) {
               /*读取本地*/
                PlayMedia.getPlaying().Mp3seekTo(KidConfig.getInstance().getPathPointRedaing() + mp3Name, mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStarttime());

            } else {
              /*读取网络*/
                PlayMedia.getPlaying().Mp3seekTo(mPointReadViewList.get(readingFlag).getBePlayReadingBook().getAudio_url(), mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStarttime());
            }

            this.currentPointReadView = mPointReadViewList.get(readingFlag);
            /*放中文翻译*/
            mTranslate.setText("翻译："+mPointReadViewList.get(readingFlag).getBePlayReadingBook().getStartX() + "");
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



    /*清空临时文件*/
    private void deleteTemp(String name) {
        FileUtil fileUtil = new FileUtil();
        fileUtil.deleteFile(new File(name));
    }

}

