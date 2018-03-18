package com.fxtx.framework.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fxtx.framework.R;
import com.fxtx.framework.ui.FxFragment;


/**
 * @author Administrator
 */

public class WelcomeFr extends FxFragment {
    public ImageView rootview;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_welcome, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootview = ((ImageView) rootView);
        ((ImageView) rootView).setImageResource(bundle.getInt("id"));
    }
}

