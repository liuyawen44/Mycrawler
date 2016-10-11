package com.yawen.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yawen.Fetcher.FetchResult;
import com.yawen.Fetcher.Page;
import com.yawen.Fetcher.PageFetcher;
import com.yawen.Frontier.Frontier;
import com.yawen.Frontier.MongodbDAO;
import com.yawen.Url.CrawlUrl;

import com.yawen.Util.MD5Util;

import com.yawen.parser.BinaryParseData;
import com.yawen.parser.ParseData;
import com.yawen.parser.Parser;

public class Crawler implements Runnable {
	protected static final Logger logger = LoggerFactory.getLogger(Crawler.class);
	protected Frontier frontier;
	protected int id;
	protected boolean isWaitingForNewURLs;
	protected CrawlController myController;
	protected PageFetcher pageFetcher;
	protected Parser parser;
	protected Thread myThread;
	public Thread getMyThread() {
		return myThread;
	}

	public void setMyThread(Thread myThread) {
		this.myThread = myThread;
	}

	public Frontier getFrontier() {
		return frontier;
	}

	public int getId() {
		return id;
	}
	public boolean isNotWaitingForNewURLs() {
        return !isWaitingForNewURLs;
    }
	public CrawlController getMyController() {
		return myController;
	}

	public PageFetcher getPageFetcher() {
		return pageFetcher;
	}

	public Parser getParser() {
		return parser;
	}

	public void init(Integer id, CrawlController controller) {
		this.id = id;
		myController = controller;
		frontier = myController.getFrontier();
	
		parser = new Parser(myController.getConfig());
		pageFetcher = myController.getPageFetcher();
		isWaitingForNewURLs=false;

	}

	public boolean isWaitingForNewURLs() {
		return isWaitingForNewURLs;
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			List<CrawlUrl> assignedURLs = new ArrayList<CrawlUrl>(50);
			isWaitingForNewURLs = true;
			frontier.getURL(50, assignedURLs);
			isWaitingForNewURLs = false;
			if (assignedURLs.isEmpty()) {
				if (frontier.isFinished()) {
					return;
				}
				try {
					// 队列为空等待3秒
					Thread.sleep(3000);
				} catch (Exception e) {
					System.out.println("出现问题");
				}
			} else {
				for (CrawlUrl curURL : assignedURLs) {
					if (myController.isShuttingDown()) {

						return;
					}
					if (curURL != null) {

						processPage(curURL);
						this.getMyController().bloomfiter.add(curURL.getUrl());
					}
				}
			}
		}

	}

	public void processPage(CrawlUrl crawlUrl) {
		FetchResult fetchResult = null;
		try {
			if (crawlUrl == null)
				return;
			fetchResult = pageFetcher.FetchPage(crawlUrl);
			int statusCode = fetchResult.getStatusCode();
			Page page = new Page(crawlUrl);
			if (fetchResult.getEntity() != null)
				page.load(fetchResult.getEntity());
			page.setFetchResponseHeaders(fetchResult.getResponseHeaders());
			page.setStatusCode(statusCode);
			if (statusCode < 200 || statusCode > 299) { // Not 2XX: 2XX status
														// codes indicate
														// success
				if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY
						|| statusCode == HttpStatus.SC_MULTIPLE_CHOICES || statusCode == HttpStatus.SC_SEE_OTHER
						|| statusCode == HttpStatus.SC_TEMPORARY_REDIRECT || statusCode == 308) {
					page.setRedirect(true);
					String moveToUrl = fetchResult.getMovedToUrl();
					if (this.getMyController().bloomfiter.contains(moveToUrl)) {
						logger.debug("此URL爬虫已浏览过");
						return;
					} 
					page.setRedirectedToUrl(moveToUrl);
					CrawlUrl webURL = new CrawlUrl();
					webURL.setUrl(moveToUrl);

					webURL.setParenturl(crawlUrl.getParenturl());
					webURL.setDepth(crawlUrl.getDepth());
					webURL.setTag(crawlUrl.getTag());
					webURL.setAnchor(crawlUrl.getAnchor());
					webURL.setCash(crawlUrl.getCash());
					if (shouldVisit(page, webURL)) {
						Map<String, Object> map = new HashMap<String, Object>();
						String md5URL = MD5Util.getMd5(webURL.getUrl());
						String primarykey = webURL.getCash() + md5URL;
						map.put("_id", primarykey);
						map.put("entity", webURL);
						frontier.schedule(map);
					} else
						logger.debug("don't need to visit" + webURL.getUrl());
					;
				} else {// 不是200也不是3XX
				}
			} else {// 200
				if(this.getMyController().bloomfiter.contains(crawlUrl.getUrl()))
				{
					logger.debug("It has been visited:"+crawlUrl.getUrl());
					return ;
					
				}
				parser.parse(page, crawlUrl.getUrl());
				ParseData parseData=page.getParseData();
				if(parseData==null)
					return;
				 if (parseData instanceof BinaryParseData) {
					//是否下载
					 
					return ;
				}
				 List<Map<String, Object>> toSchedule=new ArrayList<Map<String,Object>>();
				 int maxDepth=myController.getConfig().getMaxDepth();
				 for(CrawlUrl url:parseData.getOutgoingUrls())
				 {
					 url.setParenturl(crawlUrl.getUrl());
					 url.setDepth(crawlUrl.getDepth()+1);
					 Double cash=crawlUrl.getCash();
					
					 cash=cash/parseData.getOutgoingUrls().size();
					 url.setCash(url.getCash()+cash);
					 if(url.getDepth()<=maxDepth)
						 //从当前页面中发现的页面如果需要抓取
					 if(shouldVisit(page, url))
					 {
						
					
							
						 Map<String, Object> map=new HashMap<String, Object>();
						 String PrimaryKey=url.getCash()+MD5Util.getMd5(url.getUrl());
						 
						 map.put("_id", PrimaryKey);
						 map.put("entity", url);
						 toSchedule.add(map);
					 }
					 else
					 {
						 logger.debug("not need to visit it :"+url.getUrl());
					 }
					 
					 
				 }
				 crawlUrl.setCash(0.0);
				 frontier.scheduleAll(toSchedule);
				 visit(page);
				 
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.debug(e.getMessage());
		}finally {
			if(fetchResult!=null)
			{
				fetchResult.discardContentIfNotConsumed();
			}
		}
	}
	public void visit(Page page)
	{
		
	}

	

	public void setFrontier(Frontier frontier) {
		this.frontier = frontier;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMyController(CrawlController myController) {
		this.myController = myController;
	}

	public void setPageFetcher(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public void setWaitingForNewURLs(boolean isWaitingForNewURLs) {
		this.isWaitingForNewURLs = isWaitingForNewURLs;
	}

	
	public boolean shouldVisit(Page page, CrawlUrl url) {
		return true;
	}
	

}
