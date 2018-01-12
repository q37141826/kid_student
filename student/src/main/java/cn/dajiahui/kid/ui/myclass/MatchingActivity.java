//package cn.dajiahui.kid.ui.myclass;
//
//import android.annotation.SuppressLint;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.fxtx.framework.ui.FxActivity;
//import com.fxtx.framework.util.BaseUtil;
//
//import java.util.ArrayList;
//
//import cn.dajiahui.kid.R;
//import cn.dajiahui.kid.ui.homework.view.MoveImageView;
//import cn.dajiahui.kid.ui.myclass.bean.Matching;
//
///*
//* 排序
//* */
//public class MatchingActivity extends FxActivity {
//
//    private TextView textView1;
//    private TextView textView2;
//    private TextView textView3;
//    ArrayList<Matching> list = new ArrayList<>();
//    private Matching m2;
//    private Matching m1;
//    private Matching m3;
//
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    protected void initView() {
////
////        Log.d("majin", "手机宽度：" + BaseUtil.getPhoneWidth(MatchingActivity.this));
////        Log.d("majin", "手机高度：" + BaseUtil.getPhoneHeight(MatchingActivity.this));
////        Log.d("majin", "density：" + BaseUtil.getScreenDensity(MatchingActivity.this));
////        Log.d("majin", "densityDpi：" + BaseUtil.getScreenDensityDpi(MatchingActivity.this));
////        Log.d("majin", "控件寬100dp =  ：" + BaseUtil.dip2px(MatchingActivity.this, 100) + " px");
//
//
//        setContentView(R.layout.activity_matching);
//        RelativeLayout re_root = getView(R.id.re_root);
//        initialize();
//
////
////        if (BaseUtil.getPhoneWidth(MatchingActivity.this) == 720) {
////            m1 = new Matching(textView1, 0, 90, R.color.blue);
////            m2 = new Matching(textView2, 260, 90, R.color.btn_green_noraml);
////            m3 = new Matching(textView3, 520, 90, R.color.red);
////            Log.d("majin", "畅玩4x");
////
////        } else if (BaseUtil.getPhoneWidth(MatchingActivity.this) == 1080) {
////            m1 = new Matching(textView1, 0, 150, R.color.blue);
////            m2 = new Matching(textView2, 390, 150, R.color.btn_green_noraml);
////            m3 = new Matching(textView3, 780, 150, R.color.red);
////            Log.d("majin", "荣耀8");
////        }
//
//        m1 = new Matching(textView1, BaseUtil.ComputationalDistanceWidth(this, (BaseUtil.dip2px(this, 100)), 1, 0), BaseUtil.ComputationalDistanceHeight(this, BaseUtil.dip2px(this, 50), 1), R.color.blue);
//        m2 = new Matching(textView2, BaseUtil.ComputationalDistanceWidth(this, (BaseUtil.dip2px(this, 100)), 2, 0), BaseUtil.ComputationalDistanceHeight(this, BaseUtil.dip2px(this, 50), 2), R.color.btn_green_noraml);
//        m3 = new Matching(textView3, BaseUtil.ComputationalDistanceWidth(this, (BaseUtil.dip2px(this, 100)), 3, 0), BaseUtil.ComputationalDistanceHeight(this, BaseUtil.dip2px(this, 50), 3), R.color.red);
//        list.add(m1);
//        list.add(m2);
//        list.add(m3);
//
//        for (int i = 0; i < list.size(); i++) {
//            drawTextview(list.get(i), re_root, i);
//        }
//
//
//        BaseUtil.ComputationalDistanceHeight(this, BaseUtil.dip2px(this, 50), 1);
//        BaseUtil.ComputationalDistanceHeight(this, BaseUtil.dip2px(this, 50), 2);
//        BaseUtil.ComputationalDistanceHeight(this, BaseUtil.dip2px(this, 50), 3);
//    }
//
//    /**
//     * 绘制形同可拖动的图形
//     *
//     * @param RelativeLayout re_root 跟视图
//     * @param TextView       textView 要绘制的图形
//     * @param int            num      第几个图形
//     */
//
//    @SuppressLint("NewApi")
//    private void drawTextview(Matching matching, RelativeLayout re_root, int num) {
//
//
//        MoveImageView moveImageView = new MoveImageView(MatchingActivity.this, BaseUtil.getPhoneWidth(MatchingActivity.this), BaseUtil.getPhoneHeight(MatchingActivity.this), matching);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(BaseUtil.dip2px(MatchingActivity.this, 100), BaseUtil.dip2px(MatchingActivity.this, 50));
//
//        layoutParams.leftMargin = matching.getLeftMargin();
//        layoutParams.topMargin = matching.getTopMargin();
//
//        moveImageView.setLayoutParams(layoutParams);
//        moveImageView.setBackgroundResource(matching.getColor());
////        moveImageView.setGravity(CENTER);
////        moveImageView.setText("TV" + num);
//        re_root.addView(moveImageView);
//
//    }
//
//    private void initialize() {
//        textView1 = (TextView) findViewById(R.id.textView1);
//        textView2 = (TextView) findViewById(R.id.textView2);
//        textView3 = (TextView) findViewById(R.id.textView3);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//}
