package cn.dajiahui.kid.http;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.OkHttpClientManager;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;
import com.squareup.okhttp.Request;

import java.io.File;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;
import cn.dajiahui.kid.util.MD5;
import cn.dajiahui.kid.util.OpFileUtil;


/**
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

        if (StringUtil.isEmpty(befile.getFileUrl())) return;
        int index = befile.getFileUrl().lastIndexOf("/");
        if (index == -1) {
            this.fileName = MD5.getMD5(befile.getFileUrl());

        } else {

            if (befile.getFileType() == Constant.file_pointreading) {
                Logger.d("点读本类型");
                this.fileName = MD5.getMD5(befile.getFileUrl().substring(befile.getFileUrl().lastIndexOf("/"))) + ".mp3";
            } else if (befile.getFileType() == Constant.file_textbookplay) {
                String pathTextbookPlay = KidConfig.getInstance().getPathTextbookPlay();
                Logger.d("课本剧类型:"+pathTextbookPlay);
                this.fileName = MD5.getMD5(befile.getFileUrl().substring(befile.getFileUrl().lastIndexOf("/"))) + ".mp4";

            }


        }
        showfxDialog("下载中");
        Logger.d("要下載文件的名字："+this.fileName);
        file = new File(KidConfig.getInstance().getPathTemp() + this.fileName);//参数文件名字
        if (file.exists()) {
            if (isDownLoad) {
                Logger.d("---------------------1" );
                file.delete();
                downLoad(befile.getFileUrl(), fileName);
                return;
            }
            Logger.d("---------------------2" );
            befile.setLocaUrl(KidConfig.getInstance().getPathTemp());
            if (onDown != null) {
                Logger.d("---------------------3" );
                onDown.onDownload(befile.getLocaUrl() + fileName, progressDialog);
            }
//            else {
//                OpFileUtil.openFile(mContext, befile);
//            }
        } else {
            Logger.d("---------------------4" );
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
    public void downLoad(String url, final String fileName) {
        if (!url.startsWith("http://")) {
            url = RequestUtill.getInstance().getFileUrl() + url;
        }

        RequestUtill.getInstance().downMaterialFile(mContext, url, fileName, new ResultCallback() {

            @Override
            public void onError(Request request, Exception e) {
                dismissfxDialog(0);
                if (file.exists()) file.delete();
                ToastUtil.showToast(mContext, ErrorCode.error(e));
                Logger.d("文件下载失败");
            }

            @Override
            public void onResponse(String response) {
                Logger.d("文件下完成");
                befile.setLocaUrl(KidConfig.getInstance().getPathTemp());
                if (befile.getFileType() == Constant.file_pointreading) {
                      /*复制文件到 pathPointRedaing*/
                    FileUtil.copy(KidConfig.getInstance().getPathTemp(), KidConfig.getInstance().getPathPointRedaing());
                    Logger.d("点读本复制完成！");
                } else if (befile.getFileType() == Constant.file_textbookplay) {
                     /*复制文件到 PathTextbookPlay*/
                    FileUtil.copy(KidConfig.getInstance().getPathTemp(), KidConfig.getInstance().getPathTextbookPlay());
                    Logger.d("課本剧复制完成！");
                }

                if (onDown != null) {
                    Logger.d("下载成功回调");
                    onDown.onDownload(befile.getLocaUrl() + fileName, progressDialog);
                } else {
                    Logger.d("直接打开！");
                    OpFileUtil.openFile(mContext, befile);
                }
//                dialog.dismiss();
//                ToastUtil.showToast(mContext, "下载成功保存到" + KidConfig.getInstance().getPathTemp());
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                Logger.d("文件下载中");
//                if (progressBar != null)
//                    progressBar.setProgress((int) (progress * 100.f));
            }
        });
    }

    private FxProgressDialog progressDialog;
    protected final int PROGRESS_BACK = -1;

    public void showfxDialog(Object title) {

        if (progressDialog == null) {
            progressDialog = new FxProgressDialog(mContext);
            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        //点击返回
                        dismissfxDialog(PROGRESS_BACK);
                        return true;
                    }
                    return false;
                }
            });
        }
        if (title != null) {
            if (title instanceof String) {
                progressDialog.setTextMsg((String) title);
            } else {
                progressDialog.setTextMsg((Integer) title);
            }
        }
        progressDialog.show();
    }

    protected void dismissfxDialog(int flag) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
