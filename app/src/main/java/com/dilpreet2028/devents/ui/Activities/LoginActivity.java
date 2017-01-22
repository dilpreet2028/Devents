package com.dilpreet2028.devents.ui.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dilpreet2028.devents.InterfaceImpl.LoginPresenterImpl;
import com.dilpreet2028.devents.Interfaces.LoginPresenter;
import com.dilpreet2028.devents.Interfaces.LoginView;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.api.EventsApi;
import com.dilpreet2028.devents.data.api.NewsApi;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,LoginView,
														ResultCallback<LocationSettingsResult>{
	private GoogleApiClient googleApiClient;
	private SignInButton signInButton;
	private final int SIGN_IN_CODE=1290;
	private final int LOCATION_CODE=12;
	private LoginPresenter loginPresenter;
	private SharedPreferences sharedPreferences;
	private LocationRequest locationRequest;
	protected LocationSettingsRequest mLocationSettingsRequest;
	private Location location;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
			UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	private final int REQUEST_CHECK_SETTINGS=12;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		sharedPreferences= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

		if(alreadyRegistered())
		{
			proceedToNext();
			return;
		}


		NewsApi newsApi=new NewsApi(getApplicationContext());
		newsApi.getNews();


		GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		googleApiClient=new GoogleApiClient.Builder(this)
				.enableAutoManage(this,this)
				.addApi(Auth.GOOGLE_SIGN_IN_API,gso)
				.addApi(LocationServices.API)
				.build();

		signInButton=(SignInButton)findViewById(R.id.sign_in_button);
		signInButton.setSize(SignInButton.SIZE_WIDE);

		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
				startActivityForResult(intent,SIGN_IN_CODE);

			}
		});

		loginPresenter=new LoginPresenterImpl(this);



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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==SIGN_IN_CODE){
			GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			loginPresenter.processGoogleSignInData(result);
		}

		if (requestCode == REQUEST_CHECK_SETTINGS) {
			if (resultCode == RESULT_OK) {
				fetchLatLong();

			} else {

				Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
			}

		}
	}



	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Utility.logger("connection failed: "+connectionResult.getErrorMessage());
	}

	@Override
	public void onSucess(String email) {
		//start intent

		sharedPreferences.edit().putString(getString(R.string.pref_email),email).apply();
		getLocationDetails();
	}

	@Override
	public void onError(String msg) {
		Utility.logger(msg);
	}

	private boolean alreadyRegistered(){
		return sharedPreferences.contains(getString(R.string.pref_email));
	}

	private void proceedToNext(){
		startActivity(new Intent(LoginActivity.this,MainActivity.class));
		finish();
	}


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
//			Utility.logger(location.getLongitude()+"  "+location.getLatitude());
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
					status.startResolutionForResult(LoginActivity.this,
						REQUEST_CHECK_SETTINGS);
				Utility.logger("ssdds");
				}catch (IntentSender.SendIntentException e){

				}
				break;

			case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
				break;
		}
	}

	private void fetchLatLong(){
		try {
			location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
			if (location==null)
				return;
			Utility.logger(location.getLongitude()+"  ");

			sharedPreferences.edit().putString(getString(R.string.pref_lat),location.getLatitude()+"").apply();
			sharedPreferences.edit().putString(getString(R.string.pref_long),location.getLongitude()+"").apply();

			EventsApi.initEvents(getApplicationContext());


			proceedToNext();
		}catch (SecurityException e){}
	}

}
