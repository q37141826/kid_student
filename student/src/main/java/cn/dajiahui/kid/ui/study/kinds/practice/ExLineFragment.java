package cn.dajiahui.kid.ui.study.kinds.practice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.json.GsonUtil;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.Dir;
import cn.dajiahui.kid.ui.homework.bean.DrawPath;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.Point;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.view.DrawView;
import cn.dajiahui.kid.ui.study.kinds.practice.myinterface.ExSublineinfo;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExDrawView;
import cn.dajiahui.kid.ui.study.kinds.practice.view.ExLineImagePointView;


/**
 * 连线题
 */
/*显示我的答案和正确答案之后要禁止连线*/
public class ExLineFragment extends ExBaseHomeworkFragment implements
        CheckHomework, ExSublineinfo, View.OnClickListener {


    private LineQuestionModle inbasebean;
    private SubmitLineFragment submit;
    private ImageView img_play;
    private TextView mLeft, mRight, tv_line;
    private RelativeLayout selectview_root, draw_root;

    private ExDrawView drawView;
    private ExLineImagePointView currentSelectedView;//当前选中的view
    // 左邊視圖
    private List<ExLineImagePointView> leftViews = new ArrayList<>();
    // 右边视图
    private List<ExLineImagePointView> rightViews = new ArrayList<>();
    int mLeftTop = 0;
    int mRightTop = 0;
    final List<Point> listPoint = new ArrayList<>();
    private int LEFT = 1;
    private int RIGHT = 2;
    private int PREPARERIGHT = 3;//准备数据
    private int PREPMINEARERIGHT = 4;//准备数据
    private List<DrawPath> drawPathList = new ArrayList();//保存绘制路径的集合

    private Map<String, Point> ponitViewXY = new HashMap();//通过val获取point点的map（提供显示正确答案 和 自己的答案 用）
    private boolean calculation = false;//false 监听  测量连线题图片的左右第一个 坐标
    private List<String> substringRightList = new ArrayList<>();//截取字符串的集合（正确答案）
    private List<String> substringMineList = new ArrayList<>();//截取字符串的集合（我的答案）
    private List<String> mLeftAnswerList = new ArrayList<>();//保存正确答案的集合（左边）
    private List<String> mRightAnswerList = new ArrayList<>();//保存正确答案的集合（右边）

    private List<String> mLeftMineAnswerList = new ArrayList<>();//保存我的答案的集合（左边）
    private List<String> mRightMineAnswerList = new ArrayList<>();//保存我的答案的集合（右边）

    private List<Point> mRightLPonitList = new ArrayList<>();//正确答案左边point集合
    private List<Point> mRightRPonitList = new ArrayList<>();// 正确答案右边point集合
    private List<Point> mMineLPonitList = new ArrayList<>();//我的答案左边point集合
    private List<Point> mMineRPonitList = new ArrayList<>();//我的答案右边point集
    private Map<String, String> myanswerMap = new HashMap<>();//我的答案集合（val对应）
    private String media;
    private List<ExLineImagePointView> mRemoveLeftList = new ArrayList<>();//移除左边的View的集合
    private List<ExLineImagePointView> mRemoveRightList = new ArrayList<>();//移除右边的View的集合
    private Map<String, ExLineImagePointView> showT_RMap = new HashMap<>();//用于显示判断划线的颜色
    private String title;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_line, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (LineQuestionModle) bundle.get("LineQuestionModle");
        inbasebean.setEachposition(bundle.getInt("position"));
        media = inbasebean.getMedia();
        title = inbasebean.getTitle();
    }

    /*添加左右侧图片*/
    private void addGroupImage(int size, RelativeLayout lin, Dir direction) {

        for (int i = 0; i < size; i++) {
            ExLineImagePointView mView = new ExLineImagePointView(getActivity(), this, i, inbasebean, direction);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (direction == Dir.left) {
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                lp.topMargin = mLeftTop;
                mLeftTop += 300;
                lp.leftMargin = 50;
                leftViews.add(mView);
                showT_RMap.put("" + (i + 1), mView);

            } else {
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                lp.topMargin = mRightTop;
                lp.rightMargin = 50;
                mRightTop += 300;
                rightViews.add(mView);
                showT_RMap.put("" + (i + 1 + size), mView);
            }
            mView.setLayoutParams(lp);
            lin.addView(mView); //动态添加图片
//            myanswerMap.put((i + 1) + "", "");//初始化答案集合
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LEFT) {
                Point pLeft = (Point) msg.obj;
                listPoint.add(pLeft);
                /*第一个左边第一个点的X Y*/
                int pLeftX = pLeft.getX();
                int pLeftY = pLeft.getY();
                for (int i = 0; i < leftViews.size(); i++) {
                    ponitViewXY.put("" + (i + 1), new Point(pLeftX, pLeftY, "" + (i + 1)));
                    pLeftY = pLeftY += 300;//左边所有点的y坐标

                }

            } else if (msg.what == RIGHT) {
                Point pRight = (Point) msg.obj;
                listPoint.add(pRight);
                /*第一个左边第一个点的X Y*/
                int pRightX = pRight.getX();
                int pRightY = pRight.getY();

                for (int i = 0; i < rightViews.size(); i++) {
                    ponitViewXY.put("" + ((i + 1) + leftViews.size()), new Point(pRightX, pRightY, "" + ((i + 1) + leftViews.size())));
                    pRightY = pRightY += 300;//左边所有点的y坐标
                    if (ponitViewXY.size() == (leftViews.size() * 2)) {
                        handler.sendEmptyMessage(PREPARERIGHT);
                        handler.sendEmptyMessage(PREPMINEARERIGHT);
                    }
                }


            }
            /*为点击正确答案准备数据*/
            if (msg.what == PREPARERIGHT) {
                if (ponitViewXY.size() == (leftViews.size() * 2)) {// && inbasebean.getIs_answer().equals("1")&& inbasebean.getStandard_answer() != null
                             /*获取正确答案的 坐标点*/
                    for (int m = 0; m < mLeftAnswerList.size(); m++) {
                        Point pointL = ponitViewXY.get(mLeftAnswerList.get(m));
                        mRightLPonitList.add(pointL);
                        Point pointR = ponitViewXY.get(mRightAnswerList.get(m));
                        mRightRPonitList.add(pointR);
                    }
                }
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRightAnswer();
        initialize();
        tv_line.setText(title);
        /*非空校验*/
        if (inbasebean.getOptions().getRight() != null) {
            //添加左侧图片
            addGroupImage(inbasebean.getOptions().getRight().size(), selectview_root, Dir.left);
            //添加右侧图片
            addGroupImage(inbasebean.getOptions().getRight().size(), selectview_root, Dir.right);

            /*监听视图加载*/
            ViewTreeObserver observer = selectview_root.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!calculation) {
                             /*左边*/
                        if (leftViews.get(0).getChildAt(0) != null) {
                            int leftPointViewX = (leftViews.get(0).getLeft()) + (leftViews.get(0).getWidth());
                            /*获取第一个控件的距离左边的距离*/
                            int leftpointViewY = leftViews.get(0).getChildAt(0).getTop() + leftViews.get(0).getChildAt(0).getHeight() / 2;
                            Message msg = Message.obtain();
                            msg.obj = new Point(leftPointViewX, leftpointViewY);
                            msg.what = LEFT;
                            handler.sendMessage(msg);
                            calculation = !calculation;
                        }
                          /*右边*/
                        if (rightViews.get(0).getChildAt(0) != null) {
                            int rightPointViewX = rightViews.get(0).getLeft() + 15;
                            int rightPointViewY = rightViews.get(0).getChildAt(0).getTop() + rightViews.get(0).getChildAt(0).getHeight() / 2;
                            Message msg = Message.obtain();
                            msg.what = RIGHT;
                            msg.obj = new Point(rightPointViewX, rightPointViewY);
                            handler.sendMessage(msg);
                        }
                    }
                }
            });
        }

    }

    /*初始化*/
    @SuppressLint("ResourceType")
    private void initialize() {
        draw_root = getView(R.id.draw_root);
        selectview_root = getView(R.id.selectview_root);
        img_play = getView(R.id.img_play);
        mLeft = getView(R.id.mLeft);
        mRight = getView(R.id.mRight);
        tv_line = getView(R.id.tv_line);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        img_play.setOnClickListener(this);
    }

    /*删除线练习模块*/
    private void removeLineHomework(ExLineImagePointView firstView, ExLineImagePointView secondView) {
        /* 查找firstView */
        for (int i = 0; i < drawPathList.size(); i++) {
            DrawPath drawPath = drawPathList.get(i);
            if (firstView.getDirection() == Dir.left) {
                if (drawPath.getLeftPoint().getY() == firstView.getPoint().getY()) {
                    draw_root.removeViewAt(i);
                    // 当前这点是有线的，要删除
                    drawPathList.remove(i);
                    break;
                }
            } else {
                if (drawPath.getRightPoint().getY() == firstView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);
                    // 当前这点是有线的，要删除
                    drawPathList.remove(i);
                    break;
                }
            }
        }
        /* 查找secondView */
        for (int i = 0; i < drawPathList.size(); i++) {
            DrawPath drawPath = drawPathList.get(i);
            if (secondView.getDirection() == Dir.left) {
                if (drawPath.getLeftPoint().getY() == secondView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);

                    // 当前这点是有线的，要删除
                    drawPathList.remove(i);

                    break;
                }
            } else {
                if (drawPath.getRightPoint().getY() == secondView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);

                    // 当前这点是有线的，要删除
                    drawPathList.remove(i);

                    break;
                }
            }
        }
    }

    /*添加线练习模块*/
    private void addLineHomework(ExLineImagePointView firstView, ExLineImagePointView secondView) {
        DrawPath path = null;
        if (firstView.getDirection() == Dir.left) {
            myanswerMap.put(firstView.value, secondView.value);
            path = new DrawPath(firstView.getPoint(), secondView.getPoint());
        } else {
            myanswerMap.put(secondView.value, firstView.value);
            path = new DrawPath(secondView.getPoint(), firstView.getPoint());
        }


        drawPathList.add(path);

    }


    /*要连线的图片的点击事件*/
    @Override
    public void submitlininfo(ExLineImagePointView lineImagePointView) {
         /*false 未 check  true 已经check*/
        if (inbasebean.isAnswer() == false) {
            //判断当前选择的view是否为空 若等于空把点击的view赋值给当前选择的view
            if (currentSelectedView == null) {
                lineImagePointView.selected(true);//设置为选中
                currentSelectedView = lineImagePointView;
            } else {//当前选择的view不为空  把点击的
            /*判断再次点击的是不是自己  是自己 就把 点击的view付给选择的currentSelectedView*/
                if (lineImagePointView == currentSelectedView) {
                    lineImagePointView.selected(false);//设置非选中
                    currentSelectedView = null;
                } else {
                /*再次点击的不是自己*/
                /* 判断是否同侧点 */
                    if (currentSelectedView.getDirection() == lineImagePointView.getDirection()) {
                        currentSelectedView.selected(false); // 把上一个view设置成非选中
                        lineImagePointView.selected(true); // 把当前的view设置成选中
                        currentSelectedView = lineImagePointView;
                    } else {
                      /* 非同侧view */
                      /*先删除*/
                        removeLineHomework(currentSelectedView, lineImagePointView);
                        /*必须每次都要new出来 要不不显示划线*/
                        drawView = new ExDrawView(getActivity(), currentSelectedView, lineImagePointView);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                        draw_root.addView(drawView, params);
                        addLineHomework(currentSelectedView, lineImagePointView);
                        currentSelectedView.selected(false);
                        currentSelectedView = null;
                        lineImagePointView.selected(false);

                          inbasebean.setAnswerflag("true");//答题标志

//                        inbasebean.setDrawPathList(drawPathList);
                        inbasebean.setMyanswerMap(myanswerMap);
                        submit.submitLineFragment(inbasebean);//告诉活动每次连线的数据


                    }
                }

            }
        }

    }

    /*翻页回来掉的接口*/
    @Override
    public void submitHomework(Object questionModle) {
        inbasebean = (LineQuestionModle) questionModle;
        /*获取我的答案*/
        getMineAnswer();
        mLeft.setText("正确答案");
        mRight.setText("我的答案");
        showRightAnswer();

    }


    /*我的答案  ，正确答案的点击事件*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_play:
                playMp3(media);
                break;
            case R.id.mLeft:
                if (!mOnclickAnswer) {
                       /*清空我的答案的遮罩*/
                    for (int i = 0; i < mRemoveLeftList.size(); i++) {
                        mRemoveLeftList.get(i).mContentView.removeView(mMaskRightListR.get(i));
                        mRemoveLeftList.remove(i);
                        mMaskRightListR.remove(i);
                        mRemoveRightList.get(i).mContentView.removeView(mMaskMineListR.get(i));
                        mMaskMineListR.remove(i);
                        mRemoveRightList.remove(i);
                    }
                        /*显示正确答案*/
                    showRightAnswer();
                }

                break;
            case R.id.mRight:
                if (mOnclickAnswer) {
                         /*清空*/
                    for (int i = 0; i < leftViews.size(); i++) {
                        leftViews.get(i).mContentView.removeView(mMaskRightListL.get(i));
                        rightViews.get(i).mContentView.removeView(mMaskMineListL.get(i));
                    }
                    mMaskRightListL.clear();
                    mMaskMineListL.clear();
                    /*显示我的答案*/
                    showMineAnswer();
                }
                break;
            default:
                break;
        }
    }


    /*与activity通信*/
    public interface SubmitLineFragment {
        public void submitLineFragment(LineQuestionModle questionModle);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitLineFragment) activity;

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
        mediaPlayer.stop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    /*我的答案*/
    public void showMineAnswer() {
        if (inbasebean != null) {
            mOnclickAnswer = !mOnclickAnswer;
            /*保证我的答案的点的集合有值*/
            if (mMineRPonitList.size() > 0) {

                mRight.setBackgroundResource(R.drawable.line_answer_bg_yellow_fbf12);
                mLeft.setTextColor(getResources().getColor(R.color.black));
                mRight.setTextColor(getResources().getColor(R.color.yellow_FEBF12));
                mLeft.setBackgroundResource(R.drawable.line_answer_bg_gray_97938f);

                drawPathList.clear();
                /*划线父布局清空view*/
                draw_root.removeAllViews();

                for (int n = 0; n < mMineRPonitList.size(); n++) {
                    DrawPath drawPath = new DrawPath(mMineRPonitList.get(n), mMineLPonitList.get(n));
                    DrawView drawView = null;

                        /*正确答案 左边的集合数和我的答案左边的集合数相等 改变画线的颜色*/
                    if ((mLeftMineAnswerList.get(n).equals(mLeftAnswerList.get(n))
                            && mRightMineAnswerList.get(n).equals(mRightAnswerList.get(n)))) {

                            /*改变划线颜色 green*/
                        drawView = new DrawView(getActivity(), getResources().getColor(R.color.green_9DEAA6));

                             /*添加左边view的遮罩*/
                        RelativeLayout.LayoutParams paramsL = new RelativeLayout.LayoutParams(200, 200);
                        paramsL.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageViewL = new ImageView(getActivity());
                        imageViewL.setLayoutParams(paramsL);
                        imageViewL.setBackgroundResource(R.drawable.answer_true_bg);
                        mMaskRightListR.add(imageViewL);
                        showT_RMap.get(mMineRPonitList.get(n).getVal()).mContentView.addView(imageViewL);
                        /*改变小点颜色 绿色*/
                        showT_RMap.get(mMineRPonitList.get(n).getVal()).pointview.setcolor(getResources().getColor(R.color.green_9DEAA6));
                        showT_RMap.get(mMineRPonitList.get(n).getVal()).pointview.refreshPonitColor();
                        mRemoveLeftList.add(showT_RMap.get(mMineRPonitList.get(n).getVal()));/*添加右边view的遮罩*/
                        RelativeLayout.LayoutParams paramsR = new RelativeLayout.LayoutParams(200, 200);
                        paramsR.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageViewR = new ImageView(getActivity());
                        imageViewR.setLayoutParams(paramsR);
                        imageViewR.setBackgroundResource(R.drawable.answer_true_bg);
                        mMaskMineListR.add(imageViewR);
                        showT_RMap.get(mMineLPonitList.get(n).getVal()).mContentView.addView(imageViewR);
                        showT_RMap.get(mMineLPonitList.get(n).getVal()).pointview.setcolor(getResources().getColor(R.color.green_9DEAA6));
                        showT_RMap.get(mMineLPonitList.get(n).getVal()).pointview.refreshPonitColor();
                        mRemoveRightList.add(showT_RMap.get(mMineLPonitList.get(n).getVal()));
                    } else {
                        /*改变划线颜色 red*/
                        drawView = new DrawView(getActivity(), getResources().getColor(R.color.red));
                        /*添加遮罩*/
                        RelativeLayout.LayoutParams paramsL = new RelativeLayout.LayoutParams(200, 200);
                        paramsL.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageViewL = new ImageView(getActivity());
                        imageViewL.setLayoutParams(paramsL);
                        imageViewL.setBackgroundResource(R.drawable.answer_false_bg);
                        mMaskRightListR.add(imageViewL);
                        showT_RMap.get(mMineRPonitList.get(n).getVal()).mContentView.addView(imageViewL);
                        /*改变小点颜色 红色*/
                        showT_RMap.get(mMineRPonitList.get(n).getVal()).pointview.setcolor(getResources().getColor(R.color.red));
                        showT_RMap.get(mMineRPonitList.get(n).getVal()).pointview.refreshPonitColor();
                        mRemoveLeftList.add(showT_RMap.get(mMineRPonitList.get(n).getVal()));

                        /*添加右边view的遮罩*/
                        RelativeLayout.LayoutParams paramsR = new RelativeLayout.LayoutParams(200, 200);
                        paramsR.addRule(RelativeLayout.CENTER_IN_PARENT);
                        ImageView imageViewR = new ImageView(getActivity());
                        imageViewR.setLayoutParams(paramsR);
                        imageViewR.setBackgroundResource(R.drawable.answer_false_bg);
                        mMaskMineListR.add(imageViewR);
                        showT_RMap.get(mMineLPonitList.get(n).getVal()).mContentView.addView(imageViewR);
                        showT_RMap.get(mMineLPonitList.get(n).getVal()).pointview.refreshPonitColor();
                        showT_RMap.get(mMineLPonitList.get(n).getVal()).pointview.setcolor(getResources().getColor(R.color.red));
                        mRemoveRightList.add(showT_RMap.get(mMineLPonitList.get(n).getVal()));
                    }

                    drawView.DrawViewOnback(drawPath);
                    drawPathList.add(drawPath);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                    draw_root.addView(drawView, params);

                }
            }
        }
    }

    private List<ImageView> mMaskRightListL = new ArrayList();//正确答案的遮罩list 左侧
    private List<ImageView> mMaskMineListL = new ArrayList();//我的答案的遮罩list  左侧
    private List<ImageView> mMaskRightListR = new ArrayList();//正确答案的遮罩list 右侧
    private List<ImageView> mMaskMineListR = new ArrayList();//我的答案的遮罩list  右侧
    private Boolean mOnclickAnswer = false;//false可以点击正确答案

    /*显示正确答案*/
    private void showRightAnswer() {
        mOnclickAnswer = !mOnclickAnswer;
        mRight.setBackgroundResource(R.drawable.line_answer_bg_gray_97938f);
        mRight.setTextColor(getResources().getColor(R.color.black));

        mLeft.setBackgroundResource(R.drawable.line_answer_bg_yellow_fbf12);
        mLeft.setTextColor(getResources().getColor(R.color.yellow_FEBF12));
        drawPathList.clear();
        /*划线父布局清空view*/
        draw_root.removeAllViews();

        for (int n = 0; n < mRightLPonitList.size(); n++) {
            DrawPath drawPath = new DrawPath(mRightLPonitList.get(n), mRightRPonitList.get(n));
            DrawView drawView = new DrawView(getActivity(), getResources().getColor(R.color.green_9DEAA6));
            drawView.DrawViewOnback(drawPath);
            drawPathList.add(drawPath);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
            draw_root.addView(drawView, params);
            /*改变小点颜色*/
            leftViews.get(n).pointview.setcolor(getResources().getColor(R.color.green_9DEAA6));
            rightViews.get(n).pointview.setcolor(getResources().getColor(R.color.green_9DEAA6));
            leftViews.get(n).pointview.refreshPonitColor();
            rightViews.get(n).pointview.refreshPonitColor();

            /*正确答案添加遮罩（左边视图）*/
            RelativeLayout.LayoutParams paramsL = new RelativeLayout.LayoutParams(200, 200);
            paramsL.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageViewL = new ImageView(getActivity());
            imageViewL.setLayoutParams(paramsL);
            imageViewL.setBackgroundResource(R.drawable.answer_true_bg);
            mMaskRightListL.add(imageViewL);
            leftViews.get(n).mContentView.addView(imageViewL);
           /*正确答案添加遮罩（右边视图）*/
            RelativeLayout.LayoutParams paramsR = new RelativeLayout.LayoutParams(200, 200);
            paramsR.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageViewR = new ImageView(getActivity());
            imageViewR.setLayoutParams(paramsR);
            imageViewR.setBackgroundResource(R.drawable.answer_true_bg);
            mMaskMineListL.add(imageViewR);
            rightViews.get(n).mContentView.addView(imageViewR);

        }


    }

    /*获取答案的方法  正确答案 2个*/
    private void getRightAnswer() {
           /*正确答案start */
        String standard_answer = inbasebean.getStandard_answer();
        String substring = standard_answer.substring(1, (standard_answer.length() - 1));
        String[] strSt = substring.split(",");
                              /*截取字符串*/
        for (int i = 0, len = strSt.length; i < len; i++) {
            String split = strSt[i].toString();
            substringRightList.add(split);
        }
                             /*二次截取 获取答案 获取 */
        for (int l = 0; l < substringRightList.size(); l++) {
            String split1 = substringRightList.get(l).toString().substring(1, 2);
            mLeftAnswerList.add(split1);
            String s = substringRightList.get(l).toString();
            String split2 = null;
            if (s.length() == 5) {/*配合测试后台数据*/
                split2 = substringRightList.get(l).toString().substring(4);
            } else {
                split2 = substringRightList.get(l).toString().substring(5, 6);
            }
            mRightAnswerList.add(split2);
        }
        /*正确答案end */
    }
    /*获取答案的方法  我的答案 2个*/
    private void getMineAnswer() {

        JsonElement jsonElement = new GsonUtil().getJsonElement(myanswerMap);
        /*我的答案start */
        String substringmyanswer = jsonElement.toString().substring(1, (jsonElement.toString().length() - 1));
        String[] strsMy = substringmyanswer.split(",");
                             /*截取字符串*/
        for (int i = 0, len = strsMy.length; i < len; i++) {
            String split = strsMy[i].toString();
            substringMineList.add(split);
        }
                             /*二次截取 获取答案 获取 */
        for (int l = 0; l < substringMineList.size(); l++) {
            String split1 = substringMineList.get(l).toString().substring(1, 2);
            mLeftMineAnswerList.add(split1);
            String s = substringMineList.get(l).toString();
            String split2 = null;
            if (s.length() == 5) {/*配合测试后台数据*/
                split2 = substringMineList.get(l).toString().substring(4);
            } else {
                split2 = substringMineList.get(l).toString().substring(5, 6);
            }
            mRightMineAnswerList.add(split2);
        }

        if (ponitViewXY.size() == (leftViews.size() * 2)) {
            /*获取我的答案的 坐标点*/
            for (int m = 0; m < mLeftAnswerList.size(); m++) {
                Point pointL = ponitViewXY.get(mLeftMineAnswerList.get(m));
                mMineLPonitList.add(pointL);
                Point pointR = ponitViewXY.get(mRightMineAnswerList.get(m));
                mMineRPonitList.add(pointR);
            }
        }
        /*我的答案end */
    }
}




