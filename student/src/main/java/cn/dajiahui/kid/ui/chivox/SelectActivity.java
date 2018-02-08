package cn.dajiahui.kid.ui.chivox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.dajiahui.kid.R;

// 驰声测试用
public class SelectActivity extends Activity implements OnClickListener {
    protected String TAG = this.getClass().getName();
    //declare button view here
    private Button btnStartWordSentPred;
    Intent activityIntent = new Intent();
    private boolean isOnline;
    private boolean loadVad;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
		isOnline = getIntent().getBooleanExtra("isOnline", true);
        loadVad = getIntent().getBooleanExtra("isVadLoad", false);
        initView();
    }
    
    private void initView(){
        //regist button view here
        btnStartWordSentPred = (Button)findViewById(R.id.btnStartWordSentPred); 
        btnStartWordSentPred.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //add click listener here
        if(v==btnStartWordSentPred) {
        activityIntent.putExtra("isOnline", isOnline); 
        activityIntent.putExtra("isVadLoad", loadVad); 
        activityIntent.setClass(this, WordSentPred.class);}
        startActivity(activityIntent);
    }
}
