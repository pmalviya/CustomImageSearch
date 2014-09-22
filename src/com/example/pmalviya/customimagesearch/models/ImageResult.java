package com.example.pmalviya.customimagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private String fullUrl;
	private String tbUrl;
	private int width;
	private int height;
	private String title;

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public String getTbUrl() {
		return tbUrl;
	}

	public void setTbUrl(String tbUrl) {
		this.tbUrl = tbUrl;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.tbUrl = json.getString("tbUrl");
			this.title = json.getString("title");
			this.width = Integer.parseInt(json.getString("width"));
			this.height = Integer.parseInt(json.getString("height"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// parser that results arraylist with ImageResult objects
	public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < array.length(); i++) {
			try {
				results.add(new ImageResult(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

}
