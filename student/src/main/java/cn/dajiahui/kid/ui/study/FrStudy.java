package cn.dajiahui.kid.ui.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxFragment;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.adapter.ApChooseUtils;
import cn.dajiahui.kid.ui.study.bean.BeStudy;
import cn.dajiahui.kid.ui.study.bean.ChooseUtils;
import cn.dajiahui.kid.util.DjhJumpUtil;
import cn.dajiahui.kid.util.Logger;

import static cn.dajiahui.kid.controller.Constant.GOCHOICETEACHINGMATERIAL;

/**
 * 学习
 */
public class FrStudy extends FxFragment implements ChoiceTeachingMaterialInfoActivity.Assignment {

    private ViewPager pager;
    private ImageView imgsupplementary;
    private TextView tvtitle;
    private TextView tvunit;
    private TextView tvchoiceMaterial;
    private ListView mListview;
    private LinearLayout tabfragment;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_study, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
          /*模拟数据*/

        final List<ChooseUtils> list = new ArrayList<>();

        for (int a = 0; a < 20; a++) {
            list.add(new ChooseUtils("", "Until+" + a + "frist to schoo", (a + 20) + ""));
        }

        final ApChooseUtils apChooseUtils = new ApChooseUtils(getActivity(), list);
        mListview.setAdapter(apChooseUtils);

        //选择单元学习
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "选择学习模式", Toast.LENGTH_SHORT).show();
                DjhJumpUtil.getInstance().startBaseActivity(getActivity(), StudyDetailsActivity.class);
            }
        });

    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_choiceMaterial:
                    Toast.makeText(activity, "选择教材", Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();

                    DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), ChoiceTeachingMaterialActivity.class, b, GOCHOICETEACHINGMATERIAL);


                    break;
                case R.id.im_user:


                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


    @Override
    public void onResume() {
        super.onResume();

        Logger.d("FrStudy-------------------------onResume()");

    }

    /*初始化*/
    private void initialize() {
        imgsupplementary = getView(R.id.img_supplementary);
        tvtitle = getView(R.id.tv_title);
        tvunit = getView(R.id.tv_unit);
        tvchoiceMaterial = getView(R.id.tv_choiceMaterial);
        mListview = getView(R.id.listview);
        tabfragment = getView(R.id.tab_fragment);

        tvchoiceMaterial.setOnClickListener(onClick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void assignment(BeStudy beStudy) {

//      imgsupplementary.setImageResource();
        tvtitle.setText(beStudy.getTv_title());
        tvunit.setText(beStudy.getTv_unit());
    }
}
