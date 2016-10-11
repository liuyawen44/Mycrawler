package com.yawen.MyCrawler;

import java.util.Iterator;
import java.util.Set;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yawen.Fetcher.Page;
import com.yawen.Url.CrawlUrl;
import com.yawen.crawler.Crawler;
import com.yawen.parser.HtmlParseData;



public class MyCrawler extends Crawler{
	public static int i=0;
	@Override
	public boolean shouldVisit(Page referringPage, CrawlUrl url) {
	      

		if(url.getUrl().contains("qqzssl.com"))
		{
			if("联系方式".equals(url.getAnchor()))
			{
				return true;
			}
		}
			
	       
			
			
		return false;
	    }

	    /**
	     * This function is called when a page is fetched and ready to be processed
	     * by your program.
	     */
	@Override
	    public void visit(Page page) {
			
		Document document=Jsoup.parse(page.getEntityToContext());
		System.out.println(document.select("title").first().text());
		Elements elements=document.getElementsByClass("contactus").select("li");
        if(elements!=null)
		for(Element element:elements)
        {
      	 System.out.println(element.text());
        }
	            
	           
	            	
	           
	        }
	
	    }

