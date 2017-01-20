package com.dilpreet2028.devents.FCMReg;

import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.api.EventsApi;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by dilpreet on 15/1/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
	@Override
	public void onTokenRefresh() {
		super.onTokenRefresh();
		String token= FirebaseInstanceId.getInstance().getToken();
		Utility.logger("here man");
		EventsApi.register(this,token);
	}


}
