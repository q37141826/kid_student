package cn.dajiahui.kid.ui.mine;

import android.os.Bundle;
import java.util.ArrayList;
import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.Constant;
import cn.dajiahui.kid.ui.mine.bean.BeHelp;

/**
 * Created by z on 2016/2/25.
 */
public class AllFounctionActivity extends HelpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Constant.bundle_title);
        setfxTtitle(title);
        onBackText();
    }

    @Override
    public void httpData() {
        ArrayList<BeHelp> BeHelp = (ArrayList) getIntent().getSerializableExtra(Constant.bundle_obj);
        if (BeHelp != null)
            helpList.addAll(BeHelp);
        adapter.notifyDataSetChanged();
        tvNull.setText(R.string.not_data);
    }
}
