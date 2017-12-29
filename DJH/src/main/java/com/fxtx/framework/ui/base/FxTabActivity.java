package com.fxtx.framework.ui.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.fxtx.framework.R;
import com.fxtx.framework.ui.FxActivity;
import com.fxtx.framework.ui.FxFragment;
import com.fxtx.framework.ui.bean.BeTab;
import com.fxtx.framework.widgets.badge.BadgeRadioButton;

import java.util.LinkedHashMap;


/**
 * Created by z on 2016/1/18.
 * <p/>
 * 注意。 1  继承FxTab工具的Activity  存储的Fragment显示区域的View 必须叫R.id.tab_fragment
 * 2 必须在layout添加layout_tab 布局对象。
 */
public abstract class FxTabActivity extends FxActivity implements RadioGroup.OnCheckedChangeListener {
    protected LinkedHashMap<Integer, View> radio = new LinkedHashMap<Integer, View>();
    protected FxFragment isFragment; //当前活动的Fragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
    }

    /**
     * 采用 merge 标签 加载的 radio对象
     *
     * @param radioView
     */
    protected void initRadioGroup(View radioView, RadioGroup group) {
        group.addView(radioView);
    }

    /**
     * 自动添加 添加radioButton
     *
     * @param tab
     */
    protected BadgeRadioButton addRadioView(BeTab tab, RadioGroup group) {
        BadgeRadioButton buttton = initRadioView();
        buttton.setText(tab.getTitle());
        buttton.setId(tab.getRedid());
        buttton.setChecked(tab.isChecked());
        buttton.setCompoundDrawablesWithIntrinsicBounds(0, tab.getRadioBtn(), 0, 0);
        group.addView(buttton);
        radio.put(tab.getRedid(), buttton);
        return buttton;
    }

    protected BadgeRadioButton initRadioView() {
        BadgeRadioButton radio = new BadgeRadioButton(this);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        radio.setGravity(Gravity.CENTER);
        radio.setPadding(4, 4, 4, 4);
        radio.setTextColor(ContextCompat.getColor(this, radioTextColor()));
        radio.setButtonDrawable(android.R.color.transparent);
        radio.setBackgroundResource(R.color.white);
        radio.setLayoutParams(layoutParams);
        return radio;
    }

    public int radioTextColor() {
        return R.color.index_radion_tv;
    }

    /*
    * 当fragment进行切换时，采用隐藏与显示的方法加载fragment以防止数据的重复加载
    * @param from
    *
    * @param to
    */
    public void switchContent(FxFragment from, FxFragment to) {
        if (isFragment == null || from == null) {
            isFragment = to;
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.add(R.id.tab_fragment, to, to.getFxTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        } else {
            if (isFragment != to) {
                isFragment = to;
                FragmentTransaction ft = fm.beginTransaction();
                if (!to.isAdded()) {    // 先判断是否被add过
                    ft.hide(from).add(R.id.tab_fragment, to, to.getFxTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
                }
            }
        }
    }


    /**
     * 为页面加载初始状态的fragment
     */
    public void initFragment(Bundle savedInstanceState) {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();
            isFragment = initIndexFragment();
            if (isFragment != null) {
                ft.replace(R.id.tab_fragment, isFragment, isFragment.getFxTag()).commit();
            }
        }
    }

    /**
     * 重构 设置第一个启动的Fragment 对象
     *
     * @return
     */
    protected abstract FxFragment initIndexFragment();
}
