package com.example.pmalviya.customimagesearch.models;

public class SearchFilter {
	private String size;
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Integer getSizeIndex() {
		return sizeIndex;
	}
	public void setSizeIndex(Integer sizeIndex) {
		this.sizeIndex = sizeIndex;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getColorIndex() {
		return colorIndex;
	}
	public void setColorIndex(Integer colorIndex) {
		this.colorIndex = colorIndex;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getTypeIndex() {
		return typeIndex;
	}
	public void setTypeIndex(Integer typeIndex) {
		this.typeIndex = typeIndex;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	private Integer sizeIndex;
	private String color;
	private Integer colorIndex;
	private String type;
	private Integer typeIndex;
	private String site;
	
	public SearchFilter(){
		this.colorIndex =0;
		this.sizeIndex = 0;
		this.typeIndex = 0;
		this.site ="";
		this.color="";
		this.size="";
		this.type="";
	}
	
}
