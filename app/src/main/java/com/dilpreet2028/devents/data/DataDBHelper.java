package com.dilpreet2028.devents.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dilpreet2028.devents.Utils.Utility;

/**
 * Created by dilpreet on 8/1/17.
 */

public class DataDBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION=1;
	private static final String DB_NAME="news.db";

	private final String CREATE_NEWS_TABLE="Create table "+DataContract.NewsItems.TABLE_NAME+"( "+
											DataContract.NewsItems._ID+" INTEGER PRIMARY KEY ,"+
											DataContract.NewsItems.COLUMN_AUTHOR+" TEXT NOT NULL ,"+
											DataContract.NewsItems.COLUMN_TITLE+" TEXT NOT NULL ,"+
											DataContract.NewsItems.COLUMN_DESC+" TEXT NOT NULL ,"+
											DataContract.NewsItems.COLUMN_URL+" TEXT NOT NULL ,"+
											DataContract.NewsItems.COLUMN_URL_IMG+" TEXT NOT NULL ,"+
											DataContract.NewsItems.COLUMN_PUB_AT+" TEXT NOT NULL );";

	private final String DROP_NEWS_TABLE="drop table "+DataContract.NewsItems.TABLE_NAME+" if exists;";

	public DataDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
		Utility.logger("created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL(DROP_NEWS_TABLE);
		onCreate(sqLiteDatabase);
	}
}
