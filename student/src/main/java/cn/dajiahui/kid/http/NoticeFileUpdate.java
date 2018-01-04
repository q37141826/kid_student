package cn.dajiahui.kid.http;

import android.content.Context;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.ArrayList;

import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.album.bean.BePhoto;

/**
 * Created by z on 2016/3/17.
 * 通知图片上传
 */
public class NoticeFileUpdate {
    private ArrayList<BePhoto> filePath = new ArrayList<BePhoto>();//总数据
    private ArrayList<BePhoto> photos = new ArrayList<BePhoto>();//上传成功后获取的图片数据
    private Context context;
    private int updateIndex;//上传数量
    private OnNoticeFileUpdate onFileUpdate;
    private String userId;
    private boolean isCancel;//是否取消
    private int error;//失败数
    private int successful;//成功数

    public NoticeFileUpdate(ArrayList<BePhoto> filePath, Context context, OnNoticeFileUpdate onFileUpdate) {
        this.filePath = filePath;
        this.context = context;
        this.onFileUpdate = onFileUpdate;
        userId = UserController.getInstance().getUserId();
    }

    //开始上传
    public void startUpdate() {
        httpImgUpdate(filePath.get(updateIndex), userId);
    }

    private void httpImgUpdate(final BePhoto path, String userId) {
        String text = path.getPicUrl().replace("file://", "");
        File file = new File(text);
        RequestUtill.getInstance().uploadPaperImageFile(context, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {
                setUpdateIndex();
                error++;
            }

            @Override
            public void onResponse(String response) {
                HeadJson json = new HeadJson(response);
                if (json.getstatus() == 1) {
                    //成功
                    successful++;
                    BePhoto photo = json.parsingObject(BePhoto.class);
                    photo.setObjectId(path.getObjectId());
                    if (photo != null)
                        photos.add(photo);
                } else {
                    error++;
                }
                setUpdateIndex();
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
            }
        }, file, userId);
    }

    private void setUpdateIndex() {
        if (isCancel) {
            updateIndex = filePath.size();
        } else {
            updateIndex++;
        }
        if (updateIndex == filePath.size()) {
            //上传结束 清除已经失败的图片
            onFileUpdate.saveFile(photos);
        } else {
            httpImgUpdate(filePath.get(updateIndex), userId);
        }
    }
}
