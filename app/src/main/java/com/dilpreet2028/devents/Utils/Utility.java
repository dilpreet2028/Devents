package com.dilpreet2028.devents.Utils;

import android.util.Log;

import com.dilpreet2028.devents.BuildConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by dilpreet on 8/1/17.
 */

public class Utility {

	public static final int MAX_DISTANCE=50;
	public static final String ACTION_DATA_UPDATED="com.dilpreet2028.devents.DATA_UPDATED";


	public static void logger(String msg){
		if(BuildConfig.DEBUG)
		Log.d("mytag",msg);
	}

	public static long parseDate(String date){
		long time=-1;
		try {
			time=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date).getTime();

		}catch (ParseException e){
			Utility.logger(e.toString());
		}
		return time;
	}

	public static boolean shouldAdd(String currentTime,String savedTime){
		return parseDate(currentTime)>parseDate(savedTime);
	}
}
