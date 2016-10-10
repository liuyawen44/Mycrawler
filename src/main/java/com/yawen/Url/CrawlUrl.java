package com.yawen.Url;

import java.io.Serializable;



public class CrawlUrl implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1439548464186650551L;
	private String anchor;
	

	private Double cash=0.0;
	private int depth;
	private String parenturl;
	private String tag;
	private String url;
	public CrawlUrl()
	{
		
	}
	public String getAnchor() {
		return anchor;
	}
	public Double getCash() {
		return cash;
	}
	public int getDepth() {
		return depth;
	}
	public String getParenturl() {
		return parenturl;
	}
	
	public String getTag() {
		return tag;
	}
	public String getUrl() {
		return url;
	}
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	public void setCash(Double cash) {
		this.cash = cash;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void setParenturl(String parentUrl) {
		this.parenturl = parentUrl;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
}
