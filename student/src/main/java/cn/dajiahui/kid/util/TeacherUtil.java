package cn.dajiahui.kid.util;


import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.widgets.badge.Badgeable;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.AppSet;
import cn.dajiahui.kid.controller.Constant;


/**
 * Created by z on 2016/1/27.
 */
public class TeacherUtil {

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



    public static void setBadge(Badgeable helper, int count) {
        if (count == 0) {
            helper.hiddenBadge();
        } else {
            helper.showTextBadge(count + "");
        }
    }


    //验证长度
    public static boolean loginUserLenght(String user) {
        String temp = user.replaceAll("[\u4E00-\u9FA5]", "");
        int lenght = (user.length() - temp.length()) * 3 + temp.length();
        if (lenght >= AppSet.login_minlenght && lenght <= AppSet.login_maxlenght) {
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

    public static int setFileRes(int fileType, boolean isBig) {
        int resiD;
        switch (fileType) {
            case Constant.file_excel:
                resiD = isBig ? R.drawable.ico_file_excle_big : R.drawable.ico_file_excle;
                break;
            case Constant.file_img:
                resiD = isBig ? R.drawable.ico_file_img_big : R.drawable.ico_file_img;
                break;
            case Constant.file_mp3:
                resiD = isBig ? R.drawable.ico_file_mp3_big : R.drawable.ico_file_mp3;
                break;
            case Constant.file_pdf:
                resiD = isBig ? R.drawable.ico_file_pdf_big : R.drawable.ico_file_pdf;
                break;
            case Constant.file_ppt:
                resiD = isBig ? R.drawable.ico_file_ppt_big : R.drawable.ico_file_ppt;
                break;
            case Constant.file_word:
                resiD = isBig ? R.drawable.ico_file_word_big : R.drawable.ico_file_word;
                break;
            case Constant.file_video:
                resiD = isBig ? R.drawable.ico_file_mp4_big : R.drawable.ico_file_mp4;
                break;
            case Constant.file_wps:
                resiD = isBig ? R.drawable.ico_file_swf_big : R.drawable.ico_file_swf;
                break;
            default:
                resiD = isBig ? R.drawable.ico_file_no_big : R.drawable.ico_file_no;
                break;
        }
        return resiD;
    }

    public static int setFileType(String url) {
        int i = R.drawable.ico_file_no_big;
        if (StringUtil.isFileType(url, StringUtil.wordType)) {
            i = R.drawable.ico_file_word_big;
        } else if (StringUtil.isFileType(url, StringUtil.excleType)) {
            i = R.drawable.ico_file_excle_big;
        } else if (StringUtil.isFileType(url, StringUtil.pdfType)) {
            i = R.drawable.ico_file_pdf_big;
        } else if (StringUtil.isFileType(url, StringUtil.pptType)) {
            i = R.drawable.ico_file_ppt_big;
        } else if (StringUtil.isFileType(url, StringUtil.txtType)) {
            i = R.drawable.ico_file_txt_big;
        } else if (StringUtil.isFileType(url, StringUtil.imageType)) {
            i = R.drawable.ico_file_img_big;
        } else if (StringUtil.isFileType(url, StringUtil.videoType)) {
            i = R.drawable.ico_file_mp4_big;
        } else if (StringUtil.isFileType(url, StringUtil.audioType)) {
            i = R.drawable.ico_file_mp3_big;
        } else if (StringUtil.isFileType(url, StringUtil.wpsType)) {
            i = R.drawable.ico_file_swf_big;
        }
        return i;
    }


    public static int getFileType(String url) {
        int i = -1;
        if (StringUtil.isFileType(url, StringUtil.imageType)) {
            i = Constant.file_img;
        } else if (StringUtil.isFileType(url, StringUtil.videoType)) {
            i = Constant.file_video;
        } else if (StringUtil.isFileType(url, StringUtil.audioType)) {
            i = Constant.file_mp3;
        } else if (StringUtil.isFileType(url, StringUtil.wordType) || StringUtil.isFileType(url, StringUtil.txtType)) {
            i = Constant.file_word;
        } else if (StringUtil.isFileType(url, StringUtil.excleType)) {
            i = Constant.file_excel;
        } else if (StringUtil.isFileType(url, StringUtil.pdfType)) {
            i = Constant.file_pdf;
        } else if (StringUtil.isFileType(url, StringUtil.pptType)) {
            i = Constant.file_ppt;
        } else if (StringUtil.isFileType(url, StringUtil.wpsType)) {
            i = Constant.file_wps;
        }
        return i;
    }

}
