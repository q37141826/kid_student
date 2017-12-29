package com.fxtx.framework.image.util;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxtx.framework.FxtxConstant;
import com.fxtx.framework.image.ist.TfRoundCorner;
import com.fxtx.framework.image.ist.TfRoundCornerAngle;
import com.fxtx.framework.text.StringUtil;

/**
 * @author djh-zy
 * @ClassName: GlideUtil.java
 * @Date 2016年5月12日 上午10:00:30
 * @Description: 异步图片加载显示工具类
 */
public class GlideUtil {
    /**
     * 显示圆形图片
     *
     * @param context
     * @param url
     * @param placeholder
     * @param isresize    ：是否对图片进行压缩 ，默认压缩调整为 FxtxConstant.IMAGE_RESIZE 大小
     */
    public static void showRoundImage(Context context, String url, ImageView imageView, int placeholder, boolean isresize) {
        if (!showNullImage(context, url, imageView, placeholder)) return;
        Glide.with(context)
                .load(StringUtil.isImageUrl(url))
                .asBitmap()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .transform(new TfRoundCorner(context))
                .into(imageView);
    }

    /**
     * 显示 圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param placeholder
     */
    public static void showFilletImage(Context context, String url, ImageView imageView, int placeholder) {
        if (!showNullImage(context, url, imageView, placeholder)) return;
        Glide.with(context)
                .load(StringUtil.isImageUrl(url))
                .asBitmap()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .transform(new TfRoundCornerAngle(context, FxtxConstant.IMAGE_DEFAULT_RATION))
                .into(imageView);
    }



    private static boolean showNullImage(Context context, String url, ImageView imageView, int placeholder) {
        if (context == null || imageView == null)
            return false;
        if (StringUtil.isEmpty(url)) {
            Glide.with(context)
                    .load(placeholder).into(imageView);
            return false;
        }
        return true;
    }

    //不变形
    public static void showNoneImage(Context context, String url, ImageView imageView, int placeholder, boolean isresize) {
        if (!showNullImage(context, url, imageView, placeholder)) return;
        Glide.with(context)
                .load(StringUtil.isImageUrl(url))
                .asBitmap()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
    //不变形
    public static void showNoneImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(StringUtil.isImageUrl(url))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    //显示本地图片
    public static void showImageFile(Context context, String filePath, ImageView imageView, int placeholder) {
        if (!showNullImage(context, filePath, imageView, placeholder)) return;
        Glide.with(context)
                .load(filePath)
                .asBitmap()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(imageView);
    }

    //显示Gif动画
    public static void showGifImage(Activity activity, String url, ImageView image, int placeholder) {
        if (!showNullImage(activity, url, image, placeholder)) return;
        Glide.with(activity)
                .load(StringUtil.isImageUrl(url))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .error(placeholder)
                .into(image);
    }


}




