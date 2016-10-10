package com.yawen.parser;

import java.util.Set;

import com.yawen.Url.CrawlUrl;



public interface ParseData {
	 Set<CrawlUrl> getOutgoingUrls();

	    void setOutgoingUrls(Set<CrawlUrl> outgoingUrls);
}
