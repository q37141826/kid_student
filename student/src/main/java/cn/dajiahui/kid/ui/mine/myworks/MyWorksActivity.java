package cn.dajiahui.kid.ui.mine.myworks;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.adapter.ApMyWorks;
import cn.dajiahui.kid.ui.mine.bean.BeMyWorks;


/*
* 我的作品
* */
public class MyWorksActivity extends FxActivity {

    private TextView tvbookaudio;
    private TextView tvcaraok;
    private Button btndelete;
    private ListView mListview;
    private boolean isshowcheckbox = false;//false 不显示 true 显示
    private ApMyWorks apMyWorks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.myworks);
        onBackText();
        onRightBtn(R.drawable.ico_share, R.string.edit);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_works);
        initialize();
        final List<BeMyWorks> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new BeMyWorks("作品" + i, "2018.0" + i));

        }
        apMyWorks = new ApMyWorks(context, data);

        mListview.setAdapter(apMyWorks);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的position传递到adapter里面去
                apMyWorks.changeState(position);
                Toast.makeText(context, "当前点击" + data.get(position).getWorksname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        tvbookaudio = getView(R.id.tv_bookaudio);
        tvcaraok = getView(R.id.tv_caraok);
        btndelete = getView(R.id.btn_delete);
        mListview = getView(R.id.listview);
        btndelete.setOnClickListener(onCick);
    }

    @Override
    public void onRightBtnClick(View view) {
        isshowcheckbox = !isshowcheckbox;

        if (!isshowcheckbox) {
            btndelete.setVisibility(View.GONE);
            apMyWorks.changeState(-2);
        } else {
            btndelete.setVisibility(View.VISIBLE);
            apMyWorks.changeState(-1);
        }

    }


    private View.OnClickListener onCick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btn_delete:


                    break;
                default:
                    break;

            }
        }
    };
}
