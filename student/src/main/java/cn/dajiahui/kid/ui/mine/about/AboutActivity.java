package cn.dajiahui.kid.ui.mine.about;

import android.os.Bundle;

import com.fxtx.framework.ui.FxActivity;

import cn.dajiahui.kid.R;


/**
 * 关于魔耳
 */
public class AboutActivity extends FxActivity {

//    protected TextView tvNull;
//    protected ListView listView;
//    protected List<BeHelp> helpList = new ArrayList<BeHelp>();
//    protected ApHelp adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about);
//        tvNull = getView(R.id.tv_null);
//        listView = getView(R.id.help_listview);
//        listView.setEmptyView(tvNull);
//        adapter = new ApHelp(context, helpList);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(itemListener);
//        httpData();
    }

//    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String isTarget = helpList.get(position).getIsTarget();
//            BeHelp help = helpList.get(position);
//            if (StringUtil.sameStr("1", isTarget)) {
//                // 跳转WebView
//                DjhJumpUtil.getInstance().startWebActivity(context, help.getCmsName(), help.getCmsUrl(), false);
//            } else {
//                // 进入下一级界面
//                Bundle bundle = new Bundle();
//                bundle.putString(Constant.bundle_title, help.getCmsName());
//                bundle.putSerializable(Constant.bundle_obj, help.getAllList());
//                DjhJumpUtil.getInstance().startBaseActivity(AboutActivity.this, AllFounctionActivity.class, bundle, 0);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setfxTtitle("关于摩尔");
        onBackText();
    }

    @Override
    protected void dismissfxDialog(int flag) {
        super.dismissfxDialog(flag);
//        tvNull.setText(R.string.not_data);
    }

//    @Override
//    public void httpData() {
//        showfxDialog();
//        RequestUtill.getInstance().httpHelp(context, "1", new ResultCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//                dismissfxDialog();
//                ToastUtil.showToast(context, ErrorCode.error(e));
//            }
//
//            @Override
//            public void onResponse(String response) {
//                dismissfxDialog();
//                HeadJson json = new HeadJson(response);
//                if (json.getstatus() == 0) {
//                    List<BeHelp> temp = json.parsingListArray(new GsonType<List<BeHelp>>() {
//                    });
//                    if (temp != null && temp.size() > 0) {
//                        helpList.clear();
//                        helpList.addAll(temp);
//                    }
//                    adapter.notifyDataSetChanged();
//                } else {
//                    ToastUtil.showToast(context, json.getMsg());
//                }
//            }
//        });
//    }
}
