package com.dilpreet2028.devents.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.ui.Activities.EventInfoActivity;
import com.dilpreet2028.devents.ui.Activities.MainActivity;

/**
 * Created by dilpreet on 21/1/17.
 */

public class EventWidget extends AppWidgetProvider {
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if(Utility.ACTION_DATA_UPDATED.equals(intent.getAction()))
		{

			AppWidgetManager manager=AppWidgetManager.getInstance(context);
			int ids[]=manager.getAppWidgetIds(new ComponentName(context,getClass()));
			manager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
			Log.d("mytag","manager notified");
		}


	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("mytag","onpudate");
		for(int appId:appWidgetIds){
			RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget_layout);
			Log.d("mytag","inside update");
			Intent intent=new Intent(context, MainActivity.class);
			PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
			views.setOnClickPendingIntent(R.id.widget_header,pendingIntent);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				setRemoteAdapter(context, views);
			} else {
				setRemoteAdapterV11(context, views);
			}

			views.setEmptyView(R.id.widget_list, R.id.widget_empty);

			Intent clickIntentTemplate =new Intent(context, EventInfoActivity.class);
			PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
					.addNextIntentWithParentStack(clickIntentTemplate)
					.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);


			appWidgetManager.updateAppWidget(appId,views);

		}

	}



	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(R.id.widget_list,
				new Intent(context, EventsWidgetService.class));
	}

	@SuppressWarnings("deprecation")
	private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(0, R.id.widget_list,new Intent(context, EventsWidgetService.class));
	}
	}
