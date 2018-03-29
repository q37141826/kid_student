package cn.dajiahui.kid.ui.study.kinds.practice.view;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.fxtx.framework.util.BaseUtil;

import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.study.kinds.practice.myinterface.ExMoveLocation;

import static cn.dajiahui.kid.ui.homework.homeworkdetails.SortFragment.isLinecheck;

/**
 * Created by lenovo on 2018/1/16.
 */

@SuppressLint("AppCompatCustomView")
public class ExMoveImagview extends RelativeLayout implements View.OnTouchListener {

    private Context context;
    private Activity activity;
    //起始 x y
    private int startX;
    private int startY;
    //图片中心点
    private float centerPointX;
    private float centerPointY;
    private int position;//移动图片的索引值（从0开始）
    private SortQuestionModle inbasebean;//数据模型
    private ExMoveLocation moveLocation;//接口实例
    public String val;//当前拖动图片的val值
    private List<String> mRightContentList;
    public String content;
    private int widthPixels;

    /*构造*/
    public ExMoveImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.activity = (Activity) context;
    }

    /*构造*/
    public ExMoveImagview(Context context, ExMoveLocation moveLocation, int position, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.setOnTouchListener(this);
        this.inbasebean = inbasebean;
        this.moveLocation = moveLocation;
        this.position = position;
        this.val = inbasebean.getOptions().get(position).getVal();
        this.content = inbasebean.getOptions().get(position).getContent();
        widthPixels = BaseUtil.getWidthPixels(activity);
        /*添加视图*/
        addImageView();

    }

    /*正确答案构造（后台返回数据后需重新加载一遍排序的图片）*/
    public ExMoveImagview(Context context, int position, SortQuestionModle inbasebean, List<String> mRightContentList) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.inbasebean = inbasebean;
        this.position = position;
        this.mRightContentList = mRightContentList;
        this.content = inbasebean.getOptions().get(position).getContent();
        widthPixels = BaseUtil.getWidthPixels(activity);
       /*添加视图*/
        addImageView();
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

                /*未check可以拖动*/
                if (inbasebean.isAnswer() == false) {
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
    private void addImageView() {
   /*屏幕宽度*/

        LayoutParams params = new LayoutParams(widthPixels/5, widthPixels/5);
        String content = null;
        /*未回答的时候是false 走解析的图片集合   check后是true 走 自己正确答案的集合*/
        if (inbasebean.isAnswer() == false) {
            content = inbasebean.getOptions().get(position).getContent();
        } else {
            content = mRightContentList.get(position);
        }

        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            if (inbasebean.isAnswer() == false) {
                imageView.setLayoutParams(params);
                this.addView(imageView);
                Glide.with(context).load(inbasebean.getOptions().get(position).getContent())
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

            } else {
                this.removeAllViews();

                this.addView(imageView);
                ImageView imageViewT = new ImageView(context);
                Glide.with(context).load(mRightContentList.get(position)).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

                /*正确答案 添加遮罩*/
                RelativeLayout.LayoutParams paramsT = new RelativeLayout.LayoutParams(widthPixels/5, widthPixels/5);
                paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
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

            if (inbasebean.isAnswer() == false) {
                textView.setText(inbasebean.getOptions().get(position).getContent());
            } else {
                textView.setText(mRightContentList.get(position));
                 /*正确答案 添加遮罩*/
                RelativeLayout relativeLayout = new RelativeLayout(context);
                LayoutParams paramsT = new LayoutParams(widthPixels/5, widthPixels/5);
                paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
                relativeLayout.setBackgroundResource(R.drawable.answer_true_bg);
                relativeLayout.setLayoutParams(paramsT);
                relativeLayout.addView(textView);
                this.addView(relativeLayout);
            }
            this.addView(textView);
        }
    }
}
