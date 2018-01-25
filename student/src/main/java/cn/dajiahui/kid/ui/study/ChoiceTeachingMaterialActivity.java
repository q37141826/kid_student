package cn.dajiahui.kid.ui.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.adapter.ApTeachingMaterial;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterial;
import cn.dajiahui.kid.util.DjhJumpUtil;

import static cn.dajiahui.kid.controller.Constant.CHOICETEACHINGMATERIAL;
/*
* 教材选择
* */
public class ChoiceTeachingMaterialActivity extends FxActivity {

    private TextView mTextMessage;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle(R.string.choicebook);
        onBackText();


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choice_teaching_material);

        mListView = getView(R.id.listview);


        final List<BeChoiceTeachingMaterial> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            list.add(new BeChoiceTeachingMaterial("Move up " + i, "共计" + i + "本"));
        }
        mListView = getView(R.id.listview);
        ApTeachingMaterial apTeachingMaterial = new ApTeachingMaterial(ChoiceTeachingMaterialActivity.this, list);

        mListView.setAdapter(apTeachingMaterial);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putString("unit", list.get(position).getTeachingMaterialName());

                DjhJumpUtil.getInstance().startBaseActivity(ChoiceTeachingMaterialActivity.this, ChoiceTeachingMaterialInfoActivity.class, b, CHOICETEACHINGMATERIAL);

                Toast.makeText(context, "选择教材", Toast.LENGTH_SHORT).show();

                finishActivity();

            }


        });
    }

}
