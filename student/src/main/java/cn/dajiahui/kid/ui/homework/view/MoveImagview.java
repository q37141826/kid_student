package cn.dajiahui.kid.ui.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.MoveLocation;

import static cn.dajiahui.kid.ui.homework.homeworkdetails.SortFragment.isLinecheck;

/**
 * Created by lenovo on 2018/1/16.
 */

@SuppressLint("AppCompatCustomView")
public class MoveImagview extends RelativeLayout implements View.OnTouchListener {

    private Context context;
    //起始 x y
    private int startX;
    private int startY;
    //图片中心点
    private float centerPointX;
    private float centerPointY;
    private int position;//移动图片的索引值（从0开始）
    private SortQuestionModle inbasebean;//数据模型
    private MoveLocation moveLocation;//接口实例
    public String val;//当前拖动图片的val值
    private List<String> mRightContentList;

    /*构造*/
    public MoveImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*构造*/
    public MoveImagview(Context context, MoveLocation moveLocation, int position, List<String> mRightContentList, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.setOnTouchListener(this);
        this.inbasebean = inbasebean;
        this.moveLocation = moveLocation;
        this.position = position;
        this.val = inbasebean.getOptions().get(position).getVal();
        if (inbasebean.getIs_answer().equals("1")) {
            this.mRightContentList = mRightContentList;
        }
        addview();

    }

    /*正确答案构造（后台返回数据后需重新加载一遍排序的图片）*/
    public MoveImagview(Context context, int position, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.inbasebean = inbasebean;
        this.position = position;
        /*添加图片*/
        addRightview();
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 初始化起点坐标
                startX = (int) motionEvent.getRawX();
                startY = (int) motionEvent.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:


                if (inbasebean.getIs_answer().equals("0")) {
                    int endX = (int) motionEvent.getRawX();
                    int endY = (int) motionEvent.getRawY();

                    // 计算移动偏移量
                    int dx = endX - startX;
                    int dy = endY - startY;

                    // 更新左上右下距离
                    int l = this.getLeft() + dx;
                    int r = this.getRight() + dx;
                    int t = this.getTop() + dy;
                    int b = this.getBottom() + dy;
                    /*如果是练习模式，已作答就锁定移动的view位置*/
                    if (isLinecheck == false) {
                        /*移动刷新位置*/
                        this.layout(l, t, r, b);
                    }


                    // 重新初始化起点坐标
                    startX = (int) motionEvent.getRawX();
                    startY = (int) motionEvent.getRawY();

                    /*获取拖动的中心点*/
                    centerPointX = (((float) 1 / (float) 2) * this.getWidth()) + l;
                    centerPointY = (((float) 1 / (float) 2) * getHeight()) + t;

                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                /* 通知碎片  然后回调 手指抬起时回调当前拖动的view的中心点  */
                BeLocation beLocation = moveLocation.submitCenterPoint(this, position, centerPointX, centerPointY);
                if (beLocation != null) {
                /*更新滑动之后的位置*/
                    this.layout(beLocation.getGetLeft(), beLocation.getGetTop(), beLocation.getGetRight(), beLocation.getGetBottom());
                }

                break;
            default:
                break;
        }
        return true;

    }

    /*刷新起始位置*/
    public void refreshLocation(BeLocation beLocation) {
        if (beLocation != null) {
            this.layout(beLocation.getGetLeft(), beLocation.getGetTop(), beLocation.getGetRight(), beLocation.getGetBottom());
        }
    }

    /*添加视图*/
    private void addview() {

        LayoutParams params = new LayoutParams(150, 150);
        String content = inbasebean.getOptions().get(position).getContent();

        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            ImageView imageView = new ImageView(context);

            imageView.setLayoutParams(params);
            this.addView(imageView);
            if (inbasebean.getIs_answer().equals("0")) {
                Glide.with(context)
                        .load(inbasebean.getOptions().get(position).getContent())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(mRightContentList.get(position))
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

                /*正确答案 添加遮罩*/
                RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(150, 150);
                paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView imageViewT = new ImageView(context);
                imageViewT.setLayoutParams(paramsT);
                imageViewT.setBackgroundResource(R.drawable.answer_true_bg);
                this.addView(imageViewT);
            }

        } else {

            TextView textView = new TextView(context);
            LayoutParams tparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setTextColor(getResources().getColor(R.color.blue));
            params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
            textView.setLayoutParams(tparams);
            if (inbasebean.getIs_answer().equals("0")) {
                textView.setText(inbasebean.getOptions().get(position).getContent());
            } else {
                textView.setText(mRightContentList.get(position));
            }
            addView(textView);
        }


    }

    /*添加视图*/
    private void addRightview() {
        String content = inbasebean.getOptions().get(position).getContent();
        LayoutParams params = new LayoutParams(150, 150);

        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            ImageView imageView = new ImageView(context);

            imageView.setLayoutParams(params);
            this.addView(imageView);
            Glide.with(context)
                    .load(inbasebean.getOptions().get(position).getContent())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            TextView textView = new TextView(context);
            LayoutParams tparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setTextColor(getResources().getColor(R.color.blue));
            params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
            textView.setLayoutParams(tparams);
            textView.setText("第" + (position + 1) + "个");
            addView(textView);
        }

    }

    /*显示我的答案*/
    public void showAnswer(BeLocation beLocation) {

        if (beLocation != null) {
            this.layout(beLocation.getGetLeft(), beLocation.getGetTop(), beLocation.getGetRight(), beLocation.getGetBottom());
            this.setFocusable(false);
            this.setClickable(false);
        }
    }


}
