package cn.dajiahui.kid.util;

/**
 * Created by lenovo on 2018/2/1.
 */


import android.media.MediaRecorder;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 录音工具
 */
public class RecorderUtil {

    private String mFileName = null;
    private MediaRecorder mRecorder = null;
    private long startTime;
    //    private long timeInterval;
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
//        timeInterval = System.currentTimeMillis() - startTime;
        try {
//            if (timeInterval > 1000) {
            mRecorder.stop();
//            }
            mRecorder.release();
            mRecorder = null;
            isRecording = false;
        } catch (Exception e) {

        }

        return mFileName;
    }

    public String stop(){

        mRecorder.stop();
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


//    /**
//     * 获取录音时长,单位秒
//     */
//    public long getTimeInterval() {
//        return timeInterval / 1000;
//    }


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


    /**
     * 拼接多段Mp4音频
     */
    public void appendAutio(List<String> mRecordingList) {

        // 把选中的item的path存到一个List里
        // List<String> phthList = new ArrayList<String>();

        Logger.d("mRecordingList:" + mRecordingList.size());

        try {
            List<Movie> inMovies = new ArrayList<Movie>();
            for (int i = 0; i < mRecordingList.size(); i++) {
                inMovies.add(MovieCreator.build(mRecordingList.get(i)));
            }

            List<Track> audioTracks = new LinkedList<Track>();
            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                }
            }

            Movie result = new Movie();

            if (!audioTracks.isEmpty()) {
                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
            }

            Container con = new DefaultMp4Builder().build(result);
            @SuppressWarnings("resource")
            /*拼接音频输出地址*/
                    FileChannel fc = new RandomAccessFile(KidConfig.getInstance().getPathRecordingAudio() + "splicedaudio.mp4", "rw").getChannel();
            con.writeContainer(fc);
            fc.close();
            mRecordingList.clear();
            inMovies.clear();
            audioTracks.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}