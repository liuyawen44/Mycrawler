package com.yawen.parser;

import java.util.Set;

import com.yawen.Url.CrawlUrl;
import com.yawen.Util.Util;

public class HtmlParseData implements ParseData{
	public Set<CrawlUrl> outgoingUrls;
	public String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String text;
	public Set<CrawlUrl> getOutgoingUrls() {
		// TODO Auto-generated method stub
		return outgoingUrls;
	}

	public void setOutgoingUrls(Set<CrawlUrl> outgoingUrls) {
		// TODO Auto-generated method stub
		this.outgoingUrls=outgoingUrls;
	}
	
	/**
	 * @param contentType
	 * @return
	 * 
	 * 
	 */
	public boolean isTextParseData(String contentType)
	{
		return Util.hasPlainTextContent(contentType);
	}
	
}
