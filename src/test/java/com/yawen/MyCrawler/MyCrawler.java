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
	      

		if(url.getUrl().startsWith("https:")||url.getUrl().startsWith("http:"))
	        
	       
			
			return true;
		return false;
	    }

	    /**
	     * This function is called when a page is fetched and ready to be processed
	     * by your program.
	     */
	@Override
	    public void visit(Page page) {
			
//	        
	     System.out.println(page.getUrl().getAnchor()+" "+page.getUrl().getUrl());
	            
	           
	            	
	           
	        }
	
	    }

