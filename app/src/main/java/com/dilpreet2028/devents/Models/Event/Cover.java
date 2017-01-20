package com.dilpreet2028.devents.Models.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 15/1/17.
 */

public class Cover {
	@SerializedName("source")
	@Expose
	private String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
