package com.dilpreet2028.devents.Models.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 15/1/17.
 */

public class Event {
	@SerializedName("cover")
	@Expose
	private Cover cover;
	@SerializedName("interested_count")
	@Expose
	private Integer interestedCount;
	@SerializedName("attending_count")
	@Expose
	private Integer attendingCount;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("place")
	@Expose
	private Place place;
	@SerializedName("id")
	@Expose
	private String id;

	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}

	public Integer getInterestedCount() {
		return interestedCount;
	}

	public void setInterestedCount(Integer interestedCount) {
		this.interestedCount = interestedCount;
	}

	public Integer getAttendingCount() {
		return attendingCount;
	}

	public void setAttendingCount(Integer attendingCount) {
		this.attendingCount = attendingCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

