package com.dilpreet2028.devents.Widget;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;
import com.dilpreet2028.devents.ui.Activities.EventInfoActivity;

/**
 * Created by dilpreet on 21/1/17..
 */
public class EventsWidgetService extends RemoteViewsService {


	@Override
	public RemoteViewsFactory onGetViewFactory(final Intent intent) {

		return new RemoteViewsFactory() {
			private Cursor cursorData = null;

			@Override
			public void onCreate() {

			}

			@Override
			public void onDataSetChanged() {
				if (cursorData != null) {
					cursorData.close();
				}

				final long identityToken = Binder.clearCallingIdentity();
				cursorData = getContentResolver().query(DataContract.EventsItem.CONTENT_URI, DataContract.EventsItem.PROJECTIONS, null,
						null, null);
				DatabaseUtils.dumpCursor(cursorData);

				Binder.restoreCallingIdentity(identityToken);
			}

			@Override
			public void onDestroy() {
				if (cursorData != null) {
					cursorData.close();
					cursorData = null;
				}
			}

			@Override
			public int getCount() {
				return cursorData == null ? 0 : cursorData.getCount();
			}

			@Override
			public RemoteViews getViewAt(int position) {
				if (position == AdapterView.INVALID_POSITION ||
						cursorData == null || !cursorData.moveToPosition(position)) {
					return null;
				}
				RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.events_item_widget);


				String name = cursorData.getString(cursorData.getColumnIndex(DataContract.EventsItem.COLUMN_NAME));
				String going = cursorData.getString(cursorData.getColumnIndex(DataContract.EventsItem.COLUMN_GOING));
				String eId = cursorData.getString(cursorData.getColumnIndex(DataContract.EventsItem.COLUMN_E_ID));
				String imageUrl = cursorData.getString(cursorData.getColumnIndex(DataContract.EventsItem.COLUMN_PIC));
				try {
					Bitmap theBitmap = Glide.
							with(getApplicationContext()).
							load(imageUrl).
							asBitmap().
							into(250, 300).
							get();
					remoteViews.setImageViewBitmap(R.id.events_item_image, theBitmap);

				} catch (Exception e) {
					Utility.logger(e.toString());
				}


				remoteViews.setTextViewText(R.id.events_item_title, name);
				remoteViews.setTextViewText(R.id.events_item_going, getString(R.string.going, going));
				Utility.logger(name + " asdsad");

				Intent detailIntent = new Intent(getApplicationContext(), EventInfoActivity.class);
				detailIntent.putExtra(getString(R.string.event_id), eId);
				remoteViews.setOnClickFillInIntent(R.id.event_item_layout, detailIntent);

				return remoteViews;
			}

			@Override
			public RemoteViews getLoadingView() {
				return new RemoteViews(getPackageName(), R.layout.events_item_widget);
			}

			@Override
			public int getViewTypeCount() {
				return 1;
			}

			@Override
			public long getItemId(int position) {
				if (cursorData.moveToPosition(position))
					return cursorData.getLong(cursorData.getColumnIndex(DataContract.EventsItem._ID));
				return position;
			}

			@Override
			public boolean hasStableIds() {
				return true;
			}
		};
	}
}
