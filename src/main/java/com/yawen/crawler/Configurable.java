package com.yawen.crawler;

import com.yawen.Util.LogProperty;

public abstract class Configurable {
	static{
		LogProperty logProperty=new LogProperty();
	}
	protected CrawlConfig config;
	public Configurable(CrawlConfig config)
	{
		this.config=config;
	}
	public CrawlConfig getConfig()
	{
		return config;
	}
}
