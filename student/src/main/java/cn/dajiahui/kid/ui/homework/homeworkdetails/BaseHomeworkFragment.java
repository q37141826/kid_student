package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.fxtx.framework.ui.FxFragment;

import java.io.IOException;

/**
 * Created by lenovo on 2018/1/5.
 */

public abstract class BaseHomeworkFragment extends FxFragment {
    public BaseHomeworkFragment.GetMediaPlayer Mp3;

    public MediaPlayer mediaPlayer;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        bundle = getArguments();
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Mp3 = (GetMediaPlayer) activity;
    }

    @Override
    public abstract void setArguments(Bundle bundle);

    /*公共接口 音乐播放器*/
    public interface GetMediaPlayer {
        public void getMediaPlayer(MediaPlayer mediaPlayer);
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public void onPause() {
        super.onPause();

        mediaPlayer.stop();
    }

    public void playMp3(String mp3path) {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mp3path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Mp3 = (BaseHomeworkFragment.GetMediaPlayer) getActivity();
            Mp3.getMediaPlayer(mediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
