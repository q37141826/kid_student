package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.http.RequestUtill;
import cn.dajiahui.kid.ui.homework.adapter.ApAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BeAnswerCArd;
import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BeSubmitAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.ToAnswerCardJson;
import cn.dajiahui.kid.util.Logger;

/*
* 答题卡
* */
public class AnswerCardActivity extends FxActivity {
    private int ANSWERCARD = 3;
    private TextView tvtrue;
    private TextView tvfalse;
    private TextView tvnoanswer;
    private TextView tvanswer;
    private GridView grildview;
    private Button mBtnsubmit;
    private int answernum;


    //    private List<QuestionModle> listdata = new ArrayList<>();//显示答题卡的集合
    private BeSaveAnswerCard beSaveAnswerCard;
    private String homework_id;

    List<BeSubmitAnswerCard> submitAnswerCardList = new ArrayList<>();
    private List<BeAnswerCArd> beAnswerCArds;
    private StringBuilder append;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.AnswerCard);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_answer_card);
        initialize();

        Bundle bundle = getIntent().getExtras();
        beSaveAnswerCard = (BeSaveAnswerCard) bundle.getSerializable("answerCard");
        homework_id = beSaveAnswerCard.getHomework_id();
        beAnswerCArds = beSaveAnswerCard.getmAnswerCardList();


        Logger.d("答题卡的数据：" + beAnswerCArds.toString());

        HashMap<Integer, Object> pageMap = beSaveAnswerCard.getPageMap();

        for (int i = 0; i < pageMap.size(); i++) {

            QuestionModle questionModle = (QuestionModle) pageMap.get(i);

            switch (questionModle.getQuestion_cate_id()) {

                case Constant.Judje:
                    JudjeQuestionModle jude = (JudjeQuestionModle) pageMap.get(i);
                    /*如果答过题 自动提交答案的标记默认是0*/
                    if (jude.getMy_answer() != null) {
                        jude.setIs_auto("0");
                        for (int ij = 0; ij < beAnswerCArds.size(); ij++) {
                            if (beAnswerCArds.get(ij).getQuestion_id().equals(jude.getId())) {
                                int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                beAnswerCArds.get(indexOf).setAnswerFlag("true");
                            }
                        }
                    } else {
                        jude.setMy_answer("");
                    }

//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("判断 question_id:----" + jude.getId());
//                    Logger.d("判断 question_cate_id:----" + jude.getQuestion_cate_id());
//                    Logger.d("判断 my_answer:----" + jude.getMy_answer());
//                    Logger.d("判断 is_right:----" + jude.getIs_right());
//                    Logger.d("判断 is_auto:----" + jude.getIs_auto());
//

                    submitAnswerCardList.add(new BeSubmitAnswerCard(jude.getId(), jude.getQuestion_cate_id(), jude.getMy_answer(), jude.getIs_right(), jude.getIs_auto()));

//                    Logger.d( "AnswerCardActivity-----判断getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----判断getAnswerflag:" + anJudje );
                    break;
                case Constant.Choice:
                    ChoiceQuestionModle choice = (ChoiceQuestionModle) pageMap.get(i);

                      /*如果答过题 自动提交答案的标记默认是0*/
                    if (choice.getMy_answer() != null) {
                        choice.setIs_auto("0");
                        for (int ij = 0; ij < beAnswerCArds.size(); ij++) {

                            if (beAnswerCArds.get(ij).getQuestion_id().equals(choice.getId())) {
                                int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                beAnswerCArds.get(indexOf).setAnswerFlag("true");

                            }
                        }
                    } else {
                        choice.setMy_answer("");
                    }

//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("选择 question_id:----" + choice.getId());
//                    Logger.d("选择 question_cate_id:----" + choice.getQuestion_cate_id());
//                    Logger.d("选择 my_answer:----" + choice.getMy_answer());
//                    Logger.d("选择 is_right:----" + choice.getIs_right());
//                    Logger.d("选择 is_auto:----" + choice.getIs_auto());

                    submitAnswerCardList.add(new BeSubmitAnswerCard(choice.getId(), choice.getQuestion_cate_id(), choice.getMy_answer(), choice.getIs_right(), choice.getIs_auto()));


                    break;
                case Constant.Sort:
                    SortQuestionModle sort = (SortQuestionModle) pageMap.get(i);

//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("排序 question_id:----" + sort.getId());
//                    Logger.d("排序 question_cate_id:----" + sort.getQuestion_cate_id());
//                    Logger.d("排序 my_answer:----" + sort.getMy_answer());
//                    Logger.d("排序 is_right:----" + sort.getIs_right());
//                    Logger.d("排序 is_auto:----" + sort.getIs_auto());

//                    submitAnswerCardList.add(new BeSubmitAnswerCard("5", "1", "我的答案", "0", ""));

//                    Logger.d( "AnswerCardActivity-----排序getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----排序getAnswerflag:" + ansort );
                    break;
                case Constant.Line:
                    LineQuestionModle line = (LineQuestionModle) pageMap.get(i);
                    String anline = line.getAnswerflag();

//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("连线 question_id:----" + line.getId());
//                    Logger.d("连线 question_cate_id:----" + line.getQuestion_cate_id());
//                    Logger.d("连线 my_answer:----" + line.getMy_answer());
//                    Logger.d("连线 is_right:----" + line.getIs_right());
//                    Logger.d("连线 is_auto:----" + line.getIs_auto());

//                    submitAnswerCardList.add(new BeSubmitAnswerCard("5", "1", "我的答案", "0", ""));

//                    Logger.d( "AnswerCardActivity-----连线getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----连线getAnswerflag:" + anline );
                    break;
                case Constant.Completion:
                    CompletionQuestionModle completion = (CompletionQuestionModle) pageMap.get(i);
                    Map<Integer, Map<Integer, String>> integerMapMap = completion.getmAllMap();

                    List<Map<Integer, String>> completeList = new ArrayList<>();

                    for (int c = 0; c < integerMapMap.size(); c++) {
                        if (integerMapMap.get(c) != null) {
                            Map<Integer, String> integerStringMap = integerMapMap.get(c);
                            completeList.add(integerStringMap);
                        }
                    }

                    Logger.d("填空我的答案：" + completeList.toString());
                    /*需要和后台商量填空题的提交答案的形式*/

                    /*没有作答答案自动提交*/
                    if (integerMapMap.size() >= 0) {
                        completion.setIs_auto("0");
                        completion.setMy_answer(completeList.toString());

//                        for (int ij = 0; ij < beAnswerCArds.size(); ij++) {
//                            if (beAnswerCArds.get(ij).getQuestion_id().equals(completion.getId())) {
//                                int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
//                                beAnswerCArds.get(indexOf).setAnswerFlag("true");
//                            }
//                        }

                    } else {
                        completion.setIs_auto("1");
                        completion.setMy_answer("");
                    }

//                    Logger.d("---------------------------------------------------------------integerMapMap.size()" + integerMapMap.size());
//                    Logger.d("---------------------------------------------------------------integerMapMap.tostring" + integerMapMap.toString());

                    Logger.d("---------------------------------------------------------------");
                    Logger.d("填空 question_id:----" + completion.getId());
                    Logger.d("填空 question_cate_id:----" + completion.getQuestion_cate_id());
                    Logger.d("填空 my_answer:----" + completion.getMy_answer());
                    Logger.d("填空 is_right:----" + completion.getIs_right());
                    Logger.d("填空 is_auto:----" + completion.getIs_auto());

//                    submitAnswerCardList.add(new BeSubmitAnswerCard("5", "1", "我的答案", "0", ""));

//                    Logger.d( "AnswerCardActivity-----填空getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----填空getAnswerflag:" + ancompletion );
                    break;

                default:
                    break;
            }

        }

        tvanswer.setText(answernum + "/" + beAnswerCArds.size());

        Toast.makeText(context, "答题卡", Toast.LENGTH_SHORT).show();

        ApAnswerCard apAnswerCard = new ApAnswerCard(context, beAnswerCArds);

        grildview.setAdapter(apAnswerCard);


    }


    private void initialize() {
        tvnoanswer = getView(R.id.tv_noanswer);
        tvanswer = getView(R.id.tv_answer);
        grildview = getView(R.id.grildview);
        mBtnsubmit = getView(R.id.btn_submit);
        mBtnsubmit.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                     /*网络请求提交答案*/
                    httpData();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public void httpData() {
        super.httpData();

        RequestUtill.getInstance().httpSubmitAnswerCard(AnswerCardActivity.this, callSubmitAnswerCard, homework_id, "-1",
                new GsonUtil().getJsonElement(new ToAnswerCardJson(submitAnswerCardList)).toString());

    }

    ResultCallback callSubmitAnswerCard = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {

            Logger.d("测试返回json：" + response.toString());
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {


                //    Bundle bundle = new Bundle();
//                    bundle.putSerializable("answerCard", beSaveAnswerCard);
//                    DjhJumpUtil.getInstance().startBaseActivity(AnswerCardActivity.this, HomeWorkResultActivity.class, bundle, ANSWERCARD);
//                    ActivityUtil.getInstance().finishActivity(DoHomeworkActivity.class);//结束指定的activity
//                    finishActivity();
            } else {
                ToastUtil.showToast(AnswerCardActivity.this, json.getMsg());
            }
        }
    };
}
