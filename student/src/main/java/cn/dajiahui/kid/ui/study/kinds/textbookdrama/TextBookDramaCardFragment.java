package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDramaoptions;
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


    private BeTextBookDramaoptions beTextBookDrama;
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
        beTextBookDrama = (BeTextBookDramaoptions) bundle.get("BeTextBookDramaoptions");
        totalsize = bundle.getString("size");
        tvenglish.setText(beTextBookDrama.getEnglish());
        tvchinese.setText(beTextBookDrama.getChinese());
        tvtotaltime.setText("时间");

        tvcurrentnum.setText("1/" + totalsize);
    }


//    private View.OnClickListener onClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//
//        }
//    };

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
}
