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
		config.setMaxPageToFetch(1000);
		config.setMaxOutgoingUrls(400);
		config.setConnectTimeout(20000);
		config.setSocketTimeout(60000);
		CrawlController controller=new CrawlController(config);
		controller.addSeed("http://www.qqzssl.com/list_c9675.html");
//		controller.addSeed("https://yq.aliyun.com/articles/6807");
		int number=3;
		controller.start(MyCrawler.class, number);

	}

}
