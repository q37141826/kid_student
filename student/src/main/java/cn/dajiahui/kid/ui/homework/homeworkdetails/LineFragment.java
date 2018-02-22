package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.dajiahui.kid.ui.homework.myinterface.Sublineinfo;
import cn.dajiahui.kid.ui.homework.view.DrawView;
import cn.dajiahui.kid.ui.homework.view.LineImagePointView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * 连线题
 */
/*显示我的答案和正确答案之后要禁止连线*/
public class LineFragment extends BaseHomeworkFragment implements
        CheckHomework, Sublineinfo, View.OnClickListener {


    private LineQuestionModle inbasebean;
    private SubmitLineFragment submit;
    private ImageView img_play;
    private TextView mLeft, mRight;
    private RelativeLayout selectview_root, draw_root;
    private LinearLayout linroot;
    private DrawView drawView;
    private LineImagePointView currentSelectedView;//当前选中的view
    // 左邊視圖
    private List<LineImagePointView> leftViews = new ArrayList<>();
    // 右边视图
    private List<LineImagePointView> rightViews = new ArrayList<>();
    int mLeftTop = 0;
    int mRightTop = 0;
    final List<Point> listPoint = new ArrayList<>();
    private int LEFT = 1;
    private int RIGHT = 2;
    private int PREPARERIGHT = 3;//准备数据
    private int PREPMINEARERIGHT = 4;//准备数据
    private List<DrawPath> drawPathList = new ArrayList();//保存绘制路径的集合
    private List<DrawPath> praticeDrawPathList = new ArrayList();//保存绘制路径的集合

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
    private List<Point> mMineRPonitList = new ArrayList<>();//我的答案右边point集合


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_line, null);
    }

    @Override
    public void setArguments(Bundle bundle) {
        /*OK 解析连线*/
//        String zz = "{    \"data\": {        \"book_id\": 8,        \"id\": 4,        \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",        \"options\": {            \"left\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109y5aih34p.png\",                    \"label\": \"头部label\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109sge2pcdz.png\",                    \"label\": \"颈部label\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099mnygtvk.png\",                    \"label\": \"胸部label\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099p4ryhvt.png\",                    \"label\": \"尾部label\",                    \"type\": \"1\",                    \"val\": \"4\"                }            ],            \"right\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01098rwcvhz7.png\",                    \"label\": \"head label\",                    \"type\": \"1\",                    \"val\": \"5\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109xvkfimpt.png\",                    \"label\": \"neck label\",                    \"type\": \"1\",                    \"val\": \"6\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109fxrt53uw.png\",                    \"label\": \"chest label\",                    \"type\": \"1\",                    \"val\": \"7\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109vp24bntc.png\",                    \"label\": \"foot label\",                    \"type\": \"1\",                    \"val\": \"8\"                }            ]        },        \"org_id\": 100,        \"question_cate_id\": 4,        \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",        \"school_id\": 3,        \"standard_answer\": \"{3:5,2:6,1:7,4:8}\",        \"title\": \"连线题的示例\",        \"unit_id\": 7    },    \"msg\": \"成功\",    \"status\": 0}";
//        HeadJson h2 = new HeadJson(zz);
//        inbasebean = h2.parsingObject(LineQuestionModle.class);
        inbasebean = (LineQuestionModle) bundle.get("LineQuestionModle");

//        Logger.d("连线 inbasebean getAnswerflag. "+inbasebean.getAnswerflag());
//        Logger.d("连线 inbasebean getQuestion_cate_id. "+ inbasebean.getQuestion_cate_id());

    }

    /*添加左右侧图片*/
    private void addGroupImage(int size, RelativeLayout lin, Dir direction) {

        /*应该判断是否是从check过 从网上获取的答案*/
        /*不是从网上*/
        for (int i = 0; i < size; i++) {
            LineImagePointView mView = new LineImagePointView(getActivity(), this, R.drawable.ic_launcher, i, inbasebean, direction);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

            if (direction == Dir.left) {
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                lp.topMargin = mLeftTop;
                mLeftTop += 300;
                lp.leftMargin = 50;
                leftViews.add(mView);


            } else {
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                lp.topMargin = mRightTop;
                lp.rightMargin = 50;
                mRightTop += 300;
                rightViews.add(mView);
            }
            mView.setLayoutParams(lp);
            lin.addView(mView); //动态添加图片
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
                    ponitViewXY.put("" + (i + 1), new Point(pLeftX, pLeftY));
                    pLeftY = pLeftY += 300;//左边所有点的y坐标

                }

            } else if (msg.what == RIGHT) {
                Point pRight = (Point) msg.obj;
                listPoint.add(pRight);
                /*第一个左边第一个点的X Y*/
                int pRightX = pRight.getX();
                int pRightY = pRight.getY();

                for (int i = 0; i < rightViews.size(); i++) {
                    ponitViewXY.put("" + ((i + 1) + leftViews.size()), new Point(pRightX, pRightY));
                    pRightY = pRightY += 300;//左边所有点的y坐标
                    if (ponitViewXY.size() == (leftViews.size() * 2)) {

                        handler.sendEmptyMessage(PREPARERIGHT);
                        handler.sendEmptyMessage(PREPMINEARERIGHT);
                    }
                }


            }
            /*为点击正确答案准备数据*/
            if (msg.what == PREPARERIGHT) {
                if (inbasebean != null) {
                    if (inbasebean.getIs_answer().equals("1")) {
                        mRight.setText("正确答案");
                    }
                    if (ponitViewXY.size() == (leftViews.size() * 2) && inbasebean.getStandard_answer() != null) {// && inbasebean.getIs_answer().equals("1")
                        String standard_answer = inbasebean.getStandard_answer();
                        String substring = standard_answer.substring(1, (standard_answer.length() - 1));
                        String[] strs = substring.split(",");
                   /*截取字符串*/
                        for (int i = 0, len = strs.length; i < len; i++) {
                            String split = strs[i].toString();
                            substringRightList.add(split);
                        }
                    /*二次截取 获取答案 获取 */
                        for (int l = 0; l < substringRightList.size(); l++) {
                            String split1 = substringRightList.get(l).toString().substring(0, 1);
                            mLeftAnswerList.add(split1);
                            String split2 = substringRightList.get(l).toString().substring(2);
                            mRightAnswerList.add(split2);
                        }

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
            /*为我的答案准备数据*/
            if (msg.what == PREPMINEARERIGHT) {
                if (inbasebean != null) {
                    if (inbasebean.getIs_answer().equals("1")) {
                        mLeft.setText("我的答案");
                    }
                            /*获取json解析的我的答案*/
                    if (ponitViewXY.size() == (leftViews.size() * 2) && inbasebean.getMy_answer() != null
                            && inbasebean.getMy_answer().length() > 0) { // 要判断json传过来的数据是“”的情况
                        String my_answer = inbasebean.getMy_answer();

                        String substringmyanswer = my_answer.substring(1, (my_answer.length() - 1));
                        String[] strs = substringmyanswer.split(",");
                             /*截取字符串*/
                        for (int i = 0, len = strs.length; i < len; i++) {
                            String split = strs[i].toString();
                            substringMineList.add(split);
                        }
                             /*二次截取 获取答案 获取 */
                        for (int l = 0; l < substringMineList.size(); l++) {
                            String split1 = substringMineList.get(l).toString().substring(0, 1);
                            mLeftMineAnswerList.add(split1);
                            String split2 = substringMineList.get(l).toString().substring(2);
                            mRightMineAnswerList.add(split2);
                        }
                            /*获取我的答案的 坐标点*/
                        for (int m = 0; m < mLeftAnswerList.size(); m++) {
                            Point pointL = ponitViewXY.get(mLeftMineAnswerList.get(m));
                            mMineLPonitList.add(pointL);
                            Point pointR = ponitViewXY.get(mRightMineAnswerList.get(m));
                            mMineRPonitList.add(pointR);
                        }
                            /*显示我的答案*/
                        if (DoHomeworkActivity.sourceFlag.equals("HomeWork") && inbasebean.getIs_answer().equals("1")) {
                            showMineAnswer();
                        }

                    }
                }
            }

        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();

        //添加左侧图片
        addGroupImage(inbasebean.getOptions().getRight().size(), selectview_root, Dir.left);
        //添加右侧图片
        addGroupImage(inbasebean.getOptions().getRight().size(), selectview_root, Dir.right);


        ViewTreeObserver observer = selectview_root.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!calculation) {
                 /*左边*/
                    if (leftViews.get(0).getChildAt(0) != null) {
                        int leftPointViewX = (leftViews.get(0).getLeft()) + (leftViews.get(0).getWidth()) - 15;
                    /*获取第一个控件的距离左边的距离*/
                        int leftpointViewY = leftViews.get(0).getChildAt(0).getTop() + 15;
                        Message msg = Message.obtain();
                        msg.obj = new Point(leftPointViewX, leftpointViewY);
                        msg.what = LEFT;
                        handler.sendMessage(msg);
                        calculation = !calculation;
                    }
                 /*右边*/
                    if (rightViews.get(0).getChildAt(0) != null) {
                        int rightPointViewX = rightViews.get(0).getLeft() + 15;
                        int rightPointViewY = rightViews.get(0).getChildAt(0).getTop() + 15;
                        Message msg = Message.obtain();
                        msg.what = RIGHT;
                        msg.obj = new Point(rightPointViewX, rightPointViewY);
                        handler.sendMessage(msg);
                    }
                }
            }
        });


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*初始化*/
    @SuppressLint("ResourceType")
    private void initialize() {
        draw_root = getView(R.id.draw_root);

        linroot = getView(R.id.linroot);
        selectview_root = getView(R.id.selectview_root);
        img_play = getView(R.id.img_play);
        mLeft = getView(R.id.mLeft);
        mRight = getView(R.id.mRight);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        img_play.setOnClickListener(this);

    }

    /*删除线作业模块*/
    private void removeLineHomework(LineImagePointView firstView, LineImagePointView secondView) {
        /* 查找firstView */
        for (int i = 0; i < drawPathList.size(); i++) {
            DrawPath drawPath = drawPathList.get(i);
            if (firstView.getDirection() == Dir.left) {
                if (drawPath.getLeftPoint().getY() == firstView.getPoint().getY()) {
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                        // 当前这点是有线的，要删除
                        drawPathList.remove(i);
                    }
                    break;
                }
            } else {
                if (drawPath.getRightPoint().getY() == firstView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                        // 当前这点是有线的，要删除
                        drawPathList.remove(i);
                    }
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
                    if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                        // 当前这点是有线的，要删除
                        drawPathList.remove(i);
                    }
                    break;
                }
            } else {
                if (drawPath.getRightPoint().getY() == secondView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                        // 当前这点是有线的，要删除
                        drawPathList.remove(i);
                    }
                    break;
                }
            }
        }
    }

    /*添加线作业模块*/
    private void addLineHomework(LineImagePointView firstView, LineImagePointView secondView) {
        DrawPath path = null;
        if (firstView.getDirection() == Dir.left) {
            path = new DrawPath(firstView.getPoint(), secondView.getPoint());
        } else {
            path = new DrawPath(secondView.getPoint(), firstView.getPoint());
        }

        if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
            drawPathList.add(path);
        }
    }

    /*删除线练习模块*/
    private void removeLinePratice(LineImagePointView firstView, LineImagePointView secondView) {
        /* 查找firstView */
        for (int i = 0; i < praticeDrawPathList.size(); i++) {
            DrawPath drawPath = praticeDrawPathList.get(i);
            if (firstView.getDirection() == Dir.left) {
                if (drawPath.getLeftPoint().getY() == firstView.getPoint().getY()) {
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                        // 当前这点是有线的，要删除
                        praticeDrawPathList.remove(i);
                    }
                    break;
                }
            } else {
                if (drawPath.getRightPoint().getY() == firstView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                        // 当前这点是有线的，要删除
                        praticeDrawPathList.remove(i);
                    }
                    break;
                }
            }
        }
        /* 查找secondView */
        for (int i = 0; i < praticeDrawPathList.size(); i++) {
            DrawPath drawPath = praticeDrawPathList.get(i);
            if (secondView.getDirection() == Dir.left) {
                if (drawPath.getLeftPoint().getY() == secondView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                        // 当前这点是有线的，要删除
                        praticeDrawPathList.remove(i);
                    }
                    break;
                }
            } else {
                if (drawPath.getRightPoint().getY() == secondView.getPoint().getY()) {
                    // 当前这点是有线的，要删除
                    draw_root.removeViewAt(i);
                    if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                        // 当前这点是有线的，要删除
                        praticeDrawPathList.remove(i);
                    }
                    break;
                }
            }
        }
    }

    /*添加线练习模块*/
    private void addLinePratice(LineImagePointView firstView, LineImagePointView secondView) {
        DrawPath path = null;
        if (firstView.getDirection() == Dir.left) {
            path = new DrawPath(firstView.getPoint(), secondView.getPoint());
        } else {
            path = new DrawPath(secondView.getPoint(), firstView.getPoint());
        }

        if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
            praticeDrawPathList.add(path);
        }
    }


    /*要连线的图片的点击事件*/
    @Override
    public void submitlininfo(LineImagePointView lineImagePointView) {
        /*作业*/
        if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
        /*&&inbasebean.isAnswer()==false 是控制在 pratice中check过之后就不能点击了*/
            if (inbasebean != null && inbasebean.getIs_answer().equals("0") && inbasebean.isAnswer() == false) {
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
                            drawView = new DrawView(getActivity(), currentSelectedView, lineImagePointView);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                            draw_root.addView(drawView, params);
                            addLineHomework(currentSelectedView, lineImagePointView);
                            currentSelectedView.selected(false);
                            currentSelectedView = null;
                            lineImagePointView.selected(false);

                            inbasebean.setAnswerflag("true");//答题标志
                            inbasebean.setDrawPathList(drawPathList);
                            submit.submitLineFragment(inbasebean);//告诉活动每次连线的数据
                        }

                    }
                }

            }
        }
        /*练习模块未作答*/
        else if ((DoHomeworkActivity.sourceFlag.equals("Practice")) && inbasebean.isAnswer() == false) {

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
                        removeLinePratice(currentSelectedView, lineImagePointView);
                    /*必须每次都要new出来 要不不显示划线*/
                        drawView = new DrawView(getActivity(), currentSelectedView, lineImagePointView);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                        draw_root.addView(drawView, params);
                        addLinePratice(currentSelectedView, lineImagePointView);
                        currentSelectedView.selected(false);
                        currentSelectedView = null;
                        lineImagePointView.selected(false);

                        inbasebean.setAnswerflag("true");//答题标志
                        inbasebean.setDrawPathList(praticeDrawPathList);
                        submit.submitLineFragment(inbasebean);//告诉活动每次连线的数据
                    }

                }
            }

        }
    }

    /*翻页回来掉的接口*/
    @Override
    public void submitHomework(Object questionModle) {
        if (questionModle != null) {
            inbasebean = (LineQuestionModle) questionModle;

            if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                if (currentSelectedView != null) {
                    currentSelectedView.selected(true);
                }
                drawPathList = inbasebean.getDrawPathList();
                for (int i = 0; i < drawPathList.size(); i++) {
                    DrawPath drawPath = drawPathList.get(i);
                    drawPath.setPathColor(getResources().getColor(R.color.btn_green_noraml));
                    DrawView drawView = new DrawView(getActivity());
                    drawView.DrawViewOnback(drawPath);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                    draw_root.addView(drawView, params);

                }
            } else if (DoHomeworkActivity.sourceFlag.equals("Practice") && inbasebean.getAnswerflag().equals("true")) {// check之后要显示正确答案和我的答案

                /*添加我的答案  和正确答案按钮*/
                mLeft.setText("我的答案");
                mLeft.setTextColor(getResources().getColor(R.color.white));
                mLeft.setBackgroundResource(R.color.btn_green_noraml);
                mRight.setTextColor(getResources().getColor(R.color.btn_green_noraml));
                mRight.setBackgroundResource(R.color.white);
                mRight.setText("正确答案");

            }


        }
    }


    /*我的答案  ，正确答案的点击事件*/
    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.img_play:
                playMp3("http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3");
                break;
            case R.id.mLeft:
                if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                    showMineAnswer();

                } else if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                    mLeft.setTextColor(getResources().getColor(R.color.white));
                    mLeft.setBackgroundResource(R.color.btn_green_noraml);
                    mRight.setTextColor(getResources().getColor(R.color.btn_green_noraml));
                    mRight.setBackgroundResource(R.color.white);
                 /*可以更新小点颜色*/
                    leftViews.get(1).pointview.setcolor(getResources().getColor(R.color.red));
                    leftViews.get(1).pointview.refreshPonitColor();

                    Toast.makeText(activity, "我的答案", Toast.LENGTH_SHORT).show();
                    draw_root.removeAllViews();
                    for (int i = 0; i < praticeDrawPathList.size();
                         i++) {
                        DrawPath drawPath = praticeDrawPathList.get(i);
//                        drawPath.setPathColor(getResources().getColor(R.color.btn_green_noraml));
                        DrawView drawView = new DrawView(getActivity());
                        drawView.DrawViewOnback(drawPath);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                        draw_root.addView(drawView, params);

                    }

                }

                break;
            case R.id.mRight:
                if (DoHomeworkActivity.sourceFlag.equals("HomeWork")) {
                    draw_root.removeAllViews();
                    showRightAnswer();
                } else if (DoHomeworkActivity.sourceFlag.equals("Practice")) {
                    draw_root.removeAllViews();
                    showRightAnswer();
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
        if (inbasebean != null && inbasebean.getIs_answer().equals("1")) {
            mLeft.setTextColor(getResources().getColor(R.color.white));
            mLeft.setBackgroundResource(R.color.btn_green_noraml);
            mRight.setTextColor(getResources().getColor(R.color.btn_green_noraml));
            mRight.setBackgroundResource(R.color.white);
                 /*可以更新小点颜色*/
            leftViews.get(1).pointview.setcolor(getResources().getColor(R.color.red));
            leftViews.get(1).pointview.refreshPonitColor();
            drawPathList.clear();

            draw_root.removeAllViews();
            for (int n = 0; n < mMineRPonitList.size(); n++) {
                DrawPath drawPath = new DrawPath(mMineRPonitList.get(n), mMineLPonitList.get(n));
                DrawView drawView = new DrawView(getActivity());
                drawView.DrawViewOnback(drawPath);
                drawPathList.add(drawPath);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
                draw_root.addView(drawView, params);
            }

            inbasebean.setDrawPathList(drawPathList);
            submit.submitLineFragment(inbasebean);//告诉活动每次连线的数据
        }
    }

    /*显示正确答案*/
    private void showRightAnswer() {
//        if (inbasebean != null && inbasebean.getIs_answer().equals("1")) {
        mRight.setTextColor(getResources().getColor(R.color.white));
        mRight.setBackgroundResource(R.color.btn_green_noraml);
        mLeft.setTextColor(getResources().getColor(R.color.btn_green_noraml));
        mLeft.setBackgroundResource(R.color.white);
        drawPathList.clear();
                /*条件换成后台提供的是否回答的标志 json获得*/

        draw_root.removeAllViews();
        for (int n = 0; n < mRightLPonitList.size(); n++) {
            DrawPath drawPath = new DrawPath(mRightLPonitList.get(n), mRightRPonitList.get(n));
            DrawView drawView = new DrawView(getActivity());
            drawView.DrawViewOnback(drawPath);
            drawPathList.add(drawPath);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, selectview_root.getMeasuredHeight());
            draw_root.addView(drawView, params);
        }

        inbasebean.setDrawPathList(drawPathList);//
        submit.submitLineFragment(inbasebean);//告诉活动每次连线的数据
//        Toast.makeText(activity, "正确答案", Toast.LENGTH_SHORT).show();
//        }

    }
}


