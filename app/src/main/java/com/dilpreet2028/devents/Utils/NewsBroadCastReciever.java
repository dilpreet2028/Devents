package com.dilpreet2028.devents.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by dilpreet on 20/1/17.
 */

public class NewsBroadCastReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Utility.logger("assa");
		Toast.makeText(context, "adsjadshsjkadhkl", Toast.LENGTH_SHORT).show();
//		context.startService(new Intent(context,NewsIntentService.class));
	}
}
