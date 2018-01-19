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

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.BeLocation;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.MoveLocation;

/**
 * Created by lenovo on 2018/1/16.
 */

@SuppressLint("AppCompatCustomView")
public class MoveImagview extends RelativeLayout implements View.OnTouchListener {
    private int startY;
    private int startX;
    private float pointX;
    private float pointY;
    private Context context;
    private MoveLocation moveLocation;

    private SortQuestionModle inbasebean;
    private int position;

    public MoveImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImagview(Context context, MoveLocation moveLocation, int position, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;
        this.setOnTouchListener(this);
        this.inbasebean = inbasebean;
        /*接口实例*/
        this.moveLocation = moveLocation;

        this.position = position;

        /*添加图片*/
        addview();

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


                    this.layout(l, t, r, b);

                    // 更新界面
                    // 重新初始化起点坐标
                    startX = (int) motionEvent.getRawX();
                    startY = (int) motionEvent.getRawY();

                /*获取拖动的中心点*/
                    pointX = (((float) 1 / (float) 2) * this.getWidth()) + l;
                    pointY = (((float) 1 / (float) 2) * getHeight()) + t;

                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                /*手指抬起时回调当前拖动的view的中心点*/
                BeLocation beLocation = moveLocation.submitCenterPoint(this, pointX, pointY);
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
        if(beLocation!=null){
        this.layout(beLocation.getGetLeft(), beLocation.getGetTop(), beLocation.getGetRight(), beLocation.getGetBottom());
    }}

    /*添加视图*/
    private void addview() {
        ImageView imageView = new ImageView(context);
//        imageView.setImageResource(R.drawable.ic_launcher);
        LayoutParams params = new LayoutParams(150, 150);
        imageView.setLayoutParams(params);
        this.addView(imageView);
        Glide.with(context)
                .load(inbasebean.getOptions().get(position).getContent())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        TextView textView = new TextView(context);
        LayoutParams tparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setTextColor(getResources().getColor(R.color.blue));
        params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        textView.setLayoutParams(tparams);
        textView.setText("第" + (position + 1) + "个");
        addView(textView);
    }

    /*锁定移动图片*/
    public void lockMoveImage(SortQuestionModle inbasebean) {
        this.inbasebean = inbasebean;
    }
}
