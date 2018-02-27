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
import android.widget.Toast;

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


/**
 * 排序题
 */


public class SortFragment extends BaseHomeworkFragment implements
        View.OnClickListener, CheckHomework, MoveLocation {

    private SortQuestionModle inbasebean;
    private TextView tv_1;
    private RelativeLayout relaroot;
    private SubmitSortFragment submit;

    int mLeftTop = 0;
    int mTop = 0;
    int mRightTop = 0;
    /*正确答案视图*/
    private List<MoveImagview> showRightViews = new ArrayList<>();
    // 左邊視圖
    private List<MoveImagview> leftViews = new ArrayList<>();
    // 右边视图
    private List<FixedImagview> rightViews = new ArrayList<>();


    private boolean calculation = false;//false 监听  测量连线题图片的左右第一个 坐标
    private final int RIGHT = 1;
    private final int LEFT = 2;
    private final int PREPARERIGHT = 3;//准备数据
    private final int PREPMINEARERIGHT = 4;//准备数据我的答案
    private final int GETRIGHTANSWER = 5;//正确答案
    private final int GETMINEANSWER = 6;//我的答案

    private List<BeLocation> pointRightList = new ArrayList<>(); //右视图坐标点的集合
    private List<BeLocation> pointLeftList = new ArrayList<>();//左视图位置的集合

    private List<String> substringRightList = new ArrayList<>();//截取字符串的集合（正确答案）
    private List<String> substringMineList = new ArrayList<>();//截取字符串的集合（我的答案）


    private Map<Integer, BeLocation> sortMineAnswerMap = new HashMap<>();//我的答案（ isanswer=1）
    private Map<Integer, BeLocation> sortRightAnswerMap = new HashMap<>();//正确答案（ isanswer=1）
    private TextView mRight;/*正确答案*/
    private TextView mLeft;/*我的答案*/
    private ImageView sort_img_play;//播放器按钮

    private List<String> initMyanswerList = new ArrayList<>();//初始我的答案集合（用于获取我的答案顺序）


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
                        pLeftY = (pLeftY += 300);//左边所有点的y坐标
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
                        pRightY = (pRightY += 300);//左边所有点的y坐标
                        initMyanswerList.add("");
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
                            if (inbasebean.getIs_answer().equals("1") && substringRightList.size() == inbasebean.getOptions().size()) {/*应该是1  测试用0*/
                                Message msg3 = Message.obtain();
                                msg3.what = GETRIGHTANSWER;
                                handler.sendMessage(msg3);
                            }
                            /*练习模块正确答案准备数据*/
                            if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                                Message msg3 = Message.obtain();
                                msg3.what = GETRIGHTANSWER;
                                handler.sendMessage(msg3);
                            }

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
                            if (inbasebean.getIs_answer().equals("1") && substringMineList.size() == inbasebean.getOptions().size()) {
                                Message msg4 = Message.obtain();
                                msg4.what = GETMINEANSWER;
                                handler.sendMessage(msg4);
                            }
                        }

                    }
                    break;

                case GETRIGHTANSWER://正确答案

//                    if (sortRightAnswerMap.size() > 0) {
//                        if (inbasebean.getIs_answer().equals("1")) {//作业模块submit提交之后

//                        }
//                        for (int a = 0; a < sortRightAnswerMap.size(); a++) {
//                           /*循环取出*/
//                            BeLocation beLocation = sortRightAnswerMap.get(a + 1);
//                            MoveImagview moveImagview = showRightViews.get((Integer.parseInt(substringRightList.get(a)) - 1));
//                            moveImagview.showAnswer(beLocation);
//                        }
//                    }

                    break;

                case GETMINEANSWER:  /*sunmit 后我的答案 */
                    if (sortMineAnswerMap.size() > 0) {

//                        for (int a = 0; a < sortMineAnswerMap.size(); a++) {
                         /*循环取出*/
//                            BeLocation beLocation = sortMineAnswerMap.get(a + 1);
//                            MoveImagview moveImagview = leftViews.get((Integer.parseInt(substringMineList.get(a)) - 1));

//                              /*正确答案 添加遮罩*/
//                            RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(150, 150);
//                            paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
//                            ImageView imageViewT = new ImageView(getActivity());
//                            imageViewT.setLayoutParams(paramsT);
//
//                            if (substringMineList.get(a).equals(substringRightList.get(a))) {
//                                imageViewT.setBackgroundResource(R.drawable.answer_true_bg);
//                            } else {
//                                imageViewT.setBackgroundResource(R.drawable.answer_false_bg);
//                            }
//                            rightViews.get((Integer.parseInt(substringMineList.get(a)) - 1)).addView(imageViewT);
//                            moveImagview.showAnswer(beLocation);
//                        }
                    }
                default:
                    break;
            }
        }
    };
    private String media;
    private List<String> mRightContentList;
    private List<String> mMineContentList;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_sort, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
         /*添加左侧图片*/
        addGroupMoviewImage(inbasebean.getOptions().size(), relaroot);
        /*添加右侧视图*/
        addGroupImage(inbasebean.getOptions().size(), relaroot);


        if (inbasebean.getIs_answer().equals("1")) {
            mRight.setText("正确答案");
            mLeft.setText("我的答案");
        }
//        /*添加正确答案  网络请求已经回答过题就添加正确答案视图*/
//        if (inbasebean.getIs_answer().equals("1")) {
//            addGroupRightImage(inbasebean.getOptions().size(), relaroot);
//        }

        /*练习模块添加正确答案view  要隐藏*/
        if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
            addGroupRightImage(inbasebean.getOptions().size(), relaroot);
            if (showRightViews.size() == inbasebean.getOptions().size()) {
            /*隐藏左右view*/
                for (int i = 0; i < showRightViews.size(); i++) {
                    showRightViews.get(i).setVisibility(View.INVISIBLE);
                }
            }
        }


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

    /*添加右侧图片*/
    private void addGroupImage(int size, RelativeLayout lin) {
        String my_answer = inbasebean.getMy_answer();
        List<String> mMineList = new ArrayList<>();//截取字符串的集合（我的答案）
        //我的答案的顺序集合
        mMineContentList = new ArrayList<>();
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
        for (int i = 0; i < size; i++) {
            FixedImagview fixedImagview = new FixedImagview(getActivity(), R.drawable.default_null, i, mMineContentList, inbasebean);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp.topMargin = mRightTop;
            mRightTop += 300;
            lp.rightMargin = 100;
            fixedImagview.setLayoutParams(lp);

            RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(150, 150);
            paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageViewT = new ImageView(getActivity());
            imageViewT.setLayoutParams(paramsT);
            /*添加遮罩*/
            if (mRightContentList.get(i).equals(mMineContentList.get(i))) {
                /*正确答案 添加遮罩*/
                imageViewT.setBackgroundResource(R.drawable.answer_true_bg);
            } else {
                 /*错误答案 添加遮罩*/
                imageViewT.setBackgroundResource(R.drawable.answer_false_bg);
            }
            fixedImagview.addView(imageViewT);
            rightViews.add(fixedImagview);

            lin.addView(fixedImagview); //动态添加图片
        }

    }

    /*添加左可以动的侧图片*/
    private void addGroupMoviewImage(int size, RelativeLayout lin) {
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

        /*遍历我的答案的集合*/
        for (int i = 0; i < mSandardAnswerList.size(); i++) {
            /*遍历解析的集合找到 我的答案所对应的val*/
            for (int t = 0; t < inbasebean.getOptions().size(); t++) {
                /*如果val值相等*/
                if (mSandardAnswerList.get(i).equals(inbasebean.getOptions().get(t).getVal())) {
                    mRightContentList.add(inbasebean.getOptions().get(t).getContent());
                }
            }

        }

        for (int i = 0; i < size; i++) {
            MoveImagview mMoveView = new MoveImagview(getActivity(), this, i, mRightContentList, inbasebean);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lp.topMargin = mLeftTop;
            mLeftTop += 300;
            lp.leftMargin = 100;
            leftViews.add(mMoveView);
            mMoveView.setLayoutParams(lp);
            lin.addView(mMoveView); //动态添加图片
        }
    }

    /*添加正确答案*/
    private void addGroupRightImage(int size, RelativeLayout lin) {

        for (int i = 0; i < size; i++) {
            MoveImagview mShowRightView = new MoveImagview(getActivity(), i, inbasebean);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lp.topMargin = mTop;
            mTop += 300;
            lp.leftMargin = 100;

            mShowRightView.setLayoutParams(lp);
            showRightViews.add(mShowRightView);
            lin.addView(mShowRightView); //动态添加图片

        }
    }


    /**********************************************排序题翻页后逻辑修改正常*/
    /*mBeforeView 当前view  position 当前view的索引  X Y 中心点坐标 */
    /*手指抬起时的view是件*/
    @Override
    public BeLocation submitCenterPoint(MoveImagview mBeforeView, int position, float X, float Y) {



        /*未答题状态下*/
        if (inbasebean.getIs_answer().equals("0")) {
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
//                            Logger.d("2排序我的答案：indexOf"+indexOf);
                        }

//                        /*把左边视图的val添加到初始的集合中*/
//                        for (int s = 0; s < initMyanswerList.size(); s++) {
//                            String val = leftViews.get(a).val;
//                            Logger.d("左边当前的val： "+val);
//                        }

//                        Logger.d("右边当前的位置：i----" + i);
                    }


                    /*获取右视图坐标点*/
                    BeLocation beLocation = pointRightList.get(i);
                    /*保存移动之后的坐标点  position 是当前移动view的在leftViews的索引  */
                    mMineAnswerMap.put((position), beLocation);
                    inbasebean.setSortAnswerMap(mMineAnswerMap);//我的答案的集合
                    inbasebean.setAnswerflag("true");//答题标志

                    initMyanswerList.set(i, mBeforeView.val);
                    inbasebean.setInitMyanswerList(initMyanswerList);
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

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (SortQuestionModle) bundle.get("SortQuestionModle");
        media = inbasebean.getMedia();
    }

    /*初始化*/
    private void initialize() {
        tv_1 = getView(R.id.tv_1);
        sort_img_play = getView(R.id.sort_img_play);
        mRight = getView(R.id.mRight);
        mLeft = getView(R.id.mLeft);
        relaroot = getView(R.id.relaroot);
        sort_img_play.setOnClickListener(this);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);

    }


    @Override
    public void submitHomework(Object questionModle) {
        inbasebean = (SortQuestionModle) questionModle;

        if (inbasebean != null) {
            /*作业翻页回来会走 submitHomework*/
            if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {

            /*条件换成后台的是否作答标记*/
                if (inbasebean.getIs_answer().equals("0")) {

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
                    }
                }

            }
            /*练习check之后会走 submitHomework*/
            else if (DoHomeworkActivity.sourceFlag.equals("Practice") && inbasebean.getAnswerflag().equals("true")) {
                isLinecheck = true;
                Map<Integer, BeLocation> mPsortAnswerMap = inbasebean.getSortAnswerMap();//练习模块我的答案
                    /*自己答题 非网络请求*/
                for (int a = 0; a < leftViews.size(); a++) {
                    BeLocation beLocation = mPsortAnswerMap.get(a);
                    MoveImagview moveImagview = leftViews.get(a);
                    moveImagview.refreshLocation(beLocation);
                    mMineAnswerMap.put(a, beLocation);
                }

                   /*显示正确答案按钮*/
                for (int i = 0; i < showRightViews.size(); i++) {
                    showRightViews.get(i).setVisibility(View.VISIBLE);
                }

            }

        }
    }

    /*我的答案  正确答案的点击事件*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mLeft:
//                mLeft.setTextColor(getResources().getColor(R.color.white));
//                mLeft.setBackgroundResource(R.color.btn_green_noraml);
//                mRight.setTextColor(getResources().getColor(R.color.btn_green_noraml));
//                mRight.setBackgroundResource(R.color.white);

//                Toast.makeText(activity, "排序我的答案", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mRight:
//                mRight.setTextColor(getResources().getColor(R.color.white));
//                mRight.setBackgroundResource(R.color.btn_green_noraml);
//                mLeft.setTextColor(getResources().getColor(R.color.btn_green_noraml));
//                mLeft.setBackgroundResource(R.color.white);

//                Toast.makeText(activity, "排序正确答案", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sort_img_play:
                Toast.makeText(activity, "播放音频！", Toast.LENGTH_SHORT).show();
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

}

