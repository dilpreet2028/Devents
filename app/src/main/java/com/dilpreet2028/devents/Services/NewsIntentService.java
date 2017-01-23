package com.dilpreet2028.devents.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.dilpreet2028.devents.data.api.NewsApi;

public class NewsIntentService extends IntentService {
	public NewsIntentService() {
		super("NewsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		NewsApi newsApi = new NewsApi(this);
		newsApi.getNews();
	}


}
