package com.yawen.Fetcher;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.yawen.Url.CrawlUrl;
import com.yawen.parser.ParseData;



public class Page {
	/**
     * The URL of this page.
     */
    protected CrawlUrl url;

    /**
     * Redirection flag
     */
    protected boolean redirect;

    /**
     * The URL to which this page will be redirected to
     */
    protected String redirectedToUrl;

    /**
     * Status of the page
     */
    protected int statusCode;

    /**
     * The content of this page in binary format.
     */
    protected byte[] contentData;

    /**
     * The ContentType of this page.
     * For example: "text/html; charset=UTF-8"
     */
    protected String contentType;

    /**
     * The encoding of the content.
     * For example: "gzip"
     */
    protected String contentEncoding;

    /**
     * The charset of the content.
     * For example: "UTF-8"
     */
    protected String contentCharset;

    /**
     * Language of the Content.
     */
    private String language;

    /**
     * Headers which were present in the response of the fetch request
     */
    protected Header[] fetchResponseHeaders;

    /**
     * The parsed data populated by parsers
     */
    protected ParseData parseData;
    
    protected String EntityToContext="";
    public String getEntityToContext() {
		return EntityToContext;
	}
	public void setEntityToContext(String entityToContext) {
		EntityToContext = entityToContext;
	}
	public Page(CrawlUrl crawlUrl)
	{
		this.url=crawlUrl;
	}
	public void load(HttpEntity entity)  {

        contentType = null;
        Header type = entity.getContentType();
        if (type != null) {
            contentType = type.getValue();
        }

        contentEncoding = null;
        Header encoding = entity.getContentEncoding();
        if (encoding != null) {
            contentEncoding = encoding.getValue();
        }

        Charset charset = ContentType.getOrDefault(entity).getCharset();
        if (charset != null) {
            contentCharset = charset.displayName();
        }

//        try {
//			contentData = EntityUtils.toByteArray(entity);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("转化为二进制数组出错");
//		}
        try {
			EntityToContext = EntityUtils.toString(entity,"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("转化为字符串出错");
		}
    }
	public CrawlUrl getUrl() {
		return url;
	}

	public void setUrl(CrawlUrl url) {
		this.url = url;
	}

	public boolean isRedirect() {
		return redirect;
	}

	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}

	public String getRedirectedToUrl() {
		return redirectedToUrl;
	}

	public void setRedirectedToUrl(String redirectedToUrl) {
		this.redirectedToUrl = redirectedToUrl;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public byte[] getContentData() {
		return contentData;
	}

	public void setContentData(byte[] contentData) {
		this.contentData = contentData;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Header[] getFetchResponseHeaders() {
		return fetchResponseHeaders;
	}

	public void setFetchResponseHeaders(Header[] fetchResponseHeaders) {
		this.fetchResponseHeaders = fetchResponseHeaders;
	}

	public ParseData getParseData() {
		return parseData;
	}

	public void setParseData(ParseData parseData) {
		this.parseData = parseData;
	}
    
}
