package com.dilpreet2028.devents.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dilpreet2028.devents.R;
import com.dilpreet2028.devents.Utils.Utility;
import com.dilpreet2028.devents.data.DataContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 17/1/17.
 */

public class EventsAdapter extends CursorRecyclerAdapter<EventsAdapter.EventsHolder> {

	private Context context;
	private OnItemClickListener onItemClickListener;

	public EventsAdapter(Context context, Cursor cursor, OnItemClickListener onItemClickListener) {
		super(context, cursor);
		this.context = context;
		this.onItemClickListener = onItemClickListener;

	}

	@Override
	public EventsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.events_item, parent, false);
		return new EventsHolder(itemView);
	}

	@Override
	public void onBindViewHolder(EventsHolder viewHolder, Cursor cursor) {
		String title = cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_NAME));
		String going = cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_GOING));
		String url = cursor.getString(cursor.getColumnIndex(DataContract.EventsItem.COLUMN_PIC));

		viewHolder.titleView.setText(title);
		viewHolder.goingView.setText(context.getString(R.string.going, going));

		viewHolder.titleView.setContentDescription(title);
		viewHolder.goingView.setText(context.getString(R.string.going, going));

		Glide.with(context)
				.load(url)
				.into(viewHolder.imageView);

	}

	public class EventsHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.events_item_title)
		TextView titleView;
		@BindView(R.id.events_item_going)
		TextView goingView;
		@BindView(R.id.events_item_image)
		ImageView imageView;

		public EventsHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Utility.logger(getAdapterPosition() + "  pos");
					onItemClickListener.onClick(getAdapterPosition());
				}
			});
		}
	}

	public interface OnItemClickListener {
		public void onClick(int pos);
	}
}
