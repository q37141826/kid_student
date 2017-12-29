package cn.dajiahui.kid.ui.homework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;

/**
 *
 */

public class FrTaskTab extends FxFragment {

    private String Stv;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        bundle = getArguments();
        Stv = (String) bundle.get("TV");
        return inflater.inflate(R.layout.fr_task_tab, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv = getView(R.id.tvtasktab);
        tv.setText(Stv);

    }
}
