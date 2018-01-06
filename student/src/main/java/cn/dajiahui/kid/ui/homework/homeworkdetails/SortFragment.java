package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;


/**
 * 排序
 */


public class SortFragment extends FxFragment {

    private int path;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {

        bundle = getArguments();
//        path = (int) bundle.get("path");
        return inflater.inflate(R.layout.fr_sort, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvtest = getView(R.id.test);
//        tvtest.setText(path + "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        System.gc();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.d("majin", " ReadFragment onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d("majin", " ReadFragment onPause");
    }


}

