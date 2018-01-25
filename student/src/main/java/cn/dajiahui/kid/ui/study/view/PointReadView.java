package cn.dajiahui.kid.ui.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BePlayReadingBook;

/**
 * Created by lenovo on 2018/1/23.
 * <p>
 * 点读的view
 */

public class PointReadView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private int position;
    private BePlayReadingBook bePlayReadingBook;
    private GetPointReadView pointReadView;


    public BePlayReadingBook getBePlayReadingBook() {
        return bePlayReadingBook;
    }

    public PointReadView(Context context, GetPointReadView pointReadView, int position, BePlayReadingBook bePlayReadingBook) {
        super(context);
        this.context = context;
        this.position = position;
        this.bePlayReadingBook = bePlayReadingBook;
        this.pointReadView = pointReadView;
        this.setBackgroundResource(R.drawable.select_readingbook_bg_red);
        this.setOnClickListener(this);

    }


    public PointReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public void onClick(View v) {
        pointReadView.getPointReadView((PointReadView) v, bePlayReadingBook);
    }


    public interface GetPointReadView {
        public void getPointReadView(PointReadView pointReadView, BePlayReadingBook bePlayReadingBook);
    }
}
