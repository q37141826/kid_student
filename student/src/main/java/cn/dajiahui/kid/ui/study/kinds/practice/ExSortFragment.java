package cn.dajiahui.kid.ui.study.kinds.practice;

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
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.study.kinds.practice.myinterface.ExMoveLocation;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExFixedImagview;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExMoveImagview;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cn.dajiahui.kid.ui.study.kinds.practice.DoPraticeActivity.screenWidth;


/**
 * 练习 排序题
 */
public class ExSortFragment extends ExBaseHomeworkFragment implements
        View.OnClickListener, CheckHomework, ExMoveLocation {

    private SortQuestionModle inbasebean;
    private RelativeLayout relaroot, answerroot;
    private SubmitSortFragment submit;
    int mLeftTop = 0;
    int mRightTop = 0;
    // 左邊視圖
    private List<ExMoveImagview> leftViewsNoanswer = new ArrayList<>();//作答时候
    // 左邊視圖
    private List<ExMoveImagview> leftViews = new ArrayList<>();
    // 右边视图
    private List<ExFixedImagview> rightViews = new ArrayList<>();
    private boolean calculation = false;//false 监听  测量连线题图片的左右第一个 坐标
    private final int RIGHT = 1;
    private final int LEFT = 2;

    private List<BeLocation> pointRightList = new ArrayList<>(); //右视图坐标点的集合
    private List<BeLocation> pointLeftList = new ArrayList<>();//左视图位置的集合


    private Map<Integer, BeLocation> sortMineAnswerMap = new HashMap<>();//我的答案（ isanswer=1）
    private Map<Integer, BeLocation> sortRightAnswerMap = new HashMap<>();//正确答案（ isanswer=1）

    private TextView tv_sort, mSchedule;
    private ImageView sort_img_play;//播放器按钮
    private Map<Integer, BeLocation> mMineAnswerMap = new HashMap<>();//（isanswer=0）
    private List<String> mRightContentList;//正确答案的顺序（内容是选项图片的网址）
    private List<String> mMineContentList;//我的答案的顺序（我的答案内容是选项的网址）
    private String media;
    private String title;
    private Bundle bundle;
    private ScrollView mSortScrollview;
    public static int mExSortScrollviewHeight;//srcllowview高度

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

                        inbasebean.getInitSortMyanswerList().add("㊒");
                    }

                    mExSortScrollviewHeight = mSortScrollview.getHeight();
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
        mSchedule.setText(bundle.getString("currntQuestion"));
        getAnswerList();
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

    /*添加右侧图片*/
    private void addGroupImage(int size, RelativeLayout rela) {
        ExFixedImagview fixedImagview = null;
        for (int i = 0; i < size; i++) {
            if (inbasebean.isAnswer() == true) {
                fixedImagview = new ExFixedImagview(getActivity(), i, inbasebean, mMineContentList);
            } else {
                fixedImagview = new ExFixedImagview(getActivity(), i, inbasebean);
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth / 5, screenWidth / 5);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp.topMargin = mRightTop;
            mRightTop += screenWidth / 4;

            lp.rightMargin = screenWidth / 6;
            fixedImagview.setLayoutParams(lp);
            /*check之后*/
            if (inbasebean.isAnswer() == true) {
                RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(screenWidth / 5, screenWidth / 5);
                paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView imageViewT = new ImageView(getActivity());
                imageViewT.setLayoutParams(paramsT);
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
            rela.addView(fixedImagview); //动态添加图片
        }
    }

    /*添加左可以动的侧图片*/
    private void addGroupMoviewImage(int size, RelativeLayout rela) {
        ExMoveImagview mMoveView = null;
        leftViews.clear();
        for (int i = 0; i < size; i++) {
            if (inbasebean.isAnswer() == true) {
                mMoveView = new ExMoveImagview(getActivity(), i, inbasebean, mRightContentList);
            } else {
                mMoveView = new ExMoveImagview(getActivity(), this, i, inbasebean);
                leftViewsNoanswer.add(mMoveView);
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth / 5, screenWidth / 5);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lp.topMargin = mLeftTop;
            mLeftTop += screenWidth / 4;
            lp.leftMargin = screenWidth / 6;
            leftViews.add(mMoveView);
            mMoveView.setLayoutParams(lp);
            rela.addView(mMoveView); //动态添加图片
        }
    }

    /**********************************************排序题翻页后逻辑修改正常*/
    /*mBeforeView 当前view  position 当前view的索引  X Y 中心点坐标 */
    /*手指抬起时的view是件*/
    @Override
    public BeLocation submitCenterPoint(ExMoveImagview mBeforeView, int position, float X, float Y) {

        /*未答题状态下*/
        if (inbasebean.isAnswer() == false) {
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
                            ExMoveImagview moveImagview = leftViews.get(a);
                            int indexOf = leftViews.indexOf(moveImagview);//找到对应view的索引
                            BeLocation beLocation = pointLeftList.get(indexOf);//通过索引找到位置信息
                            moveImagview.refreshLocation(beLocation);
                            mMineAnswerMap.put(indexOf, beLocation);
                            inbasebean.setOptions(inbasebean.getOptions());
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
                    /*保存移动之后的坐标点  position 是当前移动view的在leftViews的索引*/
                    mMineAnswerMap.put((indexOf), beLocation);
                    inbasebean.setSortAnswerMap(mMineAnswerMap);//我的答案的集合
                    inbasebean.setOptions(inbasebean.getOptions());
                    inbasebean.setAnswerflag("true");//答题标志
                    submit.submitSoreFragment(inbasebean);//告诉活动每次滑动的数据
                }

            }

        }

        return null;

    }

    @Override
    public void RefreshDown() {
        mSortScrollview.scrollBy(0, 5);

    }

    @Override
    public void RefreshUp() {
        mSortScrollview.scrollBy(0, -5);
    }

    @Override
    public void setArguments(Bundle bundle) {
        this.bundle = bundle;

        inbasebean = (SortQuestionModle) bundle.get("SortQuestionModle");
        inbasebean.setEachposition(bundle.getInt("position"));
        media = inbasebean.getMedia();
        title = inbasebean.getTitle();
    }

    /*初始化*/
    private void initialize() {
        sort_img_play = getView(R.id.sort_img_play);
        mSortScrollview = getView(R.id.sort_scrollview);
        tv_sort = getView(R.id.tv_sort);
        mSchedule = getView(R.id.tv_schedule);
        answerroot = getView(R.id.answerroot);
        relaroot = getView(R.id.relaroot);
        sort_img_play.setOnClickListener(this);

        sort_img_play.setBackground(animationDrawable);


    }


    @Override
    public void submitHomework(Object questionModle) {
        inbasebean = (SortQuestionModle) questionModle;

        relaroot.removeAllViews();
        if (inbasebean.isAnswer() == true) {

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
            /*正确答案*/
            mLeftTop = 0;
            /*添加左侧图片 正确答案全部添加遮罩*/
            addGroupMoviewImage(mRightContentList.size(), relaroot);
            /*我的答案*/
            mRightTop = 0;

            /*遍历我的答案（自己作答答案）的集合*/
            for (int i = 0; i < inbasebean.getInitSortMyanswerList().size(); i++) {
                /*遍历解析的集合找到 我的答案所对应的val*/
                for (int t = 0; t < inbasebean.getOptions().size(); t++) {
                    /*如果val值相等*/
                    if (inbasebean.getInitSortMyanswerList().get(i).equals(inbasebean.getOptions().get(t).getVal())) {
                        mMineContentList.add(inbasebean.getOptions().get(t).getContent());
                    }
                }
            }
            /*添加右侧视图*/
            addGroupImage(mMineContentList.size(), relaroot);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    /*我的答案  正确答案的点击事件*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.sort_img_play:
                if (!media.equals("")) {
                    playMp3(media);
                } else {
                    audioDialog.show();
                }

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

    }


    /*获取正确答案*/
    private void getAnswerList() {

        /*正确答案 start*/
        String standard_answer = inbasebean.getStandard_answer();
        List<String> mSandardAnswerList = new ArrayList<>();//截取字符串的集合参考答案）

        mRightContentList = new ArrayList<>();//参考答案内容的顺序集合
        mMineContentList = new ArrayList<>();//我的答案内容的集合
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
        }/*正确答案 end*/
    }
}

