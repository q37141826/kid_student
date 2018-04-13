package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.GsonUtil;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.Logger;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.widgets.dialog.FxDialog;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionadapterItemModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.ToAnswerCardJson;
import cn.dajiahui.kid.util.DjhJumpUtil;

/*
 * 答题卡
 * */
public class AnswerCardActivity extends FxActivity {

    private TextView tvanswer;
    private GridView grildview;
    private Button mBtnsubmit;

    private BeSaveAnswerCard beSaveAnswerCard;
    private String homework_id;
    private List<BeSubmitAnswerCard> submitAnswerCardList = new ArrayList<>();//转json的答题卡集合
    private List<BeAnswerCArd> beAnswerCArds = new ArrayList<>();//显示已作答，未作答的答题卡集合
    private StringBuffer append1;
    private StringBuffer append2;

    private int answerNum = 0;//已经作答的个数
    private final String RIGHT = "1";  // 正确
    private final String WRONG = "0";  // 错误

    private final String AUTOMATIC = "1";//自动
    private final String MANUAL = "0";//手动

    private final String ANSWER_YES = "1";//已经回答
    private final String ANSWER_NO = "0";//未回答

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
        beSaveAnswerCard = (BeSaveAnswerCard) bundle.getSerializable("ANSWER_CARD");
        /*作业id*/
        homework_id = beSaveAnswerCard.getHomework_id();

        /*获取所有数据的集合*/
        List<Object> mDatalist = beSaveAnswerCard.getmDatalist();

        /*循环所有数据模型*/
        for (int i = 0; i < mDatalist.size(); i++) {
            /*得到基本的数据模型 取出题型的标记*/
            QuestionModle qm = (QuestionModle) mDatalist.get(i);
            /*判断题型*/
            switch (qm.getQuestion_cate_id()) {

                case Constant.Judje:
                    JudjeQuestionModle jqm = (JudjeQuestionModle) mDatalist.get(i);
                    if (!jqm.getIs_complete().equals("1")) {
                        Judje(jqm);
                        if (jqm.getAnswerflag().equals("true"))
                            answerNum++;
                        beAnswerCArds.add(new BeAnswerCArd(i, jqm.getAnswerflag()));
                    }
                    break;
                case Constant.Choice:
                    ChoiceQuestionModle cqm = (ChoiceQuestionModle) mDatalist.get(i);
                    if (!cqm.getIs_complete().equals("1")) {
                        Choice(cqm);
                        beAnswerCArds.add(new BeAnswerCArd(i, cqm.getAnswerflag()));
                        if (cqm.getAnswerflag().equals("true"))
                            answerNum++;
                    }
                    break;
                case Constant.Sort:
                    SortQuestionModle sqm = (SortQuestionModle) mDatalist.get(i);
                    if (!sqm.getIs_complete().equals("1")) {
                        Sort(sqm);
                        beAnswerCArds.add(new BeAnswerCArd(i, sqm.getAnswerflag()));
                        if (sqm.getAnswerflag().equals("true"))
                            answerNum++;
                    }
                    break;
                case Constant.Line:
                    LineQuestionModle lqm = (LineQuestionModle) mDatalist.get(i);
                    if (!lqm.getIs_complete().equals("1")) {
                        Line(lqm);
                        beAnswerCArds.add(new BeAnswerCArd(i, lqm.getAnswerflag()));
                        if (lqm.getAnswerflag().equals("true"))
                            answerNum++;
                    }
                    break;

                case Constant.Completion:
                    CompletionQuestionModle coqm = (CompletionQuestionModle) mDatalist.get(i);
                    if (!coqm.getIs_complete().equals("1")) {
                        Completion(coqm);
                        beAnswerCArds.add(new BeAnswerCArd(i, coqm.getAnswerflag()));
                        if (coqm.getAnswerflag().equals("true"))
                            answerNum++;
                    }
                    break;
                default:
                    break;
            }

        }
        tvanswer.setText("已作答：" + answerNum + "/" + mDatalist.size());
        ApAnswerCard apAnswerCard = new ApAnswerCard(context, beAnswerCArds);

        grildview.setAdapter(apAnswerCard);


        grildview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                int current_num = beAnswerCArds.get(position).getCurrent_num();
                intent.putExtra("current_num", current_num);

                setResult(DjhJumpUtil.getInstance().activity_todohomework, intent);
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
        Logger.d("答题卡json：" + s);
        /* 要自己判断是否已经完成作业 -1:未开始 0:进行中 1:已完成  */
        RequestUtill.getInstance().httpSubmitAnswerCard(AnswerCardActivity.this, callSubmitAnswerCard, homework_id, "1",
                new GsonUtil().getJsonElement(new ToAnswerCardJson(submitAnswerCardList)).toString());

    }

    /*提交答案回调*/
    ResultCallback callSubmitAnswerCard = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {
            dismissfxDialog();
            Logger.d("完全提交答题卡失败！");
        }

        @Override
        public void onResponse(String response) {

            Logger.d("完全提交答题卡成功！");

            dismissfxDialog();
            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {
                setResult(DjhJumpUtil.getInstance().activity_answerCardSubmit);
                finishActivity();
            } else {
                ToastUtil.showToast(AnswerCardActivity.this, json.getMsg());
            }
        }
    };

    private FxDialog submitDialog;

    /*判断题答题卡数据*/
    private void Judje(JudjeQuestionModle jqm) {

        /*如果答过题 手动提交是0 自动提交是1*/
        if (!jqm.getMy_answer().equals("")) {
            jqm.setIs_auto(MANUAL);//手动提交
            jqm.setIs_answered(ANSWER_YES);//已经回答
            /*判断正误*/
            if (jqm.getMy_answer().equals(jqm.getStandard_answer())) {
                jqm.setIs_right(RIGHT);//正确
            } else {
                jqm.setIs_right(WRONG);//错误
            }

        } else {
            jqm.setIs_auto(AUTOMATIC);//自动提交
            jqm.setIs_answered(ANSWER_NO);//未回答
            jqm.setIs_right(WRONG);
        }

        submitAnswerCardList.add(new BeSubmitAnswerCard(jqm.getId(), jqm.getQuestion_cate_id(), jqm.getMy_answer(), jqm.getIs_right(), jqm.getIs_auto(), jqm.getIs_answered()));

    }

    /*选择题答题卡数据*/
    private void Choice(ChoiceQuestionModle cqm) {
        /*如果答过题 手动提交是0 自动提交是1*/
        if (!cqm.getMy_answer().equals("")) {
            cqm.setIs_auto(MANUAL);//手动提交
            cqm.setIs_answered(ANSWER_YES);//已经回答
            /*判断正误*/
            if (cqm.getMy_answer().equals(cqm.getStandard_answer())) {
                cqm.setIs_right(RIGHT);//正确
            } else {
                cqm.setIs_right(WRONG);//错误
            }
        } else {
            cqm.setIs_auto(AUTOMATIC);//自动提交
            cqm.setIs_answered(ANSWER_NO);//未回答
            cqm.setIs_right(WRONG);
        }
        submitAnswerCardList.add(new BeSubmitAnswerCard(cqm.getId(), cqm.getQuestion_cate_id(), cqm.getMy_answer(), cqm.getIs_right(), cqm.getIs_auto(), cqm.getIs_answered()));

    }

    /*排序题答题卡数据*/
    private void Sort(SortQuestionModle sqm) {
        String strMineAnswer = "";
        for (int s = 1; s < sqm.getInitSortMyanswerList().size(); s++) {
            strMineAnswer = strMineAnswer + "," + sqm.getInitSortMyanswerList().get(s);
        }
        strMineAnswer = sqm.getInitSortMyanswerList().get(0) + strMineAnswer;

        int mAnsNum = 0;//排序作答题的个数 判断自己回答的个数 mAnsNum>0 就是已经回答过题
        for (int a = 0; a < sqm.getInitSortMyanswerList().size(); a++) {
            if (!sqm.getInitSortMyanswerList().get(a).equals("")) {
                mAnsNum++;
            }
        }

        if (mAnsNum > 0) {
            sqm.setIs_auto(MANUAL);//手动提交
            sqm.setIs_answered(ANSWER_YES);//已经回答
            sqm.setMy_answer(strMineAnswer);
            /*判断正误*/
            if (sqm.getMy_answer().equals(sqm.getStandard_answer())) {
                sqm.setIs_right(RIGHT);//正确
            } else {
                sqm.setIs_right(WRONG);//错误
            }
        } else {
            sqm.setIs_auto(AUTOMATIC);//自动提交
            sqm.setIs_answered(ANSWER_NO);//未回答
            sqm.setMy_answer(strMineAnswer);
            sqm.setIs_right(WRONG);
        }
//        Logger.d("sqm.getMy_answer():" + sqm.getMy_answer());

        submitAnswerCardList.add(new BeSubmitAnswerCard(sqm.getId(), sqm.getQuestion_cate_id(), sqm.getMy_answer(), sqm.getIs_right(), sqm.getIs_auto(), sqm.getIs_answered()));

    }

    /*连线题答题卡数据*/
    private void Line(LineQuestionModle lqm) {
        /*我的答案*/
        Map<String, String> myAnswerMap = lqm.getInitLineMyanswerMap();
        Map<String, String> mStandMap = jsonToObject(lqm.getStandard_answer());
        JsonElement jsonElement = new GsonUtil().getJsonElement(myAnswerMap);
        int mAnsNum = 0;//连线作答题的个数 判断自己回答的个数 mAnsNum>0 就是已经回答过题
        for (int l = 1; l <= myAnswerMap.size(); l++) {
            if (!(myAnswerMap.get((l) + "")).equals("")) {
                mAnsNum++;
            }
        }
        if (mAnsNum > 0) {
            lqm.setIs_right(RIGHT);//默认为正确
            /*循环对比正确答案 */
            for (int m = 1; m <= myAnswerMap.size(); m++) {
                if (!(myAnswerMap.get((m) + "")).equals(mStandMap.get((m) + ""))) {
                    lqm.setIs_right(WRONG);//判断正误  有一个不相等就认为是回答错误
                    break;
                }
            }
            lqm.setIs_auto(MANUAL);//自动提交
            lqm.setIs_answered(ANSWER_YES);//已作答
            lqm.setMy_answer(jsonElement.toString());//我的答案

        } else {
            lqm.setIs_right(WRONG);//回答错误
            lqm.setIs_auto(AUTOMATIC);//自动提交
            lqm.setIs_answered(ANSWER_NO);//未作答
            lqm.setMy_answer(jsonElement.toString());
        }
        submitAnswerCardList.add(new BeSubmitAnswerCard(lqm.getId(), lqm.getQuestion_cate_id(), lqm.getMy_answer(), lqm.getIs_right(), lqm.getIs_auto(), lqm.getIs_answered()));
    }

    /*json转map*/
    public Map jsonToObject(String jsonStr) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<String, String> outMap = new HashMap<String, String>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            try {
                if (!jsonObj.getString(name).equals(""))
                    outMap.put(name, jsonObj.getString(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return outMap;
    }

    /*填空題答题卡数据*/
    private void Completion(CompletionQuestionModle coqm) {

        LinkedHashMap<Integer, LinkedHashMap<Integer, CompletionQuestionadapterItemModle>> integerMapMap = coqm.getmCompletionAllMap();
        StringBuffer sbAll = new StringBuffer();
        String appendAll = "";
        int mAnsNum = 0;//填空作答题的个数 判断自己回答的个数 mAnsNum>0 就是已经回答过题
        for (int c = 0; c < integerMapMap.size(); c++) {
            if (integerMapMap.get(c) != null) {
                LinkedHashMap<Integer, CompletionQuestionadapterItemModle> integerStringMap = integerMapMap.get(c);
                StringBuffer stringBuffer = new StringBuffer();
                for (int d = 0; d < integerStringMap.size(); d++) {

                    if (!integerStringMap.get(d).getShowItemMy().equals("㊒")) {
                        mAnsNum++;
                    }
                    StringBuffer sb1 = stringBuffer.append(integerStringMap.get(d).getShowItemMy().toString());
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

        if (mAnsNum > 0) {
            coqm.setIs_auto(MANUAL);//手动提交
            coqm.setIs_answered(ANSWER_YES);
            if (!appendAll.equals("")) {
                coqm.setMy_answer(appendAll.toString());
                if (appendAll.equals(coqm.getStandard_answer())) {
                    coqm.setIs_right(RIGHT);//正确
                } else {
                    coqm.setIs_right(WRONG);//错误
                }
            }
        } else {
            coqm.setIs_auto(AUTOMATIC);
            coqm.setMy_answer(appendAll.toString());
            coqm.setIs_answered(ANSWER_NO);
            coqm.setIs_right(WRONG);
        }

        submitAnswerCardList.add(new BeSubmitAnswerCard(coqm.getId(), coqm.getQuestion_cate_id(), coqm.getMy_answer(), coqm.getIs_right(), coqm.getIs_auto(), coqm.getIs_answered()));
    }

}
