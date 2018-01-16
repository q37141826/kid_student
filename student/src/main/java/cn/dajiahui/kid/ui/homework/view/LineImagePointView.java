package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    private final ImageView imageView;
    private CricleTextView pointview;
    private int cLeftposiion = 0;
    private LineQuestionModle inbasebean;
    private Dir direction;
    public String value;
    public List<BeLineLeft> lefts = new ArrayList<>();
    public List<BeLineRight> rights = new ArrayList<>();


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
    public LineImagePointView(Context context, Sublineinfo sublineinfo, int pic, int cLeftposiion, LineQuestionModle inbasebean, Dir direction) {
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

        imageView = new ImageView(context);

        imageView.setImageResource(pic);
        imageView.setId(R.string.show_pointleft);
        if (direction == Dir.left) {//左边

            Glide.with(context)
                    .load(inbasebean.getOptions().getRight().get(cLeftposiion).getContent())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            this.value = inbasebean.getOptions().getLeft().get(cLeftposiion).getVal();

            addPointLeft();//添加左边小黑点
            imageView.setLayoutParams(lp);

            this.addView(imageView);


        } else {//右边
            Glide.with(context)
                    .load(inbasebean.getOptions().getRight().get(cLeftposiion).getContent())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            this.value = inbasebean.getOptions().getRight().get(cLeftposiion).getVal();
            lp.leftMargin = 30;
            addPointRight();//添加右边小黑点
            imageView.setLayoutParams(lp);
            this.addView(imageView);
        }
        this.selected(false);

    }

    /*添加小黑点 右*/
    public void addPointRight() {
        pointview = new CricleTextView(context);
        LayoutParams lp = new LayoutParams(30, 30);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        pointview.setLayoutParams(lp);
        this.addView(pointview);


    }

    /*添加小黑点 左*/
    public void addPointLeft() {

        pointview = new CricleTextView(context);
        LayoutParams lp = new LayoutParams(30, 30);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lp.addRule(CENTER_VERTICAL, TRUE);
        lp.addRule(RIGHT_OF, imageView.getId());
        pointview.setLayoutParams(lp);
        this.addView(pointview);

    }


    @Override
    public void onClick(View v) {
        this.setBackgroundResource(R.drawable.btnline);
        sublineinfo.submitlininfo(this);
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
