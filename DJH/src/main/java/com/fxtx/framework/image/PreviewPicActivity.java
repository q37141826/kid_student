package com.fxtx.framework.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bm.library.PhotoView;
import com.fxtx.framework.R;
import com.fxtx.framework.image.util.ImageUtil;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;

import java.io.File;

public class PreviewPicActivity extends FxActivity {
    private PhotoView previewImg;
    private String previewPicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle("照片");
        onRightBtn(0, R.string.sure);
        onBackText();
        initData();
    }

    @Override
    public void onRightBtnClick(View view) {
        super.onRightBtnClick(view);
        if (!StringUtil.isEmpty(previewPicPath)) {
            setResult(RESULT_OK);
            finishActivity();
        } else {
            ToastUtil.showToast(this, "对不起，获取相片路径失败，请重新拍照！");
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            previewPicPath = intent.getStringExtra("_file");
            if (StringUtil.isEmpty(previewPicPath)) {
                ToastUtil.showToast(this, "对不起，获取相片路径失败，请重新拍照！");
                return;
            }
            Bitmap map = ImageUtil.uriToBitmap(Uri.fromFile(new File(previewPicPath)), this);
            if (map == null) {
                return;
            }
            int degree = ImageUtil.readPictureDegree(previewPicPath);
            map = ImageUtil.rotaingImageView(degree, map);
            previewImg.setImageBitmap(map);
            ImageUtil.bitmapToFile(map, previewPicPath,2048);
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_preview_pic);
        previewImg = getView(R.id.photoView);
        previewImg.enable();
    }
}
