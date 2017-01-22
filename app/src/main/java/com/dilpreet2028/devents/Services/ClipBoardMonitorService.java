package com.dilpreet2028.devents.Services;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.View;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;

public class ClipBoardMonitorService extends Service {


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
			if(Utility.containsFBUrl(link)) {
				String eventId=Utility.getEventId(link);

				Intent intent = new Intent(getApplicationContext(), CreateEventService.class);
				intent.putExtra(getString(R.string.event), eventId);
				startService(intent);
			}
			Utility.logger("out");
		}




	}

	//	/*
//	* Displaying a toast as an overlay window
//	* */
//	private void displayOverlayWindow(final String eventId){
//		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(R.layout.share_layout,null);
//
//		final Toast toast = new Toast(getApplicationContext());
//		toast.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
//		toast.show();
//
//		layout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Utility.logger("clickedd");
//				Intent intent = new Intent(getApplicationContext(), CreateEventService.class);
//				intent.putExtra(getString(R.string.event), eventId);
//				startService(intent);
//
//				toast.cancel();
//				Toast.makeText(getApplicationContext(),"Event Shared",Toast.LENGTH_LONG).show();
//
//
//			}
//		});
//	}


}
