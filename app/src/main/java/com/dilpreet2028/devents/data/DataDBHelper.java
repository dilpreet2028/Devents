package com.dilpreet2028.devents.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dilpreet2028.devents.Utils.Utility;

/**
 * Created by dilpreet on 8/1/17.
 */

public class DataDBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION=3;
	private static final String DB_NAME="news.db";

	private final String CREATE_NEWS_TABLE="Create table "+DataContract.NewsItems.TABLE_NAME+"( "+
			DataContract.NewsItems._ID+" INTEGER PRIMARY KEY ,"+
			DataContract.NewsItems.COLUMN_AUTHOR+" TEXT NOT NULL ,"+
			DataContract.NewsItems.COLUMN_TITLE+" TEXT NOT NULL ,"+
			DataContract.NewsItems.COLUMN_DESC+" TEXT NOT NULL ,"+
			DataContract.NewsItems.COLUMN_URL+" TEXT NOT NULL ,"+
			DataContract.NewsItems.COLUMN_URL_IMG+" TEXT NOT NULL ,"+
			DataContract.NewsItems.COLUMN_PUB_AT+" TEXT NOT NULL );";

	private final String CREATE_EVENT_TABLE="Create table "+DataContract.EventsItem.TABLE_NAME+"( "+
			DataContract.EventsItem._ID+" INTEGER PRIMARY KEY ,"+
			DataContract.EventsItem.COLUMN_NAME+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_E_ID+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_DESC+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_PIC+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_INTERESTED+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_GOING+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_PLACE_NAME+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_CITY+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_LAT+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_LONG+" TEXT NOT NULL ,"+
			DataContract.EventsItem.COLUMN_STREET+" TEXT NOT NULL );";



	private final String DROP_NEWS_TABLE="drop table if exists "+DataContract.NewsItems.TABLE_NAME;
	private final String DROP_EVENTS_TABLE="drop table if exists "+DataContract.EventsItem.TABLE_NAME;

	public DataDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
		sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);

		Utility.logger("created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL(DROP_NEWS_TABLE);
		sqLiteDatabase.execSQL(DROP_EVENTS_TABLE);
		onCreate(sqLiteDatabase);
	}
}
