package cn.dajiahui.kid.ui.study;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fxtx.framework.anim.AnimUtil;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.util.BitmapUtil;

import cn.dajiahui.kid.R;


/**
 * 学习
 */


public class ReadFragment extends FxFragment implements View.OnTouchListener {

    protected Bundle bundle;// 用于保存信息以及标志
    private Bitmap bitmapItem;
    public FrameLayout fr_read_show;
    public Bitmap ScaleBitmap;
    public ImageView scaleimageView;

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
        ((FrameLayout) rootView).setBackground(new BitmapDrawable(this.bitmapItem));
        ((FrameLayout) rootView).setOnTouchListener(this);


    }

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
//        Log.d("majin", " ReadFragment onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d("majin", " ReadFragment onPause");
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               /*计算落指点的x y 坐标（图片拉伸变形后）*/
                float fallX = bitmapItem.getWidth() / (v.getWidth() / event.getX());
                float fallY = bitmapItem.getHeight() / (v.getHeight() / event.getY());

//                Log.d("majin", "x1  " + fallX + " y1: " + fallY);
////                /*计算图片（背景数据）拉伸比例*/
//                float W = (float) bitmapItem.getWidth() / (float) v.getWidth();
//                float H = (float) bitmapItem.getHeight() / (float) v.getHeight();
//                Log.d("majin", "W  " + W + " h: " + H);

//                Log.d("majin", "bitmapItem.getWidth()  " + bitmapItem.getWidth() + " bitmapItem.getHeight() : " + bitmapItem.getHeight());
//                Log.d("majin", "v.getWidth()  " + v.getWidth() + " v.getHeight() : " + v.getHeight());


                Matrix matrix = new Matrix();
                matrix.postScale(5, 5);// 缩放比例
                ScaleBitmap = BitmapUtil.createBitmap(bitmapItem, (int) (fallX) - 100, (int) (fallY) - 50, 200, 100, matrix, true);
                scaleimageView = new ImageView(getActivity());
                scaleimageView.setImageBitmap(ScaleBitmap);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 200);
                params.leftMargin = (int) event.getX() - 250;
                params.topMargin = (int) event.getY() - 100;

                scaleimageView.setLayoutParams(params);
                fr_read_show.addView(scaleimageView);

                AnimUtil.magnifyingAnimation(scaleimageView).setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fr_read_show.removeAllViews();
                        scaleimageView = null;
                        if (ScaleBitmap != null) {
                            ScaleBitmap.recycle();
                        }
                        ((FrameLayout) rootView).refreshDrawableState();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_UP:
                fr_read_show.removeAllViews();
                scaleimageView = null;
                if (ScaleBitmap != null) {
                    ScaleBitmap.recycle();
                }
                ((FrameLayout) rootView).refreshDrawableState();
                Log.d("majin", " ACTION_UP");

                break;
            default:
                break;
        }
        return true;
    }


}

