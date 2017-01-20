package com.dilpreet2028.devents.Utils;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;

import com.dilpreet2028.devents.R;

public class ClipBoardMonitorService extends Service {

	private final String FACEBOOK_URL="facebook.com/events/";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private ClipboardManager clipboardManager;
	private MyClipManager myClipManager;
	@Override
	public void onCreate() {
		super.onCreate();
		myClipManager=new MyClipManager();
		clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		clipboardManager.addPrimaryClipChangedListener(myClipManager);
	}

	@Override
	public void onDestroy() {
		clipboardManager.removePrimaryClipChangedListener(myClipManager);
		super.onDestroy();
	}

	private class MyClipManager implements ClipboardManager.OnPrimaryClipChangedListener{
		@Override
		public void onPrimaryClipChanged() {
			ClipData clipData=clipboardManager.getPrimaryClip();
			String link=clipData.getItemAt(0).getText().toString();
			Utility.logger(link);
			if(containsFBUrl(link)) {
				String eventId=getEventId(link);
						Utility.logger("inside");
							Intent intent = new Intent(getApplicationContext(), CreateEventService.class);
				intent.putExtra(getString(R.string.event), eventId);
				startService(intent);
			}
			Utility.logger("out");
		}

		private boolean containsFBUrl(String data){
			return data.contains(FACEBOOK_URL);
		}

		private String getEventId(String url){
			return url.split(FACEBOOK_URL)[1].split("/")[0].split("\\?")[0];
		}
	}


}
