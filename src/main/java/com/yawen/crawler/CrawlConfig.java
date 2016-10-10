
package com.yawen.crawler;



public class CrawlConfig {
	 //网页默认编码方式
	  private String charsetName = "iso-8859-1";
	//连接超时时间
	  private   int connectTimeout = 5000;
	//默认最大访问次数
	  private   int maxConnectTimes = 3;
	//深度
	  private   int MaxDepth=3;
	
	  //连接读取时间
	  private   int readTimeout = 3500;
	  //Socket超时时间
	  private   int socketTimeout=5000;
	//是否下载二进制文件
	  private boolean isDownloadBinaryData=false;
	  //每个页面发现最大连接数
	  private int MaxOutgoingUrls=100;
	  //空队列时是否关闭
	  private boolean isShutDownWhenEmpty=false;
	  //最多爬取的页面
	  private int MaxPageToFetch;
	public int getMaxPageToFetch() {
		return MaxPageToFetch;
	}
	public void setMaxPageToFetch(int maxPageToFetch) {
		MaxPageToFetch = maxPageToFetch;
	}
	public boolean isShutDownWhenEmpty() {
		return isShutDownWhenEmpty;
	}
	public void setShutDownWhenEmpty(boolean isShutDownWhenEmpty) {
		this.isShutDownWhenEmpty = isShutDownWhenEmpty;
	}
	public int getMaxOutgoingUrls() {
		return MaxOutgoingUrls;
	}
	public void setMaxOutgoingUrls(int maxOutgoingUrls) {
		MaxOutgoingUrls = maxOutgoingUrls;
	}
	public String getCharsetName() {
		return charsetName;
	}
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getMaxConnectTimes() {
		return maxConnectTimes;
	}
	public void setMaxConnectTimes(int maxConnectTimes) {
		this.maxConnectTimes = maxConnectTimes;
	}
	public int getMaxDepth() {
		return MaxDepth;
	}
	public void setMaxDepth(int maxDepth) {
		MaxDepth = maxDepth;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public boolean isDownloadBinaryData() {
		return isDownloadBinaryData;
	}
	public void setDownloadBinaryData(boolean isDownloadBinaryData) {
		this.isDownloadBinaryData = isDownloadBinaryData;
	}
	public Boolean getIsIncludeHttpsPages() {
		return isIncludeHttpsPages;
	}
	public void setIsIncludeHttpsPages(Boolean isIncludeHttpsPages) {
		this.isIncludeHttpsPages = isIncludeHttpsPages;
	}
	public String getPageSourceCode() {
		return pageSourceCode;
	}
	public void setPageSourceCode(String pageSourceCode) {
		this.pageSourceCode = pageSourceCode;
	}
	//是否包含https
	  private Boolean isIncludeHttpsPages=true;
	//链接源代码
	  private String pageSourceCode = "";
	 
	
	 
}
