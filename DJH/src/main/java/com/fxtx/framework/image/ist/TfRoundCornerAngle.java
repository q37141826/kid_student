package com.fxtx.framework.image.ist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 切圆角
 *
 * @author Administrator
 */
public class TfRoundCornerAngle extends BitmapTransformation {
    private float ratio;

    public TfRoundCornerAngle(Context context, float ration) {
	super(context);
	this.ratio = ration;
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
	if (toTransform == null) return null;

	Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
	if (result == null) {
	    result = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
	}
	Canvas canvas = new Canvas(result);
	Paint paint = new Paint();
	paint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
	paint.setAntiAlias(true);
	RectF rectF = new RectF(0f, 0f, toTransform.getWidth(), toTransform.getHeight());
	canvas.drawRoundRect(rectF, ratio, ratio, paint);
	return result;
    }

    @Override
    public String getId() {
	return getClass().getName() + Math.round(ratio);
    }
}