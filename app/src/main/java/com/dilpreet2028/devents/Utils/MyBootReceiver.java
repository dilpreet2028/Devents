package com.dilpreet2028.devents.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dilpreet2028.devents.Services.ClipBoardMonitorService;

import static android.content.Context.ALARM_SERVICE;

public class MyBootReceiver extends BroadcastReceiver {
	public MyBootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, ClipBoardMonitorService.class));

		Intent in=new Intent(context, NewsBroadCastReciever.class);

		PendingIntent pi=PendingIntent.getBroadcast(context,0,in,0);
		AlarmManager alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000*60, 18000000,pi);

	}
}
