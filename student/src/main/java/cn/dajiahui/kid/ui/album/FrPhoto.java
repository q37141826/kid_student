package cn.dajiahui.kid.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.bm.library.PhotoView;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;

/**
 * Created by z on 2016/3/7.
 * 图片信息详情界面
 */
public class FrPhoto extends FxFragment {

    private PhotoPageActivity.OnImageClick onImageClick;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_album_photo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 显示图片
        PhotoView image = (PhotoView) rootView;
        String path = bundle.getString(Constant.bundle_obj);
        boolean isGif = bundle.getBoolean(Constant.bundle_type);
        if (isGif) {
            GlideUtil.showGifImage(getActivity(), path, image, R.drawable.ico_default);
        } else {
            GlideUtil.showNoneImage(getActivity(), path, image, R.drawable.ico_default,true);
        }
        image.enable();
        image.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onImageClick != null) {
                onImageClick.onItemClick();
            }
        }
    };

    public void setOnImageClick(PhotoPageActivity.OnImageClick onImageClick) {
        this.onImageClick = onImageClick;
    }
}
