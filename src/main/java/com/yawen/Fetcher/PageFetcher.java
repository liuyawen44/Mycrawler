package com.yawen.Fetcher;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yawen.Url.CrawlUrl;
import com.yawen.crawler.Configurable;
import com.yawen.crawler.CrawlConfig;



public class PageFetcher extends Configurable {
	protected static final Logger logger = LoggerFactory.getLogger(PageFetcher.class);

	protected PoolingHttpClientConnectionManager connectionManager;
	protected CloseableHttpClient httpClient;
	protected final Object mutex = new Object();
	protected long lastFetchTime = 0;
	  protected IdleConnectionMonitorThread connectionMonitorThread = null;
	// protected IdleConnectionMonitorThread connectionMonitorThread = null;
	public PageFetcher(CrawlConfig config) {
		super(config);
		// TODO Auto-generated constructor stub

		// 设置连接参数
		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(false)
				.setCookieSpec(CookieSpecs.STANDARD).setRedirectsEnabled(false).setSocketTimeout(5000)
				.setConnectTimeout(5000).build();
		/*
		 * 当访问url的schema为http时， 调用明文连接套节工厂来建立连接；
		 * 当访问url的schema为https时，调用SSL连接套接字工厂来建立连接
		 */
		RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
		connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);
		if (config.getIsIncludeHttpsPages()) {

			try { // Fixing:
					// https://code.google.com/p/crawler4j/issues/detail?id=174
					// By always trusting the ssl certificate
				SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
					public boolean isTrusted(final X509Certificate[] chain, String authType) {
						return true;
					}
				}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
						SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				connRegistryBuilder.register("https", sslsf);
			} catch (Exception e) {
				logger.warn("Exception thrown while trying to register https");
				
			}

		}
		Registry<ConnectionSocketFactory> connRegistry = connRegistryBuilder.build();
		connectionManager = new PoolingHttpClientConnectionManager(connRegistry);
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setConnectionManager(connectionManager);
		clientBuilder.setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
		// clientBuilder.setDefaultHeaders(config.getDefaultHeaders());
		httpClient = clientBuilder.build();

        if (connectionMonitorThread == null) {
            connectionMonitorThread = new IdleConnectionMonitorThread(connectionManager);
        }
        connectionMonitorThread.start();
	}

	public FetchResult FetchPage(CrawlUrl url) throws ClientProtocolException, IOException,SocketTimeoutException
	{
		FetchResult fetchResult=new FetchResult();
		HttpUriRequest request=null;
		String toFetchURL=url.getUrl();
		
		 request = new HttpGet(toFetchURL);
		 CloseableHttpResponse response;
	
			try {
				response = httpClient.execute(request);
				 fetchResult.setEntity(response.getEntity());
		         fetchResult.setResponseHeaders(response.getAllHeaders());
				 
		         int statusCode = response.getStatusLine().getStatusCode();
		         if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY ||
		                 statusCode == HttpStatus.SC_MOVED_TEMPORARILY ||
		                 statusCode == HttpStatus.SC_MULTIPLE_CHOICES ||
		                 statusCode == HttpStatus.SC_SEE_OTHER ||
		                 statusCode == HttpStatus.SC_TEMPORARY_REDIRECT || statusCode ==
		                                                                   308) 
		         { // todo follow
		               // https://issues.apache.org/jira/browse/HTTPCORE-389

		                 Header header = response.getFirstHeader("Location");
		                 if(header!=null)
		                 {
		                	 String moveToURL=header.getValue();
		                	 fetchResult.setMovedToUrl(moveToURL);
//		                	 System.out.println("MovetoURL:"+moveToURL);
		                 }
		         }
		                 else if (statusCode >= 200 && statusCode <= 299) 
		                 {
							fetchResult.setFetchedUrl(toFetchURL);
							 String uri = request.getURI().toString();
							 if(!uri.equals(toFetchURL))
								 fetchResult.setFetchedUrl(uri);
						}
		                 fetchResult.setStatusCode(statusCode);
		                 return fetchResult;
			}finally { // occurs also with thrown exceptions
	            if ((fetchResult.getEntity() == null) && (request != null)) {
	                request.abort();
	            }
	        }
			
		
		
              
      
         
		
		}
	 public synchronized void shutDown() {
	        if (connectionMonitorThread != null) {
	            connectionManager.shutdown();
	            connectionMonitorThread.shutdown();
	        }
	    }
	
	
	}

