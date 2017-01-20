package com.dilpreet2028.devents.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.dilpreet2028.devents.Utils.Utility;

/**
 * Created by dilpreet on 8/1/17.
 */

public class DataProvider extends ContentProvider {

	private static final int NEWS_LIST=1;
	private static final int NEWS_ITEM=2;
	private static final int EVENTS_LIST=3;
	private static final int EVENTS_ITEM=4;
	private static UriMatcher uriMatcher=null;
	private DataDBHelper dbHelper;

	static {
		uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_NEWS,NEWS_LIST);
		uriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_NEWS+"/*",NEWS_ITEM);
		uriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_EVENTS,EVENTS_LIST);
		uriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_EVENTS+"/*",EVENTS_ITEM);
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		Uri returnUri=null;
		long id;
		switch (uriMatcher.match(uri)){
			case NEWS_LIST:
				id=db.insert(DataContract.NewsItems.TABLE_NAME,null,contentValues);
				if(id==-1)
					throw  new SQLException("Unable to insert record");
				returnUri= ContentUris.withAppendedId(uri,id);
				break;
			case EVENTS_LIST:
				id=db.insert(DataContract.EventsItem.TABLE_NAME,null,contentValues);
				if(id==-1)
					throw  new SQLException("Unable to insert record");
				returnUri= ContentUris.withAppendedId(uri,id);
				break;

			default:
				throw new UnsupportedOperationException("Unkown uri:");
		}
		getContext().getContentResolver().notifyChange(uri,null);
		return returnUri;
	}

	private Cursor getEventCursor(Uri uri,String[] projection,String sortOrder){

		String selection=DataContract.EventsItem.COLUMN_E_ID+" =?";
		String eventId=getEventIdFromUri(uri);
		Utility.logger(eventId.toString());
		String[] selectionArgs=new String[]{eventId};

		return dbHelper.getReadableDatabase().query(DataContract.EventsItem.TABLE_NAME,
											projection,selection,selectionArgs,null,null,sortOrder);
	}

	private String getEventIdFromUri(Uri uri){
		return uri.getPathSegments().get(1);
	}
	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {
		Cursor retCursor;
		switch (uriMatcher.match(uri)){
			case NEWS_LIST:
				retCursor=dbHelper.getReadableDatabase().query(DataContract.NewsItems.TABLE_NAME,
						projection,selection,selectionArgs,null,null,sortOrder);
				break;
			case EVENTS_LIST:
				retCursor=dbHelper.getReadableDatabase().query(DataContract.EventsItem.TABLE_NAME,
						projection,selection,selectionArgs,null,null,sortOrder);
				break;

			case EVENTS_ITEM:
				retCursor=getEventCursor(uri,projection,sortOrder);
				DatabaseUtils.dumpCursor(retCursor);
				break;
			default:
				throw new UnsupportedOperationException("Unkown Uri :"+uri);
		}
		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)){
			case NEWS_LIST:
				try{
					db.beginTransaction();
					for(ContentValues value:values)
						db.insert(DataContract.NewsItems.TABLE_NAME,null,value);
					db.setTransactionSuccessful();
				}finally {
					db.endTransaction();
					getContext().getContentResolver().notifyChange(uri, null);
					return 1;
				}

			case EVENTS_LIST:
				try{
					db.beginTransaction();
					for(ContentValues value:values)
						db.insert(DataContract.EventsItem.TABLE_NAME,null,value);
					db.setTransactionSuccessful();
				}finally {
					db.endTransaction();
					getContext().getContentResolver().notifyChange(uri, null);
					return 1;
				}
			default:
				throw new UnsupportedOperationException("Unknown uri: "+uri);
		}

	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
		return 0;
	}

	@Override
	public int delete(Uri uri, String s, String[] strings) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		int rowsDeleted=0;
		switch (uriMatcher.match(uri)){
			case NEWS_LIST:
				rowsDeleted=db.delete(DataContract.NewsItems.TABLE_NAME,s,strings);
				break;
			case EVENTS_LIST:
				rowsDeleted=db.delete(DataContract.EventsItem.TABLE_NAME,s,strings);
				break;
			default:
				throw new UnsupportedOperationException("Unknown URI: "+uri);
		}

		getContext().getContentResolver().notifyChange(uri,null);
		return rowsDeleted;
	}

	@Override
	public boolean onCreate() {
		dbHelper=new DataDBHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
			case NEWS_LIST:
				return DataContract.NewsItems.CONTENT_TYPE;
			case NEWS_ITEM:
				return DataContract.NewsItems.CONTENT_ITEM_TYPE;
			case EVENTS_LIST:
				return DataContract.EventsItem.CONTENT_TYPE;
			case EVENTS_ITEM:
				return DataContract.EventsItem.CONTENT_ITEM_TYPE;
			default:
				throw new UnsupportedOperationException("Unkown uri: "+uri);
		}
	}

	@Override
	public void shutdown() {
		dbHelper.close();
		super.shutdown();

	}
}
