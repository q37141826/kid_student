package com.fxtx.framework.lamemp3;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Message;

import com.fxtx.framework.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;

/**
 * MP3录制
 *
 * @author Administrator
 */
public class Mp3Recorder {
    static {
        System.loadLibrary("mp3lame");
    }

    private static final int DEFAULT_SAMPLING_RATE = 22050;
    private static final int FRAME_COUNT = 160;
    /* Encoded bit rate. MP3 file will be encoded with bit rate 32kbps */
    private static final int BIT_RATE = 32;
    private AudioRecord audioRecord = null;
    private int bufferSize;
    private File mp3File;
    private RingBuffer ringBuffer;
    private byte[] buffer;
    private FileOutputStream os = null;
    private DataEncodeThread encodeThread;
    private int samplingRate;
    private int channelConfig;
    private PCMFormat audioFormat;
    private boolean isRecording = false;
    private int maxTime = 60; // 默认录音最大时间 单位秒
    private AudioListener listener;
    private double maxAmplitude;
    private int runnTime = 0; // 录制长度
    private Timer durationTimer; // 计时器
    private Timer maxTimer;

    // 语音合成对象
    private Context context;

    public Mp3Recorder(Context context, int samplingRate, int channelConfig,
                       PCMFormat audioFormat, AudioListener listener) {
        this.samplingRate = samplingRate;
        this.channelConfig = channelConfig;
        this.audioFormat = audioFormat;
        this.listener = listener;
    }

    //

    /**
     * Default constructor. Setup recorder with default sampling rate 1 channel,
     * 16 bits pcmdouble
     */
    public Mp3Recorder(Context context, AudioListener listener) {
        this(context, DEFAULT_SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,
                PCMFormat.PCM_16BIT, listener);
    }

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     *
     * @throws IOException
     */
    public void startRecording() {
        if (isRecording)
            return;
        if (audioRecord == null) {
            try {
                initAudioRecorder();
            } catch (IOException e) {
                listener.onError("录音就绪失败");
            }
        }
        audioRecord.startRecording();
        runnTime = -1;
        startTimer();
        listener.onRecorderStart();
        new Thread() {

            @Override
            public void run() {
                isRecording = true;
                while (isRecording) {
                    int bytes = audioRecord.read(buffer, 0, bufferSize);
                    if (bytes > 0) {
                        ringBuffer.write(buffer, bytes);
                    }
                    int v = 0;
                    for (int i = 0; i < bufferSize; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    maxAmplitude = v / (float) bytes;

                }
                try {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                    // stop the encoding thread and try to wait
                    // until the thread finishes its job
                    Message msg = Message.obtain(encodeThread.getHandler(),
                            DataEncodeThread.PROCESS_STOP);
                    msg.sendToTarget();
                    encodeThread.join();

                    // 停止计时
                    listener.onRecorderStop();
                    stopTimer();
                } catch (InterruptedException e) {
                    listener.onError("录音过程中错误");
                } finally {
                    if (os != null) {
                        try {
                            os.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }.start();
    }

    /**
     * @throws IOException
     */
    public void stopRecording() {
        isRecording = false;
    }

    /**
     * 返回当前录制状态， true在录制，false没有录制
     *
     * @return
     */
    public boolean getRecording() {
        return isRecording;
    }

    /**
     * 设置最大时长
     */
    public void setMaxTime(int time) {
        this.maxTime = time;
    }

    /**
     * 启动计时
     */
    private void startTimer() {
        maxTime = maxTime <= 0 ? 60 : maxTime;
        maxTimer = new Timer();
        maxTimer.schedule(new TimerStop(maxTimer), maxTime * 1000);
        // 记录录制时间
        durationTimer = new Timer();
        durationTimer.schedule(new TimerRunning(), 0, 1000);
    }

    private void stopTimer() {
        durationTimer.cancel();
        maxTimer.cancel();
    }

    class TimerRunning extends java.util.TimerTask {

        @Override
        public void run() {
            runnTime++;// 一秒加一
            listener.onDuration(runnTime); // 接口回调
        }
    }

    /**
     * 计时处理
     *
     * @author Administrator
     */
    class TimerStop extends java.util.TimerTask {
        Timer timer = null;

        public TimerStop(Timer timer) {
            this.timer = timer;
        }

        public void run() {
            timer.cancel();// 到时间之后 停止录制
            Mp3Recorder.this.stopRecording();
        }
    }

    /**
     * Initialize audio recorder
     */
    private void initAudioRecorder() throws IOException {
        int bytesPerFrame = audioFormat.getBytesPerFrame();
                /*
                 * Get number of samples. Calculate the buffer size (round up to the
		 * factor of given frame size)
		 */
        int frameSize = AudioRecord.getMinBufferSize(samplingRate,
                channelConfig, audioFormat.getAudioFormat()) / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize = frameSize + (FRAME_COUNT - frameSize % FRAME_COUNT);
        }

        bufferSize = frameSize * bytesPerFrame;

		/* Setup audio recorder */
        new MediaRecorder();

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                samplingRate, channelConfig, audioFormat.getAudioFormat(),
                bufferSize);

        // Setup RingBuffer. Currently is 10 times size of hardware buffer
        // Initialize buffer to hold data
        ringBuffer = new RingBuffer(10 * bufferSize);
        buffer = new byte[bufferSize];

        // Initialize lame buffer
        // mp3 sampling rate is the same as the recorded pcm sampling rate
        // The bit rate is 32kbps
        SimpleLame.init(samplingRate, 1, samplingRate, BIT_RATE);

        // Initialize the place to put mp3 file
        File directory = new File(new FileUtil().dirFile(context) +
                File.separator + "ata" +
                File.separator + "recorder" +
                File.separator);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        mp3File = new File(directory, System.currentTimeMillis() + ".mp3");
        os = new FileOutputStream(mp3File);
        // Create and run thread used to encode data
        // The thread will
        encodeThread = new DataEncodeThread(ringBuffer, os, bufferSize);
        encodeThread.start();
        audioRecord.setRecordPositionUpdateListener(encodeThread,
                encodeThread.getHandler());
        audioRecord.setPositionNotificationPeriod(FRAME_COUNT);
    }

    //获取录制的视频对象
    public File getFile() {
        return mp3File;
    }

    public double getMaxAmplitude() {
        return maxAmplitude;
    }

    public int getMaxDuration() {
        return runnTime;
    }
}