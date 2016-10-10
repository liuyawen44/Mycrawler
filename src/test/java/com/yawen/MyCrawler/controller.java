package com.yawen.MyCrawler;

import com.yawen.crawler.CrawlConfig;
import com.yawen.crawler.CrawlController;

public class controller {

	public static void main(String[] args) {
		// TODO Auto-g	enerated method stub
		CrawlConfig config=new CrawlConfig();
		config.setDownloadBinaryData(false);
		config.setIsIncludeHttpsPages(true);
		config.setMaxDepth(2);
		config.setMaxPageToFetch(500);
		config.setMaxOutgoingUrls(400);
		CrawlController controller=new CrawlController(config);
		controller.addSeed("http://www.open-open.com/jsoup/");
		controller.addSeed("https://yq.aliyun.com/articles/6807");
		int number=5;
		controller.start(MyCrawler.class, number);

	}

}
