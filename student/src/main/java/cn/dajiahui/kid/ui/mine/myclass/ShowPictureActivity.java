package cn.dajiahui.kid.ui.mine.myclass;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;

/*班级空间显示图片*/
public class ShowPictureActivity extends BaseActivity {

    private String mImgUrl;
    private ImageView mImgShow;
    private Drawable mBackground;

    private int mScaleY;
    private int mScaleX;
    private int mTop;
    private int mLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

    }

    @Override
    public void getIntentData() {
        super.getIntentData();

    }

    @Override
    public void initView() {

        final int left = getIntent().getIntExtra("locationX", 0);
        final int top = getIntent().getIntExtra("locationY", 0);
        final int width = getIntent().getIntExtra("width", 0);
        final int height = getIntent().getIntExtra("height", 0);
        Bundle classSpacebundle = this.getIntent().getExtras();
        mImgUrl = (String) classSpacebundle.get("IMG_URL");

        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.id_layout);
        mBackground = new ColorDrawable(Color.BLACK);
        mLayout.setBackground(mBackground);

        mImgShow = (ImageView) findViewById(R.id.img_show);
        ImageView mImgDown = (ImageView) findViewById(R.id.img_down);

        mImgShow.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {


            @Override
            public boolean onPreDraw() {
                mImgShow.getViewTreeObserver().removeOnPreDrawListener(this);
                int location[] = new int[2];
                mImgShow.getLocationOnScreen(location);
                mLeft = left - location[0];
                mTop = top - location[1];
                mScaleX = (int) (width * 1.0f / mImgShow.getWidth());
                mScaleY = (int) (height * 1.0f / mImgShow.getHeight());
                activityEnterAnim();

                GlideUtil.showNoneImage(ShowPictureActivity.this, mImgUrl, mImgShow);
                return true;
            }


        });


        mImgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityExitAnim(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });

          /*下载图片*/
        mImgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showfxDialog("下载图片");
                httpDownImg();
            }
        });
    }
    private void activityEnterAnim() {
        mImgShow.setPivotX(0);
        mImgShow.setPivotY(0);
        mImgShow.setScaleX(mScaleX);
        mImgShow.setScaleY(mScaleY);
        mImgShow.setTranslationX(mLeft);
        mImgShow.setTranslationY(mTop);
        mImgShow.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground,"alpha",255,255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    private void activityExitAnim(Runnable runnable) {
        mImgShow.setPivotX(0);
        mImgShow.setPivotY(0);
        mImgShow.animate().scaleX(mScaleX).scaleY(mScaleY).translationX(mLeft).translationY(mTop).
                withEndAction(runnable).
                setDuration(500).setInterpolator(new DecelerateInterpolator()).start();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mBackground,"alpha",255,255);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    //下载照片
    public void httpDownImg() {
        final String fileName = StringUtil.getStrLeSplitter(mImgUrl, "/");
        RequestUtill.getInstance().downImageFile(
                this, mImgUrl, fileName,
                new ResultCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtil.showToast(context, ErrorCode.error(e));
                    }

                    @Override
                    public void onResponse(String response) {
//                        ImageUtil.scanFile(context, KidConfig.getInstance().getPathClassSpace() + fileName);
                        ToastUtil.showToast(context, R.string.hint_down_ok);
                        dismissfxDialog();
                    }

                    @Override
                    public void inProgress(float progress) {
                        super.inProgress(progress);

                    }
                });

    }

}
