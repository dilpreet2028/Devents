package com.dilpreet2028.devents.ui.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.dilpreet2028.devents.Adapters.EventsAdapter;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Services.CreateEventService;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;
import com.dilpreet2028.devents.ui.Activities.EventInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


	@BindView(R.id.events_recycler_view)
	RecyclerView recyclerView;
	@BindView(R.id.fab)
	FloatingActionButton fab;

	private EventsAdapter eventsAdapter;
	private final int EVENT_LOADER = 1290;
	private Cursor mCursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {


		View view = inflater.inflate(R.layout.fragment_events_list, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mCursor = null;
		eventsAdapter = new EventsAdapter(getContext(), null, new EventsAdapter.OnItemClickListener() {
			@Override
			public void onClick(int pos) {
				Intent intent = new Intent(getContext(), EventInfoActivity.class);
				mCursor.moveToPosition(pos);
				String eventId = mCursor.getString(mCursor.getColumnIndex(DataContract.EventsItem.COLUMN_E_ID));
				intent.putExtra(getString(R.string.event_id), eventId);
				getContext().startActivity(intent);
			}
		});

		recyclerView.setAdapter(eventsAdapter);

		getLoaderManager().initLoader(EVENT_LOADER, null, this);


		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setupDialog();
			}
		});

	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getContext(), DataContract.EventsItem.CONTENT_URI,
				DataContract.EventsItem.PROJECTIONS, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		DatabaseUtils.dumpCursor(data);
		eventsAdapter.swapCursor(data);
		mCursor = data;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	private void setupDialog() {
		Dialog dialog = new Dialog(getContext());
		dialog.setContentView(R.layout.new_event_dialog);
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.show();

		final EditText editText = (EditText) dialog.findViewById(R.id.dialog_edittext);
		Button btn = (Button) dialog.findViewById(R.id.dialog_button);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String link = editText.getText().toString();

				if (Utility.containsFBUrl(link)) {
					String eventId = Utility.getEventId(link);

					Intent intent = new Intent(getContext(), CreateEventService.class);
					intent.putExtra(getString(R.string.event), eventId);
					getContext().startService(intent);
				}
			}
		});
	}
}
