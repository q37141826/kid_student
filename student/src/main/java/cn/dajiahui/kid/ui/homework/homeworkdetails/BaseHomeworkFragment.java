package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.fxtx.framework.ui.FxFragment;

/**
 * Created by lenovo on 2018/1/5.
 */

public abstract class BaseHomeworkFragment extends FxFragment {

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        bundle = getArguments();
        return null;
    }

    @Override
    public abstract void setArguments(Bundle bundle);
}
