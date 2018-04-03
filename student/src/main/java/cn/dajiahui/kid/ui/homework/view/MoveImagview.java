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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cn.dajiahui.kid.controller.Constant.SortAnswerView_margin;
import static cn.dajiahui.kid.ui.homework.homeworkdetails.DoHomeworkActivity.screenWidth;
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
    private List<String> mRightContentList;//正确答案内容的集合


    /*构造*/
    public MoveImagview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*构造*/
    public MoveImagview(Context context, MoveLocation moveLocation, int position, List<String> mRightContentList, SortQuestionModle inbasebean) {
        super(context);
        this.context = context;

        this.inbasebean = inbasebean;
        this.moveLocation = moveLocation;
        this.position = position;
        this.val = inbasebean.getOptions().get(position).getVal();
        this.mRightContentList = mRightContentList;
        this.setBackgroundResource(R.drawable.sortview_default_bg);
        this.setPadding(SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin, SortAnswerView_margin);



        /*首先判断是否作答*/
        switch (inbasebean.getIs_complete()) {
            /*未开始*/
            case "-1":

                this.setOnTouchListener(this);
                ShowNoCompleteUI(inbasebean.getOptions().get(position).getContent());

                break;
            /*进行中*/
            case "0":

                this.setOnTouchListener(this);
                ShowNoCompleteUI(inbasebean.getOptions().get(position).getContent());

                break;
            /*已完成*/
            case "1":
                ShowCompleteUI(mRightContentList.get(position));
                /*添加遮罩*/
                AddMaskView();
                break;
            default:
                break;

        }

    }

    /*未完成UI*/
    private void ShowNoCompleteUI(String imgUrl) {

        String content = inbasebean.getOptions().get(position).getContent();
        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {
            ShowImageViewUI(imgUrl);
        } else {
            ShowTextViewUI(inbasebean.getOptions().get(position).getContent());
        }
    }

    /*添加遮罩*/
    private void AddMaskView() {
        /*正确答案 添加遮罩*/
        LayoutParams paramsT = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsT.addRule(RelativeLayout.CENTER_IN_PARENT);
        ImageView imageViewT = new ImageView(context);
        imageViewT.setLayoutParams(paramsT);
        imageViewT.setBackgroundResource(R.drawable.answer_true_bg);
        this.addView(imageViewT);
    }

    /*完成UI*/
    private void ShowCompleteUI(String imgUrl) {
        String content = inbasebean.getOptions().get(position).getContent();
        if (content.startsWith("h", 0) && content.startsWith("t", 1)) {

            ShowImageViewUI(imgUrl);

        } else {
            ShowTextViewUI(mRightContentList.get(position));
        }
    }

    /*显示文本*/
    private void ShowTextViewUI(String textcontext) {
        TextView textView = new TextView(context);
        LayoutParams tparams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setTextColor(context.getResources().getColor(R.color.gray_9c9c9c));
        tparams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(tparams);
        if (inbasebean.getIs_answered().equals("0")) {
            textView.setText(textcontext);
        } else {
            textView.setText(textcontext);
        }
        addView(textView);
    }

    /*显示图片*/
    private void ShowImageViewUI(String imgUrl) {
        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(params);
        this.addView(imageView);

        Glide.with(context).load(imgUrl)
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

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


                /*排序题  首先判断是否完成提交*/
                if (!inbasebean.getIs_complete().equals("1")) {
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


}
