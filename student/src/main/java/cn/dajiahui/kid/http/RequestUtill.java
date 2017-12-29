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
import com.fxtx.framework.text.StringUtil;

import java.io.File;
import java.util.IdentityHashMap;

import cn.dajiahui.kid.BuildConfig;
import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.controller.UserController;





/**
 * Created by z on 2016/1/20.
 * 网络请求处理工具
 */
public class RequestUtill {
    private static RequestUtill util;

    public final String homeworkUrl = getUrl() + "doQuestion/initView?";
    public final String lessonkUrl = getUrl() + "workAta/paper?";//微课试卷连接
    public final String errorbookUrl = getUrl() + "workAta/errorBook?";//错题本跳转连接
    public final String saveQuestion = getUrl() + "doQuestion/showQuestion?";//添加错题本
    public final String accessoryUrl = getUrl() + "material/fileShow?httpFileUrl=";//附件连接

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
        //文件名称 和文件地址
        httpDownFile(context, url, callback, fileName, UserController.getInstance().getUserMaterial(context));
    }

    /**
     * 版本更新
     *
     * @return key:entity
     */
    public void httpUpdateApp(Context context, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("versionType", "0");
        params.put("type", "2");
        getHttpBuilder(context, "version/update.json").params(params).post(callback);
    }

    public void httpRegist(Context context, ResultCallback callback, String userName,
                           String password, String confimPassword, String classNo, String email,
                           String phone, String birthday, String sex, String realName) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("confimPassword", confimPassword);
        params.put("classNo", classNo);
        params.put("email", email);
        params.put("phone", phone);
        params.put("birthday", birthday);
        params.put("sex", sex);
        params.put("realName", realName);
        getHttpBuilder(context, "user/register.json").params(params).post(callback);
    }

    /**
     * 登录
     */
    public void httpLogin(Context context, ResultCallback callback, String userName, String passowrd) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("username", userName);
        params.put("password", passowrd);
        params.put("plat", "android");
        getHttpBuilder(context, "login/studentLogin.json").params(params).post(callback);
    }

    /**
     * 获取某月的所有课程
     *
     * @param monthNum: -0代表当前月 1是代表下一个月，-1代表上一个月 其他以此类推
     */
    public void httpLessonsMonth(Context context, ResultCallback callback, String userid, String monthNum) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userid);
        params.put("monthNum", monthNum);
        getHttpBuilder(context, "user/studentLessonsOfMonth.json").params(params).post(callback);
    }


    /**
     * 获取周课程
     *
     * @param dateStr "日期字符串 格式"yyyy-MM-dd"
     * @param weekNum 0代表当前周 1是代表下一个周，-1代表上一个周 其他以此类推
     */
    public void httpLessonWeek(Context context, ResultCallback callback, String userid, String dateStr, String weekNum) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userid);
        params.put("dateStr", dateStr);
        params.put("weekNum", weekNum);
        getHttpBuilder(context, "user/studentLessonsOfWeek.json").params(params).post(callback);
    }


    /**
     * 获取教师班级和课程信息
     */
    public void httpLessonClass(Context context, ResultCallback callback, String userid, int pageSize, String pageNum) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userid);
        params.put("pageSize", pageSize + "");
        params.put("pageNum", pageNum);
        getHttpBuilder(context, "orgClass/getMyClassWithLesson.json").params(params).post(callback);
    }

    /**
     * 获取班级和课程列表信息
     */
    public void httpCourse(Context context, ResultCallback callback, String classId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classId", classId);
        params.put("userId", UserController.getInstance().getUserId());
        getHttpBuilder(context, "orgClass/getMyClassAndLesson.json").params(params).post(callback);
    }


    /**
     * 获取课次详情
     */
    public void httpLessonDetail(Context context, ResultCallback callback, String userId, String lessonId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("lessonId", lessonId);
        params.put("userId", userId);
        getHttpBuilder(context, "orgClass/getLessonDetail.json").params(params).post(callback);
    }

    public void addDownCount(Context context, String id, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("materialId", id);
        new OkHttpRequest.Builder().tag(context).url(getUrl() + "material/addCount.json").params(params).get(callback);
    }

    /**
     * 获取作业列表
     */
    public void httpPaper(Context context, ResultCallback callback, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        getHttpBuilder(context, "classPaper/getClassPaper.json").params(params).post(callback);
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

    /**
     * 微课班级列表
     */
    public void httpMicroKwngList(Context context, ResultCallback callback, String userId, int num, String size) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pageSize", size);
        params.put("pageNum", num + "");
        getHttpBuilder(context, "microKwng/getMyClassWithMicroKwng.json").params(params).post(callback);
    }

    public void httpPictureDetails(Context context, ResultCallback callback, String userId, String pictureId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pictureId", pictureId);
        getHttpBuilder(context, "picture/getById.json").params(params).post(callback);
    }

    public void httpAddCommnet(Context context, ResultCallback callback, String userId, String pictureId, String content, String parentId, String replyUserId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pictureId", pictureId);
        params.put("content", content);
        params.put("parentId", parentId);
        params.put("replyUserId", replyUserId);
        getHttpBuilder(context, "picture/addComment.json").params(params).post(callback);
    }


    /**
     * 微客详情
     */
    public void httpMicroKwng(Context context, ResultCallback callback, String classId, int num, String size) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classId", classId);
        params.put("pageSize", size);
        params.put("pageNum", num + "");
        getHttpBuilder(context, "microKwng/getMyClassMicroKwng.json").params(params).post(callback);
    }

    /**
     * 评价(我的)
     */
    public void httpOpinionDetails(Context context, ResultCallback callback, String classId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classId", classId);
        params.put("userId", UserController.getInstance().getUserId());
        getHttpBuilder(context, "studentEvaluate/getMyClassAndLessonWithStudentEvaluate.json").params(params).post(callback);
    }

    /**
     * 评价(教师)
     */
    public void httpOpinionTeacherDetails(Context context, ResultCallback callback, String userId, String classId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("classId", classId);
        getHttpBuilder(context, "teacherEvaluate/getMyClassAndLessonWithTeacherEvaluate.json").params(params).post(callback);
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

    public void httpEvaluatInfo(Context context, ResultCallback callback, String userid, String lessonId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userid);
        params.put("lessonId", lessonId);
        getHttpBuilder(context, "studentEvaluate/findStudentEvaluate.json").params(params).post(callback);
    }

    public void httpEvaluatTeacherInfo(Context context, ResultCallback callback, String userid, String lessonId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userid);
        params.put("lessonId", lessonId);
        getHttpBuilder(context, "teacherEvaluate/findTeacherEvaluate.json").params(params).post(callback);
    }



    //获取通知列表
    public void httpNoticeList(Context context, ResultCallback callback, String userId, int num, String size) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pageSize", size);
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

    //修改绑定手机  获取验证码
    public void httpSendCode(Context context, ResultCallback callback, String access_token, String username, String telnum, String password) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", access_token);
        params.put("username", username);
        params.put("telnum", telnum);
        params.put("password", password);
        getHttpBuilder(context, "user/modifyTelnumSendCode.json").params(params).post(callback);
    }

    public void httpUserSendCode(Context context, ResultCallback callback, String access_token, String telnum) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", access_token);
        params.put("telnum", telnum);
        getHttpBuilder(context, "user/sendCode.json").params(params).post(callback);
    }

    public void httpModifyAccount(Context context, ResultCallback callback, String telnum, String xinUsername, String code, String userId, String password) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("telnum", telnum);
        params.put("xinUsername", xinUsername);
        params.put("code", code);
        params.put("userId", userId);
        params.put("password", password);
        getHttpBuilder(context, "user/modifyAccount.json").params(params).post(callback);
    }

    public void httpModifyPwd(Context context, ResultCallback callback, String access_token, String username, String oldPassword, String xinPassword, String confirmPassword) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", access_token);
        params.put("username", username);
        params.put("oldPassword", oldPassword);
        params.put("xinPassword", xinPassword);
        params.put("confirmPassword", confirmPassword);
        getHttpBuilder(context, "user/modifyPassword.json").params(params).post(callback);

    }

    public void httpboundPhone(Context context, ResultCallback callback, String userId, String telnum, String captcha) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("telnum", telnum);
        params.put("captcha", captcha);
        getHttpBuilder(context, "user/boundPhone.json").params(params).post(callback);
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
        getHttpBuilder(context, "index/createSuggest.json").params(params).post(callback);
    }

    /**
     * 使用帮助
     */
    public void httpHelp(Context context, String type, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("type", type);
        getHttpBuilder(context, "cms/findCms.json").params(params).post(callback);
    }

    /**
     * 关于我们
     */
    public void httpAboutUs(Context context, ResultCallback callback) {
        getHttpBuilder(context, "index/aboutUs.json").get(callback);
    }

    /**
     * @param context
     * @param callback
     */
    public void httpTest(Context context, ResultCallback callback, String userId, String type) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("type", type);
        getHttpBuilder(context, "classPaper/getMyClassTestForStudent.json").params(params).post(callback);
    }

    /**
     * @param context
     * @param callback
     * @param userId
     * @param pageSize
     * @param pageNum
     */
    public void httpAttence(Context context, ResultCallback callback, String userId, String pageSize, int pageNum, int type) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum + "");
        if (type == Constant.all_attence) {
            getHttpBuilder(context, "attence/getMyClassForStudent.json").params(params).post(callback);
        } else if (type == Constant.all_op_teacher) {
            getHttpBuilder(context, "teacherEvaluate/getMyClassForStudent.json").params(params).post(callback);
        } else if (type == Constant.all_op_my) {
            getHttpBuilder(context, "studentEvaluate/getMyClassForStudent.json").params(params).post(callback);
        }

    }

    /**
     * 考勤详情
     *
     * @param context
     * @param callback
     * @param userId
     * @param classId
     */
    public void httpAttenceDetail(Context context, ResultCallback callback, String userId, String classId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("classId", classId);
        getHttpBuilder(context, "attence/getMyClassAndLessonWithAttence.json").params(params).post(callback);
    }

    public void httpContactDetail(Context context, ResultCallback callback, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        getHttpBuilder(context, "user/studentContacts.json").params(params).post(callback);
    }

    public void httpWrongQuestion(Context context, ResultCallback callback, String userId, String pageSize, int pageNum) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum + "");
        getHttpBuilder(context, "wrongQuestion/getMyWrongQuestion.json").params(params).post(callback);
    }

    /**
     * 错题统计
     *
     * @param context
     * @param callback
     * @param userId
     */
    public void httpWrongQuesCount(Context context, ResultCallback callback, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        getHttpBuilder(context, "wrongQuestion/getMyWrongQuestionReport.json").params(params).post(callback);
    }

    /**
     * 上传头像
     *
     * @param context
     * @param callback
     * @param files
     * @param userId
     */
    public void uploadUserIcon(Context context, ResultCallback callback, File files, String userId) {
        Bitmap map = ImageUtil.uriToBitmap(Uri.fromFile(files), context);
        map = ImageUtil.centerSquareScaleBitmap(map, 400);
        File file = ImageUtil.bitmapToFile(map, UserController.getInstance().getUserImageFile(context) + System.currentTimeMillis() + ".jpg", -1);
        if (file == null) {
            ToastUtil.showToast(context, "文件错误无法提交");
            callback.onError(null, null);
        } else {
            IdentityHashMap params = new IdentityHashMap<>();
            params.put("userId", userId);
            new OkHttpRequest.Builder().tag(context).url(getFileUrl() + "user/uploadAvator.json").files(new Pair<String, File>("file", file)).params(params).upload(callback);
        }

    }

    public void httpUserMessage(Context context, ResultCallback callback, String userId, String realName, String email, String birthday, String signature) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("realName", realName);
        params.put("email", email);
        params.put("birthday", birthday);
        params.put("signature", signature);
        getHttpBuilder(context, "user/updateUserInfo.json").params(params).post(callback);
    }

    /**
     * 获取消息类型
     *
     * @param context
     * @param type     1教师端消息类型 2学生端消息类型
     * @param callback
     */
    public void httpMessageType(Context context, String type, String userId, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("type", type);
        params.put("userId", userId);
        getHttpBuilder(context, "caseMsg/getCaseMsgType.json").params(params).post(callback);

    }

    public void httpMessageByType(Context context, String userId, String type, ResultCallback callback, String pageSize, String pageNum) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        if (!StringUtil.isEmpty(type)) {
            params.put("type", type);
        }

        getHttpBuilder(context, "caseMsg/findMsgs.json").params(params).post(callback);
    }

    public void httpReApply(Context context, String classApplyId, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classApplyId", classApplyId);
        getHttpBuilder(context, "classApply/reApply.json").params(params).post(callback);
    }

    // 扫码添加班级
    public void httpAddClassAply(Context context, String userId, String classNo, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("classNo", classNo);
        getHttpBuilder(context, "classApply/addClassApply.json").params(params).post(callback);
    }

    //视频
    public void httpVideo(Context context, String userId, String mid, ResultCallback callback) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("sid", userId);
        params.put("mid", mid);
        getHttpBuilder(context, "/material/getMaterialViewInfo.json").params(params).post(callback);
    }



    public void httpUserSex(Context context, ResultCallback callback, String userId, String sex) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("sex", sex);
        getHttpBuilder(context, "user/updateUserInfo.json").params(params).post(callback);
    }

    public void httpReadMsg(Context context, ResultCallback callback, String caseMsgId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("caseMsgId", caseMsgId);
        getHttpBuilder(context, "caseMsg/readMsg.json").params(params).post(callback);
    }

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

    public void httpUpLoadPaper(Context context, ResultCallback callback, String classPaperId, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classPaperId", classPaperId);
        params.put("userId", userId);
        getHttpBuilder(context, "classPaper/submitPaper.json").params(params).post(callback);
    }

    public void httppaperStatus(Context context, ResultCallback callback, String classPaperId, String userId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("classPaperId", classPaperId);
        params.put("userId", userId);
        getHttpBuilder(context, "classPaper/getPaperStatus.json").params(params).post(callback);
    }

    /**
     * 批改
     *
     * @param context
     * @param callback
     * @param myTestDetail
     */
    public void getpgContent(Context context, ResultCallback callback, String myTestDetail) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("myTestDetailId", myTestDetail);
        getHttpBuilder(context, "classPaper/getMyTestDetailById.json").params(params).post(callback);
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

    //忘记密码  获取验证码
    public void sendPhoneCode(Context context, ResultCallback callback, String phone, String userName) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("phone", phone);
        params.put("userName", userName);
        getHttpBuilder(context, "login/sendChangePwdCode.json").params(params).post(callback);
    }

    //忘记密码  提交信息接口
    public void changePwd(Context context, ResultCallback callback, String userName, String phone, String code,
                          String toChangePwd, String pwdAgain) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userName", userName);
        params.put("phone", phone);
        params.put("code", code);
        params.put("toChangePwd", toChangePwd);
        params.put("pwdAgain", pwdAgain);
        getHttpBuilder(context, "login/changePwdCode.json").params(params).post(callback);
    }

    //获取测评报告 或 结课报告
    public void httpReportDate(Context context, ResultCallback callback, String userId, int type, int pageNum) {
        IdentityHashMap params = new IdentityHashMap<>();
        String url = "report/studentPaper/getList.json";
        if (type == R.drawable.ico_re_session) {
            url = "report/studentOver/getList.json";
        } else if (type == R.drawable.ico_re_classroom) {
            url = "report/studentClass/getList.json";
        }
        params.put("uid", userId);
        params.put("pageNum", pageNum + "");
        getHttpBuilder(context, url).params(params).post(callback);
    }

    /**
     * 微课观看记录
     *
     * @param context
     * @param callback
     * @param microKwngMapId
     */
    public void httpLookinfo(Context context, ResultCallback callback, String microKwngMapId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("microKwngMapId", microKwngMapId);
        getHttpBuilder(context, "microKwng/getMyClassMicroKwngLookInfo.json").params(params).post(callback);
    }

    /**
     * 获取需要评价的教师列表
     *
     * @param context
     * @param callback
     * @param userId
     * @param lessonId
     */
    public void httpOpinionTeacherList(Context context, ResultCallback callback, String userId, String lessonId) {
        IdentityHashMap params = new IdentityHashMap<>();
        params.put("userId", userId);
        params.put("lessonId", lessonId);
        getHttpBuilder(context, "teacherEvaluate/teacherEvaluateTeacher.json").params(params).post(callback);
    }



}

