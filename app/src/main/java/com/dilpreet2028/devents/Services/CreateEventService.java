package com.dilpreet2028.devents.Services;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.dilpreet2028.devents.BuildConfig;
import com.dilpreet2028.devents.Models.EventResult;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.api.EventsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateEventService extends IntentService {

	public CreateEventService() {
		super("CreateEventService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String eventId=intent.getStringExtra(getString(R.string.event));

		Utility.logger(eventId);
		fetchInfo(eventId);

	}

	private void fetchInfo(String eventId) {

		String email= PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
						.getString(getString(R.string.pref_email),null);


		Retrofit retrofit=new Retrofit.Builder()
				.baseUrl(BuildConfig.EVENT_API_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		EventsApi.Api eApi=retrofit.create(EventsApi.Api.class);
		Call<EventResult> call=eApi.regEvent(eventId,email);
		call.enqueue(new Callback<EventResult>() {
			@Override
			public void onResponse(Call<EventResult> call, Response<EventResult> response) {
				Utility.logger(response.body().getStr()+" ");
				Toast.makeText(getApplicationContext(), getString(R.string.event_shared), Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(Call<EventResult> call, Throwable t) {
				Utility.logger("Failure");
			}
		});
	}


}
