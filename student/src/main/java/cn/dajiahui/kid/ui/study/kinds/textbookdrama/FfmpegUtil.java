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

                    retriever.setDataSource(outputFile.getPath());
                    String fileLength = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    // *获取视频某一帧
                    Bitmap frameAtTime = retriever.getFrameAtTime(Long.parseLong(fileLength) * 5, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    saveThumbnailToSDCard(frameAtTime, outputFile.getName().substring(0, outputFile.getName().lastIndexOf(".")));

                    handler.sendEmptyMessage(MESSAGE_SYNTHETIC_VIDEO);

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

//
//    /**
//     * 音视频合成
//     *
//     * @param videoFile
//     * @param audioFile
//     * @param outputFile
//     */
//    public void audioVideoSyn(File videoFile, File audioFile, final File outputFile) {
////        String cmd = "-i /sdcard/test/video001a.mp4 -i /sdcard/test/output.mp3 /sdcard/test/output.mp4";  // 插入音频
//        StringBuilder cmd = new StringBuilder();
//        cmd.append("-i");
//        cmd.append(" ");
//        cmd.append(videoFile.getPath());
//        cmd.append(" ");
//        cmd.append("-i");
//        cmd.append(" ");
//        cmd.append(audioFile.getPath());
//        cmd.append(" ");
//        cmd.append(outputFile.getPath());
//        Logger.d("音视频合成cmd = " + cmd);
//        try {
//            ffmpeg.execute(cmd.toString().split(" "), new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    Logger.d("FFmpeg onStart  ---- ");
//                }
//
//                @Override
//                public void onProgress(String message) {
////                    Logger.d("FFmpeg onProgress ----   音视频合成");
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    Logger.d("FFmpeg onFailure ----  ");
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    Logger.d("FFmpeg onSuccess ----  ");
//
//                    retriever.setDataSource(outputFile.getPath());
//                    String fileLength = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    // *获取视频某一帧
//                    Bitmap frameAtTime = retriever.getFrameAtTime(Long.parseLong(fileLength) * 5, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//                    String name = outputFile.getName();
//                    Logger.d("outputFile.getName():" + outputFile.getName());
//
//                    saveThumbnailToSDCard(frameAtTime, outputFile.getName().substring(0,outputFile.getName().lastIndexOf(".")));
//
//                    handler.sendEmptyMessage(MESSAGE_SYNTHETIC_VIDEO);
//
//                }
//
//                @Override
//                public void onFinish() {
////                    Logger.d("FFmpeg onFinish ----音视频合成 ");
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
////            Logger.d("FFmpeg exception  ----音视频合成 = " + e);
//        }
//    }


    // 分离视频  直接分离到课本剧的文件夹下
    public void getNoSoundVideo(String VideoPath, String outPath) {
        String cmd;
        //-i input_file -vcodec copy -an output_file_video     //分离视频流 MlecConfig.getInstance().getPathSeparateVideoAudio() + "/out_video.mp4"
        cmd = "-i " + VideoPath + " -vcodec copy -an " + outPath + "out_nosound_video.mp4";
        separateVideo(cmd);

    }

    /*分离视频-获取无声视频*/
    private void separateVideo(String str) {

        String cmd[] = str.split(" ");
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {


                @Override
                public void onStart() {
                }

                @Override
                public void onProgress(String message) {
                    Logger.d("分离视频");
                }

                @Override
                public void onFailure(String message) {

                }

                @Override
                public void onSuccess(String message) {
                    Logger.d("分离视频成功");
                    Message msg = Message.obtain();
                    msg.what = 4;
                    mHandler.sendMessage(msg);

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

    /*保存缩略图到sd卡*/
    private void saveThumbnailToSDCard(Bitmap mBitmap, String bitName) {

        File f = new File(KidConfig.getInstance().getPathMineWorksThumbnail() + bitName + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap zoom = zoom(mBitmap, 300, 200);
        if (zoom != null) {
            zoom.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
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
}
