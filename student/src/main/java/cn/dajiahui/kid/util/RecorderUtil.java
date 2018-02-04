package cn.dajiahui.kid.util;

/**
 * Created by lenovo on 2018/2/1.
 */


import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 录音工具
 */
public class RecorderUtil {

    private String mFileName = null;
    private MediaRecorder mRecorder = null;
    private long startTime;
    private long timeInterval;
    private boolean isRecording;

    public RecorderUtil() {
        mFileName = KidConfig.getInstance().getPathRecordingAudio();
    }

    /**
     * 开始录音
     */
    public void startRecording(String mp3Name) {
        if (mFileName == null) return;
        if (isRecording) {
            mRecorder.release();
            mRecorder = null;
        }
        String s = mp3Name + ".mp3";
        mFileName += s;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        startTime = System.currentTimeMillis();
        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
        } catch (Exception e) {

        }

    }


    /**
     * 停止录音
     */
    public String stopRecording() {
        if (mFileName == null) return "";
        timeInterval = System.currentTimeMillis() - startTime;
        try {
            if (timeInterval > 1000) {
                mRecorder.stop();
            }
            mRecorder.release();
            mRecorder = null;
            isRecording = false;
        } catch (Exception e) {

        }

        return mFileName;
    }

    /**
     * 取消录音
     */
    public synchronized void cancelRecording() {

        if (mRecorder != null) {
            try {
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(mFileName);
            file.deleteOnExit();
        }

        isRecording = false;
    }

    /**
     * 获取录音文件
     */
    public byte[] getDate() {
        if (mFileName == null) return null;
        try {
            return readFile(new File(mFileName));
        } catch (IOException e) {

            return null;
        }
    }

    /**
     * 获取录音文件地址
     */
    public String getFilePath() {
        return mFileName;
    }


    /**
     * 获取录音时长,单位秒
     */
    public long getTimeInterval() {
        return timeInterval / 1000;
    }


    /**
     * 将文件转化为byte[]
     *
     * @param file 输入文件
     */
    private static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    /*文件名置空*/
    public void cleanFileName() {
        mFileName = KidConfig.getInstance().getPathRecordingAudio();
    }


}