package cn.dajiahui.kid.http;

import android.content.Context;

import com.fxtx.framework.http.ErrorCode;
import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.squareup.okhttp.Request;

import java.io.File;

import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.http.bean.BeTeFile;

/**
 * Created by z on 2016/3/17.
 * 附件文档上传
 */
public class AttachmentFileUpdate {
    private File filePath;//总数据
    private Context context;
    private OnAttachmentUpdate onFileUpdate;
    private String userId;

    public AttachmentFileUpdate(File filePath, Context context, OnAttachmentUpdate onFileUpdate) {
        this.filePath = filePath;
        this.context = context;
        this.onFileUpdate = onFileUpdate;
        userId = UserController.getInstance().getUserId();
    }

    //开始上传
    public void startUpdate() {
        if (filePath == null || !filePath.exists() || !filePath.isFile()) {
            onFileUpdate.error("文件不存在");
        } else {
            httpAttachmentUpdate(filePath, userId);
        }
    }

    public void httpAttachmentUpdate(final File file, String userId) {
        RequestUtill.getInstance().uploadAttachment(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                onFileUpdate.error(ErrorCode.error(e));
            }

            @Override
            public void onResponse(String response) {
                HeadJson json = new HeadJson(response);
                if (json.getstatus()  == 1) {
                    //成功
                    BeTeFile beTeFile = json.parsingObject(BeTeFile.class);
                    onFileUpdate.saveFile(beTeFile);
                } else {
                    onFileUpdate.error(json.getMsg());
                }
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
            }
        }, file, userId);
    }


}