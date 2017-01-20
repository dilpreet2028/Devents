package com.dilpreet2028.devents.data.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.dilpreet2028.devents.BuildConfig;
import com.dilpreet2028.devents.Models.Article;
import com.dilpreet2028.devents.Models.News;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by dilpreet on 7/1/17.
 */

public class NewsApi {
	private Context context;
	public NewsApi(Context context) {
		this.context=context;
	}

	public interface FetchNews{
		@GET("v1/articles?source=techcrunch&sortBy=latest&apiKey="+ BuildConfig.NEWS_API_KEY)
		public Call<News> getNews();
	}

	public void getNews(){
		Retrofit retrofit=new Retrofit.Builder()
				.baseUrl(BuildConfig.NEWS_API_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		FetchNews fetchNews=retrofit.create(FetchNews.class);
		Call<News> call=fetchNews.getNews();
		call.enqueue(new Callback<News>() {
			@Override
			public void onResponse(Call<News> call, Response<News> response) {
				News news=response.body();
				appendData(news.getArticles());

			}

			@Override
			public void onFailure(Call<News> call, Throwable t) {
				Log.d("mytag","error");
			}
		});

	}
	private void appendData(List<Article> articleList){

		Vector<ContentValues> cVector=new Vector<>(articleList.size());
		String savedDate=maxDatePresent();

		for(Article article:articleList){

			if(!Utility.shouldAdd(article.getPublishedAt(),savedDate))
				continue;


			ContentValues values=new ContentValues();
			values.put(DataContract.NewsItems.COLUMN_AUTHOR,article.getAuthor());
			values.put(DataContract.NewsItems.COLUMN_DESC,article.getDescription());
			values.put(DataContract.NewsItems.COLUMN_PUB_AT,article.getPublishedAt());
			values.put(DataContract.NewsItems.COLUMN_TITLE,article.getTitle());
			values.put(DataContract.NewsItems.COLUMN_URL,article.getUrl());
			values.put(DataContract.NewsItems.COLUMN_URL_IMG,article.getUrlToImage());
			cVector.add(values);
		}

		if(cVector.size()>0) {
			context.getContentResolver().delete(DataContract.NewsItems.CONTENT_URI
					,DataContract.NewsItems.COLUMN_PUB_AT+"< ?",
					new String[]{timeStampBeforeMonth()});

			ContentValues[] values = new ContentValues[cVector.size()];
			cVector.toArray(values);
			context.getContentResolver().bulkInsert(DataContract.NewsItems.CONTENT_URI,values);
		}
	}

	private String maxDatePresent(){
		String timeStamp=null;
		Cursor cursor=context.getContentResolver().query(DataContract.NewsItems.CONTENT_URI,
				new String[]{"max("+DataContract.NewsItems.COLUMN_PUB_AT+")"}
				,null,null,null);
		if(cursor.moveToNext())
			timeStamp=cursor.getString(0);

		if(timeStamp==null)
			timeStamp=timeStampBeforeMonth();

		cursor.close();
		return timeStamp;
	}

	private String timeStampBeforeMonth(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,Calendar.getInstance().get(Calendar.MONTH)-1);
		String str=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").
				format(new Date(cal.getTimeInMillis()));
		return str;
	}
}
