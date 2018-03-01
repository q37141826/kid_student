package cn.dajiahui.kid.ui.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.ui.FxActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity;
import cn.dajiahui.kid.ui.study.adapter.ApChoiceStudy;
import cn.dajiahui.kid.ui.study.bean.BeChoiceStudy;
import cn.dajiahui.kid.ui.study.kinds.cardpractice.CardPracticeActivity;
import cn.dajiahui.kid.ui.study.kinds.karaoke.KaraOkeActivity;
import cn.dajiahui.kid.ui.study.kinds.personalstereo.PersonalStereoActivity;
import cn.dajiahui.kid.ui.study.kinds.readingbook.ReadingBookActivity;
import cn.dajiahui.kid.ui.study.kinds.textbookdrama.TextBookDramaActivity;
import cn.dajiahui.kid.ui.study.view.OpenGrildView;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.KidConfig;

/*6大题型的学习首页*/
public class StudyDetailsActivity extends FxActivity {

    private OpenGrildView mGrildview;
    private Bundle studyDetailsBundle;
    private String book_id;
    private String unit_id;
    private String unit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unit_name = studyDetailsBundle.getString("UNIT_NAME");
        setfxTtitle(unit_name);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_study_details);
        studyDetailsBundle = getIntent().getExtras();
        book_id = studyDetailsBundle.getString("BOOK_ID");
        unit_id = studyDetailsBundle.getString("UNIT_ID");
        initialize();

        final List<BeChoiceStudy> list = new ArrayList<>();

        list.add(new BeChoiceStudy("1", R.drawable.readingbook, "", "点读本", ""));
        list.add(new BeChoiceStudy("2", R.drawable.textbookplay, "", "课本剧", ""));
        list.add(new BeChoiceStudy("3", R.drawable.cardpratice, "", "K拉OK", ""));
        list.add(new BeChoiceStudy("4", R.drawable.karaoke, "", "卡片练习", ""));
        list.add(new BeChoiceStudy("5", R.drawable.personalstereo, "", "随身听", ""));
        list.add(new BeChoiceStudy("6", R.drawable.pratice, "", "练习", ""));

        ApChoiceStudy apChooseSupplementary = new ApChoiceStudy(StudyDetailsActivity.this, list);
        mGrildview.setAdapter(apChooseSupplementary);


        mGrildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, list.get(position).getStudyname(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("BOOK_ID", book_id);
                bundle.putString("UNIT_ID", unit_id);
                /*点读*/
                /*网络请求  下载音频*/
                deleteTemp();
                switch (list.get(position).getType()) {

                    case Constant.READINGBOOK:

                        bundle.putString("UNIT_NAME", unit_name);
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, ReadingBookActivity.class, bundle, 0);
                        break;
                    case Constant.TEXTBOOKPLAY:
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, TextBookDramaActivity.class, bundle, 0);
                        break;
                    case Constant.KARAOKE:
                        bundle.putString("UNIT_NAME", unit_name);
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, KaraOkeActivity.class, bundle, 0);

                        break;
                    case Constant.CAREDPRATICE:
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, CardPracticeActivity.class, bundle, 0);

                        break;
                    case Constant.PERSONALSTEREO:
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, PersonalStereoActivity.class, bundle, 0);

                        break;
                    case Constant.PRATICE:
                        /*练习与作业复用一个activity*/

//                        Bundle bundle = new Bundle();
                        bundle.putString("SourceFlag", "Practice");
                        DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, DoHomeworkActivity.class, bundle, 0);

                        break;

                }

            }
        });

    }

//    /*下载课本剧资源*/
//    private void downloadTextBookPlayData() {
//
//        BeDownFile file = new BeDownFile(Constant.file_textbookplay_mp4, "http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/test/hDTD2i2Yxy.mp4", "", KidConfig.getInstance().getPathTemp());
//          /*下载成功后跳转 ReadingBookActivity*/
//        new DownloadFile(this, file, false, new OnDownload() {
//            @Override
//            public void onDownload(String fileurl, FxProgressDialog progressDialog) {
//
//                    /*关闭下载dialog*/
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                /*进行判断后在跳转*/
//                DjhJumpUtil.getInstance().startBaseActivity(StudyDetailsActivity.this, TextBookDramaActivity.class);
//
//                Logger.d("majin-------------课本剧" + fileurl);
//
//
//            }
//        });
//    }

    /*初始化*/
    private void initialize() {
        mGrildview = getView(R.id.grildview);
    }


    /*清空临时文件*/
    private void deleteTemp() {
        FileUtil.deleteAllFiles(new File(KidConfig.getInstance().getPathTemp()));

    }
}
