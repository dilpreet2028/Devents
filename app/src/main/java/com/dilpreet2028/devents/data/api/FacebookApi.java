package com.dilpreet2028.devents.data.api;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dilpreet2028.devents.BuildConfig;
import com.dilpreet2028.devents.InterfaceImpl.LocationImpl;
import com.dilpreet2028.devents.Interfaces.ILocation;
import com.dilpreet2028.devents.Models.Event.Event;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;
import com.dilpreet2028.devents.ui.Activities.EventInfoActivity;
import com.dilpreet2028.devents.ui.Activities.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dilpreet on 15/1/17.
 */

public class FacebookApi {
	public interface GraphApi{

		@GET("v2.8/{event_id}?" +
				"fields=cover,interested_count,attending_count,description,name,place&access_token="
				+ BuildConfig.FB_APP_ID+"|"+ BuildConfig.FB_APP_SECRET)
		public Call<Event> fetchEvent(@Path("event_id") String eventId);

		public interface CallBack{
			public void onSuccess(Event event);
			public void onError();
		}
	}

	public static void fetchInfo(final Context context,final String eventId, final GraphApi.CallBack callBack){

		Retrofit retrofit=new Retrofit.Builder()
				.baseUrl("https://graph.facebook.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		FacebookApi.GraphApi graphApi=retrofit.create(FacebookApi.GraphApi.class);
		Call<Event> call=graphApi.fetchEvent(eventId);
		call.enqueue(new Callback<Event>() {
			@Override
			public void onResponse(Call<Event> call, Response<Event> response) {
				Event event=response.body();
				Utility.logger(event.getPlace().getLocation().getLatitude()+"  latitude");
				appendData(event,context);
				callBack.onSuccess(event);

			}

			@Override
			public void onFailure(Call<Event> call, Throwable t) {
				callBack.onError();
			}
		});
	}



	private static void appendData(final Event event,final Context context){

		final String lati=event.getPlace().getLocation().getLatitude()+"";
		final String longi=event.getPlace().getLocation().getLongitude()+"";

		ILocation iLocation=new LocationImpl(new ILocation.Callback() {
			@Override
			public void fetchDistance(int distance) {
				if(distance< Utility.MAX_DISTANCE){

					ContentValues contentValue=new ContentValues();
					contentValue.put(DataContract.EventsItem.COLUMN_NAME,event.getName());
					contentValue.put(DataContract.EventsItem.COLUMN_E_ID,event.getId());
					contentValue.put(DataContract.EventsItem.COLUMN_DESC,event.getDescription());
					contentValue.put(DataContract.EventsItem.COLUMN_PIC,event.getCover().getSource());
					contentValue.put(DataContract.EventsItem.COLUMN_INTERESTED,event.getInterestedCount());
					contentValue.put(DataContract.EventsItem.COLUMN_GOING,event.getAttendingCount());
					contentValue.put(DataContract.EventsItem.COLUMN_PLACE_NAME,event.getPlace().getName());
					contentValue.put(DataContract.EventsItem.COLUMN_CITY,event.getPlace().getLocation().getCity());
					contentValue.put(DataContract.EventsItem.COLUMN_LAT,event.getPlace().getLocation().getLatitude());
					contentValue.put(DataContract.EventsItem.COLUMN_LONG,event.getPlace().getLocation().getLongitude());
					contentValue.put(DataContract.EventsItem.COLUMN_STREET,event.getPlace().getLocation().getStreet());

					context.getContentResolver().insert(DataContract.EventsItem.CONTENT_URI,contentValue);

					Utility.logger("addedddd");

					Log.d("mytag","updated widget");
					Intent updatedDataIntent=new Intent(Utility.ACTION_DATA_UPDATED);
					updatedDataIntent.setPackage(context.getPackageName());
					context.sendBroadcast(updatedDataIntent);

					notifyUser(context,event.getName(),event.getId());

				}
				else {
					Utility.logger("out of coverage area :p");
					Toast.makeText(context, "out of coverage area :p", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(String msg) {
				Utility.logger("ilocation "+msg);
			}
		});

		Utility.logger(lati+" "+longi);
		iLocation.calculate(lati+","+longi,context);

	}


	private static void notifyUser(Context context,String eventTitle,String eventId){

		Intent intent=new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pi=PendingIntent.getActivity(context,0,intent,0);


		NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
								.setSmallIcon(R.mipmap.ic_launcher)
								.setContentTitle("New Event")
								.setContentIntent(pi)
								.setContentText(eventTitle);
		manager.notify(12,builder.build());
	}
}
