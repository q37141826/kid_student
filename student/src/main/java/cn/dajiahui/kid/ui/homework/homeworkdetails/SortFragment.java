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
    int mRightTop = 0;
    // 左邊視圖
    private List<MoveImagview> leftViews = new ArrayList<>();
    // 右边视图
    private List<FixedImagview> rightViews = new ArrayList<>();
    private boolean calculation = false;//false 监听  测量连线题图片的左右第一个 坐标
    private final int RIGHT = 1;
    private final int LEFT = 2;
    private int PREPARERIGHT = 3;//准备数据
    private int PREPMINEARERIGHT = 4;//准备数据我的答案
    private List<BeLocation> pointRightList = new ArrayList<>();//左视图位置的集合
    private List<BeLocation> pointLeftList = new ArrayList<>();//右视图坐标点的集合
    private List<String> substringRightList = new ArrayList<>();//截取字符串的集合（正确答案）
    private List<String> substringMineList = new ArrayList<>();//截取字符串的集合（正确答案）

    private List<BeLocation> mRightList = new ArrayList<>();//check后装正确答案点坐标的集合
    private List<BeLocation> mMineList = new ArrayList<>();//check后装我的答案点坐标的集合


    private Map<Integer, BeLocation> sortMineAnswerMap = new HashMap<>();//我的答案
    private Map<Integer, BeLocation> sortRightAnswerMap = new HashMap<>();//正确答案
    private TextView mRight;/*正确答案*/
    private TextView mLeft;/*我的答案*/

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
              /*算法  计算每个view的位置*/
            if (msg.what == LEFT) {
                Point pRight = (Point) msg.obj;

                 /*第一个左边第一个点的X Y*/
                int pLeftX = pRight.x;
                int pLeftY = pRight.y;
                for (int i = 0; i < (leftViews.size()); i++) {
                    sortRightAnswerMap.put((i + 1), new BeLocation(pLeftX, pLeftY, leftViews.get(0).getRight(), leftViews.get(i).getBottom(), leftViews.get(0).getWidth(), leftViews.get(0).getHeight()));
                    pointLeftList.add(new BeLocation(pLeftX, pLeftY, leftViews.get(0).getRight(), leftViews.get(i).getBottom(), leftViews.get(0).getWidth(), leftViews.get(0).getHeight()));
                    pLeftY = (pLeftY += 300);//左边所有点的y坐标
                }
                handler.sendEmptyMessage(PREPARERIGHT);
                handler.sendEmptyMessage(PREPMINEARERIGHT);
            }
            if (msg.what == RIGHT) {
                Point pRight = (Point) msg.obj;

                /*第一个左边第一个点的X Y*/
                int pRightX = pRight.x;
                int pRightY = pRight.y;
                for (int i = 0; i < (rightViews.size()); i++) {
                    pointRightList.add(new BeLocation(pRightX, pRightY, rightViews.get(0).getRight(), rightViews.get(i).getBottom(), rightViews.get(0).getWidth(), rightViews.get(0).getHeight()));
                    pRightY = (pRightY += 300);//左边所有点的y坐标
                }
            }
                /*为点击正确答案准备数据*/
            if (msg.what == PREPARERIGHT) {
                String standard_answer = inbasebean.getStandard_answer();

                String[] strs = standard_answer.split(",");
                  /*截取字符串*/
                for (int i = 0, len = strs.length; i < len; i++) {
                    String split = strs[i].toString();
                    substringRightList.add(split);
//                    Logger.d("majin", "正确答案:" + substringRightList.get(i));
                }

                handler.sendEmptyMessage(4);
            }

            /*为点击我的答案准备数据*/
            if (msg.what == PREPMINEARERIGHT) {

            }
            if (msg.what == 4) {
                for (int a = 0; a < substringRightList.size(); a++) {
//                    Logger.d("majin", "正确答案:" + substringRightList.toString());
                    /*循环取出*/
                    BeLocation beLocation = sortRightAnswerMap.get(substringRightList.get(a));
//                    Logger.d("majin", "正确答案:" + beLocation.toString());
                    mRightList.add(beLocation);
                }
            }
        }
    };
    private ImageView sort_img_play;


    @Override

    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_sort, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();

        /*没有答过题    初始化  视图布局 is_answer=0*/
        /*添加右侧视图*/
        addGroupImage(inbasebean.getOptions().size(), relaroot);
        //添加左侧图片
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


    /*添加左右侧图片*/
    private void addGroupImage(int size, RelativeLayout lin) {

        for (int i = 0; i < size; i++) {
            FixedImagview fixedImagview = new FixedImagview(getActivity(), R.drawable.ic_launcher, i, inbasebean);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp.topMargin = mRightTop;
            mRightTop += 300;
            lp.rightMargin = 100;
            fixedImagview.setLayoutParams(lp);
            rightViews.add(fixedImagview);
            lin.addView(fixedImagview); //动态添加图片
        }

    }

    /*添加左可以动的侧图片*/
    private void addGroupMoviewImage(int size, RelativeLayout lin) {

        for (int i = 0; i < size; i++) {
            MoveImagview mMoveView = new MoveImagview(getActivity(), this, i, inbasebean);
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

    /*手指抬起时的view*/
    @Override
    public BeLocation submitCenterPoint(MoveImagview mBeforeView, float X, float Y) {
        /*没有点击cheke的时候可以作答*/
        if (inbasebean.isAnswer() != true) {
            for (int i = 0; i < pointRightList.size(); i++) {
            /*算法  循环判断中心点  */
                int getLeft = pointRightList.get(i).getGetLeft();
                int getTop = pointRightList.get(i).getGetTop();
                int width = pointRightList.get(i).getWidth();
                int height = pointRightList.get(i).getHeight();

                boolean left = (float) getLeft <= X;
                boolean right = (float) getLeft + (float) width >= X;
                boolean top = (float) getTop <= Y;
                boolean bottom = (float) getTop + (float) height >= Y;

             /*判断中心点在右侧的view视图上*/
                if (left && right && top && bottom) {
                       /*让上一个视图回到原来的位置*/
                    for (int a = 0; a < leftViews.size(); a++) {
                        int mLeft = leftViews.get(a).getLeft();
                        int mTop = leftViews.get(a).getTop();

                        if (getLeft == mLeft && getTop == mTop) {
                            MoveImagview moveImagview = leftViews.get(a);
                            moveImagview.refreshLocation(pointLeftList.get(leftViews.indexOf(moveImagview)));


                            submit.submitSoreFragment(inbasebean);
                        }
                    }
                    /*获取坐标点*/
                    BeLocation beLocation = pointRightList.get(i);
                    /*保存坐标点答案*/
                    sortMineAnswerMap.put((i + 1), beLocation);
                    inbasebean.setSortAnswerMap(sortMineAnswerMap);
                    inbasebean.setAnswerflag("true");//答题标志
                    submit.submitSoreFragment(inbasebean);//告诉活动每次连线的数据

                    /*保存作答答案 1*/
                    return beLocation;
                } else {

                    int indexOf = leftViews.indexOf(mBeforeView);
                    BeLocation beLocation = pointLeftList.get(indexOf);
                     /*保存作答答案 2*/
                    mBeforeView.refreshLocation(beLocation);

                }


            }


        }
        return null;

    }


    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (SortQuestionModle) bundle.get("SortQuestionModle");

//        Logger.d("majin", "inbasebean " + inbasebean.getStandard_answer());
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    /*点击check回调的接口*/
    @Override
    public void submitHomework(Object questionModle) {
        inbasebean = (SortQuestionModle) questionModle;
        Map<Integer, BeLocation> sortAnswerMap = inbasebean.getSortAnswerMap();

        if (inbasebean.isAnswer() == true) {
            /*添加我的答案  和正确答案按钮*/
            mLeft.setText("我的答案");
            mRight.setText("正确答案");
            /*自己答题 非网络请求*/
            /*显示我的答案  和正确答案*/


            for (int a = 0; a < leftViews.size(); a++) {
                BeLocation beLocation = sortAnswerMap.get(a + 1);
                MoveImagview moveImagview = leftViews.get(a);
                moveImagview.refreshLocation(beLocation);
                leftViews.get(a).lockMoveImage(inbasebean);
            }

            /*显示正确答案*/
//            for (int i = 0; i < leftViews.size(); i++) {
//
//                /*预留方法*/
//                /*取出对应的坐标点*/
//                BeLocation beLocation =   mRightList.get((i ));
//                leftViews.get(i).refreshLocation(beLocation);
//                leftViews.get(i).lockMoveImage(inbasebean);
//
//            }
        } else {
//            Logger.d("majin", "1111111111我的答案:" + sortAnswerMap.size());
            /*weicheck  显示答案逻辑不完整*/
            for (int a = 0; a < leftViews.size(); a++) {
                BeLocation beLocation = sortAnswerMap.get(a + 1);
                MoveImagview moveImagview = leftViews.get(a);
                moveImagview.refreshLocation(beLocation);
                leftViews.get(a).lockMoveImage(inbasebean);
//                sortMineAnswerMap.put((a + 1), beLocation);
            }
        }

        //TODO  // 向后台传答案 网络请求解析正确答案后

    }

    /*我的答案  ，正确答案的点击事件*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mLeft:
                mLeft.setTextColor(getResources().getColor(R.color.white));
                mLeft.setBackgroundResource(R.color.btn_green_noraml);
                mRight.setTextColor(getResources().getColor(R.color.btn_green_noraml));
                mRight.setBackgroundResource(R.color.white);

                Toast.makeText(activity, "我的答案", Toast.LENGTH_SHORT).show();


                break;
            case R.id.mRight:

                if (inbasebean.isAnswer() == true) {

                }
                break;
            case R.id.sort_img_play:
                playMp3("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");

                break;

            default:
                break;
        }
    }

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
}

