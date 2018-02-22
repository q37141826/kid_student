package cn.dajiahui.kid.ui.mine.myworks;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.ui.base.FxTabActivity;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.myinterface.ShowbtnDelete;


/*
* 我的作品
* */
public class MyWorksActivity extends FxTabActivity {

    private TextView tvbookaudio, tvcaraok;

    private View lin_bookaudio, lin_caraok;
    private FrTextBookAudio frTextBookAudio;//课本剧
    private boolean isShowcheckboxTextbook = false;//mfalse 不显示 mtrue 显示
    private boolean isShowcheckboxaraok = false;//mfalse 不显示 mtrue 显示
    private FrCaraOK frCaraOK;//卡拉OK
    private ShowbtnDelete showbtnDeleteTextbook;
    private ShowbtnDelete showbtnDeleteCaraok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.myworks);
        onBackText();
        onRightBtn(R.drawable.ico_share, R.string.edit);
        /*初始化碎片*/
        initFragment(savedInstanceState);
    }

    @Override
    protected FxFragment initIndexFragment() {
        if (frTextBookAudio == null) {
            frTextBookAudio = new FrTextBookAudio();
            showbtnDeleteTextbook = frTextBookAudio;
        }
        return frTextBookAudio;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_works);
        initialize();


//        for (int i = 0; i < 20; i++) {
//            data.add(new BeMyWorks("作品" + i, "2018.0" + i));
//
//        }
//        apMyWorks = new ApMyWorks(context, data);
//
//        mListview.setAdapter(apMyWorks);
//        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //把点击的position传递到adapter里面去
//                apMyWorks.changeState(position);
//                Toast.makeText(context, "当前点击" + data.get(position).getWorksname(), Toast.LENGTH_SHORT).show();
//                DjhJumpUtil.getInstance().startVideoActivity(MyWorksActivity.this, null);
//
//            }
//        });
    }

    private void initialize() {
        tvbookaudio = getView(R.id.tv_bookaudio);
        tvcaraok = getView(R.id.tv_caraok);
        lin_bookaudio = getView(R.id.lin_bookaudio);
        lin_caraok = getView(R.id.lin_caraok);
        tvbookaudio.setOnClickListener(onCick);
        tvcaraok.setOnClickListener(onCick);

    }

    @Override
    public void onRightBtnClick(View view) {

        if (isFragment == showbtnDeleteTextbook) {
            isShowcheckboxTextbook = !isShowcheckboxTextbook;
            if (isShowcheckboxTextbook) {
                showbtnDeleteTextbook.showbtnDelete(1);
            } else {
                showbtnDeleteTextbook.showbtnDelete(2);
            }
        } else {
            isShowcheckboxaraok = !isShowcheckboxaraok;
            if (isShowcheckboxaraok) {
                showbtnDeleteCaraok.showbtnDelete(1);
            } else {
                showbtnDeleteCaraok.showbtnDelete(2);
            }
        }

    }

    private View.OnClickListener onCick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.tv_bookaudio:

                    tvbookaudio.setTextColor(getResources().getColor(R.color.yellow_FEBF12));
                    tvcaraok.setTextColor(getResources().getColor(R.color.black));
                    lin_bookaudio.setBackgroundColor(getResources().getColor(R.color.yellow_FEBF12));
                    lin_caraok.setBackgroundColor(getResources().getColor(R.color.gray_E9E9E9));

                    if (frTextBookAudio == null) {
                        frTextBookAudio = new FrTextBookAudio();
                        showbtnDeleteTextbook = frTextBookAudio;
                    }
                    switchContent(isFragment, frTextBookAudio);

                    Toast.makeText(context, "课本剧作品", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_caraok:
                    tvbookaudio.setTextColor(getResources().getColor(R.color.black));
                    tvcaraok.setTextColor(getResources().getColor(R.color.yellow_FEBF12));
                    lin_caraok.setBackgroundColor(getResources().getColor(R.color.yellow_FEBF12));
                    lin_bookaudio.setBackgroundColor(getResources().getColor(R.color.gray_E9E9E9));

                    switchContent(isFragment, frTextBookAudio);
                    if (frCaraOK == null) {
                        frCaraOK = new FrCaraOK();
                        showbtnDeleteCaraok = (ShowbtnDelete) frCaraOK;
                    }
                    switchContent(isFragment, frCaraOK);

                    Toast.makeText(context, "klaok作品", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
