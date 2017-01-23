package com.dilpreet2028.devents.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dilpreet on 7/1/17.
 */

public final class DataContract {

	public static final String CONTENT_AUTHORITY = "com.dilpreet2028.devents";

	public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_NEWS = "news";
	public static final String PATH_EVENTS = "events";

	public static final class NewsItems implements BaseColumns {

		public static final Uri CONTENT_URI = Uri.withAppendedPath(DataContract.CONTENT_URI, "news");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

		public static final String TABLE_NAME = "news";
		public static final String COLUMN_AUTHOR = "author";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESC = "desc";
		public static final String COLUMN_URL = "url";
		public static final String COLUMN_URL_IMG = "url_image";
		public static final String COLUMN_PUB_AT = "pub_at";


		public static final String[] PROJECTIONS = {
				TABLE_NAME + "." + _ID,
				COLUMN_AUTHOR,
				COLUMN_TITLE,
				COLUMN_DESC,
				COLUMN_URL,
				COLUMN_URL_IMG,
				COLUMN_PUB_AT
		};

	}

	public static final class EventsItem implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(DataContract.CONTENT_URI, PATH_EVENTS);
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;

		public static final String TABLE_NAME = "events";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_E_ID = "e_id";
		public static final String COLUMN_DESC = "desc";
		public static final String COLUMN_PIC = "pic";
		public static final String COLUMN_INTERESTED = "interested";
		public static final String COLUMN_GOING = "going";
		public static final String COLUMN_PLACE_NAME = "place_name";
		public static final String COLUMN_CITY = "city";
		public static final String COLUMN_LAT = "lat";
		public static final String COLUMN_LONG = "long";
		public static final String COLUMN_STREET = "street";

		public static final String[] PROJECTIONS = {
				TABLE_NAME + "." + _ID,
				COLUMN_NAME,
				COLUMN_E_ID,
				COLUMN_DESC,
				COLUMN_PIC,
				COLUMN_INTERESTED,
				COLUMN_GOING,
				COLUMN_PLACE_NAME,
				COLUMN_CITY,
				COLUMN_LAT,
				COLUMN_LONG,
				COLUMN_STREET,
		};

		public static Uri getEventUri(String eventId) {
			return CONTENT_URI.buildUpon().appendPath(eventId).build();
		}

	}

}
