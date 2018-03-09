package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.adapter.ApHomeWorkDetail;
import cn.dajiahui.kid.ui.homework.bean.BeAnswerSheet;
import cn.dajiahui.kid.ui.homework.bean.BeHomeWorkDetails;
import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;

/*
*
* 答题完成的答题卡
*
* */
public class AnswerCardCompleteActivity extends FxActivity {

    private String homework_id;
    private BeSaveAnswerCard beSaveAnswerCard;
    private TextView tvnoanswer;
    private GridView grildview;
    private LinearLayout linhomeworkdetail;
    private List<BeAnswerSheet> mBeAnswerSheetList = new ArrayList<>();
    private ApHomeWorkDetail apHomeWorkDetail;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_answer_card_complete);

        Bundle bundle = getIntent().getExtras();
        beSaveAnswerCard = (BeSaveAnswerCard) bundle.getSerializable("ANSWER_CARD_COMPLETE");
        homework_id = beSaveAnswerCard.getHomework_id();
        initialize();
        onBackTextShowProgress();
        /*网络请求*/
        httpData();

        apHomeWorkDetail = new ApHomeWorkDetail(AnswerCardCompleteActivity.this, mBeAnswerSheetList);
        grildview.setAdapter(apHomeWorkDetail);


        /*item条目点击事件*/
        grildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("current_num", position);

                setResult(DjhJumpUtil.getInstance().activity_todohomework, intent);
                finishActivity();
            }
        });

    }

    /*初始化*/
    private void initialize() {
        tvnoanswer = getView(R.id.tv_noanswer);
        grildview = (GridView) findViewById(R.id.grildview);
        linhomeworkdetail = (LinearLayout) findViewById(R.id.linhomework_detail);
    }

    @Override
    public void httpData() {
        super.httpData();
        showfxDialog();

        RequestUtill.getInstance().httpGetStudentHomeWorkDetails(AnswerCardCompleteActivity.this, callHomeWorkDetails, homework_id);

    }

    /**
     * 学生作业列表详情callback函数
     */
    ResultCallback callHomeWorkDetails = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {
            Logger.d("作业详情response:" + response);
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                BeHomeWorkDetails beHomeWorkDetails = json.parsingObject(BeHomeWorkDetails.class);
                String is_complete = beHomeWorkDetails.getIs_complete();
                if (beHomeWorkDetails != null) {

                    if (beHomeWorkDetails.getIs_complete().equals("-1")) {


                    } else if (beHomeWorkDetails.getIs_complete().equals("1")) {
                        setfxTtitle("答题卡");

                        mBeAnswerSheetList.addAll(beHomeWorkDetails.getAnswer_sheet());

                        apHomeWorkDetail.notifyDataSetChanged();
                    }
                }

            } else {
                ToastUtil.showToast(AnswerCardCompleteActivity.this, json.getMsg());
            }

        }
    };

    /*半路退出答题*/
    @Override
    public void onBackShowProgress(View view) {
        super.onRightBtnClick(view);

        finishActivity();
    }
}
