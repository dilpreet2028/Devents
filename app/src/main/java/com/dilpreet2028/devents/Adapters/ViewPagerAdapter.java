package com.dilpreet2028.devents.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dilpreet2028.devents.ui.Fragments.EventsListFragment;
import com.dilpreet2028.devents.ui.Fragments.NewsListFragment;

/**
 * Created by dilpreet on 7/1/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		return position==0?"News":"Events";
	}

	@Override
	public Fragment getItem(int position) {
		if (position==0)
			return new NewsListFragment();
		else
			return new EventsListFragment();
	}


}
