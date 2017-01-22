package com.dilpreet2028.devents.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dilpreet2028.devents.Services.NewsIntentService;

/**
 * Created by dilpreet on 20/1/17.
 */

public class NewsBroadCastReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		context.startService(new Intent(context,NewsIntentService.class));
	}
}
