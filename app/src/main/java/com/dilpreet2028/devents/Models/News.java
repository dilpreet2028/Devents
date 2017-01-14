package com.dilpreet2028.devents.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dilpreet on 7/1/17.
 */

public class News {
	@SerializedName("articles")
	@Expose
	private List<Article> articles = null;

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
