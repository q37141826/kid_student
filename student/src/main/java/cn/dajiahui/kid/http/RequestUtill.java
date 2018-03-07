package cn.dajiahui.kid.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Pair;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.http.request.OkHttpDownloadRequest;
import com.fxtx.framework.http.request.OkHttpRequest;
import com.fxtx.framework.image.util.ImageUtil;
import com.fxtx.framework.log.ToastUtil;

import java.io.File;
import java.util.IdentityHashMap;

import cn.dajiahui.kid.BuildConfig;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;


/**
 * Created by z on 2016/1/20.
 * 网络请求处理工具
 */
public class RequestUtill {
    private static RequestUtill util;

    public String getUrl() {
        return BuildConfig.httpUrl;
    }

    public String getFileUrl() {
        return BuildConfig.httpFileUrl;
    }

    /**
     * 单一实例
     */
    public static RequestUtill getInstance() {
        if (util == null) {
            synchronized (RequestUtill.class) {
                if (util == null) {
                    util = new RequestUtill();
                }
            }
        }
        return util;
    }


    /**
     * 获取网络请求
     */
    public OkHttpRequest.Builder getHttpBuilder(Context context, String httpAction) {
        return new OkHttpRequest.Builder().tag(context).url(getUrl() + httpAction);
    }

    public void httpDownFile(Context context, String url, ResultCallback callback, String file, String dir) {
        new OkHttpDownloadRequest.Builder().tag(context).url(url).destFileName(file).destFileDir(dir).download(callback);
    }

    //下载
    public void downImageFile(Context context, String url, String fileName, ResultCallback callback) {
        //文件名称 和文件地址
        httpDownFile(context, url, callback, fileName, UserController.getInstance().getUserImageFile(context));
    }

    //下载资料文件
    public void downMaterialFile(Context context, String url, String fileName, ResultCallback callback) {
        // url 文件地址  fileName文件名字   KidConfig.getInstance().getPathTemp() 文件本地路径
        httpDownFile(context, url, callback, fileName, KidConfig.getInstance().getPathTemp());
    }

    /**
     * 版本更新
     *
     * @return key:entity
     */
    public void httpUpdateApp(Context context, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
//        params.put("versionType", "0");
//        params.put("type", "2");
//        getHttpBuilder(context, "version/update.json").params(params).post(callback);
    }


    public void addDownCount(Context context, String id, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("materialId", id);
        new OkHttpRequest.Builder().tag(context).url(getUrl() + "material/addCount.json").params(params).get(callback);
    }

    /**
     * 获取班级相册
     *
     * @param num
     * @param size
     */
    public void httpMyClassAlbumList(Context context, ResultCallback callback, String classId, int num, String size) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classId", classId);
        params.put("pageSize", size);
        params.put("pageNum", num + "");
        getHttpBuilder(context, "classAlbum/getClassAlbumList.json").params(params).post(callback);
    }

    /**
     * 获取班级相册
     */
    public void httpClassAlbumList(Context context, ResultCallback callback, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        getHttpBuilder(context, "classAlbum/getClassAndClassAlbumList.json").params(params).post(callback);
    }


    public void httpPictureDetails(Context context, ResultCallback callback, String userId, String pictureId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pictureId", pictureId);
        getHttpBuilder(context, "picture/getById.json").params(params).post(callback);
    }


    public void httpPictureList(Context context, ResultCallback callback, String albumid, String userId, int num, String size) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classAlbumId", albumid);
        params.put("userId", userId);
        params.put("pageSize", size);
        params.put("pageNum", num + "");
        getHttpBuilder(context, "picture/getPictureList.json").params(params).post(callback);
    }

    public void httpPictureLike(Context context, ResultCallback callback, String urserId, String pictureId, int type) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", urserId);
        params.put("pictureId", pictureId);
        params.put("type", type + "");//1赞，0取消
        getHttpBuilder(context, "picture/like.json").params(params).post(callback);
    }

    public void httpPictureDelete(Context context, ResultCallback callback, String pictureId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("pictureId", pictureId);
        getHttpBuilder(context, "picture/delete.json").params(params).post(callback);
    }


    //获取通知列表
    public void httpNoticeList(Context context, ResultCallback callback, String userId, int num, int size) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pageSize", size + "");
        params.put("pageNum", num + "");
        getHttpBuilder(context, "notice/getMyNoticeList.json").params(params).post(callback);
    }

    //通知详情
    public void httpNoticeDetail(Context context, ResultCallback callback, String userId, String noticeId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("noticeId", noticeId);
        getHttpBuilder(context, "notice/findNotice.json").params(params).post(callback);
    }

    //通知评论
    public void httpNoticeComment(Context context, ResultCallback callback, String userId, String noticeId, String content, String parentId, String replyUserId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("noticeId", noticeId);
        params.put("parentId", parentId);
        params.put("content", content);
        params.put("replyUserId", replyUserId);
        getHttpBuilder(context, "notice/addComment.json").params(params).post(callback);
    }

    public void httpReadNotice(Context context, ResultCallback callback, String userId, String noticeId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("noticeId", noticeId);
        getHttpBuilder(context, "notice/readNotice.json").params(params).post(callback);
    }

    /**
     * 获取班级申请列表(公用)
     *
     * @param context
     * @param callback
     * @param userid
     */
    public void httpAuditList(Context context, ResultCallback callback, String userid) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userid);
        getHttpBuilder(context, "classApply/getClassApplyList.json").params(params).post(callback);
    }


    /**
     * 获取班级申请
     *
     * @param context
     * @param callback
     * @param classApplyId
     */
    public void httpAuditDetails(Context context, ResultCallback callback, String classApplyId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classApplyId", classApplyId);
        getHttpBuilder(context, "classApply/getClassApply.json").params(params).post(callback);
    }


    //获取待办事项
    public void httpCommission(Context context, ResultCallback callback, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        getHttpBuilder(context, "caseMsg/findList.json").params(params).post(callback);
    }


    //获取作业和测评数量
    public void httpCountInfo(Context context, ResultCallback callback, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        getHttpBuilder(context, "caseMsg/findListforLearningSpace.json").params(params).post(callback);
    }


    public void httpModifyAccount(Context context, ResultCallback callback, String telnum, String xinUsername, String code, String userId, String password) {
        IdentityHashMap params = new IdentityHashMap<>();
//        params.put("telnum", telnum);
//        params.put("xinUsername", xinUsername);
//        params.put("code", code);
//        params.put("userId", userId);
//        params.put("password", password);
//        getHttpBuilder(context, "user/modifyAccount.json").params(params).post(callback);
    }


    /**
     * 意见反馈
     *
     * @param context
     * @param callback
     * @param addUserId
     * @param content
     */
    public void httpOpinion(Context context, ResultCallback callback, String addUserId, String content) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("addUserId", addUserId);
        params.put("content", content);
//        getHttpBuilder(context, "index/createSuggest.json").params(params).post(callback);
    }

    /**
     * 使用帮助
     */
    public void httpHelp(Context context, String type, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("type", type);
//        getHttpBuilder(context, "cms/findCms.json").params(params).post(callback);
    }

    /**
     * 关于我们
     */
    public void httpAboutUs(Context context, ResultCallback callback) {
        getHttpBuilder(context, "index/aboutUs.json").get(callback);
    }


//    public void httpUserMessage(Context context, ResultCallback callback, String userId, String realName, String email, String birthday, String signature) {
//        IdentityHashMap params = new IdentityHashMap<>();
//        params.put("userId", userId);
//        params.put("realName", realName);
//        params.put("email", email);
//        params.put("birthday", birthday);
//        params.put("signature", signature);
//        getHttpBuilder(context, "user/updateUserInfo.json").params(params).post(callback);
//    }
//
//
//    // 扫码添加班级
//    public void httpAddClassAply(Context context, String userId, String classNo, ResultCallback callback) {
//        IdentityHashMap params = new IdentityHashMap<>();
//        params.put("userId", userId);
//        params.put("classNo", classNo);
//        getHttpBuilder(context, "classApply/addClassApply.json").params(params).post(callback);
//    }

//
//    public void httpUserSex(Context context, ResultCallback callback, String userId, String sex) {
//        IdentityHashMap params = new IdentityHashMap<>();
//        params.put("userId", userId);
//        params.put("sex", sex);
//        getHttpBuilder(context, "user/updateUserInfo.json").params(params).post(callback);
//    }


    /**
     * 上传作业答题时的图片
     *
     * @param context
     * @param callback
     * @param file
     * @param userId
     */
    public void uploadPaperImageFile(Context context, ResultCallback callback, File file, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        new OkHttpRequest.Builder().tag(context).url(getFileUrl() + "picture/uploadNoticeImg.json").files(new Pair<String, File>("file", file)).params(params).upload(callback);
    }


    //附件上传
    public void uploadAttachment(Context context, ResultCallback callback, File files, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        new OkHttpRequest.Builder().tag(context).url(getFileUrl() + "attachment/uploadAttachment.json").files(new Pair<String, File>("file", files)).params(params).upload(callback);
    }

    //获取分享数据
    public void shareMsg(Context context, ResultCallback callback, String pictureId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("pictureId", pictureId);
        getHttpBuilder(context, "picture/shareMsg.json").params(params).post(callback);
    }

/*8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888*/

    /*注册*/
    public void httpRegist(Context context, ResultCallback callback, String truename, String sex, String phone, String phonecode, String password, String birthday, String equipment) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("truename", truename);
        params.put("gender", sex);
        params.put("telnum", phone);
        params.put("telcode", phonecode);
        params.put("password", password);
        params.put("birthday", birthday);
        params.put("source", equipment);

        getHttpBuilder(context, "student/public/register").params(params).post(callback);
    }

    /*登录*/
    public void httpLogin(Context context, ResultCallback callback, String userName, String passowrd) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("username", userName);
        params.put("password", passowrd);
        getHttpBuilder(context, "student/public/login").params(params).post(callback);
    }


    //获取手机获取验证码
    public void sendPhoneCode(Context context, ResultCallback callback, String phone) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("telnum", phone);
        getHttpBuilder(context, "student/public/send-code").params(params).post(callback);
    }

    //忘记密码  提交信息接口
    public void changePwd(Context context, ResultCallback callback, String phone, String password, String pwdAgain, String telcode) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("telnum", phone);
        params.put("password", password);
        params.put("repassword", pwdAgain);
        params.put("telcode", telcode);

        getHttpBuilder(context, "site/find-passwd").params(params).post(callback);
    }


    /**
     * 根据班级码查询班级
     *
     * @param context
     * @param callback
     * @param classCode
     */
    public void httpSearchClass(Context context, ResultCallback callback, String classCode) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("code", classCode);

        getHttpBuilder(context, "student/classroom/get-class").params(params).post(callback);
    }

    /**
     * 申请加入班级
     *
     * @param context
     * @param callback
     * @param classId
     */

    public void httpApplyClass(Context context, ResultCallback callback, String classId) {

        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("class_id", classId);

        getHttpBuilder(context, "student/classroom/add-class").params(params).post(callback);
    }

    /**
     * @param context
     * @param callback
     * @param showClassmates 是否显示同班同学
     */
    public void httpGetContactList(Context context, ResultCallback callback, boolean showClassmates) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        if (showClassmates) {
            params.put("show_classmates", "1");
        } else {
            params.put("show_classmates", "0");
        }
        getHttpBuilder(context, "student/classroom/contact").params(params).post(callback);
    }

    /*卡拉ok*/
    public void httpGetKaraOke(Context context, ResultCallback callback, String bookId, String unitId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("book_id", bookId);
        params.put("unit_id", unitId);
        getHttpBuilder(context, "class-book/get-karaoke").params(params).post(callback);
    }


    /*首页获取学生作业列表*/
    public void httpGetStudentHomeWork(Context context, ResultCallback callback, int pageSize, int page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("pageSize", pageSize + "");//分页显示数目
        params.put("page", page + "");//当前页数
        getHttpBuilder(context, "student/homework/list").params(params).post(callback);
    }

    /*首页获取学生作业列表详情*/
    public void httpGetStudentHomeWorkDetails(Context context, ResultCallback callback, String homework_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        Logger.d("UserController.getInstance().getUser().getToken():" + UserController.getInstance().getUser().getToken());
        Logger.d("homework_id:" + homework_id);
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("homework_id", homework_id);//分页显示数目

        getHttpBuilder(context, "/student/homework/answersheet").params(params).post(callback);
    }

    /*获取学生作业所有题*/
    public void httpGetStudentHomeWorkhomeworkContinue(Context context, ResultCallback callback, String homework_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("homework_id", homework_id);
        getHttpBuilder(context, "student/homework/questions").params(params).post(callback);

    }

    /*提交答题卡*/
    public void httpSubmitAnswerCard(Context context, ResultCallback callback, String homework_id, String is_complete, String json) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("homework_id", homework_id);
        params.put("is_complete", is_complete);//-1 未开始 0 未完成 1 已完成
        params.put("json", json);
        getHttpBuilder(context, "student/homework/submit").params(params).post(callback);

    }

    /*自学首页*/
    public void httpStudyHomePage(Context context, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());

        getHttpBuilder(context, "student/book/self-study").params(params).post(callback);
    }

    /*选择教材*/
    public void httpChoiceTeachingMaterial(Context context, ResultCallback callback, int pageSize, int page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("pageSize", pageSize + "");
        params.put("page", page + "");
        getHttpBuilder(context, "student/book/series").params(params).post(callback);
    }

    /*选择教材系列下的列表*/
    public void httpChoiceTeachingMaterialInfo(Context context, ResultCallback callback, String org_id, String series, String pageSize, String page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("org_id", org_id);
        params.put("series", series);
//        params.put("pageSize", pageSize);
        params.put("page", page);
        getHttpBuilder(context, "student/book/list").params(params).post(callback);
    }

    /*选择教材系列下的列表 点击开始学习*/
    public void ChoiceTeachingMaterialBook(Context context, ResultCallback callback, String org_id, String book_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("org_id", org_id);
        params.put("book_id", book_id);

        getHttpBuilder(context, "student/book/select").params(params).post(callback);
    }


    /*我的*/
    public void httpMine(Context context, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());

        getHttpBuilder(context, "student/").params(params).post(callback);
    }

    /*我的班级*/
    public void httpMyClass(Context context, ResultCallback callback, String pageSize, String page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("pageSize", UserController.getInstance().getUser().getToken());
        params.put("page", UserController.getInstance().getUser().getToken());

        getHttpBuilder(context, "student/classroom/list").params(params).post(callback);
    }

    /*班级详情*/
    public void httpClassDetail(Context context, ResultCallback callback, String class_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("class_id", class_id);

        getHttpBuilder(context, "student/classroom/detail").params(params).post(callback);
    }

    /*退出班级*/
    public void httpQuitClass(Context context, ResultCallback callback, String class_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
//        params.put("class_id", class_id);
//        params.put("class_id", class_id);

        getHttpBuilder(context, "student/ ").params(params).post(callback);
    }

    /*班级空间 接口暂时用教师端的接口*/
    public void httpClassSpace(Context context, ResultCallback callback, String class_id, String pageSize, String page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("class_id", class_id);
        params.put("pageSize", pageSize);
        params.put("page", page);

        getHttpBuilder(context, "teacher/classroom/dynamic").params(params).post(callback);
    }

    /*摩尔通知*/
    public void httpNotice(Context context, ResultCallback callback, int pageSize, int page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("pageSize", pageSize + "");
        params.put("page", page + "");

        getHttpBuilder(context, "teacher/notice/index").params(params).post(callback);
    }

    /*已读通知*/
    public void httpNoticeRead(Context context, ResultCallback callback, String notice_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("notice_id", notice_id);
        getHttpBuilder(context, "teacher/notice/read").params(params).post(callback);
    }

    /*清空通知*/
    public void httpCleanNotice(Context context, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());

        getHttpBuilder(context, "teacher/notice/clear").params(params).post(callback);
    }

    /*点读本*/
    public void httpReadingBook(Context context, ResultCallback callback, String book_id, String unit_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("book_id", book_id);
        params.put("unit_id", unit_id);

        getHttpBuilder(context, "class-book/get-touch-and-read").params(params).post(callback);
    }

    /*课本剧*/
    public void httpTextBookDrama(Context context, ResultCallback callback, String book_id, String unit_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("book_id", book_id);
        params.put("unit_id", unit_id);

        getHttpBuilder(context, "class-book/get-textbook-play").params(params).post(callback);
    }

    /*随身听*/
    public void httpPersonalStereo(Context context, ResultCallback callback, String book_id, String unit_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("book_id", book_id);
        params.put("unit_id", unit_id);

        getHttpBuilder(context, "class-book/get-walkman").params(params).post(callback);
    }

    /*点读本*/
    public void httpCardPratice(Context context, ResultCallback callback, String book_id, String unit_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("book_id", book_id);
        params.put("unit_id", unit_id);

        getHttpBuilder(context, "class-book/get-word-card").params(params).post(callback);
    }

    /*练习*/
    public void httpExercise(Context context, ResultCallback callback, String book_id, String unit_id) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("book_id", book_id);
        params.put("unit_id", unit_id);

        getHttpBuilder(context, "class-book/exercise").params(params).post(callback);
    }

    /*展示用户信息*/
    public void httpShowUserProfileInfo(Context context, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        getHttpBuilder(context, "teacher/member/index").params(params).post(callback);
    }

    /**
     * 上传头像
     *
     * @param context
     * @param callback
     * @param files
     */
    public void uploadUserIcon(Context context, ResultCallback callback, File files) {
        Bitmap map = ImageUtil.uriToBitmap(Uri.fromFile(files), context);
        map = ImageUtil.centerSquareScaleBitmap(map, 400);
        File file = ImageUtil.bitmapToFile(map, UserController.getInstance().getUserImageFile(context) + System.currentTimeMillis() + ".jpg", -1);
        if (file == null) {
            ToastUtil.showToast(context, "文件错误无法提交");
            callback.onError(null, null);
        } else {
            IdentityHashMap params = new IdentityHashMap<>();
            params.put("token", UserController.getInstance().getUser().getToken());

            new OkHttpRequest.Builder().tag(context).url(getFileUrl() + "uploader/image").files(new Pair<String, File>("file", file)).params(params).upload(callback);
        }
    }

    /*修改用户信息*/
    public void httpChangeUserInfo(Context context, ResultCallback callback, int type, String info) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        switch (type) {
            case 0://修改头像
                params.put("avatar", info);
                break;
            case 1://修改姓名
                params.put("nickname", info);
                break;
            case 2://修改性别
                params.put("gender", info);
                break;
            case 3://生日
                params.put("birthday", info);
                break;

        }
        getHttpBuilder(context, "teacher/member/update").params(params).post(callback);
    }

    /*修改手机号*/
    public void httpModifyPhone(Context context, ResultCallback callback, String telnum, String telcode) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("telnum", telnum);
        params.put("telcode", telcode);

        getHttpBuilder(context, "teacher/setting/telnum").params(params).post(callback);
    }

    /*修改密码*/
    public void httpModifyPwd(Context context, ResultCallback callback, String oldPassword, String new_password, String re_password) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("password", oldPassword);
        params.put("new_password", new_password);
        params.put("re_password", re_password);

        getHttpBuilder(context, "teacher/setting/password").params(params).post(callback);

    }

    /*保存我的作品*/
    public void httpSaveMineWorks(Context context, ResultCallback callback,
                                  String page_id, String date,
                                  String videoPath, String thumbnailPath,
                                  String score, String title,
                                  String author) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("page_id", page_id);
        params.put("date", date);
        params.put("score", score);
        params.put("title", title);
        params.put("author", author);
//        params.put("description", title);
        Pair<String, File> videofile = new Pair<String, File>("video", new File(videoPath));
        Pair<String, File> thumbnailfile = new Pair<String, File>("thumbnail", new File(thumbnailPath));
        new OkHttpRequest.Builder().tag(context).url(getUrl() + "student/work/save").
                files(videofile, thumbnailfile).params(params).upload(callback);
    }

    /*获取课本剧*/
    public void httpGetMineWorksTextBookDrama(Context context, ResultCallback callback, int pageSize, int page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("pageSize", pageSize + "");
        params.put("page", page + "");

        getHttpBuilder(context, "student/work/textbook").params(params).post(callback);
    }

    /*获取卡拉OK*/
    public void httpGetMineWorksKalaOke(Context context, ResultCallback callback, int pageSize, int page) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("token", UserController.getInstance().getUser().getToken());
        params.put("pageSize", pageSize + "");
        params.put("page", page + "");

        getHttpBuilder(context, "student/work/karaoke").params(params).post(callback);
    }
}

