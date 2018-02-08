package cn.dajiahui.kid.ui.chivox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chivox.cube.util.FileHelper;
import com.fxtx.framework.chivox.config.Config;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.dajiahui.kid.R;

// 驰声测试用
public class ChivoxMainActivity extends Activity implements OnClickListener {
    protected String TAG = this.getClass().getName();
	Intent activityIntent = new Intent();
	
    //declare button view here
    private Button btnStartCloud;
    

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chivox_main_activity);
        initView();
        loadAllResOncce();
    }
    
    private void initView(){
        //regist button view here
        btnStartCloud = (Button)findViewById(R.id.btnStartCloud);
        btnStartCloud.setOnClickListener(this);
    }

    private void loadAllResOncce(){
        loadProvisionFile();
        unZipNativeRes();
    }


    private void loadProvisionFile(){
        File provisionFile = FileHelper.extractProvisionOnce(ChivoxMainActivity.this, Config.provisionFilename);
        Log.d("loadProvisionFile :", "provisionFile :"+provisionFile.getAbsolutePath());
    }

    private void unZipNativeRes(){

        Log.d(TAG, "unzip start");
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("unZipNativeRes...");
        pDialog.show();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run()
            {
				File vadFile = FileHelper.extractResourceOnce(ChivoxMainActivity.this, "vad.zip");
                Log.d("vadFile :", "vadFile :"+vadFile.getAbsolutePath());
                //native resource unzip process
                pDialog.dismiss();
            }
        }, true);
        Log.d(TAG, "unzip ended");
    }



    @Override
    public void onClick(View v) {
        //add click listener here
        if(v==btnStartCloud) {
        activityIntent.putExtra("isOnline", true);
        activityIntent.putExtra("isVadLoad", true);
        activityIntent.setClass(this, SelectActivity.class);}
        startActivity(activityIntent);
    }
}
