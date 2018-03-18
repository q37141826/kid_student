package com.fxtx.framework.platforms.umeng;

import android.app.Activity;
import android.widget.Toast;

import com.fxtx.framework.FxtxConstant;
import com.fxtx.framework.log.ToastUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * @author
 * @ClassName: UmengUtil.java
 * @Date 2018年3月15日
 * @Description: 友盟分享
 */
public class UmengShare {
    private Activity context;
    //设置 默认可以分享的平台
    private final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN,
                    SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ,
                    SHARE_MEDIA.QZONE,
//                    SHARE_MEDIA.SINA
            };

    //授权 分享平台
    public static void initSharePlat() {
        PlatformConfig.setWeixin(FxtxConstant.WEIXIN_APPID, FxtxConstant.WEIXIN_SECRET);
//        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(FxtxConstant.QQ_APPID, FxtxConstant.QQ_APPKEY);
//        // QQ和Qzone appid appkey
//        PlatformConfig.setSinaWeibo(FxtxConstant.SINA, FxtxConstant.SINA_APPSECRET, "http://sns.whalecloud.com");
    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            if ((platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE) && !StringUtil.isEmpty(beShareContent.getSharePictureUrlMin())) {
////                beShareContent.setSharePictureUrlMin(null);
////                beShareContent.setThumbRes(R.drawable.ico_default);
////                onclick(null, platform);
//            } else {
            ToastUtil.showToast(context, getShaerMedia(platform) + " 分享失败啦", Toast.LENGTH_SHORT);
//            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.showToast(context, getShaerMedia(platform) + " 分享取消了", Toast.LENGTH_SHORT);
        }
    };


    private String getShaerMedia(SHARE_MEDIA platform) {
        String str;
        switch (platform) {
            case WEIXIN:
                str = "微信好友";
                break;
            case WEIXIN_CIRCLE:
                str = "朋友圈";
                break;
            case QQ:
                str = "QQ好友";
                break;
            case QZONE:
                str = "QQ空间";
                break;
            default:
                str = platform.toString();
                break;
        }
        return str;
    }

//    @Override
//    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//        ShareAction action = new ShareAction(context);
//        UMWeb web = new UMWeb(beShareContent.getShareUrl());
//        web.setTitle(beShareContent.getShareTitle());//标题
//        UMImage image = null;
//        if (!StringUtil.isEmpty(beShareContent.getSharePictureUrlMin())) {
//            image = new UMImage(context, beShareContent.getSharePictureUrlMin());
//        } else if (beShareContent.getThumbRes() > 0) {
//            image = new UMImage(context, beShareContent.getThumbRes());
//        }
//        if (image != null)
//            web.setThumb(image);  //缩略图
//        web.setDescription(beShareContent.getShareContent());//描述
//        action.withMedia(web);
//        action.setPlatform(share_media).setCallback(umShareListener).share();
//    }


    private BeShareContent beShareContent;


    public ShareAction share(Activity context, BeShareContent beShareContent) {
        ShareAction action = new ShareAction(context);
        UMWeb web = new UMWeb(beShareContent.getShareUrl());
        web.setTitle(beShareContent.getShareTitle());//标题
        UMImage image = null;
//                    if (!StringUtil.isEmpty(beShareContent.getSharePictureUrlMin())) {
//                        image = new UMImage(context, beShareContent.getSharePictureUrlMin());
//                    } else if (beShareContent.getThumbRes() > 0) {

        image = new UMImage(context, beShareContent.getThumbRes());//beShareContent.getThumbRes()
//                    }
        if (image != null)
            web.setThumb(image);  //缩略图
        web.setDescription(beShareContent.getShareContent());//描述
        action.withMedia(web);
        action.setCallback(umShareListener);
        return action;
    }


//    /**
//     * 分享功能
//     */
//    public void shartShare(Activity activity, BeShareContent beShareContent) {
//        this.context = activity;
//        this.beShareContent = beShareContent;
//        if (beShareContent == null) {
//            ToastUtil.showToast(context, "数据不完整，分享失败");
//            return;
//        }
//        ShareAction action = new ShareAction(activity).setDisplayList(displaylist);
////        MyShareBord myShareBord = new MyShareBord(beShareContent, activity);
//        action.setShareboardclickCallback(this);
//        action.open();
//
//    }
}