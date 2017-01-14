package com.dilpreet2028.devents.GCMReg;

import android.os.Bundle;

import com.dilpreet2028.devents.Utils.Utility;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by dilpreet on 13/1/17.
 */

public class MyGcmListenerService extends GcmListenerService {
	@Override
	public void onMessageReceived(String s, Bundle bundle) {
		super.onMessageReceived(s, bundle);
		String message=bundle.getString("message");
		Utility.logger(message);
	}
}
