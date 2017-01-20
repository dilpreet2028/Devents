package com.dilpreet2028.devents.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 20/1/17.
 */

public class EventListModel {
	@SerializedName("event_id")
	@Expose
	private String eventId;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
}
