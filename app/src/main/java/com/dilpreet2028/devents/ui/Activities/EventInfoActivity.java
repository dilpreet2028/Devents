package com.dilpreet2028.devents.ui.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.MyScrollView;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventInfoActivity extends AppCompatActivity implements OnMapReadyCallback{

	@BindView(R.id.event_info_pic) ImageView imageView;
	@BindView(R.id.event_info_name) TextView nameView;
	@BindView(R.id.event_info_desc) TextView descView;
	@BindView(R.id.event_info_going) TextView goingView;
	@BindView(R.id.event_info_interested) TextView interestedView;
	@BindView(R.id.event_card) CardView cardView;
	@BindView(R.id.scrollview)	MyScrollView myScrollView;

	private Cursor cursor;
	private LatLng latLng;
	private String locationName;
	private String eventId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_info);
		ButterKnife.bind(this);
			eventId = getIntent().getStringExtra(getString(R.string.event_id));

		Utility.logger(eventId);
		cursor=getContentResolver().query(DataContract.EventsItem.getEventUri(eventId),DataContract.EventsItem.PROJECTIONS,null
									,null,null);

		cursor.moveToNext();
		Glide.with(getApplicationContext())
				.load(cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_PIC)))
				.into(imageView);

		String desc=cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_DESC));

		String latitude=cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_LAT));
		String longitude=cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_LONG));
		String name=cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_NAME));
		latLng=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
		locationName=cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_PLACE_NAME));
		nameView.setText(name);
		nameView.setContentDescription(name);
		descView.setText(desc);
		descView.setText(desc);
		goingView.setText(cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_GOING)));
		interestedView.setText(cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_INTERESTED)));


		myScrollView.setCallback(new MyScrollView.Callback() {
			@Override
			public void onScroll() {
				int scrollY=myScrollView.getScrollY();
				imageView.setTranslationY(scrollY*0.45f);
				cardView.setTranslationY(-scrollY*0.05f);
			}
		});

		SupportMapFragment mapFragment =
				(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.event_map);

		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		googleMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12.0f));

	}

	public void eventInfoAttendBtn(View view){
		String eId=cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_E_ID));

		String url=getString(R.string.fb_event_url)+eId;
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
}
