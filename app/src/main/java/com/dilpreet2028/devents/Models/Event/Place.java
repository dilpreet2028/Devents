package com.dilpreet2028.devents.Models.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 15/1/17.
 */

public class Place {
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("location")
	@Expose
	private Location location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
