package com.dilpreet2028.devents.GCMReg;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dilpreet2028.devents.BuildConfig;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.api.EventsApi;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by dilpreet on 13/1/17.
 */

public class RegistrationIntentService extends IntentService {
	public RegistrationIntentService() {
		super("RegistrationIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			InstanceID instanceID=InstanceID.getInstance(this);
			String token=instanceID.getToken(getString(R.string.gcm_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
			Utility.logger(token);
			register(token);

		}catch (Exception e){
			Utility.logger(e.getMessage());
		}
	}

	private void register(final String token){
		final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
		String email=sharedPreferences.getString(getString(R.string.pref_email),null);

		Retrofit retrofit=new Retrofit.Builder()
								.baseUrl(BuildConfig.EVENT_API_URL)
								.build();
		EventsApi.Api api=retrofit.create(EventsApi.Api.class);

		Call<Void> call=api.gcm_reg(email,token);
		call.enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				sharedPreferences.edit().putString(getString(R.string.pref_token),token).apply();
				Utility.logger("gcm registered");
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				Utility.logger(t.getMessage());
			}
		});
	}
}
