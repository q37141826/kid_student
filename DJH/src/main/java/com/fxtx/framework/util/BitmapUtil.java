package com.fxtx.framework.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
*截取图片的工具类
* **/
public class BitmapUtil {
    /**
     * @param source 产生子位图的源位图
     * @param x      子位图第一个像素在源位图的X坐标
     * @param y      子位图第一个像素在源位图的y坐标
     * @param width  子位图每一行的像素个数
     * @param height 子位图的行数
     * @param m      对像素值进行变换的可选矩阵
     * @param filter 如果为true，源图要被过滤。该参数仅在matrix包含了超过一个翻转才有效
     * @return 一个描述了源图指定子集的位图。 Bitmap
     */
    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        Bitmap bitmap = null;
        try {
            if ((y >= 0) &&(x >= 0) && (x + width) <= source.getWidth() && (y + height) <= source.getHeight()) {
                bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
            }
        } catch (OutOfMemoryError localOutOfMemoryError) {
            System.gc();
            bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
        }
        return bitmap;
    }


    public static Bitmap byteArrayToBitmap(byte[] array) {
        if (null == array) {
            return null;
        }

        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static byte[] bitampToByteArray(Bitmap bitmap) {
        byte[] array = null;
        try {
            if (null != bitmap) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                array = os.toByteArray();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    /**
     * 保存方法
     */
    public static void saveBitmaptoTemporaryPicture(Bitmap bm, String path, String picname) {


        File f = new File(path + picname + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bm != null) {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveBitmapToSDCard(Bitmap bmp, String strPath) {
        if (null != bmp && null != strPath && !strPath.equalsIgnoreCase("")) {
            try {
                File file = new File(strPath);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = BitmapUtil.bitampToByteArray(bmp);
                fos.write(buffer);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap loadBitmapFromSDCard(String strPath) {
        File file = new File(strPath);

        try {
            FileInputStream fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;   //width��hight��Ϊԭ���Ķ���һ
            Bitmap btp = BitmapFactory.decodeStream(fis, null, options);
            return btp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
