package cn.dajiahui.kid.util;


import com.fxtx.framework.widgets.badge.Badgeable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.controller.Constant;

/**
 * Created by z on 2016/1/27.
 */
public class StudentUtil {
    private static int[] driver = {R.drawable.all_bg_line1, R.drawable.all_bg_line2};

    public static int getDriverBg(int index) {
        int resid = 0;
        if (index == 0)
            resid = 1;
        if ((index + 1) % 3 != 0) {
            resid = 1;
        }
        return driver[resid];
    }
    public static void setBadge(Badgeable helper ,int count){
        if(count==0){
            helper.hiddenBadge();
        }else {
            helper.showTextBadge(count+"");
        }
    }

    //验证长度和特殊标点
    public static boolean loginUserLenght(String user) {
        String temp = user.replaceAll("[\u4E00-\u9FA5]", "");
        int lenght = (user.length() - temp.length()) * 3 + temp.length();
        String regEx = "^[\u4E00-\u9FA5A-Za-z0-9]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(user);
        boolean b = m.find();
        if (((lenght >= AppSet.login_minlenght) && (lenght <= AppSet.login_maxlenght)) && (b)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getLastName(int type) {
        int resId;
        switch (type) {
            case Constant.file_img:
                resId = R.drawable.swyywk_img;
                break;
            case Constant.file_word:
                resId = R.drawable.swyywk_word;
                break;
            case Constant.file_ppt:
                resId = R.drawable.swyywk_ppt;
                break;
            case Constant.file_excel:
                resId = R.drawable.swyywk_excel;
                break;
            case Constant.file_pdf:
                resId = R.drawable.swyywk_pdf;
                break;
            case Constant.file_mp3:
                resId = R.drawable.swyywk_audio;
                break;
            case Constant.file_video:
                resId = R.drawable.swyywk_video;
                break;
            case Constant.file_paper:
                resId = R.drawable.swyywk_page;
                break;
            case Constant.file_wps:
                resId = R.drawable.swyywk_wps;
                break;
            default:
                resId = R.drawable.swyywk_no;
                break;
        }
        return resId;
    }


    public static int setFileRes(int fileType) {
        int resiD;
        switch (fileType) {
            case Constant.file_excel:
                resiD = R.drawable.ico_file_excle;
                break;
            case Constant.file_img:
                resiD = R.drawable.ico_file_img;
                break;
            case Constant.file_mp3:
                resiD = R.drawable.ico_file_mp3;
                break;
            case Constant.file_pdf:
                resiD = R.drawable.ico_file_pdf;
                break;
            case Constant.file_ppt:
                resiD = R.drawable.ico_file_ppt;
                break;
            case Constant.file_word:
                resiD = R.drawable.ico_file_word;
                break;
            case Constant.file_video:
                resiD = R.drawable.ico_file_mp4;
                break;
            case Constant.file_wps:
                resiD = R.drawable.ico_file_wps;
                break;
            default:
                resiD = R.drawable.ico_file_no;
                break;
        }
        return resiD;
    }
}
