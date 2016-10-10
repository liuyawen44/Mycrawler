package com.yawen.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yawen.Fetcher.PageFetcher;
import com.yawen.Frontier.BloomFitler;
import com.yawen.Frontier.Frontier;
import com.yawen.Url.CrawlUrl;

import com.yawen.Util.MD5Util;

import com.yawen.parser.Parser;



public class CrawlController extends Configurable {
	private final static Logger logger = LoggerFactory.getLogger(CrawlController.class);
	protected boolean finished;
	protected Frontier frontier;
	protected PageFetcher pageFetcher;
	protected Parser parser;
	protected boolean shuttingDown;
	protected final static Object waitinglock=new Object();
	protected List<Object> crawlersLocalData = new ArrayList<Object>();
	protected BloomFitler bloomfiter;
	public CrawlController(CrawlConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
		this.pageFetcher = new PageFetcher(config);
		this.parser = new Parser(config);
		frontier = new Frontier(config);
		finished = false;
		shuttingDown = false;
		bloomfiter=new BloomFitler();
	

	}

	public List<Object> getCrawlersLocalData() {
		return crawlersLocalData;
	}

	public void setCrawlersLocalData(List<Object> crawlersLocalData) {
		this.crawlersLocalData = crawlersLocalData;
	}

	public interface WebCrawlerFactory<T extends Crawler> {
		T newInstance() throws Exception;
	}

	private static class DefaultWebCrawlerFactory<T extends Crawler> implements WebCrawlerFactory<T> {
		final Class<T> clazz;

		DefaultWebCrawlerFactory(Class<T> clazz) {
			this.clazz = clazz;
		}

		public T newInstance() throws Exception {
			try {
				return clazz.newInstance();
			} catch (ReflectiveOperationException e) {
				throw e;
			}
		}

	}

	/**
	 * Start the crawling session and wait for it to finish. This method
	 * utilizes default crawler factory that creates new crawler using Java
	 * reflection
	 *
	 * @param clazz
	 *            the class that implements the logic for crawler threads
	 * @param numberOfCrawlers
	 *            the number of concurrent threads that will be contributing in
	 *            this crawling session.
	 * @param <T>
	 *            Your class extending WebCrawler
	 */
	public <T extends Crawler> void start(Class<T> clazz, int numberOfCrawlers) {
		this.start(new DefaultWebCrawlerFactory<T>(clazz), numberOfCrawlers, true);
	}

	/**
	 * Start the crawling session and wait for it to finish.
	 *
	 * @param crawlerFactory
	 *            factory to create crawlers on demand for each thread
	 * @param numberOfCrawlers
	 *            the number of concurrent threads that will be contributing in
	 *            this crawling session.
	 * @param <T>
	 *            Your class extending WebCrawler
	 */
	public <T extends Crawler> void start(WebCrawlerFactory<T> crawlerFactory, int numberOfCrawlers) {
		this.start(crawlerFactory, numberOfCrawlers, true);
	}

	/**
	 * Start the crawling session and return immediately.
	 *
	 * @param crawlerFactory
	 *            factory to create crawlers on demand for each thread
	 * @param numberOfCrawlers
	 *            the number of concurrent threads that will be contributing in
	 *            this crawling session.
	 * @param <T>
	 *            Your class extending WebCrawler
	 */
	public <T extends Crawler> void startNonBlocking(WebCrawlerFactory<T> crawlerFactory, final int numberOfCrawlers) {
		this.start(crawlerFactory, numberOfCrawlers, true);
	}

	/**
	 * @param crawlerFactory
	 * @param numberOfCrawlers 线程数量
	 * @param isBlocking
	 */
	protected <T extends Crawler> void start(final WebCrawlerFactory<T> crawlerFactory, final int numberOfCrawlers,
			boolean isBlocking) {
			try {
				finished=false;
				final List<Thread> threads = new ArrayList<Thread>();
	            final List<T> crawlers = new ArrayList<T>();
				for(int i=1;i<=numberOfCrawlers;i++)
				{
					T crawler=crawlerFactory.newInstance();
					Thread thread=new Thread(crawler,"Crawler " + i);
					crawler.setMyThread(thread);
					crawler.init(i, this);
					thread.start();
					crawlers.add(crawler);
	                threads.add(thread);
	                logger.info("Crawler {} started", i);
				}
				final CrawlController controller = this;
					Thread monitorThread=new Thread(new Runnable() {
						public void run() {
							try {
								synchronized (waitinglock) {
									while(true)
									{
										
											Thread.sleep(1000);
											 boolean someoneIsWorking = false;
											for(int i=0;i<threads.size();i++)
											{
												  Thread thread = threads.get(i);
												if(!thread.isAlive())
												{
													if(!shuttingDown)
													{
														logger.info("Thread {} was dead, I'll recreate it", i);
														 T crawler = crawlerFactory.newInstance();
				                                            thread = new Thread(crawler, "Crawler " + (i + 1));
				                                            threads.remove(i);
				                                            threads.add(i, thread);
				                                            crawler.setMyThread(thread);
				                                            crawler.init(i + 1, controller);
				                                            thread.start();
				                                            crawlers.remove(i);
				                                            crawlers.add(i, crawler);
													}
												}else if(crawlers.get(i).isNotWaitingForNewURLs())
													{
														someoneIsWorking=true;
													}
											}
													if(!someoneIsWorking)
													{
														 logger.info(
							                                        "It looks like no thread is working, waiting for 10 " +
							                                        "seconds to make sure...");
														 Thread.sleep(10000);
														   someoneIsWorking = false;
						                                    for (int i = 0; i < threads.size(); i++) {
						                                        Thread thread = threads.get(i);
						                                        if (thread.isAlive() &&
						                                            crawlers.get(i).isNotWaitingForNewURLs()) {
						                                            someoneIsWorking = true;
						                                        }
						                                    }
													}
//													boolean shutOnEmpty = config.isShutDownWhenEmpty();
													if (!someoneIsWorking) {
				                                        if (!shuttingDown) {
				                                            long dbcount = frontier.getDBcount();
				                                            if (dbcount > 0) {
				                                                continue;
				                                            }
				                                            logger.info(
				                                                "No thread is working and no more URLs are in " +
				                                                "queue waiting for another 10 seconds to make " +
				                                                "sure...");
				                                            Thread.sleep(10000);
				                                            dbcount = frontier.getDBcount();
				                                            if (dbcount > 0) {
				                                                continue;
				                                            }
				                                            logger.info(
				                                                    "All of the crawlers are stopped. Finishing the " +
				                                                    "process...");
				                                            frontier.finish();
				                                            logger.info(
				                                                    "Waiting for 10 seconds before final clean up...");
				                                            pageFetcher.shutDown();
				                                            finished = true;
				                                            waitinglock.notifyAll();
				                                            return ;
				                                            
				                                        }
												
													}
									
										
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								logger.debug(e.getMessage());
							}
				
						}
					});
				monitorThread.start();
				if (isBlocking) {
	                waitUntilFinish();
	            }
			} catch (Exception e) {
				// TODO: handle exception
			}
	}
	 /**
     * Wait until this crawling session finishes.
     */
    public void waitUntilFinish() {
        while (!finished) {
            synchronized (waitinglock) {
                if (finished) {
                    return;
                }
                try {
                    waitinglock.wait();
                } catch (InterruptedException e) {
                    logger.error("Error occurred", e);
                }
            }
        }
    }
    public void addSeed(String pageUrl) {
        

            CrawlUrl webUrl = new CrawlUrl();
            webUrl.setUrl(pageUrl);
            webUrl.setParenturl("123");
            webUrl.setDepth(0);
            webUrl.setCash(getConfig().getMaxPageToFetch()/1.0);
            webUrl.setTag("");
            webUrl.setAnchor("");
            String primarykey=webUrl.getCash()+MD5Util.getMd5(webUrl.getUrl());
            Map<String, Object>map=new HashMap<String, Object>();
            map.put("_id", primarykey);
            map.put("entity",webUrl);
            frontier.schedule(map);
         
          
           
        
    }
	public Frontier getFrontier() {
		return frontier;
	}

	public PageFetcher getPageFetcher() {
		return pageFetcher;
	}

	public Parser getParser() {
		return parser;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isShuttingDown() {
		return shuttingDown;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setFrontier(Frontier frontier) {
		this.frontier = frontier;
	}

	public void setPageFetcher(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public void setShuttingDown(boolean shuttingDown) {
		this.shuttingDown = shuttingDown;
	}
	 public void shutdown() {
	        logger.info("Shutting down...");
	        this.shuttingDown = true;
	        pageFetcher.shutDown();
	        frontier.finish();
	    }
}
