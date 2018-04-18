package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;

import com.fxtx.framework.log.Logger;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import cn.dajiahui.kid.util.KidConfig;

/**
 * Created by majin on 2018/2/2.
 */

public class FfmpegUtil {
    private FFmpeg ffmpeg;
    Activity context;
    private File mOriginVideo;
    private File mOutputFile;
    /**
     * 多个音频混音然后合成到视频中，音频已经混音完成
     */
    private final int MESSAGE_MIX_V_T_A = 0;
    private final int MESSAGE_SYNTHETIC_VIDEO = 1;
    Handler mHandler;
    private String page_id;//缩略图的名字 用page_id与课本剧好匹配

    /*截取缩略图*/
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public FfmpegUtil(Activity context, Handler mHandler, String page_id)

    {
        this.context = context;
        this.mHandler = mHandler;
        this.page_id = page_id;

        if (ffmpeg == null) {
            ffmpeg = FFmpeg.getInstance(context);
            loadFFMpegLib();
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_MIX_V_T_A:
                    audioVideoSyn(mOriginVideo, new File(KidConfig.getInstance().getPathMixAudios() + "mixAutios.mp3"), mOutputFile);
                    break;
                case MESSAGE_SYNTHETIC_VIDEO:
                    mHandler.sendEmptyMessage(3);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 多个音频混音然后合成到视频中
     *
     * @param videoFile  视屏
     * @param audioList  音频列表
     * @param outputFile 输出文件
     */

    public void mixAudiosToVideo(File videoFile, Map<Integer, Map<String, Object>> audioList, File outputFile) {
        mOriginVideo = videoFile;
        mOutputFile = outputFile;
        File tt = (File) audioList.get(0).get("filePathName");
        Logger.d("backgroundSound =" + tt);
        /* 多个音频混音，所有的音频都是往第一个音频中混合 */
        StringBuilder cmd = new StringBuilder();
        cmd.append("-i");
        cmd.append(" ");
        cmd.append(((File) audioList.get(0).get("filePathName")).getPath());

        for (int a = 1; a < audioList.size(); a++) {
            if ((audioList.get(a) != null)) {
                cmd.append(" ");
                cmd.append("-itsoffset");
                cmd.append(" ");
                cmd.append((String) (audioList.get(a)).get("startTime"));
                cmd.append(" ");
                cmd.append("-i");
                cmd.append(" ");
                cmd.append((((File) audioList.get(a).get("filePathName"))).getPath());
            } else {
                break;
            }
        }
        cmd.append(" ");
        cmd.append("-filter_complex amix=inputs=");
        cmd.append(audioList.size());
        cmd.append(":duration=first:dropout_transition=");
        cmd.append(audioList.size());
        cmd.append(" ");
        cmd.append("-async 1");
        cmd.append(" ");
        cmd.append(KidConfig.getInstance().getPathMixAudios() + "mixAutios.mp3");//混音之后的地址
        // 混合音频

//        String cmd = "-i /storage/emulated/0/kid_student/SoundRecording/pd1.mp3 -itsoffset 00:00:04 -i /storage/emulated/0/kid_student/SoundRecording/pd2.mp3 -itsoffset 00:00:14 -i /storage/emulated/0/kid_student/SoundRecording/pd3.mp3 -filter_complex amix=inputs=3:duration=first:dropout_transition=4 -async 1 /storage/emulated/0/kid_student/13051152997/hunyin.mp3";  // 混合音频

//        executeFFMpeg(cmd);
//        Logger.d("多个音频混音然后合成到视频中 cmd = " + cmd);

        try {
            ffmpeg.execute(cmd.toString().split(" "), new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Logger.d("FFmpeg onStart 混合音频");
                }

                @Override
                public void onProgress(String message) {
//                    Logger.d("FFmpeg onProgress--- 混合音频");
                }

                @Override
                public void onFailure(String message) {
                    Logger.d("FFmpeg onFailure 混合音频" + message);
                }

                @Override
                public void onSuccess(String message) {
                    handler.sendEmptyMessage(MESSAGE_MIX_V_T_A);
//                    Logger.d("FFmpeg onSuccess 混合音频");
                }

                @Override
                public void onFinish() {
//                    Logger.d("FFmpeg onFinish 混合音频 ");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            Logger.d("FFmpeg exception = " + e);
        }
    }

    /**
     * 音视频合成
     *
     * @param videoFile
     * @param audioFile
     * @param outputFile -i /sdcard/test/1-1-001.mp4
     *                   -i
     *                   /sdcard/test/output.mp3
     *                   -map 0:0 -map 1:0 -vcodec
     *                   copy
     *                   -acodec
     *                   copy /sdcard/test/out111.mp4
     */
    public void audioVideoSyn(File videoFile, File audioFile, final File outputFile) {
//        String cmd = "-i /sdcard/test/video001a.mp4 -i /sdcard/test/output.mp3 /sdcard/test/output.mp4";  // 插入音频
        StringBuilder cmd = new StringBuilder();
        cmd.append("-i");
        cmd.append(" ");
        cmd.append(videoFile.getPath());/*应该修改成本地原视频的地址*/
        cmd.append(" ");
        cmd.append("-i");
        cmd.append(" ");
        cmd.append(audioFile.getPath());
        cmd.append(" ");
        cmd.append("-map");
        cmd.append(" ");
        cmd.append("0:0");
        cmd.append(" ");
        cmd.append("-map");
        cmd.append(" ");
        cmd.append("1:0");
        cmd.append(" ");
        cmd.append("-vcodec");
        cmd.append(" ");
        cmd.append("copy");
        cmd.append(" ");
        cmd.append("-acodec");
        cmd.append(" ");
        cmd.append("copy");
        cmd.append(" ");
        cmd.append(outputFile.getPath());
        Logger.d("音视频合成cmd = " + cmd);
        try {
            ffmpeg.execute(cmd.toString().split(" "), new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Logger.d("FFmpeg onStart 音视频合成  ---- ");
                }

                @Override
                public void onProgress(String message) {
//                    Logger.d("FFmpeg onProgress ----   音视频合成");
                }

                @Override
                public void onFailure(String message) {
                    Logger.d("FFmpeg onFailure ---- 音视频合成 " + message);
                }

                @Override
                public void onSuccess(String message) {
                    Logger.d("FFmpeg onSuccess ---- 音视频合成音视频合成 ");

                    /*majin fix 2018.4.18 修改获取视频缩略图方式*/
                    obtainingThumbnails(outputFile.getPath(), KidConfig.getInstance().getPathMineWorksThumbnail() + outputFile.getName().substring(0, outputFile.getName().lastIndexOf(".")) + ".png");

                }

                @Override
                public void onFinish() {
//                    Logger.d("FFmpeg onFinish ----音视频合成 ");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
//            Logger.d("FFmpeg exception  ----音视频合成 = " + e);
        }
    }


    /*获取缩略图*/
    private void obtainingThumbnails(String inputFileName, String outFileName) {
//ffmpeg -y -ss 2 -t 5 -i inputFileName -vf "select=eq(pict_type\\,I)" -vframes 1 -f image2 -s 250x140 outuptFileName

        StringBuilder cmd = new StringBuilder();
        cmd.append("-y");
        cmd.append(" ");
        cmd.append("-ss");
        cmd.append(" ");
        cmd.append("2");
        cmd.append(" ");
        cmd.append("-t");
        cmd.append(" ");
        cmd.append("5");
        cmd.append(" ");
        cmd.append("-i");
        cmd.append(" ");
        cmd.append(inputFileName);
        cmd.append(" ");
        cmd.append("-vf");
        cmd.append(" ");
        cmd.append("select=eq(pict_type\\,I)");
        cmd.append(" ");
        cmd.append("-vframes");
        cmd.append(" ");
        cmd.append("1");
        cmd.append(" ");
        cmd.append("-f");
        cmd.append(" ");
        cmd.append("image2");
        cmd.append(" ");
        cmd.append("-s");
        cmd.append(" ");
        cmd.append("250x140");
        cmd.append(" ");
        cmd.append(outFileName);
//        Logger.d("获取缩略图：" + cmd);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd.toString().split(" "), new ExecuteBinaryResponseHandler() {


                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
//                    Logger.d("获取缩略图");
                }

                @Override
                public void onFailure(String message) {
                    Logger.d("获取缩略图onFailure " + message);
                }

                @Override
                public void onSuccess(String message) {
                    Logger.d("获取缩略图成功");
                    handler.sendEmptyMessage(MESSAGE_SYNTHETIC_VIDEO);
                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {

        }

    }


    private void loadFFMpegLib() {

        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Logger.d("loadFFMpegLib onStart");

                }

                @Override
                public void onFailure() {
                    Logger.d("loadFFMpegLib onFailure");

                }

                @Override
                public void onSuccess() {


//                    Logger.d("loadFFMpegLib onSuccess");
                }

                @Override
                public void onFinish() {
//                    Logger.d("loadFFMpegLib onFinish");

                }
            });
        } catch (FFmpegNotSupportedException e) {
            Logger.d("loadFFMpegLib e = " + e);

        }
    }



    /**
     * 放大缩小图片
     *
     * @param bitmap 源Bitmap
     * @param w      宽
     * @param h      高
     * @return 目标Bitmap
     */
    private Bitmap zoom(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }


    public static Bitmap getVideoBitmap(String path) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            Bitmap frameAtTime = retriever.getFrameAtTime();
            return frameAtTime;
        } catch (Exception e) {
            return null;
        } finally {
            retriever.release();
        }

    }
}
