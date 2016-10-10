package com.yawen.parser;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.util.EntityUtils;

import com.yawen.Fetcher.Page;
import com.yawen.Url.CrawlUrl;
import com.yawen.Util.*;
import com.yawen.crawler.Configurable;
import com.yawen.crawler.CrawlConfig;



public class Parser extends Configurable{

	public Parser(CrawlConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
	}
	public void parse(Page page ,String url)
	{	String contentType=page.getContentType();
		if(Util.hasBinaryContent(contentType))
		{
			BinaryParseData binaryParseData=new BinaryParseData();
			if(config.isDownloadBinaryData())
			{
				page.setParseData(binaryParseData);
			}
		}
		else if (Util.hasPlainTextContent(contentType)) {
			TextParseData textParseData=new TextParseData();
			if (page.getContentCharset() == null) {
                textParseData.setTextcontent(new String(page.getContentData()));
            } else {
                try {
					textParseData.setTextcontent(
					    new String(page.getContentData(), page.getContentCharset()));
					textParseData.setOutgoingUrls(Net.extractUrls(textParseData.getTextcontent()));
		               page.setParseData(textParseData);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("textparsedata出错");
				}
               
            }
			
		}else{
			HtmlParseData htmlParseData=new HtmlParseData();
			//记得给Page初始化
			 
			HtmlContentHandler htmlContentHandler=new HtmlContentHandler(page.getEntityToContext());
			htmlParseData.setText(page.getEntityToContext());
			htmlParseData.setTitle(htmlContentHandler.getTitle(page.getEntityToContext()));
			Set<CrawlUrl> outgoingUrls = new HashSet<CrawlUrl>();
			int urlCount=0;
			for(ExtractedUrlAnchorPair extractedUrlAnchorPair:htmlContentHandler.getOutgoingUrls())
			{
				String href=extractedUrlAnchorPair.getHref();
				if(href!=null)
				{
					CrawlUrl crawlUrl=new CrawlUrl();
					crawlUrl.setUrl(href);
					crawlUrl.setAnchor(extractedUrlAnchorPair.getAnchor());
					crawlUrl.setTag(extractedUrlAnchorPair.getTag());
					urlCount++;
					if(urlCount>config.getMaxOutgoingUrls())
						break;
					outgoingUrls.add(crawlUrl);
					
					//这里可以设置最大Outgoing数量，防止数据量过大
				}
				
			}
			htmlParseData.setOutgoingUrls(outgoingUrls);
			page.setParseData(htmlParseData);
		}
		
	}
	
}
