package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.myinterface.MoveLocation;
import cn.dajiahui.kid.ui.homework.view.FixedImagview;
import cn.dajiahui.kid.ui.homework.view.MoveImagview;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity.screenWidth;


/**
 * 排序题
 */


public class SortFragment extends BaseHomeworkFragment implements
        View.OnClickListener, CheckHomework, MoveLocation {

    private SortQuestionModle inbasebean;
    private RelativeLayout relaroot, answerroot;
    private SubmitSortFragment submit;

    int mLeftTop = 0;
    int mTop = 0;
    int mRightTop = 0;

    // 左邊視圖
    private List<MoveImagview> leftViews = new ArrayList<>();
    // 右边视图
    private List<FixedImagview> rightViews = new ArrayList<>();


    private boolean calculation = false;//false 监听  测量连线题图片的左右第一个 坐标
    private final int RIGHT = 1;
    private final int LEFT = 2;
    private final int PREPARERIGHT = 3;//准备数据
    private final int PREPMINEARERIGHT = 4;//准备数据我的答案

    private List<BeLocation> pointRightList = new ArrayList<>(); //右视图坐标点的集合
    private List<BeLocation> pointLeftList = new ArrayList<>();//左视图位置的集合

    private List<String> substringRightList = new ArrayList<>();//截取字符串的集合（正确答案）
    private List<String> substringMineList = new ArrayList<>();//截取字符串的集合（我的答案）


    private Map<Integer, BeLocation> sortMineAnswerMap = new HashMap<>();//我的答案（ isanswer=1）
    private Map<Integer, BeLocation> sortRightAnswerMap = new HashMap<>();//正确答案（ isanswer=1）

    private TextView tv_sort, tv_schedule;
    private ImageView sort_img_play;//播放器按钮
    private String media;
    private List<String> mRightContentList;
    private List<String> mMineContentList;
    private String title;

    public static boolean isLinecheck = false;//江湖救急  后续更改
    private Map<Integer, BeLocation> mMineAnswerMap = new HashMap<>();//（isanswer=0）
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case LEFT:
                      /*算法  计算每个view的位置*/
                    Point pLeft = (Point) msg.obj;
                    /*第一个左边第一个点的X Y*/
                    int pLeftX = pLeft.x;
                    int pLeftY = pLeft.y;
                    for (int i = 0; i < (leftViews.size()); i++) {
                        BeLocation beLocation = new BeLocation(pLeftX, pLeftY, leftViews.get(0).getRight(), leftViews.get(i).getBottom(), leftViews.get(0).getWidth(), leftViews.get(0).getHeight());
                        sortRightAnswerMap.put((i + 1), beLocation);
                        pointLeftList.add(beLocation);
                        pLeftY = (pLeftY += screenWidth / 4);//左边所有点的y坐标
                    }
                    Message msg1 = Message.obtain();
                    msg1.what = PREPARERIGHT;
                    handler.sendMessage(msg1);

                    break;
                case RIGHT:
                    /*算法  计算每个view的位置*/
                    Point pRight = (Point) msg.obj;
                    /*第一个右边第一个点的X Y*/
                    int pRightX = pRight.x;
                    int pRightY = pRight.y;
                    for (int i = 0; i < (rightViews.size()); i++) {
                        BeLocation beLocation = new BeLocation(pRightX, pRightY, rightViews.get(0).getRight(), rightViews.get(i).getBottom(), rightViews.get(0).getWidth(), rightViews.get(0).getHeight());
                        sortMineAnswerMap.put((i + 1), beLocation);
                        pointRightList.add(beLocation);
                        pRightY = (pRightY += screenWidth / 4);//左边所有点的y坐标

                    }
                    Message msg2 = Message.obtain();
                    msg2.what = PREPMINEARERIGHT;
                    handler.sendMessage(msg2);
                    break;
                case PREPARERIGHT: /*为点击正确答案准备数据*/
                    if (inbasebean.getStandard_answer() != null && inbasebean.getOptions() != null) {
                        String standard_answer = inbasebean.getStandard_answer();
                        String[] strs = standard_answer.split(",");
                        /*截取字符串*/
                        for (int i = 0, len = strs.length; i < len; i++) {
                            String split = strs[i].toString();
                            substringRightList.add(split);
                        }
                    }
                    break;
                case PREPMINEARERIGHT:  /*我的答案准备数据*/
                    if (inbasebean.getMy_answer() != null && inbasebean.getOptions() != null) {
                        String getMy_answer = inbasebean.getMy_answer();
                        String[] strs = getMy_answer.split(",");
                        /*截取字符串*/
                        for (int i = 0, len = strs.length; i < len; i++) {
                            String split = strs[i].toString();
                            substringMineList.add(split);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_sort, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        tv_sort.setText(title);
        tv_schedule.setText(bundle.getString("currntQuestion"));
        if (inbasebean.getIs_answered().equals("1")) {
            showAnswer();
            getAnswerList();
        }
        /*添加右侧视图*/
        addGroupImage(inbasebean.getOptions().size(), relaroot);
       /*添加左侧图片*/
        addGroupMoviewImage(inbasebean.getOptions().size(), relaroot);

        /*监听 relaroot 上的子视图绘制完成*/
        ViewTreeObserver observer = relaroot.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!calculation) {
                     /*左边*/
                    if (leftViews.get(0) != null) {
                        int leftPointViewX = leftViews.get(0).getLeft();
                        int leftPointViewY = leftViews.get(0).getTop();
                        Point point = new Point(leftPointViewX, leftPointViewY);
                        Message msg = Message.obtain();
                        msg.what = LEFT;
                        msg.obj = point;
                        handler.sendMessage(msg);
                    }
                    /*右边*/
                    if (rightViews.get(0).getChildAt(0) != null) {
                        int rightPointViewX = rightViews.get(0).getLeft();
                        int rightPointViewY = rightViews.get(0).getTop();
                        Point point = new Point(rightPointViewX, rightPointViewY);
                        Message msg = Message.obtain();
                        msg.what = RIGHT;
                        msg.obj = point;
                        handler.sendMessage(msg);
                    }

                    calculation = true;
                }
            }
        });
    }

    /*显示正确，我的答案文案*/
    private void showAnswer() {
        TextView mLeft = new TextView(getActivity());
        mLeft.setText("正确答案");
        mLeft.setTextColor(getResources().getColor(R.color.gray_9f938f));
        RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lpLeft.leftMargin = screenWidth / 5;
        answerroot.addView(mLeft);
        mLeft.setLayoutParams(lpLeft);

        TextView mRight = new TextView(getActivity());
        mRight.setText("我的答案");
        mRight.setTextColor(getResources().getColor(R.color.gray_9f938f));
        RelativeLayout.LayoutParams lpRight = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        lpRight.rightMargin = screenWidth / 5;
        answerroot.addView(mRight);
        mRight.setLayoutParams(lpRight);
    }

    /*添加右侧图片*/
    private void addGroupImage(int size, RelativeLayout lin) {

        for (int i = 0; i < size; i++) {
            FixedImagview fixedImagview = new FixedImagview(getActivity(), R.drawable.default_null, i, mMineContentList, inbasebean);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp.topMargin = mRightTop;
            mRightTop += screenWidth / 4;
            lp.rightMargin = screenWidth / 6;
            fixedImagview.setLayoutParams(lp);

            RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageViewT = new ImageView(getActivity());
            imageViewT.setLayoutParams(paramsT);
            if (inbasebean.getIs_answered().equals("1")) {
                if (mMineContentList.size() > 0 && mRightContentList.size() > 0 &&
                        mRightContentList.get(i).equals(mMineContentList.get(i))) {
                /*正确答案 添加遮罩*/
                    imageViewT.setBackgroundResource(R.drawable.answer_true_bg);
                } else {
                 /*错误答案 添加遮罩*/
                    imageViewT.setBackgroundResource(R.drawable.answer_false_bg);
                }
                fixedImagview.addView(imageViewT);
            }
            rightViews.add(fixedImagview);

            lin.addView(fixedImagview); //动态添加图片

        }

    }

    /*添加左可以动的侧图片*/

    private void addGroupMoviewImage(int size, RelativeLayout lin) {


        for (int i = 0; i < size; i++) {
            MoveImagview mMoveView = new MoveImagview(getActivity(), this, i, mRightContentList, inbasebean);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lp.topMargin = mLeftTop;
            mLeftTop += screenWidth / 4;
            lp.leftMargin = screenWidth / 6;
            leftViews.add(mMoveView);
            mMoveView.setLayoutParams(lp);
            lin.addView(mMoveView); //动态添加图片
        }
    }

    /**********************************************排序题翻页后逻辑修改正常*/
    /*mBeforeView 当前view  position 当前view的索引  X Y 中心点坐标 */
    /*手指抬起时的view是件*/
    @Override
    public BeLocation submitCenterPoint(MoveImagview mBeforeView, int position, float X, float Y) {

        /*未答题状态下*/
        if (inbasebean.getIs_answered().equals("0")) {
        /*循环便利右视图的集合  判断中心点是否在右视图某一个的范围内*/
            for (int i = 0; i < pointRightList.size(); i++) {
            /*算法  循环判断中心点是否在右边的view的范围内*/
                int getLeft = pointRightList.get(i).getGetLeft();
                int getTop = pointRightList.get(i).getGetTop();
                int width = pointRightList.get(i).getWidth();
                int height = pointRightList.get(i).getHeight();

                boolean left = (float) getLeft <= X;
                boolean right = (float) getLeft + (float) width >= X;
                boolean top = (float) getTop <= Y;
                boolean bottom = (float) getTop + (float) height >= Y;
               /*加判断条件只有未作答状态*/

             /*判断中心点在右侧的view视图上*/
                if (left && right && top && bottom) {
                       /*让上一个视图回到原来的位置*/
                    for (int a = 0; a < leftViews.size(); a++) {
                        int mLeft = leftViews.get(a).getLeft();
                        int mTop = leftViews.get(a).getTop();

                          /*移除右边图片上已经排序的view  返回到原来*/
                        if (getLeft == mLeft && getTop == mTop) {
                            MoveImagview moveImagview = leftViews.get(a);
                            int indexOf = leftViews.indexOf(moveImagview);//找到对应view的索引
                            BeLocation beLocation = pointLeftList.get(indexOf);//通过索引找到位置信息
                            moveImagview.refreshLocation(beLocation);
                            mMineAnswerMap.put(indexOf, beLocation);
                            submit.submitSoreFragment(inbasebean);
                        }
                    }


                    /*获取右视图坐标点*/
                    BeLocation beLocation = pointRightList.get(i);
                    /*保存移动之后的坐标点  position 是当前移动view的在leftViews的索引  */
                    mMineAnswerMap.put((position), beLocation);
                    inbasebean.setSortAnswerMap(mMineAnswerMap);//我的答案的集合
                    inbasebean.setAnswerflag("true");//答题标志

                    inbasebean.getInitSortMyanswerList().set(i, mBeforeView.val);
                    submit.submitSoreFragment(inbasebean);//告诉活动每次连线的数据

                    return beLocation;
                } else {

                /*找到当前view在 左视图集合的位置*/
                    int indexOf = leftViews.indexOf(mBeforeView);
                    BeLocation beLocation = pointLeftList.get(indexOf);
                    mBeforeView.refreshLocation(beLocation);
                  /*保存移动之后的坐标点  position 是当前移动view的在leftViews的索引  */
                    mMineAnswerMap.put((indexOf), beLocation);
                    inbasebean.setSortAnswerMap(mMineAnswerMap);//我的答案的集合

                    inbasebean.setAnswerflag("true");//答题标志
                    submit.submitSoreFragment(inbasebean);//告诉活动每次滑动的数据
                }

            }

        }

        return null;

    }

    private Bundle bundle;

    @Override
    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
        inbasebean = (SortQuestionModle) bundle.get("SortQuestionModle");
        media = inbasebean.getMedia();
        title = inbasebean.getTitle();
    }

    /*初始化*/
    private void initialize() {
        sort_img_play = getView(R.id.sort_img_play);
        tv_schedule = getView(R.id.tv_schedule);
        tv_sort = getView(R.id.tv_sort);
        answerroot = getView(R.id.answerroot);
        relaroot = getView(R.id.relaroot);
        sort_img_play.setOnClickListener(this);
        sort_img_play.setBackground(animationDrawable);

    }


    @Override
    public void submitHomework(Object questionModle) {
        inbasebean = (SortQuestionModle) questionModle;

        if (inbasebean != null) {
            /*作业翻页回来会走 submitHomework*/

            /*条件换成后台的是否作答标记*/
            if (inbasebean.getIs_answered().equals("0")) {

                 /*获取复原的数据集合*/
                Map<Integer, BeLocation> sortAnswerMap = inbasebean.getSortAnswerMap();
                if (sortAnswerMap.size() > 0) {
                        /*自己答题 非网络请求*/
                    for (int a = 0; a < leftViews.size(); a++) {
                        BeLocation beLocation = sortAnswerMap.get(a);
                        MoveImagview moveImagview = leftViews.get(a);
                        moveImagview.refreshLocation(beLocation);
                        mMineAnswerMap.put(a, beLocation);

                    }
                    inbasebean.setAnswerflag("true");//答题标志
                    submit.submitSoreFragment(inbasebean);//告诉活动每次滑动的数据
                }
            }
        }
    }

    /*我的答案  正确答案的点击事件*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sort_img_play:
//                Toast.makeText(activity, "播放音频！", Toast.LENGTH_SHORT).show();
                playMp3(media);
                break;
            default:
                break;
        }
    }

    /*保存排序数据接口*/
    public interface SubmitSortFragment {
        public void submitSoreFragment(SortQuestionModle questionModle);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitSortFragment) activity;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        isLinecheck = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isLinecheck = false;
    }

    /*获取答案集合*/
    private void getAnswerList() {
        /*我的答案 start*/
        String my_answer = inbasebean.getMy_answer();
        List<String> mMineList = new ArrayList<>();//截取字符串的集合（我的答案）
        //我的答案的顺序集合
        mMineContentList = new ArrayList<>();
        if (!my_answer.equals("")) {
            String[] strs = my_answer.split(",");
        /*截取字符串*/
            for (int i = 0, len = strs.length; i < len; i++) {
                String split = strs[i].toString();
                mMineList.add(split);
            }

        /*遍历我的答案的集合*/
            for (int i = 0; i < mMineList.size(); i++) {
            /*遍历解析的集合找到 我的答案所对应的val*/
                for (int t = 0; t < inbasebean.getOptions().size(); t++) {
                /*如果val值相等*/
                    if (mMineList.get(i).equals(inbasebean.getOptions().get(t).getVal())) {
                        mMineContentList.add(inbasebean.getOptions().get(t).getContent());
                    }
                }
            }
        }
         /*我的答案 end*/

         /*正确答案 start*/
        String standard_answer = inbasebean.getStandard_answer();
        List<String> mSandardAnswerList = new ArrayList<>();//截取字符串的集合参考答案）
        //参考答案的顺序集合
        mRightContentList = new ArrayList<>();
        String[] strs = standard_answer.split(",");
        /*截取字符串*/
        for (int i = 0, len = strs.length; i < len; i++) {
            String split = strs[i].toString();
            mSandardAnswerList.add(split);
        }

        /*遍历正确答案的集合*/
        for (int i = 0; i < mSandardAnswerList.size(); i++) {
            /*遍历解析的集合找到 我的答案所对应的val*/
            for (int t = 0; t < inbasebean.getOptions().size(); t++) {
                /*如果val值相等*/
                if (mSandardAnswerList.get(i).equals(inbasebean.getOptions().get(t).getVal())) {
                    mRightContentList.add(inbasebean.getOptions().get(t).getContent());
                }
            }
        }
        /*正确答案 end*/
    }
}

