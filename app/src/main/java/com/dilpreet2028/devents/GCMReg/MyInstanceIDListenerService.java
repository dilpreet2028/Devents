package com.dilpreet2028.devents.GCMReg;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by dilpreet on 13/1/17.
 */

public class MyInstanceIDListenerService extends InstanceIDListenerService {
	@Override
	public void onTokenRefresh() {
		startService(new Intent(this,RegistrationIntentService.class));
	}
}
