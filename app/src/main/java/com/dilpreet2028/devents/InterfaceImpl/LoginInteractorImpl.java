package com.dilpreet2028.devents.InterfaceImpl;

import android.content.SharedPreferences;

import com.dilpreet2028.devents.BuildConfig;
import com.dilpreet2028.devents.Interfaces.LoginInteractor;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.api.EventsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dilpreet on 12/1/17.
 */

public class LoginInteractorImpl implements LoginInteractor {
	@Override
	public void login(String name, final String email, final OnLoginFinished onLoginFinished) {


		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BuildConfig.EVENT_API_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		Utility.logger("sending request");
		EventsApi.Api api = retrofit.create(EventsApi.Api.class);
		Call<Void> call = api.login(name, email);
		call.enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {

				onLoginFinished.onSuccess(email);
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				onLoginFinished.onError(t.getMessage());
			}
		});


	}

}
