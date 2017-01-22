package com.dilpreet2028.devents.ui.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dilpreet2028.devents.Adapters.ViewPagerAdapter;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Services.ClipBoardMonitorService;
import com.dilpreet2028.devents.Utils.NewsBroadCastReciever;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.Utils.ViewPagerTransformer;
import com.dilpreet2028.devents.data.api.EventsApi;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ResultCallback<LocationSettingsResult> {

	@BindView(R.id.main_viewpager)
	ViewPager viewPager;
	@BindView(R.id.tabs)
	TabLayout tabLayout;
	private LocationRequest locationRequest;
	private LocationSettingsRequest mLocationSettingsRequest;
	private Location location;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
			UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	private final int REQUEST_CHECK_SETTINGS=12;
	private ViewPagerAdapter viewPagerAdapter;
	private final int LOCATION_CODE=12;
	private GoogleApiClient googleApiClient;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.firebase_subcribe_topic));
		viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setPageTransformer(false,new ViewPagerTransformer());
		viewPager.setPageMargin(30);

		tabLayout.setupWithViewPager(viewPager);

		sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

		startService(new Intent(this, ClipBoardMonitorService.class));

		setAlarmForService();

		initGoogleApi();
	}

	private void initGoogleApi(){
		googleApiClient=new GoogleApiClient.Builder(getApplicationContext())
				.enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

					}
				})
				.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
					@Override
					public void onConnected(@Nullable Bundle bundle) {

							if(fetchLatLong()==-1);
						{
							getLocationDetails();
							Utility.logger("adffd asas");
						}
					}

					@Override
					public void onConnectionSuspended(int i) {

					}
				})
				.addApi(LocationServices.API)
				.build();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		if (requestCode == REQUEST_CHECK_SETTINGS) {
			if (resultCode == RESULT_OK) {
				fetchLatLong();

			} else {

				Toast.makeText(getApplicationContext(), getString(R.string.gps_not_enabled), Toast.LENGTH_LONG).show();
			}

		}
	}


	private void setAlarmForService(){
		Intent intent=new Intent(getApplicationContext(), NewsBroadCastReciever.class);

		PendingIntent pi=PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

		AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000*60*60, 18000000,pi);
	}

	//starter
	public void getLocationDetails() {

		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
		{

			createRequest();

			PendingResult<LocationSettingsResult> result =
					LocationServices.SettingsApi.checkLocationSettings(
							googleApiClient,
							mLocationSettingsRequest
					);

			result.setResultCallback(this);

			Utility.logger("in here");
		}
		else
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_CODE);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode==LOCATION_CODE){
			if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
				Utility.logger("granted");
				getLocationDetails();
			}
		}
	}


	@Override
	public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
		Status status=locationSettingsResult.getStatus();

		switch (status.getStatusCode()){
			case LocationSettingsStatusCodes.SUCCESS:
				Utility.logger("success");
				fetchLatLong();
				break;

			case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
				try {
					status.startResolutionForResult(MainActivity.this,
							REQUEST_CHECK_SETTINGS);
					Utility.logger("ssdds");
				}catch (IntentSender.SendIntentException e){

				}
				break;

			case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
				break;
		}
	}

	private int fetchLatLong(){

		try {


			location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

			if (location==null)
			{
				Utility.logger("returning null");
				return -1;
			}
			Utility.logger(location.getLongitude()+"  ");

			sharedPreferences.edit().putString(getString(R.string.pref_lat),location.getLatitude()+"").apply();
			sharedPreferences.edit().putString(getString(R.string.pref_long),location.getLongitude()+"").apply();

			if(!sharedPreferences.contains(getString(R.string.pref_init_event)))
			{
				EventsApi.initEvents(getApplicationContext());
				sharedPreferences.edit().putBoolean(getString(R.string.pref_init_event),true).apply();
			}


		}catch (SecurityException e){}

		return 10;
	}

	private void createRequest(){
		locationRequest=new LocationRequest();
		locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(locationRequest);
		builder.setAlwaysShow(true);

		mLocationSettingsRequest = builder.build();

	}

}
