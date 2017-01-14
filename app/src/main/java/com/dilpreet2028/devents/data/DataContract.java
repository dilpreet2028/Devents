package com.dilpreet2028.devents.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dilpreet on 7/1/17.
 */

public final class DataContract {

	public static final String CONTENT_AUTHORITY="com.dilpreet2028.devents";

	public static final Uri CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
	public static final String PATH_NEWS="news";
	public static final class NewsItems implements BaseColumns{

		public static final Uri CONTENT_URI=Uri.withAppendedPath(DataContract.CONTENT_URI,"news");
		public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_NEWS;
		public static final String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_NEWS;

		public static final String TABLE_NAME="news";
		public static final String COLUMN_AUTHOR="author";
		public static final String COLUMN_TITLE="title";
		public static final String COLUMN_DESC="desc";
		public static final String COLUMN_URL="url";
		public static final String COLUMN_URL_IMG="url_image";
		public static final String COLUMN_PUB_AT="pub_at";

		public static final int COLUMN_ID_INT=0;
		public static final int COLUMN_AUTHOR_INT=2;
		public static final int COLUMN_TITLE_INT=3;
		public static final int COLUMN_DESC_INT=4;
		public static final int COLUMN_URL_INT=5;
		public static final int COLUMN_URL_IMG_INT=6;
		public static final int COLUMN_PUB_AT_INT=7;

		public static final String[] PROJECTIONS={
				TABLE_NAME+"."+_ID,
				COLUMN_AUTHOR,
				COLUMN_TITLE,
				COLUMN_DESC,
				COLUMN_URL,
				COLUMN_URL_IMG,
				COLUMN_PUB_AT
		};

	}

}
