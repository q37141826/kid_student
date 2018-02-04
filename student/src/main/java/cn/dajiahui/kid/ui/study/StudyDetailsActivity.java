package cn.dajiahui.kid.ui.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.DownloadFile;
import cn.dajiahui.kid.http.OnDownload;
import cn.dajiahui.kid.http.bean.BeDownFile;
import cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity;
import cn.dajiahui.kid.ui.study.adapter.ApChoiceStudy;
import cn.dajiahui.kid.ui.study.bean.BeChoiceStudy;
import cn.dajiahui.kid.ui.study.kinds.cardpractice.CardPracticeActivity;
import cn.dajiahui.kid.ui.study.kinds.personalstereo.PersonalStereoActivity;
import cn.dajiahui.kid.ui.study.kinds.readingbook.ReadingBookActivity;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.TextBookDramaActivity;
import cn.dajiahui.kid.ui.study.view.OpenGrildView;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;

/*6大题型的学习首页*/
public class StudyDetailsActivity extends FxActivity {

    private OpenGrildView mGrildview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setfxTtitle(11111);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_study_details);
        initialize();

        final List<BeChoiceStudy> list = new ArrayList<>();

        list.add(new BeChoiceStudy("1", "", (20) + "", "点读本", ""));
        list.add(new BeChoiceStudy("2", "", (50) + "", "课本剧", ""));
        list.add(new BeChoiceStudy("3", "", (80) + "", "K拉OK", ""));
        list.add(new BeChoiceStudy("4", "", (10) + "", "卡片练习", ""));
        list.add(new BeChoiceStudy("5", "", (30) + "", "随身听", ""));
        list.add(new BeChoiceStudy("6", "", (50) + "", "练习", ""));

        ApChoiceStudy apChooseSupplementary = new ApChoiceStudy(StudyDetailsActivity.this, list);
        mGrildview.setAdapter(apChooseSupplementary);


        mGrildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, list.get(position).getStudyname(), Toast.LENGTH_SHORT).show();

                /*点读*/
                /*网络请求  下载音频*/


                switch (list.get(position).getType()) {

                    case Constant.READINGBOOK:
                        deleteTemp();
                        initDownMp3();
                        break;
                    case Constant.TEXTBOOKPLAY:
                        deleteTemp();

                         /*进行判断后在跳转*/
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, TextBookDramaActivity.class);
                        /*  应该改成先跳转  然后现用现下载 */
//                        downloadTextBookPlayData();
                        break;
                    case Constant.KARAOKE:

                        break;
                    case Constant.CAREDPRATICE:

                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, CardPracticeActivity.class);

                        break;
                    case Constant.PERSONALSTEREO:
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, PersonalStereoActivity.class);

                        break;
                    case Constant.PRATICE:
                        /*练习与作业复用一个activity*/

                        Bundle bundle = new Bundle();
                        bundle.putString("SourceFlag", "Practice");
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, DoHomeworkActivity.class, bundle, 0);

                        break;

                }

            }
        });

    }

    /*下载课本剧资源*/
    private void downloadTextBookPlayData() {
        BeDownFile file = new BeDownFile(Constant.file_textbookplay, "http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/media/JNxwSTSBAW.mp4", "", KidConfig.getInstance().getPathTemp());
          /*下载成功后跳转 ReadingBookActivity*/
        new DownloadFile(this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {

                    /*关闭下载dialog*/
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                /*进行判断后在跳转*/
                DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, TextBookDramaActivity.class);

                Logger.d("majin-------------课本剧" + fileurl);


            }
        });
    }

    /*初始化*/
    private void initialize() {
        mGrildview = getView(R.id.grildview);
    }

    /*下载点读本资源*/
    private void initDownMp3() {
           /*下载到临时文件夹下  1.  2.文件类型 3.文件网络地址 4.本地路径  name 保存本地文件的名字*/
        BeDownFile file = new BeDownFile(Constant.file_pointreading, "http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3", "", KidConfig.getInstance().getPathTemp());

             /*下载成功后跳转 ReadingBookActivity*/
        new DownloadFile(this, file, false, new OnDownload() {
            @Override
            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
                /*进行判断后在跳转*/
                DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, ReadingBookActivity.class);
                Logger.d("majin-------------fileurl" + fileurl);
                Logger.d("majin-------------点读本" + fileurl);
                  /*关闭下载dialog*/
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        });

    }

    /*清空临时文件*/
    private void deleteTemp() {
        FileUtil fileUtil = new FileUtil();
        fileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathMixAudios()));

    }
}
