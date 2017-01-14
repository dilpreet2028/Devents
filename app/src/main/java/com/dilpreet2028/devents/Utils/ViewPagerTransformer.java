package com.dilpreet2028.devents.Utils;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.dilpreet2028.devents.R;

/**
 * Created by dilpreet on 7/1/17.
 */

public class ViewPagerTransformer implements ViewPager.PageTransformer {
	@Override
	public void transformPage(View page, float position) {
		int height=page.getHeight();

		if (position<-1)
			page.setAlpha(0.3f);
		else if (position<=1){
			page.setAlpha(1f);
		}
		else
			page.setAlpha(0.3f);
	}
}
