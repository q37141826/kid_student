package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import java.util.Timer;
import java.util.TimerTask;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaPageDataItem;
import cn.dajiahui.kid.ui.video.util.JCVideoPlayerStudent;
import cn.dajiahui.kid.util.Logger;

/**
 * Created by mj on 2018/1/31.
 * <p>
 * 课本剧中可滑动的卡片
 * <p>
 * <p>
 * 打分 之后显示小星星（隐藏进度条和 时间）待续完成
 */

public class TextBookDramaCardFragment extends FxFragment implements MakeTextBookDrmaActivity.RefreshWidget {


    private BeTextBookDramaPageDataItem beTextBookDramaPageDataItem;
    private TextView tvunit;
    private JCVideoPlayerStudent mVideoplayer;
    private RelativeLayout videoplayerroot;
    private Button btn_look;
    private String totalsize;
    private TextView tvcurrentnum;
    private TextView tvenglish;
    private TextView tvchinese;
    private cn.dajiahui.kid.ui.homework.view.ProhibitMoveSeekbar recordseek;
    private TextView tvtotaltime;
    private RatingBar ratingBar;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_text_book_drama_card, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        beTextBookDramaPageDataItem = (BeTextBookDramaPageDataItem) bundle.get("BeTextBookDramaPageDataItem");
        totalsize = bundle.getString("size");
        tvenglish.setText(beTextBookDramaPageDataItem.getEnglish());
        tvchinese.setText(beTextBookDramaPageDataItem.getChinese());
        tvtotaltime.setText("时间");

        tvcurrentnum.setText("1/" + totalsize);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
//        RefreshWidget refreshWidget = (RefreshWidget) activity;
    }

    @Override
    public void refresgWidget(int position) {
        tvcurrentnum.setText(position + "/" + totalsize);
        Logger.d("------------position：" + position);
    }

    private void initialize() {
        tvcurrentnum = getView(R.id.tv_currentnum);
        tvenglish = getView(R.id.tv_english);
        tvchinese = getView(R.id.tv_chinese);
        recordseek = getView(R.id.record_seek);
        tvtotaltime = getView(R.id.tv_totaltime);
        ratingBar = getView(R.id.rb_score);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int arg1 = msg.arg1;
                recordseek.setProgress(allSecond);
                if (allSecond == arg1) {
                    allSecond = 0;
                    mProssTimer.cancel();
                    mProssTimer = null;
                }
            }

        }
    };

    Timer mProssTimer;
    int allSecond = -1;

    /*刷新进度条*/
    public void refreshProgress(final int Second) {

        tvtotaltime.setText(Second + "s");
        recordseek.setMax(Second);

        if (mProssTimer == null) {
            mProssTimer = new Timer();
            mProssTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    allSecond += 1;
                    msg.arg1 = Second;
                    msg.what = 1;
                    mHandler.sendMessage(msg); // 发送消息
                }
            }, 0, 1000);
        }
    }

}
