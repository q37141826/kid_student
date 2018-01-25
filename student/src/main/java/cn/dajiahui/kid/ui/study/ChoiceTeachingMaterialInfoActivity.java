package cn.dajiahui.kid.ui.study;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fxtx.framework.ui.FxActivity;

import java.util.ArrayList;
import java.util.List;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.study.adapter.ApTeachingMaterialIfo;
import cn.dajiahui.kid.ui.study.bean.BeChoiceTeachingMaterialInfo;
import cn.dajiahui.kid.ui.study.bean.BeStudy;

/*
* 选择教材详情
* 选择教材二级界面
* */
public class ChoiceTeachingMaterialInfoActivity extends FxActivity {
    private ListView mListView;
    private BeChoiceTeachingMaterialInfo beChoiceTeachingMaterialInfo;
    private Bundle myBundelForGetName;
    public Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从Intent 中获取数据
        myBundelForGetName = this.getIntent().getExtras();

        setfxTtitle(myBundelForGetName.getString("unit"));
        onBackText();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choice_teaching_material_info);
//        assignment = (Assignment) ChoiceTeachingMaterialInfoActivity.this;
        mListView = getView(R.id.listview);


        final List<BeChoiceTeachingMaterialInfo> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            /*模拟数据源*/
            beChoiceTeachingMaterialInfo = new BeChoiceTeachingMaterialInfo("", "move up " + (i + 1));
            list.add(beChoiceTeachingMaterialInfo);
        }
        mListView = getView(R.id.listview);
        ApTeachingMaterialIfo apTeachingMaterial = new ApTeachingMaterialIfo(ChoiceTeachingMaterialInfoActivity.this, list);

        mListView.setAdapter(apTeachingMaterial);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//
                BeStudy beStudy = new BeStudy("", list.get(position).getTeachingMaterialInfoName(), myBundelForGetName.getString("unit"));
//                assignment.assignment(beStudy);

                finishActivity();
                Toast.makeText(context, "选择教材详情", Toast.LENGTH_SHORT).show();

            }

        });
    }

    /*给左自学左上角属性赋值*/
    public interface Assignment {
        public void assignment(BeStudy beStudy);
    }
}

