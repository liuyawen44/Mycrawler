package com.yawen.parser;

import java.util.Set;

import com.yawen.Url.CrawlUrl;

public class TextParseData  implements ParseData{
	public Set<CrawlUrl> outgoingUrls;
	public String textcontent;
	public String getTextcontent() {
		return textcontent;
	}

	public void setTextcontent(String textcontent) {
		this.textcontent = textcontent;
	}

	public Set<CrawlUrl> getOutgoingUrls() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOutgoingUrls(Set<CrawlUrl> outgoingUrls) {
		// TODO Auto-generated method stub
		this.outgoingUrls=outgoingUrls;
	}

}
