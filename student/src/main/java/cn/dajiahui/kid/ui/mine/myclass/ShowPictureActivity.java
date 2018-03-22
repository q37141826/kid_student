package cn.dajiahui.kid.ui.mine.myclass;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;

/*班级空间显示图片*/
public class ShowPictureActivity extends FxActivity {

    private String mImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onBackText();
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_show_picture);
        Bundle classSpacebundle = this.getIntent().getExtras();
        mImgUrl = (String) classSpacebundle.get("IMG_URL");
        ImageView mImgShow = getView(R.id.img_show);
        ImageView mImgDown = getView(R.id.img_down);
        GlideUtil.showNoneImage(ShowPictureActivity.this, mImgUrl, mImgShow);
        //;//拍照存放路径
        mImgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
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

    //下载照片
    public void httpDownImg() {
        final String fileName = StringUtil.getStrLeSplitter(mImgUrl, "/");
        RequestUtill.getInstance().downImageFile(
                context, mImgUrl, fileName,
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
