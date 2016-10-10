package com.yawen.Fetcher;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class FetchResult {
	
	protected HttpEntity entity = null;
    protected String fetchedUrl = null;
	protected String movedToUrl = null;
	protected Header[] responseHeaders = null;
	protected int statusCode;
	public HttpEntity getEntity() {
		return entity;
	}
	public String getFetchedUrl() {
		return fetchedUrl;
	}
	public String getMovedToUrl() {
		return movedToUrl;
	}
	public Header[] getResponseHeaders() {
		return responseHeaders;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
	public void setFetchedUrl(String fetchedUrl) {
		this.fetchedUrl = fetchedUrl;
	}
    public void setMovedToUrl(String movedToUrl) {
		this.movedToUrl = movedToUrl;
	}
    public void setResponseHeaders(Header[] responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
    public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
    public void discardContentIfNotConsumed() {
        try {
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        } catch (Exception ignored) {
            // We can EOFException (extends IOException) exception. It can happen on compressed
            // streams which are not
            // repeatable
           System.out.println(ignored.getMessage());
        	// We can ignore this exception. It can happen if the stream is closed.
        } 
    }
}
