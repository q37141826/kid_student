package cn.dajiahui.kid.controller;

import android.content.Context;

import com.fxtx.framework.FxtxConstant;
import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.platforms.jpush.JpushUtil;

import java.io.File;

import cn.dajiahui.kid.DjhStudentApp;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.ui.login.bean.BeUserAuth;
import cn.dajiahui.kid.util.SpUtil;

/**
 * 用户信息控制器  控制用户基本信息数据
 */
public class UserController {

    private static UserController controller;
    private BeUser user;//用户信息
    private BeUserAuth userAuth; // 用户权限

    private UserController() {

    }

    /*保存用户信息 实例*/
    public void savaUser(BeUser user) {
        this.user = user;
        userAuth = new BeUserAuth();
        if (user.getAuthList() != null)
            for (String auth : user.getAuthList()) {
                isAuth(auth);
            }
    }

    /**
     * 单一实例
     */
    public static UserController getInstance() {
        if (controller == null) {
            synchronized (UserController.class) {
                if (controller == null) {
                    controller = new UserController();
                }
            }
        }
        return controller;
    }

    //返回用户id
    public String getUserId() {
        return getUser().getObjectId();
    }

    //获取用户的图片存储地址
    public String getUserImageFile(Context context) {
        String path = new FileUtil().dirFile(context) +
                File.separator + "djh" +
                File.separator + getUser().getTelnum() +
                File.separator + "classSpace" +
                File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 获取用户资料的路径
     *
     * @param context
     * @return
     */
    public String getUserMaterial(Context context) {
        String path = new FileUtil().dirFile(context) +
                File.separator + "ata" +
                File.separator + getUserId() +
                File.separator + "material" +
                File.separator;
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        return path;
    }

    /**
     * 拍照目录
     *
     * @param context
     * @return
     */
    public String getUserImageAnswer(Context context) {
        String path = new FileUtil().dirFile(context) +
                File.separator + FxtxConstant.ImageFile;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    //用户调用退出登录后的处理
    public void exitLogin(Context context) {
        initBean();
        SpUtil spUtil = new SpUtil(context);
        spUtil.cleanUser();
        JpushUtil.stopJpushAlias(context);
        JpushUtil.clearAllNotifications(context);
    }

    public void initBean() {
        controller = null;
        userAuth = null;
        BadgeController.getInstance().initNum();
    }

    public BeUser getUser() {
        if (user == null)
            user = new SpUtil(DjhStudentApp.getInstance()).getUser();
        return user;
    }

    public BeUserAuth getUserAuth() {
        if (userAuth == null) {
            userAuth = new BeUserAuth();
            if (getUser().getAuthList() != null)
                for (String auth : getUser().getAuthList()) {
                    isAuth(auth);
                }
        }
        return userAuth;
    }

    public void isAuth(String string) {
        switch (string) {
            case Constant.code_class:
                userAuth.isClass = true;
                break;
            case Constant.code_msn:
                userAuth.isMsn = true;
                break;
            case Constant.code_job:
                userAuth.isJob = true;
                break;
            case Constant.code_attend:
                userAuth.isAttend = true;
                break;
            case Constant.code_errQue:
                userAuth.isErrQue = true;
                break;
            case Constant.code_microClass:
                userAuth.isMicroClass = true;
                break;
            case Constant.code_evaluation:
                userAuth.isEvaluation = true;
                break;
            case Constant.code_lesson:
                userAuth.isLesson = true;
                break;
            case Constant.code_album:
                userAuth.isAlbum = true;
                break;
            case Constant.code_stuEval:
                userAuth.isStuEval = true;
                break;
            case Constant.code_stuVerify:
                userAuth.isStuVerify = true;
                break;
            case Constant.code_myFile:
                userAuth.isMyFile = true;
        }
    }
}
