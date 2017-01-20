package com.dilpreet2028.devents.data.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dilpreet on 20/1/17.
 */

public class LocationApi {
	public interface Api{
		@GET("maps/api/directions/json")
		Call<ResponseBody> getDistance(@Query("origin") String latlong1, @Query("destination") String latlong2,
									   @Query("sensor") String sensor);
	}


}
