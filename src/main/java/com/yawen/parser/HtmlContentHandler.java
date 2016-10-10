package com.yawen.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlContentHandler {
		
	private final List<ExtractedUrlAnchorPair> outgoingUrls;
	
	public HtmlContentHandler(String context)
	{
		outgoingUrls=new ArrayList<ExtractedUrlAnchorPair>();
		parseHtml(context);
	}
	
	public List<ExtractedUrlAnchorPair> getOutgoingUrls() {
		return outgoingUrls;
	}
	
	public String getTitle(String context)
	{
		Document document=Jsoup.parse(context);
		Element element=document.select("title").first();
		if(element!=null)
		return element.text();
		return "";
	}

	public void parseHtml(String context)
	{	Elements elements;
		Document document=Jsoup.parse(context);
		
		//a href形式的uri
		elements=document.select("a[href]");
		for(Element element:elements)
		{	
			
			if(element.attr("abs:href")!="")
			{	String tagName=element.tagName();
				String anchor=element.text();
				String href=element.attr("abs:href");
				addToOutgoingUrls(href, tagName, anchor);
			}
		}
		//img iframe frame embed src
		elements=document.select("[src]");
		for(Element element:elements)
		{		String tag=element.tagName();
			if(tag.equals("img")||tag.equals("embed")||tag.equals("iframe")
					||tag.equals("frame"))
			if(element.attr("abs:src")!="")
			{	String tagName=element.tagName();
				String anchor=element.text();
				String href=element.attr("abs:src");
				addToOutgoingUrls(href, tagName, anchor);
			}
		}
		elements =document.select("meta");
		for(Element element:elements)
		{		
				if("refresh".equals(element.attr("http-equiv")))
				{
					String content=element.attr("content");
					int pos = content.toLowerCase().indexOf("url=");
                    if (pos != -1) {
                    	String tagName=element.tagName();
						String anchor=element.text();
						String href=content.substring(pos + 4);;
						addToOutgoingUrls(href, tagName, anchor);
                       
                    }
					
						
					
				}
			
		}
		
	}
	public void addToOutgoingUrls(String href,String tagName,String anchor)
	{
		ExtractedUrlAnchorPair extractedUrlAnchorPair=new ExtractedUrlAnchorPair();
		extractedUrlAnchorPair.setAnchor(anchor);
		extractedUrlAnchorPair.setHref(href);
		extractedUrlAnchorPair.setTag(tagName);
		outgoingUrls.add(extractedUrlAnchorPair);
	}
}
