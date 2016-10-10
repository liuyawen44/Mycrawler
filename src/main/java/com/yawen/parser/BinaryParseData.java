package com.yawen.parser;

import java.util.Set;

import com.yawen.Util.*;
import com.yawen.Url.CrawlUrl;

public class BinaryParseData  implements ParseData{
	public Set<CrawlUrl> outgoingUrls;
	public Set<CrawlUrl> getOutgoingUrls() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOutgoingUrls(Set<CrawlUrl> outgoingUrls) {
		// TODO Auto-generated method stub
		this.outgoingUrls=outgoingUrls;
	}
	public boolean isBinaryData(String contentType)
	{
		return Util.hasBinaryContent(contentType);
	}
}
