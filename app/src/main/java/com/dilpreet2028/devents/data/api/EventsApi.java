package com.dilpreet2028.devents.data.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dilpreet2028.devents.BuildConfig;
import com.dilpreet2028.devents.Models.Event.Event;
import com.dilpreet2028.devents.Models.EventListModel;
import com.dilpreet2028.devents.Models.EventResult;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dilpreet on 12/1/17.
 */

public class EventsApi {

	public interface Api {
		@FormUrlEncoded
		@POST("api/login/")
		public Call<Void> login(@Field("name") String name, @Field("email") String email);

		@FormUrlEncoded
		@POST("api/gcm_reg/")
		public Call<Void> gcm_reg(@Field("email") String email, @Field("key") String token);

		@GET("api/reg_event")
		public Call<EventResult> regEvent(@Query("event_id") String eventId, @Query("email") String email);

		@GET("api/events/")
		public Call<List<EventListModel>> getEventList();

	}

	public static void register(final Context context, final String token) {
		final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String email = sharedPreferences.getString(context.getString(R.string.pref_email), null);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BuildConfig.EVENT_API_URL)
				.build();
		EventsApi.Api api = retrofit.create(EventsApi.Api.class);

		Call<Void> call = api.gcm_reg(email, token);
		call.enqueue(new Callback<Void>() {
			@Override
			public void onResponse(Call<Void> call, Response<Void> response) {
				sharedPreferences.edit().putString(context.getString(R.string.pref_token), token).apply();
				Utility.logger("gcm registered");
			}

			@Override
			public void onFailure(Call<Void> call, Throwable t) {
				Utility.logger("Reg Server error " + t.getMessage());
			}
		});
	}

	public static void initEvents(final Context context) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BuildConfig.EVENT_API_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		Api api = retrofit.create(Api.class);
		Call<List<EventListModel>> call = api.getEventList();
		call.enqueue(new Callback<List<EventListModel>>() {
			@Override
			public void onResponse(Call<List<EventListModel>> call, Response<List<EventListModel>> response) {
				processEvents(context, response.body());
			}

			@Override
			public void onFailure(Call<List<EventListModel>> call, Throwable t) {
				Utility.logger(t.getMessage());
			}
		});
	}

	private static void processEvents(Context context, List<EventListModel> eventListModels) {
		for (EventListModel model : eventListModels) {
			FacebookApi.fetchInfo(context, model.getEventId(), new FacebookApi.GraphApi.CallBack() {
				@Override
				public void onSuccess(Event event) {

				}

				@Override
				public void onError() {
					Utility.logger("error");
				}
			});
		}
	}
}
