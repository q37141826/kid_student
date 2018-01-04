package cn.dajiahui.kid.ui.mine.myclass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.mine.adapter.ApClassSpace;
import cn.dajiahui.kid.ui.mine.bean.BeClassSpace;


/*
* 班级空间
* */
public class ClassSpaceActivity extends FxActivity {
    private ListView mListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.mine_class_space);
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_class_space);
        mListview = getView(R.id.listview);

        List<BeClassSpace> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new BeClassSpace("二年" + i + "班", "201" + i, "真好" + i));

        }
        ApClassSpace apClassSpace = new ApClassSpace(this, list);

        mListview.setAdapter(apClassSpace);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
