package com.dilpreet2028.devents.ui.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dilpreet2028.devents.Adapters.EventsAdapter;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.data.DataContract;
import com.dilpreet2028.devents.ui.Activities.EventInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


	@BindView(R.id.events_recycler_view)
	RecyclerView recyclerView;

	private EventsAdapter eventsAdapter;
	private final int EVENT_LOADER=1290;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		View view=inflater.inflate(R.layout.fragment_events_list, container, false);
		ButterKnife.bind(this,view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		eventsAdapter=new EventsAdapter(getContext(), null, new EventsAdapter.OnItemClickListener() {
			@Override
			public void onClick(String eventId) {
				Intent intent=new Intent(getContext(), EventInfoActivity.class);
				intent.putExtra(getString(R.string.event_id),eventId);
				getContext().startActivity(intent);
			}
		});

		recyclerView.setAdapter(eventsAdapter);

		getLoaderManager().initLoader(EVENT_LOADER,null,this);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getContext(), DataContract.EventsItem.CONTENT_URI,
								DataContract.EventsItem.PROJECTIONS,null,null,null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		DatabaseUtils.dumpCursor(data);
		eventsAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
