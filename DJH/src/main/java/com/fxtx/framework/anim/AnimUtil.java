package com.fxtx.framework.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.fxtx.framework.R;
import com.fxtx.framework.annotation.ViewExpandAnimation;

/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年8月5日 上午11:39:01
 * @description :
 */
public class AnimUtil {
    public static boolean flag = true;

    // 180水平翻转动画
    public static RotateAnimation startAnimation(boolean isAnim) {
        RotateAnimation animation;
        if (isAnim) {
            animation = new RotateAnimation(0f, 180f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(180f, 360f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setDuration(100);// 设置动画持续时间
        animation.setFillAfter(true);
        return animation;
    }


    public static ValueAnimator createHeightAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static void animateExpanding(final View view) {
        view.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        ValueAnimator animator = createHeightAnimator(view, 0, view.getMeasuredHeight());
        animator.start();
    }

    public static void animateCollapsing(final View view, AnimatorListenerAdapter adapter) {
        int origHeight = view.getHeight();

        ValueAnimator animator = createHeightAnimator(view, origHeight, 0);

        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();

    }

    //从下往上 出现
    public static void showDowntoUp(View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.pop_enter);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
    }

    //从上往下 消失
    public static void hideUptoDown(final View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.pop_exit);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //开始
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //结束
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //重复
            }
        });
        view.startAnimation(animation);
    }

    //从下往上 消失
    public static void hideDowntoUp(final View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.pop_enter_hide);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //开始
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //结束
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //重复
            }
        });
        view.startAnimation(animation);
    }

    //从上往下 出现
    public static void showUptoDown(final View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.pop_exit_show);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
    }

    public static void alphaView(Context context, final View view, final View view2) {
        if (flag) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpher);
            animation.setDuration(100);
            view.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    flag = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    flag = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    public static void alphaViewShow(Context context, final View view, final View view2) {
        if (flag) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpher);
            animation.setDuration(100);
            view2.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    flag = false;
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    flag = true;
                    view2.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    /**
     * 折叠消失和显示动画
     *
     * @param v
     */
    public static void expandAnimation(View v) {
        v.startAnimation(new ViewExpandAnimation(v, 300));
    }

    /**
     * 放大动画
     *
     * @param ImageView 需要被放大的控件
     */
    public static ScaleAnimation magnifyingAnimation(ImageView scaleimageView,int during) {
        scaleimageView.bringToFront();
        ScaleAnimation scal = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scal.setDuration(during);
        scal.setFillAfter(true);

        scaleimageView.startAnimation(scal);
        scaleimageView.invalidate();
        return scal;
    }


}
