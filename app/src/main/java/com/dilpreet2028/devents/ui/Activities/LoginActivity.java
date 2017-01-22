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

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,LoginView{
	private GoogleApiClient googleApiClient;
	private SignInButton signInButton;
	private final int SIGN_IN_CODE=1290;
	private LoginPresenter loginPresenter;
	private SharedPreferences sharedPreferences;




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



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==SIGN_IN_CODE){
			GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			loginPresenter.processGoogleSignInData(result);
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
		proceedToNext();
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


}
