package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.SortQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;
import cn.dajiahui.kid.ui.homework.view.MoveImageView;


/**
 * 排序题
 */


public class SortFragment extends BaseHomeworkFragment implements CheckHomework {

    private SortQuestionModle inbasebean;
    private TextView tvsort;
    private TextView tv_1;
    private LinearLayout lin_right;
    private LinearLayout rela_left;
    List<Integer> list = new ArrayList<>();
    private SubmitSortFragment submit;


    @Override

    protected View initinitLayout(LayoutInflater inflater) {


        return inflater.inflate(R.layout.fr_sort, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
//        tv_1.setText(inbasebean.getId() + "");
//        inbasebean.getOptions().size()
        //inbasebean.getOptions().size()
        addGroupImage(6);
        addGroupImageMove(6);


    }

    //size:代码中获取到的图片数量   右边
    @SuppressLint("ResourceType")
    private void addGroupImage(int size) {
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);//设置图片宽高
            layoutParams.topMargin = 30;
            layoutParams.gravity = Gravity.CENTER;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.ic_launcher); //图片资源
            imageView.setBackgroundResource(R.color.btn_green_noraml);

            lin_right.addView(imageView); //动态添加图片
        }
    }   //size:代码中获取到的图片数量

    @SuppressLint("ResourceType")
    private void addGroupImageMove(int size) {
        for (int i = 0; i < size; i++) {
            MoveImageView imageView = new MoveImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);//设置图片宽高
            layoutParams.topMargin = 30;
            layoutParams.leftMargin = 50;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.ic_launcher); //图片资源
            imageView.setBackgroundResource(R.color.yellow);

            rela_left.addView(imageView); //动态添加图片
        }
    }


    @Override
    public void setArguments(Bundle bundle) {
        inbasebean = (SortQuestionModle) bundle.get("SortQuestionModle");


//        String content = inbasebean.getOptions().get(0).getContent();

//        inbasebean.toString();
//        Logger.d("majin", "content++++++++++" +  inbasebean.toString());
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitSortFragment) activity;

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

    /*初始化*/
    private void initialize() {
//        tvsort = getView(R.id.sort);
//        tv_1 = getView(R.id.tv_1);
        rela_left = getView(R.id.lin_left);
        lin_right = getView(R.id.lin_right);

    }


    @Override
    public void submitHomework(Object questionModle) {

    }

    public interface SubmitSortFragment {
        public void submitSoreFragment(SortQuestionModle questionModle);
    }
}

