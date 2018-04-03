package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fxtx.framework.util.BaseUtil;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLineLeft;
import cn.dajiahui.kid.ui.homework.bean.BeLineRight;
import cn.dajiahui.kid.ui.homework.bean.Dir;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.Point;
import cn.dajiahui.kid.ui.homework.myinterface.Sublineinfo;

import static cn.dajiahui.kid.controller.Constant.pointViewDiameter;
import static cn.dajiahui.kid.controller.Constant.pointViewDiameter_margin;
import static cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity.screenWidth;

/**
 * Created by lenovo on 2018/1/12.
 * 连线题的item视图 左
 */


public class LineImagePointView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private ImageView imageViewL;
    private ImageView imageViewR;
    private TextView textViewL;
    private TextView textViewR;
    public CricleTextView pointview;
    private int cLeftposiion = 0;
    private LineQuestionModle inbasebean;
    private Dir direction;
    public String value;
    public List<BeLineLeft> lefts = new ArrayList<>();
    public List<BeLineRight> rights = new ArrayList<>();

    private final LinearLayout linRoot;//imageview或者textview 和小点的父亲布局
    public RelativeLayout mContentView;//左边的view添加遮罩的父view
    private final int screenWidth;
    private RelativeLayout.LayoutParams mParams;


    @SuppressLint("ResourceType")
    public void selected(boolean flag) {
        if (flag) {
//            mContentView.setBackgroundColor(getResources().getColor(R.color.red));//设置选中状态背景色
        } else {

//            mContentView.setBackgroundColor(getResources().getColor(R.color.transparent));//设置选中状态背景色//
        }
    }

    public LineQuestionModle getInbasebean() {
        return inbasebean;
    }

    public Dir getDirection() {
        return direction;
    }

    public void setDirection(Dir direction) {
        this.direction = direction;
    }

    private final Sublineinfo sublineinfo;
    private Point point;

    public Point getPoint() {
        Point point = new Point();
        if (direction == Dir.left) {
            point.setX(this.getLeft() + pointview.getLeft() + pointViewDiameter / 2); // 小圆点直径的一半
        } else {
            point.setX(getLeft() + pointViewDiameter / 2);
        }
        point.setY(this.getTop() + pointview.getTop() + pointViewDiameter / 2);
        return point;
    }

    @SuppressLint("ResourceType")
    public LineImagePointView(Context context, Sublineinfo sublineinfo, int cLeftposiion, LineQuestionModle inbasebean, Dir direction) {
        super(context);
        this.sublineinfo = sublineinfo;
        this.context = context;
        //获取屏幕宽度
        screenWidth = BaseUtil.getWidthPixels((Activity) context);
        this.cLeftposiion = cLeftposiion;
        this.direction = direction;
        this.inbasebean = inbasebean;
        this.lefts = inbasebean.getOptions().getLeft();
        this.rights = inbasebean.getOptions().getRight();

        if (!inbasebean.getIs_complete().equals("1")) {
            this.setOnClickListener(this);
        }
        /*imageview和textview的大小*/


        linRoot = new LinearLayout(context);


        if (direction == Dir.left) {//左边

            String content = inbasebean.getOptions().getLeft().get(cLeftposiion).getContent();

            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                mParams = new RelativeLayout.LayoutParams(screenWidth / 5, screenWidth / 5);
                imageViewL = addLImageView();
                imageViewL.setLayoutParams(mParams);
                mContentView = new RelativeLayout(context);
                mContentView.addView(imageViewL);
                linRoot.addView(mContentView);

            } else {
                mParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mContentView = new RelativeLayout(context);
                textViewL = addLTextView();
                mParams.addRule(CENTER_IN_PARENT);
                textViewL.setLayoutParams(mParams);
                mContentView.addView(textViewL);

                linRoot.addView(mContentView);

            }
            mContentView.setPadding(5, 5, 5, 5);
            mContentView.setBackgroundResource(R.drawable.line_bg);
            this.value = inbasebean.getOptions().getLeft().get(cLeftposiion).getVal();
            addPointLeft();//添加左边小黑点

        } else {//右边

            this.value = inbasebean.getOptions().getRight().get(cLeftposiion).getVal();
            addPointRight();//添加右边小黑点
            String content = inbasebean.getOptions().getRight().get(cLeftposiion).getContent();

            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                mContentView = new RelativeLayout(context);
                mParams = new RelativeLayout.LayoutParams(screenWidth/5, screenWidth/5);
                imageViewR = addRImageView();
                imageViewR.setLayoutParams(mParams);
                mContentView.addView(imageViewR);
                linRoot.addView(mContentView);

            } else {
                mParams = new RelativeLayout.LayoutParams(screenWidth/5, screenWidth/5);
                mContentView = new RelativeLayout(context);
                textViewR = addRTextView();
                mParams.addRule(CENTER_IN_PARENT);
                mContentView.addView(textViewR);
                textViewR.setLayoutParams(mParams);
                linRoot.addView(mContentView);
            }
            mContentView.setPadding(5, 5, 5, 5);
            mContentView.setBackgroundResource(R.drawable.line_bg);
        }
        this.selected(false);

        this.addView(linRoot);
    }


    /*添加左侧图片*/
    private ImageView addLImageView() {
        ImageView imageView = new ImageView(context);
        Glide.with(context)
                .load(inbasebean.getOptions().getLeft().get(cLeftposiion).getContent())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return imageView;
    }

    /*添加左侧文字*/
    private TextView addLTextView() {

        mParams = new RelativeLayout.LayoutParams(screenWidth/5, screenWidth/5);
        mParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(context);
        textView.setText(inbasebean.getOptions().getLeft().get(cLeftposiion).getContent());
        textView.setLayoutParams(mParams);
        return textView;
    }


    /*添加右侧图片*/
    private ImageView addRImageView() {
        ImageView imageView = new ImageView(context);
        Glide.with(context)
                .load(inbasebean.getOptions().getRight().get(cLeftposiion).getContent())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return imageView;
    }

    /*添加右侧文字*/
    private TextView addRTextView() {
        mParams = new RelativeLayout.LayoutParams(screenWidth/5, screenWidth/5);
        mParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(context);
        textView.setText(inbasebean.getOptions().getRight().get(cLeftposiion).getContent());
        textView.setLayoutParams(mParams);
        return textView;
    }


    /*添加小黑点 右*/
    @SuppressLint("ResourceType")
    public void addPointRight() {
        pointview = new CricleTextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pointViewDiameter, pointViewDiameter);
        lp.rightMargin = pointViewDiameter_margin;
        //设置居中显示：
        lp.gravity = Gravity.CENTER;
        pointview.setLayoutParams(lp);
        linRoot.addView(pointview);


    }

    /*添加小黑点 左*/
    public void addPointLeft() {

        pointview = new CricleTextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pointViewDiameter, pointViewDiameter);
        lp.leftMargin = pointViewDiameter_margin;
        //设置居中显示：
        lp.gravity = Gravity.CENTER;
        pointview.setLayoutParams(lp);
        linRoot.addView(pointview);

    }


    @Override
    public void onClick(View v) {
//        this.setBackgroundResource(R.drawable.btnline);
        /*0 未作答  1 已经提交过了*/
        if (!inbasebean.getIs_complete().equals("1")) {
            sublineinfo.submitlininfo(this);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

    }

    /*测量子视图*/
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}
