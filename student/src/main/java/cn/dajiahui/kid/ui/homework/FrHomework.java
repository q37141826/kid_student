package cn.dajiahui.kid.ui.homework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxtx.framework.ui.FxFragment;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.adapter.ApHomework;
import cn.dajiahui.kid.ui.homework.bean.BeHomework;
import cn.dajiahui.kid.ui.homework.homeworkdetails.HomedetailsActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;

/**
 * 作业
 */
public class FrHomework extends FxFragment {


    private ListView mListview;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_homework, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = getView(R.id.tool_title);
        mListview = getView(R.id.mlistview);
        title.setText(R.string.tab_homework);


        List<BeHomework> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            data.add(new BeHomework(
                    "2017.01.0" + i,
                    "2018.0" + i,
                    "二年" + i + "班",
                    "0" + i,
                    "教材：" + i,
                    "unit:" + i,
                    "aa" + i
            ));
        }
        ApHomework apHomework = new ApHomework(getActivity(), data);
        mListview.setAdapter(apHomework);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), HomedetailsActivity.class);
            }
        });
        Logger.d("majin", "11111111" + "");
    }


    @Override
    public void onResume() {
        super.onResume();
    }


}
