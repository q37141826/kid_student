package cn.dajiahui.kid.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.fxtx.framework.image.SelectPhotoActivity;
import com.fxtx.framework.ui.base.WebActivity;
import com.fxtx.framework.util.JumpUtil;

import java.util.ArrayList;

import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.album.PhotoActivity;
import cn.dajiahui.kid.ui.album.PhotoDetailsActivity;
import cn.dajiahui.kid.ui.album.PhotoPageActivity;
import cn.dajiahui.kid.ui.album.bean.BePhoto;
import cn.dajiahui.kid.ui.chat.ChatActivity;
import cn.dajiahui.kid.ui.chat.ConcactForClassActivity;
import cn.dajiahui.kid.ui.mine.myworks.VideoActivity;
import cn.dajiahui.kid.ui.mine.personalinformation.UserSetActivity;
import cn.dajiahui.kid.ui.mp3.AudioActivity;
import cn.dajiahui.kid.ui.video.FullscreenVideoActivity;
import cn.dajiahui.kid.ui.video.bean.BeVideo;

/**
 * @author djh-zy
 * @version :1
 * @CreateDate 2015年8月3日 下午4:49:49
 * @description :跳转工具类
 */
public class DjhJumpUtil extends JumpUtil {

    private static DjhJumpUtil nUtil;
    public final int activity_PhotoPage = 1002;

    public final int activtiy_SelectPhoto = 3001;
    public final int activtiy_UserSet = 5001;
    public final int activtiy_ClassInfo = 4002;  // 班级信息 1.进入班级空间 2.退出班级

    public final int activity_answerCard = 7000;
    public final int activity_todohomework = 7100;
    public final int activity_answerCardComplete = 7200;
    public final int activity_answerCardSubmit = 7300;

    public final int activity_makekalaok_request = 7400;//重新录制请求码
    public final int activity_makekalaok_result = 7500;//重新录制结果码

    public final int activity_makekalaok_out = 7510;//录制完成之后点击返回键 与左上角返回键
    public final int activity_makebookdrame = 7530;//制作课本剧
    public final int activity_makebookdrame_out = 7520;//录制完成之后点击返回键 与左上角返回键

    public final int activity_searchclass = 7600;//搜索班级
    public final int activtiy_Notice = 1008;//我的通知

    private DjhJumpUtil() {
    }

    /**
     * 单一实例
     */
    public static DjhJumpUtil getInstance() {
        if (nUtil == null) {
            synchronized (DjhJumpUtil.class) {
                if (nUtil == null) {
                    nUtil = new DjhJumpUtil();
                }
            }
        }
        return nUtil;
    }

    //我的相册
    public void startPhotoActivity(Context context, String albumId, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.bundle_title, title);
        bundle.putString(Constant.bundle_id, albumId);
        startBaseActivity(context, PhotoActivity.class, bundle, 0);
    }

    public void startPhotoPageActivity(Activity activity, ArrayList<BePhoto> lists, int index, boolean isMo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.bundle_obj, lists);
        bundle.putInt(Constant.bundle_id, index);
        bundle.putBoolean(Constant.bundle_type, isMo);
        startBaseActivityForResult(activity, PhotoPageActivity.class, bundle, activity_PhotoPage);
    }

    public void startPhotoDetails(Context context, String photoId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.bundle_id, photoId);
        startBaseActivity(context, PhotoDetailsActivity.class, bundle, 0);
    }


    //启动相册
    public void startSelectPhotoActivity(Activity activity, int photoMax) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.bundle_obj, photoMax);
        startBaseActivityForResult(activity, SelectPhotoActivity.class, bundle, activtiy_SelectPhoto);
    }


    public void startUserSetActivity(Activity activity, int type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.bundle_type, type);
        startBaseActivityForResult(activity, UserSetActivity.class, bundle, activtiy_UserSet);
    }

    public void startChatActivity(Context context, String imId, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", imId);
        bundle.putString("phone", phone);
        startBaseActivity(context, ChatActivity.class, bundle, 0);
    }

    public void startContactActivity(Context context, String ids) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.bundle_id, ids);
        startBaseActivity(context, ConcactForClassActivity.class, bundle, 0);
    }


    /**
     * 访问网页
     *
     * @param context
     * @param title
     * @param Url
     */
    public void startWebActivity(Context context, String title, String Url, Boolean type) {
        startBaseWebActivity(context, title, Url, type, WebActivity.class);
    }

    /**
     * 访问网页
     *
     * @param context
     * @param title
     * @param Url
     */
    public void startBaseWebActivity(Context context, String title, String Url, Boolean type, Class classs) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.bundle_title, title);
        bundle.putString(Constant.bundle_obj, Url);
        bundle.putBoolean(Constant.bundle_type, type);
        startBaseActivity(context, classs, bundle, 0);
    }

    /**
     * 视频播放
     *
     * @param activity
     */
    public void startVideoActivity(Context activity, BeVideo video) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.bundle_obj, video);
        startBaseActivity(activity, VideoActivity.class, bundle, 0);
    }

    /**
     * 音频播放
     *
     * @param activity
     */
    public void startAudioActivity(Context activity, BeVideo video) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.bundle_obj, video);
        startBaseActivity(activity, AudioActivity.class, bundle, 0);
    }

    /**
     * 横版全屏视频播放
     *
     * @param activity
     */
    public void startFullscreenVideoActivity(Context activity, String url) {
        Bundle bundle = new Bundle();
        BeVideo video = new BeVideo();
        video.url = url;
        bundle.putSerializable(Constant.bundle_obj, video);
        startBaseActivity(activity, FullscreenVideoActivity.class, bundle, 0);
    }

}