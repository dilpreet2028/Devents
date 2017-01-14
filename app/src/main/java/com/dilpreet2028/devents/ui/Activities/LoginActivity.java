package com.dilpreet2028.devents.ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dilpreet2028.devents.GCMReg.RegistrationIntentService;
import com.dilpreet2028.devents.InterfaceImpl.LoginPresenterImpl;
import com.dilpreet2028.devents.Interfaces.LoginPresenter;
import com.dilpreet2028.devents.Interfaces.LoginView;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,LoginView{

	private GoogleApiClient googleApiClient;
	private SignInButton signInButton;
	private final int SIGN_IN_CODE=1290;
	private LoginPresenter loginPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
								.requestEmail()
								.build();
		googleApiClient=new GoogleApiClient.Builder(this)
							.enableAutoManage(this,this)
							.addApi(Auth.GOOGLE_SIGN_IN_API,gso)
							.build();

		signInButton=(SignInButton)findViewById(R.id.sign_in_button);
		signInButton.setSize(SignInButton.SIZE_STANDARD);

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
		SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		sharedPreferences.edit().putString(getString(R.string.pref_email),email).apply();
		startActivity(new Intent(LoginActivity.this,MainActivity.class));

	}

	@Override
	public void onError(String msg) {
		Utility.logger(msg);
	}
}
