package cn.dajiahui.kid.controller;

/**
 *
 */
public class Constant {
    public static final String broad_badge_count_action = "broad_badeg_count_action_ata_student";//角标广播发送
    /**
     * 系统常量 不允许修改
     **/
    public static String bundle_id = "_id";
    public static String bundle_obj = "_object";
    public static String bundle_title = "_title";
    public static String bundle_type = "_type";
    public static String bundle_ser = "_ser";
    public static String bundle_teaId = "_teaid";
    /**
     * 文件格式
     */
    public static final int file_img = 0;
    public static final int file_word = 1;
    public static final int file_ppt = 2;
    public static final int file_excel = 3;
    public static final int file_pdf = 4;//pdf
    public static final int file_mp3 = 5;//录音
    public static final int file_video = 6;//视频
    public static final int file_paper = 7; //试卷
    public static final int file_wps = 8; //wps


    /**
     * 通过我的班级列表进入的
     */
    public static final int all_class = 0;//我的班级
    public static final int all_course = 4;//微客

    public static final int all_attence = 100;//考勤
    public static final int all_op_my = 101;//我的评价
    public static final int all_op_teacher = 102;//评价教师

    public static final int Album_Photo_Max = 20;//相册可选择上传图片最大值
    public static final int Alum_phone_UserIcon = 1;//相册选择用户头像时只能选一张
    public static final int classNotice = 3;//班级 通知 也叫 教师通知
    public static final int schoolNotice = 2;
    public static final int systemNotice = 1;


    public static final int user_edit_pwd = 1;
    public static final int user_edit_user = 2;
    public static final int user_edit_phone = 3;
    public static final int user_edit_sign = 4;
    public static final int user_edit_birth = 5;
    public static final int user_edit_email = 6;
    public static final int user_edit_name = 7;
    public static final int user_edit_sex = 8;

    //待办
    public static final String type_tz = "tz";//通知
    public static final String type_tzpl = "tzpl";//通知评论
    public static final String type_tzhf = "tzhf";//通知回复

    public static final String type_zybz = "zybz";//作业布置
    public static final String type_pjjs = "pjjs";//评价教师
    public static final String type_wdpj = "wdpj";//我的评价
    public static final String type_cp = "cp";//测评发布

//    public static final String type_jxyj = "jxyj";//教学研究
//    public static final String type_jybg = "jybg";//教学报告
//    public static final String type_jxzj = "jxzj";//教学总结

    //事务  所有待办也是事务
    public static final String type_xc = "xc";//相册
    public static final String type_xcpl = "xcpl";//相册评论
    public static final String type_xchf = "xchf";//相册回复

    public static final String type_xygl = "xygl";//学生管理
    public static final String type_zytj = "zytj";//作业提交
    public static final String type_bjsq = "bjsq";//班级申请
    public static final String type_cptj = "cptj";//测评提交
    public static final String type_cjzy = "cjzy";//催缴作业

    public static final String type_zb = "zb";//转班
    public static final String type_jk = "jk";//结课
    public static final String type_ycbj = "ycbj";//移除班级
    public static final String type_sktx = "sktx";//上课提醒
    public static final String type_rmcs = "rmcs";//入门测试
    public static final String type_zypz = "zypz";//作业批注
    public static final String type_cppz = "cppz";//测评批注

    public static final String type_dmtz = "dmtz";//教师点完名学生收到

    /**
     * 功能拆分code
     * <p/>
     */
    // 依赖：课次 班级
    public static final String code_lesson = "lesson";
    public static final String code_class = "class";

    public static final String code_microClass = "microClass"; // 微课
    public static final String code_album = "album"; // 相册
    public static final String code_stuVerify = "stuVerify"; // 班级申请
    public static final String code_attend = "attend"; // 考勤
    public static final String code_stuEval = "stuEval"; // 我的评价 评价教师
    public static final String code_job = "job"; // 作业
    public static final String code_evaluation = "evaluation"; // 测评
    public static final String code_notice = "notice"; // 通知
    public static final String code_msn = "msn"; // 通讯录 沟通
    public static final String code_errQue = "errQue"; // 错题本
    public static final String code_myFile = "myFile"; // 资料
}
