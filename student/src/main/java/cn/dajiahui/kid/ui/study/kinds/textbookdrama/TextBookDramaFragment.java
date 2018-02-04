package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.bean.BeTextBookDrama;
import cn.dajiahui.kid.ui.video.util.JCVideoPlayerStudent;
import cn.dajiahui.kid.util.DjhJumpUtil;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by mj on 2018/1/31.
 * <p>
 * 课本剧首页Fragment
 * <p>
 * 逻辑：点击视频播放按钮可以播放当前课本剧
 * <p>
 * <p>
 * 点击查看按钮要判断是否有自己的作品  有：播放自己作品  无 ：播放原音作品
 */

public class TextBookDramaFragment extends FxFragment {


    private BeTextBookDrama beTextBookDrama;
    private TextView tvunit;
    private JCVideoPlayerStudent mVideoplayer;
    private RelativeLayout videoplayerroot;
    private Button btn_look;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_text_book_drama, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        beTextBookDrama = (BeTextBookDrama) bundle.get("BeTextBookDrama");
        tvunit.setText(beTextBookDrama.getTitle());


        mVideoplayer.setUp(beTextBookDrama.getVideo_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST);
        mVideoplayer.setOnDuration(new JCVideoPlayerStudent.OnDuration() {
            @Override
            public void onDuration(String duration) {

            }
        });
        mVideoplayer.startVideo();
        mVideoplayer.hideView();
        mVideoplayer.hideBackButton();


    }

    /*初始化*/
    private void initialize() {
        tvunit = getView(R.id.tv_unit);
        mVideoplayer = getView(R.id.videoplayer);
        videoplayerroot = getView(R.id.videoplayerroot);
        btn_look = getView(R.id.btn_look);

        btn_look.setOnClickListener(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("BeTextBookDrama", beTextBookDrama);
            DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MakeTextBookDrmaActivity.class, bundle, 0);

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        JCMediaManager.instance().mediaPlayer.release();
    }
}
