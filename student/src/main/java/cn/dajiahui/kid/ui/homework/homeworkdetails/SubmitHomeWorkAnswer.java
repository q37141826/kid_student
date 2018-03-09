package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Context;

import com.fxtx.framework.http.callback.ResultCallback;
import com.fxtx.framework.json.HeadJson;
import com.fxtx.framework.log.ToastUtil;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BeSubmitAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.util.Logger;

/**
 * Created by majin on 2018/2/8.
 * <p>
 * 按返回时候的提交答案
 */

public class SubmitHomeWorkAnswer {

    private List<QuestionModle> listdata = new ArrayList<>();//显示答题卡的集合
    private List<BeSubmitAnswerCard> submitAnswerCardList = new ArrayList<>();
    private Context context;
    private String homework_id;
    private BeSaveAnswerCard beSaveAnswerCard;


    public SubmitHomeWorkAnswer(Context context, BeSaveAnswerCard beSaveAnswerCard) {
        this.context = context;
        this.beSaveAnswerCard = beSaveAnswerCard;
    }

    /*提交答案*/
//    public void submitAnswerCard() {
//
//        homework_id = beSaveAnswerCard.getHomework_id();
//
//        HashMap<Integer, Object> pageMap = beSaveAnswerCard.getPageMap();
//
//        for (int i = 0; i < pageMap.size(); i++) {
//
//            QuestionModle questionModle = (QuestionModle) pageMap.get(i);
//
//            switch (questionModle.getQuestion_cate_id()) {
//
//                case Constant.Judje:
//                    JudjeQuestionModle jude = (JudjeQuestionModle) pageMap.get(i);
//                    /*如果答过题 自动提交答案的标记默认是0*/
//                    if (jude.getMy_answer() != null) {
//                        jude.setIs_auto("0");
//                    }
//                    listdata.add(new QuestionModle(jude.getCurrentpage()));
//
//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("判断 question_id:----" + jude.getId());
//                    Logger.d("判断 question_cate_id:----" + jude.getQuestion_cate_id());
//                    Logger.d("判断 my_answer:----" + jude.getMy_answer());
//                    Logger.d("判断 is_right:----" + jude.getIs_right());
//                    Logger.d("判断 is_auto:----" + jude.getIs_auto());
////
//
////                    submitAnswerCardList.add(new BeSubmitAnswerCard(jude.getId(), jude.getQuestion_cate_id(), jude.getMy_answer(), jude.getIs_right(), jude.getIs_auto()));
//
////                    Logger.d( "AnswerCardActivity-----判断getSubjectype :" + questionModle.getQuestion_cate_id() );
////                    Logger.d( "AnswerCardActivity-----判断getAnswerflag:" + anJudje );
//                    break;
//                case Constant.Choice:
//                    ChoiceQuestionModle choice = (ChoiceQuestionModle) pageMap.get(i);
//                      /*如果答过题 自动提交答案的标记默认是0*/
//                    if (choice.getMy_answer() != null) {
//                        choice.setIs_auto("0");
//                    }
//
//                    listdata.add(new QuestionModle(choice.getCurrentpage()));
//                    Logger.d("---------------------------------------------------------------");
//                    Logger.d("选择 question_id:----" + choice.getId());
//                    Logger.d("选择 question_cate_id:----" + choice.getQuestion_cate_id());
//                    Logger.d("选择 my_answer:----" + choice.getMy_answer());
//                    Logger.d("选择 is_right:----" + choice.getIs_right());
//                    Logger.d("选择 is_auto:----" + choice.getIs_auto());
//
////                    submitAnswerCardList.add(new BeSubmitAnswerCard(choice.getId(), choice.getQuestion_cate_id(), choice.getMy_answer(), choice.getIs_right(), choice.getIs_auto()));
//
//
//                    break;
//                case Constant.Sort:
//                    SortQuestionModle sort = (SortQuestionModle) pageMap.get(i);
//
//                    listdata.add(new QuestionModle(sort.getCurrentpage()));
////                    Logger.d("---------------------------------------------------------------");
////                    Logger.d("排序 question_id:----" + sort.getId());
////                    Logger.d("排序 question_cate_id:----" + sort.getQuestion_cate_id());
////                    Logger.d("排序 my_answer:----" + sort.getMy_answer());
////                    Logger.d("排序 is_right:----" + sort.getIs_right());
////                    Logger.d("排序 is_auto:----" + sort.getIs_auto());
//
////                    submitAnswerCardList.add(new BeSubmitAnswerCard("5", "1", "我的答案", "0", ""));
//
////                    Logger.d( "AnswerCardActivity-----排序getSubjectype :" + questionModle.getQuestion_cate_id() );
////                    Logger.d( "AnswerCardActivity-----排序getAnswerflag:" + ansort );
//                    break;
//                case Constant.Line:
//                    LineQuestionModle line = (LineQuestionModle) pageMap.get(i);
//                    String anline = line.getAnswerflag();
//                    listdata.add(new QuestionModle(line.getCurrentpage()));
////                    Logger.d("---------------------------------------------------------------");
////                    Logger.d("连线 question_id:----" + line.getId());
////                    Logger.d("连线 question_cate_id:----" + line.getQuestion_cate_id());
////                    Logger.d("连线 my_answer:----" + line.getMy_answer());
////                    Logger.d("连线 is_right:----" + line.getIs_right());
////                    Logger.d("连线 is_auto:----" + line.getIs_auto());
//
////                    submitAnswerCardList.add(new BeSubmitAnswerCard("5", "1", "我的答案", "0", ""));
//
////                    Logger.d( "AnswerCardActivity-----连线getSubjectype :" + questionModle.getQuestion_cate_id() );
////                    Logger.d( "AnswerCardActivity-----连线getAnswerflag:" + anline );
//                    break;
//                case Constant.Completion:
//                    CompletionQuestionModle completion = (CompletionQuestionModle) pageMap.get(i);
//                    String ancompletion = completion.getAnswerflag();
//
//                    listdata.add(new QuestionModle(completion.getCurrentpage()));
////                    Logger.d("---------------------------------------------------------------");
////                    Logger.d("填空 question_id:----" + completion.getId());
////                    Logger.d("填空 question_cate_id:----" + completion.getQuestion_cate_id());
////                    Logger.d("填空 my_answer:----" + completion.getMy_answer());
////                    Logger.d("填空 is_right:----" + completion.getIs_right());
////                    Logger.d("填空 is_auto:----" + completion.getIs_auto());
//
////                    submitAnswerCardList.add(new BeSubmitAnswerCard("5", "1", "我的答案", "0", ""));
//
////                    Logger.d( "AnswerCardActivity-----填空getSubjectype :" + questionModle.getQuestion_cate_id() );
////                    Logger.d( "AnswerCardActivity-----填空getAnswerflag:" + ancompletion );
//                    break;
//
//                default:
//                    break;
//            }
//
//        }
//
//
//        RequestUtill.getInstance().httpSubmitAnswerCard(context, callSubmitAnswerCard, homework_id, beSaveAnswerCard.getIs_complete(),
//                new GsonUtil().getJsonElement(new ToAnswerCardJson(submitAnswerCardList)).toString());
//
//    }

    ResultCallback callSubmitAnswerCard = new ResultCallback() {
        @Override
        public void onError(Request request, Exception e) {

            Logger.d("测试答题一半返回错误：" + e.toString());
        }

        @Override
        public void onResponse(String response) {

            Logger.d("测试答题一半返回json：" + response.toString());

            HeadJson json = new HeadJson(response);
            if (json.getstatus() == 0) {

                //    Bundle bundle = new Bundle();
//                    bundle.putSerializable("answerCard", beSaveAnswerCard);
//                    DjhJumpUtil.getInstance().startBaseActivity(AnswerCardActivity.this, HomeWorkResultActivity.class, bundle, ANSWERCARD);
//                    ActivityUtil.getInstance().finishActivity(DoPraticeActivity.class);//结束指定的activity
//                    finishActivity();
            } else {
                ToastUtil.showToast(context, json.getMsg());
            }
        }
    };

}
