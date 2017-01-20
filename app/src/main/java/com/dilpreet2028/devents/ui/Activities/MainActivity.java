package com.dilpreet2028.devents.ui.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dilpreet2028.devents.Adapters.ViewPagerAdapter;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.ClipBoardMonitorService;
import com.dilpreet2028.devents.Utils.NewsBroadCastReciever;
import com.dilpreet2028.devents.Utils.ViewPagerTransformer;
import com.dilpreet2028.devents.data.api.LocationApi;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.main_viewpager)
	ViewPager viewPager;

	private ViewPagerAdapter viewPagerAdapter;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		FirebaseMessaging.getInstance().subscribeToTopic("events");

		viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setPageTransformer(false,new ViewPagerTransformer());
		viewPager.setPageMargin(30);

		sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

		startService(new Intent(this, ClipBoardMonitorService.class));

		setAlarmForService();
	}

	private void setAlarmForService(){
		Intent intent=new Intent(getApplicationContext(), NewsBroadCastReciever.class);

		PendingIntent pi=PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

		AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000*60, 1000*60*5,pi);
	}
}
