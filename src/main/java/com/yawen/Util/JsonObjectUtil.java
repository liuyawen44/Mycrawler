package com.yawen.Util;

import com.alibaba.fastjson.JSON;
import com.yawen.Url.CrawlUrl;

public class JsonObjectUtil {
	public static CrawlUrl toCrawlUrl(Object object)
	{
		String json=JSON.toJSONString(object);
		CrawlUrl crawlUrl=JSON.parseObject(json,CrawlUrl.class);
		return crawlUrl;
	}
}
