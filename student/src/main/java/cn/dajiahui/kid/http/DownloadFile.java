package cn.dajiahui.kid.http;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.OkHttpClientManager;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.ArrayList;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.album.bean.BePhoto;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.OpFileUtil;


/**
 * Created by Administrator on 2016/4/9.
 * 下载文件
 */
public class DownloadFile {
    private FxActivity mContext;
    private BeDownFile befile;
    private File file;
    private String fileName;
    private OnDownload onDown;//文件更新回调处理


    /**
     * @param context
     * @param befile
     * @param isDownLoad 是否始终下载
     */
    public DownloadFile(FxActivity context, BeDownFile befile, boolean isDownLoad, OnDownload onDown) {
        this.onDown = onDown;
        this.mContext = context;
        this.befile = befile;
        if (befile.getFileType() == Constant.file_img) {
            ArrayList<BePhoto> photos = new ArrayList<BePhoto>();
            photos.add(new BePhoto(befile.getFileUrl(), befile.getFileUrl(), befile.getFileUrl()));
            DjhJumpUtil.getInstance().startPhotoPageActivity(context, photos, 0, false);
            return;
        }
        if (StringUtil.isEmpty(befile.getFileUrl())) return;
        int index = befile.getFileUrl().lastIndexOf("/");
        if (index == -1) {
            this.fileName = befile.getFileUrl();
        } else {
            this.fileName = befile.getFileUrl().substring(befile.getFileUrl().lastIndexOf("/"));
        }
        file = new File(UserController.getInstance().getUserMaterial(context) + fileName);
        if (file.exists()) {
            if (isDownLoad) {
                file.delete();
                downLoad(befile.getFileUrl(), fileName);
                return;
            }
            befile.setLocaUrl(file.getAbsolutePath());
            if (onDown != null) {
                onDown.onDownload(befile.getLocaUrl());
            } else {
                OpFileUtil.openFile(mContext, befile);
            }
        } else {
            downLoad(befile.getFileUrl(), fileName);
        }
    }

    /**
     * @param context
     * @param befile
     * @param isDownLoad 是否始终下载
     */
    public DownloadFile(FxActivity context, BeDownFile befile, boolean isDownLoad) {
        this(context, befile, isDownLoad, null);
    }

    /**
     * @param url
     * @param fileName
     */
    public void downLoad(String url, String fileName) {
        if (!url.startsWith("http://")) {
            url = RequestUtill.getInstance().getFileUrl() + url;
        }
        initDownDialog();
        RequestUtill.getInstance().downMaterialFile(mContext, url, fileName, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                mContext.dismissfxDialog();
                if (file.exists()) file.delete();
                ToastUtil.showToast(mContext, ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                mContext.dismissfxDialog();
                befile.setLocaUrl(file.getAbsolutePath());
                if (onDown != null) {
                    onDown.onDownload(befile.getLocaUrl());
                } else {
                    OpFileUtil.openFile(mContext, befile);
                }
                dialog.dismiss();
                ToastUtil.showToast(mContext, "下载成功保存到" + file.getAbsolutePath());
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                if (progressBar != null)
                    progressBar.setProgress((int) (progress * 100.f));
            }
        });
    }

    private FxDialog dialog;
    private ProgressBar progressBar;

    public void initDownDialog() {
        dialog = new FxDialog(mContext) {
            @Override
            public void onRightBtn(int flag) {
                dialog.dismiss();
            }

            @Override
            public void onLeftBtn(int flag) {
                //上传时点击取消
                file.delete();
                OkHttpClientManager.getInstance().cancelTag(mContext);
                dialog.dismiss();
            }

            @Override
            public boolean isOnClickDismiss() {
                return false;
            }
        };
        dialog.setCancelable(false);
        View contextView = LayoutInflater.from(mContext).inflate(
                com.fxtx.framework.R.layout.dialog_update, null);
        progressBar = (ProgressBar) contextView.findViewById(com.fxtx.framework.R.id.dialog_pb);
        progressBar.setMax(100);
        dialog.setMessage(befile.getName());
        dialog.addView(contextView);
        dialog.setRightBtnHide(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(R.string.dialog_down_file);
        progressBar.setVisibility(View.VISIBLE);
        dialog.show();

    }
}
