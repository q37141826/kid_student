package cn.dajiahui.kid.ui.homework.homeworkdetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxtx.framework.json.HeadJson;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.ui.homework.bean.LineQuestionModle;
import cn.dajiahui.kid.ui.homework.myinterface.CheckHomework;


/**
 * 连线题
 */


public class LineFragment extends BaseHomeworkFragment implements CheckHomework {

    private int path;
    private LineQuestionModle inbasebean;
    private SubmitLineFragment submit;

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_line, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvtest = getView(R.id.test);
//        tvtest.setText(path + "");
    }

    @Override
    public void setArguments(Bundle bundle) {
        //        /*OK 解析连线*/
        String zz = "{    \"data\": {        \"book_id\": 8,        \"id\": 4,        \"media\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108qbkaj98s.mp3\",        \"options\": {            \"left\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109y5aih34p.png\",                    \"label\": \"头部label\",                    \"type\": \"1\",                    \"val\": \"1\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109sge2pcdz.png\",                    \"label\": \"颈部label\",                    \"type\": \"1\",                    \"val\": \"2\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099mnygtvk.png\",                    \"label\": \"胸部label\",                    \"type\": \"1\",                    \"val\": \"3\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01099p4ryhvt.png\",                    \"label\": \"尾部label\",                    \"type\": \"1\",                    \"val\": \"4\"                }            ],            \"right\": [                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/01098rwcvhz7.png\",                    \"label\": \"head label\",                    \"type\": \"1\",                    \"val\": \"5\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109xvkfimpt.png\",                    \"label\": \"neck label\",                    \"type\": \"1\",                    \"val\": \"6\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109fxrt53uw.png\",                    \"label\": \"chest label\",                    \"type\": \"1\",                    \"val\": \"7\"                },                {                    \"content\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0109vp24bntc.png\",                    \"label\": \"foot label\",                    \"type\": \"1\",                    \"val\": \"8\"                }            ]        },        \"org_id\": 100,        \"question_cate_id\": 4,        \"question_stem\": \"http://d-static.oss-cn-qingdao.aliyuncs.com/elearning/2018/0108bg2e8n6j.jpg\",        \"school_id\": 3,        \"standard_answer\": \"{3:5,2:6,1:7,4:8}\",        \"title\": \"连线题的示例\",        \"unit_id\": 7    },    \"msg\": \"成功\",    \"status\": 0}";
        HeadJson h2 = new HeadJson(zz);
        LineQuestionModle d2 = h2.parsingObject(LineQuestionModle.class);




       inbasebean = (LineQuestionModle) bundle.get("LineQuestionModle");

    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        submit = (SubmitLineFragment) activity;

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


    @Override
    public void submitHomework(Object questionModle) {

    }

    public interface SubmitLineFragment {
        public void submitLineFragment(LineQuestionModle questionModle);
    }
}

