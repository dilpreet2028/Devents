package com.dilpreet2028.devents.InterfaceImpl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dilpreet2028.devents.Interfaces.ILocation;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.api.LocationApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by dilpreet on 20/1/17.
 */

public class LocationImpl implements ILocation {
	private ILocation.Callback callback;

	public LocationImpl(ILocation.Callback callback) {
		this.callback = callback;
	}

	@Override
	public void calculate(String locationName, Context context) {

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://maps.googleapis.com/")
				.build();

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		LocationApi.Api api = retrofit.create(LocationApi.Api.class);

		Call<ResponseBody> call = api.getDistance(sharedPreferences.getString(context.getString(R.string.pref_lat), null) + "," +
				sharedPreferences.getString(context.getString(R.string.pref_long), null), locationName, "false");
		Utility.logger("asklajslask");
		call.enqueue(new retrofit2.Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					String msg = response.body().string();
					fetchDistance(msg);

				} catch (Exception e) {
					callback.onError("LocationRetrofit: " + e.toString());
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				Utility.logger(t.getMessage());
			}
		});
	}


	private void fetchDistance(String data) {
		try {
			final JSONObject json = new JSONObject(data);
			Utility.logger(data);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);

			JSONArray newTempARr = routes.getJSONArray("legs");
			JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

			JSONObject distOb = newDisTimeOb.getJSONObject("distance");

			callback.fetchDistance(Integer.parseInt(distOb.getString("value")) / 1000);

		} catch (JSONException e) {
			Utility.logger("Error: " + e.toString());
			callback.onError("Error: " + e.toString());
		}
	}
}
