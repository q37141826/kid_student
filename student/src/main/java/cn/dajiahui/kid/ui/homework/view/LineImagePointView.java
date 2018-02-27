package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLineLeft;
import cn.dajiahui.kid.ui.homework.bean.BeLineRight;
import cn.dajiahui.kid.ui.homework.bean.BeOptionViewState;
import cn.dajiahui.kid.ui.homework.bean.Dir;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.bean.Point;
import cn.dajiahui.kid.ui.homework.myinterface.Sublineinfo;

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
    private String showLeftFlag = "";


    public void selected(boolean flag) {
        if (flag) {
            this.setBackgroundResource(R.color.red);//设置选中状态背景色
        } else {

            this.setBackgroundResource(R.color.whilte_gray);//设置选中状态背景色//
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
            point.setX(this.getLeft() + pointview.getLeft() + 15); // 小圆点直径的一半
        } else {
            point.setX(getLeft() + 15);
        }
        point.setY(this.getTop() + pointview.getTop() + 15);
        return point;
    }

    @SuppressLint("ResourceType")
    public LineImagePointView(Context context, Sublineinfo sublineinfo, int cLeftposiion, LineQuestionModle inbasebean, Dir direction) {
        super(context);
        this.sublineinfo = sublineinfo;
        this.context = context;
        this.cLeftposiion = cLeftposiion;
        this.direction = direction;
        this.inbasebean = inbasebean;
        this.lefts = inbasebean.getOptions().getLeft();
        this.rights = inbasebean.getOptions().getRight();
        this.setOnClickListener(this);
        LayoutParams lp = new LayoutParams(200, 100);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);


        if (direction == Dir.left) {//左边

            String content = inbasebean.getOptions().getLeft().get(cLeftposiion).getContent();

            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
                showLeftFlag = "IMG";
                imageViewL = addLImageView();
                imageViewL.setId(R.string.show_pointleft);

                imageViewL.setLayoutParams(lp);
                this.addView(imageViewL);
            } else {
                showLeftFlag = "TXT";
                textViewL = addLTextView();
                textViewL.setId(R.string.show_pointleft);

                textViewL.setLayoutParams(lp);
                this.addView(textViewL);
            }
            this.value = inbasebean.getOptions().getLeft().get(cLeftposiion).getVal();
            addPointLeft();//添加左边小黑点

        } else {//右边

            this.value = inbasebean.getOptions().getRight().get(cLeftposiion).getVal();
            addPointRight();//添加右边小黑点
            String content = inbasebean.getOptions().getRight().get(cLeftposiion).getContent();

            if (content.startsWith("h", 0) && content.startsWith("t", 1)) {

                imageViewR = addRImageView();
                lp.leftMargin = 30;
                imageViewR.setLayoutParams(lp);

                this.addView(imageViewR);

            } else {

                textViewR = addRTextView();
                textViewR.setId(R.string.show_pointright);

                textViewR.setLayoutParams(lp);
                this.addView(textViewR);

            }


        }
        this.selected(false);

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
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        TextView textView = new TextView(context);
        textView.setText(inbasebean.getOptions().getLeft().get(cLeftposiion).getContent());
        textView.setLayoutParams(lp);
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
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp.addRule(LEFT_OF, pointview.getId());

        TextView textView = new TextView(context);
        textView.setText(inbasebean.getOptions().getRight().get(cLeftposiion).getContent());
        textView.setLayoutParams(lp);
        return textView;
    }


    /*添加小黑点 右*/
    @SuppressLint("ResourceType")
    public void addPointRight() {
        pointview = new CricleTextView(context);
        LayoutParams lp = new LayoutParams(30, 30);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        pointview.setId(R.string.show_pointright);

        pointview.setLayoutParams(lp);
        this.addView(pointview);


    }

    /*添加小黑点 左*/
    public void addPointLeft() {

        pointview = new CricleTextView(context);
        LayoutParams lp = new LayoutParams(30, 30);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp.addRule(CENTER_VERTICAL, TRUE);
        if (showLeftFlag.equals("IMG")) {
            lp.addRule(RIGHT_OF, imageViewL.getId());
        } else {
            lp.addRule(RIGHT_OF, textViewL.getId());
        }
        pointview.setLayoutParams(lp);
        this.addView(pointview);

    }


    @Override
    public void onClick(View v) {
        this.setBackgroundResource(R.drawable.btnline);
        /*0 未作答  1 已经提交过了*/
        if (inbasebean.getIs_answer().equals("0")) {
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

    public void updateUI(BeOptionViewState state) {
        switch (state) {
            case unselected:
                // 更新未选中的UI样式
                break;
            case selected:
                // 更新选中的UI样式
                break;
            case right:
                // 更新正确的UI样式
                break;
            case wrong:
                // 更新错误的UI样式
                break;

            case aimed:
                // 更新命中的UI样式
                break;
            default:
                break;
        }
    }

}
