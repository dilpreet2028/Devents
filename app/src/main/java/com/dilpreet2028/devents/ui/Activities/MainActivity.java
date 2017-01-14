package com.dilpreet2028.devents.ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dilpreet2028.devents.Adapters.ViewPagerAdapter;
import com.dilpreet2028.devents.GCMReg.RegistrationIntentService;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.ViewPagerTransformer;
import com.dilpreet2028.devents.data.api.NewsApi;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	@BindView(R.id.main_viewpager)
	ViewPager viewPager;

	private ViewPagerAdapter viewPagerAdapter;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setPageTransformer(false,new ViewPagerTransformer());
		viewPager.setPageMargin(30);

		sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
		if(!sharedPreferences.contains(getString(R.string.pref_token)))
			startService(new Intent(this, RegistrationIntentService.class));

	}
}
