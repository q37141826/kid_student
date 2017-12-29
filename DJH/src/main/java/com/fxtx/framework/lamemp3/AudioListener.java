package com.fxtx.framework.lamemp3;

public interface AudioListener {
    /**
     * 录制结束ֹ
     */
    void onRecorderStop();

    /**
     * 录制开始
     */
    void onRecorderStart();

    /**
     * 播放开始
     */
    void onPlayStart();

    /**
     * 播放开始
     */
    void paushAudio();

    /**
     * 播放结束
     */
    void onPlayStop();

    /**
     * 时间秒
     */
    void onDuration(int duration);

    /**
     * 错误
     *
     * @param error
     */
    void onError(String error);

}
