package com.fxtx.framework.updata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.http.request.OkHttpDownloadRequest;
import com.fxtx.framework.log.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.File;

/**
 */

public abstract class UpdateManager {

    private String soft_update_no = "已经是最新版本";
    private String soft_update_title = "软件更新";
    private String soft_updating = "正在更新";
    private String soft_update_update_now = "更新";
    private String soft_update_finish = "退出应用";
    private String soft_update_cancel = "取消更新";

    /* 下载结束 */
    protected final int DOWNLOAD_FINISH = 1;
    protected final int DO_NOTHING = 2;
    protected final int DO_ERROR = 3;//下载错误
    // 强制更新
    protected boolean isMustUpdate;
    /* 下载保存路径 */
    private String mSavePath;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    protected Activity mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private UpdateDialog mDownloadDialog;
    private UpdateDialog alert;
    protected ProgressDialog progressDialog;

    protected OnUpdateListener onUpdate;
    protected String message;// 更新内容
    protected String apkName;
    protected String url;
    protected Handler updateHandler;

    public UpdateManager(Activity context, final OnUpdateListener onUpdate) {
        this.mContext = context;
        this.onUpdate = onUpdate;
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DO_NOTHING:
                        if (!mContext.isFinishing())
                            ToastUtil.showToast(mContext, soft_update_no);
                        break;
                    case DOWNLOAD_FINISH:
                        // 安装文件
                        installApk();
                        break;
                    case DO_ERROR:
                        UpdateManager.this.onUpdate.onUpdateError(msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
        };

    }

    //没有进度条的检查
    public abstract void checkUpdateOrNotAuto();


    // 检测更新
    public abstract void checkUpdateOrNot();

    /**
     * 执行更新 操作  传url地址
     */
    public void doUpdate(String url) {
        apkName = mContext.getPackageName() + ".apk";
        this.url = url;
        if (alert != null)
            alert.dismiss();
        alert = new UpdateDialog(mContext) {
            @Override
            public void onLeftBtn(int floag) {
                alert.dismiss();
                if (isMustUpdate) {
                    onUpdate.onUpdateCancel(1);
                } else {
                    onUpdate.onUpdateCancel(2);
                }
            }

            @Override
            public void onRightBtn(int floag) {
                alert.dismiss();
                showDownloadDialog();
            }
        };
        alert.setTitle(soft_update_title);
        alert.setMessage(message);
        if (isMustUpdate) {
            alert.setLeftBtnText(soft_update_finish);
        } else {
            alert.setLeftBtnText(soft_update_cancel);
        }
        alert.setRightBtnText(soft_update_update_now);
        alert.show();
    }

    private void heandError(String error) {
        Message msg = updateHandler.obtainMessage();
        msg.what = DO_ERROR;
        msg.obj = error;
        updateHandler.sendMessage(msg);
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        // 构造对话框
        if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
            mDownloadDialog.dismiss();
        }
        if (mDownloadDialog == null) {
            mDownloadDialog = new UpdateDialog(mContext) {
                @Override
                public void onLeftBtn(int floag) {
                    mDownloadDialog.dismiss();
                    cancelUpdate = true;
                    if (isMustUpdate) {
                        onUpdate.onUpdateCancel(1);
                    } else {
                        onUpdate.onUpdateCancel(2);
                    }
                }
                @Override
                public void onRightBtn(int floag) {

                }
            };// 创建下载的对象
        }
        mDownloadDialog.setTitle(soft_update_title);
        mDownloadDialog.setMessage(soft_updating);
        // 给下载对话框增加进度条
        mProgress = mDownloadDialog.getProgressBar();
        mProgress.setVisibility(View.VISIBLE);
        mDownloadDialog.setRightBtnHide(View.GONE);
        // 取消更新
        if (isMustUpdate) { // 如果是强制更新的版本
            mDownloadDialog.setLeftBtnText(soft_update_finish);
        } else {
            mDownloadDialog.setLeftBtnText(soft_update_cancel);
        }
        mDownloadDialog.show();
        // 开始下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        String sdpath = "";
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            sdpath = Environment.getExternalStorageDirectory() + "/";
        } else {
            sdpath = mContext.getCacheDir() + "/";
        }
        // 获得存储卡的路径
        mSavePath = sdpath + "download";
        File file = new File(mSavePath);
        // 判断文件目录是否存在
        if (!file.exists()) {
            file.mkdirs();
        }
        new OkHttpDownloadRequest.Builder().tag(mContext).url(url).destFileName(apkName).destFileDir(mSavePath).download(new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                heandError("下载错误" + ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                updateHandler.sendEmptyMessage(DOWNLOAD_FINISH);
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                if (mProgress != null)
                    mProgress.setProgress((int) (progress * 100.f));
            }
        });
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, apkName);
        if (!apkfile.exists()) {
            onUpdate.onUpdateError("安装文件不存在");
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
        onUpdate.onUpdateSuccess();
    }
}
