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
import com.dilpreet2028.devents.data.DataContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 7/1/17.
 */

public class NewsAdapter extends CursorRecyclerAdapter<NewsAdapter.NewsViewHolder>{


	private Context context;
	private Callback callback;
	public NewsAdapter(Context context, Cursor cursor,Callback callback) {
		super(context, cursor);
		this.callback=callback;
		this.context=context;
	}

	@Override
	public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView= LayoutInflater.from(parent.getContext())
						.inflate(R.layout.news_item,parent,false);
		return new NewsViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(NewsViewHolder viewHolder, Cursor cursor) {
		String imageUrl=cursor.getString(cursor.getColumnIndex(DataContract.NewsItems.COLUMN_URL_IMG));
		String title=cursor.getString(cursor.getColumnIndex(DataContract.NewsItems.COLUMN_TITLE));

		viewHolder.titleTextView.setText(title);

		Glide.with(context)
				.load(imageUrl)
				.crossFade()
				.into(viewHolder.imageView);


	}

	public class NewsViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.news_item_image) ImageView imageView;
		@BindView(R.id.news_item_title) TextView titleTextView;

		public NewsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this,itemView);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					callback.onClick(getAdapterPosition());
				}
			});
		}
	}

	public interface Callback{
		public void onClick(int pos);
	}
}
