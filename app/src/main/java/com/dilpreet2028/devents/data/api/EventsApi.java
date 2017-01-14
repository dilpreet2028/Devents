package com.dilpreet2028.devents.data.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dilpreet on 12/1/17.
 */

public class EventsApi {

	public interface Api{
		@FormUrlEncoded
		@POST("api/login/")
		public Call<Void> login(@Field("name")String name, @Field("email")String email);

		@FormUrlEncoded
		@POST("api/gcm_reg/")
		public Call<Void> gcm_reg(@Field("email") String email,@Field("key")String token);
	}
}
