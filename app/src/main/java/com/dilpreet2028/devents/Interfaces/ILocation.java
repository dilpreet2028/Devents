package com.dilpreet2028.devents.Interfaces;

import android.content.Context;

/**
 * Created by dilpreet on 20/1/17.
 */

public interface ILocation {
	public void calculate(String locationName, Context context);

	public interface Callback {
		public void fetchDistance(int distance);

		public void onError(String msg);
	}
}
