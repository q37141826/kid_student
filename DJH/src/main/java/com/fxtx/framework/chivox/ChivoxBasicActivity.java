package com.fxtx.framework.chivox;

import android.os.Bundle;
import android.util.Log;

import com.chivox.AIConfig;
import com.chivox.core.CoreService;
import com.chivox.core.CoreType;
import com.chivox.core.Engine;
import com.chivox.cube.NativeResource;
import com.chivox.cube.output.RecordFile;
import com.chivox.cube.param.CoreCreateParam;
import com.chivox.cube.util.FileHelper;
import com.fxtx.framework.chivox.config.Config;
import com.fxtx.framework.ui.FxActivity;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by wangzhi on 2018/2/8.
 */

public abstract class ChivoxBasicActivity extends FxActivity {
    protected String TAG = this.getClass().getName();
    protected boolean isOnline = false;
    protected boolean isRecording = false;
    protected boolean isReplaying = false;
    protected boolean isVadLoad = true;
    protected CoreType coretype;
    protected CoreService service = CoreService.getInstance();
    protected Engine engine;
    protected RecordFile lastRecordFile;
    protected String apiLog="";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOnline = getIntent().getBooleanExtra("isOnline", true);
        isVadLoad = getIntent().getBooleanExtra("isVadLoad", false);
        Log.d("Extra", "isOnline :" + isOnline);
        Log.d("Extra", "isVadLoad :" + isVadLoad);
        Log.d(TAG,"SDK VERSION: "+service.getSdkVersion());
    }

    protected abstract void initView();
    protected abstract void setCoreType();
    protected abstract void setRefText();
    protected abstract void record();
    protected abstract void recordStop();

    protected void initAIEngine(){
        initConfig();
        CoreCreateParam coreCreateParam = null;
        int connectTimeout = 20;
        int serverTimeout = 60;
        //boolean isVadLoaded = isVadLoad;
        Log.d("initAIEngine","isVadLoad: "+isVadLoad);
        //coreCreateParam = new CoreCreateParam(Config.serverUrl, connectTimeout, serverTimeout, isVadLoad);
        if (isOnline) {
            coreCreateParam = new CoreCreateParam(Config.serverUrl, connectTimeout, serverTimeout, isVadLoad);
            //set connect and server time out
            coreCreateParam.setCloudConnectTimeout(20);
            coreCreateParam.setCloudServerTimeout(60);

        } else {
            String resDir = FileHelper.getFilesDir(this).getAbsolutePath();
            Log.d(TAG, "resDir:" + resDir);
            List<NativeResource> nativeResources = new ArrayList<NativeResource>();

            nativeResources.add(new NativeResource(CoreType.en_sent_score));
            nativeResources.add(new NativeResource(CoreType.en_word_score));
            nativeResources.add(new NativeResource(CoreType.en_pred_score));
            coreCreateParam = new CoreCreateParam(nativeResources, isVadLoad);
        }
        try {
            Log.d(TAG, "new cfgText:" + coreCreateParam.getCoreCreateParams());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//            Log.d("BaseActivityMain","coreCreateParam: "+coreCreateParam.getCoreCreateParams());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        coreCreateParam.setVadSpeechLowSeek(500);
        initCore(coreCreateParam);
    }

    protected abstract void initCore(CoreCreateParam coreCreateParam);

    protected void initConfig() {
        AIConfig config = AIConfig.getInstance();
        config.setAppKey(Config.appKey);
        config.setSecretKey(Config.secertKey);
        //userID可以传任意值
        config.setUserId(Config.userId);

        //config.setDebugEnable(true);
        //config.setLogPath(FileHelper.getFilesDir(getBaseContext()).getAbsolutePath() + "/Log.txt");

        //可以传绝对路径，建议放在asset文件夹下
        config.setProvisionFile(FileHelper.getFilesDir(getBaseContext()).getAbsolutePath()+"/" + Config.provisionFilename);
        //config.setVadRes(FileHelper.getFilesDir(this).getAbsolutePath() + "/vad/bin/vad.0.10.20131216/vad.0.10.20131216.bin");
        //config.setVadRes(FileHelper.getFilesDir(this).getAbsolutePath() + "/vad/bin/vad.0.12.20160802/vad.0.12.20160802.bin");
        config.setVadRes(FileHelper.getFilesDir(this).getAbsolutePath() + "/vad/bin/vad.0.9/vad.0.9.bin");
        //可以传绝对路径，如果路径不存在会自动创建文件夹
        config.setRecordFilePath(FileHelper.getFilesDir(getBaseContext()).getAbsolutePath() + "/Records");
        config.setResdirectory(FileHelper.getFilesDir(getBaseContext()).getAbsolutePath()+"/Resources");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != engine) {
            Log.e(TAG, "engine destory->" + engine.getPointer());
            engine.destory();
        }
    }

}
