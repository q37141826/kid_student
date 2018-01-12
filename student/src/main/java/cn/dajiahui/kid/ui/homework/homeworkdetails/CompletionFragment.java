package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.CompletionQuestionModle;


/**
 * 填空题
 */
public class CompletionFragment extends BaseHomeworkFragment {

    private int path;
    private CompletionQuestionModle inbasebean;
    private SubmitCompletionFragment submit;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {


        return inflater.inflate(R.layout.fr_completion, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvtest = getView(R.id.test);
       tvtest.setText(inbasebean.getNomber());
    }

    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (CompletionQuestionModle) bundle.get("CompletionQuestionModle");

    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitCompletionFragment) activity;

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

    public interface SubmitCompletionFragment {
        public void submitCompletionFragment(CompletionQuestionModle questionModle);
    }
}

