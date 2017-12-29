package com.fxtx.framework.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * PagerAdaptere 适配器 主要适配Fragment
 * @param <T>
 */
public class FgPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

	private FragmentManager fm;
	private List<T> fragments;

	public FgPagerAdapter(FragmentManager fm, List<T> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}

	@Override
	public T getItem(int position) {
		if (fragments != null)
			return fragments.get(position);
		return null;
	}

	@Override
	public int getCount() {
		if (fragments != null)
			return fragments.size();
		return 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragments(List<T> fragments) {
		if (this.fragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}
}
