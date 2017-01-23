package com.dilpreet2028.devents.FCMReg;

import android.content.ContentValues;

import com.dilpreet2028.devents.Models.Event.Event;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;
import com.dilpreet2028.devents.data.api.FacebookApi;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by dilpreet on 15/1/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		Map data = remoteMessage.getData();

		Utility.logger(data.toString() + "  " + data.get("message").toString());

		String eventId = data.get("message").toString();

		FacebookApi.fetchInfo(getApplicationContext(), eventId, new FacebookApi.GraphApi.CallBack() {
			@Override
			public void onSuccess(Event event) {
				Utility.logger("event recieved");
			}

			@Override
			public void onError() {
				Utility.logger("");
			}
		});
	}


}
