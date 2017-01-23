package com.dilpreet2028.devents.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by dilpreet on 17/1/17.
 */

public class MyScrollView extends ScrollView {

	Callback callback;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		callback = null;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (callback == null)
			throw new NullPointerException("Scrollview Callback not implemented");
		callback.onScroll();

	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public interface Callback {
		public void onScroll();
	}
}
