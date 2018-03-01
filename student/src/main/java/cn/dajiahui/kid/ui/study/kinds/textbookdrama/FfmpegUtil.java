package cn.dajiahui.kid.ui.study.kinds.textbookdrama;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.coremedia.iso.boxes.Container;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import cn.dajiahui.kid.util.KidConfig;
import cn.dajiahui.kid.util.Logger;

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

    public FfmpegUtil(Activity context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
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
//                    mergeaudiovideo(mOriginVideo, new File(KidConfig.getInstance().getPathMixAudios() + "mixAutios.mp3"), mOutputFile);
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
        Logger.d("多个音频混音然后合成到视频中 cmd = " + cmd);

        try {
            ffmpeg.execute(cmd.toString().split(" "), new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Logger.d("FFmpeg onStart 混合音频");
                }

                @Override
                public void onProgress(String message) {
                    Logger.d("FFmpeg onProgress--- 混合音频");
                }

                @Override
                public void onFailure(String message) {
                    Logger.d("FFmpeg onFailure 混合音频");
                }

                @Override
                public void onSuccess(String message) {
                    handler.sendEmptyMessage(MESSAGE_MIX_V_T_A);
                    Logger.d("FFmpeg onSuccess 混合音频");
                }

                @Override
                public void onFinish() {
                    Logger.d("FFmpeg onFinish 混合音频 ");
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
     * @param outputFile
     */
    public void audioVideoSyn(File videoFile, File audioFile, File outputFile) {
//        String cmd = "-i /sdcard/test/video001a.mp4 -i /sdcard/test/output.mp3 /sdcard/test/output.mp4";  // 插入音频
        StringBuilder cmd = new StringBuilder();
        cmd.append("-i");
        cmd.append(" ");
        cmd.append(videoFile.getPath());
        cmd.append(" ");
        cmd.append("-i");
        cmd.append(" ");
        cmd.append(audioFile.getPath());
        cmd.append(" ");
        cmd.append(outputFile.getPath());
        Logger.d("音视频合成cmd = " + cmd);
        try {
            ffmpeg.execute(cmd.toString().split(" "), new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Logger.d("FFmpeg onStart  ---- ");
                }

                @Override
                public void onProgress(String message) {
                    Logger.d("FFmpeg onProgress ----   音视频合成");
                }

                @Override
                public void onFailure(String message) {
                    Logger.d("FFmpeg onFailure ----  ");
                }

                @Override
                public void onSuccess(String message) {
                    Logger.d("FFmpeg onSuccess ----  ");
                    handler.sendEmptyMessage(MESSAGE_SYNTHETIC_VIDEO);

                }

                @Override
                public void onFinish() {
                    Logger.d("FFmpeg onFinish ----音视频合成 ");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            Logger.d("FFmpeg exception  ----音视频合成 = " + e);
        }
    }


    // 分离视频  直接分离到课本剧的文件夹下
    public void getNoSoundVideo(String VideoPath,String outPath) {
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
                }

                @Override
                public void onFailure(String message) {

                }

                @Override
                public void onSuccess(String message) {
                    Logger.d("分离视频成功");
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


    public void mergeaudiovideo(File videoFile, File audioFile, File outputFile) {//iscover=false （修改录音时 没有点击完成按钮）

        String video = videoFile.getPath();//视频
        String audio = audioFile.getPath();//音频
        Movie countVideo = null;
        try {
            if (video != "") {
                countVideo = MovieCreator.build(video);
            }
//
        } catch (IOException e) {
            e.printStackTrace();
        }
        Movie countAudioEnglish = null;

        if (audio != "") {

            try {
                countAudioEnglish = MovieCreator.build(audio);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Track audioTrackEnglish = countAudioEnglish.getTracks().get(0);
        countVideo.addTrack(audioTrackEnglish);
        Container out = new DefaultMp4Builder().build(countVideo);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
            out.writeContainer(fos.getChannel());
            fos.close();//关闭资源
            Logger.d("222222222222");
            handler.sendEmptyMessage(MESSAGE_SYNTHETIC_VIDEO);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
