package cn.dajiahui.kid.ui.mine.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.adapter.ApNotice;
import cn.dajiahui.kid.ui.mine.bean.BeNotice;


/*
* 通知
* */
public class NoticeActivity extends FxActivity {

    private ListView mListview;
    private List<BeNotice> list;
    private ApNotice apNotice;
    private TextView mTvnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_notice);
        onBackText();
        onRightBtn(R.drawable.ico_share, R.string.mine_clear);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice);
        mListview = getView(R.id.listview);
        mTvnull = getView(R.id.tv_null);
        list = new ArrayList();

        for (int i = 0; i < 20; i++) {

            list.add(new BeNotice("更新内容：" + i, "更新时间201" + i));

        }


        apNotice = new ApNotice(this, list);

        mListview.setAdapter(apNotice);

    }

    @Override
    public void onRightBtnClick(View view) {
        list.clear();

        apNotice.notifyDataSetChanged();
        if (list.size() == 0) {
            mTvnull.setText(R.string.e_class_null);
            mTvnull.setVisibility(View.VISIBLE);
            mListview.setEmptyView(mTvnull);
        }

        Toast.makeText(context, "清空通知", Toast.LENGTH_SHORT).show();
    }

}
