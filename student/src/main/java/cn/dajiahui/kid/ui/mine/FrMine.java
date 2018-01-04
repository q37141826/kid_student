package cn.dajiahui.kid.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxtx.framework.image.util.GlideUtil;
import com.fxtx.framework.text.StringUtil;
import com.fxtx.framework.ui.FxFragment;

import cn.dajiahui.kid.R;
import cn.dajiahui.kid.controller.UserController;
import cn.dajiahui.kid.ui.login.bean.BeUser;
import cn.dajiahui.kid.ui.mine.myclass.MyClassActivity;
import cn.dajiahui.kid.ui.mine.myworks.MyWorksActivity;
import cn.dajiahui.kid.ui.mine.personalinformation.UserDetailsActivity;
import cn.dajiahui.kid.util.DjhJumpUtil;

/**
 * 我的
 */
public class FrMine extends FxFragment {
    private ImageView imSet;
    public ImageView imUser;

    public static final int PICFPRRESULT = 9;


    private TextView tv_userName; // 用户名

    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_mine, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imUser = getView(R.id.iv_user);
        imUser.setOnClickListener(onClick);
        imSet = getView(R.id.iv_edit);
        imSet.setOnClickListener(onClick);

        tv_userName = getView(R.id.tv_user_name);

        TextView tv_myclass = getView(R.id.tvMyclass);
        tv_myclass.setOnClickListener(onClick);
        TextView tv_myworks = getView(R.id.tvMyworks);
        tv_myworks.setOnClickListener(onClick);
        TextView tv_wrong_book = getView(R.id.tvAbout);
        tv_wrong_book.setOnClickListener(onClick);
        TextView tv_notice = getView(R.id.tvNotice);
        tv_notice.setOnClickListener(onClick);
        TextView tv_set_up = getView(R.id.tvSet_up);
        tv_set_up.setOnClickListener(onClick);


    }

    private void initData() {
        BeUser user = UserController.getInstance().getUser();
        boolean isSex = StringUtil.isSex(user.getSex());
        if (isSex) {
            tv_userName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ico_boy, 0);
        } else {
            tv_userName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ico_girl, 0);
        }
        tv_userName.setText("张三");
        GlideUtil.showRoundImage(getContext(), UserController.getInstance().getUser().getAvator(), imUser, R.drawable.ico_default_user, true);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_user:    //用户头像

//                    DjhJumpUtil.getInstance().startSelectPhotoActivity(getActivity(), Constant.Alum_phone_UserIcon);
                    break;
                case R.id.iv_edit:

                    DjhJumpUtil.getInstance().startBaseActivityForResult(getActivity(), UserDetailsActivity.class, null, PICFPRRESULT);

                    break;
                case R.id.tvMyclass://我的班级
                    Toast.makeText(activity, "我的班级", Toast.LENGTH_SHORT).show();

                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(), MyClassActivity.class);
                    break;
                case R.id.tvMyworks: //我的作品
                    Toast.makeText(activity, "我的作品", Toast.LENGTH_SHORT).show();
                    DjhJumpUtil.getInstance().startBaseActivity(getActivity(),  MyWorksActivity.class);
                    break;
                case R.id.tvAbout: //关于
                    Toast.makeText(activity, "关于", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.tvNotice: //通知
                    Toast.makeText(activity, "通知", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tvSet_up: //设置

                    DjhJumpUtil.getInstance().startBaseActivity(getContext(), SettingActivity.class);
                    break;


                default:
                    break;
            }
        }
    };


}
