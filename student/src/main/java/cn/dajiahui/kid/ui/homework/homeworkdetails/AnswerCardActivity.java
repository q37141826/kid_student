package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.util.ActivityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.homework.adapter.ApAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.BeSaveAnswerCard;
import cn.dajiahui.kid.ui.homework.bean.ChoiceQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.JudjeQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.QuestionModle;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.util.DjhJumpUtil;

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
//    private List<QuestionModle> listdata;

    private List<QuestionModle> listdata = new ArrayList<>();
    private BeSaveAnswerCard beSaveAnswerCard;


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
        HashMap<Integer, Object> pageMap = beSaveAnswerCard.getPageMap();


        for (int i = 0; i < pageMap.size(); i++) {

            QuestionModle questionModle = (QuestionModle) pageMap.get(i);

            switch (questionModle.getQuestion_cate_id()) {

                case Constant.Judje:
                    JudjeQuestionModle jude = (JudjeQuestionModle) pageMap.get(i);
                    String anJudje = jude.getAnswerflag();
                    listdata.add(new QuestionModle(jude.getCurrentpage(), anJudje));
//                    Logger.d( "AnswerCardActivity-----判断getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----判断getAnswerflag:" + anJudje );
                    break;
                case Constant.Choice:
                    ChoiceQuestionModle choice = (ChoiceQuestionModle) pageMap.get(i);
                    String anchoice = choice.getAnswerflag();
                    listdata.add(new QuestionModle(choice.getCurrentpage(), anchoice));
//                    Logger.d( "AnswerCardActivity-----选择getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----选择getAnswerflag:" + anchoice );
                    break;
                case Constant.Sort:
                    SortQuestionModle sort = (SortQuestionModle) pageMap.get(i);
                    String ansort = sort.getAnswerflag();
                    listdata.add(new QuestionModle(sort.getCurrentpage(), ansort));
//                    Logger.d( "AnswerCardActivity-----排序getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----排序getAnswerflag:" + ansort );
                    break;
                case Constant.Line:
                    LineQuestionModle line = (LineQuestionModle) pageMap.get(i);
                    String anline = line.getAnswerflag();
                    listdata.add(new QuestionModle(line.getCurrentpage(), anline));
//                    Logger.d( "AnswerCardActivity-----连线getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----连线getAnswerflag:" + anline );
                    break;
                case Constant.Completion:
                    CompletionQuestionModle completion = (CompletionQuestionModle) pageMap.get(i);
                    String ancompletion = completion.getAnswerflag();

                    listdata.add(new QuestionModle(completion.getCurrentpage(), ancompletion));
//                    Logger.d( "AnswerCardActivity-----填空getSubjectype :" + questionModle.getQuestion_cate_id() );
//                    Logger.d( "AnswerCardActivity-----填空getAnswerflag:" + ancompletion );
                    break;

                default:
                    break;
            }

        }
//
//
////        tvanswer.setText(answernum + "/" + listdata.size());
////
        ApAnswerCard apAnswerCard = new ApAnswerCard(context, listdata);
        grildview.setAdapter(apAnswerCard);

    }

    private void initialize() {
        tvtrue = getView(R.id.tv_true);
        tvfalse = getView(R.id.tv_false);
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

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("answerCard", beSaveAnswerCard);
//
                    DjhJumpUtil.getInstance().startBaseActivity(AnswerCardActivity.this, HomeWorkResultActivity.class, bundle, ANSWERCARD);
                    ActivityUtil.getInstance().finishActivity(DoHomeworkActivity.class);//结束指定的activity
                    finishActivity();
                    break;

                default:
                    break;
            }

        }
    };
}
