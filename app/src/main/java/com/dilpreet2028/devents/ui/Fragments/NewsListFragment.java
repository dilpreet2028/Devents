package com.dilpreet2028.devents.ui.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dilpreet2028.devents.Adapters.NewsAdapter;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;
import com.dilpreet2028.devents.data.api.NewsApi;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


	@BindView(R.id.news_recycler_view)
	RecyclerView newsRecyclerView;

	private NewsAdapter newsAdapter;
	private int NEWS_LOADER = 12;
	private Cursor mCursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_list, container, false);


		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		newsAdapter = new NewsAdapter(getContext(), null, new NewsAdapter.Callback() {
			@Override
			public void onClick(int pos) {
				mCursor.moveToPosition(pos);
				String url = mCursor.getString(mCursor.getColumnIndex(DataContract.NewsItems.COLUMN_URL));
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		newsRecyclerView.setAdapter(newsAdapter);

		getLoaderManager().initLoader(NEWS_LOADER, null, this);


	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		return new CursorLoader(getContext(), DataContract.NewsItems.CONTENT_URI, DataContract.NewsItems.PROJECTIONS,
				null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		newsAdapter.swapCursor(data);
		mCursor = data;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
