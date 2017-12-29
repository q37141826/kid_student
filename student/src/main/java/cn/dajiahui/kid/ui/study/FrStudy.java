package cn.dajiahui.kid.ui.study;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fxtx.framework.file.FileUtil;
import com.fxtx.framework.log.ToastUtil;
import com.fxtx.framework.ui.FxFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.dajiahui.kid.R;

/**
 * 学习
 */
public class FrStudy extends FxFragment {

    private ViewPager pager;


    @Override
    protected View initinitLayout(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fr_study, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = getView(R.id.tool_title);
        title.setText(R.string.tab_study);


        pager = getView(R.id.read_pager);
        List<String> imagePathFromSD = new FileUtil().getImagePathFromSD("/storage/emulated/0/diandu/");
        if (imagePathFromSD.size() > 0) {
            FrStudy.ReadAdapter adapter = new FrStudy.ReadAdapter(
                    getActivity().getSupportFragmentManager(), imagePathFromSD);

            pager.setAdapter(adapter);
        }
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.view_zxing:


//                    break;
//                case R.id.im_user:


//                    break;
                default:
                    break;
            }
        }
    };

    //申请权限
    private void requestPermission() {
        if (cameraIsCanUse()) {
            /*禁止扫描二维码*/
            ToastUtil.showToast(getContext(), "禁止扫描二维码");
//          DjhJumpUtil.getInstance().startBaseActivity(getActivity(), ZxingActivity.class);
        } else {
            ToastUtil.showToast(getContext(), "您已禁止拍照和录像权限，请手动打开");
        }
    }

    private boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {

            MobclickAgent.reportError(getActivity(), "二维码扫描出现的异常（open()）：  " + e);
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                MobclickAgent.reportError(getActivity(), "二维码扫描出现的异常（release()）：  " + e);
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }


    private void initData() {


    }

    //点击事件
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    /*
    *点读适配器
    * */
    private class ReadAdapter extends FragmentPagerAdapter {

        private List<String> imagePathFromSD;

        private ReadAdapter(FragmentManager fragmentManager, List<String> imagePathFromSD) {
            super(fragmentManager);
            this.imagePathFromSD = imagePathFromSD;
        }

        @Override
        public int getCount() {
            return imagePathFromSD.size();
        }

        @Override
        public Fragment getItem(int arg0) {

            ReadFragment fr = new ReadFragment();
            Bundle bundle = new Bundle();

            bundle.putString("path", imagePathFromSD.get(arg0));
            fr.setArguments(bundle);

            return fr;
        }

        @Override/*销毁的是销毁当前的页数*/
        public void destroyItem(ViewGroup container, int position, Object object) {
            //如果注释这行，那么不管怎么切换，page都不会被销毁
            super.destroyItem(container, position, object);
            //希望做一次垃圾回收
            System.gc();
        }


    }


}
