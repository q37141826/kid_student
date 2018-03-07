package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.google.gson.JsonElement;
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

/*
* 答题卡
* */
public class AnswerCardActivity extends FxActivity {

    private TextView tvanswer;
    private GridView grildview;
    private Button mBtnsubmit;
    private int answernum;
    private BeSaveAnswerCard beSaveAnswerCard;
    private String homework_id;

    private List<BeSubmitAnswerCard> submitAnswerCardList = new ArrayList<>();
    private List<BeAnswerCArd> beAnswerCArds;
    private StringBuffer append1;
    private StringBuffer append2;


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
        if (beSaveAnswerCard.getIs_complete().equals("1")) {
            mBtnsubmit.setVisibility(View.GONE);
        }
        HashMap<Integer, Object> pageMap = beSaveAnswerCard.getPageMap();

        for (int i = 0; i < pageMap.size(); i++) {

            QuestionModle questionModle = (QuestionModle) pageMap.get(i);
            if (questionModle != null)
                switch (questionModle.getQuestion_cate_id()) {

                    case Constant.Judje:
                        JudjeQuestionModle jude = (JudjeQuestionModle) pageMap.get(i);
                    /*如果答过题 手动提交是0 自动提交是1*/
                        if (jude.getMy_answer() != null) {
                            jude.setIs_auto("0");
                            jude.setIs_answered("1");
                            for (int ij = 0; ij < beAnswerCArds.size(); ij++) {
                                if (beAnswerCArds.get(ij).getQuestion_id().equals(jude.getId())) {
                                    int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                    beAnswerCArds.get(indexOf).setAnswerFlag("true");
                                }
                            }
                         /*回答正确*/
                            if (jude.getMy_answer().equals(jude.getStandard_answer())) {
                                jude.setIs_right("0");
                            } else {
                                jude.setIs_right("1");
                            }
                        } else {
                            jude.setMy_answer("");
                            jude.setIs_auto("1");
                            jude.setIs_answered("0");
                            jude.setIs_right("1");
                        }

//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("判断 question_id:----" + jude.getId());
//                    Logger.d("判断 question_cate_id:----" + jude.getQuestion_cate_id());
//                    Logger.d("判断 my_answer:----" + jude.getMy_answer());
//                    Logger.d("判断 is_right:----" + jude.getIs_right());
//                    Logger.d("判断 is_auto:----" + jude.getIs_auto());
                        submitAnswerCardList.add(new BeSubmitAnswerCard(jude.getId(), jude.getQuestion_cate_id(), jude.getMy_answer(), jude.getIs_right(), jude.getIs_auto(), jude.getIs_answered()));

//                    Logger.d( "AnswerCardActivity-----判断getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----判断getAnswerflag:" + anJudje );
                        break;
                    case Constant.Choice:
                        ChoiceQuestionModle choice = (ChoiceQuestionModle) pageMap.get(i);

                      /*如果答过题 自动提交答案的标记默认是0*/
                        if (choice.getMy_answer() != null) {
                            choice.setIs_auto("0");
                            choice.setIs_answered("1");
                            for (int ij = 0; ij < beAnswerCArds.size(); ij++) {

                                if (beAnswerCArds.get(ij).getQuestion_id().equals(choice.getId())) {
                                    int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                    beAnswerCArds.get(indexOf).setAnswerFlag("true");

                                }
                            }
                        /*回答正确*/
                            if (choice.getMy_answer().equals(choice.getStandard_answer())) {
                                choice.setIs_right("0");
                            } else {
                                choice.setIs_right("1");
                            }
                        } else {
                            choice.setMy_answer("");
                            choice.setIs_auto("1");
                            choice.setIs_answered("0");
                            choice.setIs_right("1");
                        }
//                    Logger.d("选择   :正确答案----" + choice.getStandard_answer());
//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("选择 question_id:----" + choice.getId());
//                    Logger.d("选择 question_cate_id:----" + choice.getQuestion_cate_id());
//                    Logger.d("选择 my_answer:----" + choice.getMy_answer());
//                    Logger.d("选择 is_right:----" + choice.getIs_right());
//                    Logger.d("选择 is_auto:----" + choice.getIs_auto());

                        submitAnswerCardList.add(new BeSubmitAnswerCard(choice.getId(), choice.getQuestion_cate_id(), choice.getMy_answer(), choice.getIs_right(), choice.getIs_auto(), choice.getIs_answered()));


                        break;
                    case Constant.Sort:
                        SortQuestionModle sort = (SortQuestionModle) pageMap.get(i);
                        StringBuffer append = null;
                        StringBuffer SortstringBuffer = new StringBuffer();
                        for (int q = 1; q < sort.getInitMyanswerList().size(); q++) {
                            append = SortstringBuffer.append("," + sort.getInitMyanswerList().get(q));
                        }


                        if (sort.getInitMyanswerList().size() > 0) {
                            sort.setIs_auto("0");
                            sort.setIs_answered("1");
                            sort.setMy_answer(sort.getInitMyanswerList().get(0) + append.toString());

                            /*回答正确*/
                            if (sort.getMy_answer().equals(sort.getStandard_answer())) {
                                sort.setIs_right("0");
                            } else {
                                sort.setIs_right("1");
                            }

                            for (int ij = 0; ij < beAnswerCArds.size(); ij++) {
                                if (beAnswerCArds.get(ij).getQuestion_id().equals(sort.getId())) {
                                    int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                    beAnswerCArds.get(indexOf).setAnswerFlag("true");
                                }
                            }
                        } else {
                            sort.setIs_auto("1");
                            sort.setIs_answered("0");
                            sort.setIs_right("1");
                            sort.setMy_answer("");
                        }


//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("排序 question_id:----" + sort.getId());
//                    Logger.d("排序 question_cate_id:----" + sort.getQuestion_cate_id());
//                    Logger.d("排序 my_answer:----" + sort.getMy_answer());
//                    Logger.d("排序 getStandard_answer():----" + sort.getStandard_answer());
//                    Logger.d("排序 is_right:----" + sort.getIs_right());
//                    Logger.d("排序 is_auto:----" + sort.getIs_auto());

                        submitAnswerCardList.add(new BeSubmitAnswerCard(sort.getId(), sort.getQuestion_cate_id(), sort.getMy_answer(), sort.getIs_right(), sort.getIs_auto(), sort.getIs_answered()));

//                    Logger.d( "AnswerCardActivity-----排序getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----排序getAnswerflag:" + ansort );
                        break;
                    case Constant.Line:
                        LineQuestionModle line = (LineQuestionModle) pageMap.get(i);
                        Map<String, String> myanswerMap = line.getMyanswerMap();

                    /*要先判断是不是回答正确  */
                        JsonElement jsonElement = new GsonUtil().getJsonElement(myanswerMap);
                        line.setMy_answer(jsonElement.toString());
//                    Logger.d("jsonElement.toString():" + jsonElement.toString());
                        List mSnList = new ArrayList();
                        String subSN = line.getStandard_answer().substring(1, line.getStandard_answer().length() - 1);
                        String[] spSn = subSN.split(",");


                        Map<String, Integer> standanserMap = new HashMap<>();
                    /*截取字符串 正确答案*/
                        for (int m = 0, len = spSn.length; m < len; m++) {
                            String s = spSn[m].toString();
                            mSnList.add(s);

                        }

                    /*二次截取 获取答案 获取 */
                        for (int l = 0; l < mSnList.size(); l++) {
                            String split1 = mSnList.get(l).toString().substring(1, 2);
                            String s = mSnList.get(l).toString();
                            String split2 = null;
                            if (s.length() == 5) {/*配合测试后台数据*/
                                split2 = mSnList.get(l).toString().toString().substring(4);
                            } else {
                                split2 = mSnList.get(l).toString().substring(5, 6);
                            }
                            standanserMap.put(split1, Integer.parseInt(split2));
                        }
                        int num = 0;
                    /*循环对比正确答案 */
                        if (myanswerMap.size() > 0) {
                            for (int m = 0; m < myanswerMap.size(); m++) {
                            /*取出val值*/
                                if ((standanserMap.get((m + 1) + "")).equals((myanswerMap.get((m + 1) + "")))) {
                                    num++;
                                } else {
                                    line.setIs_right("1");
                                }
                            }
                        /*判断是否回答正确*/
                            if (num == myanswerMap.size()) {
                                line.setIs_right("0");
                            }
                         /*1代表自动提交  0 手动提交*/
                            line.setIs_auto("0");
                            line.setIs_answered("1");
                            line.setMy_answer(jsonElement.toString());

                        /*确定答题*/
                            for (int ij = 0; ij < beAnswerCArds.size(); ij++) {
                                if (beAnswerCArds.get(ij).getQuestion_id().equals(line.getId())) {
                                    int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                    beAnswerCArds.get(indexOf).setAnswerFlag("true");
                                }
                            }
                        } else {
                            line.setIs_auto("1");
                            line.setIs_answered("0");
                            line.setIs_right("1");
                            line.setMy_answer("");
                        }


//                        Logger.d("连线 getStandard_answer():----" + line.getStandard_answer());
//                        Logger.d("连线 getMyanswerMap:----" + line.getMyanswerMap());
//                        Logger.d("---------------------------------------------------------------");
//                        Logger.d("连线 question_id:----" + line.getId());
//                        Logger.d("连线 question_cate_id:----" + line.getQuestion_cate_id());
//                        Logger.d("连线 my_answer:----" + line.getMy_answer());
//                        Logger.d("连线 is_right:----" + line.getIs_right());
//                        Logger.d("连线 is_auto:----" + line.getIs_auto());

                        submitAnswerCardList.add(new BeSubmitAnswerCard(line.getId(), line.getQuestion_cate_id(), line.getMy_answer(), line.getIs_right(), line.getIs_auto(), line.getIs_answered()));


                        break;
                    case Constant.Completion:
                        CompletionQuestionModle completion = (CompletionQuestionModle) pageMap.get(i);

                        Map<Integer, Map<Integer, String>> integerMapMap = completion.getmAllMap();

                        StringBuffer sbAll = new StringBuffer();
                        String appendAll = "";
                        if (integerMapMap.size() > 0) {
                            for (int c = 0; c < integerMapMap.size(); c++) {
                                if (integerMapMap.get(c) != null) {
                                    Map<Integer, String> integerStringMap = integerMapMap.get(c);
                                    StringBuffer stringBuffer = new StringBuffer();
                                    for (int d = 0; d < integerStringMap.size(); d++) {
                                        StringBuffer sb1 = stringBuffer.append(integerStringMap.get(d).toString());
                                        if (c == 0 && d + 1 == integerStringMap.size()) {
                                            append1 = sb1;
                                            break;
                                        } else if (c > 0 && d + 1 == integerStringMap.size()) {
                                            append2 = sbAll.append("۞" + sb1);
                                        }
                                    }

                                    if (c + 1 == integerMapMap.size()) {
                                        if (integerMapMap.size() == 1) {
                                            appendAll = append1.toString();
                                        } else {
                                            appendAll = append1.toString() + append2.toString();
                                        }

                                    }
                                }

                            }
                       /*没有作答答案自动提交*/
                            if (integerMapMap.size() > 0) {
                                completion.setIs_auto("0");
                                completion.setIs_answered("1");
                                if (!appendAll.equals("")) {
                                    completion.setMy_answer(appendAll.toString());
                                    if (appendAll.equals(completion.getStandard_answer())) {
                                        completion.setIs_right("0");
                                    } else {
                                        completion.setIs_right("1");
                                    }
                                }
                           /*确定答题*/
                                for (int ij = 0; ij < beAnswerCArds.size(); ij++) {
                                    if (beAnswerCArds.get(ij).getQuestion_id().equals(completion.getId())) {
                                        int indexOf = beAnswerCArds.indexOf(beAnswerCArds.get(ij));
                                        beAnswerCArds.get(indexOf).setAnswerFlag("true");
                                    }
                                }
                            }
                        } else {
                            completion.setMy_answer("");
                            completion.setIs_auto("1");
                            completion.setIs_answered("0");
                            completion.setIs_right("1");

                        }

                        submitAnswerCardList.add(new BeSubmitAnswerCard(completion.getId(), completion.getQuestion_cate_id(), completion.getMy_answer(), completion.getIs_right(), completion.getIs_auto(), completion.getIs_answered()));

//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("填空 question_id:----" + completion.getId());
//                    Logger.d("填空 question_cate_id:----" + completion.getQuestion_cate_id());
//                    Logger.d("填空 my_answer:----" + completion.getMy_answer());
//                    Logger.d("填空 getStandard_answer:----" + completion.getStandard_answer());
//                    Logger.d("填空 is_right:----" + completion.getIs_right());
//                    Logger.d("填空 is_auto:----" + completion.getIs_auto());

                        break;

                    default:
                        break;
                }

        }

        tvanswer.setText(answernum + "/" + beAnswerCArds.size());

        Toast.makeText(context, "答题卡", Toast.LENGTH_SHORT).show();

        ApAnswerCard apAnswerCard = new ApAnswerCard(context, beAnswerCArds);

        grildview.setAdapter(apAnswerCard);

        grildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                int current_num = beAnswerCArds.get(position).getCurrent_num();
                intent.putExtra("current_num", current_num);
//                Logger.d("current_num:" + current_num);
                setResult(1, intent);
                finishActivity();
            }
        });

    }

    /*初始化*/
    private void initialize() {
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
                    if (submitDialog == null) {
                        submitDialog = new FxDialog(context) {
                            @Override
                            public void onRightBtn(int flag) {
                              /*网络请求提交答案*/
                                httpData();
                            }

                            @Override
                            public void onLeftBtn(int flag) {

                                submitDialog.dismiss();
                            }
                        };
                        submitDialog.getTitle().setVisibility(View.GONE);
                    }
                    submitDialog.setMessage("确定要提交试卷么？");
                    submitDialog.show();

                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public void httpData() {
        super.httpData();

        String s = new GsonUtil().getJsonElement(new ToAnswerCardJson(submitAnswerCardList)).toString();

       /* 要自己判断是否已经完成作业 -1:未开始 0:进行中 1:已完成  */
        RequestUtill.getInstance().httpSubmitAnswerCard(AnswerCardActivity.this, callSubmitAnswerCard, homework_id, "1",
                new GsonUtil().getJsonElement(new ToAnswerCardJson(submitAnswerCardList)).toString());

    }

    ResultCallback callSubmitAnswerCard = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
        }

        @Override
        public void onResponse(String response) {

//           Logger.d("测试返回json：" + response.toString());
            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                setResult(2);
                finishActivity();
            } else {
                ToastUtil.showToast(AnswerCardActivity.this, json.getMsg());
            }
        }
    };

    private FxDialog submitDialog;


}
