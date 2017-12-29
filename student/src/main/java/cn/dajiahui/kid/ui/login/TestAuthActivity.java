package cn.dajiahui.kid.ui.login;

import android.widget.CompoundButton;
import android.widget.Switch;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.login.bean.BeUserAuth;

/**
 * Created by Mjj on 2016/5/20.
 */
public class TestAuthActivity extends FxActivity {

    private Switch switch1, switch2, switch3, switch4, switch5, switch6, switch7, switch8, switch9, switch10, switch11, switch12, switch13;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_testauth);
        BeUserAuth a = UserController.getInstance().getUserAuth();
        UserController.getInstance().getUserAuth().isLesson = true;
        UserController.getInstance().getUserAuth().isClass = true;
        switch1 = getView(R.id.switch_btn1);
        switch2 = getView(R.id.switch_btn2);
        switch3 = getView(R.id.switch_btn3);
        switch4 = getView(R.id.switch_btn4);
        switch5 = getView(R.id.switch_btn5);
        switch6 = getView(R.id.switch_btn6);
        switch7 = getView(R.id.switch_btn7);
        switch8 = getView(R.id.switch_btn8);
        switch9 = getView(R.id.switch_btn9);
        switch10 = getView(R.id.switch_btn10);
        switch11 = getView(R.id.switch_btn11);
        switch12 = getView(R.id.switch_btn12);
        switch13 = getView(R.id.switch_btn13);

        switch1.setChecked(a.isClass);
        switch2.setChecked(a.isLesson);
        switch3.setChecked(a.isAlbum);
        switch4.setChecked(a.isJob);
        switch5.setChecked(a.isEvaluation);
        switch6.setChecked(a.isStuEval);
        switch7.setChecked(a.isStuVerify);
        switch8.setChecked(a.isMicroClass);
        switch9.setChecked(a.isMsn);
//        switch10.setChecked(a.isNotice);
        switch11.setChecked(a.isErrQue);
        switch12.setChecked(a.isMyFile);
        switch13.setChecked(a.isAttend);

        switch1.setOnCheckedChangeListener(onClick);
        switch2.setOnCheckedChangeListener(onClick);
        switch3.setOnCheckedChangeListener(onClick);
        switch4.setOnCheckedChangeListener(onClick);
        switch5.setOnCheckedChangeListener(onClick);
        switch6.setOnCheckedChangeListener(onClick);
        switch7.setOnCheckedChangeListener(onClick);
        switch8.setOnCheckedChangeListener(onClick);
        switch9.setOnCheckedChangeListener(onClick);
        switch10.setOnCheckedChangeListener(onClick);
        switch11.setOnCheckedChangeListener(onClick);
        switch12.setOnCheckedChangeListener(onClick);
        switch13.setOnCheckedChangeListener(onClick);
    }

    private CompoundButton.OnCheckedChangeListener onClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.switch_btn1) {
                UserController.getInstance().getUserAuth().isClass = isChecked;
            }else if (buttonView.getId() == R.id.switch_btn2) {
                UserController.getInstance().getUserAuth().isLesson = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn3) {
                UserController.getInstance().getUserAuth().isAlbum = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn4) {
                UserController.getInstance().getUserAuth().isJob = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn5) {
                UserController.getInstance().getUserAuth().isEvaluation = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn6) {
                UserController.getInstance().getUserAuth().isStuEval = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn7) {
                UserController.getInstance().getUserAuth().isStuVerify = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn8) {
                UserController.getInstance().getUserAuth().isMicroClass = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn9) {
                UserController.getInstance().getUserAuth().isMsn = isChecked;
            }
//            else if (buttonView.getId() == R.id.switch_btn10) {
//                UserController.getInstance().getUserAuth().isNotice = isChecked;
//            }
            else if (buttonView.getId() == R.id.switch_btn11) {
                UserController.getInstance().getUserAuth().isErrQue = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn12) {
                UserController.getInstance().getUserAuth().isMyFile = isChecked;
            } else if (buttonView.getId() == R.id.switch_btn13) {
                UserController.getInstance().getUserAuth().isAttend = isChecked;
            }
        }
    };
}
