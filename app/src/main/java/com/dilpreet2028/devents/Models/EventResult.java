package com.dilpreet2028.devents.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 15/1/17.
 */

public class EventResult {

	@SerializedName("str")
	@Expose
	String str;

	@SerializedName("res")
	@Expose
	int res;

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}
